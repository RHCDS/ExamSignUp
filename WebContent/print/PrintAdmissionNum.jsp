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
		<script type="text/javascript" src="../resources/js/PrintAdmissionNum.js"></script>
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
<script language="javascript" type="text/javascript">
var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));

var printParamArray=new Array();
function initprintParamArray(){
	//top,left,width,height,fontsize,alignment,blod
	printParamArray[0]=new Array("8px","20px","700px","80px",20,2,1);//浙江省高校计算机等级考试理论考试准考证桌贴
	printParamArray[1]=new Array("48px","183px","20px","33px",18,1,0);//第	
	printParamArray[2]=new Array("48px","213px","70px","30px",12,1,0);//考场号content	 	
	printParamArray[3]=new Array("48px","263px","80px","33px",18,1,0);//考场title  	
	printParamArray[4]=new Array("48px","397px","80px","34px",12,1,0);//教室title
	printParamArray[5]=new Array("48px","460px","200px","30px",12,1,0);//教室content
	printParamArray[6]=new Array("20.9mm","-4.8mm","210.1mm","297.1mm",12,1,0);//table
	
	
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
    LODOP.PRINT_INITA(0,0,"210mm","297mm","打印理论考试准考证桌贴");
    
    
    	 for(var i=0;i<printParamArray.length;i++){
    	  	  if(i!=6)
    				{
    			    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
    			    		printParamArray[i][2],printParamArray[i][3],
    			    		InfoArray[i]);
    				LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
    				LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
    				LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
    			}
    	  	else if(i==6){
    	  		  	  		 
    	  		 LODOP.ADD_PRINT_TABLE(printParamArray[i][0],printParamArray[i][1],printParamArray[i][2],printParamArray[i][3],InfoArray[i]);
    	  	}
    	  	
    	    }//end for
    	
    }

/*
  function CreatePrintPage(logicExamroomNum,roomLocation,strTableHTML) {		  	  
      LODOP.NEWPAGE();	 
      LODOP.ADD_PRINT_TEXT(8,20,700,80,"浙江省高校计算机等级考试理论考试准考证桌贴");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);	
	  
	  LODOP.ADD_PRINT_TEXT(48,183,20,33,"第");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(76,203,77,273,0,1);
	  LODOP.ADD_PRINT_TEXT(48,213,70,30,logicExamroomNum);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
	  LODOP.ADD_PRINT_TEXT(48,263,80,33,"考场");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  
	  LODOP.ADD_PRINT_TEXT(48,397,80,34,"教室");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(76,460,77,600,0,1);
	  
	  LODOP.ADD_PRINT_TEXT(48,460,200,30,roomLocation);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
	  
	  LODOP.ADD_PRINT_TABLE("21mm","-4.8mm","210mm","297mm",strTableHTML);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);	  
}*/
  
  
</script> 

<script type="text/javascript">
initprintParamArray();
Ext.onReady(function(){
	this.printAdmissionNumPageInit();
});
</script>
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPageInit();LODOP.PRINT_DESIGN();">æ¨¡æ¿è®¾è®¡</a><br><br> -->
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPage1();LODOP.PREVIEW();">æ¨¡æ¿çæå°é¢è§</a> -->

<div id="hiddentable" style="display:none"></div>

</body>
</html>
