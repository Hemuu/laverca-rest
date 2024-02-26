//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import fi.methics.laverca.rest.util.DTBS;

/**
 * Batch signature request helper
 */
public class MSS_BatchSignatureReq extends MSS_SignatureReq {

    public MSS_BatchSignatureReq(String msisdn, DTBS dtbs, String dtbd) {
        super(msisdn, dtbs, dtbd);
    }
    
    /**
     * Add a new batch signature request
     * @param dtbs Data to be Signed
     * @param dtbd Data to be Displayed
     */
    public void addBatchSignatureRequest(DTBS dtbs, String dtbd) {
        this.addAdditionalService(new BatchSignatureReq(dtbs, dtbd));
    }

    public void addBatchSignatureRequest(BatchSignatureReq br) {
        this.addAdditionalService(br);        
    }

}
