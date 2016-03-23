var studentAdmissionInfo_currentSearchFilter = "";

var remark='';

var comboCollegeStore = new Ext.data.JsonStore
({
	fields:[
	        'value',
	        'name'
	        ]
});




var comboClassNameStore = new Ext.data.JsonStore
({
	fields:[
	        'value',
	        'name'
	        ]
});



var studentAdmissionInfo = new Ext.data.Record.create
([
		{
			name:'id'	
		},{
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


var RemarkAboutZKZInfo = new Ext.data.Record.create
([
		{
			name:'name'	
		},{
			name:'remark'
		}							                                                    
]);


var studentAdmissionInfostore = new Ext.data.Store
({
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getStudentAdmissionInfo
       })
});




var RemarkAboutZKZInfostore = new Ext.data.Store
({
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, RemarkAboutZKZInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getRemarkAboutZKZ
       })
 });
    
  

var studentAdmissionInfostore_hidden = new Ext.data.Store
({
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getStudentAdmissionInfo
       })
 });


var sm=new Ext.grid.CheckboxSelectionModel();

var studentAdmissionInfoGrid = new Ext.grid.GridPanel
({
	region:'center',
	id : 'studentadmissioncardgrid',
	store :studentAdmissionInfostore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
//	autoExpandColumn : 'name',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	sm:sm,
	columns : [new Ext.grid.RowNumberer(),sm,
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :100,
	 	sortable :true
	},{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :140,
	 	sortable :true
	},{
		id : 'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :120,
	 	sortable :true
	},{
		id : 'exLanguage',
		header : '语种',
	  	dataIndex :'exLanguage',
	  	width :200,
	 	sortable :true
	},{
		id : 'exCollege',
		header:'学院',
	  	dataIndex:'exCollege',
	  	width :140,
	 	sortable :true
	},{
		id : 'exProfession',
		header : '专业',
	  	dataIndex :'exProfession',
	  	width :120,
	 	sortable :true
	},{
		id : 'classnum',
		header : '班级',
	  	dataIndex :'classnum',
	  	width :120,
	 	sortable :true
	},{
		id : 'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :180,
	 	sortable :true
	},{
		id : 'exCampus',
		header : '校区名称',
	  	dataIndex :'exCampus',
	  	width :140,
	 	sortable :true
	
	},{
		id : 'logicExamroomNum',
		header : '考场号',
	  	dataIndex :'logicExamroomNum',
	  	width :90,
	 	sortable :true
	},{
		id : 'theoryroomlocation',
		header : '理论考试地址',
	  	dataIndex :'theoryroomlocation',
	  	width :200,
	 	sortable :true
	},{
		id : 'theorystarttime',
		header : '理论考试开始时间',
	  	dataIndex :'theorystarttime',
	  	width :160,
	 	sortable :true
	},{
		id : 'theoryendTime',
		header : '理论考试结束时间',
	  	dataIndex :'theoryendTime',
	  	width :140,
	 	sortable :true
	},{
		id : 'operateroomlocation',
		header : '上机考试地址',
	  	dataIndex :'operateroomlocation',
	  	width :200,
	 	sortable :true
	},{
		id : 'operatestarttime',
		header : '上机考试开始时间',
	  	dataIndex :'operatestarttime',
	  	width :160,
	 	sortable :true
	},{
		id : 'operateendTime',
		header : '上机结束时间',
	  	dataIndex :'operateendTime',
	  	width :140,
	 	sortable :true
	
	},{
		id : 'operateseat',
		header : '上机座位号',
	  	dataIndex :'operateseat',
	  	width :80,
	 	sortable :true
	
	}],

	 bbar : new Ext.PagingToolbar
	 ({
		 	  id:"pagingToolBar",
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
		      doLoad:function(start) 
		      {
			        var params = {};
			        params.filter = studentAdmissionInfo_currentSearchFilter;
			        params.start = start;
			        params.limit = this.pageSize;
			        if (this.fireEvent("beforechange", this, params) !== false) 
			        {
			            this.store.load({params:params});
			        }
			    }
	 })	 
	 
});


var form=new Ext.form.FormPanel
({
	   title:"选择查询条件",
	   region:"north",
	   frame:true,
	   width:300,
	   height:60,
	   labelWidth:40,
	   labelAlign:'right',
	 
	   items:
	   [{
			 layout:'column',
			 items:
			 [{
					  columnWidth:.25,	
					  layout:'form',				 
					  items:
					  [{
			        	     xtype:'combo',	
			        	     id:'college',
			        	     fieldLabel:'学院',
			        	     hiddenName:'and$student-exCollege-name$=$value',
			        	     triggerAction:'all',
			        	     store:comboCollegeStore,			        	    	 
			        	     displayField:'name',
			        	     valueField:'value',
			        	     mode:'local',
			        	     anchor:'97.5%',
			        	     editable:false,
			        	//   allowBlank: false,
			        	     emptyText:'---请选择---',
			        	     listeners: 
			        	     {	                	  
			            	  	  select:function()
			            	  	  {
			            	  		   var collegename=Ext.get('college').dom.value;
			            	  		   //得到班级combobox的列表
	                            	   StudentController.getClassNameList(collegename,function(data)
	                            	   {
	                            			if(data)
	                            			{
	                            				comboClassNameStore.loadData(data);
	                            			//	Ext.get('class').dom.value='---请选择---';
	                            			}
	                            	  	});
	                            	   studentAdmissionInfo_currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
	                            	   studentAdmissionInfostore.load
	                            	   ({
						        			params : 
						        			{
						        				filter : studentAdmissionInfo_currentSearchFilter,
						        				start : 0,
						        				limit : 30
						        			}
						        		});						        					            	  				            	  		
			            	  	  }			            	  		
			                  }
			        	 }] 
			          },
			          {
							  columnWidth:.25,	
							  layout:'form',				 
							  items:
							  [{
					        	     xtype:'combo',
					        	     id:'class',
					        	     fieldLabel:'班级',
					        	     hiddenName:'and$student-classnum$=$value',
					        	     name:'bjname',
					        	     triggerAction:'all',
					        	     store:comboClassNameStore,			        	    	 
					        	     displayField:'name',
					        	     valueField:'value',
					        	     mode:'local',
					        	     anchor:'97.5%',
					        	     editable:false,
					        	    // allowBlank: false,
					        	     emptyText:'---请选择---',
					        	     listeners: 
					        	     {	                	  
					            	  	  select:function()
					            	  	  {					            	  		
					            	  		  studentAdmissionInfo_currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
					            	  		  studentAdmissionInfostore.load
					            	  		  ({
								        			params : 
								        			{
								        				filter : studentAdmissionInfo_currentSearchFilter,
								        				start : 0,
								        				limit : 30
								        			}
								        		});								        	            	  		
					            	  	  }
					                  }
					          }] 
					   }]			       
			   }]		   		   	  	   
});


	   		   
var printForm=new Ext.form.FormPanel
({
		store :RemarkAboutZKZInfostore,
		title:"选择打印条件",
		region:"south",
		frame:true,
		width:300,
		height:120,
		labelWidth:100,
		labelAlign:'right',
	 
		
		items:
		[{
			layout:'column',
			items:
			[{
		        	  columnWidth:.75,	
		        	  layout:'form',
					  items:
					  [{
						  xtype:'textarea',
						  id:'abc',
						  fieldLabel:'标注(少于100字)',
						  name:'remark',					 
						//value:'注：考生凭准考证、身份证(或学生证)参加考试。理论考试需带2B铅笔等考试用品。考试按规定时间提前15分钟进入考场。禁止携带手机等通讯工具进入考场。',				
					//	  emptyText:'注：考生凭准考证、身份证(或学生证)参加考试。理论考试需带2B铅笔等考试用品。考试按规定时间提前15分钟进入考场。禁止携带手机等通讯工具进入考场。',
						  cls:"Wdate",
		        	      width:200,
		        	      anchor:'98%',
		        	//    allowBlank: false,
					      listeners: 
					      {	                	  
					    	  blur:function()
					    	  {
			            	  		var re=Ext.get('abc').dom.value;
			            	  		var key='0';
			            	  		if (printForm.getForm().findField("remark").getValue().length>100)
									{
											Ext.MessageBox.alert("提示","标注字数请限定在100字内" );
											key='1';
									}
	            	  	
	            	  		   //得到班级combobox的列表
			            	  		if(key=='0')
			            	  		{
			            	  			StudentController.saveRemarkAboutZKZ(re,function(data)
			            	  			{
			                     			if(data)
			                     			{
			                     			
			                     			//	Ext.MessageBox.alert(re);
			                     			}
			            	  			});
			            	  		}
			            	  		window.location=window.location.href;  //及时刷新页面
					    	  }
					      }
					  }]					  							
			     }]	         
		   }],
		   tbar:
		   [{
				text : '准考证设计',
				iconCls : 'edit',
				onClick : function() 
				{
				var canvas_temp = document.createElement("canvas");
				if(canvas_temp.getContext)//判断浏览器是否支持canvas对象
				{
					studentsSelectedArray = studentAdmissionInfoGrid.getSelectionModel().getSelections();
												
					//异步回调得到文件的相对路径
					if(studentsSelectedArray.length!=1){
						Ext.MessageBox.alert('提示', "请选择一条记录!");
					}else{
						designAdmissionCard(studentsSelectedArray);
					}
				}
				
				else { //浏览器不支持canvas，要求用户更换浏览器chrome
					
					   alert("您使用的浏览器不支持canvas对象，请更换到chrome浏览器下来打印准考证！抱歉！");
				}				
				}
			},'-',
			{
				text : '打印选中项',
				iconCls : 'print',
				onClick : function() 
				{
					var canvas_temp = document.createElement("canvas");
					if(canvas_temp.getContext)//判断浏览器是否支持canvas对象
					{
					studentsSelectedArray = studentAdmissionInfoGrid.getSelectionModel().getSelections();
					if(studentsSelectedArray.length>0){
						printSelectRecord(studentsSelectedArray);
					}else{
						Ext.MessageBox.alert('提示', "请至少选择一条记录!");
					}
					}
					
					else { //浏览器不支持canvas，要求用户更换浏览器chrome
						
						   alert("您使用的浏览器不支持canvas对象，请更换到chrome浏览器下来打印准考证！抱歉！");
					}
				}
			},'-',
			{
    			text : '打印指定页面',
    			iconCls : 'print',
    			onClick : function(){
    				var canvas_temp = document.createElement("canvas");
    				if(canvas_temp.getContext)//判断浏览器是否支持canvas对象
    				{
    				var totalRecordNum=studentAdmissionInfostore.reader.jsonData.totalProperty;
    				if(totalRecordNum==0){
    					Ext.MessageBox.alert('提示', "没有可打印的记录!");
    					return;
    				}
    				pageSelectWindow.show();
    				}
    				
    				else { //浏览器不支持canvas，要求用户更换浏览器chrome
    					
    					   alert("您使用的浏览器不支持canvas对象，请更换到chrome浏览器下来打印准考证！抱歉！");
    				}
    			}
			},'-',{
				text : '选择默认打印机',
				 iconCls : 'print',
			        scope: this,
			        handler:function(){
			    			var returnPrinter=LODOP.SELECT_PRINTER();
			    			if(returnPrinter<0){
			    				return;
			    			}else{
			    				currentPrinter=returnPrinter;
			    				LODOP.SET_PRINTER_INDEX(currentPrinter);
			    			}
			        }
			},'-',
			{
				text : '导出考生准考证信息',
				 iconCls : 'upload-icon',
			        scope: this,
			        handler:function(){
			        	exportStudentsAdmissionExcel();
			        }
			}]

	
});


var endProcessPoint;
var startProcessPoint;

function initAndShowProcessBox(startPoint,endPoint){
	startProcessPoint=startPoint;
	endProcessPoint=endPoint;
	Ext.MessageBox.show({
	    title: '请稍候',//消息框标题
	    msg: '正在加载打印数据',//显示的文本
	    width:240,//宽度
	    progress:true,//此属性证明是滚动条
	    closable:false,//不可以关闭
	    animEl: 'mb6'//飞出来的
	});
	Ext.MessageBox.updateProgress(0,'已经加载 0%');
}

function updateProcessBox(currentPoint){
	if(printModel==0)
		return;
	if(currentPoint>endProcessPoint)
		 Ext.MessageBox.hide();
	else
		Ext.MessageBox.updateProgress(Math.floor((currentPoint-startProcessPoint)/(endProcessPoint-startProcessPoint+1)*100%101)/100,
				'已经加载 ' + Math.floor((currentPoint-startProcessPoint)/(endProcessPoint-startProcessPoint+1)*100%101) + ' %');
}

function hidenProcessBox(){
	Ext.MessageBox.hide();
}

var  pageSelectfields = [
	{columnWidth:1,layout:'form',labelWidth: 90,items:[
        	{xtype:'textfield',id: 'fromPage',fieldLabel: '开始页码',name: 'fromPage',anchor:'97.5%',allowBlank: false
        	}]},
    {columnWidth:1,layout:'form',labelWidth: 90,items:[
            {xtype:'textfield',id: 'endPage',fieldLabel: '结束页码',name: 'endPage',anchor:'97.5%',allowBlank: false
     }]}];

 var pageSelectForm = new Ext.FormPanel({
     id : 'pageSelectForm',
 	labelWidth: 80,
 	region : 'center',
     labelAlign : 'left',
     layout:'column',
     frame:true,
     autoHeight : true,
     border : false,
 	bodyStyle:'padding:5',
     items : [pageSelectfields]
 });

var fromPage=0;
var endPage=0;
 var pageSelectWindow = new Ext.Window({
        title: '打印页码设置',
        width: 350,
        height:200,
        closeAction : 'hide',
        layout: 'fit',
        border:false,
        modal: true,
        shadow: true,
        hideMode: Ext.isIE ? 'offsets' : 'display',
        plain:true,
        bodyStyle:'padding:5px;',
        buttonAlign:'center',
        items: [pageSelectForm],
        buttons: [{
 	    text : '打印',
 	    handler : function() {
 	    	fromPage=parseInt(Ext.getCmp("fromPage").getValue());
 	    	endPage=parseInt(Ext.getCmp("endPage").getValue());
 	    	var pageSize=Ext.getCmp("pagingToolBar").pageSize;
 	    	var totalRecordNum=studentAdmissionInfostore.reader.jsonData.totalProperty;
 	    	var regPatrn=/^[0-9]{1,20}$/;
 	    	if(!(regPatrn.exec(fromPage)&&regPatrn.exec(endPage))){
 	    		Ext.MessageBox.alert('提示', "页码必须为数字!");
 	    		return;
 	    	}
 	    	if(fromPage>endPage)
 	    	{
 	    		Ext.MessageBox.alert('提示', "开始页码必须大于结束页码!");
 	    		return;
 	    	}
 	    	if(endPage>(totalRecordNum/pageSize+1)){
 	    		Ext.MessageBox.alert('提示', "结束页码设置过大!");
 	    		return;
 	    	}
 	    	if(fromPage<=0||totalRecordNum<=0){
 	    		Ext.MessageBox.alert('提示', "开始页码设置错误!");
 	    		return;
 	    	}
 	    	var endPoint=endPage*pageSize>totalRecordNum?totalRecordNum:endPage*pageSize;
 	    	endPoint--;
 	    	var startPoint=(fromPage-1)*pageSize;
 	    	printAllPages(startPoint,endPoint);
 		}}, {
 	    text : '取消',
 	    handler : function() {
 	    	pageSelectWindow.hide();
 	     }
 		}],
 		listeners:{
             "hide": function()
             {
            	 pageSelectWindow.hide();
             }
         }
   });

// js获取项目根路径，如： http://localhost:8080/uimcardprj
function getRootPath()
{    // 获取当前网址，如： http://localhost:8080/uimcardprj/share/meun.jsp
	var curWwwPath=window.document.location.href;    
	// 获取主机地址之后的目录，如： uimcardprj/share/meun.jsp
	var pathName=window.document.location.pathname;    
	var pos=curWwwPath.indexOf(pathName);    
	//获取主机地址，如： http://localhost:8080    
	var localhostPaht=curWwwPath.substring(0,pos);    
	//获取带"/"的项目名，如：/uimcardprj   
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);    
	return (localhostPaht+projectName);
}

function exportStudentsAdmissionExcel(){
	var f = document.getElementById('exportStudentsAdmissionExcel');
	f.action = '../exportStudentsAdmissionExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}





function studentAdmissionInfoGridInit(){
	
	StudentController.getCollegeList(function(data){
			if(data){
				comboCollegeStore.loadData(data);
		}
		});
	

  	
  //得到学校名称
  	StudentController.getSchoolName(function(data){
		if(data){				
			xxmc=data;	
		}
		else{
   			Ext.MessageBox.alert('提示', "得不到学校名称！");
   		}
  	}); 
  	
  	StudentController.getStudentPhotoPath(function(filepath){
  		if(filepath=="")
  			Ext.MessageBox.alert('提示', "得不到学校名称！");
  		else
  			filePath=filepath;//拼接文件路径成绝对路径
  			fileWebPath=getRootPath()+filePath;
	});

  	StudentController.getRemarkAboutZKZ(function(data)
  		  	{
  				if(data)
  				{
  					 remark=data;
  					 var a=document.getElementById("abc").value;    //方法１　 			
  					 document.getElementById("abc").innerText=remark;

  				}
  				else
				{
					 remark='注：考生凭准考证、身份证(或学生证)参加考试。理论考试需带2B铅笔等考试用品。考试按规定时间提前15分钟进入考场。禁止携带手机等通讯工具进入考场。';
					 var a=document.getElementById("abc").value;    //方法１　 
					 document.getElementById("abc").innerText=remark;
				}
  			});
  	

  	

  	StudentController.getRemarkAboutZKZ(function(data)
  	{
		if(data)
		{
			 remark=data;
			 var a=document.getElementById("abc").value;    //方法１　 			
			 document.getElementById("abc").innerText=remark;

		}
	});
  	
  	
	
    var params1 = {};
    params1.filter = studentAdmissionInfo_currentSearchFilter;
    params1.start = 0;
    params1.limit = 30;	
    
  	studentAdmissionInfostore.load({params:params1,  callback:function(records,operation,success)
  		{
  				if(records=="")
  					{
  						//Ext.MessageBox.alert('提示', "所有考试的理论考场和上机考场编排完毕，才能打印准考证!"); 					
  					}
  		}		
  	});
	//得到学院combobox的列表                                       
 	  		  		  	  
}



function printAdmissionCardPageInit() {
	new Ext.Viewport
	({
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [form,studentAdmissionInfoGrid,printForm],
		renderTo :Ext.getBody()
	});
	studentAdmissionInfoGridInit();
	
}
