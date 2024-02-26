//
//  (c) Copyright 2003-2024 Methics Oy. All rights reserved. 
//
package fi.methics.laverca.rest.json;

import com.google.gson.annotations.SerializedName;

/**
 * MSS AdditionalService that can be used to amend the request
 */
public class AdditionalService {
    
    public static final AdditionalService VALIDATION_AS = new AdditionalService("http://uri.etsi.org/TS102204/v1.1.2#validate");
    public static final String BATCH_AS = "http://www.methics.fi/KiuruMSSP/v5.0.0#batchsign";

    /**
     * Default constructor
     */
    public AdditionalService() {
        // Do nothing
    }
    
    /**
     * Construct an AdditionalServices object with the given description
     * @param description
     */
    public AdditionalService(final String description) {
        this.Description = description;
    }
    
    @SerializedName("Description")
    public String Description; // URI controlling parse of the rest of the data

}
