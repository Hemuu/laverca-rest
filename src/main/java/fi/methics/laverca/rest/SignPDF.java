//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import fi.methics.laverca.rest.util.SignatureProfile;

/**
 * Example for PDF signing
 * @see PdfSigner
 */
public class SignPDF {

    private static final String REST_URL      = "http://localhost:9060/rest/service";
    private static final String AP_NAME       = "TestAP";
    private static final String PASSWORD      = "9TMzfH7EKXETOB8FT5gz";
    private static final String MSISDN        = "35847001001";
    
    private static final SignatureProfile SIG_PROFILE = SignatureProfile.of("http://alauda.mobi/nonRepudiation");
    
    private static final String DOC_PATH        = "./example.pdf";
    private static final String SIGNED_DOC_PATH = "./example.signed.pdf";
    
    public static void main(String[] args) throws Exception {
        
        MssClient client = new MssClient.Builder().withRestUrl(REST_URL)
                                                 .withPassword(AP_NAME, PASSWORD) 
                                                 .build();

        PdfSigner signer = new PdfSigner(client);
        
        File doc = new File(DOC_PATH);
        InputStream is = new FileInputStream(doc);
        ByteArrayOutputStream  os = signer.signDocument(MSISDN, 
                                                        "Please sign example.pdf", 
                                                        is, 
                                                        SIG_PROFILE);
        try (FileOutputStream fos = new FileOutputStream(new File(SIGNED_DOC_PATH))) {
            os.writeTo(fos);
        }
        
        System.out.println("Saved signed document to " + new File(SIGNED_DOC_PATH).getAbsolutePath());
    }
    
    
}
