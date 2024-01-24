//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * OutputParameter of a {@link UseCase}.
 * OutputParameter consists of multiple parameter attributes defined in {@link Param}.
 * <p>Used in:
 * <ul>
 *   <li>{@link UseCaseManagementResp} to identify UseCase return values 
 *   <li>{@link MSS_RegistrationResp} to return UseCase values
 * </ul>
 * 
 * <pre>
 *  "{@link Group}":
 *     {
 *      "{@link Group#Name Name}":"UserGroup",
 *      "{@link Group#Outputs OutputParams}":[
 *         {
 *           "{@link Output#Name Name}": "State",
 *           "{@link Output#Type Type}":"String",
 *           "{@link Output#Value Value}":"ACTIVE"
 *         },
 *         {
 *           "{@link Output#Name Name}": "MSISDN",
 *           "{@link Output#Type Type}":"String",
 *           "{@link Output#Value Value}":"+35847001001"
 *         }
 *       ]
 *     }
 * </pre>
 * JSON name: "Group".
 */
public class Group {
    
    /**
     * Name of the OutputParamGroup.
     * <p>
     * JSON name: "Name"
     */
    @SerializedName("Name")
    public String Name;
    
    @SerializedName("Outputs")
    public List<Output> Outputs;
    
}
