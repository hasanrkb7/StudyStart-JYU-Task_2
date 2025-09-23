package fi.jyu.ties4560.soap.service;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;

@WebService(name = "DocumentService", targetNamespace = "http://service.soap.ties4560.jyu.fi/")
public interface DocumentService {
    
    @WebMethod(operationName = "checkResidencePermit")
    @WebResult(name = "permitStatus")
    String checkResidencePermit(@WebParam(name = "studentId") String studentId);
    
    @WebMethod(operationName = "updateResidencePermit")
    @WebResult(name = "result")
    String updateResidencePermit(
        @WebParam(name = "studentId") String studentId,
        @WebParam(name = "expiryDate") String expiryDate
    );
}