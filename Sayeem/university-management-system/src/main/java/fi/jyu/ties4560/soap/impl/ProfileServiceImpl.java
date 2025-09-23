package fi.jyu.ties4560.soap.impl;

import fi.jyu.ties4560.model.Student;
import fi.jyu.ties4560.soap.service.ProfileService;
import javax.jws.WebService;
import java.time.LocalDate;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@WebService(
    serviceName = "ProfileService",
    portName = "ProfileServicePort", 
    endpointInterface = "fi.jyu.ties4560.soap.service.ProfileService",
    targetNamespace = "http://service.soap.ties4560.jyu.fi/"
)
public class ProfileServiceImpl implements ProfileService {
    
    private static final ConcurrentHashMap<String, Student> studentDatabase = new ConcurrentHashMap<>();
    private static final AtomicInteger idCounter = new AtomicInteger(1000);
    
    @Override
    public String createProfile(String firstName, String lastName, String email, 
                              String program, int yearOfStudy, String nationality) {
        try {
            if (firstName == null || firstName.trim().isEmpty()) {
                return "Error: First name is required";
            }
            if (lastName == null || lastName.trim().isEmpty()) {
                return "Error: Last name is required";
            }
            if (email == null || email.trim().isEmpty()) {
                return "Error: Email is required";
            }
            if (program == null || program.trim().isEmpty()) {
                return "Error: Program is required";
            }
            if (nationality == null || nationality.trim().isEmpty()) {
                return "Error: Nationality is required";
            }
            if (yearOfStudy < 1 || yearOfStudy > 10) {
                return "Error: Year of study must be between 1 and 10";
            }
            
            String studentId = "STU" + idCounter.incrementAndGet();
            Student student = new Student(studentId, firstName.trim(), lastName.trim(), 
                                        email.trim(), program.trim(), yearOfStudy, nationality.trim());
            
            if (!nationality.equalsIgnoreCase("Finnish")) {
                student.setHasResidencePermit(true);
                student.setResidencePermitExpiry(LocalDate.now().plusYears(1));
            }
            
            studentDatabase.put(studentId, student);
            System.out.println("Profile created: " + student.toString());
            return "Profile created successfully with ID: " + studentId;
            
        } catch (Exception e) {
            System.err.println("Error creating profile: " + e.getMessage());
            return "Error creating profile: " + e.getMessage();
        }
    }
    
    @Override
    public String getProfile(String studentId) {
        try {
            if (studentId == null || studentId.trim().isEmpty()) {
                return "Error: Student ID is required";
            }
            
            Student student = studentDatabase.get(studentId.trim());
            if (student == null) {
                return "Student not found with ID: " + studentId;
            }
            
            return String.format(
                "ID: %s, Name: %s %s, Email: %s, Program: %s, Year: %d, Nationality: %s",
                student.getStudentId(),
                student.getFirstName(),
                student.getLastName(),
                student.getEmail(),
                student.getProgram(),
                student.getYearOfStudy(),
                student.getNationality()
            );
                    
        } catch (Exception e) {
            System.err.println("Error retrieving profile: " + e.getMessage());
            return "Error retrieving profile: " + e.getMessage();
        }
    }
    
    @Override
    public String updateProfile(String studentId, String email, String program, int yearOfStudy) {
        try {
            if (studentId == null || studentId.trim().isEmpty()) {
                return "Error: Student ID is required";
            }
            
            Student student = studentDatabase.get(studentId.trim());
            if (student == null) {
                return "Student not found with ID: " + studentId;
            }
            
            boolean updated = false;
            
            if (email != null && !email.trim().isEmpty()) {
                student.setEmail(email.trim());
                updated = true;
            }
            
            if (program != null && !program.trim().isEmpty()) {
                student.setProgram(program.trim());
                updated = true;
            }
            
            if (yearOfStudy > 0 && yearOfStudy <= 10) {
                student.setYearOfStudy(yearOfStudy);
                updated = true;
            }
            
            if (!updated) {
                return "No valid fields provided for update";
            }
            
            studentDatabase.put(studentId.trim(), student);
            System.out.println("Profile updated: " + student.toString());
            return "Profile updated successfully for student: " + studentId;
            
        } catch (Exception e) {
            System.err.println("Error updating profile: " + e.getMessage());
            return "Error updating profile: " + e.getMessage();
        }
    }
    
    public static Student getStudentById(String studentId) {
        return studentDatabase.get(studentId);
    }
}