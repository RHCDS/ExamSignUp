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
		<script type="text/javascript" src="../resources/js/PrintOperateExaminee.js"></script>
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
	printParamArray[0]=new Array("8px","10px","720px","80px",25,2,1);//浙江省高校计算机等级考试上机考试考生名册
	printParamArray[1]=new Array("67px","5px","118px","33px",15,1,0);//学校代码title		
	printParamArray[2]=new Array("67px","160px","58px","34px",15,1,0);//语种title		
	printParamArray[3]=new Array("67px","110px","80px","30px",12,1,0);//学校代码content	
	printParamArray[4]=new Array("61px","210px","220px","30px",12,1,0);//语种content
	printParamArray[5]=new Array("66px","400px","58px","34px",15,1,0);//场次	
	printParamArray[6]=new Array("65px","450px","60px","30px",15,1,0);//场次content		
	printParamArray[7]=new Array("65px","495px","110px","33px",15,1,0);//机房地址  title
	printParamArray[8]=new Array("65px","598px","178px","30px",15,1,0);//机房地址 content
	printParamArray[9]=new Array("100px","-7px","780px","950px",12,1,0);//table
	
	
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
    LODOP.PRINT_INITA(0,0,"210mm","297mm","打印上机考生名册");
    
    
    	 for(var i=0;i<printParamArray.length;i++){
    	  	  if(i!=9)
    				{
    			    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
    			    		printParamArray[i][2],printParamArray[i][3],
    			    		InfoArray[i]);
    				LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
    				LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
    				LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
    			}
    	  	else if(i==9){
    	  		  	  		 
    	  		 LODOP.ADD_PRINT_TABLE(printParamArray[i][0],printParamArray[i][1],printParamArray[i][2],printParamArray[i][3],InfoArray[i]);
    	  	}
    	  	
    	    }//end for
    	
    }



/*function CreatePrintPage(xxdm,yz,sectionnum,jfdz,strTableHTML) {		  	  
	 
	  LODOP.ADD_PRINT_TEXT(8,10,720,80,"浙江省高校计算机等级考试上机考试考生名册");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",25);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);	
	  
	  LODOP.ADD_PRINT_TEXT(67,5,110,33,"学校代码");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(97,110,96,160,0,1);
	  
	  LODOP.ADD_PRINT_TEXT(67,160,58,34,"语种");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(98,210,99,390,0,1);
	  
	  LODOP.ADD_PRINT_TEXT(67,110,80,30,xxdm);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
	  LODOP.ADD_PRINT_TEXT(61,210,220,30,yz);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);	
	  
	 // LODOP.ADD_PRINT_TEXT(69,19,120,33,"学校名称");
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	//  LODOP.ADD_PRINT_LINE(98,130,99,347,0,1);
	  
	 
	//  LODOP.ADD_PRINT_TEXT(68,130,200,30,xxmc);
	  
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
	  
	  LODOP.ADD_PRINT_TEXT(66,400,58,34,"场次");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(97,450,97,483,0,1);
	
	  LODOP.ADD_PRINT_TEXT(65,450,60,30,sectionnum);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);	  
	  
	  LODOP.ADD_PRINT_TEXT(65,495,110,33,"机房地址");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(96,608,97,768,0,1);
	  
	  LODOP.ADD_PRINT_TEXT(65,598,178,30,jfdz);
	  
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);	  	   
	  LODOP.ADD_PRINT_TABLE(100,-7,780,950,strTableHTML);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);

	

	
	 
	 

}*/
  
</script> 

<script type="text/javascript">
initprintParamArray();
Ext.onReady(function(){
	this.printOperateExamineePageInit();
});
</script>

<div id="hiddentable" style="display:none"></div>
</body>
</html>
