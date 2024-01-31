//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest.util;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

public class RestSocketFactory {
    
    /**
     * Get a new HTTP client with default values
     * @return HTTP client
     */
    public static HttpClient getNewHttpClient() {
        try {
            SSLContextBuilder sslContextBuilder = SSLContextBuilder.create();
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContextBuilder.build(), NoopHostnameVerifier.INSTANCE);

            RequestConfig requestConfig = RequestConfig.custom()
                    .setSocketTimeout(120000)
                    .setConnectTimeout(120000)
                    .build();

            return HttpClients.custom()
                    .setSSLSocketFactory(sslSocketFactory)
                    .setDefaultRequestConfig(requestConfig)
                    .build();
            
        } catch (Exception e) {
            return HttpClients.createDefault();
        }
    }
    
}