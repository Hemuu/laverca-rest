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
```java
MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                          .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz") 
                                          .build();                                       
try {
    MSS_SignatureResp resp = client.authenticate("35847001001",
                                                 "Authentication test", 
                                                 SignatureProfile.of("http://alauda.mobi/digitalSignature"));
    if (resp.isSuccess()) {
        System.out.println("Successfully authenticated " + resp.getSubjectDN()); 
    }
} catch (RestException e) {
    System.out.println("Failed to authenticate user", e);
}
``` 

PDF Signing
-----
```java
MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                          .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                          .build();
PdfSigner signer = new PdfSigner(client);

File doc = new File("example.pdf");
InputStream is = new FileInputStream(doc);
ByteArrayOutputStream  os = signer.signDocument("35847001001", 
                                                "Please sign example.pdf", 
                                                is, 
                                                SignatureProfile.of("http://alauda.mobi/digitalSignature"));
try (FileOutputStream fos = new FileOutputStream(new File("example.signed.pdf"))) {
    os.writeTo(fos);
}
```

DOCX Signing
-----
```java
MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
                                          .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
                                          .build();
DocxSigner signer = new DocxSigner(client);

File doc = new File("example.docx");
InputStream is = new FileInputStream(doc);
ByteArrayOutputStream  os = signer.signDocument("35847001001",
                                                "Please sign example.docx",
                                                is,
                                                SignatureProfile.of("http://alauda.mobi/digitalSignature"));
try (FileOutputStream fos = new FileOutputStream(new File("example.signed.docx"))) {
    os.writeTo(fos);
}
```

Manual Building
=====

Building a JAR
-----
```bash
mvn clean package
```

Building a stand-alone JAR with included dependencies
-----
```bash
mvn clean package assembly:single
```

Running the PDF signing example
-----
```bash
java -jar target/laverca-rest-1.0.0-jar-with-dependencies.jar fi.methics.laverca.rest.SignPDF
```
