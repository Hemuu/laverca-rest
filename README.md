# Laverca-REST

Laverca-REST is a simple client library for Kiuru REST API. It uses a JSON format that closely mimics the _ETSI TS 102 204_ SOAP API.

Features
=========
1. User authentication
2. Certificate retrieval
3. Simple document signing

Usage
=====

Authentication
-----
``` 
MssClient client = MssClient.initWithPassword("TestAP", "9TMzfH7EKXETOB8FT5gz", "https://demo.methics.fi/restapi/");
try {
    MSS_SignatureResp resp = client.authenticate("35847001001", "Authentication test", "http://alauda.mobi/digitalSignature");
    if (resp.isSuccess()) {
        System.out.println("Successfully authenticated " + resp.getSubjectDN()); 
    }
} catch (RestException e) {
    System.out.println("Failed to authenticate user", e);
}
``` 

PDF Signing
-----

```
MssClient client = MssClient.initWithPassword("TestAP", "9TMzfH7EKXETOB8FT5gz", "https://demo.methics.fi/restapi/");
PdfSigner signer = new PdfSigner(client);                                                                                      
                                                                                                                                 
File doc = new File("example.pdf");
InputStream is = new FileInputStream(doc);
ByteArrayOutputStream  os = signer.signDocument("35847001001", "Please sign example.pdf", is, "http://alauda.mobi/nonRepudiation");
try (FileOutputStream fos = new FileOutputStream(new File("example.signed.pdf"))) {
    os.writeTo(fos);
    os.flush(); 
}                                                                                                                      
```

DOCX Signing
-----
```
MssClient client = MssClient.initWithPassword("TestAP", "9TMzfH7EKXETOB8FT5gz", "https://demo.methics.fi/restapi/");
DocxSigner signer = new DocxSigner(client);                                                                                      
                                                                                                                                 
File doc = new File("example.docx");                                                                                            
InputStream is = new FileInputStream(doc);                                                                                       
ByteArrayOutputStream  os = signer.signDocument("35847001001", "Please sign example.docx", is, "http://alauda.mobi/nonRepudiation");
try (FileOutputStream fos = new FileOutputStream(new File("example.signed.docx"))) {                                            
    os.writeTo(fos);                                                                                                             
    os.flush();                                                                                                                  
}
```
