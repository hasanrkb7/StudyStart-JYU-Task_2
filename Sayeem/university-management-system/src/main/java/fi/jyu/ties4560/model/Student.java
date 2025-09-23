package fi.jyu.ties4560.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Student implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String studentId;
    private String firstName;
    private String lastName;
    private String email;
    private String program;
    private int yearOfStudy;
    private String nationality;
    private boolean hasResidencePermit;
    private LocalDate residencePermitExpiry;
    
    public Student() {}
    
    public Student(String studentId, String firstName, String lastName, 
                  String email, String program, int yearOfStudy, String nationality) {
        this.studentId = studentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.program = program;
        this.yearOfStudy = yearOfStudy;
        this.nationality = nationality;
        this.hasResidencePermit = !nationality.equalsIgnoreCase("Finnish");
        if (this.hasResidencePermit) {
            this.residencePermitExpiry = LocalDate.now().plusYears(1);
        }
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getProgram() { return program; }
    public void setProgram(String program) { this.program = program; }
    
    public int getYearOfStudy() { return yearOfStudy; }
    public void setYearOfStudy(int yearOfStudy) { this.yearOfStudy = yearOfStudy; }
    
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    
    public boolean isHasResidencePermit() { return hasResidencePermit; }
    public void setHasResidencePermit(boolean hasResidencePermit) { this.hasResidencePermit = hasResidencePermit; }
    
    public LocalDate getResidencePermitExpiry() { return residencePermitExpiry; }
    public void setResidencePermitExpiry(LocalDate residencePermitExpiry) { this.residencePermitExpiry = residencePermitExpiry; }
}