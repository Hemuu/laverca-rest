//
//  (c) Copyright 2003-2021 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest.util;


import java.io.IOException;
import java.util.Base64;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.client.HttpClient;

import fi.methics.laverca.rest.json.JsonRequest;
import fi.methics.laverca.rest.json.JsonResponse;

public class RestClient {

    private static final Log log = LogFactory.getLog(RestClient.class);
    
    private HttpClient httpClient;
    private String apid;
    private String apikey;
    private String resturl;
    
    public RestClient(final String apid,
                      final String apikey,
                      final String resturl) {
        this.httpClient = RestSocketFactory.getNewHttpClient();
        this.apid    = apid;
        this.apikey  = apikey;
        this.resturl = resturl;
    }
    
    /**
     * Send REST JSON request
     * @param jReq JSON request
     * @return JSON response
     * @throws KdssException 
     */
    public JsonResponse sendReq(final JsonRequest jReq) throws RestException {
        final String req  = jReq.toJson();
        final String resp = this.sendReq(req);
        try {
            JsonResponse jResp = JsonResponse.fromString(resp);
            if (jResp.isFault()) {
                throw new RestException(jResp.getFaultCode(), jResp.getFaultDetail());
            }
            return jResp;
        } catch (RestException e) {
            throw e;
        } catch (Exception e) {
            throw new RestException(RestException.INTERNAL_ERROR, e.getMessage());
        }
    }
    
    /**
     * Send HTTP Post request (multipart)
     * @param jReq JSON Request
     * @return String Response
     * @throws KdssException 
     */
    private String sendReq(final String jReq) throws RestException {
        log.debug("Connecting to " + this.resturl);
        log.debug("Sending request " + jReq);
        return this.sendHmacReq(jReq);
    }
    
    /**
     * Send HTTP Post request using HMAC authn
     * @param req       Req as raw String
     * @param targetUrl Target URL as String
     * @param userId    AP UserID
     * @param apiKey    AP API_KEY
     * @return String Response
     * @throws KdssException 
     */
    private String sendHmacReq(final String req) throws RestException {
        
        String userid = this.getUserId(this.apid);
        String apikey = this.apikey;
        
        try {
            HmacHttpClient client = new HmacHttpClient(this.httpClient, userid, apikey);
            return client.send(req, this.resturl);
        } catch (IOException e) {
            log.error("Connection to " + this.resturl + " failed: " + e.getMessage());
            throw new RestException(RestException.UNABLE_TO_PROVIDE_SERVICES, e.getMessage());
        }
    }
    
    /**
     * Get UserID from AP_ID
     * @param apid AP_ID
     * @return UserID
     */
    private String getUserId(final String apid) {
        try {
            return Base64.getEncoder().encodeToString(apid.getBytes("UTF-8"));
        } catch (Exception e) {
            log.debug("Could not encode " + apid + " to base64");
            return apid;
        }
    }

}
