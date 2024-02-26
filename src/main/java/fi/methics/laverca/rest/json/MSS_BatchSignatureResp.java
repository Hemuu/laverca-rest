//
//  (c) Copyright 2003-2024 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest.json;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import fi.methics.laverca.rest.json.ServiceResponse.BatchSignatureResponse;

/**
 * Batch signature response helper
 */
public class MSS_BatchSignatureResp extends MSS_SignatureResp {

    /**
     * Get all batch signatures if available.
     * @return Map which contains both the DTBD and Signature. Map will be empty if no batch signature was done.
     */
    public Map<String, byte[]> getBatchSignatures() {
        Map<String, byte[]> result = new HashMap<>();
        for (ServiceResponse resp : this.getServiceResponses()) {
            if (AdditionalService.BATCH_AS.equals(resp.Description)) {
                if (resp.BatchSignatureResponses != null) {
                    for (BatchSignatureResponse bResp : resp.BatchSignatureResponses) {
                        if (bResp == null) continue;
                        String signature = bResp.MSS_Signature != null ? bResp.MSS_Signature.Base64Signature : null;
                        result.put(bResp.DTBD, Base64.getDecoder().decode(signature));
                    }
                }
            }
        }
        return result;
    }
    
}
