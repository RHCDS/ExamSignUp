var xxmc="";
var xxdm="";


var form=new Ext.form.FormPanel({
	   title:"选择打印条件",
	   region:"north",
	   frame:true,
	   width:300,
	   height:110,
	   labelWidth:80,
	   labelAlign:'right',
	   items:[{
		  layout:'column',
		  items:[{
			  columnWidth:.3,	
			  layout:'form',				 
			  items:[{
				  id:'yiji',
				  xtype:'numberfield',
				  fieldLabel: '一级报名费',
				  name: 'AGradeFee',
				  allowNegative:false,
				  anchor:'97.5%',
				  allowBlank: false,
				  value:'30'}] 
	          },{
				  columnWidth:.3,	
				  layout:'form',				 
				  items:[{
					  id:'erji',
					  xtype:'numberfield',
					  fieldLabel: '二级报名费',
					  name: 'BGradeFee',
					  allowNegative:false,
					  anchor:'97.5%',
					  allowBlank: false,
					  value:'30'}] 
		          },{
					  columnWidth:.4,	
					  layout:'form',				 
					  items:[{
						  id:'sanji',
						  xtype:'numberfield',
						  fieldLabel: '三级报名费',
						  name: 'CGradeFee',
						  allowNegative:false,
						  anchor:'97.5%',
						  allowBlank: false,
						  value:'30'}] 
			          },{
						  columnWidth:.3,	
						  layout:'form',				 
						  items:[{
							  id:'mainExaminer',
							  xtype:'textfield',
							  fieldLabel: '主考人',
							  name: 'mainExaminer',
							  anchor:'97.5%',
							  allowBlank: false
							  }] 
				          },{
							  columnWidth:.3,	
							  layout:'form',				 
							  items:[{
								  id:'inspector',
								  xtype:'textfield',
								  fieldLabel: '校外巡视人员',
								  name: 'inspector',
								  anchor:'97.5%',
								  allowBlank: false
								  }] 
					          },{
					        	  columnWidth:.4,	
					        	  layout:'form',
								  items:[{
									  id:'signupdate',
									  xtype:'datefield',
									  fieldLabel:'报名日期',
									  emptyText:'请选择',
									  format:'Y-m-d',
					        	      name:'signupdate',
					        	      cls:"Wdate",	
					        	      anchor:'98%',
					        	      allowBlank: false				        	      
								  }]			      
						          },{
									  columnWidth:.3,	
									  layout:'form',				 
									  items:[{
										  id:'campusnum',
										  xtype:'textfield',
										  fieldLabel: '校区代码',
										  name: 'campusnum',
										  anchor:'97.5%',
										  emptyText:'不填则打印全校'}] 
							          }//,					           					          
//						          {
//	        	                   columnWidth:.09,	
//	        	                   layout:'form',
//	        	                   items:[{
//										   xtype:'button',
//					                       text:'汇总',
//					                       anchor:'87.5%',
//					                        handler : function() {
//				        	                if(form.getForm().isValid()){
//				        		
//				        	                	var Condition = form.getForm().getValues();	
//				        	                	StudentController.getSignUpInfo(Condition,function(data){ 
//									     	    	if(data){
//									     	    		languageListStore.loadData(data);
//									     	    	}else
//									     	    		Ext.MessageBox.alert('提示',"查询失败！");
//									     	    	
//									     	    });  
//				        		
//				        	                   }else{
//				        	                	Ext.MessageBox.alert('提示',"输入打印条件有误！");
//				        	                        }
//				                               }
//				                    }]
		//	                }
	   ]
		       
		   }]		   		   
});













var gridRecordList=[];

var institutionnum = "";

var gradeRecord = Ext.data.Record.create([ {
	name : 'gradename'
} ]);

//var parentcollegeStore = new Ext.data.Store({
//	reader : new Ext.data.JsonReader({
//		totalProperty : 'totalProperty',
//		root : 'root'
//	}, parentcollegeRecord),
//	proxy : new Ext.ux.data.DWRProxy({
//		dwrFunction : InstitutionController.loadChildInstitution
//	})
//});

var languageListRecord =new Ext.data.Record.create([{
	name :'languagename'
}, {
	name :'languagenum'
}, {
	name :'total'
}
]);

var languageListStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'languagenum'
  }, languageListRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction :LanguageController.loadlanguageList
  })
});

var statisticsRecord ;
var statisticsStore;
var statisticsGrid;

					

		var gridColumnList=[];
		languageListStore.load({callback: function(){
		i=2;
		language_count=0;
		gridRecordList[0]={name:"gradename"};
		gridRecordList[1]={name:"total"};
		languageListStore.each(function(record){
				gridRecordList[i]={name:record.get("languagenum")+"count"};
				i++;
				language_count++;
		});
		gridRecordList[i]={name:"count"};
		gridRecordList[i+1]={name:"currentState"};
		
		i=3;
	//	gridColumnList[0]=new Ext.grid.RowNumberer();
		gridColumnList[0]=new Ext.grid.CheckboxSelectionModel();
		gridColumnList[1]={id:"gradename",header:"入学年份",dataIndex:"gradename",width:150,sortable:true};
		gridColumnList[2]={id:"total",header:"报考人数",dataIndex:"total",width:150,sortable:true};
		levelNum = "";
		languageListStore.each(function(record){
			gridColumnList[i]={id:  eval("\""+record.get("languagenum")+"count"+"\""),header: eval("\""+record.get("languagenum")+"\""),
					dataIndex: eval("\""+record.get("languagenum")+"count"+"\""),width:50,sortable:true};
			i++;
			
		});

		
		statisticsRecord= new Ext.data.Record.create(gridRecordList);
		statisticsStore = new Ext.data.Store( {
			reader : new Ext.data.JsonReader({
		         totalProperty : 'totalProperty',
		         root : 'root'
		      }, statisticsRecord),
			proxy : new Ext.ux.data.DWRProxy({
			     dwrFunction : StudentController.calcStuForGrade
			  })
		});
		
		var sm=new Ext.grid.CheckboxSelectionModel();
		
		statisticsGrid=new Ext.grid.GridPanel({
			region:'center',
			id : 'statisticsGrid',
			title : '年级报名统计表',
			store : statisticsStore,
		 loadMask :true,
		 sm:sm,
			stripeRows :true,
			autoScroll:true,
			//autoExpandColumn : 'institutionname',
			viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	//		columns : [gridColumnList[0],gridColumnList[1],gridColumnList[2],gridColumnList[3],gridColumnList[4],gridColumnList[5]],
			columns : gridColumnList,
			tbar:[{
			text: '导出年级报名统计表',
			iconCls : 'upload-icon',
			onClick : function(){
				exportNJBMTJB();
			}
			}],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : institutionnum
						
						}
					});
				}
			},
			tbar:[ {
    			text : '汇总表设计',
    			iconCls : 'edit',
    			onClick : function() {
    				if(form.getForm().isValid()){		
    					//判断用户是否选中记录
    					if (statisticsGrid.getSelectionModel().hasSelection()) {
    						
    						
    						var yiji=Ext.getCmp("yiji").getValue();
    						var erji=Ext.getCmp("erji").getValue();
    						var sanji=Ext.getCmp("sanji").getValue();
    						var mainExaminer=Ext.getCmp("mainExaminer").getValue();//得到主考人
    						var inspector=Ext.getCmp("inspector").getValue();//得到校外巡视人员
    						var campusnum=Ext.getCmp("campusnum").getValue();//得到校区代码
    						var signupdate=Ext.getCmp("signupdate").getValue().format('Y年m月d日');//得到日期
    								   						
    						var selections = statisticsGrid.getSelectionModel().getSelections();
    						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
    						
    						//每次先要清掉printTable，保证不会重复多个printTable
          					var tableId=document.getElementById("printTable");                                					
          					if (tableId!=null) {
          						tableId.parentNode.removeChild(tableId);
          						     
          						}
          					
    						var _table=document.createElement("TABLE");
    						    _table.setAttribute('border','1');
    						    _table.setAttribute('width', '1060');
    						    _table.setAttribute('height', '400');
    						    _table.setAttribute('id', 'printTable');
    						    _table.setAttribute('align', 'center');
    						    _table.setAttribute('border-collapse', 'collapse');
    						    _table.setAttribute('cellspacing', '0');
    						    _table.setAttribute('cellpadding', '4');
          					
    						    
    					    var tbody=document.createElement("TBODY");  
    					    
    						    var tr=document.createElement("TR");
    						        tr.setAttribute('align', 'center');
    						    
    						    var td=document.createElement("TD");
    						    var text=document.createTextNode("入学年份");
    						        td.appendChild(text);
    						        tr.appendChild(td);
    						        tbody.appendChild(tr);
    						        
    						    //    var tr=document.createElement("TR");
    						    var td=document.createElement("TD");
    						    var text=document.createTextNode("报考人数");
    						        td.appendChild(text);
                                    tr.appendChild(td);
//                                    tbody.appendChild(tr);
                                
                                    for(var i=0;i<language_count;i++)
                                    {
                                    	 var td=document.createElement("TD");
                 					    var text= document.createTextNode(languageListStore.getAt(i).get("languagenum"));
                 						    td.appendChild(text);
                                            tr.appendChild(td);
                                            tbody.appendChild(tr);
                                    	
                                    }
                                    
                                    
                                var td=document.createElement("TD");
                    			var text=document.createTextNode("报名费(元)");
                    			    td.appendChild(text);
                                    tr.appendChild(td);
                                    tbody.appendChild(tr);
                                 
    						    tbody.appendChild(tr);
    						    _table.appendChild(tbody);
    						    
    						    
    						//生成所有打印页面
    						    
    						for(var i=0;i<selections.length;i++)
    						{
    							var printobject=selections[i];//得到grid中选中的记录
    							
    							var tr=document.createElement("TR");
    							    tr.setAttribute('align', 'center');
    							    
    							var td=document.createElement("TD");
    						    var text=document.createTextNode(printobject.get("gradename"));
    						        td.appendChild(text);
    						        tr.appendChild(td);
    						        
    						        
    						        var td=document.createElement("TD");
        						    var text=document.createTextNode(printobject.get("total"));
        						        td.appendChild(text);
        						        tr.appendChild(td);
        						        
        						        
    						        
    						        var temp;							   
    						        sum=0;
    						      
    						        	languageListStore.each(function(record){
    						    			temp = record.get("languagenum")+"count";
    						    			
    						    			 var td=document.createElement("TD");
    						    		
    						    			 
    						    			 if(temp.substr(0,1)=="1")
     						    				sum=sum+printobject.get(temp)*yiji;
    						    			 else if(temp.substr(0,1)=="2")
      						    				sum=sum+printobject.get(temp)*erji;
   						    			 else if(temp.substr(0,1)=="3")
     						    				sum=sum+printobject.get(temp)*sanji;
                     					    var text= document.createTextNode(printobject.get(temp));
                     						    td.appendChild(text);
                                                tr.appendChild(td);
                                                tbody.appendChild(tr);
    						    		});
    						        	
    						        	 var td=document.createElement("TD");
						    			
                 					    var text= document.createTextNode(sum);
                 						    td.appendChild(text);
                                            tr.appendChild(td);
                                    	
                          
    						        
    						        
    						}
    						   
    						_table.appendChild(tbody); 
    						var _div=document.getElementById("hiddentable");
    						_div.appendChild(_table);
    						
    						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
    						//alert(printTable);
    						var str="";
    						if(campusnum=="不填则打印全校"||campusnum=="")str=xxmc;
    						else str=xxmc+"(校区代码:"+campusnum+")";
    						
    						var InfoArray=new Array("浙江省高校计算机等级考试学校集体报名汇总表",
    								"学校代码:",   								
    								"学校名称:",
    								xxdm,
    								str,
    								"报   名   情   况",
    								printTable,
    								"主考人:",
    								"校外巡视人员:",
    								"负责人（签名）:",
    								mainExaminer,
    								inspector,
    								signupdate);
    						
    					    						    									    
    					    CreatePrintPage(InfoArray);
    																
    						//CreatePrintPage(xxdm,str,mainExaminer,inspector,signupdate,printTable);	
     						
    						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
    						LODOP.PRINT_SETUP();
    						reSizePrintParam();
    		    			
    					  }else{
    						    Ext.MessageBox.alert('提示', "请选择一条记录!");
    					       }	
    				}else{
	                	Ext.MessageBox.alert('提示',"请选择打印条件！");
	                        }
    				
    			}
    		},'-',{
    			text : '打印汇总表',
    			iconCls : 'print',
    			onClick : function() {
    				if(form.getForm().isValid()){		
    					//判断用户是否选中记录
    					if (statisticsGrid.getSelectionModel().hasSelection()) {
    						
    						
    						var yiji=Ext.getCmp("yiji").getValue();
    						var erji=Ext.getCmp("erji").getValue();
    						var sanji=Ext.getCmp("sanji").getValue();
    						var mainExaminer=Ext.getCmp("mainExaminer").getValue();//得到主考人
    						var inspector=Ext.getCmp("inspector").getValue();//得到校外巡视人员
    						var campusnum=Ext.getCmp("campusnum").getValue();//得到校区代码
    						var signupdate=Ext.getCmp("signupdate").getValue().format('Y年m月d日');//得到日期
    								   						
    						var selections = statisticsGrid.getSelectionModel().getSelections();
    						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
    						
    						//每次先要清掉printTable，保证不会重复多个printTable
          					var tableId=document.getElementById("printTable");                                					
          					if (tableId!=null) {
          						tableId.parentNode.removeChild(tableId);
          						     
          						}
//          					
//    						var _table=document.createElement("TABLE");
//    						    _table.setAttribute('border','1');
//    						    _table.setAttribute('width', '900');
//    						    _table.setAttribute('height', '400');
//    						    _table.setAttribute('id', 'printTable');
//    						    _table.setAttribute('align', 'center');
//    						    _table.setAttribute('border-collapse', 'collapse');
//    						    _table.setAttribute('cellspacing', '0');
//    						    _table.setAttribute('cellpadding', '4');
          					
          					var _table=document.createElement("TABLE");
						    _table.setAttribute('border','1');
						    _table.setAttribute('width', '1060');
						    _table.setAttribute('height', '400');
						    _table.setAttribute('id', 'printTable');
						    _table.setAttribute('align', 'center');
						    _table.setAttribute('border-collapse', 'collapse');
						    _table.setAttribute('cellspacing', '0');
						    _table.setAttribute('cellpadding', '4');
    						    
    					  
    						    
    						    
    						    
    						    
    						    
    						    
    						    
    						    var tbody=document.createElement("TBODY");  
        					    
    						    var tr=document.createElement("TR");
    						        tr.setAttribute('align', 'center');
    						    
    						    var td=document.createElement("TD");
    						    var text=document.createTextNode("入学年份");
    						        td.appendChild(text);
    						        tr.appendChild(td);
    						        tbody.appendChild(tr);
    						        
    						    //    var tr=document.createElement("TR");
    						    var td=document.createElement("TD");
    						    var text=document.createTextNode("报考人数");
    						        td.appendChild(text);
                                    tr.appendChild(td);
//                                    tbody.appendChild(tr);
                                
                                    for(var i=0;i<language_count;i++)
                                    {
                                    	 var td=document.createElement("TD");
                 					    var text= document.createTextNode(languageListStore.getAt(i).get("languagenum"));
                 						    td.appendChild(text);
                                            tr.appendChild(td);
                                            tbody.appendChild(tr);
                                    	
                                    }
                                    
                                    
                                var td=document.createElement("TD");
                    			var text=document.createTextNode("报名费(元)");
                    			    td.appendChild(text);
                                    tr.appendChild(td);
                                    tbody.appendChild(tr);
                                 
    						    tbody.appendChild(tr);
    						    _table.appendChild(tbody);
    						    
    						    
    						    
    						    
    						    
    						    
    						    
    						    
    						    
    						//生成所有打印页面
    						for(var i=0;i<selections.length;i++)
    						{
    							
    							
    							
    							
    							var printobject=selections[i];//得到grid中选中的记录
    							
    							var tr=document.createElement("TR");
    							    tr.setAttribute('align', 'center');
    							    
    							var td=document.createElement("TD");
    						    var text=document.createTextNode(printobject.get("gradename"));
    						        td.appendChild(text);
    						        tr.appendChild(td);
    						        
    						        
    						        var td=document.createElement("TD");
        						    var text=document.createTextNode(printobject.get("total"));
        						        td.appendChild(text);
        						        tr.appendChild(td);
        						        
        						        
    						        
    						        var temp;		
    						       sum=0;
    						        	languageListStore.each(function(record){
    						    			temp = record.get("languagenum")+"count";
    						    			
    						    			 if(temp.substr(0,1)=="1")
      						    				sum=sum+printobject.get(temp)*yiji;
     						    			 else if(temp.substr(0,1)=="2")
       						    				sum=sum+printobject.get(temp)*erji;
     						    			 else if(temp.substr(0,1)=="3")
       						    				sum=sum+printobject.get(temp)*sanji;
    						    			 var td=document.createElement("TD");
                     					     var text= document.createTextNode(printobject.get(temp));
                     						    td.appendChild(text);
                                                tr.appendChild(td);
                                                tbody.appendChild(tr);
    						    		});
    						        	
    						        	
    						        	var td=document.createElement("TD");
                 					    var text= document.createTextNode(sum);
                 						    td.appendChild(text);
                                            tr.appendChild(td);
                                            tbody.appendChild(tr);
    							
    							
    							
    							
    							                              						                                       						    
    						   }
    						_table.appendChild(tbody); 
    						var _div=document.getElementById("hiddentable");
    						_div.appendChild(_table);
    						
    						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
    						//alert(printTable);
    						var str="";
    						if(campusnum=="不填则打印全校"||campusnum=="")str=xxmc;
    						else str=xxmc+"(校区代码:"+campusnum+")";
    						
    						
    						var InfoArray=new Array("浙江省高校计算机等级考试学校集体报名汇总表",
    								"学校代码:",   								
    								"学校名称:",
    								xxdm,
    								str,
    								"报   名   情   况",
    								printTable,
    								"主考人:",
    								"校外巡视人员:",
    								"负责人（签名）:",
    								mainExaminer,
    								inspector,
    								signupdate);
    						
    					    						    									    
    					    CreatePrintPage(InfoArray);
    						//CreatePrintPage(xxdm,str,mainExaminer,inspector,signupdate,printTable);	
     						
    						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");    						
    						LODOP.PREVIEW();
    						
    		    			
    					  }else{
    						    Ext.MessageBox.alert('提示', "请选择一条记录!");
    					       }	
    				}else{
	                	Ext.MessageBox.alert('提示',"输入打印条件有误！");
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
			}]
});
		
	
	}
	});










function printSignUpSummaryPageInit(institution) {
	institutionnum = institution;
	

	
  	new Ext.Viewport( {
  		layout :'border',
  		hideMode: Ext.isIE ? 'offsets' : 'display',
  		items : [form,statisticsGrid],
  		renderTo :Ext.getBody()
  	});
//  	StudentController.getSignUpInfo(function(data){
//		if(data){
//			collegesignupstore.loadData(data);
//		}else{
//			Ext.MessageBox.alert('提示', "没有数据！");
//		}
//	});
//  	
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
  	
  	
//  	Ext.form.DateField在firefox 3、chrome和safari中显示会有问题，界面将会拉的很长很长。
//  	下面的代码能够修复该问题：
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

