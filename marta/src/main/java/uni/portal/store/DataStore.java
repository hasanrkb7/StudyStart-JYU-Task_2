package uni.portal.store;

import uni.portal.model.Profile;
import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class DataStore {
    private DataStore(){}
    public static final Map<Integer, Profile> PROFILES = new ConcurrentHashMap<>();
    public static final Map<Integer, LocalDate> PERMITS = new ConcurrentHashMap<>();
    static {
        PROFILES.put(111, new Profile(111,"Marta","marta@jyu.fi","Ukraine","Computer Science"));
        PROFILES.put(222, new Profile( 222,"Kateryna","katya@jyu.fi","Ukraine","Software Engineering"));
        PROFILES.put(333, new Profile(333,"Sayeem","sayeem@jyu.fi","Bangladesh","AI"));
        PROFILES.put(444, new Profile(444,"Remon","remon@jyu.fi","Bangladesh","Computer Science"));
        PROFILES.put(555, new Profile(555,"Islam","islam@jyu.fi","Bangladesh","Software Engineering"));
        PERMITS.put(111, LocalDate.now().plusMonths(9));
        PERMITS.put(222, LocalDate.now().minusDays(10));
        PERMITS.put(333, LocalDate.now().plusMonths(1));
        PERMITS.put(444, LocalDate.now().minusDays(20));
        PERMITS.put(555, LocalDate.now().plusMonths(8));
    }
}
