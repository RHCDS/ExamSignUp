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
		<script type="text/javascript" src="../resources/js/PrintSectionDoorFlag.js"></script>
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
function initPrintParamArray(){
	//top,left,width,height,fontsize,alignment,blod
	printParamArray[0]=new Array("29px","57px","900px","155px",50,2,1);//浙江省高校计算机等级考试	
	printParamArray[1]=new Array("237px","22px","290px","53px",40,1,0);//考试时间title	
	printParamArray[2]=new Array("236px","259px","800px","56px",40,1,0);//考试时间content
	printParamArray[3]=new Array("330px","23px","450px","53px",40,1,0);//语种title	
	printParamArray[4]=new Array("330px","420px","800px","56px",40,1,0);//语种content
	printParamArray[5]=new Array("412px","22px","300px","58px",40,1,0);//考场编号title	
	printParamArray[6]=new Array("412px","290px","550px","56px",40,1,0);//考场编号content
	printParamArray[7]=new Array("486px","22px","300px","58px",40,1,0);//考试地点title
	printParamArray[8]=new Array("486px","290px","550px","56px",40,1,0);//考试地点content
	printParamArray[9]=new Array("550px","22px","487px","53px",40,1,0);//起讫准考证号title
	printParamArray[10]=new Array("610px","22px","420px","56px",40,1,0);//开始准考证
	printParamArray[11]=new Array("630px","442px","41px","52px",40,1,0);//连接符
	printParamArray[12]=new Array("610px","481px","430px","56px",40,1,0);//结束准考证
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
      
      for(var i=0;i<printParamArray.length;i++){
		    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
		    		printParamArray[i][2],printParamArray[i][3],
		    		InfoArray[i]);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
			LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
			LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
		}
	  
      
      
      
      
	  //LODOP.ADD_PRINT_TEXT(29,57,900,155,"浙江省高校计算机等级考试\n考场标贴\n");
	  /*LODOP.SET_PRINT_STYLEA(0,"FontSize",50);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);
	  LODOP.ADD_PRINT_TEXT(237,22,290,53,"考试时间:");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(236,259,800,56,kssj);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(330,23,450,53,"应试等级及语种:");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(330,420,800,56,yz);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(412,22,300,58,"考场编号:");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(412,290,550,56,kcbh+"号考场");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  
	  LODOP.ADD_PRINT_TEXT(486,22,300,58,"考试地点:");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(486,290,550,56,dd);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  
	  LODOP.ADD_PRINT_TEXT(550,22,487,53,"起讫准考证号:");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(610,22,420,56,qszkz);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.ADD_PRINT_TEXT(630,442,41,52,"~");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",0);
	  LODOP.ADD_PRINT_TEXT(610,481,430,56,jszkz);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",40);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",0);*/

  }
  
  
</script> 

<script type="text/javascript">
initPrintParamArray();
Ext.onReady(function(){
	this.printSectionDoorFlagPageInit();
});
</script>
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPageInit();LODOP.PRINT_DESIGN();">æ¨¡æ¿è®¾è®¡</a><br><br> -->
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPage1();LODOP.PREVIEW();">æ¨¡æ¿çæå°é¢è§</a> -->
</body>
</html>
