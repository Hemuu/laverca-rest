//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class UseCase {
    
    /**
     * Name of the UseCase. This is required for all requests and
     * returned by all responses containing the UseCase JSON object.
     * <p>
     * JSON name: "Name"
     */
    @SerializedName("Name")
    public String Name;
    
    /**
     * NameSpace of the UseCase.
     * <p>
     * JSON name: "NameSpace"
     */
    @SerializedName("NameSpace")
    public String NameSpace;
    
    /**
     * MReg operation SessionId
     * <p>
     * JSON name: "SessionId"
     */
    @SerializedName("SessionId")
    public String SessionId;
    
    /**
     * Input parameters for the UseCase.
     * 
     * <p><b>Inputs are used for:</b>
     * <ul>
     *   <li> Delivering parameter values to UseCase in MSS_RegistrationReq
     *   <li> Displaying possible Inputs for Admin operation DisplayUseCase
     * </ul>
     * 
     * <p><b>Example:</b>
     * <pre>
     * "Inputs": [
     *   {
     *     "Name": "ApID",
     *     "Value": "http://test1_ap"
     *   }
     * ]
     * </pre>
     * JSON name: "Inputs"
     */
    @SerializedName("Inputs")
    public List<Input> Inputs;    

    /**
     * Grouped OutputParameters of the UseCase in a response.
     * <p>Contains list entries that wrap multiple Outputs. 
     * 
     * <p><b>Groups are used for:</b>
     * <ul>
     *   <li> Delivering grouped return values in MSS_RegistrationResp
     * </ul>
     * 
     * <p>The example below lists two entries (users) and their State and MSISDN.
     * 
     * <p><b>Example:</b>
     * <pre>
     * "Groups":[
     *   {
     *     "{@link Group#Name Name}":"UserGroup",
     *     "{@link Group#Outputs Outputs}":[
     *       {
     *         "Name": "State",
     *         "Type":"String",
     *         "Value":"ACTIVE"
     *       },
     *       {
     *         "Name": "MSISDN",
     *         "Type":"String",
     *         "Value":"+35847001001"
     *       }
     *     ]
     *   },
     *   {
     *     "{@link Group#Name Name}":"UserGroup",
     *     "{@link Group#Outputs Outputs}":[
     *       {
     *         "Name": "State"
     *         "Type":"String",
     *         "Value":"ACTIVE"
     *       },
     *       {
     *         "Name": "MSISDN"
     *         "Type":"String",
     *         "Value":"+35847001001"
     *       }
     *     ]
     *   }
     * ]
     * </pre>
     * <p>
     * JSON name: "Groups"
     */
    @SerializedName("Groups")
    public List<Group> Groups;
    
    /**
     * Outputs for the UseCase in a request or a response
     * 
     * <p><b>Outputs are used for:</b>
     * <ul>
     *   <li> Delivering return values in MSS_RegistrationResp
     *   <li> Displaying possible Outputs for Admin operation DisplayUseCase
     * </ul>
     * 
     * <p><b> OutputParam processing rules:</b>
     * <ul>
     *   <li>If used in a request, works as a filter: only the given Outputs will be returned in the Response. 
     *   <li>If left empty on the request, all Outputs will be returned.
     * </ul>
     * 
     * <p><b>Example:</b>
     * <pre>
     * "Outputs":[
     *   {
     *     "Name": "IteratorId"
     *     "Type":"String",
     *     "Value": "12A"
     *   },
     *   {
     *     "Name": "Count",
     *     "Type": "String",
     *     "Value": "35"
     *   }
     * ]
     * </pre>
     * JSON name: "Outputs"
     */
    @SerializedName("Outputs")
    public List<Output> Outputs;
    
}
