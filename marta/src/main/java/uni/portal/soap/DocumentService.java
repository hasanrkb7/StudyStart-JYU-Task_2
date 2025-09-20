package uni.portal.soap;

import uni.portal.model.PermitStatus;
import uni.portal.store.DataStore;
import javax.jws.WebMethod;
import javax.jws.WebService;
import java.time.LocalDate;

@WebService(targetNamespace = "http://intl/portal/document")
public class DocumentService {
    @WebMethod
    public PermitStatus checkResidencePermit(int studentId){
        LocalDate until = DataStore.PERMITS.get(studentId);
        if (until == null) return new PermitStatus(false, null);
        boolean ok = !until.isBefore(LocalDate.now());
        return new PermitStatus(ok, until.toString());
    }
}
