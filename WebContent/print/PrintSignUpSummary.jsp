<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<html>
<head>
		<link rel="stylesheet" type="text/css" href="../resources/ext/resources/css/ext-all.css" />
		<script type="text/javascript" src="../resources/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="../resources/ext/ext-all.js"></script>
		<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
 		<script type='text/javascript' src='/ExamSignUp/dwr/interface/StudentController.js'></script>
 		<script type='text/javascript' src='/ExamSignUp/dwr/interface/InstitutionController.js'></script>
 		<script type='text/javascript' src='/ExamSignUp/dwr/interface/LanguageController.js'></script>
		<script type="text/javascript" src="../resources/custom/DwrProxy.js"></script>
		<script type="text/javascript" src="../resources/js/PrintSignUpSummary.js"></script>
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
	  function initPrintParamArray(){
	  	//top,left,width,height,fontsize,alignment,blod
	  	printParamArray[0]=new Array("10px","41px","850px","57px",30,1,0);//浙江省高校计算机等级考试学校集体报名汇总表	
	  	printParamArray[1]=new Array("87px","70px","130px","33px",20,1,0);//学校代码title		  	
	  	printParamArray[2]=new Array("87px","367px","133px","34px",20,1,0);//学校名称title		
	  	printParamArray[3]=new Array("87px","200px","100px","30px",16,1,0);//学校代码content  	
	  	printParamArray[4]=new Array("86px","500px","330px","30px",16,1,0);//学校名称content	  	
	  	printParamArray[5]=new Array("134px","320px","265px","35px",20,1,0);//报名情况	  	
	  	printParamArray[6]=new Array("162px","12px","771px","400px",14,1,0);//table	  	
	  	printParamArray[7]=new Array("600px","42px","93px","29px",15,1,0);//主考人	  		  	
	  	printParamArray[8]=new Array("600px","290px","151px","28px",15,1,0);//校外巡视人员	  		    	
	  	printParamArray[9]=new Array("600px","600px","158px","30px",15,1,0);//负责人title	  	  
	  	printParamArray[10]=new Array("600px","148px","115px","24px",15,1,0);//主考人content	  	
	  	printParamArray[11]=new Array("600px","440px","110px","27px",14,1,0);//校外巡视人员content	  	
		printParamArray[12]=new Array("640px","780px","150px","35px",15,1,0);//日期
	  	  		  	
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
	      LODOP.PRINT_INITA(0,0,"210mm","297mm","打印汇总报表");
	      
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
	  }
	  }
	  

	  /*  
  function CreatePrintPage(xxdm,xxmc_campusnum,Examiner,inspector,date,strTableHTML) {		  	  
     
	  LODOP.PRINT_INITA(50,50,"210mm","297mm","打印汇总报表");
		  	 	  
	  LODOP.ADD_PRINT_TEXT(10,41,850,57,"浙江省高校计算机等级考试学校集体报名汇总表");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",30);
	  LODOP.ADD_PRINT_TEXT(87,80,120,33,"学校代码");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
	  LODOP.ADD_PRINT_LINE(117,200,118,290,0,1);
	  LODOP.ADD_PRINT_TEXT(87,380,120,34,"学校名称");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
	  LODOP.ADD_PRINT_LINE(118,500,119,800,0,1);
	  LODOP.ADD_PRINT_TEXT(87,200,100,30,xxdm);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
	  LODOP.ADD_PRINT_TEXT(86,500,330,30,xxmc_campusnum);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",16);
	  LODOP.ADD_PRINT_TEXT(134,320,265,35,"报   名   情   况");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",20);
	  LODOP.ADD_PRINT_TABLE(184,12,771,400,strTableHTML);
	  LODOP.ADD_PRINT_TEXT(600,42,93,29,"主考人：");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",15);
	  LODOP.ADD_PRINT_LINE(620,140,621,240,0,1);
	  LODOP.ADD_PRINT_TEXT(600,290,151,28,"校外巡视人员：");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",15);
	  LODOP.ADD_PRINT_LINE(620,440,621,540,0,1);
	  LODOP.ADD_PRINT_TEXT(600,600,158,30,"负责人（签名）");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",15);
	  LODOP.ADD_PRINT_LINE(620,750,621,880,0,1);
	  LODOP.ADD_PRINT_TEXT(600,148,115,24,Examiner);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
	  LODOP.ADD_PRINT_TEXT(600,440,110,27,inspector);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",14);
	  LODOP.ADD_PRINT_TEXT(640,780,150,35,date);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",15);


  }*/
  
  
</script> 

<script type="text/javascript">
initPrintParamArray();
Ext.onReady(function(){
<!--	this.printSignUpSummaryPageInit();        -->
	
	var institution ="<%=session.getAttribute("institution")%>";
	this.printSignUpSummaryPageInit(institution);
});
</script>
<!-- 进入<a href="javascript:;" onclick="javascript:CreatePrintPageInit();LODOP.PRINT_DESIGN();">模板设计</a><br><br> -->
<!-- 进入<a href="javascript:;" onclick="javascript:CreatePrintPage1();LODOP.PREVIEW();">模板的打印预览</a> -->

<div id="hiddentable" style="display:none"></div>

</body>
</html>
