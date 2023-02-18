//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.rest.json.MSS_SignatureResp;
import fi.methics.laverca.rest.util.MssCertificate;
import fi.methics.laverca.rest.util.SignatureProfile;

public class TestAuthentication {


    public static final String BASE_URL = "https://demo.methics.fi/restapi/";
    public static final String APNAME   = "TestAP";
    public static final String PASSWORD = "9TMzfH7EKXETOB8FT5gz";
    public static final String MSISDN   = "35847001001";
    public static final String SIGPROF  = "http://alauda.mobi/digitalSignature";

    
    @Test
    public void testAuthentication() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        MSS_SignatureResp resp = client.authenticate(MSISDN, "testAuthentication", SIGPROF);
        Assertions.assertTrue(resp.isSuccessful(), "Authentication succeeded");
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
        MssCertificate cert = client.getCertificate(MSISDN, SignatureProfile.of(SIGPROF));
        Assertions.assertNotNull(cert, "Got certificate object");
        Assertions.assertNotNull(cert.getCertificate(), "Got X509 certificate");
        Assertions.assertNotNull(cert.getCertificateChain(), "Got X509 certificate chain");
    }
    
}
