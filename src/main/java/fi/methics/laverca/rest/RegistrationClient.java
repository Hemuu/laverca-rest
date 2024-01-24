//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest;

import fi.methics.laverca.rest.json.JsonRequest;
import fi.methics.laverca.rest.json.JsonResponse;
import fi.methics.laverca.rest.json.MSS_RegistrationReq;
import fi.methics.laverca.rest.json.MSS_RegistrationResp;
import fi.methics.laverca.rest.util.MssRestException;

/**
 * REST MSS Registration Client class.
 * Example usage:
 * 
 * <pre>
 * MssClient client = new MssClient.Builder().withRestUrl("https://demo.methics.fi/restapi/")
 *                                           .withPassword("TestAP", "9TMzfH7EKXETOB8FT5gz")
 *                                           .build();
 * RegistrationClient regClient = new RegistrationClient(client);
 * 
 * try {
 *     MSS_RegistrationReq  req  = new MSS_RegistrationReqBuilder(...).build();
 *     MSS_RegistrationResp resp = regClient.sendMregReq(req);
 * } catch (RestException e) {
 *     System.out.println("MReg failed", e);
 * }
 * </pre>
 */
public class RegistrationClient {

    private MssClient client;
    
    public RegistrationClient(MssClient client) {
        this.client = client;
    }

    /**
     * Send an MReg request
     * @param req MReg request
     * @return MReg response
     */
    public MSS_RegistrationResp sendMRegReq(MSS_RegistrationReq req) {
        try {
            JsonRequest jReq = new JsonRequest();
            jReq.MSS_RegistrationReq = req;
            JsonResponse jResp = this.client.getRestClient().sendReq(jReq);
            return jResp.MSS_RegistrationResp;
        } catch (MssRestException e) {
            throw e;
        } catch (Exception e) {
            throw new MssRestException(e);
        }
    }
    
}
