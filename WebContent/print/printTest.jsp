<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" type="text/css" href="../resources/ext/resources/css/ext-all.css" />
		<script type="text/javascript" src="../resources/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="../resources/ext/ext-all.js"></script>
		<script type='text/javascript' src='/ExamSignUp/dwr/engine.js'></script>
  		<script type='text/javascript' src='/ExamSignUp/dwr/util.js'></script>
 		<script type='text/javascript' src='/ExamSignUp/dwr/interface/StudentController.js'></script>
		<script type="text/javascript" src="../resources/custom/DwrProxy.js"></script>
		<script type="text/javascript" src="../resources/js/PrintOneAdmissionCard.js"></script>
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
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>打印准考证</title>
</head>
<body>
<object  id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
       <embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0></embed>
</object>
<script type="text/javascript">
var LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));

var studentAdmissionInfo_currentSearchFilter = "";

var comboCollegeStore = new Ext.data.JsonStore({
	fields:[
	        'value',
	        'name'
	        ]
});

var comboLanguageStore = new Ext.data.ArrayStore({
	fields:[
	        'languagenum',
	        'name'
	        ]
});



var comboProfessionStore = new Ext.data.ArrayStore({
	fields:[
	        'professionnum',
	        'name'
	        ]
});


var studentAdmissionInfo = new Ext.data.Record.create([
                                                   	{
                                                   		name:'exLanguage'
                                                   	},{
                                                   		name:'exProfession'
                                                   	},{
                                                   		name:'exCollege'
                                                   	},{
                                                   		name:'name'
                                                   	},{
                                                   		name:'studentnum'
                                                   	},{
                                                   		name:'idnum'
                                                   	},{
                                                   		name:'examnum'
                                                   	},{
                                                        name:'logicExamroomNum'
                                                    },{
                                                    	name:'theoryroomlocation'
                                                    },{
                                                    	name:'theorystarttime'
                                                    },{
                                                    	name:'operateroomlocation'
                                                    },{
                                                    	name:'operatestarttime'
                                                    },{
                                                    	name:'theoryendTime'
                                                    },{
                                                    	name:'operateendTime'
                                                    },{
                                                    	name:'exCampus'                                                   	                                                   	                                          
                                                   	},{
                                                    	name:'operateseat'                                                    	                                                   	                                          
                                                   	},{
                                                    	name:'classnum'                                                    	                                                   	                                          
                                                   	}
                                                   	
          ]);







var studentAdmissionInfostore = new Ext.data.Store( {
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getOneStudentAdmissionInfo
       })
    });



var studentAdmissionInfostore_hidden = new Ext.data.Store( {
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getOneStudentAdmissionInfo
       })
    });
    
    
var sm=new Ext.grid.CheckboxSelectionModel();

var studentAdmissionInfoGrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentadmissioncardgrid',
	store :studentAdmissionInfostore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'name',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	//sm:sm,
	columns : [//new Ext.grid.RowNumberer(),
	           
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :120,
	 	sortable :true
	},{
		id : 'exCollege',
		header:'学院',
	  	dataIndex:'exCollege',
	  	width :120,
	 	sortable :true
	},	{
		id : 'exProfession',
		header : '专业',
	  	dataIndex :'exProfession',
	  	width :100,
	 	sortable :true
	},	{
		id : 'classnum',
		header : '班级',
	  	dataIndex :'classnum',
	  	width :100,
	 	sortable :true
	},	{
		id : 'exLanguage',
		header : '语种',
	  	dataIndex :'exLanguage',
	  	width :150,
	 	sortable :true
	},	{
		id : 'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :150,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :200,
	 	sortable :true
	},{
		id : 'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :200,
	 	sortable :true
	},{
		id : 'exCampus',
		header : '校区名称',
	  	dataIndex :'exCampus',
	  	width :120,
	 	sortable :true
	
	},{
		id : 'logicExamroomNum',
		header : '考场号',
	  	dataIndex :'logicExamroomNum',
	  	width :120,
	 	sortable :true
	},	{
		id : 'theoryroomlocation',
		header : '理论考试地址',
	  	dataIndex :'theoryroomlocation',
	  	width :200,
	 	sortable :true
	},{
		id : 'theorystarttime',
		header : '理论考试开始时间',
	  	dataIndex :'theorystarttime',
	  	width :220,
	 	sortable :true
	},{
		id : 'theoryendTime',
		header : '理论结束时间',
	  	dataIndex :'theoryendTime',
	  	width :120,
	 	sortable :true
	},{
		id : 'operateroomlocation',
		header : '上机考试地址',
	  	dataIndex :'operateroomlocation',
	  	width :120,
	 	sortable :true
	},{
		id : 'operatestarttime',
		header : '上机考试开始时间',
	  	dataIndex :'operatestarttime',
	  	width :220,
	 	sortable :true
	},{
		id : 'operateendTime',
		header : '上机结束时间',
	  	dataIndex :'operateendTime',
	  	width :120,
	 	sortable :true
	
	},{
		id : 'operateseat',
		header : '上机座位号',
	  	dataIndex :'operateseat',
	  	width :120,
	 	sortable :true
	
	}
	],

	
	 bbar : new Ext.PagingToolbar({
		  	  pageSize:30,//设置为每页30条记录
		      store : studentAdmissionInfostore,
		      displayInfo : true,
		      firstText:'首页',
		      lastText:'尾页',
		      prevText:'上一页',
		      nextText:'下一页',
		      refreshText:'刷新',
		      displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		      emptyMsg : "没有记录",
		      doLoad:function(start) {
			        var params = {};
			        params.filter = studentAdmissionInfo_currentSearchFilter;
			        params.start = start;
			        params.limit = this.pageSize;
			        if (this.fireEvent("beforechange", this, params) !== false) {
			            this.store.load({params:params});
			        }
			    }
	 })	 
	 
});








function print() {
	
	studentAdmissionInfostore.load({
		params : {
			filter : studentAdmissionInfo_currentSearchFilter,
			start : 0,
			limit : 20
		}
	});
	
	StudentController.getSchoolName(function(data){
		if(data){				
			xxmc=data;			
		}
		else{
   			Ext.MessageBox.alert('提示', "得不到学校名称！");
   		}
  	}); 

	var params = {};
	   params.fliter = studentAdmissionInfo_currentSearchFilter;
	   params.start = 0;
	   params.limit = 10000;
	   studentAdmissionInfostore_hidden.load({params:params,callback: function(){
		   
		   
	
			
		  var fileWebPath="";
			
			//异步回调得到文件的相对路径
			StudentController.getStudentPhotoPath(function(filepath){
			
				fileWebPath=getRootPath()+filepath;//拼接文件路径成绝对路径
				
				var tableId=document.getElementById("printTable");                                					
				if (tableId!=null) {
					tableId.parentNode.removeChild(tableId);
					     
					}
			
					var printobject=studentAdmissionInfostore_hidden.getAt(0);//得到grid中选中的记录
					
				
						
				
								var big_table=document.createElement("TABLE");
						       big_table.setAttribute('border','1');
						       big_table.setAttribute('width', '350');	
						       
						       var bigTbody=document.createElement("TBODY");  
							
							var small_table=document.createElement("TABLE");
						        small_table.setAttribute('border','0');
						        small_table.setAttribute('width', '340');					
						   //     small_table.setAttribute('align', 'center');
					//	        small_table.setAttribute('border-collapse', 'collapse');
						        small_table.setAttribute('cellspacing', '0');
						        small_table.setAttribute('cellpadding', '1');
						    
					        var smallTbody=document.createElement("TBODY");  
					        
					        
					        //表格设计
					        //tr1(td1)  				浙江省高校计算机等级考试
					        //tr2(td2,td3)						准  考  证                                          照片
					        //tr3(td20,td21,td3)                             		        照片    
					        //tr4(td4,td3)			学校名称                   					        照片
					        //tr5(td5,td3)  				******学校         			        照片
					        //tr6(td6)				应试等级及语种：一级windows
					        //tr7(td7)				准考证号：11111111111111
					        //tr8(td8)				姓名：张三
					        //tr9(td9)				理论考试地点：*******
					        //tr10(td10)			理论考试时间：*******
					        //tr11(td11)			上机考试地点：*******
					        //tr12(td12) 		           上机考试时间：******    
					        //tr13(td13)     		注：请务必带好准考证、身份证（学生证或考试证可替代）
					        //tr14(td14)  			空白
					        
					        
						    var tr1=document.createElement("TR");
						    var tr2=document.createElement("TR");
						    var tr3=document.createElement("TR");
						    var tr4=document.createElement("TR");
						    var tr5=document.createElement("TR");
						    var tr6=document.createElement("TR");
						    var tr7=document.createElement("TR");
						    var tr8=document.createElement("TR");
						    var tr9=document.createElement("TR");
						    var tr10=document.createElement("TR");
						    var tr11=document.createElement("TR");
						    var tr12=document.createElement("TR");
						    var tr13=document.createElement("TR");
						    var tr14=document.createElement("TR");
						    var tr15=document.createElement("TR");
						    
						    var tr18=document.createElement("TR");
						    var tr19=document.createElement("TR");
					
						    var td1=document.createElement("TD");
						    var td2=document.createElement("TD");
						    var td3=document.createElement("TD");
						    var td4=document.createElement("TD");
						    var td5=document.createElement("TD");
						    var td6=document.createElement("TD");
						    var td7=document.createElement("TD");
						    var td8=document.createElement("TD");
						    var td9=document.createElement("TD");
						    var td10=document.createElement("TD");
						    var td11=document.createElement("TD");
						    var td12=document.createElement("TD");
						    var td13=document.createElement("TD");
						    var td14=document.createElement("TD");
						    var td15=document.createElement("TD");
						    
						    var td18=document.createElement("TD");
						    var td19=document.createElement("TD");
						    
						    var td20=document.createElement("TD");
						    var td21=document.createElement("TD");
						   										   								    
						    
						    var div1 = document.createElement('div');
				            div1.style.fontSize='18px';
				            div1.style.fontWeight='bold';
				            var text = document.createTextNode('浙江省高校计算机等级考试');
				            div1.style.textAlign = "center";
				            div1.appendChild(text);
						    td1.appendChild(div1);
						    td1.colSpan=3;
						    tr1.appendChild(td1);
							smallTbody.appendChild(tr1);							
							 
						
							   
					
						       var div2 = document.createElement('div');
					           div2.style.fontSize = '17px';
					           div2.style.fontWeight='bold';
					           var text = document.createTextNode('准  考  证');
					           div2.style.textAlign = "right";	
					      //     div2.style.paddingLeft = '100px';
					           td2.colSpan=2;
					           div2.appendChild(text);
					           td2.appendChild(div2);
						       tr2.appendChild(td2);
						       
						       
						       
							     td3.rowSpan=4;
								 var div3=document.createElement('div');											 										
								 div3.setAttribute('id', 'div3id');
								 td3.setAttribute('id', 'td3id');
								 var imgNode = document.createElement('img');
								 imgNode.setAttribute('id', 'photo');																					    							
								 div3.style.height='85px';
								 div3.style.width='75px';
								 div3.style.float="right";
								 imgNode.style.height='100%';
								 imgNode.style.width='100%';
								 td3.style.marginRight = '0px';
								 td3.style.paddingRight = '0px';
								 div3.style.marginRight = '0px';
								 div3.style.paddingRight = '0px';
								
								 imgNode.setAttribute('overflow','hidden');											 											 									
								 var filewebpath=fileWebPath+printobject.get("studentnum")+".jpg";//拼接好文件全路径					
								 imgNode.setAttribute('src',filewebpath);																			 
							//	 imgNode.setAttribute('src','C:/3.jpg');
								 div3.appendChild(imgNode);																		 
								 td3.appendChild(div3);
								 tr2.appendChild(td3);	
						         smallTbody.appendChild(tr2); 
						   										       
						       //td20、td21只是让tr3划分三格的作用，没实际太大意义
						         td20.setAttribute('width', '600');
						         td21.style.width='900';
						       //td20.setAttribute('height', '20');
						     //  td21.setAttribute('width', '900');
						         tr3.appendChild(td20);
						         tr3.appendChild(td21);	                       
						         smallTbody.appendChild(tr3); 
							   									
	
						        var text=document.createTextNode("学校名称:  ");
						        var div4= document.createElement('div');
						        div4.style.fontSize = '13px';									
						        div4.style.fontWeight='bold';
						        div4.style.paddingLeft = '10px';
						        td4.colSpan=2;									
						        div4.appendChild(text);
								td4.appendChild(div4);
								tr4.appendChild(td4);				
								smallTbody.appendChild(tr4); 
								
								
								
								td5.colSpan=2;
								var text=document.createTextNode(xxmc);//xxmc学校名称
							    var div5 = document.createElement('div');
						        div5.style.textAlign = "center";
						        div5.style.fontSize = '13px';
						        div5.style.fontWeight='bold';
						        div5.appendChild(text);
								td5.appendChild(div5);
								tr5.appendChild(td5);
							    smallTbody.appendChild(tr5); 
						
							    
							   
							    
							    td6.colSpan=3;
							    var text=document.createTextNode("应试等级及语种:  "+printobject.get("exLanguage"));
							    var div6= document.createElement('div');
						        div6.style.fontSize = '13px';
						        div6.style.fontWeight='bold';
						        div6.style.paddingLeft = '10px';
						        div6.appendChild(text);
								td6.appendChild(div6);
								tr6.appendChild(td6);
								smallTbody.appendChild(tr6); 
								 
										
							
											
							    var text=document.createTextNode("准考证号:  "+printobject.get("examnum"));
							    var div7= document.createElement('div');
						        div7.style.fontSize = '13px';
						        div7.style.fontWeight='bold';
						        div7.style.paddingLeft = '10px';
						        div7.appendChild(text);
						        td7.colSpan=3;
								td7.appendChild(div7);
								tr7.appendChild(td7);
							    smallTbody.appendChild(tr7); 
							    
							    var text=document.createTextNode("院   系:  "+printobject.get("exCollege"));
							    var div18= document.createElement('div');
						        div18.style.fontSize = '13px';
						        div18.style.fontWeight='bold';
						        div18.style.paddingLeft = '10px';
						        div18.appendChild(text);
						        td18.colSpan=3;
								td18.appendChild(div18);
								tr18.appendChild(td18);
							    smallTbody.appendChild(tr18); 
							    
							    
							    var text=document.createTextNode("班   级:  "+printobject.get("classnum"));
							    var div19= document.createElement('div');
						        div19.style.fontSize = '13px';
						        div19.style.fontWeight='bold';
						        div19.style.paddingLeft = '10px';
						        div19.appendChild(text);
						        td19.colSpan=3;
								td19.appendChild(div19);
								tr19.appendChild(td19);
							    smallTbody.appendChild(tr19); 
												
												
								var text=document.createTextNode("姓   名:  "+printobject.get("name")+" ("+printobject.get("studentnum")+")");
								var div8= document.createElement('div');
						        div8.style.fontSize = '13px';
						        div8.style.fontWeight='bold';
						        div8.style.paddingLeft = '10px';
						        div8.appendChild(text);
						        td8.colSpan=3;
								td8.appendChild(div8);
								tr8.appendChild(td8);
							    smallTbody.appendChild(tr8); 
							    
							    					
							   
							    
							    
							    if(printobject.get("theoryroomlocation")=="无")
							    {
							    	var text=document.createTextNode("理论考试地点:  无");
							    }
							    else
							    {
							    	var text=document.createTextNode("理论考试地点:  "+printobject.get("exCampus")+"  第"+printobject.get("logicExamroomNum")+"考场       "+printobject.get("theoryroomlocation"));
							    }
							    var div9= document.createElement('div');
						        div9.style.fontSize = '13px';
						        div9.style.fontWeight='bold';
						        div9.style.paddingLeft = '10px';
						        td9.colSpan=3;
						        div9.appendChild(text);
								td9.appendChild(div9);
								tr9.appendChild(td9);
						        smallTbody.appendChild(tr9); 
													
							   	
							    
						        if(printobject.get("theoryroomlocation")=="无")
							    {
							    	var text=document.createTextNode("理论考试时间:  无");
							    }
							    else
							    {
							    	var text=document.createTextNode("理论考试时间:  "+printobject.get("theorystarttime")+"—"+printobject.get("theoryendTime"));
							    }
							    var div10= document.createElement('div');
						        div10.style.fontSize = '13px';
						        div10.style.fontWeight='bold';
						        div10.style.paddingLeft = '10px';
								div10.appendChild(text);
								td10.colSpan=3;
								td10.appendChild(div10);
								tr10.appendChild(td10);
							    smallTbody.appendChild(tr10); 
							    
								if(printobject.get("operateroomlocation")=="无")
								{
									var text=document.createTextNode("上机考试地点:  无");
								}
								else
								{
									var text=document.createTextNode("上机考试地点:  "+printobject.get("exCampus")+"  "+printobject.get("operateroomlocation")+"   第"+printobject.get("operateseat")+"座位");
								}
								var div11= document.createElement('div');
						        div11.style.fontSize = '13px';
						        div11.style.fontWeight='bold';
						        div11.style.paddingLeft = '10px';
						        div11.appendChild(text);
						        td11.colSpan=3;
								td11.appendChild(div11);
								tr11.appendChild(td11);
								smallTbody.appendChild(tr11); 
															
								if(printobject.get("operateroomlocation")=="无")
								{
									var text=document.createTextNode("上机考试时间:  无");
								}
								else
								{
									var text=document.createTextNode("上机考试时间:  "+printobject.get("operatestarttime")+"—"+printobject.get("operateendTime"));
								}							
								var div12= document.createElement('div');
						        div12.style.fontSize = '13px';
						        div12.style.fontWeight='bold';
						        div12.style.paddingLeft = '10px';
						        td12.colSpan=3;
						        div12.appendChild(text);
								td12.appendChild(div12);
								tr12.appendChild(td12);
								smallTbody.appendChild(tr12); 
								
					
								
								
							
								var text1=document.createTextNode('注：考生凭准考证、身份证(或学生证)参加考试。理论考试需带2B铅笔等考试用品。考试按规定时间提前15分钟进入考场。禁止携带手机等通讯工具进入考场。');
							
								var div13= document.createElement('div');
						        div13.style.fontSize = '12px';
						        div13.style.paddingLeft = '10px';
						        div13.appendChild(text1);
								td13.colSpan=3;
								td13.appendChild(div13);
								tr13.appendChild(td13);							
							    smallTbody.appendChild(tr13); 
							    
							   
							    
							    //tr14目的：留一行空白处
							    td14.colSpan=3;	
							    td14.setAttribute('height', '6');
							    var text=document.createTextNode(" ");
							    td14.appendChild(text);
								tr14.appendChild(td14);
							    smallTbody.appendChild(tr14);
						    				    
							    small_table.appendChild(smallTbody);
							    
							    var big_div= document.createElement('div');
							    big_div.style.width="340";
							    big_div.style.border="1 #000000 solid";
							   big_div.appendChild(small_table);
							 
					
				var _div=document.getElementById("hiddentable");
			
				_div.appendChild(big_div); 
												
				document.body.innerHTML=document.getElementById("hiddentable").innerHTML;
			
			
		});
		}});
	   }
	   
	   
	   
				
	function getRootPath()
	{    //获取当前网址，如： http://localhost:8080/uimcardprj/share/meun.jsp    
	var curWwwPath=window.document.location.href;    
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp   
	var pathName=window.document.location.pathname;    
	var pos=curWwwPath.indexOf(pathName);    
	//获取主机地址，如： http://localhost:8080    
	var localhostPaht=curWwwPath.substring(0,pos);    
	//获取带"/"的项目名，如：/uimcardprj   
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);    
	return (localhostPaht+projectName);
	}
</script>

<script type="text/javascript">
Ext.onReady(function(){
	this.print();
});
</script>

<div id="hiddentable" style="display:none"></div>

</body>
</html>