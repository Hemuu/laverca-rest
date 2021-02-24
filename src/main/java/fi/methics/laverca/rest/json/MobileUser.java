//
//  (c) Copyright 2003-2020 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import com.google.gson.annotations.SerializedName;

public class MobileUser {
    
    /**
     * Default constructor
     */
    public MobileUser() {
        super();
    }
    
    public MobileUser(String msisdn) {
        this();
        this.MSISDN = msisdn;
    }
    
//    @SerializedName("IdentityIssuer")
//    public IdentityIssuer IdentityIssuer;
    
    @SerializedName("UserIdentifier")
    public String UserIdentifier;
    
//    @SerializedName("HomeMSSP")
//    public HomeMSSP HomeMSSP;
    
    @SerializedName("MSISDN")
    public String MSISDN;
    
}
