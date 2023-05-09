//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.rest.json.MSS_SignatureResp;
import fi.methics.laverca.rest.util.MssCertificate;
import fi.methics.laverca.rest.util.MssRestException;
import fi.methics.laverca.rest.util.SignatureProfile;

public class TestAuthentication {


    public static final String BASE_URL = "https://demo.methics.fi/restapi/";
    public static final String APNAME   = "TestAP";
    public static final String PASSWORD = "9TMzfH7EKXETOB8FT5gz";
    public static final String MSISDN   = "35847001001";
    public static final SignatureProfile SIGPROF = SignatureProfile.of("http://alauda.mobi/digitalSignature");

    
    @Test
    public void testAuthentication() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        MSS_SignatureResp resp = client.authenticate(MSISDN, "testAuthentication", SIGPROF);
        Assertions.assertTrue(resp.isSuccessful(), "Authentication succeeded");
    }
    
    @Test
    public void testAuthenticationWithUnknownMsisdn() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        MssRestException e = Assertions.assertThrows(MssRestException.class, () -> 
            {
                client.authenticate("358479991141", "testAuthentication", SIGPROF);
            });
        Assertions.assertEquals("105", e.getErrorCode());
    }
    
    @Test
    public void testAuthenticationWithUnknownSignatureProfile() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        MssRestException e = Assertions.assertThrows(MssRestException.class, () -> 
            {
                client.authenticate(MSISDN, "testAuthentication", SignatureProfile.of("FOOBAR"));
            });
        Assertions.assertEquals("109", e.getErrorCode());
    }
    
    @Test
    public void testListCertificates() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        Map<SignatureProfile, MssCertificate> certs = client.listCertificates(MSISDN);
        Assertions.assertTrue(certs.size() > 0, "Got certificates");
    }

    @Test
    public void testGetCertificateChain() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        MssCertificate cert = client.getCertificate(MSISDN, SIGPROF);
        Assertions.assertNotNull(cert, "Got certificate object");
        Assertions.assertNotNull(cert.getCertificate(), "Got X509 certificate");
        Assertions.assertNotNull(cert.getCertificateChain(), "Got X509 certificate chain");
    }
    

    
    @Test
    public void testSecondaryUrl() {
        MssClient client = new MssClient.Builder().withRestUrl("http://localhost")
                                                  .withSecondaryUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        MSS_SignatureResp resp = client.authenticate(MSISDN, "testAuthentication", SIGPROF);
        Assertions.assertTrue(resp.isSuccessful(), "Authentication succeeded");
    }
    
}
