package uni.portal.soap;

import uni.portal.model.Profile;
import uni.portal.store.DataStore;
import javax.jws.WebMethod;
import javax.jws.WebService;

@WebService(targetNamespace = "http://intl/portal/profile/update")
public class UpdateProfileService {
    @WebMethod
    public String updateProfile(int studentId, String newName, String newEmail,
                                String newCountry, String newFaculty) {
        Profile p = DataStore.PROFILES.get(studentId);
        if (p == null) {
            p = new Profile(studentId,
                    (newName!=null && !newName.isEmpty()? newName : "unknown"),
                    (newEmail!=null && !newEmail.isEmpty()? newEmail : "unknown"),
                    (newCountry!=null && !newCountry.isEmpty()? newCountry : "unknown"),
                    (newFaculty!=null && !newFaculty.isEmpty()? newFaculty : "unknown"));
            DataStore.PROFILES.put(studentId, p);
            return "created";
        }
        if (newName!=null && !newName.isEmpty())     p.setName(newName);
        if (newEmail!=null && !newEmail.isEmpty())   p.setEmail(newEmail);
        if (newCountry!=null && !newCountry.isEmpty()) p.setCountry(newCountry);
        if (newFaculty!=null && !newFaculty.isEmpty()) p.setFaculty(newFaculty);
        return "updated";
    }

}
