//
//  (c) Copyright 2003-2020 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MSS_SignatureResp extends MSS_AbstractMessage {

    @SerializedName("MSSP_TransID")
    public String MSSP_TransID;
    
    @SerializedName("MobileUser")
    public MobileUser MobileUser;

    @SerializedName("MSS_Signature")
    public MSS_Signature MSS_Signature;

    @SerializedName("SignatureProfile")
    public String SignatureProfile;
    
    @SerializedName("Status")
    public Status Status;

    @SerializedName("ServiceResponses")
    public List<ServiceResponses> ServiceResponses;
    
}
