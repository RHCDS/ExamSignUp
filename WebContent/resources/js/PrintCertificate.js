var passstudent_currentSearchFilter = "";

var filePath="";
var backgroundFilePath="";
var studentImage;
var certiInfoArray=new Array();
var collectPictureCount=0;
var errorImageCount=0;

var comboCollegeStore = new Ext.data.JsonStore({
	fields:[
	        'value',
	        'name'
	        ]
});

var comboClassNameStore = new Ext.data.JsonStore({
	fields:[
	        'value',
	        'name'
	        ]
});


var comboLanguageStore = new Ext.data.JsonStore({
	fields:[
	        'languagenum',
	        'languagename'
	        ]
});





var form=new Ext.form.FormPanel({
	   title:"选择查询条件",
	   region:"north",
	   frame:true,
	   width:300,
	   height:60,
	   labelWidth:40,
	   labelAlign:'right',
	   items:[{
		 layout:'column',
		 items:[{
			  columnWidth:.25,	
			  layout:'form',				 
			  items:[{
	        	     xtype:'combo',
	        	     fieldLabel:'语种',
	        	     hiddenName:'and$student-exLanguage-languagenum$=$value',
	        	     triggerAction:'all',
	        	     store:comboLanguageStore,			        	    	 
	        	     displayField:'languagename',
	        	     valueField:'languagenum',
	        	     mode:'local',
	        	     anchor:'97.5%',
	        	     editable:false,
	        	   //  allowBlank: false,
	        	     emptyText:'---请选择---',
	        	     listeners: {	                	  
	            	  	  select:function(){
	            	  		passstudent_currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
			        		passstudentstore.load({
			        			params : {
			        						filter : passstudent_currentSearchFilter,
			        						start : 0,
			        						limit : 20
			        			}
			        		});
	            	  		
	            	  	  }
	                  }
	        	    }] 
	          },{
				  columnWidth:.25,	
				  layout:'form',				 
				  items:[{
		        	     xtype:'combo',
		        	     id:'college',
		        	     fieldLabel:'院系',
		        	     hiddenName:'and$student-exCollege-name$=$value',
		        	     triggerAction:'all',
		        	     store:comboCollegeStore,			        	    	 
		        	     displayField:'name',
		        	     valueField:'value',
		        	     mode:'local',
		        	     editable:false,
		        	     anchor:'97.5%',
		        	   //  allowBlank: false,
		        	     emptyText:'---请选择---',
		        	     listeners: {	                	  
		            	  	  select:function(){
		            	  		var collegename=Ext.get('college').dom.value;
		            	  		   //得到班级combobox的列表
                         	   StudentController.getClassNameList(collegename,function(data){
                         			if(data){
                         				comboClassNameStore.loadData(data);
                         				Ext.get('class').dom.value='---请选择---';
                         			}
                         	  	});
		            	  		passstudent_currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
				        		passstudentstore.load({
				        			params : {
				        						filter : passstudent_currentSearchFilter,
				        						start : 0,
				        						limit : 20
				        			}
				        		});
		            	  		
		            	  	  }
		                  }
		        	    }] 
		          }, {
					  columnWidth:.25,	
					  layout:'form',				 
					  items:[{
			        	     xtype:'combo',
			        	     id:'class',
			        	     fieldLabel:'班级',
			        	     hiddenName:'and$student-classnum$=$value',
			        	     triggerAction:'all',
			        	     store:comboClassNameStore,			        	    	 
			        	     displayField:'name',
			        	     valueField:'value',
			        	     mode:'local',
			        	     editable:false,
			        	     anchor:'97.5%',
			        	   //  allowBlank: false,
			        	     emptyText:'---请选择---',
			        	     listeners: {	                	  
			            	  	  select:function(){
			            	  		passstudent_currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
					        		passstudentstore.load({
					        			params : {
					        						filter : passstudent_currentSearchFilter,
					        						start : 0,
					        						limit : 20
					        			}
					        		});
			            	  		
			            	  	  }
			                  }
			        	    }] 
			          }]
		       
		   }]		   		   
});

var printForm=new Ext.form.FormPanel({
	   title:"选择打印条件",
	   region:"south",
	   frame:true,
	   width:300,
	   height:100,
	   labelWidth:80,
	   labelAlign:'right',
	   items:[{
		 layout:'column',
		 items:[/*{
	        	  columnWidth:.25,	
	        	  layout:'form',
				  items:[{
					  xtype:'datefield',
					  id:'joinYMID',
					  fieldLabel:'参加年月',
					  emptyText:'请选择',
					  format:'Y-m-d',
	        	      name:'joinYM',
	        	      cls:"Wdate",
	        	      width:100,
	        	      anchor:'98%',
	        	      allowBlank: false
				  }]
			      
		          },*/{
		        	  columnWidth:.25,	
		        	  layout:'form',
		        
					  items:[{
						  xtype:'datefield',
						  id:'publicDateID',
						  fieldLabel:'发证日期',
						  emptyText:'请选择',
						  format:'Y-m-d',
		        	      name:'publicDate',        	     
		        	      cls:"Wdate",
		        	      width:100,
		        	      anchor:'98%',
		        	      allowBlank: false			        	      
					  }]
		  	
			          }
		           ]	         
		   }],
		   tbar:[ {
				text : '证书设计',
				iconCls : 'edit',
				onClick : function() {
				
					var canvas_temp = document.createElement("canvas");
					if(canvas_temp.getContext)//判断浏览器是否支持canvas对象
					{
						if(printForm.getForm().isValid()  && passstudentgrid.getSelectionModel().hasSelection()&& 
								passstudentgrid.getSelectionModel().getSelections().length==1){
							
							var publicDate=Ext.getCmp("publicDateID").getValue().format('Y-m-d');//得到填发日期
							LODOP.PRINT_INITA("0mm","0mm","210mm","148mm","套打等级考试证书模板");
							var selections = passstudentgrid.getSelectionModel().getSelections();
							var fileWebPath="";
								
							fileWebPath=getRootPath()+filePath;//拼接文件路径成绝对路径

							var printobject=selections[selections.length-1];//得到grid中选中的记录
						    var filewebpath=fileWebPath+printobject.get("studentnum")+".jpg?rnd="+Math.random();//拼接好文件全路径
						    
						    var yearAndM=printobject.get("examnum");
						    var year="20"+yearAndM.substring(0,2);
						    var month=yearAndM.substring(2,3);
						    var upYearOrNor="上";
							if(month==2)upYearOrNor="下";//判断是上半年还是下半年	
						    
							var certiInfoArray=new Array(printobject.get("exInstitution"),
									printobject.get("examnum"),
									publicDate,
									printobject.get("name"),
									printobject.get("sex"),
									year,
									upYearOrNor,
									printobject.get("exLanguage"),
									printobject.get("score"),
									null);
							
							studentImage=new Image();
							
							studentImage.onload=function(){
								
								var canvas = document.createElement("canvas");								
								canvas.width = studentImage.width;							
								canvas.height = studentImage.height;
								
									var ctx = canvas.getContext("2d");//???????????????有问题	,ie不支持							
									ctx.drawImage(studentImage, 0, 0);
									
									var dataURL = canvas.toDataURL("image/jpg");
									certiInfoArray[certiInfoArray.length-1]=dataURL;							
									CreatePrintPage(certiInfoArray);							
									LODOP.PRINT_SETUP();
									reSizePrintParam();																							
							};
							studentImage.onerror=function(){
								studentImage=null;
								Ext.MessageBox.alert("提示","无法加载该考生照片！" );
							};
							studentImage.src=filewebpath;
							
			        	}else{
			        		
			        		if(!printForm.getForm().isValid())	        			
			        				Ext.MessageBox.alert('提示',"请在打印条件中选择发证日期！");
			        		if(passstudentgrid.getSelectionModel().getSelections().length!=1)
			        			Ext.MessageBox.alert('提示', "请选择一条记录!");
			        	}
						
						
					}
					else { //浏览器不支持canvas，要求用户更换浏览器chrome
						
						   alert("您使用的浏览器不支持canvas对象，请更换到chrome浏览器下来打印证书！抱歉！");
					}
									
				}
			},'-',{
				text : '打印当前页',
				iconCls : 'print',
				onClick : function() {
					var canvas_temp = document.createElement("canvas");
					if(canvas_temp.getContext)//判断浏览器是否支持canvas对象
					{
					
					//先检查表单数据是不是合法
					if(printForm.getForm().isValid()  && passstudentgrid.getSelectionModel().hasSelection()){
						
						var publicDate=Ext.getCmp("publicDateID").getValue().format('Y-m-d');//得到填发日期
						    LODOP.PRINT_INITA("0mm","0mm","210mm","150mm","套打等级考试证书模板");
							var selections = passstudentgrid.getSelectionModel().getSelections();
							var fileWebPath="";	
							fileWebPath=getRootPath()+filePath;//拼接文件路径成绝对路径

							//生成所有打印页面
							collectPictureCount=selections.length;
							errorImageCount=0;
							certiInfoArray= new Array();
							
							for(var i=0;i<selections.length;i++)
							{
								var printobject=selections[i];//得到grid中选中的记录
							    var filewebpath=fileWebPath+printobject.get("studentnum")+".jpg?rnd="+Math.random();//拼接好文件全路径
							    
							    var yearAndM=printobject.get("examnum");
							    var year="20"+yearAndM.substring(0,2);
							    var month=yearAndM.substring(2,3);
							    var upYearOrNor="上";
								if(month==2)upYearOrNor="下";//判断是上半年还是下半年
							    
								certiInfoArray[i]=new Array(printobject.get("exInstitution"),
										printobject.get("examnum"),
										publicDate,
										printobject.get("name"),
										printobject.get("sex"),
										year,
										upYearOrNor,
										printobject.get("exLanguage"),
										printobject.get("score"),
										new Image());
								
								certiInfoArray[i][certiInfoArray[i].length-1].onload=function(image){
									collectPictureCount--;
									var canvas = document.createElement("canvas");
									canvas.width = image.target.width;
									canvas.height = image.target.height;	
									
										var ctx = canvas.getContext("2d");
										ctx.drawImage(image.target, 0, 0);
	
										var dataURL = canvas.toDataURL("image/jpg");
										var i;
										for(i=0;i<certiInfoArray.length;i++){
											if(certiInfoArray[i][certiInfoArray[0].length-1]==image.target)
												break;
										}
										certiInfoArray[i][certiInfoArray[0].length-1]=dataURL;
										CreatePrintPage(certiInfoArray[i]);
										LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
										LODOP.PRINT();
									
								};
								certiInfoArray[i][certiInfoArray[i].length-1].onerror=function(image){
									errorImageCount++;
									collectPictureCount--;
									var i;
									for(i=0;i<certiInfoArray.length;i++){
										if(certiInfoArray[i][certiInfoArray[0].length-1]==image.target)
											break;
									}
									certiInfoArray[i][certiInfoArray[i].length-1]=null;
									if(collectPictureCount==0)
										Ext.MessageBox.alert("提示",errorImageCount+"个考生照片加载失败！" );
								};
								certiInfoArray[i][certiInfoArray[i].length-1].src=filewebpath;
							}									
		        	}else{
		        		
		        		if(!printForm.getForm().isValid())	        			
		        				Ext.MessageBox.alert('提示',"请在打印条件中选择发证日期！");
		        		if(!passstudentgrid.getSelectionModel().hasSelection())
		        			Ext.MessageBox.alert('提示', "请勾选待打印记录!");
		        	}
					}
					else { //浏览器不支持canvas，要求用户更换浏览器chrome
						
						   alert("您使用的浏览器不支持canvas对象，请更换到chrome浏览器下来打印证书！抱歉！");
					}				
					
				}
			},'-',{
				text : '打印全部',
				iconCls : 'print',
				onClick : function() {
					var canvas_temp = document.createElement("canvas");
					if(canvas_temp.getContext)//判断浏览器是否支持canvas对象
					{
					//先检查表单数据是不是合法
					if(printForm.getForm().isValid()){
						
						///var joinYM=Ext.getCmp("joinYMID").getValue().format('Y-m');//得到参加日期
						var publicDate=Ext.getCmp("publicDateID").getValue().format('Y-m-d');//得到填发日期
						///var  str=joinYM.split("-");
						///var year=str[0];//得到参加日期中的年份
						///var upYearOrNor="上";
						///if(str[1]>6)upYearOrNor="下";//判断是上半年还是下半年	
						
						 var params = {};
						   params.fliter = passstudent_currentSearchFilter;
						   params.start = 0;
						   params.limit = 10000;
						   hiddenstore.load({params:params,callback: function(){
							   
							   //判断数据库中是否有符合条件的数据
							   if (hiddenstore.getCount()>0) {
								    LODOP.PRINT_INITA("0mm","0mm","210mm","150mm","套打等级考试证书模板");
									//LODOP.SET_PRINT_PAGESIZE(1,"210mm","150mm","");
									
									var fileWebPath="";
									
									
									
								 fileWebPath=getRootPath()+filePath;//拼接文件路径成绝对路径
										
								
										
									//生成所有打印页面
								   collectPictureCount=hiddenstore.getCount();
									errorImageCount=0;
									certiInfoArray= new Array();
									for(var i=0;i<hiddenstore.getCount();i++)
									{
										var printobject=hiddenstore.getAt(i);//得到store中的记录
										
									    var filewebpath=fileWebPath+printobject.get("studentnum")+".jpg?rnd="+Math.random();//拼接好文件全路径
									    
									    var yearAndM=printobject.get("examnum");
									    var year="20"+yearAndM.substring(0,2);
									    var month=yearAndM.substring(2,3);
									    var upYearOrNor="上";
										if(month==2)upYearOrNor="下";//判断是上半年还是下半年
										
										certiInfoArray[i]=new Array(printobject.get("exInstitution"),
												printobject.get("examnum"),
												publicDate,
												printobject.get("name"),
												printobject.get("sex"),
												year,
												upYearOrNor,
												printobject.get("exLanguage"),
												printobject.get("score"),
												new Image());
										
										certiInfoArray[i][certiInfoArray[i].length-1].onload=function(image){
											collectPictureCount--;
											var canvas = document.createElement("canvas");
											canvas.width = image.target.width;
											canvas.height = image.target.height;
											
												var ctx = canvas.getContext("2d");
												ctx.drawImage(image.target, 0, 0);
	
												var dataURL = canvas.toDataURL("image/jpg");
												var i;
												for(i=0;i<certiInfoArray.length;i++){
													if(certiInfoArray[i][certiInfoArray[0].length-1]==image.target)
														break;
												}
												certiInfoArray[i][certiInfoArray[0].length-1]=dataURL;
												CreatePrintPage(certiInfoArray[i]);
												LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
												LODOP.PRINT();
											
											
										};
										certiInfoArray[i][certiInfoArray[i].length-1].onerror=function(image){
											errorImageCount++;
											collectPictureCount--;
											var i;
											for(i=0;i<certiInfoArray.length;i++){
												if(certiInfoArray[i][certiInfoArray[0].length-1]==image.target)
													break;
											}
											certiInfoArray[i][certiInfoArray[i].length-1]=null;
											if(collectPictureCount==0)
												Ext.MessageBox.alert("提示",errorImageCount+"个考生照片加载失败！" );
										};
										certiInfoArray[i][certiInfoArray[i].length-1].src=filewebpath;
										
										
										
									  //  CreatePrintPage(printobject.get("exInstitution"),printobject.get("examnum"),"www.zjccet.com",publicDate,printobject.get("name"),printobject.get("sex"),year,upYearOrNor,printobject.get("exLanguage"),printobject.get("score"),"<img src=\"" +filewebpath +"\"/>");	
									
									  //  LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
									//	LODOP.PRINT();
									}
									
									
					    			
								}else{
									Ext.MessageBox.alert('提示', "没有相应记录!");
								} 
						   }});
						
						
																		
		        	}else{
		        		Ext.MessageBox.alert('提示',"请在打印条件中选择发证日期！");
		        	}
					}
					else { //浏览器不支持canvas，要求用户更换浏览器chrome
						
						   alert("您使用的浏览器不支持canvas对象，请更换到chrome浏览器下来打印证书！抱歉！");
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
			    				//currentPrinter=returnPrinter;
			    				LODOP.SET_PRINTER_INDEX(returnPrinter);
			    			}
			        }
			}
	    ]
});

//js获取项目根路径，如： http://localhost:8080/uimcardprj
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



var passstudent = new Ext.data.Record.create([
	{
		name:'id'
	},{
		name:'exInstitution'
	},{
		name:'exLanguage'
	},{
		name:'exProfession'
	},{
		name:'exCollege'
	},{
		name:'exCampus'
	},{
		name:'name'
	},{
		name:'sex',convert:function(data){if(data=="M"){return "男";}else return "女";}
	},{
		name:'studentnum'
	},{
		name:'idnum'
	},{
		name:'examnum'
	},{
		name:'exambatch'
	},{
		name:'score',convert:function(data){if(data>=60&&data<85){return "合格";}else return "优秀";}
	}
]);

var passstudentstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, passstudent),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.passStudentShow
	  })
});

var hiddenstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, passstudent),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.passStudentShow
	  })
});


var sm=new Ext.grid.CheckboxSelectionModel();

var passstudentgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'passstudentgrid',
	store :passstudentstore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
//	autoExpandColumn : 'name',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	sm:sm,
	columns : [new Ext.grid.RowNumberer(),
	           sm,
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :100,
	 	sortable :true
	},	{
		id : 'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :120,
	 	sortable :true
	},	{
		header : '学院',
	  	dataIndex :'exCollege',
	  	width :180,
	 	sortable :true
	},	{
		id : 'exLanguage',
		header : '语种',
	  	dataIndex :'exLanguage',
	  	width :200,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '证书编号',
	  	dataIndex :'examnum',
	  	width :160,
	 	sortable :true
	},{
		id : 'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :200,
	 	sortable :true
	}, {
		id : 'score',
		header : '成绩',
	  	dataIndex :'score',
	  	width :100,
	 	sortable :true
	}
	],
	    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : passstudentstore,
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
	        params.filter = passstudent_currentSearchFilter;
	        params.start = start;
	        params.limit = this.pageSize;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  })
});


function passStudentGridInit(){	
	
	backgroundFilePath=getRootPath()+"/resources/icons/certificate.jpg";
	
	//异步回调得到文件的相对路径
	StudentController.getStudentPhotoPath(function(filepath){
	
		filePath=filepath;//拼接文件路径成绝对路径
		
	});
	
	//得到学院combobox的列表
	   StudentController.getCollegeList(function(data){
			if(data){
				comboCollegeStore.loadData(data);
			}
	  	});
	   //得到语种combobox的列表
	  	StudentController.getLanguageList_without_languagenum(function(data){
			if(data){
				comboLanguageStore.loadData(data);
			}
	  	});
	  	
	passstudentstore.load({
		params : {
			filter : passstudent_currentSearchFilter,
			start : 0,
			limit : 20
		}
	});
	
	
//	Ext.form.DateField在firefox 3、chrome和safari中显示会有问题，界面将会拉的很长很长。
//	下面的代码能够修复该问题：
	Ext.override(Ext.menu.DateMenu,{	 
 		render : function()
 		{	 
 			Ext.menu.DateMenu.superclass.render.call(this);		 
 			if(Ext.isGecko|| Ext.isSafari || Ext.isChrome)
 			{	 
 			this.picker.el.dom.childNodes[0].style.width = '178px';	 
 			this.picker.el.dom.style.width = '178px';	 
 			}	 
 		}	 
 		});
	
	
}

function printCertificatePageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [form,passstudentgrid,printForm],
		renderTo :Ext.getBody()
	});
	passStudentGridInit();
}
