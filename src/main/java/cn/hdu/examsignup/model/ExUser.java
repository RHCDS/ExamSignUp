package cn.hdu.examsignup.model;

import org.directwebremoting.annotations.DataTransferObject;
// Generated 2012-3-26 19:37:27 by Hibernate Tools 3.4.0.CR1



/**
 * ExUser generated by hbm2java
 */
@DataTransferObject
public class ExUser  implements java.io.Serializable {


     private String id;
     private ExRole exRole;
     private ExInstitution exInstitution;
     private String loginname;
     private String password;
     private String name;
     private String contactnum;

    public ExUser() {
    }

	
    public ExUser(String id) {
        this.id = id;
    }
    public ExUser(String id, ExRole exRole, ExInstitution exInstitution, String loginname, String password, String name, String contactnum) {
       this.id = id;
       this.exRole = exRole;
       this.exInstitution = exInstitution;
       this.loginname = loginname;
       this.password = password;
       this.name = name;
       this.contactnum = contactnum;
    }
   
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    public ExRole getExRole() {
        return this.exRole;
    }
    
    public void setExRole(ExRole exRole) {
        this.exRole = exRole;
    }
    public ExInstitution getExInstitution() {
        return this.exInstitution;
    }
    
    public void setExInstitution(ExInstitution exInstitution) {
        this.exInstitution = exInstitution;
    }
    public String getLoginname() {
        return this.loginname;
    }
    
    public void setLoginname(String loginname) {
        this.loginname = loginname;
    }
    public String getPassword() {
        return this.password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    public String getContactnum() {
        return this.contactnum;
    }
    
    public void setContactnum(String contactnum) {
        this.contactnum = contactnum;
    }




}

