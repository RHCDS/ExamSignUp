package cn.hdu.examsignup.model;

import java.util.HashSet;
import java.util.Set;

import org.directwebremoting.annotations.DataTransferObject;

@DataTransferObject
public class ExArrangeSupervisor implements java.io.Serializable {
	
	private String id;
    private ExInstitution exInstitution;
    private ExSupervisor exSupervisor;
    private ExArrangement exArrangement;
    private String logicExamroomNum;
    
    public ExArrangeSupervisor() {
    }
    public ExArrangeSupervisor(String id) {
        this.id = id;
    }
    public ExArrangeSupervisor(String id, ExInstitution exInstitution, ExArrangement exArrangement,ExSupervisor exSupervisor, String logicExamroomNum) {
       this.id = id;
       this.exInstitution = exInstitution;
       this.exSupervisor=exSupervisor;
       this.exArrangement=exArrangement;
       this.logicExamroomNum=logicExamroomNum;
    }
    
    public String getId(){
    	return this.id;
    }
    public void setId(String id){
    	this.id=id;
    }
    
    public ExInstitution getExInstitution(){
    	return this.exInstitution;
    }
    public void setExInstitution(ExInstitution exInstitution){
    	this.exInstitution=exInstitution;
    }
    
    public ExSupervisor getExSupervisor(){
    	return this.exSupervisor;
    }
    public void setExSupervisor(ExSupervisor exSupervisor){
    	this.exSupervisor=exSupervisor;
    }
    
    public ExArrangement getExArrangement(){
    	return this.exArrangement;
    }
    public void setExArrangement(ExArrangement exArrangement){
    	this.exArrangement=exArrangement;
    }
    
    public String getLogicExamroomNum(){
    	return this.logicExamroomNum;
    }
    public void setLogicExamroomNum(String logicExamroomNum){
    	this.logicExamroomNum=logicExamroomNum;
    }
}
