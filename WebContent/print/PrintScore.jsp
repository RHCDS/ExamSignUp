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
		<script type="text/javascript" src="../resources/js/PrintScore.js"></script>
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
	printParamArray[0]=new Array("8px","57px","700px","80px",30,2,1);//浙江省高校计算机等级考试成绩单
	printParamArray[1]=new Array("67px","88px","140px","33px",18,1,0);//学校代码title		
	printParamArray[2]=new Array("67px","380px","80px","34px",18,1,0);//学院title	
	printParamArray[3]=new Array("67px","220px","100px","30px",16,1,0);//学校代码content	
	printParamArray[4]=new Array("66px","440px","330px","30px",16,1,0);//学院content	 	
	printParamArray[5]=new Array("100px","-7px","780px","950px",14,2,0);//table	  		
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
    LODOP.PRINT_INITA(0,0,"210mm","297mm","打印考试成绩单");
    
    
    	 for(var i=0;i<printParamArray.length;i++){
    	  	  if(i!=5)
    				{
    			    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
    			    		printParamArray[i][2],printParamArray[i][3],
    			    		InfoArray[i]);
    				LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
    				LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
    				LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
    			}
    	  	else if(i==5){
    	  		  	  		 
    	  		 LODOP.ADD_PRINT_TABLE(printParamArray[i][0],printParamArray[i][1],printParamArray[i][2],printParamArray[i][3],InfoArray[i]);
    	  	}
    	  	
    	    }//end for
    	
    }
    
   

	
	
	
	
  /*function CreatePrintPage(xxdm,xy,strTableHTML) {		  	  
    	 
	  LODOP.ADD_PRINT_TEXT(8,57,700,80,"浙江省高校计算机等级考试成绩单");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",30);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);	  
	  LODOP.ADD_PRINT_TEXT(67,110,120,33,"学校代码");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(97,220,98,300,0,1);
	  LODOP.ADD_PRINT_TEXT(67,380,80,34,"学院");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(98,440,99,700,0,1);
	  LODOP.ADD_PRINT_TEXT(67,220,100,30,xxdm);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
	  LODOP.ADD_PRINT_TEXT(66,440,330,30,xy);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);	  
	  LODOP.ADD_PRINT_TABLE(100,-7,780,950,strTableHTML);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);

  }*/
  
  
</script> 

<script type="text/javascript">
initprintParamArray();
Ext.onReady(function(){
	this.printScorePageInit();
});
</script>
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPageInit();LODOP.PRINT_DESIGN();">æ¨¡æ¿è®¾è®¡</a><br><br> -->
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPage1();LODOP.PREVIEW();">æ¨¡æ¿çæå°é¢è§</a> -->

<div id="hiddentable" style="display:none"></div>

</body>
</html>
