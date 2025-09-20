package uni.portal.model;
import java.io.Serializable;

public class PermitStatus implements Serializable {
    private boolean valid; private String validUntil; // yyyy-MM-dd
    public PermitStatus() {}
    public PermitStatus(boolean valid, String validUntil){ this.valid=valid; this.validUntil=validUntil; }
    public boolean isValid(){ return valid; } public void setValid(boolean v){ this.valid=v; }
    public String getValidUntil(){ return validUntil; } public void setValidUntil(String s){ this.validUntil=s; }
}
