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
		<script type="text/javascript" src="../resources/js/PrintSignUpInterface.js"></script>
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
	printParamArray[0]=new Array("120px","41px","714px","45px",25,2,0);//浙江省高校计算机等级考试	
	printParamArray[1]=new Array("50px","25px","745px","51px",30,2,1);//学校名称
	
	printParamArray[2]=new Array("160px","44px","705px","30px",18,2,0);//语种
	printParamArray[3]=new Array("240px","374px","44px","600px",35,2,1);//报名册	
	printParamArray[4]=new Array("850px","35px","721px","39px",26,2,1);//杭州电子科技大学教务处
	printParamArray[5]=new Array("920px","35px","721px","39px",26,2,1);//春季还是秋季
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
	    
	  LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印报名册封面");
	  //LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
	  for(var i=0;i<printParamArray.length;i++){
		    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
		    		printParamArray[i][2],printParamArray[i][3],
		    		InfoArray[i]);
			LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
			LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
			LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
		}
	  
	 // LODOP.ADD_PRINT_TEXT(120,41,714,45,"浙江省高校计算机等级考试\n");
	////  LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[0][0]);
	//  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	//  LODOP.ADD_PRINT_TEXT(50,25,745,51,xxmc);
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[1][0]);
	//  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	//  LODOP.SET_PRINT_STYLEA(0,"Bold",1);
	//  LODOP.ADD_PRINT_TEXT(160,44,705,30,yz);
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	//  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	//  LODOP.ADD_PRINT_TEXT(240,374,44,600,"报\n\n\n名\n\n\n册");
	
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",35);
	//  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	//  LODOP.SET_PRINT_STYLEA(0,"Bold",1);
	//  LODOP.ADD_PRINT_TEXT(850,35,721,39,xxmc+"教务处");
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",26);
	//  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	//  LODOP.SET_PRINT_STYLEA(0,"Bold",1);
//	  LODOP.ADD_PRINT_TEXT(920,35,721,39,mydate);
	//  LODOP.SET_PRINT_STYLEA(0,"FontSize",26);
	//  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	//  LODOP.SET_PRINT_STYLEA(0,"Bold",1);


  }
  
  
</script> 

<script type="text/javascript">
initPrintParamArray();
Ext.onReady(function(){
	this.printSignUpInterfacePageInit();
});
</script>
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPageInit();LODOP.PRINT_DESIGN();">æ¨¡æ¿è®¾è®¡</a><br><br> -->
<!-- è¿å¥<a href="javascript:;" onclick="javascript:CreatePrintPage1();LODOP.PREVIEW();">æ¨¡æ¿çæå°é¢è§</a> -->
</body>
</html>
