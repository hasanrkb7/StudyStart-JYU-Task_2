package fi.jyu.ties4560.client;

import fi.jyu.ties4560.soap.service.ProfileService;
import fi.jyu.ties4560.soap.service.DocumentService;

import javax.xml.ws.Service;
import javax.xml.namespace.QName;
import java.net.URL;

/**
 * SOAP Client for connecting to SOAP Web Services
 */
public class SOAPClient {
    private ProfileService profileService;
    private DocumentService documentService;
    
    public SOAPClient() {
        try {
            // Initialize ProfileService client
            URL profileUrl = new URL("http://localhost:9090/ProfileService?wsdl");
            QName profileQName = new QName("http://service.soap.ties4560.jyu.fi/", "ProfileService");
            Service profileSvc = Service.create(profileUrl, profileQName);
            profileService = profileSvc.getPort(ProfileService.class);
            
            // Initialize DocumentService client
            URL documentUrl = new URL("http://localhost:9090/DocumentService?wsdl");
            QName documentQName = new QName("http://service.soap.ties4560.jyu.fi/", "DocumentService");
            Service documentSvc = Service.create(documentUrl, documentQName);
            documentService = documentSvc.getPort(DocumentService.class);
            
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize SOAP clients", e);
        }
    }
    
    public String createProfile(String firstName, String lastName, String email, 
                              String program, int yearOfStudy, String nationality) {
        return profileService.createProfile(firstName, lastName, email, program, yearOfStudy, nationality);
    }
    
    public String getProfile(String studentId) {
        return profileService.getProfile(studentId);
    }
    
    public String updateProfile(String studentId, String email, String program, int yearOfStudy) {
        return profileService.updateProfile(studentId, email, program, yearOfStudy);
    }
    
    public String checkResidencePermit(String studentId) {
        return documentService.checkResidencePermit(studentId);
    }
    
    public String updateResidencePermit(String studentId, String expiryDate) {
        return documentService.updateResidencePermit(studentId, expiryDate);
    }
}