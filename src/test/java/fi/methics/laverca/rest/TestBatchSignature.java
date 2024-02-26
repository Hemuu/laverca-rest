//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest;

import java.util.Collection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.rest.json.MSS_SignatureReq;
import fi.methics.laverca.rest.json.MSS_SignatureResp;
import fi.methics.laverca.rest.util.DTBS;
import fi.methics.laverca.rest.util.MSS_SignatureReqBuilder;
import fi.methics.laverca.rest.util.SignatureProfile;

public class TestBatchSignature {


    public static final String BASE_URL = "https://demo.methics.fi/restapi/";
    public static final String APNAME   = "TestAP";
    public static final String PASSWORD = "9TMzfH7EKXETOB8FT5gz";
    public static final String MSISDN   = "35847001001";
    public static final SignatureProfile SIGPROF = SignatureProfile.of("http://alauda.mobi/digitalSignature");

    
    @Test
    public void testTwoSignatures() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        
        MSS_SignatureReq req = new MSS_SignatureReqBuilder()
                .withMsisdn(MSISDN)
                .withDtbs(new DTBS("test1")).withDtbd("test1")
                .withBatchSinature("test2", new DTBS("test2"))
                .build();
        
        MSS_SignatureResp resp = client.sign(req);
        Assertions.assertTrue(resp.isSuccessful(), "Batch signature succeeded");
    }
    
    
    @Test
    public void testFourSignatures() {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        
        MSS_SignatureReq req = new MSS_SignatureReqBuilder()
                .withMsisdn(MSISDN)
                .withDtbs(new DTBS("test1")).withDtbd("test1")
                .withBatchSinature("test2", new DTBS("test2"))
                .withBatchSinature("test3", new DTBS("test3"))
                .withBatchSinature("test4", new DTBS("test4"))
                .build();
        
        MSS_SignatureResp resp = client.sign(req);
        Assertions.assertTrue(resp.isSuccessful(), "Batch signature succeeded");
        
        Collection<String> dtbds = resp.getBatchSignatures().keySet();
        for (String dtbd : dtbds) {
            Assertions.assertNotNull(resp.getBatchSignatures().get(dtbd), "Signature for DTBD " + dtbd);
        }
        Assertions.assertTrue(dtbds.contains("test1"), "Got signature for test1");
        Assertions.assertTrue(dtbds.contains("test2"), "Got signature for test2");
        Assertions.assertTrue(dtbds.contains("test3"), "Got signature for test3");
        Assertions.assertTrue(dtbds.contains("test4"), "Got signature for test4");
    }
    
}
