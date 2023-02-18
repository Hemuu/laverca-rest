//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.util;

import java.util.Objects;

/**
 * Wrapper class for SignatureProfile URI
 */
public class SignatureProfile {

    // Common profiles
    public static final String URI_AUTHN   = "http://alauda.mobi/digitalSignature";
    public static final String URI_SIGNING = "http://alauda.mobi/nonRepudiation";
    
    public String uri;
    
    /**
     * Make a SignatureProfile out of given URI
     * @param uri URI
     * @return SignatureProfile object
     */
    public static SignatureProfile of(final String uri) {
        return new SignatureProfile(uri);
    }
    
    /**
     * Create a new SignatureProfile
     * @param uri SignatureProfile URI
     */
    public SignatureProfile(final String uri) {
        this.uri = uri;
    }
    
    /**
     * Get the SignatureProfile URI
     * @return URI
     */
    public String getUri() {
        return this.uri;
    }
    
    /**
     * Is this SignatureProfile null?
     * @return true for null profile
     */
    public boolean isNull() {
        return this.uri == null;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.uri);
    }
    
    /**
     * Check if this SignatureProfile matches given one
     * @param sigprof SignatureProfile URI
     * @return true if this SignatureProfile object matches the given URI
     */
    public boolean matches(final String sigprof) {
        return (Objects.equals(this.uri, sigprof));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        SignatureProfile other = (SignatureProfile) obj;
        return Objects.equals(this.uri, other.uri);
    }
    
}
