var currentSearchFilter="";


var xxmc="";
var xxdm="";

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
	   border:true,
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
	        	     name:'yzname',
	        	     triggerAction:'all',
	        	     store:comboLanguageStore,			        	    	 
	        	     displayField:'languagename',
	        	     valueField:'languagenum',
	        	     mode:'local',
	        	     editable:false,
	        	     anchor:'97.5%',
	        	    // allowBlank: false,
	        	     emptyText:'---请选择---',
	        	     listeners: {	                	  
	            	  	  select:function(){
	            	  		if(form.getForm().isValid()){
				        		currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
				        		studentExamnumgridstore.load({
				        			params : {
				        						filter : currentSearchFilter,
				        						start : 0,
				        						limit : 20
				        			}
				        		});
				        	}else{
				        		Ext.MessageBox.alert('提示',"输入查询条件有误！");
				        	}  
	            	  		
	            	  	  }
	                  }
	        	    }] 
	          }]
		       
		   }]		   		   
});


var studentExamnum = new Ext.data.Record.create([{
	    name:'name'
    },{
		name:'examnum'
	},{
		name:'languagename'
	}
]);

var hiddenstore = new Ext.data.JsonStore({
    fields : studentExamnum
});


var studentExamnumstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
       totalProperty : 'totalProperty',
       root : 'root'
    }, studentExamnum),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.getAdmissionNum
	  })
});

var studentExamnumgridRecord = new Ext.data.Record.create([
                         {
                               name:'logicExamroomNum'
                         },{
                               name:'roomlocation'
                         },{
                               name:'languagename'
                         }
                      ]);

 var studentExamnumgridstore = new Ext.data.Store( {
          reader : new Ext.data.JsonReader({
          totalProperty : 'totalProperty',
          root : 'root'
          }, studentExamnumgridRecord),
          proxy : new Ext.ux.data.DWRProxy({
        dwrFunction : StudentController.getLogicExamRoom
               })
    });

var studentExamnumgridhiddenstore = new Ext.data.Store( {
      reader : new Ext.data.JsonReader({
      totalProperty : 'totalProperty',
                  root : 'root'
     }, studentExamnumgridRecord),
     proxy : new Ext.ux.data.DWRProxy({
     dwrFunction : StudentController.getLogicExamRoom
      })
          });

                                          

var sm=new Ext.grid.CheckboxSelectionModel();

var studentExamnumgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentExamnumgrid',
	store :studentExamnumgridstore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'languagename',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	sm:sm,
	columns : [new Ext.grid.RowNumberer(),
	           sm,
	           {
	       		id : 'logicExamroomNum',
	       		header : '考场号',
	       	  	dataIndex :'logicExamroomNum',
	       	  	width :120,
	       	 	sortable :true
	       	},	{
	       		id : 'roomlocation',
	       		header : '考场地址',
	       	  	dataIndex :'roomlocation',
	       	  	width :200,
	       	 	sortable :true
	       	},	{
	       		id:'languagename',
	       		header : '语种',
	       	  	dataIndex :'languagename',
	       	  	width :120,
	       	 	sortable :true
	       	}],
	
	
	tbar:[ {
		text : '桌贴设计',
		iconCls : 'edit',
		onClick : function() {
			//判断用户是否选中记录
			if (studentExamnumgrid.getSelectionModel().hasSelection() && (studentExamnumgrid.getSelectionModel().getSelections()).length==1) {
												   
				var selections = studentExamnumgrid.getSelectionModel().getSelections();	
				var k=0;			
				DesignFunction(selections,k);                                  						                                       						    										  																																					    			
			}else{
				Ext.MessageBox.alert('提示', "请选择一条记录,且只能选择一条记录!");
			}						
				
			
		}
	},'-',{
		text : '打印当前页',
		iconCls : 'print',
		onClick : function() {
			Ext.MessageBox.confirm("确认","确定打印当前页?",function(btn){				
				if(btn=="yes"){
					//判断用户是否选中记录
					if (studentExamnumgrid.getSelectionModel().hasSelection()) {
						LODOP.SELECT_PRINTER(); 
						var selections = studentExamnumgrid.getSelectionModel().getSelections();	
						var k=0;			
						printFunction(selections,k);                                  						                                       						    										  																																					    			
					}else{
						Ext.MessageBox.alert('提示', "没有选中一条记录!");
					}	
					}
									
			});																																									    																									        							
		}
	},'-',{
		text : '打印全部',
		iconCls : 'print',
		onClick : function() {
			Ext.MessageBox.confirm("确认","确定打印全部?",function(btn){	
				if(btn=="yes"){
					var params = {};
					   params.fliter = currentSearchFilter;
					   params.start = 0;
					   params.limit = 10000;
					   studentExamnumgridhiddenstore.load({params:params,callback: function(){	
						   
						   //判断数据库中是否有符合条件的数据
						   if (studentExamnumgridhiddenstore.getCount()>0) {																   								
								var k=0;		
								LODOP.SELECT_PRINTER();
								printAllFunction(studentExamnumgridhiddenstore,k);                                  						                                       						    
														  																																					    			
							}else{
								Ext.MessageBox.alert('提示', "数据库中没有符合条件的数据!");
							}		  
						   
					   }});
					
				}	
				
			});
																																																		        							
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
	  pageSize:20,//设置为每页30条记录
      store : studentExamnumgridstore,
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


function DesignFunction(selections,k){
	if(k==selections.length) return;
	
	var selectobject=selections[k];//得到grid中选中的记录
	var logicroomnum=selectobject.get("logicExamroomNum");
	var roomLocation=selectobject.get("roomlocation");
	
	StudentController.getAdmissionNum(logicroomnum,function(data){
		if(data){
			hiddenstore.loadData(data);
			     
			     
			    
			     
			     //判断数据库中是否有符合条件的数据
				   if (hiddenstore.getCount()>0) {
						
					   LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印");
						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
						
						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
						
						//每次先要清掉printTable，保证不会重复多个printTable
							var tableId=document.getElementById("printTable");                                					
							if (tableId!=null) {
								tableId.parentNode.removeChild(tableId);
								     
								}
							
						var _table=document.createElement("TABLE");
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '750');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
						    
					    var tbody=document.createElement("TBODY");  
					    
					   
	                   
						      _table.appendChild(tbody);
						    
						   
						    var big_tr=null;
						  	//生成所有打印页面
								for(var i=0;i<hiddenstore.getCount();i++)
								{
									var printobject=hiddenstore.getAt(i);//得到grid中选中的记录
									
									if(i%2==0){
									   big_tr=null;
									   big_tr=document.createElement("TR");
									}
										var big_td=document.createElement("TD");
											
											var small_table=document.createElement("TABLE");
										        small_table.setAttribute('border','1');
										        small_table.setAttribute('width', '360');					
										        small_table.setAttribute('align', 'center');
										        small_table.setAttribute('border-collapse', 'collapse');
										        small_table.setAttribute('cellspacing', '0');
										        small_table.setAttribute('cellpadding', '4');
										    
									        var smallTbody=document.createElement("TBODY");  
									    
										    var tr=document.createElement("TR");
										    var td=document.createElement("TD");
										        td.setAttribute('width', '120');
										    var text=document.createTextNode("应试等级及语种:");						    
										        td.appendChild(text);
										        tr.appendChild(td);
										    var td=document.createElement("TD");
											var text=document.createTextNode(printobject.get("languagename"));
											    td.appendChild(text);
											    tr.appendChild(td);		                       
											    smallTbody.appendChild(tr);
										    var tr=document.createElement("TR");
										    var td=document.createElement("TD");
										    var text=document.createTextNode("准考证号:");
											    td.appendChild(text);
											    tr.appendChild(td);
										    var td=document.createElement("TD");
											var text=document.createTextNode(printobject.get("examnum"));
												td.appendChild(text);
												tr.appendChild(td);		                       
												smallTbody.appendChild(tr); 
										    var tr=document.createElement("TR");
										    var td=document.createElement("TD");
										    var text=document.createTextNode("姓    名:");
											    td.appendChild(text);
											    tr.appendChild(td);
											var td=document.createElement("TD");
										    var text=document.createTextNode(printobject.get("name"));
												td.appendChild(text);
												tr.appendChild(td);		                       
												smallTbody.appendChild(tr); 
										    				    
											    small_table.appendChild(smallTbody);	
											    
											    big_td.appendChild(small_table);					   
											       big_tr.appendChild(big_td);	
											      /* if(hiddenstore.length==1)
											    	   {
											    	     tbody.appendChild(big_tr); 	
											    	   }
											       else if((i!=hiddenstore.length-1)&&(i%2==1)) {
											    	      tbody.appendChild(big_tr); 
											       }
											       else if(i==(hiddenstore.length-1)){
											    	      tbody.appendChild(big_tr); 
											       }*/
											       tbody.appendChild(big_tr);
						 }
					
						
						_table.appendChild(tbody); 
						var _div=document.getElementById("hiddentable");
						_div.appendChild(_table);
						
						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
						//alert(printTable);
						var InfoArray=new Array("浙江省高校计算机等级考试理论考试准考证桌贴",							
								"第",
								logicroomnum,
								"考场",
								"教室:",                                  							
								roomLocation,                              								
								printTable);
						
					    						    									    
					    CreatePrintPage(InfoArray);			
							
						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PRINT_SETUP();
						reSizePrintParam();
						
						// CreatePrintPage(logicroomnum,roomLocation,printTable);	
							
						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						//LODOP.PRINT_SETUP();
						k=k+1;
						DesignFunction(selections,k);
						
				   }else{
						Ext.MessageBox.alert('提示', "装载数据没有成功!");
					}	
		}else{
			Ext.MessageBox.alert('提示', "没有数据！");
		}
	
});
}




function printFunction(selections,k){
	
	if(k==selections.length) return;
	
	var selectobject=selections[k];//得到grid中选中的记录
	var logicroomnum=selectobject.get("logicExamroomNum");
	var roomLocation=selectobject.get("roomlocation");
	
	StudentController.getAdmissionNum(logicroomnum,function(data){
		if(data){
			hiddenstore.loadData(data);
			     
			     
			    
			     
			     //判断数据库中是否有符合条件的数据
				   if (hiddenstore.getCount()>0) {
						
					   LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印");
						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
						
						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
						
						//每次先要清掉printTable，保证不会重复多个printTable
							var tableId=document.getElementById("printTable");                                					
							if (tableId!=null) {
								tableId.parentNode.removeChild(tableId);
								     
								}
							
						var _table=document.createElement("TABLE");
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '750');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
						    
					    var tbody=document.createElement("TBODY");  
					    
					   
	                   
						      _table.appendChild(tbody);
						    
						      var big_tr=null;
						    
						  	//生成所有打印页面
								for(var i=0;i<hiddenstore.getCount();i++)
								{
									var printobject=hiddenstore.getAt(i);//得到grid中选中的记录
									
									if(i%2==0){
									   big_tr=null;
									   big_tr=document.createElement("TR");
									}
										var big_td=document.createElement("TD");
											
											var small_table=document.createElement("TABLE");
										        small_table.setAttribute('border','1');
										        small_table.setAttribute('width', '360');					
										        small_table.setAttribute('align', 'center');
										        small_table.setAttribute('border-collapse', 'collapse');
										        small_table.setAttribute('cellspacing', '0');
										        small_table.setAttribute('cellpadding', '4');
										    
									        var smallTbody=document.createElement("TBODY");  
									    
										    var tr=document.createElement("TR");
										    var td=document.createElement("TD");
										        td.setAttribute('width', '120');
										    var text=document.createTextNode("应试等级及语种:");						    
										        td.appendChild(text);
										        tr.appendChild(td);
										    var td=document.createElement("TD");
											var text=document.createTextNode(printobject.get("languagename"));
											    td.appendChild(text);
											    tr.appendChild(td);		                       
											    smallTbody.appendChild(tr);
										    var tr=document.createElement("TR");
										    var td=document.createElement("TD");
										    var text=document.createTextNode("准考证号:");
											    td.appendChild(text);
											    tr.appendChild(td);
										    var td=document.createElement("TD");
											var text=document.createTextNode(printobject.get("examnum"));
												td.appendChild(text);
												tr.appendChild(td);		                       
												smallTbody.appendChild(tr); 
										    var tr=document.createElement("TR");
										    var td=document.createElement("TD");
										    var text=document.createTextNode("姓    名:");
											    td.appendChild(text);
											    tr.appendChild(td);
											var td=document.createElement("TD");
										    var text=document.createTextNode(printobject.get("name"));
												td.appendChild(text);
												tr.appendChild(td);		                       
												smallTbody.appendChild(tr); 
										    				    
											    small_table.appendChild(smallTbody);	
											    
											    big_td.appendChild(small_table);					   
											       big_tr.appendChild(big_td);	
											      /* if(selections.length==1)
											    	   {
											    	     tbody.appendChild(big_tr); 	
											    	   }
											       else if((i!=selections.length-1)&&(i%2==1)) {
											    	      tbody.appendChild(big_tr); 
											       }
											       else if(i==(selections.length-1)){
											    	      tbody.appendChild(big_tr); 
											       }*/
											       tbody.appendChild(big_tr); 
						 }
					
						
						_table.appendChild(tbody); 
						var _div=document.getElementById("hiddentable");
						_div.appendChild(_table);
						
						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
						//alert(printTable);
						var InfoArray=new Array("浙江省高校计算机等级考试理论考试准考证桌贴",							
								"第",
								logicroomnum,
								"考场",
								"教室:",                                  							
								roomLocation,                              								
								printTable);
						
					    						    									    
					    CreatePrintPage(InfoArray);			
							
						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PRINT();
						
						// CreatePrintPage(logicroomnum,roomLocation,printTable);	
							
						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");						
						//LODOP.PRINT();
						k=k+1;
						printFunction(selections,k);
						
				   }else{
						Ext.MessageBox.alert('提示', "装载数据没有成功!");
					}	
		}else{
			Ext.MessageBox.alert('提示', "没有数据！");
		}
	
});
}


function printAllFunction(studentExamnumgridhiddenstore,k){
	
	if(k==studentExamnumgridhiddenstore.getCount()) return;
	
	var printobject=studentExamnumgridhiddenstore.getAt(k);//得到store中的记录
	var logicroomnum=printobject.get("logicExamroomNum");
	var roomLocation=printobject.get("roomlocation");
	
	StudentController.getStudentInfo(logicroomnum,function(data){
		if(data){
			     hiddenstore.loadData(data);
			     
			     //判断数据库中是否有符合条件的数据
				   if (hiddenstore.getCount()>0) {
						
					   LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印");
						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
					  
						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
						
						//每次先要清掉printTable，保证不会重复多个printTable
							var tableId=document.getElementById("printTable");                                					
							if (tableId!=null) {
								tableId.parentNode.removeChild(tableId);
								     
								}
							
						var _table=document.createElement("TABLE");
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '750');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
						
	
						    var tbody=document.createElement("TBODY");  
						    
							   
			                   
						      _table.appendChild(tbody);
						    

						   
						    var  big_tr=null;;
						  //生成所有打印页面
							for(var i=0;i<hiddenstore.getCount();i++)
							{
								var printobject=hiddenstore.getAt(i);//得到grid中选中的记录
								
								if(i%2==0){
								   big_tr=null;
								   big_tr=document.createElement("TR");
								}
									var big_td=document.createElement("TD");
										
										var small_table=document.createElement("TABLE");
									        small_table.setAttribute('border','1');
									        small_table.setAttribute('width', '360');					
									        small_table.setAttribute('align', 'center');
									        small_table.setAttribute('border-collapse', 'collapse');
									        small_table.setAttribute('cellspacing', '0');
									        small_table.setAttribute('cellpadding', '4');
									    
								        var smallTbody=document.createElement("TBODY");  
								    
									    var tr=document.createElement("TR");
									    var td=document.createElement("TD");
									        td.setAttribute('width', '120');
									    var text=document.createTextNode("应试等级及语种:");						    
									        td.appendChild(text);
									        tr.appendChild(td);
									    var td=document.createElement("TD");
										var text=document.createTextNode(printobject.get("languagename"));
										    td.appendChild(text);
										    tr.appendChild(td);		                       
										    smallTbody.appendChild(tr);
									    var tr=document.createElement("TR");
									    var td=document.createElement("TD");
									    var text=document.createTextNode("准考证号:");
										    td.appendChild(text);
										    tr.appendChild(td);
									    var td=document.createElement("TD");
										var text=document.createTextNode(printobject.get("examnum"));
											td.appendChild(text);
											tr.appendChild(td);		                       
											smallTbody.appendChild(tr); 
									    var tr=document.createElement("TR");
									    var td=document.createElement("TD");
									    var text=document.createTextNode("姓    名:");
										    td.appendChild(text);
										    tr.appendChild(td);
										var td=document.createElement("TD");
									    var text=document.createTextNode(printobject.get("name"));
											td.appendChild(text);
											tr.appendChild(td);		                       
											smallTbody.appendChild(tr); 
									    				    
										    small_table.appendChild(smallTbody);	
										    
										    big_td.appendChild(small_table);					   
										       big_tr.appendChild(big_td);	
										      /* if(studentExamnumgridhiddenstore.getCount()==1)
										    	   {
										    	     tbody.appendChild(big_tr); 	
										    	   }
										       else if((i!=studentExamnumgridhiddenstore.getCount()-1)&&(i%2==1)) {
										    	      tbody.appendChild(big_tr); 
										       }
										       else if(i==(studentExamnumgridhiddenstore.getCount()-1)){
										    	      tbody.appendChild(big_tr); 
										       }*/
										       tbody.appendChild(big_tr); 
					 }
				
					
					_table.appendChild(tbody); 
					var _div=document.getElementById("hiddentable");
					_div.appendChild(_table);
					
					var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
					//alert(printTable);
					var InfoArray=new Array("浙江省高校计算机等级考试理论考试准考证桌贴",							
							"第",
							logicroomnum,
							"考场",
							"教室:",                                  							
							roomLocation,                              								
							printTable);
					
				    						    									    
				    CreatePrintPage(InfoArray);			
						
					LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
					LODOP.PRINT();
					
					
					//CreatePrintPage(logicroomnum,roomLocation,printTable);
						
					//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
					
					//LODOP.PRINT();
					k=k+1;
					printAllFunction(studentExamnumgridhiddenstore,k);
					
			   }else{
					Ext.MessageBox.alert('提示', "此教室没有安排学生！");
				}	
	}else{
		Ext.MessageBox.alert('提示', "此教室没有安排学生！");
	}
	
});
}


function studentExamnumgridInit(){	
  	
  	   //得到语种combobox的列表
  	  	StudentController.getLanguageList_theory(function(data){
  			if(data){
  				comboLanguageStore.loadData(data);
  			}
  	  	});

  	  	
  	  studentExamnumgridstore.load({
  		params : {
  			filter : currentSearchFilter,
  			start : 0,
  			limit : 20
  		},
  		callback:function(records,operation,success){
			if(records=="")Ext.MessageBox.alert('提示', "您还没有编排理论考场!");}
  	});
  }

  
  function printAdmissionNumPageInit() {
  	new Ext.Viewport( {
  		layout :'border',
  		hideMode: Ext.isIE ? 'offsets' : 'display',
  		items : [form,studentExamnumgrid],
  		renderTo :Ext.getBody()
  	});
  	studentExamnumgridInit();
  }

