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
		<script type="text/javascript" src="../resources/js/PrintTheoryExaminee.js"></script>
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
	printParamArray[0]=new Array("8px","10px","720px","80px",25,2,1);//浙江省高校计算机等级考试理论考试考生名册
	printParamArray[1]=new Array("67px","69px","140px","33px",18,1,0);//学校代码title		
	printParamArray[2]=new Array("67px","290px","70px","34px",18,1,0);//语种title		
	printParamArray[3]=new Array("67px","610px","20px","33px",18,1,0);//第	
	printParamArray[4]=new Array("67px","640px","70px","30px",12,1,0);//考场号content	 	
	printParamArray[5]=new Array("67px","690px","80px","33px",18,1,0);//考场title  	
	printParamArray[6]=new Array("67px","190px","80px","30px",12,1,0);//学校代码content
	printParamArray[7]=new Array("66px","350px","220px","30px",12,1,0);//语种content
	printParamArray[8]=new Array("110px","-14px","780px","950px",12,1,0);//table
	
	
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
    LODOP.PRINT_INITA(0,0,"210mm","297mm","打印理论考试考生名册");
    
    
    	 for(var i=0;i<printParamArray.length;i++){
    	  	  if(i!=8)
    				{
    			    LODOP.ADD_PRINT_TEXT(printParamArray[i][0],printParamArray[i][1],
    			    		printParamArray[i][2],printParamArray[i][3],
    			    		InfoArray[i]);
    				LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][4]);
    				LODOP.SET_PRINT_STYLEA(0,"Alignment",printParamArray[i][5]);
    				LODOP.SET_PRINT_STYLEA(0,"Bold",printParamArray[i][6]);
    			}
    	  	else if(i==8){
    	  		  	  		 
    	  		 LODOP.ADD_PRINT_TABLE(printParamArray[i][0],printParamArray[i][1],printParamArray[i][2],printParamArray[i][3],InfoArray[i]);
    	  	}
    	  	
    	    }//end for
    	
    }

	
/*function CreatePrintPage(xxdm,yz,logicroomnum,strTableHTML) {		  	  
	 
	  LODOP.ADD_PRINT_TEXT(8,10,720,80,"浙江省高校计算机等级考试理论考试考生名册");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",25);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);
	  LODOP.SET_PRINT_STYLEA(0,"Bold",1);	
	  
	  
	  LODOP.ADD_PRINT_TEXT(67,90,120,33,"学校代码");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(97,190,98,280,0,1);
	  LODOP.ADD_PRINT_TEXT(67,300,60,34,"语种");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(98,350,99,590,0,1);
	  
	  LODOP.ADD_PRINT_TEXT(67,610,20,33,"第");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	  LODOP.ADD_PRINT_LINE(97,630,98,690,0,1);  
	  LODOP.ADD_PRINT_TEXT(67,640,70,30,logicroomnum);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);	  
	  LODOP.ADD_PRINT_TEXT(67,690,80,33,"考场");
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",18);
	
	  LODOP.ADD_PRINT_TEXT(67,190,80,30,xxdm);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);
	  LODOP.ADD_PRINT_TEXT(66,350,220,30,yz);
	  LODOP.SET_PRINT_STYLEA(0,"FontSize",12);	  
	  LODOP.ADD_PRINT_TABLE(110,-14,780,950,strTableHTML);
	  LODOP.SET_PRINT_STYLEA(0,"Alignment",2);

}*/
  
</script> 

<script type="text/javascript">
initprintParamArray();
Ext.onReady(function(){
	this.printTheoryExamineePageInit();
});
</script>

<div id="hiddentable" style="display:none"></div>
</body>
</html>
