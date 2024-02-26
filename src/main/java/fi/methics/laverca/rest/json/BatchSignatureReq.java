//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import com.google.gson.annotations.SerializedName;

import fi.methics.laverca.rest.json.MSS_SignatureReq.Data;
import fi.methics.laverca.rest.util.DTBS;

/**
 * MSS AdditionalService for requesting an additional signature.
 * Up to 9 of these can be present in each request.
 */
public class BatchSignatureReq extends AdditionalService {

    @SerializedName("BatchSignatureRequest")
    public BatchSignatureRequest BatchSignatureRequest;
    
    public BatchSignatureReq(DTBS dtbs, String dtbd) {
        this.Description = BATCH_AS;
        this.BatchSignatureRequest = new BatchSignatureRequest();
        this.BatchSignatureRequest.DataToBeSigned = new Data(dtbs);
        this.BatchSignatureRequest.DataToBeDisplayed = new Data();
        this.BatchSignatureRequest.DataToBeDisplayed.Data = dtbd;
    }
    
    public class BatchSignatureRequest {

        @SerializedName("DataToBeSigned")
        public Data DataToBeSigned;
        
        @SerializedName("DataToBeDisplayed")
        public Data DataToBeDisplayed;
        
    }
    
}
