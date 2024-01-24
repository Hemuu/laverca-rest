//
// (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.SerializedName;

public class MSS_RegistrationResp extends MSS_AbstractMessage {
    
    /**
     * UseCase execution result. 
     * <p>Contains all {@link Output OutputParams} received from the UseCase execution.
     * <p>See {@link UseCase} for more information.
     * <p>
     * JSON name: "UseCase"
     */
    @SerializedName("UseCase")
    public UseCase UseCase;
    
    /**
     * Status of the response
     * <p><b>Contains:</b>
     * <ul>
     *   <li> {@link Status#StatusCode StatusCode} - <i>String</i> value of the status code.
     *   <li> {@link Status#StatusMessage StatusMessage} - <i>String</i> containing operation status indication message.
     *   <li> {@link Status#StatusDetail StatusDetail} - <i>String</i> containing a human readable message describing what happened.
     * </ul>
     * 
     * <p><b>Example:</b>
     * <pre>
     * "{@link Status}": {
     *   "{@link Status#StatusCode StatusCode}": {
     *     "Value": "100"
     *   },
     *   "{@link Status#StatusMessage StatusMessage}": "OK"
     *   "{@link Status#StatusDetail StatusDetail}": {
     *     "StatusDetail": "Operation Succeeded"
     *   }
     * }
     * </pre>
     * JSON name: "Status"
     */
    @SerializedName("Status")
    public Status Status;    

    
    /**
     * Check if this was a successful registration response
     * @return true if StatusCode was 100
     */
    public boolean isSuccessful() {
        return "100".equals(this. getStatusCode());
    }
    
    /**
     * Get the MReg StatusCode
     * @return
     */
    public String getStatusCode() {
        if (this.Status == null) return null;
        if (this.Status.StatusCode == null) return null;
        return this.Status.StatusCode.Value;
    }
    
    /**
     * Get a list of ungrouped output values
     * @return output values
     */
    public List<Output> getOutputs() {
        if (this.UseCase == null) return Collections.emptyList();
        return this.UseCase.Outputs;
    }
    
    /**
     * Get a list of all value groups
     * @return groups
     */
    public List<Group> getGroups() {
        if (this.UseCase == null) return Collections.emptyList();
        return this.UseCase.Groups;
    }
    
}
