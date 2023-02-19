//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import fi.methics.laverca.rest.MssClient;

public abstract class DocumentSigner {

    protected MssClient client;
    
    public DocumentSigner(MssClient client) {
        this.client = client;
    }
    
    /**
     * Sign a document
     * 
     * @param msisdn  User's phone number (MSISDN, international format)
     * @param message Message to display to user (e.g. "Sign document x")
     * @param is      InputStream containing the document
     * @param signatureProfile SignatureProfile URI
     * @return Signed document OutputStream
     * @throws IOException      if document manipulation fails
     * @throws MssRestException if signing fails
     */
    public abstract ByteArrayOutputStream signDocument(final String msisdn,
                                                       final String message,
                                                       final InputStream is,
                                                       final SignatureProfile signatureProfile) throws IOException, MssRestException;
    
}
