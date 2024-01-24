//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.json;

import com.google.gson.annotations.SerializedName;

/**
 * Param defines a generic Admin API parameter which can be used as an {@link Input} or an {@link Output}
 * <p>
 * Example:
 * <pre>
 * "{@link Param}":{
 *   "{@link Param#Name Name}":"Certificate",
 *   "{@link Param#Value Value}":"",
 *   "{@link Param#Tip Tip}":"Certificate",
 *   "{@link Param#Type Type}":"Base64",
 *   "{@link Param#Encoding Encoding}":"Base64", 
 *   "{@link Param#MimeType MimeType}":"application/base64"
 * }
 * </pre>
 */
public class Param {
    
    /**
     * Name of the parameter.
     * <p>
     * JSON name: "Name"
     */
    @SerializedName("Name")
    public String Name;

    /**
     * Value of the parameter. 
     * Used in Request InputParams and Response OutputParams.
     * <p>
     * JSON name: "Value"
     */
    @SerializedName("Value")
    public String Value;        
    
    /**
     * Flag determining whether this parameter is mandatory or not.
     * Retrieved from UseCase or MSpec for DisplayUseCase UseCaseManagementResponses.
     * <p>
     * JSON name: "Required"
     */
    @SerializedName("Required")
    public Boolean Required;    
    
    /**
     * Description of the parameter.
     * Retrieved from UseCase or MSpec for DisplayUseCase UseCaseManagementResponses.
     * <p> This can be used for example for tooltips.
     * <p>
     * JSON name: "Tip"
     * @deprecated This produces too much spam
     */
    @Deprecated
    @SerializedName("Tip")
    public String Tip;    

    
    /**
     * Enumeration describing the parameter type.
     * <br> Normally defined in the XML UseCase.
     * <p> The JSON attribute is used for display purposes only.
     * <p><b>One of:</b>
     * <ul>
     *   <li>String
     *   <li>URI
     *   <li>Integer
     *   <li>Boolean
     *   <li>MeshMember
     *   <li>DateTime
     *   <li>Base64
     *   <li>JSON
     *   <li>AdditionalServices
     *   <li>Keyword
     *   <li>Flag
     * </ul>
     * 
     * <p>
     * JSON name: "Type"
     */
    @SerializedName("Type")
    public String Type;    
    
    
    /**
     * Content encoding of the parameter.
     * <p> The JSON attribute is only used for display purposes.
     * <p> Reserved for future use.
     * <p>
     * JSON name: "Encoding"
     */
    @SerializedName("Encoding")
    public String Encoding;    
    
    
    /**
     * MimeType of the parameter.
     * <p> The JSON attribute is only used for display purposes.
     * <p> Reserved for future use.
     * <p>
     * JSON name: "MimeType"
     */
    @SerializedName("MimeType")
    public String MimeType;

    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((this.Name == null) ? 0 : this.Name.hashCode());
        result = prime * result;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Param other = (Param) obj;
        if (this.Name == null) {
            if (other.Name != null)
                return false;
        } else if (!this.Name.equals(other.Name))
            return false;
        return true;
    }
    
    /**
     * Return a String representation of the value.
     * If the value is null, returns "".
     * @return {@link #Value} as String or "".
     */
    public String getStringValue() {
        if (this.Value == null) return "";
        return this.Value;
    }
    
    /**
     * Get param name 
     * @return name
     */
    public String getName() {
        return this.Name;
    }
    
    /**
     * Return an integer representation of the value.
     * If the value is null, or not a number, returns -1.
     * @return {@link #Value} as int or -1.
     */
    public int getIntValue() {
        try {
            return Integer.parseInt(this.getStringValue());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
    
    /**
     * Return an boolean representation of the value.
     * If the value is null, or not a boolean, returns false.
     * @return {@link #Value} as boolean.
     */
    public boolean getBooleanValue() {
        return Boolean.getBoolean(this.getStringValue());
    }
    
}
