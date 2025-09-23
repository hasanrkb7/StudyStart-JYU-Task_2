package fi.jyu.ties4560.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import fi.jyu.ties4560.servlet.ProfileServlet;
import fi.jyu.ties4560.servlet.DocumentServlet;

public class UniversityWebServer {
    
    private static final int PORT = 9091;
    private static Server server;
    
    public static void main(String[] args) {
        try {
            System.out.println("=== Starting University Management System Web Server ===");
            
            server = new Server(PORT);
            
            ServletHandler handler = new ServletHandler();
            
            ServletHolder profileHolder = new ServletHolder(new ProfileServlet());
            handler.addServletWithMapping(profileHolder, "/api/profile");
            
            ServletHolder documentHolder = new ServletHolder(new DocumentServlet());
            handler.addServletWithMapping(documentHolder, "/api/document");
            
            server.setHandler(handler);
            
            server.start();
            
            System.out.println("✓ Server started successfully on port: " + PORT);
            System.out.println("✓ Base URL: http://localhost:" + PORT);
            
            printAPIDocumentation();
            
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    System.out.println("\n=== Shutting down web server ===");
                    if (server != null) {
                        server.stop();
                        System.out.println("✓ Web server stopped successfully");
                    }
                } catch (Exception e) {
                    System.err.println("Error during shutdown: " + e.getMessage());
                }
            }));
            
            System.out.println("\n=== Server is ready for requests ===");
            System.out.println("Press Ctrl+C to stop the server");
            
            server.join();
            
        } catch (Exception e) {
            System.err.println("Error starting server: " + e.getMessage());
            e.printStackTrace();
            
            try {
                if (server != null) {
                    server.stop();
                }
            } catch (Exception stopException) {
                System.err.println("Error stopping server: " + stopException.getMessage());
            }
            
            System.exit(1);
        }
    }
    
    private static void printAPIDocumentation() {
        System.out.println("\n=== Available API Endpoints ===");
        System.out.println("\nProfile Management:");
        System.out.println("  GET  /api/profile?studentId=<id>     - Get student profile information");
        System.out.println("  POST /api/profile                    - Create new student profile");
        System.out.println("  PUT  /api/profile                    - Update existing student profile");
        
        System.out.println("\nDocument Management:");
        System.out.println("  GET  /api/document?studentId=<id>    - Check residence permit status");
        System.out.println("  PUT  /api/document                   - Update residence permit expiry");
        
        System.out.println("\n=== Example Usage ===");
        System.out.println("\n1. Create Profile (POST /api/profile):");
        System.out.println("   Content-Type: application/json");
        System.out.println("   Body: {");
        System.out.println("     \"firstName\": \"John\",");
        System.out.println("     \"lastName\": \"Doe\",");
        System.out.println("     \"email\": \"john.doe@student.jyu.fi\",");
        System.out.println("     \"program\": \"Computer Science\",");
        System.out.println("     \"yearOfStudy\": 2,");
        System.out.println("     \"nationality\": \"German\"");
        System.out.println("   }");
        
        System.out.println("\n2. Get Profile (GET /api/profile?studentId=STU1001)");
        
        System.out.println("\n3. Update Profile (PUT /api/profile):");
        System.out.println("   Content-Type: application/json");
        System.out.println("   Body: {");
        System.out.println("     \"studentId\": \"STU1001\",");
        System.out.println("     \"email\": \"new.email@student.jyu.fi\",");
        System.out.println("     \"yearOfStudy\": 3");
        System.out.println("   }");
        
        System.out.println("\n4. Check Residence Permit (GET /api/document?studentId=STU1001)");
        
        System.out.println("\n5. Update Residence Permit (PUT /api/document):");
        System.out.println("   Content-Type: application/json");
        System.out.println("   Body: {");
        System.out.println("     \"studentId\": \"STU1001\",");
        System.out.println("     \"expiryDate\": \"2025-12-31\"");
        System.out.println("   }");
        
        System.out.println("\n=== Prerequisites ===");
        System.out.println("Make sure SOAP services are running on port 9090 before using these endpoints.");
        System.out.println("Start SOAP services: fi.jyu.ties4560.soap.publisher.SOAPServicePublisher");
    }
    
    public static void stopServer() {
        try {
            if (server != null && server.isStarted()) {
                server.stop();
                System.out.println("Server stopped programmatically");
            }
        } catch (Exception e) {
            System.err.println("Error stopping server: " + e.getMessage());
        }
    }
}