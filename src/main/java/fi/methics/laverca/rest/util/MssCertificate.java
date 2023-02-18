//
//  (c) Copyright 2003-2023 Methics Oy. All rights reserved.
//
package fi.methics.laverca.rest.util;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * End-user certificate object.
 * <p>Contains both the chain and the end-user certificate.
 * <p>Additionally can be used to check which SignatureProfiles use this certificate.
 */
public class MssCertificate {

    private X509Certificate        cert;
    private List<X509Certificate>  chain;
    private List<SignatureProfile> sigprofs;
    
    public static final MssCertificate EMPTY = new MssCertificate();
    
    /**
     * Create a new MssCertificate from a JSON response
     * @param chain CertificateChain
     * @return new MssCertificate
     */
    public static MssCertificate fromJson(final List<String> chain) {
        MssCertificate cert = new MssCertificate();
        if (chain == null || chain.isEmpty()) {
            cert.cert  = null;
            cert.chain = new ArrayList<>();
        } else {
            cert.cert  = X509Util.parseCertificate(chain.get(0));
            cert.chain = chain.stream().map(c -> X509Util.parseCertificate(c)).collect(Collectors.toList());
        }
        return cert;
    }
    
    private MssCertificate() {
        this.sigprofs = new ArrayList<>();
    }
    
    /**
     * Create a new MssCertificate from a list of X509Certificates
     * @param chain CertificateChain
     * @return new MssCertificate
     */
    public MssCertificate(final List<X509Certificate> chain) {
        if (chain == null || chain.isEmpty()) {
            this.cert  = null;
            this.chain = new ArrayList<>();
        } else {
            this.cert  = chain.get(0);
            this.chain = chain;
        }
        this.sigprofs = new ArrayList<>();
    }
    
    /**
     * Add related SignatureProfiles
     * @param sigprofs SignatureProfiles
     */
    public void addSignatureProfiles(List<String> sigprofs) {
        if (sigprofs == null) return;
        this.sigprofs = new ArrayList<>(sigprofs.stream().map(s -> SignatureProfile.of(s)).collect(Collectors.toList()));
    }
    
    /**
     * Add a new related SignatureProfile
     * @param prof SignatureProfile
     */
    public void addSignatureProfile(String prof) {
        if (prof == null) return;
        this.sigprofs.add(SignatureProfile.of(prof));
    }
    
    /**
     * Get related SignatureProfiles
     * @return SignatureProfiles
     */
    public List<SignatureProfile> getSignatureProfiles() {
        return this.sigprofs;
    }
    
    /**
     * Get end-user certificate
     * @return certificate
     */
    public X509Certificate getCertificate() {
        return this.cert;
    }
    
    /**
     * Get Certificate chain
     * @return chain
     */
    public List<X509Certificate> getCertificateChain() {
        return this.chain;
    }
    
}
