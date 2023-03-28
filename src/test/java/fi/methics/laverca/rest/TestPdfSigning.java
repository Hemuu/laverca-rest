//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.junit.jupiter.api.Test;

import fi.methics.laverca.rest.util.SignatureProfile;

public class TestPdfSigning {


    public static final String BASE_URL = "https://demo.methics.fi/restapi/";
    public static final String APNAME   = "TestAP";
    public static final String PASSWORD = "9TMzfH7EKXETOB8FT5gz";
    public static final String MSISDN   = "35847001001";
    public static final SignatureProfile SIGPROF = SignatureProfile.of("http://alauda.mobi/nonRepudiation");

    
    @Test
    public void testSignPdf() throws Exception {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        PdfSigner signer = new PdfSigner(client);                                                                                      
        File pdf = new File("example.pdf");
        InputStream is = new FileInputStream(pdf);
        ByteArrayOutputStream  os = signer.signDocument(MSISDN, "Please sign example.pdf", is, SIGPROF);
        try (FileOutputStream fos = new FileOutputStream(new File("example.signed.pdf"))) {
            os.writeTo(fos);
            os.flush();
        }
    }

}
