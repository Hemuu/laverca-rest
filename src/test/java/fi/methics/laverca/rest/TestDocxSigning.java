//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import fi.methics.laverca.rest.util.SignatureProfile;

public class TestDocxSigning {


    public static final String BASE_URL = "https://demo.methics.fi/restapi/";
    public static final String APNAME   = "TestAP";
    public static final String PASSWORD = "9TMzfH7EKXETOB8FT5gz";
    public static final String MSISDN   = "35847001001";
    public static final SignatureProfile SIGPROF = SignatureProfile.of("http://alauda.mobi/nonRepudiation");

    @Disabled
    @Test
    public void testSignDocx() throws Exception {
        MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                                  .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                                  .build();
        DocxSigner signer = new DocxSigner(client);                                                                                      
        File docx = new File("example.docx");
        InputStream is = new FileInputStream(docx);
        ByteArrayOutputStream  os = signer.signDocument(MSISDN, "Please sign example.docx", is, SIGPROF);
        try (FileOutputStream fos = new FileOutputStream(new File("example.signed.docx"))) {
            os.writeTo(fos);
            os.flush();
        }
    }

}
