//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

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
    "{@link Output}":
       {
         "{@link Param#Name Name}":"ApID",
         "{@link Param#Value Value}":"http://test1_ap",
       }
 * </pre>
 * JSON name: "Output".
 */
public class Output extends Param {

    @Override
    public String toString() {
        return "OutputParam [Name=" + this.Name + 
               ", Value=" + this.Value + ", Required=" + this.Required + "]";
    }

}
