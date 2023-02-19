//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.util;

import java.util.UUID;

import fi.methics.laverca.rest.MssClient;
import fi.methics.laverca.rest.json.MSS_SignatureReq;

/**
 * Builder for MSS_SignatureReq JSON objects
 * <p>Usage:
 * <pre>
 * MSS_SignatureReqBuilder builder = new MSS_SignatureReqBuilder();
 * builder.withMsisdn("35847001001");
 * MSS_SignatureReq req = builder.build();
 * </pre>
 */
public class MSS_SignatureReqBuilder {

    public static final int DEFAULT_TIMEOUT = 60000; // 60 s
    
    private String msisdn;
    private String dtbd;
    private DTBS   dtbs;
    
    private String signatureprofile;
    private String apid;
    private String appwd = "x";
    private String mssFormat;
    private int timeout;
    
    public MSS_SignatureReqBuilder() {
        
    }
    
    /**
     * Build the MSS_SignatureReq
     * @return MSS_SignatureReq
     */
    public MSS_SignatureReq build() {
        MSS_SignatureReq req = new MSS_SignatureReq(this.msisdn, this.dtbs, this.dtbd);
        req.SignatureProfile = this.signatureprofile;
        req.MSS_Format       = this.mssFormat;
        req.AP_Info.AP_ID    = this.apid;
        req.AP_Info.AP_PWD   = this.appwd;
        req.AP_Info.AP_TransID = "A" + UUID.randomUUID().toString();
        if (this.timeout > 0) {
            req.TimeOut = String.valueOf(this.timeout);
        }
        return req;
    }
    
    /**
     * Set AP_ID to the request
     * @param apid AP_ID
     * @return
     */
    public MSS_SignatureReqBuilder withApid(String apid) {
        this.apid = apid;
        return this;
    }
    
    /**
     * Set AP_PWD to the request
     * <p>This defaults to "x" if not set.
     * @param appwd AP_PWD
     * @return this builder
     */
    public MSS_SignatureReqBuilder withAppwd(String appwd) {
        this.appwd = appwd;
        return this;
    }
    
    /**
     * Set DTBD (data to be displayed)
     * @param dtbd DTBD
     * @return this builder
     */
    public MSS_SignatureReqBuilder withDtbd(String dtbd) {
        this.dtbd = dtbd;
        return this;
    }

    /**
     * Set DTBS (Data to be signed)
     * @param dtbs DTBS
     * @return this builder
     */
    public MSS_SignatureReqBuilder withDtbs(DTBS dtbs) {
        this.dtbs = dtbs;
        return this;
    }
    
    /**
     * Set MSISDN
     * @param msisdn MSISDN
     * @return this builder
     */
    public MSS_SignatureReqBuilder withMsisdn(String msisdn) {
        this.msisdn = msisdn;
        return this;
    }
    
    /**
     * Set MSS_Format in the request. 
     * <p>See {@link MssClient} constants for a list of formats.
     * Example: {@link MssClient#FORMAT_CMS}
     * @param mssFormat MSS_Format
     * @return
     */
    public MSS_SignatureReqBuilder withMssFormat(String mssFormat) {
        this.mssFormat = mssFormat;
        return this;
    }
    
    /**
     * Set SignatureProfile
     * @param signatureprofile SignatureProfile
     * @return this builder
     */
    public MSS_SignatureReqBuilder withSignatureProfile(SignatureProfile signatureprofile) {
        if (signatureprofile == null) return this;
        this.signatureprofile = signatureprofile.getUri();
        return this;
    }
    
    /**
     * Set SignatureProfile
     * @param signatureprofile SignatureProfile
     * @return this builder
     */
    public MSS_SignatureReqBuilder withSignatureProfile(String signatureprofile) {
        this.signatureprofile = signatureprofile;
        return this;
    }
    
    /**
     * Set timeout in milliseconds
     * <p>Default is 60000 ms (60 sec)
     * @param timeout timeout ms
     * @return this builder
     */
    public MSS_SignatureReqBuilder withTimeout(int timeout) {
        this.timeout = timeout;
        return this;
    }
    
}
