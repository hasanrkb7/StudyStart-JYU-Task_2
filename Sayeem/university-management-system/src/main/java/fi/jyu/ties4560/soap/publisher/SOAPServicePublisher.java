package fi.jyu.ties4560.soap.publisher;

import fi.jyu.ties4560.soap.impl.DocumentServiceImpl;
import fi.jyu.ties4560.soap.impl.ProfileServiceImpl;
import javax.xml.ws.Endpoint;

public class SOAPServicePublisher {
    
    private static final String BASE_URL = "http://localhost:9090/";
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Starting SOAP Web Services ===");
            
            String profileServiceURL = BASE_URL + "ProfileService";
            Endpoint.publish(profileServiceURL, new ProfileServiceImpl());
            System.out.println("ProfileService published at: " + profileServiceURL);
            
            String documentServiceURL = BASE_URL + "DocumentService";
            Endpoint.publish(documentServiceURL, new DocumentServiceImpl());
            System.out.println("DocumentService published at: " + documentServiceURL);
            
            System.out.println("\n=== SOAP Services Running ===");
            System.out.println("WSDL URLs:");
            System.out.println("- " + profileServiceURL + "?wsdl");
            System.out.println("- " + documentServiceURL + "?wsdl");
            System.out.println("\nPress Ctrl+C to stop");
            
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}