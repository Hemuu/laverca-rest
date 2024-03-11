//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import fi.methics.laverca.rest.json.JsonRequest;
import fi.methics.laverca.rest.json.JsonResponse;
import fi.methics.laverca.rest.json.MSS_ProfileReq;
import fi.methics.laverca.rest.json.MSS_SignatureReq;
import fi.methics.laverca.rest.json.MSS_SignatureResp;
import fi.methics.laverca.rest.json.Status.MobileUserCertificate;
import fi.methics.laverca.rest.util.DTBS;
import fi.methics.laverca.rest.util.MSS_SignatureReqBuilder;
import fi.methics.laverca.rest.util.MssCertificate;
import fi.methics.laverca.rest.util.RestClient;
import fi.methics.laverca.rest.util.RestClient.AuthnMode;
import fi.methics.laverca.rest.util.MssRestException;
import fi.methics.laverca.rest.util.SignatureProfile;
import fi.methics.laverca.rest.util.X509Util;

/**
 * REST MSS Client class.
 * Example usage:
 * 
 * <pre>
 * MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
 *                                           .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
 *                                           .build();
 * try {
 *     MSS_SignatureResp resp = client.authenticate("35847001001", "Authentication test", "http://alauda.mobi/digitalSignature");
 *     if (resp.isSuccess()) {
 *         System.out.println("Successfully authenticated " + resp.getSubjectDN()); 
 *     }
 * } catch (RestException e) {
 *     System.out.println("Failed to authenticate user", e);
 * }
 * </pre>
 */
public class MssClient {

    public static final String FORMAT_PKCS7       = "http://uri.etsi.org/TS102204/v1.1.2#PKCS7";
    public static final String FORMAT_CMS         = "http://uri.etsi.org/TS102204/v1.1.2#CMS-Signature";
    public static final String FORMAT_KIURU_PKCS1 = "http://www.methics.fi/KiuruMSSP/v3.2.0#PKCS1";
    public static final String FORMAT_FICOM_PKCS1 = "http://mss.ficom.fi/TS102204/v1.0.0#PKCS1";

    private static final String DEFAULT_APPWD = "x";
    
    private RestClient client;
    private String apid;
    private String appwd = DEFAULT_APPWD;
    
    private MssClient() { }

    /**
     * Authenticate a user with MSSP
     * @param message          Message shown to the user (e.g. "Please authenticate to Bank A Portal")
     * @param msisdn           Phone number of the user (in international format)
     * @param signatureprofile Signatureprofile of the wanted authentication key
     * @return Authentication response
     * @throws MssRestException if signature fails
     */
    public MSS_SignatureResp authenticate(final String msisdn, 
                                          final String message, 
                                          final SignatureProfile signatureprofile)
        throws MssRestException
    {
        if (signatureprofile == null) {
            throw new MssRestException(MssRestException.MISSING_PARAM, "Missing SignatureProfile in request");
        }
        try {
            JsonRequest jReq = new JsonRequest();
            final String dtbd = message;
            final DTBS   dtbs = new DTBS(dtbd);

            jReq.MSS_SignatureReq = new MSS_SignatureReq(msisdn, dtbs, null);
            jReq.MSS_SignatureReq.SignatureProfile = signatureprofile.getUri();
            jReq.MSS_SignatureReq.MSS_Format       = FORMAT_CMS;
            jReq.MSS_SignatureReq.AP_Info.AP_ID    = this.apid;
            jReq.MSS_SignatureReq.AP_Info.AP_PWD   = this.appwd;
            jReq.MSS_SignatureReq.AP_Info.AP_TransID = "A" + UUID.randomUUID().toString();
            
            JsonResponse jResp = this.client.sendReq(jReq);
            return jResp.MSS_SignatureResp;
        } catch (MssRestException e) {
            throw e;
        } catch (Exception e) {
            throw new MssRestException(e);
        }
    }
    
    /**
     * List user's certificates with ProfileQuery. Returns a map that tells which SignatureProfiles the user has.
     * The map also tells corresponding X509Certificate for each SignatureProfile.
     * 
     * @return User's certificates in a map
     */
    public Map<SignatureProfile, MssCertificate> listCertificates(final String msisdn) {
        try {
            JsonRequest jReq = new JsonRequest();
            jReq.MSS_ProfileReq = new MSS_ProfileReq(msisdn.toString());
            jReq.MSS_ProfileReq.AP_Info.AP_ID    = this.apid;
            jReq.MSS_ProfileReq.AP_Info.AP_PWD   = this.appwd;
            
            JsonResponse resp = this.client.sendReq(jReq);
            
            List<MobileUserCertificate> jsonChain = resp.MSS_ProfileResp.Status.StatusDetail.ProfileQueryExtension.MobileUserCertificate;
            Map<SignatureProfile, MssCertificate> result = new HashMap<>();

            for (MobileUserCertificate chain : jsonChain) {
                if (chain == null) continue;
                if (chain.SignatureProfiles == null) continue;
                if (chain.X509Certificate   == null) continue;
                if (chain.State != null && !chain.State.equals("ACTIVE")) continue; // ignore inactive certs
                
                for (String sigprof : chain.SignatureProfiles) {
                    if (chain.X509Certificate == null) continue;
                    if (chain.X509Certificate.isEmpty()) continue;
                    
                    MssCertificate cert = MssCertificate.fromJson(chain.X509Certificate);
                    cert.addSignatureProfiles(chain.SignatureProfiles);
                    result.put(SignatureProfile.of(sigprof), cert);
                }
            }
            return result;
        } catch (MssRestException e) {
            throw e;
        } catch (Exception e) {
            throw new MssRestException(e);
        }
    }
    
    /**
     * Get a specific user Certificate with ProfileQuery
     * 
     * <p>Example usage:
     * <pre>
     * List&lt;X509Certificate&gt; certs = client.getCertificateChain("35847001001", SignatureProfile.of("http://alauda.mobi/nonRepudiation"));
     * X590Certificate    userCert = certs.get(0);
     * </pre>
     * 
     * @param msisdn            Phone number of the user (in international format)
     * @param signatureprofile  Signatureprofile of the wanted certificate chain
     * @return Certificate if available. If not, returns an empty MssCertificate object.
     * @throws MssRestException
     */
    public MssCertificate getCertificate(final String msisdn, 
                                         final SignatureProfile signatureprofile) 
        throws MssRestException
    {
        if (signatureprofile == null) {
            throw new MssRestException(MssRestException.MISSING_PARAM, "Missing SignatureProfile in request");
        }
        try {
            JsonRequest jReq = new JsonRequest();
            jReq.MSS_ProfileReq = new MSS_ProfileReq(msisdn);
            jReq.MSS_ProfileReq.AP_Info.AP_ID    = this.apid;
            jReq.MSS_ProfileReq.AP_Info.AP_PWD   = this.appwd;
            
            JsonResponse resp = this.client.sendReq(jReq);
            
            List<MobileUserCertificate> jsonChain = resp.MSS_ProfileResp.Status.StatusDetail.ProfileQueryExtension.MobileUserCertificate;
            MssCertificate result = MssCertificate.EMPTY;

            for (MobileUserCertificate chain : jsonChain) {
                if (chain == null) continue;
                if (chain.SignatureProfiles == null) continue;
                if (chain.X509Certificate   == null) continue;
                if (chain.State != null && !chain.State.equals("ACTIVE")) continue; // ignore inactive certs
                
                if (chain.SignatureProfiles.contains(signatureprofile.getUri())) {
                    List<X509Certificate> certs = new ArrayList<>();
                    for (String cert : chain.X509Certificate) {
                        certs.add(X509Util.parseCertificate(cert));
                    }
                    result = new MssCertificate(certs);
                    result.addSignatureProfiles(chain.SignatureProfiles);
                }
            }
            return result;
        } catch (MssRestException e) {
            throw e;
        } catch (Exception e) {
            throw new MssRestException(e);
        }
    }
    
    /**
     * Sign data. This is typically used for document signing, etc.
     * By default this method returns a CMS signature byte[].
     * 
     * <p>Recommended MimeType values:
     * <ul>
     * <li>{@link DTBS#MIME_SHA1}   for pre-computed SHA-1 hashes
     * <li>{@link DTBS#MIME_SHA256} for pre-computed SHA-256 hashes
     * <li>{@link DTBS#MIME_SHA384} for pre-computed SHA-384 hashes
     * <li>{@link DTBS#MIME_TEXTPLAIN} for plain text
     * <li>{@link DTBS#MIME_STREAM} for generic binary data
     * </ul>
     * 
     * @param msisdn   Phone number of the user (in international format)
     * @param message  Message shown to the user (e.g. "Please sign contract.pdf")
     * @param digest   Document digest
     * @param mimetype Mime-Type of the digest (e.g. "application/x-sha256") s
     * @param signatureprofile Signatureprofile of the wanted authentication key
     * @return raw CMS signature
     * @throws MssRestException if signature fails
     */
    public byte[] sign(final String msisdn,
                       final String message,
                       final byte[] digest, 
                       final String mimetype,
                       final SignatureProfile signatureprofile)
        throws MssRestException
    {
        if (signatureprofile == null) {
            throw new MssRestException(MssRestException.MISSING_PARAM, "Missing SignatureProfile in request");
        }
        final String dtbd = message;
        final DTBS   dtbs = new DTBS(digest, DTBS.ENCODING_BASE64,mimetype);

        MSS_SignatureReqBuilder builder = new MSS_SignatureReqBuilder();
        builder.withApid(this.apid);
        builder.withAppwd(this.appwd);
        builder.withDtbd(dtbd);
        builder.withDtbs(dtbs);
        builder.withMssFormat(FORMAT_CMS);
        builder.withMsisdn(msisdn);
        builder.withSignatureProfile(signatureprofile);

        MSS_SignatureResp resp = this.sign(builder.build());
        if (resp == null) {
            throw new MssRestException(MssRestException.UNABLE_TO_PROVIDE_SERVICES, "Failed to get response");
        }
        return resp.getRawSignature();
    }
    
    /**
     * Sign data. This is typically used for document signing, etc
     * This method returns a PKCS1 signature byte[].
     * 
     * <p>Recommended MimeType values:
     * <ul>
     * <li>{@link DTBS#MIME_SHA1}   for pre-computed SHA-1 hashes
     * <li>{@link DTBS#MIME_SHA256} for pre-computed SHA-256 hashes
     * <li>{@link DTBS#MIME_SHA384} for pre-computed SHA-384 hashes
     * <li>{@link DTBS#MIME_TEXTPLAIN} for plain text
     * <li>{@link DTBS#MIME_STREAM} for generic binary data
     * </ul>
     * 
     * @param msisdn   Phone number of the user (in international format)
     * @param message  Message shown to the user (e.g. "Please sign contract.pdf")
     * @param digest   Document digest
     * @param mimetype Mime-Type of the digest (e.g. "application/x-sha256") s
     * @param signatureprofile Signatureprofile of the wanted authentication key
     * @return raw CMS signature
     * @throws MssRestException if signature fails
     */
    public byte[] signPKCS1(final String msisdn,
                            final String message,
                            final byte[] digest, 
                            final String mimetype,
                            final SignatureProfile signatureprofile)
        throws MssRestException
    {
        if (signatureprofile == null) {
            throw new MssRestException(MssRestException.MISSING_PARAM, "Missing SignatureProfile in request");
        }
        final String dtbd = message;
        final DTBS   dtbs = new DTBS(digest, DTBS.ENCODING_BASE64, mimetype);

        MSS_SignatureReqBuilder builder = new MSS_SignatureReqBuilder();
        builder.withApid(this.apid);
        builder.withAppwd(this.appwd);
        builder.withDtbd(dtbd);
        builder.withDtbs(dtbs);
        builder.withMssFormat(FORMAT_KIURU_PKCS1);
        builder.withMsisdn(msisdn);
        builder.withSignatureProfile(signatureprofile);

        MSS_SignatureResp resp = this.sign(builder.build());
        if (resp == null) {
            throw new MssRestException(MssRestException.UNABLE_TO_PROVIDE_SERVICES, "Failed to get response");
        }
        return resp.getRawSignature();
    }
    
    /**
     * Advanced method that can be used to send any MSS_SignatureReq to the MSSP.
     * @param req MSS_SignatureReq
     * @return MSS_SignatureREsp
     * @throws MssRestException if signature fails
     * @see MSS_SignatureReqBuilder
     */
    public MSS_SignatureResp sign(final MSS_SignatureReq req) throws MssRestException {
        try {
            JsonRequest jReq = new JsonRequest();
            jReq.MSS_SignatureReq = req;
            JsonResponse jResp = this.client.sendReq(jReq);
            return jResp.MSS_SignatureResp;
        } catch (MssRestException e) {
            throw e;
        } catch (Exception e) {
            throw new MssRestException(e);
        }
    }
    
    /**
     * Get the underlying REST client
     * @return REST client
     */
    protected RestClient getRestClient() {
        return this.client;
    }
    
    /**
     * MSS Client Builder
     */
    public static class Builder {

        private String restUrl;
        private String secondaryUrl;
        private String apid;
        private String apname;
        private String password;
        private String apikey;
        private AuthnMode authnMode;
        
        private String appwd;
        
        public MssClient build() {
            MssClient client = new MssClient();
            client.client = new RestClient();
            if (this.authnMode != null) {
                client.client.setAuthnMode(this.authnMode);
            }
            if (this.authnMode == AuthnMode.APIKEY) {
                client.client.setApId(this.apid);
                client.client.setApiKey(this.apikey);
            } else {
                client.client.setApName(this.apname);
                client.client.setPassword(this.password);
            }
            client.client.setRestUrl(this.restUrl);
            client.client.setSecondaryUrl(this.secondaryUrl);
            client.appwd = this.appwd;
            return client;
        }
        
        /**
         * Set primary RESTAPI service URL
         * @param resturl RESTAPI service URL
         * @return this builder
         */ 
        public Builder withRestUrl(String resturl) {
            this.restUrl = resturl;
            return this;
        }
        
        /**
         * Set alternative RESTAPI service URL
         * @param alternateUrl RESTAPI service URL
         * @return this builder
         */ 
        public Builder withSecondaryUrl(String alternateUrl) {
            this.secondaryUrl = alternateUrl;
            return this;
        }
        
        /**
         * Set username/password authn
         * <p>Note: Use either this or {@link #withApiKey(String, String)}.
         * 
         * @param apname   AP Name (not AP_ID)
         * @param password AP REST Password
         * @return
         */
        public Builder withPassword(String apname, String password) {
            this.apname   = apname;
            this.password = password;
            this.authnMode = AuthnMode.PASSWORD;
            return this;
        }

        /**
         * Set AP_ID/API_KEY authn
         * <p>Note: Use either this or {@link #withPassword(String, String)}.
         * 
         * @param apid   AP_ID
         * @param apikey API_KEY
         * @return this builder
         */
        public Builder withApiKey(String apid, String apikey) {
            this.apid   = apid;
            this.apikey = apikey;
            this.authnMode = AuthnMode.APIKEY;
            return this;
        }

        /**
         * Set AP_PWD
         * <p>Note that this value is different from the REST password
         * and the default value is enough in most cases.
         * @param apid   AP_ID
         * @param apikey API_KEY
         * @return this builder
         */
        public Builder withAppwd(String appwd) {
            this.appwd = appwd;
            return this;
        }
    }
    
}
