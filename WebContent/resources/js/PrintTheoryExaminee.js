var currentSearchFilter="";

var xxmc="";
var xxdm="";
var languagename="";

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
	        	     id:'language',
	        	     fieldLabel:'语种',
	        	     hiddenName:'and$student-exLanguage-languagenum$=$value',
	        	     name:'yzname',
	        	     triggerAction:'all',
	        	     store:comboLanguageStore,	
	        	     displayField:'languagename',
	    //    	     displayField:'name',
	        	     valueField:'languagenum',
	        	     mode:'local',
	        	     anchor:'98%',
	        	     editable:false,
	        	     allowBlank: false,
	        	     emptyText:'---请选择---',
	        	     listeners: {	                	  
	            	  	  select:function(){
	            	  		currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
			        		sectiongridstore.load({
			        			params : {
			        						filter : currentSearchFilter,
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



var hiddenRecord = new Ext.data.Record.create([
	{
		name:'examnum'
	},{
		name:'name'
	},{
		name:'studentnum'
	},{
		name:'collegename'
	},{
		name:'classnum'
	},{
		name:'grade'
	},{
		name:'remark'
	}
]);

var hiddenstore = new Ext.data.JsonStore({
	fields : hiddenRecord
	});

var sectiongridRecord = new Ext.data.Record.create([
                                                	{
                                                		name:'logicExamroomNum'
                                                	},{
                                                		name:'roomlocation'
                                                	},{
                                                		name:'languagename'
                                                	}
                                                ]);

                                                var sectiongridstore = new Ext.data.Store( {
                                                	reader : new Ext.data.JsonReader({
                                                         totalProperty : 'totalProperty',
                                                         root : 'root'
                                                      }, sectiongridRecord),
                                                	proxy : new Ext.ux.data.DWRProxy({
                                                	     dwrFunction : StudentController.getLogicExamRoom
                                                	  })
                                                });

                                                var sectiongridhiddenstore = new Ext.data.Store( {
                                                	reader : new Ext.data.JsonReader({
                                                         totalProperty : 'totalProperty',
                                                         root : 'root'
                                                      }, sectiongridRecord),
                                                	proxy : new Ext.ux.data.DWRProxy({
                                                	     dwrFunction : StudentController.getLogicExamRoom
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
			text : '考生名册设计',
			iconCls : 'edit',
			onClick : function() {
				if(form.getForm().isValid()){
					 languagename=Ext.get('language').dom.value; 
				//判断用户是否选中记录
				if (sectiongrid.getSelectionModel().hasSelection() && (sectiongrid.getSelectionModel().getSelections()).length==1) {
														   
					var selections = sectiongrid.getSelectionModel().getSelections();	
					
					var k=0;			
					DesignFunction(selections,k);                                  						                                       						    										  																																					    			
				   }else{
					     Ext.MessageBox.alert('提示', "请选择一条记录,且只能选择一条记录!");
				        }	
				}else{
		        		Ext.MessageBox.alert('提示',"语种是必选的查询条件！");
		        	}
					
				
			}
		},'-',{
			text : '打印当前页',
			iconCls : 'print',
			onClick : function() {
				if(form.getForm().isValid()){
				   languagename=Ext.get('language').dom.value; 
					
				    Ext.MessageBox.confirm("确认","确定打印当前页?",function(btn){				
					if(btn=="yes"){
						//判断用户是否选中记录
						if (sectiongrid.getSelectionModel().hasSelection()) {
							   
							var selections = sectiongrid.getSelectionModel().getSelections();	
							var k=0;
							LODOP.SELECT_PRINTER(); 
							printFunction(selections,k);                                  						                                       						    										  																																					    			
						}else{
							Ext.MessageBox.alert('提示', "没有选中一条记录!");
						}	
						}
										
				});		
				}else{
	        		Ext.MessageBox.alert('提示',"语种是必选的查询条件！");
	        	}
			}
		},'-',{
			text : '打印全部',
			iconCls : 'print',
			onClick : function() {
				if(form.getForm().isValid()){
					 languagename=Ext.get('language').dom.value; 
			     	Ext.MessageBox.confirm("确认","确定打印全部?",function(btn){	
					if(btn=="yes"){
						var params = {};
						   params.fliter = currentSearchFilter;
						   params.start = 0;
						   params.limit = 10000;
						   sectiongridhiddenstore.load({params:params,callback: function(){	
							   
							   //判断数据库中是否有符合条件的数据
							   if (sectiongridhiddenstore.getCount()>0) {																   								
									var k=0;		
									LODOP.SELECT_PRINTER(); 
									printAllFunction(sectiongridhiddenstore,k);                                  						                                       						    
															  																																					    			
								}else{
									Ext.MessageBox.alert('提示', "数据库中没有符合条件的数据!");
								}		  
							   
						   }});
						
					}	
					
				});
				}else{
	        		Ext.MessageBox.alert('提示',"语种是必选的查询条件！");
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
		}],
	    bbar : new Ext.PagingToolbar({
    	pageSize:30,
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


function DesignFunction(selections,k){
	
	if(k==selections.length) return;
	
	var selectobject=selections[k];//得到grid中选中的记录
	var logicroomnum=selectobject.get("logicExamroomNum");
	
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
						_table.style.fontSize='14px';
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '750');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
						    
					    var tbody=document.createElement("TBODY");  
					    
						    var tr=document.createElement("TR");
						    tr.style.fontSize='16px';
					        tr.style.fontWeight='bold';
						        tr.setAttribute('align', 'center');
						    var td=document.createElement("TD");
						        td.setAttribute('width', '70');
						    var text=document.createTextNode("准考证号");
						        td.appendChild(text);
						        tr.appendChild(td);
						    var td=document.createElement("TD");
						        td.setAttribute('width', '50');
						    var text=document.createTextNode("姓名");						        
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
						        td.setAttribute('width', '50');
						    var text=document.createTextNode("学号");						        
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
		        		    var text=document.createTextNode("学院");
		        				td.appendChild(text);
		                        tr.appendChild(td); 
		                    var td=document.createElement("TD");
		            	    var text=document.createTextNode("班级");
		            			td.appendChild(text);
		                        tr.appendChild(td); 
		                //    var td=document.createElement("TD");
					     //   var text=document.createTextNode("入学年份");
						//		td.appendChild(text);
		                //        tr.appendChild(td);                                                  
		                    var td=document.createElement("TD");
		                        td.setAttribute('width', '80');
		        			var text=document.createTextNode("签名");
		        			    td.appendChild(text);
		                        tr.appendChild(td);                      
		                   							                        
					           tbody.appendChild(tr);
						      _table.appendChild(tbody);
						    
						   
						    
						//生成所有打印页面
						for(var i=0;i<hiddenstore.getCount();i++)
						{
							var printobject=hiddenstore.getAt(i);//得到store中的记录
							
							var tr=document.createElement("TR");
							    tr.setAttribute('align', 'center');
							var td=document.createElement("TD");
							    td.setAttribute('width', '70');
						    var text=document.createTextNode(printobject.get("examnum"));
						        td.appendChild(text);
						        tr.appendChild(td);
						    var td=document.createElement("TD");
						        td.setAttribute('width', '65');
						    var text=document.createTextNode(printobject.get("name"));
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
						        td.setAttribute('width', '65');
						    var text=document.createTextNode(printobject.get("studentnum"));
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
		        		    var text=document.createTextNode(printobject.get("collegename"));
		        			    td.appendChild(text);
		                        tr.appendChild(td); 
		                    var td=document.createElement("TD");
		            	    var text=document.createTextNode(printobject.get("classnum"));
		            		    td.appendChild(text);
		                        tr.appendChild(td);                 
		                  //  var td=document.createElement("TD");
					      //  var text=document.createTextNode(printobject.get("grade"));
						//		td.appendChild(text);
		                //        tr.appendChild(td);                                                    
		                    var td=document.createElement("TD");
		                        td.setAttribute('width', '80');
		                    var str="";
		                   // if(printobject.get("remark")==null)str="";
		                 //   else str=printobject.get("remark");
		        			var text=document.createTextNode(str);		        	
		        			    td.appendChild(text);
		                        tr.appendChild(td);  								                   
		                       
		                        
		                        tbody.appendChild(tr);   
						 }
						_table.appendChild(tbody); 
						var _div=document.getElementById("hiddentable");
						_div.appendChild(_table);
						
						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
						//alert(printTable);
						
						var InfoArray=new Array("浙江省高校计算机等级考试理论考试考生名册",
								"学校代码:",                                  					
								"语种:",  
								"第",
								logicroomnum,
								"考场",
								xxdm,                                  							
								languagename,                              								
								printTable);
						
					    						    									    
					    CreatePrintPage(InfoArray);			
							
						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PRINT_SETUP();
						reSizePrintParam();
						
						
						// CreatePrintPage(xxdm,languagename,logicroomnum,printTable);	
							
						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						//LODOP.PRINT_SETUP();
						k=k+1;
						DesignFunction(selections,k);
						
				   }else{
					   Ext.MessageBox.alert('提示', "第"+logicroomnum+"考场，没有安排学生！");
					}	
		}else{
			Ext.MessageBox.alert('提示', "第"+logicroomnum+"考场，没有安排学生！");
		}
	
});
}

function printFunction(selections,k){
	
	if(k==selections.length) return;
	
	var selectobject=selections[k];//得到grid中选中的记录
	var logicroomnum=selectobject.get("logicExamroomNum");
	
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
						_table.style.fontSize='14px';
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '750');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
						    
					    var tbody=document.createElement("TBODY");  
					    
					    var thead=document.createElement("THEAD");
					    thead.style.fontSize='16px';
				        thead.style.fontWeight='bold';
					        thead.setAttribute('align', 'center');
					    var td=document.createElement("TD");
					        td.setAttribute('width', '70');
					    var text=document.createTextNode("准考证号");
					        td.appendChild(text);
					        thead.appendChild(td);
					    var td=document.createElement("TD");
					        td.setAttribute('width', '50');
					    var text=document.createTextNode("姓名");					     
					        td.appendChild(text);
					        thead.appendChild(td);
					    var td=document.createElement("TD");
					        td.setAttribute('width', '50');
					    var text=document.createTextNode("学号");					     
					        td.appendChild(text);
					        thead.appendChild(td);
	                    var td=document.createElement("TD");                    
	        		    var text=document.createTextNode("学院");
	        				td.appendChild(text);
	        				thead.appendChild(td); 
	                    var td=document.createElement("TD");
	            	    var text=document.createTextNode("班级");
	            			td.appendChild(text);
	            			thead.appendChild(td); 
	                  //  var td=document.createElement("TD");
				      //  var text=document.createTextNode("入学年份");
						//	td.appendChild(text);
						//	thead.appendChild(td);                                                  
	                    var td=document.createElement("TD");
	                        td.setAttribute('width', '80');
	        			var text=document.createTextNode("签名");
	        			    td.appendChild(text);
	        			    thead.appendChild(td);                      
	                   							                        
				           tbody.appendChild(thead);
						      _table.appendChild(tbody);
						    
						   
						    
						//生成所有打印页面
						for(var i=0;i<hiddenstore.getCount();i++)
						{
							var printobject=hiddenstore.getAt(i);//得到store中的记录
							
							var tr=document.createElement("TR");
							    tr.setAttribute('align', 'center');
							var td=document.createElement("TD");
							    td.setAttribute('width', '70');
						    var text=document.createTextNode(printobject.get("examnum"));
						        td.appendChild(text);
						        tr.appendChild(td);
						    var td=document.createElement("TD");
						        td.setAttribute('width', '65');
						    var text=document.createTextNode(printobject.get("name"));
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
						        td.setAttribute('width', '65');
						    var text=document.createTextNode(printobject.get("studentnum"));
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
		        		    var text=document.createTextNode(printobject.get("collegename"));
		        			    td.appendChild(text);
		                        tr.appendChild(td); 
		                    var td=document.createElement("TD");
		            	    var text=document.createTextNode(printobject.get("classnum"));
		            		    td.appendChild(text);
		                        tr.appendChild(td);                 
		             //       var td=document.createElement("TD");
					 //       var text=document.createTextNode(printobject.get("grade"));
					//			td.appendChild(text);
		            //            tr.appendChild(td);                                                    
		                    var td=document.createElement("TD");
		                        td.setAttribute('width', '80');
		                    var str="";
		                  //  if(printobject.get("remark")==null)str="";
		                  //  else str=printobject.get("remark");
		        			var text=document.createTextNode(str);	   
		        			    td.appendChild(text);
		                        tr.appendChild(td);  								                   
		                       
		                        
		                        tbody.appendChild(tr);   
						 }
						//加页尾
				    	   var tfoot=document.createElement("TFOOT");
						
						   var td=document.createElement("TD");                                        							      
						        td.setAttribute('colspan', '4');
						        td.setAttribute('tdata','pageNo');
						        td.setAttribute('format','#');
						        td.setAttribute('align','right');
						    // var p=document.createElement("p"); 
						   var text=document.createTextNode("第#页");
						        td.appendChild(text);
						       tfoot.appendChild(td);
						       
						  var td=document.createElement("TD");                                        							      
				               td.setAttribute('colspan', '1');
				               td.setAttribute('tdata','pageCount');
				               td.setAttribute('format','#');
				               td.setAttribute('align','left');
				           var text=document.createTextNode("共#页");
        			       td.appendChild(text);
        			       tfoot.appendChild(td);  
        			    
        		    	  tbody.appendChild(tfoot); 
						
						_table.appendChild(tbody); 
						var _div=document.getElementById("hiddentable");
						_div.appendChild(_table);
						
						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
						//alert(printTable);
						var InfoArray=new Array("浙江省高校计算机等级考试理论考试考生名册",
								"学校代码:",                                  					
								"语种:",  
								"第",
								logicroomnum,
								"考场",
								xxdm,                                  							
								languagename,                              								
								printTable);
						
					    						    									    
					    CreatePrintPage(InfoArray);			
							
						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PRINT();
						
						// CreatePrintPage(xxdm,languagename,logicroomnum,printTable);	
							
						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						//LODOP.PRINT();
						k=k+1;
						printFunction(selections,k);
						
				   }else{
					   Ext.MessageBox.alert('提示', "第"+logicroomnum+"考场，没有安排学生！");
					}	
		}else{
			Ext.MessageBox.alert('提示', "第"+logicroomnum+"考场，没有安排学生！");
		}
	
});
}


function printAllFunction(sectiongridhiddenstore,k){
	
	if(k==sectiongridhiddenstore.getCount()) return;
	
	var printobject=sectiongridhiddenstore.getAt(k);//得到store中的记录
	var logicroomnum=printobject.get("logicExamroomNum");
	
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
						_table.style.fontSize='14px';
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '750');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
						    
					    var tbody=document.createElement("TBODY");  
					    
                        var thead=document.createElement("THEAD");
                        thead.style.fontSize='16px';
				        thead.style.fontWeight='bold';
                            thead.setAttribute('align', 'center');
					    var td=document.createElement("TD");
					        td.setAttribute('width', '70');
					    var text=document.createTextNode("准考证号");
					        td.appendChild(text);
					        thead.appendChild(td);
					    var td=document.createElement("TD");
					        td.setAttribute('width', '50');
					    var text=document.createTextNode("姓名");					     
					        td.appendChild(text);
					        thead.appendChild(td);
					    var td=document.createElement("TD");
					        td.setAttribute('width', '50');
					    var text=document.createTextNode("学号");					     
					        td.appendChild(text);
					        thead.appendChild(td);
	                    var td=document.createElement("TD");                    
	        		    var text=document.createTextNode("学院");
	        				td.appendChild(text);
	        				thead.appendChild(td); 
	                    var td=document.createElement("TD");
	            	    var text=document.createTextNode("班级");
	            			td.appendChild(text);
	            			thead.appendChild(td); 
	                  //  var td=document.createElement("TD");
				      //  var text=document.createTextNode("入学年份");
						//	td.appendChild(text);
						//	thead.appendChild(td);                                                  
	                    var td=document.createElement("TD");
	                        td.setAttribute('width', '80');
	        			var text=document.createTextNode("签名");
	        			    td.appendChild(text);
	        			    thead.appendChild(td);                      
	                   							                        
				           tbody.appendChild(thead);
						      _table.appendChild(tbody);
						    
						   
						    
						//生成所有打印页面
						for(var i=0;i<hiddenstore.getCount();i++)
						{
							var printobject=hiddenstore.getAt(i);//得到store中的记录
							
                            var tr=document.createElement("TR");
                                tr.setAttribute('align', 'center');
							var td=document.createElement("TD");
							    td.setAttribute('width', '70');
						    var text=document.createTextNode(printobject.get("examnum"));
						        td.appendChild(text);
						        tr.appendChild(td);
						    var td=document.createElement("TD");
						        td.setAttribute('width', '65');
						    var text=document.createTextNode(printobject.get("name"));
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
						        td.setAttribute('width', '65');
						    var text=document.createTextNode(printobject.get("studentnum"));
						        td.appendChild(text);
		                        tr.appendChild(td);
		                    var td=document.createElement("TD");
		        		    var text=document.createTextNode(printobject.get("collegename"));
		        			    td.appendChild(text);
		                        tr.appendChild(td); 
		                    var td=document.createElement("TD");
		            	    var text=document.createTextNode(printobject.get("classnum"));
		            		    td.appendChild(text);
		                        tr.appendChild(td);                 
		             //       var td=document.createElement("TD");
					 //       var text=document.createTextNode(printobject.get("grade"));
					//			td.appendChild(text);
		            //            tr.appendChild(td);                                                    
		                    var td=document.createElement("TD");
		                        td.setAttribute('width', '80');
		                    var str="";
		                  //  if(printobject.get("remark")==null)str="";
		                  //  else str=printobject.get("remark");
		        			var text=document.createTextNode(str);	   
		        			    td.appendChild(text);
		                        tr.appendChild(td);  								                   
		                       
		                        
		                        tbody.appendChild(tr);   
						 }
						//加页尾
				    	   var tfoot=document.createElement("TFOOT");
						
						   var td=document.createElement("TD");                                        							      
						        td.setAttribute('colspan', '4');
						        td.setAttribute('tdata','pageNo');
						        td.setAttribute('format','#');
						        td.setAttribute('align','right');
						    // var p=document.createElement("p"); 
						   var text=document.createTextNode("第#页");
						        td.appendChild(text);
						       tfoot.appendChild(td);
						       
						  var td=document.createElement("TD");                                        							      
				               td.setAttribute('colspan', '1');
				               td.setAttribute('tdata','pageCount');
				               td.setAttribute('format','#');
				               td.setAttribute('align','left');
				           var text=document.createTextNode("共#页");
           			       td.appendChild(text);
           			       tfoot.appendChild(td);  
           			    
           		    	  tbody.appendChild(tfoot); 
           		    	  
           		    	  
						_table.appendChild(tbody); 
						var _div=document.getElementById("hiddentable");
						_div.appendChild(_table);
						
						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
						//alert(printTable);
						var InfoArray=new Array("浙江省高校计算机等级考试理论考试考生名册",
								"学校代码:",                                  					
								"语种:",  
								"第",
								logicroomnum,
								"考场",
								xxdm,                                  							
								languagename,                              								
								printTable);
						
					    						    									    
					    CreatePrintPage(InfoArray);			
							
						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						LODOP.PRINT();
						
						// CreatePrintPage(xxdm,languagename,logicroomnum,printTable);	
							
					//	LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
						//LODOP.PRINT();
						k=k+1;
						printAllFunction(sectiongridhiddenstore,k);
						
				   }else{
					   Ext.MessageBox.alert('提示', "第"+logicroomnum+"考场，没有安排学生！");
					}	
		}else{
			Ext.MessageBox.alert('提示', "第"+logicroomnum+"考场，没有安排学生！");
		}
	
});
}

function Init(){
	
	//得到语种combobox的列表
  	StudentController.getLanguageList_theory(function(data){
		if(data){
			comboLanguageStore.loadData(data);
		}
  	});
  	
  	 StudentController.getSchoolName(function(data){
   		if(data){
   			xxmc=data;
   		}else{
   			Ext.MessageBox.alert('提示', "得不到学校名称！");
   		}
   	});
     	
     	StudentController.getSchoolnum(function(data){
   		if(data){
   			xxdm=data;
   		}else{
   			Ext.MessageBox.alert('提示', "得不到学校代码！");
   		}
   	});
  	
	sectiongridstore.load({
		params : {
			filter : currentSearchFilter,
			start : 0,
			limit : 30
		},
		callback:function(records,operation,success){
			if(records=="")Ext.MessageBox.alert('提示', "您还没有编排理论考场!");}
	});
}



function printTheoryExamineePageInit() 
{                                  
	new Ext.Viewport( {                                                
		layout :'border',                                          		
		hideMode: Ext.isIE ? 'offsets' : 'display',                                         		
	    items : [form,sectiongrid],                                        		
	    renderTo :Ext.getBody()                                          	
	});                                       
	Init();
}