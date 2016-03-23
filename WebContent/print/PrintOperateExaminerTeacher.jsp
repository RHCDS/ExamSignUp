<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
		<link rel="stylesheet" type="text/css" href="../resources/ext/resources/css/ext-all.css" />
		<script type="text/javascript" src="../resources/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="../resources/ext/ext-all.js"></script>
		<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
 		<script type='text/javascript' src='/ExamSignUp/dwr/interface/StudentController.js'></script>
		<script type="text/javascript" src="../resources/custom/DwrProxy.js"></script>
		<script type="text/javascript" src="../resources/js/PrintOperateExaminerTeacher.js"></script>
		<script type="text/javascript" src="./LodopFuncs.js"></script>
		<style type="text/css">
.icon-grid {
	background-image: url(../resources/icons/grid.gif) !important;
}

.add {
	background-image: url(../resources/icons/add.gif) !important;
}

.edit {
	background-image: url(../resources/icons/edit.gif) !important;
}

.remove {
	background-image: url(../resources/icons/delete.gif) !important;
}

.save {
	background-image: url(../resources/icons/save.gif) !important;
}

.back {
	background-image: url(../resources/icons/back.gif) !important;
}

.refresh {
	background-image: url(../resources/icons/refresh.gif) !important;
}

.print {
	background-image: url(../resources/icons/printer.gif) !important;
}
.upload-icon {
	background: url(../resources/icons/image_add.png) no-repeat 0 0
		!important;
}

.dIcon {
	background-image: url(../resources/icons/data.gif) !important;
}

.lIcon {
	background-image: url(../images/database_table.png) !important;
}

.pIcon {
	background-image: url(../images/house.png) !important;
}
</style>
</head>
<body>
<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript">
var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	
	
var printParamArray=new Array();
function initprintParamArray(){
	//top,left,width,height,fontsize,alignment,blod
	printParamArray[0]=new Array("29px","20px","700px","150px",25,2,1);//浙江省高校计算机等级考试理论监考教师名单
	printParamArray[1]=new Array("80px","-25px","800px","900px",12,2,0);//table
	
	
}

function reSizePrintParam(){
	
	for(var i=0;i<printParamArray.length;i++){						
			printParamArray[i][0]=LODOP.GET_VALUE("ItemTop",i+1).replace(/\"/g,"");
			printParamArray[i][1]=LODOP.GET_VALUE("ItemLeft",i+1).replace(/\"/g,"");
			printParamArray[i][2]=LODOP.GET_VALUE("ItemWidth",i+1).replace(/\"/g,"");
			printParamArray[i][3]=LODOP.GET_VALUE("ItemHeight",i+1).replace(/\"/g,"");
			printParamArray[i][4]=LODOP.GET_VALUE("ItemFontSize",i+1);
			printParamArray[i][5]=LODOP.GET_VALUE("ItemAlign",i+1);
			printParamArray[i][6]=LODOP.GET_VALUE("Itembold",i+1);				
		}
							
}

function CreatePrintPage(InfoArray) {		  	  
    LODOP.NEWPAGE();	 
    LODOP.PRINT_INITA(0,0,"210mm","297mm","打印上机监考教师名单");
    
    
    	 for(var i=0;i<printParamArray.length;i++){
    	  	  if(i!=1)
    				{
    			    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
    			    		printParamArray[i][2],printParamArray[i][3],
    			    		InfoArray[i]);
    				LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
    				LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
    				LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
    			}
    	  	else if(i==1){
    	  		  	  		 
    	  		 LODOP.ADD_PRINT_TABLE(printParamArray[i][0],printParamArray[i][1],printParamArray[i][2],printParamArray[i][3],InfoArray[i]);
    	  	}
    	  	
    	    }//end for
    	
    }
    
 /* function CreatePrintPage(strTableHTML) {	
	  
	  LODOP.NEWPAGE();	 
	  LODOP.ADD_PRINT_TEXT(29,20,700,150,"浙江省高校计算机等级考试上机监考教师名单");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",25);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);
	  LODOP.ADD_PRINT_TABLE(80,-22,800,900,strTableHTML);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);

  }*/
  
</script> 

<script type="text/javascript">
initprintParamArray();
Ext.onReady(function(){
	this.printOperateExaminerTeacherPageInit();
});
</script>

<div id="hiddentable" style="display:none"></div>
</body>
</html>
