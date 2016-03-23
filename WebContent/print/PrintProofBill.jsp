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
		<script type="text/javascript" src="../resources/js/PrintProofBill.js"></script>
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
//选择班级的情况
var printParamArray=new Array();
function initprintParamArray(){
	//top,left,width,height,fontsize,alignment,blod
	printParamArray[0]=new Array("9px","20px","700px","100px",25,2,1);//浙江省高校计算机等级考试校对单
	printParamArray[1]=new Array("55px","100px","60px","34px",14,1,0);//学院title	
	printParamArray[2]=new Array("55px","160px","240px","30px",12,1,0);//学院content	 	
	printParamArray[3]=new Array("55px","400px","60px","34px",14,1,0);//班级title	
	printParamArray[4]=new Array("55px","460px","300px","30px",12,1,0);//班级content	 	
	printParamArray[5]=new Array("90px","-27px","800px","900px",14,2,0);//table	  		
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
    //LODOP.PRINT_INITA(0,0,"210mm","297mm","打印汇总报表");
    
    
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
    
   



//只选择学院的情况
var printParamArray_college=new Array();

function initprintParamArray_college(){
	//top,left,width,height,fontsize,alignment,blod
	printParamArray_college[0]=new Array("9px","20px","700px","100px",25,2,1);//浙江省高校计算机等级考试校对单
	printParamArray_college[1]=new Array("55px","280px","60px","34px",14,1,0);//学院title	
	printParamArray_college[2]=new Array("55px","340px","300px","30px",12,1,0);//学院content	 	
	printParamArray_college[3]=new Array("90px","-27px","800px","900px",14,2,0);//table	  		
}


function reSizePrintParam_college(){
	for(var i=0;i<printParamArray_college.length;i++){			
			printParamArray_college[i][0]=LODOP.GET_VALUE("ItemTop",i+1).replace(/\"/g,"");
			printParamArray_college[i][1]=LODOP.GET_VALUE("ItemLeft",i+1).replace(/\"/g,"");
			printParamArray_college[i][2]=LODOP.GET_VALUE("ItemWidth",i+1).replace(/\"/g,"");
			printParamArray_college[i][3]=LODOP.GET_VALUE("ItemHeight",i+1).replace(/\"/g,"");
			printParamArray_college[i][4]=LODOP.GET_VALUE("ItemFontSize",i+1);
			printParamArray_college[i][5]=LODOP.GET_VALUE("ItemAlign",i+1);
			printParamArray_college[i][6]=LODOP.GET_VALUE("Itembold",i+1);
			
		}		
	
}



function CreatePrintPage_college(InfoArray_college) {		  	  
    LODOP.NEWPAGE();	 
    //LODOP.PRINT_INITA(0,0,"210mm","297mm","打印汇总报表");
    
    for(var i=0;i<printParamArray_college.length;i++){
  	  if(i!=3)
			{
		    LODOP.ADD_PRINT_TEXT(printParamArray_college[i][0],printParamArray_college[i][1],
		    		printParamArray_college[i][2],printParamArray_college[i][3],
		    		InfoArray_college[i]);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray_college[i][4]);
			LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray_college[i][5]);
			LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray_college[i][6]);
		}
  	else if(i==3){
  		 
  		 LODOP.ADD_PRINT_TABLE(printParamArray_college[i][0],printParamArray_college[i][1],printParamArray_college[i][2],printParamArray_college[i][3],InfoArray_college [i]);
  	}
  	
}
}
	
	
  /*function CreatePrintPage(collegename,classname,strTableHTML) {		  	  
      LODOP.NEWPAGE();	 
	  LODOP.ADD_PRINT_TEXT(9,20,700,100,"浙江省高校计算机等级考试校对单");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",25);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);
	  
	//  LODOP.ADD_PRINT_TEXT(50,115,60,33,"语种");
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
	//  LODOP.ADD_PRINT_LINE(78,160,79,380,0,1);
	//  LODOP.ADD_PRINT_TEXT(50,160,300,30,languagename);
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
	  if(classname=="---请选择---"||classname=="不选")
	  {
		  LODOP.ADD_PRINT_TEXT(55,280,60,34,"学院");
		  LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
		  LODOP.ADD_PRINT_LINE(83,340,84,500,0,1);
		  
		  LODOP.ADD_PRINT_TEXT(55,340,300,30,collegename);
		  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
		  
		  
		  LODOP.ADD_PRINT_TABLE(90,-27,800,900,strTableHTML);
		  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
		  
	  }
	  else {
		  LODOP.ADD_PRINT_TEXT(55,100,60,34,"学院");
		  LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
		  LODOP.ADD_PRINT_LINE(83,160,84,390,0,1);
		  
		  LODOP.ADD_PRINT_TEXT(55,160,240,30,collegename);
		  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
		  
		  LODOP.ADD_PRINT_TEXT(55,400,60,34,"班级");
		  LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
		  LODOP.ADD_PRINT_LINE(83,450,84,600,0,1);
		  
		  LODOP.ADD_PRINT_TEXT(55,460,300,30,classname);
		  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
		  
		  LODOP.ADD_PRINT_TABLE(90,-27,800,900,strTableHTML);
		  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  }
	  
	  
	  

  }*/
  
  
</script> 

<script type="text/javascript">
initprintParamArray_college();
initprintParamArray();

Ext.onReady(function(){
	this.printProofBillPageInit();
});
</script>
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPageInit();LODOP.PRINT_DESIGN();">æ¨¡æ¿è®¾è®¡</a><br><br> -->
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPage1();LODOP.PREVIEW();">æ¨¡æ¿çæå°é¢è§</a> -->

<div id="hiddentable" style="display:none"></div>

</body>
</html>
