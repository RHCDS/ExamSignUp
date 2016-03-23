var currentSearchFilter="";

var sectiongridRecord = new Ext.data.Record.create([
	{
		name:'logicExamroomNum'
	},{
		name:'roomlocation'
	},{
		name:'languagename'
	},{
		name:'startTime'
	},{
		name:'endTime'
	},{
		name:'startExamnum'
	},,{
		name:'endExamnum'
	}
]);

var sectiongridstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, sectiongridRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.getSection
	  })
});

var hiddenstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, sectiongridRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.getSection
	  })
});


var sm=new Ext.grid.CheckboxSelectionModel();

var sectiongrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'sectiongrid',
	store :sectiongridstore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
//	autoExpandColumn : 'roomlocation',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	sm:sm,
	columns : [new Ext.grid.RowNumberer(),
	           sm,
	{
		id : 'logicExamroomNum',
		header : '考场号',
	  	dataIndex :'logicExamroomNum',
	  	width :90,
	 	sortable :true
	},	{
		id : 'roomlocation',
		header : '考场地址',
	  	dataIndex :'roomlocation',
	  	width :200,
	 	sortable :true
	},	{
		header : '语种',
	  	dataIndex :'languagename',
	  	width :180,
	 	sortable :true
	},	{
		id : 'startTime',
		header : '考试开始时间',
	  	dataIndex :'startTime',
	  	width :180,
	 	sortable :true
	},{
		id : 'endTime',
		header : '考试结束时间',
	  	dataIndex :'endTime',
	  	width :140,
	 	sortable :true
	},	{
		id : 'startExamnum',
		header : '起始准考证号',
	  	dataIndex :'startExamnum',
	  	width :160,
	 	sortable :true
	},{
		id : 'endExamnum',
		header : '终止准考证号',
	  	dataIndex :'endExamnum',
	  	width :160,
	 	sortable :true
	}],
	 tbar:[ {
			text : '考场门口标贴设计',
			iconCls : 'edit',
			onClick : function() {
				//判断用户是否选中记录
				if (sectiongrid.getSelectionModel().hasSelection()) {
					
					
					
					
				    LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印考场门口标贴");
					//LODOP.SET_PRINT_PAGESIZE(2,"210mm","297mm","");
				    //LODOP.SET_PRINT_PAGESIZE(2,"","","");
					var selections = sectiongrid.getSelectionModel().getSelections();
																								
					//生成打印页面
					if (selections.length==1) 
					{
						var printobject=selections[0];//得到grid中选中的记录
						var time=printobject.get("startTime")+" 到 "+printobject.get("endTime");
						
						var InfoArray=new Array("浙江省高校计算机等级考试\n考场标贴\n",
								"考试时间:",
								time,
								"应试等级及语种:",
								printobject.get("languagename"),
								"考场编号:",
								printobject.get("logicExamroomNum")+"号考场",
								"考试地点:",
								printobject.get("roomlocation"),
								"起讫准考证号:",
								printobject.get("startExamnum"),
								"~",
								printobject.get("endExamnum"));
						
					    						    									    
					    CreatePrintPage(InfoArray);
					    LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PRINT_SETUP();
						reSizePrintParam();
																					
					   // CreatePrintPage(time,printobject.get("languagename"),printobject.get("logicExamroomNum"),printobject.get("roomlocation"),printobject.get("startExamnum"),printobject.get("endExamnum"));	
					}
					else{
						Ext.MessageBox.alert('提示', "抱歉,只能选择一条记录!");
					}
					
					
	    			
				}else{
					Ext.MessageBox.alert('提示', "请选择一条记录,且只能选择一条记录!");
				}				
				
			}
		},'-',{
			text : '打印当前页',
			iconCls : 'print',
			onClick : function() {
								
					//判断用户是否选中记录
					if (sectiongrid.getSelectionModel().hasSelection()) {
						LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印考场门口标贴");
						//LODOP.SET_PRINT_PAGESIZE(2,"210mm","297mm","");
						//LODOP.SET_PRINT_PAGESIZE(2,"","","");
						var selections = sectiongrid.getSelectionModel().getSelections();
																									
						//生成所有打印页面
						for(var i=0;i<selections.length;i++)
						{
							var printobject=selections[i];//得到grid中选中的记录
							var time=printobject.get("startTime")+" 到 "+printobject.get("endTime");
						  
							var InfoArray=new Array("浙江省高校计算机等级考试\n考场标贴\n",
									"考试时间:",
									time,
									"应试等级及语种:",
									printobject.get("languagename"),
									"考场编号:",
									printobject.get("logicExamroomNum")+"号考场",
									"考试地点:",
									printobject.get("roomlocation"),
									"起讫准考证号:",
									printobject.get("startExamnum"),
									"~",
									printobject.get("endExamnum"));
							
						    						    									    
						    CreatePrintPage(InfoArray);
							
							
							// CreatePrintPage(time,printobject.get("languagename"),printobject.get("logicExamroomNum"),printobject.get("roomlocation"),printobject.get("startExamnum"),printobject.get("endExamnum"));	
						}
						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PREVIEW();
						
		    			
					}else{
						Ext.MessageBox.alert('提示', "请选择一条记录!");
					}																	        							
			}
		},'-',{
			text : '打印全部',
			iconCls : 'print',
			onClick : function() {
				
				var params = {};
				   params.fliter = currentSearchFilter;
				   params.start = 0;
				   params.limit = 10000;
				   hiddenstore.load({params:params,callback: function(){
					   
					 //判断数据库中是否有符合条件的数据
					   if (hiddenstore.getCount()>0) {
						    LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印考场门口标贴");
							//LODOP.SET_PRINT_PAGESIZE(2,"210mm","297mm","");
						    //LODOP.SET_PRINT_PAGESIZE(2,"","","");
							
																																
							//生成所有打印页面
							for(var i=0;i<hiddenstore.getCount();i++)
							{
								var printobject=hiddenstore.getAt(i);//得到store中的记录
								var time=printobject.get("startTime")+" 到 "+printobject.get("endTime");
							    
								var InfoArray=new Array("浙江省高校计算机等级考试\n考场标贴\n",
										"考试时间:",
										time,
										"应试等级及语种:",
										printobject.get("languagename"),
										"考场编号:",
										printobject.get("logicExamroomNum")+"号考场",
										"考试地点:",
										printobject.get("roomlocation"),
										"起讫准考证号:",
										printobject.get("startExamnum"),
										"~",
										printobject.get("endExamnum"));
								
							    						    									    
							    CreatePrintPage(InfoArray);
																																					
								//CreatePrintPage(time,printobject.get("languagename"),printobject.get("logicExamroomNum"),printobject.get("roomlocation"),printobject.get("startExamnum"),printobject.get("endExamnum"));	
							}
							LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
							LODOP.PREVIEW();
							
			    			
						}else{
							Ext.MessageBox.alert('提示', "没有相应记录!");
						}									
					   
					   
				   }});
								
													        							
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
		}],
	    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : sectiongridstore,
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
	        params.filter = currentSearchFilter;
	        params.start = start;
	        params.limit = this.pageSize;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  })
});

function Init(){	
		  	
	sectiongridstore.load({
		params : {
			filter : currentSearchFilter,
			start : 0,
			limit : 20
		},
		callback:function(records,operation,success){
			if(records=="")Ext.MessageBox.alert('提示', "您还没有编排理论考场!");}
	});
}



function printSectionDoorFlagPageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [sectiongrid],
		renderTo :Ext.getBody()
	});
	Init();
}