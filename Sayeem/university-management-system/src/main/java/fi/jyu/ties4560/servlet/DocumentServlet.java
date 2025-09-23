package fi.jyu.ties4560.servlet;

import fi.jyu.ties4560.client.SOAPClient;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Servlet for handling document operations (residence permits)
 */
public class DocumentServlet extends HttpServlet {
    private SOAPClient soapClient;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.soapClient = new SOAPClient();
            this.objectMapper = new ObjectMapper();
            System.out.println("DocumentServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize DocumentServlet: " + e.getMessage());
            throw new ServletException("Failed to initialize SOAP client", e);
        }
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String studentId = request.getParameter("studentId");
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        if (studentId == null || studentId.trim().isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            result.put("error", "Student ID is required");
            result.put("message", "Please provide studentId parameter: ?studentId=<id>");
        } else {
            String permitStatus = soapClient.checkResidencePermit(studentId);
            
            if (permitStatus.startsWith("Student not found")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("error", "Student not found");
                result.put("message", permitStatus);
            } else if (permitStatus.startsWith("Error")) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                result.put("error", "Service error");
                result.put("message", permitStatus);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                result.put("success", true);
                result.put("studentId", studentId);
                result.put("permitStatus", permitStatus);
                
                // Add status categorization
                if (permitStatus.contains("EXPIRED")) {
                    result.put("status", "EXPIRED");
                    result.put("urgent", true);
                } else if (permitStatus.contains("EXPIRES SOON")) {
                    result.put("status", "EXPIRES_SOON");
                    result.put("urgent", true);
                } else if (permitStatus.contains("VALID")) {
                    result.put("status", "VALID");
                    result.put("urgent", false);
                } else if (permitStatus.contains("Finnish citizen")) {
                    result.put("status", "NOT_REQUIRED");
                    result.put("urgent", false);
                } else {
                    result.put("status", "UNKNOWN");
                    result.put("urgent", false);
                }
            }
        }
        
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
    }
    
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        Map<String, Object> result = new HashMap<>();
        
        try {
            String requestBody = request.getReader().lines().collect(Collectors.joining());
            
            if (requestBody.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("error", "Empty request body");
                result.put("message", "JSON request body is required");
                out.write(objectMapper.writeValueAsString(result));
                return;
            }
            
            Map<String, Object> requestData = objectMapper.readValue(requestBody, Map.class);
            
            String studentId = (String) requestData.get("studentId");
            String expiryDate = (String) requestData.get("expiryDate");
            
            if (studentId == null || studentId.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("error", "Student ID is required");
                result.put("message", "Please provide studentId in the request body");
            } else if (expiryDate == null || expiryDate.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("error", "Expiry date is required");
                result.put("message", "Please provide expiryDate in YYYY-MM-DD format");
            } else {
                String updateResult = soapClient.updateResidencePermit(studentId, expiryDate);
                
                if (updateResult.startsWith("Residence permit updated successfully")) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    result.put("success", true);
                    result.put("message", updateResult);
                } else if (updateResult.contains("not found")) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("error", "Student not found");
                    result.put("message", updateResult);
                } else if (updateResult.contains("Invalid date format")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.put("error", "Invalid date format");
                    result.put("message", updateResult);
                } else if (updateResult.contains("Finnish citizen")) {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.put("error", "Not applicable");
                    result.put("message", updateResult);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.put("error", "Update failed");
                    result.put("message", updateResult);
                }
            }
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            result.put("error", "Internal server error");
            result.put("message", e.getMessage());
        }
        
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
    }
}