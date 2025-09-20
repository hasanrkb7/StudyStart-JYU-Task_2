package uni.portal.soap;

import uni.portal.model.Profile;
import uni.portal.store.DataStore;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://intl/portal/profile")
public class GetProfileService {
    @WebMethod
    public Profile getProfile(int studentId){
        Profile p = DataStore.PROFILES.get(studentId);
        if (p == null){
            Profile e = new Profile(); e.setId(studentId);
            e.setName("unknown"); e.setEmail("unknown"); e.setCountry("unknown"); e.setFaculty("unknown");
            return e;
        }
        return p;
    }
}
