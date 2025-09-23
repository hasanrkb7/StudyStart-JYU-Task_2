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
 * Servlet for handling student profile operations
 */
public class ProfileServlet extends HttpServlet {
    private SOAPClient soapClient;
    private ObjectMapper objectMapper;
    
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.soapClient = new SOAPClient();
            this.objectMapper = new ObjectMapper();
            System.out.println("ProfileServlet initialized successfully");
        } catch (Exception e) {
            System.err.println("Failed to initialize ProfileServlet: " + e.getMessage());
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
            String profileInfo = soapClient.getProfile(studentId);
            
            if (profileInfo.startsWith("Student not found")) {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                result.put("error", "Student not found");
                result.put("message", profileInfo);
            } else if (profileInfo.startsWith("Error")) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                result.put("error", "Service error");
                result.put("message", profileInfo);
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
                result.put("success", true);
                result.put("studentId", studentId);
                result.put("profileInfo", profileInfo);
            }
        }
        
        out.write(objectMapper.writeValueAsString(result));
        out.flush();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
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
            
            String firstName = (String) requestData.get("firstName");
            String lastName = (String) requestData.get("lastName");
            String email = (String) requestData.get("email");
            String program = (String) requestData.get("program");
            String nationality = (String) requestData.get("nationality");
            Integer yearOfStudy = (Integer) requestData.get("yearOfStudy");
            
            if (firstName == null || lastName == null || email == null || 
                program == null || nationality == null || yearOfStudy == null) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("error", "Missing required fields");
                result.put("required", "firstName, lastName, email, program, nationality, yearOfStudy");
            } else {
                String createResult = soapClient.createProfile(firstName, lastName, email, program, yearOfStudy, nationality);
                
                if (createResult.startsWith("Profile created successfully")) {
                    response.setStatus(HttpServletResponse.SC_CREATED);
                    result.put("success", true);
                    result.put("message", createResult);
                    
                    // Extract student ID from the result
                    String studentId = createResult.substring(createResult.lastIndexOf(": ") + 2);
                    result.put("studentId", studentId);
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    result.put("error", "Profile creation failed");
                    result.put("message", createResult);
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
            String email = (String) requestData.get("email");
            String program = (String) requestData.get("program");
            Integer yearOfStudy = (Integer) requestData.get("yearOfStudy");
            
            if (studentId == null || studentId.trim().isEmpty()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                result.put("error", "Student ID is required");
                result.put("message", "Please provide studentId in the request body");
            } else {
                int year = (yearOfStudy != null) ? yearOfStudy : 0;
                String updateResult = soapClient.updateProfile(studentId, email, program, year);
                
                if (updateResult.startsWith("Profile updated successfully")) {
                    response.setStatus(HttpServletResponse.SC_OK);
                    result.put("success", true);
                    result.put("message", updateResult);
                } else if (updateResult.contains("not found")) {
                    response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    result.put("error", "Student not found");
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