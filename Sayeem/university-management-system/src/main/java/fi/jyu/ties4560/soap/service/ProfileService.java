package fi.jyu.ties4560.soap.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "ProfileService", targetNamespace = "http://service.soap.ties4560.jyu.fi/")
public interface ProfileService {
    
    @WebMethod(operationName = "createProfile")
    @WebResult(name = "result")
    String createProfile(
        @WebParam(name = "firstName") String firstName,
        @WebParam(name = "lastName") String lastName,
        @WebParam(name = "email") String email,
        @WebParam(name = "program") String program,
        @WebParam(name = "yearOfStudy") int yearOfStudy,
        @WebParam(name = "nationality") String nationality
    );
    
    @WebMethod(operationName = "getProfile")
    @WebResult(name = "profileInfo")
    String getProfile(@WebParam(name = "studentId") String studentId);
    
    @WebMethod(operationName = "updateProfile")
    @WebResult(name = "result")
    String updateProfile(
        @WebParam(name = "studentId") String studentId,
        @WebParam(name = "email") String email,
        @WebParam(name = "program") String program,
        @WebParam(name = "yearOfStudy") int yearOfStudy
    );
}