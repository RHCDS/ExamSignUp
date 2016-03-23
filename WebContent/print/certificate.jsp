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
		<script type="text/javascript" src="../resources/js/PrintCertificate.js"></script>
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
	
	//var currentPrinter=-1;
	
	
	var printParamArray=new Array();
	function initPrintParamArray(){
		printParamArray[0]=new Array(12,"100.3mm","33.6mm","58.7mm","6.6mm");//学校名称
		printParamArray[1]=new Array(12,"109.5mm","33.6mm","58.7mm","6.6mm");//准考证号
		printParamArray[2]=new Array(12,"127.3mm","33.6mm","57.9mm","6.6mm");//填发日期
		printParamArray[3]=new Array(15,"24.9mm","134.9mm","29.1mm","7.9mm");//姓名
		printParamArray[4]=new Array(15,"24.6mm","177.3mm","15.3mm","7.9mm");//性别
		printParamArray[5]=new Array(15,"35.7mm","123.6mm","20.6mm","7.9mm");//年份
		printParamArray[6]=new Array(15,"35.7mm","158mm","20.6mm","8.2mm");//季度
		printParamArray[7]=new Array(15,"55.6mm","124.1mm","68.5mm","8.2mm");//语种
		printParamArray[8]=new Array(15,"68mm","125.4mm","26.5mm","7.9mm");//成绩
		printParamArray[9]=new Array(2,"46.3mm","31mm","30.4mm","40.7mm");//照片
	}
	function reSizePrintParam(){
		for(var i=0;i<printParamArray.length-1;i++){
			printParamArray[i][0]=LODOP.GET_VALUE("ItemFontSize",i+1);
			printParamArray[i][1]=LODOP.GET_VALUE("ItemTop",i+1).replace(/\"/g,"");
			printParamArray[i][2]=LODOP.GET_VALUE("ItemLeft",i+1).replace(/\"/g,"");
			printParamArray[i][3]=LODOP.GET_VALUE("ItemWidth",i+1).replace(/\"/g,"");
			printParamArray[i][4]=LODOP.GET_VALUE("ItemHeight",i+1).replace(/\"/g,"");
		}
		printParamArray[i][0]=LODOP.GET_VALUE("ItemStretch",i+1);
		printParamArray[i][1]=LODOP.GET_VALUE("ItemTop",i+1).replace(/\"/g,"");
		printParamArray[i][2]=LODOP.GET_VALUE("ItemLeft",i+1).replace(/\"/g,"");
		printParamArray[i][3]=LODOP.GET_VALUE("ItemWidth",i+1).replace(/\"/g,"");
		printParamArray[i][4]=LODOP.GET_VALUE("ItemHeight",i+1).replace(/\"/g,"");
	}
  function CreatePrintPage(certiInfoArray) {
	  //cxwz已经废弃
	    LODOP.NewPage();

	    LODOP.PRINT_INITA("0mm","0mm","210.1mm","147.9mm","套打等级考试证书模板");
	    for(var i=0;i<printParamArray.length-1;i++){
		    LODOP.ADD_PRINT_TEXTA("text"+i,printParamArray[i][1],printParamArray[i][2],
		    		printParamArray[i][3],printParamArray[i][4],
		    		certiInfoArray[i]);
	    	LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][0]);
	    }
	    LODOP.ADD_PRINT_IMAGE(printParamArray[i][1],printParamArray[i][2],printParamArray[i][3],printParamArray[i][4],certiInfoArray[i]);
	    LODOP.SET_PRINT_STYLEA(0,"Stretch",printParamArray[i][0]);
	  }
  
</script> 

<script type="text/javascript">
initPrintParamArray();
Ext.onReady(function(){
	this.printCertificatePageInit();
});
</script>
</body>
</html>
