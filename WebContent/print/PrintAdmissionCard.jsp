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
		<script type="text/javascript" src="../resources/js/PrintAdmissionCard.js"></script>
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
		<div style = "display:none">
  			<form name ="exportStudentsAdmissionExcel" id = "exportStudentsAdmissionExcel" action = "" target = "export" method ="post" accept-charset="UTF-8"></form>
  			<iframe name="export" id="export"></iframe>
  		</div>


<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript">

	var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));
	
	printParamArray=new Array();
	
	//常量区
	constValue= new Array(
			"浙江省高校计算机等级考试",
			"准考证",
			"学校名称:",
			"",
			"语  种:",
			"准考证号:",
			"院  系:",
			"班  级:",
			"姓  名:",
			"理论考试地点:",
			"理论考试时间:",
			"上机考试地点:",
			"上机考试时间:",
			"",
			""
			);
	defaultRectOffeset=new Array(0,5,5,0,0);
	
	//初始化位置信息
	function initPrintParamArray(){
		//以下为变量,假设矩形框左上角为原点，偏移为相对偏移
		
		printParamArray[0]=new Array(15,1,15,70,7);//浙江省高校计算机等级考试
		printParamArray[1]=new Array(13,8,35,25,7);//准考证
		printParamArray[2]=new Array(10,15,5,20,4);//学校名称
		printParamArray[3]=new Array(10,20,5,65,5);//杭州电子科技大学
		printParamArray[4]=new Array(10,24,5,65,9);//语种
		printParamArray[5]=new Array(10,33,5,65,5);//准考证号
		printParamArray[6]=new Array(10,41,5,80,4);//院系
		printParamArray[7]=new Array(10,45,5,80,5);//班级
		printParamArray[8]=new Array(10,37,5,65,4);//姓名
		printParamArray[9]=new Array(10,49,5,80,5);//理论考试地点
		printParamArray[10]=new Array(10,53,5,80,4);//理论考试时间
		printParamArray[11]=new Array(10,57,5,80,5);//上机考试地点
		printParamArray[12]=new Array(10,61,5,80,4);//上机考试时间
		printParamArray[13]=new Array(10,66,5,85,12);//注意事项
		printParamArray[14]=new Array(2,14,68,20,24);//照片
		
		/*
		printParamArray[0]=new Array(15,3,17,70,7);//浙江省高校计算机等级考试
		printParamArray[1]=new Array(13,10,37,25,7);//准考证
		printParamArray[2]=new Array(10,17,7,20,4);//学校名称
		printParamArray[3]=new Array(10,22,7,65,5);//杭州电子科技大学
		printParamArray[4]=new Array(10,26,7,65,9);//语种
		printParamArray[5]=new Array(10,35,7,65,5);//准考证号
		printParamArray[6]=new Array(10,43,7,80,4);//院系
		printParamArray[7]=new Array(10,47,7,80,5);//班级
		printParamArray[8]=new Array(10,39,7,65,4);//姓名
		printParamArray[9]=new Array(10,51,7,80,5);//理论考试地点
		printParamArray[10]=new Array(10,55,7,80,4);//理论考试时间
		printParamArray[11]=new Array(10,59,7,80,5);//上机考试地点
		printParamArray[12]=new Array(10,63,7,80,4);//上机考试时间
		printParamArray[13]=new Array(10,68,7,85,12);//注意事项
		printParamArray[14]=new Array(2,16,70,20,24);//照片
		*/
		//intLineWidth(pixel),Top, Left, Width, Height,intLineStyle
		//要求在打印初始化后调用，建议在文本类函数之前调用
		printParamArray[15]=new Array(2,0,0,90,85,0);
	}
	
	//相对位置重置
	function reSizePrintParam(){
		var top=parseInt(LODOP.GET_VALUE("ItemTop",1).replace(/\"/g,"").replace(/mm/g,""));
		var left=parseInt(LODOP.GET_VALUE("ItemLeft",1).replace(/\"/g,"").replace(/mm/g,""));
		printParamArray[printParamArray.length-1][3]=parseInt(LODOP.GET_VALUE("ItemWidth",1).replace(/\"/g,"").replace(/mm/g,""));
		printParamArray[printParamArray.length-1][4]=parseInt(LODOP.GET_VALUE("ItemHeight",1).replace(/\"/g,"").replace(/mm/g,""));
		for(var i=0;i<printParamArray.length-2;i++){
			printParamArray[i][0]=LODOP.GET_VALUE("ItemFontSize",i+2);
			printParamArray[i][1]=parseInt(LODOP.GET_VALUE("ItemTop",i+2).replace(/\"/g,"").replace(/mm/g,""))-top;
			printParamArray[i][2]=parseInt(LODOP.GET_VALUE("ItemLeft",i+2).replace(/\"/g,"").replace(/mm/g,""))-left;
			printParamArray[i][3]=parseInt(LODOP.GET_VALUE("ItemWidth",i+2).replace(/\"/g,"").replace(/mm/g,""));
			printParamArray[i][4]=parseInt(LODOP.GET_VALUE("ItemHeight",i+2).replace(/\"/g,"").replace(/mm/g,""));
		}
		printParamArray[i][0]=LODOP.GET_VALUE("ItemStretch",i+2);
		printParamArray[i][1]=parseInt(LODOP.GET_VALUE("ItemTop",i+2).replace(/\"/g,"").replace(/mm/g,""))-top;
		printParamArray[i][2]=parseInt(LODOP.GET_VALUE("ItemLeft",i+2).replace(/\"/g,"").replace(/mm/g,""))-left;
		printParamArray[i][3]=parseInt(LODOP.GET_VALUE("ItemWidth",i+2).replace(/\"/g,"").replace(/mm/g,""));
		printParamArray[i][4]=parseInt(LODOP.GET_VALUE("ItemHeight",i+2).replace(/\"/g,"").replace(/mm/g,""));
	}
	
	//准考证内容合并
	function paramJoinConstValue(printobject){
		var finalParamArray=new Array();
		finalParamArray[0]=constValue[0];//浙江省高校计算机等级考试
		finalParamArray[1]=constValue[1];//准考证
		finalParamArray[2]=constValue[2];//学校名称
		finalParamArray[3]=constValue[3]+xxmc;//杭州电子科技大学
		finalParamArray[4]=constValue[4]+printobject.get("exLanguage");//语种
		finalParamArray[5]=constValue[5]+printobject.get("examnum");//准考证号
		finalParamArray[6]=constValue[6]+printobject.get("exCollege");//院系
		finalParamArray[7]=constValue[7]+printobject.get("classnum");//班级
		finalParamArray[8]=constValue[8]+printobject.get("name")+" ("+printobject.get("studentnum")+")";//姓名
		if(printobject.get("theoryroomlocation")== "无")
			finalParamArray[9]=constValue[9]+printobject.get("theoryroomlocation");
		else	
			finalParamArray[9]=constValue[9]+printobject.get("theoryroomlocation");//理论考试地点
		finalParamArray[10]=constValue[10]+printobject.get("theorystarttime");//理论考试时间
		if(printobject.get("operateroomlocation")=="无")
			finalParamArray[11]=constValue[11]+printobject.get("operateroomlocation");
		else
			finalParamArray[11]=constValue[11]+printobject.get("operateroomlocation")+" "+"第"+printobject.get("operateseat")+"座位";//上机考试地点
		finalParamArray[12]=constValue[12]+printobject.get("operatestarttime");//上机考试时间
		finalParamArray[13]=constValue[13]+remark;//注意事项
		finalParamArray[14]=constValue[14]+fileWebPath+printobject.get("studentnum")+".jpg?rnd="+Math.random();//照片路径
		return finalParamArray;
	}
	//intLineWidth(pixel),Top, Left, Width, Height,intLineStyle
	//要求在打印初始化后调用，建议在文本类函数之前调用
	function createNumRect(rectCount){
		rectCount=rectCount>6?6:rectCount;
		var topOffset=0;
		var leftOffset=0;
		for(var i=0;i<rectCount;i++){
			if(i%2 == 0){
			topOffset=Math.floor(i/2)*printParamArray[printParamArray.length-1][4]+(i/2)*3;
			leftOffset=i%2*printParamArray[printParamArray.length-1][3];
			}
			else
				{topOffset=Math.floor(i/2)*printParamArray[printParamArray.length-1][4]+((i-1)/2)*3;
				leftOffset=i%2*printParamArray[printParamArray.length-1][3]+3;
				};
			
			LODOP.ADD_PRINT_RECT(
					topOffset+defaultRectOffeset[1]+"mm",
					leftOffset+defaultRectOffeset[2]+"mm",
					printParamArray[printParamArray.length-1][3]+"mm",
					printParamArray[printParamArray.length-1][4]+"mm",
					0,
					printParamArray[printParamArray.length-1][0]
			);
		}
	}
		
	function CreateSelectedPage(){
		if(startRecordInPrint>=studentsSelectedArray.length){
			return;
		}
		contextArray=new Array();
		var topOffset=0;
		var leftOffset=0;
	    LODOP.NEWPAGEA();
	    printCount=(studentsSelectedArray.length-startRecordInPrint)>limitRecordInOnePage?limitRecordInOnePage:(studentsSelectedArray.length-startRecordInPrint);
	    createNumRect(printCount);
	    for(var j=0;j<printCount;j++){
	    	if(j%2 == 0){
	    	topOffset=Math.floor(j/2)*printParamArray[printParamArray.length-1][4]+(j/2)*3;
			leftOffset=j%2*printParamArray[printParamArray.length-1][3];
	    	}
	    	else{
	    		topOffset=Math.floor(j/2)*printParamArray[printParamArray.length-1][4]+((j-1)/2)*3;
				leftOffset=j%2*printParamArray[printParamArray.length-1][3]+3;	
	    	}
			contextArray[j]=paramJoinConstValue(studentsSelectedArray[j+startRecordInPrint]);
			
	    	for(var i=0;i<printParamArray.length-2;i++){
	    			LODOP.ADD_PRINT_TEXTA(
					"text"+j+""+i,
					printParamArray[i][1]+topOffset+defaultRectOffeset[1]+"mm",
					printParamArray[i][2]+leftOffset+defaultRectOffeset[2]+"mm",
		    		printParamArray[i][3]+"mm",
		    		printParamArray[i][4]+"mm",
		    		contextArray[j][i]);
				LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][0]);
	    	}
	    }
	    
	    needLoadCount=printCount;
	    errorCount=0;
	    studentImageArray=new Array();
	    for(var i=0;i<printCount;i++){
	    	studentImageArray[i]=new Image();
	    }
	    for(var i=0;i<printCount;i++){
	    		studentImageArray[i].onload=function(image){
				printSelected(image,0);
			};
			studentImageArray[i].onerror=function(image){
				printSelected(image,1);
			};
			studentImageArray[i].src=contextArray[i][contextArray[i].length-1];
	    }
	}
	
	function printSelected(image,errorFlag){
	    updateProcessBox(startRecordInPrint+printCount);
		if(errorFlag)
			errorCount++;
		needLoadCount--;
		var current;
		for(current=0;current<studentImageArray.length;current++){
			if(studentImageArray[current]==image.target)
				break;
		}
		if(current==studentImageArray.length){
			errorCount++;
			needLoadCount--;
			Ext.MessageBox.alert("提示","图片无法匹配！");
		}else{
			if(errorFlag)
				studentImageArray[current]=null;
			else{
				var canvas = document.createElement("canvas");
				canvas.width = image.target.width;
				canvas.height = image.target.height;								 
				var ctx = canvas.getContext("2d");
				ctx.drawImage(image.target, 0, 0);
				studentImageArray[current] = canvas.toDataURL("image/jpg");
			}
		}
		if(needLoadCount==0){
			var errorString="";
			for(var i=0;i<studentImageArray.length;i++){
				if(studentImageArray[i]==null)
					errorString+=contextArray[i][5]+",";
			}
			if(errorString!="")
				alert("以下考生照片缺失"+errorString);
			for(var j=0;j<printCount;j++){
				if(j%2 == 0){
		    	topOffset=Math.floor(j/2)*printParamArray[printParamArray.length-1][4]+(j/2)*3;
				leftOffset=j%2*printParamArray[printParamArray.length-1][3];
				}
				else
					{
					topOffset=Math.floor(j/2)*printParamArray[printParamArray.length-1][4]+((j-1)/2)*3;
					leftOffset=j%2*printParamArray[printParamArray.length-1][3]+3;
					}
				
				if(studentImageArray[j]!=null)
				{
		   			LODOP.ADD_PRINT_IMAGE(
		    				printParamArray[14][1]+topOffset+defaultRectOffeset[1]+"mm",
		    				printParamArray[14][2]+leftOffset+defaultRectOffeset[2]+"mm",
		    				printParamArray[14][3]+"mm",
		    				printParamArray[14][4]+"mm",
		    				studentImageArray[j]);
		    		LODOP.SET_PRINT_STYLEA(0,"Stretch",printParamArray[14][0]);
		    	}
			
	    		
		    } 
	    	//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
	    	if(printModel==1)
	    	{
	    		if((printCount+startRecordInPrint)>=studentsSelectedArray.length){
	    			hidenProcessBox();
	    			LODOP.PRINT();
	    			return;
	    		}
				startRecordInPrint+=printCount;
				hasPrintedPage++;
				if(hasPrintedPage>0&&hasPrintedPage%oneTaskPages==0){
					LODOP.PRINT();
		//			LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","准考证"+hasPrintedPage+1);
					LODOP.PRINT_INITA("0mm","0mm","220mm","300mm","准考证"+hasPrintedPage+1);
				}
				CreateSelectedPage();
	    	}else if(printModel==0){
	    		LODOP.PRINT_SETUP();
	    		reSizePrintParam();
	    	}else if(printModel==2){
	    		if((printCount+startRecordInPrint)>endRecordInPrint){
	    			hidenProcessBox();
	    			LODOP.PRINT();
	    			return;
	    		}
				startRecordInPrint+=printCount;
				hasPrintedPage++;
				if(hasPrintedPage>0&&hasPrintedPage%oneTaskPages==0){
					LODOP.PRINT();
		//			LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","准考证"+hasPrintedPage+1);
					LODOP.PRINT_INITA("0mm","0mm","220mm","300mm","准考证"+hasPrintedPage+1);
				}
				CreatePrintAllPage();
	    	}
		}
	}
	
	
	
	//CreatePrintAllPage()
	var startRecordInPrint;//完成全部打印之前，禁止开始新的打印，否则会产生竞争！
	var endRecordInPrint;
	var hasPrintedPage;
	
	//CreateSelectedPage---------
	//var startRecordInPrint;
	//var endRecordInPrint;
	//var hasPrintedPage;
	var studentsSelectedArray=new Array();
	
	//都要设置
	//printModel 0设计,1打印选择，2打印全部，异步陷阱！！！！！！！！！
	var printModel=0;
	
	//以下变量只为方便异步调用而设置，在完成以上两个函数之前，禁止修改这些变量
	var limitRecordInOnePage=6;
	var printCount;
	var contextArray;
	var needLoadCount;
	var errorCount;
	var studentImageArray;
	var currentPrinter=-1;
	var oneTaskPages=10;
	
	function choosePrinter(){
		if(currentPrinter<0){
			currentPrinter=LODOP.SELECT_PRINTER();
			if(currentPrinter<0){
				return -1;
			}else{
				LODOP.SET_PRINTER_INDEX(currentPrinter);
				return 1;
			}
		}else{
			LODOP.SET_PRINTER_INDEX(currentPrinter);
			return 1;
		}
	}
	
	function designAdmissionCard(studentArrayParam){
		printModel=0;
		startRecordInPrint=0;
		studentsSelectedArray=studentArrayParam;
	//	LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","准考证");
		LODOP.PRINT_INITA("0mm","0mm","220mm","300mm","准考证");
		CreateSelectedPage();
	}
	
	function printSelectRecord(studentArrayParam){
		printModel=1;
		startRecordInPrint=0;
		hasPrintedPage=0;
		endRecordInPrint=studentArrayParam.length;
		studentsSelectedArray=studentArrayParam;
		initAndShowProcessBox(startRecordInPrint,studentsSelectedArray.length);
	//	LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","准考证1");
		LODOP.PRINT_INITA("0mm","0mm","220mm","300mm","准考证1");
		if(choosePrinter()!=1){
			Ext.MessageBox.alert('提示', "请设置默认打印机!");
			return;
		}
		CreateSelectedPage();
	}
	
	function printAllPages(startRecordInPrintParam,endRecordInPrintParam){
		printModel=2;
		startRecordInPrint=startRecordInPrintParam;
		endRecordInPrint=endRecordInPrintParam;
		hasPrintedPage=0;
		initAndShowProcessBox(startRecordInPrint,endRecordInPrint);
	//	LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","准考证1");
	LODOP.PRINT_INITA("0mm","0mm","220mm","300mm","准考证1");
		if(choosePrinter()!=1){
			Ext.MessageBox.alert('提示', "请设置默认打印机!");
			return;
		}
		CreatePrintAllPage();
	}
	
	
	
	//按照studentAdmissionInfostore_hidden打印
	function CreatePrintAllPage() {
		//printobject is array
		if(startRecordInPrint>endRecordInPrint)
			return;
		var params = {};
		params.fliter = studentAdmissionInfo_currentSearchFilter;
		params.start = startRecordInPrint;
		params.limit = (endRecordInPrint-startRecordInPrint+1)>limitRecordInOnePage?limitRecordInOnePage:(endRecordInPrint-startRecordInPrint+1);
		studentAdmissionInfostore_hidden.load({params:params,
			callback: function(){
				printCount=studentAdmissionInfostore_hidden.getCount();
				if (printCount) {
					contextArray=new Array();
					var topOffset=0;
					var leftOffset=0;
				    LODOP.NEWPAGEA();
				    createNumRect(printCount);
				    for(var j=0;j<printCount;j++){
				    	if(j % 2 == 0){
				    	topOffset=Math.floor(j/2)*printParamArray[printParamArray.length-1][4]+(j/2)*3;
						leftOffset=j%2*printParamArray[printParamArray.length-1][3];
				    	}
				    	else{
				    	topOffset=Math.floor(j/2)*printParamArray[printParamArray.length-1][4]+((j-1)/2)*3;
						leftOffset=j%2*printParamArray[printParamArray.length-1][3]+3;
				    	}
						contextArray[j]=paramJoinConstValue(studentAdmissionInfostore_hidden.getAt(j));
						
				    	for(var i=0;i<printParamArray.length-2;i++){
				    			LODOP.ADD_PRINT_TEXTA(
								"text"+j+""+i,
								printParamArray[i][1]+topOffset+defaultRectOffeset[1]+"mm",
								printParamArray[i][2]+leftOffset+defaultRectOffeset[2]+"mm",
					    		printParamArray[i][3]+"mm",
					    		printParamArray[i][4]+"mm",
					    		contextArray[j][i]);
							LODOP.SET_PRINT_STYLEA(0,"FontSize",printParamArray[i][0]);
				    	}
				    }
				    
				    needLoadCount=printCount;
				    errorCount=0;
				    studentImageArray=new Array();
				    for(var i=0;i<printCount;i++){
				    	studentImageArray[i]=new Image();
				    }
				    for(var i=0;i<printCount;i++){
				    		studentImageArray[i].onload=function(image){
								printSelected(image,0);
						};
						studentImageArray[i].onerror=function(image){
							printSelected(image,1);
						};
						studentImageArray[i].src=contextArray[i][contextArray[i].length-1];
				    }
				}
		}
		});
	}
 
</script> 

<script type="text/javascript">
initPrintParamArray();
Ext.onReady(function(){
	this.printAdmissionCardPageInit();
});
</script>


<div id="hiddentable" style="display:none"></div>

</body>
</html>
