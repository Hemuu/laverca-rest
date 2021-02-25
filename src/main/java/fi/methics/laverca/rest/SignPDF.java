//
//  (c) Copyright 2003-2021 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.stream.Collectors;

import org.bouncycastle.cms.CMSSignedData;

import eu.europa.esig.dss.enumerations.DigestAlgorithm;
import eu.europa.esig.dss.enumerations.SignatureLevel;
import eu.europa.esig.dss.enumerations.SignaturePackaging;
import eu.europa.esig.dss.model.DSSDocument;
import eu.europa.esig.dss.model.FileDocument;
import eu.europa.esig.dss.model.TimestampParameters;
import eu.europa.esig.dss.model.x509.CertificateToken;
import eu.europa.esig.dss.pades.PAdESSignatureParameters;
import eu.europa.esig.dss.service.crl.OnlineCRLSource;
import eu.europa.esig.dss.service.ocsp.OnlineOCSPSource;
import eu.europa.esig.dss.service.tsp.OnlineTSPSource;
import eu.europa.esig.dss.validation.CertificateVerifier;
import eu.europa.esig.dss.validation.CommonCertificateVerifier;
import fi.methics.laverca.rest.util.LavercaPAdESService;

public class SignPDF {

    private static final DigestAlgorithm    DIGEST_ALG    = DigestAlgorithm.SHA256;
    
    // PWD authn
    private static final String AP_NAME       = "laverca-test";
    private static final String PASSWORD      = "3Zqka5MR1vfzzSxPHIYf";
    
    // API_KEY authn
    private static final String AP_ID         = "http://laverca-test";
    private static final String API_KEY       = "h4h428QAiNuhnljvw9U1fsYRUDwuv9ytDzAiepv1ywtvyJpy";
    
    private static final String MSISDN        = "35847001001";
    private static final String SIG_PROFILE   = "http://alauda.mobi/nonRepudiation";
    private static final String REST_URL      = "http://localhost:9060/rest/service";
    
    private static final String REASON          = "Signing test";
    private static final String DOC_PATH        = "./example.pdf";
    private static final String SIGNED_DOC_PATH = "./example.signed.pdf";
    
    private MssClient client;
    private DSSDocument doc;

    public static void main(String[] args) throws Exception {
        new SignPDF().run();
    }
    
    /**
     * Run the example
     * @throws Exception
     */
    public void run() throws Exception {
        
        //this.client = MssClient.initWithApiKey(AP_ID, API_KEY, REST_URL);
        this.client = MssClient.initWithPassword(AP_NAME, PASSWORD, REST_URL);
        this.doc    = new FileDocument(DOC_PATH);
        
        CommonCertificateVerifier verifier   = this.createVerifier();
        LavercaPAdESService       service    = this.createService(verifier);
        PAdESSignatureParameters  parameters = this.createParams();
        
        // sign digest
        final byte[] digest    = service.computeDocumentDigest(this.doc, parameters);
        final byte[] signature = this.client.signDocument(digest, MSISDN, SIG_PROFILE);
        
        // Attach signature to PDF
        final CMSSignedData signedData = new CMSSignedData(signature);
        final DSSDocument   signedDoc  = service.signDocument(this.doc, parameters, signedData);
        signedDoc.save(SIGNED_DOC_PATH);
        System.out.println("Saved signed document to " + new File(SIGNED_DOC_PATH).getAbsolutePath());
    }
    
    /**
     * Create ESIG PAdES parameters 
     * @return PAdES parameters
     */
    private PAdESSignatureParameters createParams() {
        List<X509Certificate> chain = this.client.getCertificateChain(MSISDN, SIG_PROFILE);
        PAdESSignatureParameters parameters = new PAdESSignatureParameters();
        parameters.setSignatureLevel(SignatureLevel.PAdES_BASELINE_LTA);
        parameters.setSignaturePackaging(SignaturePackaging.ENVELOPED);
        parameters.setDigestAlgorithm(DIGEST_ALG);
        parameters.setReason(REASON);
        parameters.setSignatureSize(25600);
        parameters.setArchiveTimestampParameters(new TimestampParameters(DIGEST_ALG));
        parameters.setCertificateChain(chain.stream().map(CertificateToken::new).collect(Collectors.toList()));
        parameters.setSigningCertificate(new CertificateToken(chain.get(0)));
        
        return parameters;
    }
    
    /**
     * Create ESIG PAdES verifier 
     * @return PAdES verifier
     */
    private CommonCertificateVerifier createVerifier() {
        CommonCertificateVerifier verifier = new CommonCertificateVerifier();
        verifier.setExceptionOnMissingRevocationData(false);
        verifier.setCheckRevocationForUntrustedChains(true);
        verifier.setIncludeCertificateRevocationValues(true);
        verifier.setOcspSource(new OnlineOCSPSource());
        verifier.setCrlSource(new OnlineCRLSource());
        return verifier;
    }
    
    /**
     * Create ESIG PAdES service 
     * @return PAdES service
     */
    private LavercaPAdESService createService(CertificateVerifier verifier) {
        LavercaPAdESService service = new LavercaPAdESService(verifier);
        service.setTspSource(new OnlineTSPSource("http://timestamp.digicert.com/"));
        return service;
    }
    
}
