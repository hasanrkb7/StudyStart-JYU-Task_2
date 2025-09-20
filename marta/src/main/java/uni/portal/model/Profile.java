package uni.portal.model;
import java.io.Serializable;

public class Profile implements Serializable {
    private int id; private String name; private String email; private String country; private String faculty;
    public Profile() {}
    public Profile(int id, String name, String email, String country, String faculty){
        this.id=id; this.name=name; this.email=email; this.country=country; this.faculty=faculty;
    }
    public int getId(){ return id; } public void setId(int id){ this.id=id; }
    public String getName(){ return name; } public void setName(String name){ this.name=name; }
    public String getEmail(){ return email; } public void setEmail(String email){ this.email=email; }
    public String getCountry(){ return country; } public void setCountry(String country){ this.country=country; }
    public String getFaculty(){ return faculty; } public void setFaculty(String faculty){ this.faculty=faculty; }
}
