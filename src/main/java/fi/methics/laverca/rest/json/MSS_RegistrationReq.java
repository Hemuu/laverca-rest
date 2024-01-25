//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import com.google.gson.annotations.SerializedName;

/**
 * MSS_RegistrationReq is used for calling registration operations called {@link UseCase UseCases}.
 * A response datatype is {@link MSS_RegistrationResp}.
 * 
 * <p>MSS_RegistrationReq identifies:
 * <ul>
 *   <li> {@link User}
 *   <li> {@link UseCase}
 *   <li> UseCase {@link Input input parameters}
 * </ul>
 * 
 * The user of the request is identified in the optional
 * {@link User User} object or in the REST call by using HTTP authentication.
 * <p>
 * UseCases are documented in the <b>Kiuru MSSP 5.0 UseCase Specification</b>.
 * The document specifies UseCase Name and InputParams.
 * </ul>
 * <p>
 * <p><b>Example:</b>
 * <pre>
 * {
 *   "{@link MSS_RegistrationReq}":{
 *     "{@link User}": {
 *       "{@link User#Role Role}": "enduser"
 *     },
 *     "{@link UseCase}": {
 *       "{@link UseCase#Name Name}": "kiuru:CreateMobileUser",
 *       "{@link UseCase#Inputs InputParams}": [
 *         {
 *           "{@link Input#Name Name}": "MSISDN",
 *           "{@link Input#Value Value}": "+35847001001"
 *         },
 *         {
 *           "{@link Input#Name Name}": "SignatureProfile",
 *           "{@link Input#Value Value}": "http://sigprof"
 *         }
 *       ]
 *     }
 *   }
 * }
 * </pre>
 * <p>
 * UseCases can be used to handle the flow of multiple sequential MReg operations. 
 * Additionally a simple UseCase is generated for each MReg operation which allows calling any MReg operation using REST Admin.
 */
public class MSS_RegistrationReq extends MSS_AbstractMessage {
    
    @SerializedName("UseCase")
    public UseCase UseCase;
    
    /**
     * Default constructor
     */
    public MSS_RegistrationReq() {
        this.UseCase = new UseCase();
    }
    
    public MSS_RegistrationReq(String namespace, String name) {
        this.UseCase = new UseCase();
        this.UseCase.Name = name;
        this.UseCase.NameSpace = namespace;
    }
    
}
