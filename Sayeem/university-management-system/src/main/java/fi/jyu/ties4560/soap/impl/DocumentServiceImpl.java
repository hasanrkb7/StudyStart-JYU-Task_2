package fi.jyu.ties4560.soap.impl;

import fi.jyu.ties4560.model.Student;
import fi.jyu.ties4560.soap.service.DocumentService;
import javax.jws.WebService;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@WebService(
    serviceName = "DocumentService",
    portName = "DocumentServicePort",
    endpointInterface = "fi.jyu.ties4560.soap.service.DocumentService",
    targetNamespace = "http://service.soap.ties4560.jyu.fi/"
)
public class DocumentServiceImpl implements DocumentService {
    
    @Override
    public String checkResidencePermit(String studentId) {
        try {
            if (studentId == null || studentId.trim().isEmpty()) {
                return "Error: Student ID is required";
            }
            
            Student student = ProfileServiceImpl.getStudentById(studentId.trim());
            if (student == null) {
                return "Student not found with ID: " + studentId;
            }
            
            if (student.getNationality().equalsIgnoreCase("Finnish")) {
                return "Student is Finnish citizen - no residence permit required";
            }
            
            if (!student.isHasResidencePermit()) {
                return "No residence permit found for student: " + studentId;
            }
            
            LocalDate expiry = student.getResidencePermitExpiry();
            LocalDate today = LocalDate.now();
            
            if (expiry == null) {
                return "Residence permit expiry date not set for student: " + studentId;
            }
            
            if (expiry.isBefore(today)) {
                return "Residence permit EXPIRED on: " + expiry.format(DateTimeFormatter.ISO_LOCAL_DATE);
            } else if (expiry.isBefore(today.plusMonths(1))) {
                long daysUntilExpiry = java.time.temporal.ChronoUnit.DAYS.between(today, expiry);
                return "Residence permit EXPIRES SOON on: " + expiry.format(DateTimeFormatter.ISO_LOCAL_DATE) + 
                       " (In " + daysUntilExpiry + " days - Renewal required!)";
            } else {
                return "Residence permit is VALID until: " + expiry.format(DateTimeFormatter.ISO_LOCAL_DATE);
            }
            
        } catch (Exception e) {
            System.err.println("Error checking residence permit: " + e.getMessage());
            return "Error checking residence permit: " + e.getMessage();
        }
    }
    
    @Override
    public String updateResidencePermit(String studentId, String expiryDate) {
        try {
            if (studentId == null || studentId.trim().isEmpty()) {
                return "Error: Student ID is required";
            }
            
            if (expiryDate == null || expiryDate.trim().isEmpty()) {
                return "Error: Expiry date is required";
            }
            
            Student student = ProfileServiceImpl.getStudentById(studentId.trim());
            if (student == null) {
                return "Student not found with ID: " + studentId;
            }
            
            if (student.getNationality().equalsIgnoreCase("Finnish")) {
                return "Student is Finnish citizen - no residence permit update needed";
            }
            
            LocalDate newExpiry;
            try {
                newExpiry = LocalDate.parse(expiryDate.trim(), DateTimeFormatter.ISO_LOCAL_DATE);
            } catch (DateTimeParseException e) {
                return "Invalid date format. Please use YYYY-MM-DD format (e.g., 2025-12-31)";
            }
            
            if (newExpiry.isBefore(LocalDate.now())) {
                return "Error: New expiry date cannot be in the past";
            }
            
            student.setHasResidencePermit(true);
            student.setResidencePermitExpiry(newExpiry);
            
            System.out.println("Residence permit updated for student: " + studentId + 
                             " - New expiry: " + newExpiry);
            
            return "Residence permit updated successfully. New expiry date: " + 
                   newExpiry.format(DateTimeFormatter.ISO_LOCAL_DATE);
            
        } catch (Exception e) {
            System.err.println("Error updating residence permit: " + e.getMessage());
            return "Error updating residence permit: " + e.getMessage();
        }
    }
}