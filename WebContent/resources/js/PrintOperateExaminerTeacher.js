
var passstudent_currentSearchFilter="";


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
		 items:[ {
			  columnWidth:.25,	
			  layout:'form',				 
			  items:[{
	        	     xtype:'combo',
	        	     fieldLabel:'语种',
	        	     hiddenName:'and$arrangeSupervisor-exArrangement-exLanguage-languagenum$=$value',
	        	     triggerAction:'all',
	        	     store:comboLanguageStore,			        	    	 
	        	     displayField:'languagename',
	        	     valueField:'languagenum',
	        	     mode:'local',
	        	     anchor:'97.5%',
	        	     editable:false,
	        	     allowBlank: false,
	        	     emptyText:'---请选择---',
	        	     listeners: {	                	  
	            	  	  select:function(){
	            	  		passstudent_currentSearchFilter = Ext.encode(form.getForm().getFieldValues());
			        		passstudentstore.load({
			        			params : {
			        						filter : passstudent_currentSearchFilter,
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



var passstudent = new Ext.data.Record.create([
                                          	{
                                          		name:'sectionnum'
                                          	},{
                                          		name:'examroom'
                                          	},{
                                          		name:'campusname'
                                          	},{
                                          		name:'languagename'
                                          	},{
                                        		name :'operateflag',convert:function(data){if(data==1){return "是";}else{return "否";}}
                                        	},{
                                          		name:'startTime'
                                          	},{
                                          		name:'supervisor'
                                          	}
                                          ]);

                                          var passstudentstore = new Ext.data.Store( {
                                          	reader : new Ext.data.JsonReader({
                                                   totalProperty : 'totalProperty',
                                                   root : 'root'
                                                }, passstudent),
                                          	proxy : new Ext.ux.data.DWRProxy({
                                          	     dwrFunction : StudentController.getOperateExaminerTeacher
                                          	  })
                                          });
                                          
                                          
                                          var hiddenstore = new Ext.data.Store( {
                                            	reader : new Ext.data.JsonReader({
                                                     totalProperty : 'totalProperty',
                                                     root : 'root'
                                                  }, passstudent),
                                            	proxy : new Ext.ux.data.DWRProxy({
                                            	     dwrFunction : StudentController.getOperateExaminerTeacher
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
                                          	autoExpandColumn : 'supervisor',
                                          	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
                                          	sm:sm,
                                          	columns : [new Ext.grid.RowNumberer(),
                                          	           sm,
                                          	{
                                          		id : 'sectionnum',
                                          		header : '场次',
                                          	  	dataIndex :'sectionnum',
                                          	  	width :100,
                                          	 	sortable :true
                                          	},	{
                                          		id : 'examroom',
                                          		header : '考试地点',
                                          	  	dataIndex :'examroom',
                                          	  	width :200,
                                          	 	sortable :true
                                          	},{
                                          		id : 'campusname',
                                          		header : '校区',
                                          	  	dataIndex :'campusname',
                                          	  	width :140,
                                          	 	sortable :true
                                          	},	{
                                          		id : 'languagename',
                                          		header : '语种',
                                          	  	dataIndex :'languagename',
                                          	  	width :180,
                                          	 	sortable :true
                                          	},{
                                          		id : 'operateflag',
                                          		header : '上机考场',
                                          	  	dataIndex :'operateflag',
                                          	  	width :80,
                                          	 	sortable :true
                                          	},{
                                          		id : 'startTime',
                                          		header : '开考时间',
                                          	  	dataIndex :'startTime',
                                          	  	width :180,
                                          	 	sortable :true
                                          	},{
                                          		id:'supervisor',
                                          		header : '监考教师',
                                          	  	dataIndex :'supervisor',
                                          	  	width :100,
                                          	 	sortable :true
                                          	}
                                          	],tbar:[ {
                                    			text : '监考教师名单设计',
                                    			iconCls : 'edit',
                                    			onClick : function() {
                                    				//判断用户是否选中记录
                                  					if (passstudentgrid.getSelectionModel().hasSelection()) {
                                  						
                                  						LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印");
                                  						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
                                  						var selections = passstudentgrid.getSelectionModel().getSelections();
                                  						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
                                  					
                                  						//每次先要清掉printTable，保证不会重复多个printTable
                                      					var tableId=document.getElementById("printTable");                                					
                                      					if (tableId!=null) {
                                      						tableId.parentNode.removeChild(tableId);
                                      						     
                                      						}
                                      					
                                  						var _table=document.createElement("TABLE");
                                  						    _table.style.fontSize='14px';                         						        
                                  						    _table.setAttribute('border','1');
                                  						    _table.setAttribute('width', '720');
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
                        						        td.setAttribute('width', '40');
                        						    var text=document.createTextNode("场次");
                        						        td.appendChild(text);
                        						        tr.appendChild(td);
                        						    var td=document.createElement("TD");
                        						        td.setAttribute('width', '100');
                        						    var text=document.createTextNode("考试地点");
                        						        td.appendChild(text);
                                                      tr.appendChild(td);
                                                  var td=document.createElement("TD");
                                                  td.setAttribute('width', '60');
                        						    var text=document.createTextNode("校区");
                        						        td.appendChild(text);
                                                      tr.appendChild(td);                                                                                                                        
                                                  var td=document.createElement("TD");
                                                  td.setAttribute('width', '150');
                            					    var text=document.createTextNode("语种");
                            						    td.appendChild(text);
                                                      tr.appendChild(td);
                                            //      var td=document.createElement("TD");
                        						//    var text=document.createTextNode("上机考场");
                        						//        td.appendChild(text);
                                              //        tr.appendChild(td);
                                                  var td=document.createElement("TD");
                                                  td.setAttribute('width', '110');
                        						    var text=document.createTextNode("开考时间");
                        						        td.appendChild(text);
                                                      tr.appendChild(td);
                                                    var td=document.createElement("TD");
                                                    td.setAttribute('width', '100');
                                			        var text=document.createTextNode("监考教师");
                                						td.appendChild(text);
                                                        tr.appendChild(td);                    
                                                              
                                                                  
                                  						    tbody.appendChild(tr);
                                  						    _table.appendChild(tbody);
                                  						    
                                  						    
                                  						//生成所有打印页面
                                  						for(var i=0;i<selections.length;i++)
                                  						{
                                  							var printobject=selections[i];//得到grid中选中的记录
                                  							
                                  							var tr=document.createElement("TR");
                                  							tr.setAttribute('align', 'center');
                                  							var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("sectionnum"));
                                  						        td.appendChild(text);
                                  						        tr.appendChild(td);
                                  						    var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("examroom"));
                                  						        td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("campusname"));
                                  						        td.appendChild(text);
                                                                  tr.appendChild(td);                                                                  
                                                              var td=document.createElement("TD");
                                      					    var text=document.createTextNode(printobject.get("languagename"));
                                      						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                       //     var td=document.createElement("TD");
                                  						//    var text=document.createTextNode(printobject.get("operateflag"));
                                  						 //       td.appendChild(text);
                                                        //        tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("startTime"));
                                  						        td.appendChild(text);
                                                                  tr.appendChild(td);
                                                              var td=document.createElement("TD");
                                          			        var text=document.createTextNode(printobject.get("supervisor"));
                                          						td.appendChild(text);
                                                                  tr.appendChild(td);           
                                                 
                                                                  
                                                                  tbody.appendChild(tr);                                       						                                       						    
                                  						}
                                  						_table.appendChild(tbody); 
                                  						var _div=document.getElementById("hiddentable");
                                  						_div.appendChild(_table);
                                  						
                                  						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
                                  						//alert(printTable);
                                  						var InfoArray=new Array("浙江省高校计算机等级考试上机监考教师名单",                             								                            								
                                  								printTable);
                                  						
                                  					    						    									    
                                  					    CreatePrintPage(InfoArray);			
                                  							
                                  						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                  						LODOP.PRINT_SETUP();
                                  						reSizePrintParam();
                                  						
                                  						
                                  						// CreatePrintPage(printTable);	
                                  						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                  						//LODOP.PRINT_SETUP();                       					                         						
                                  		    			
                                  					}else{
                                  						Ext.MessageBox.alert('提示', "请选择一条记录!");
                                  					}													
                                    				
                                    							
                                    			}
                                    		},'-',{
                                    			text : '打印当前页',
                                    			iconCls : 'print',
                                    			onClick : function() {
                                    				
                                    				//判断用户是否选中记录
                                  					if (passstudentgrid.getSelectionModel().hasSelection()) {
                                  						
                                  						LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印");
                                  						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
                                  						var selections = passstudentgrid.getSelectionModel().getSelections();
                                  						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
                                  					
                                  						//每次先要清掉printTable，保证不会重复多个printTable
                                      					var tableId=document.getElementById("printTable");                                					
                                      					if (tableId!=null) {
                                      						tableId.parentNode.removeChild(tableId);
                                      						     
                                      						}
                                      					
                                  						var _table=document.createElement("TABLE");
                                  						    _table.style.fontSize='14px';
                                  						    _table.setAttribute('border','1');
                                  						    _table.setAttribute('width', '720');
                                  						    _table.setAttribute('id', 'printTable');
                                  						    _table.setAttribute('align', 'center');
                                  						    _table.setAttribute('border-collapse', 'collapse');
                                  						    _table.setAttribute('cellspacing', '0');
                                  						    _table.setAttribute('cellpadding', '4');
                                  						    _table.setAttribute('font-size', '1');
                                  						    
                                  					    var tbody=document.createElement("TBODY");  
                                  					    
                                  						    var tr=document.createElement("TR");
                                  						        tr.style.fontSize='16px';
                                  						        tr.style.fontWeight='bold';
                                  						        tr.setAttribute('align', 'center');
                                  						    var td=document.createElement("TD");
                                  						        td.setAttribute('width', '40');
                                  						    var text=document.createTextNode("场次");
                                  						        td.appendChild(text);
                                  						        tr.appendChild(td);
                                  						    var td=document.createElement("TD");
                                  						        td.setAttribute('width', '100');
                                  						    var text=document.createTextNode("考试地点");
                                  						        td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '60');
                                  						    var text=document.createTextNode("校区");
                                  						        td.appendChild(text);
                                                                tr.appendChild(td);                                                                                                                        
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '140');
                                      					    var text=document.createTextNode("语种");
                                      						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                      //      var td=document.createElement("TD");
                                  						//    var text=document.createTextNode("上机考场");
                                  						//        td.appendChild(text);
                                                        //        tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '120');
                                  						    var text=document.createTextNode("开考时间");
                                  						        td.appendChild(text);
                                                                tr.appendChild(td);
                                                              var td=document.createElement("TD");
                                                              td.setAttribute('width', '100');
                                          			        var text=document.createTextNode("监考教师");
                                          						td.appendChild(text);
                                                                  tr.appendChild(td);           
                                                              
                                                                  
                                  						    tbody.appendChild(tr);
                                  						    _table.appendChild(tbody);
                                  						    
                                  						    
                                  						//生成所有打印页面
                                  						for(var i=0;i<selections.length;i++)
                                  						{
                                  							var printobject=selections[i];//得到grid中选中的记录
                                  							
                                  							var tr=document.createElement("TR");
                                  							tr.setAttribute('align', 'center');
                                  							var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("sectionnum"));
                                  						        td.appendChild(text);
                                  						        tr.appendChild(td);
                                  						    var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("examroom"));
                                  						        td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("campusname"));
                                  						        td.appendChild(text);
                                                                  tr.appendChild(td);                                                                  
                                                              var td=document.createElement("TD");
                                      					    var text=document.createTextNode(printobject.get("languagename"));
                                      						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                         //   var td=document.createElement("TD");
                                  						 //   var text=document.createTextNode(printobject.get("operateflag"));
                                  						 //       td.appendChild(text);
                                                        //        tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                  						    var text=document.createTextNode(printobject.get("startTime"));
                                  						        td.appendChild(text);
                                                                  tr.appendChild(td);
                                                              var td=document.createElement("TD");
                                          			        var text=document.createTextNode(printobject.get("supervisor"));
                                          						td.appendChild(text);
                                                                  tr.appendChild(td);           
                                                 
                                                                  
                                                                  tbody.appendChild(tr);                                       						                                       						    
                                  						}
                                  						_table.appendChild(tbody); 
                                  						var _div=document.getElementById("hiddentable");
                                  						_div.appendChild(_table);
                                  						
                                  						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
                                  						//alert(printTable);
                                  						var InfoArray=new Array("浙江省高校计算机等级考试上机监考教师名单",                             								                            								
                                  								printTable);
                                  						
                                  					    						    									    
                                  					    CreatePrintPage(InfoArray);			
                                  							
                                  						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                  						LODOP.PREVIEW(); 
                                  						
                                  						// CreatePrintPage(printTable);	
                                  						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                  						//LODOP.PREVIEW();                       					                         						
                                  		    			
                                  					}else{
                                  						Ext.MessageBox.alert('提示', "请选择一条记录!");
                                  					}													
                                    				
                                    							
                                    			}
                                    		},'-',{
                                    			text : '打印全部',
                                    			iconCls : 'print',
                                    			onClick : function() {
                                    				
                                    				var params = {};
                                    				   params.fliter = passstudent_currentSearchFilter;
                                    				   params.start = 0;
                                    				   params.limit = 10000;
                                    				   hiddenstore.load({params:params,callback: function(){
                                    					   
                                    					 //判断数据库中是否有符合条件的数据
                                    					   if (hiddenstore.getCount()>0) {
                                         						
                                    						    LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印");
                                         						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
                                         						var selections = passstudentgrid.getSelectionModel().getSelections();
                                         						//动态创建table利用 LODOP.ADD_PRINT_TABLE(40,57,680,500,strTableHTML);来打印表格
                                         					
                                         						//每次先要清掉printTable，保证不会重复多个printTable
                                             					var tableId=document.getElementById("printTable");                                					
                                             					if (tableId!=null) {
                                             						tableId.parentNode.removeChild(tableId);
                                             						     
                                             						}
                                             					
                                         						var _table=document.createElement("TABLE");
                                         						    _table.style.fontSize='14px';
                                         						    _table.setAttribute('border','1');
                                         						    _table.setAttribute('width', '720');
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
                                 						        td.setAttribute('width', '40');
                                 						    var text=document.createTextNode("场次");
                                 						        td.appendChild(text);
                                 						       thead.appendChild(td);
                                 						    var td=document.createElement("TD");
                                 						        td.setAttribute('width', '100');
                                 						    var text=document.createTextNode("考试地点");
                                 						        td.appendChild(text);
                                 						       thead.appendChild(td);
                                                           var td=document.createElement("TD");
                                                           td.setAttribute('width', '60');
                                 						    var text=document.createTextNode("校区");
                                 						        td.appendChild(text);
                                 						       thead.appendChild(td);                                                                                                                        
                                                           var td=document.createElement("TD");
                                                           td.setAttribute('width', '150');
                                     					    var text=document.createTextNode("语种");
                                     						    td.appendChild(text);
                                     						   thead.appendChild(td);
                                                     //      var td=document.createElement("TD");
                                 						//    var text=document.createTextNode("上机考场");
                                 						//        td.appendChild(text);
                                                       //        tr.appendChild(td);
                                                           var td=document.createElement("TD");
                                                           td.setAttribute('width', '110');
                                 						    var text=document.createTextNode("开考时间");
                                 						        td.appendChild(text);
                                 						       thead.appendChild(td);
                                                             var td=document.createElement("TD");
                                                             td.setAttribute('width', '100');
                                         			        var text=document.createTextNode("监考教师");
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
                                              						    var text=document.createTextNode(printobject.get("sectionnum"));
                                              						        td.appendChild(text);
                                              						        tr.appendChild(td);
                                              						    var td=document.createElement("TD");
                                              						    var text=document.createTextNode(printobject.get("examroom"));
                                              						        td.appendChild(text);
                                                                            tr.appendChild(td);
                                                                        var td=document.createElement("TD");
                                              						    var text=document.createTextNode(printobject.get("campusname"));
                                              						        td.appendChild(text);
                                                                              tr.appendChild(td);                                                                  
                                                                          var td=document.createElement("TD");
                                                  					    var text=document.createTextNode(printobject.get("languagename"));
                                                  						    td.appendChild(text);
                                                                            tr.appendChild(td);
                                                                    //    var td=document.createElement("TD");
                                              						//    var text=document.createTextNode(printobject.get("operateflag"));
                                              						//        td.appendChild(text);
                                                                    //        tr.appendChild(td);
                                                                        var td=document.createElement("TD");
                                              						    var text=document.createTextNode(printobject.get("startTime"));
                                              						        td.appendChild(text);
                                                                              tr.appendChild(td);
                                                                          var td=document.createElement("TD");
                                                      			        var text=document.createTextNode(printobject.get("supervisor"));
                                                      						td.appendChild(text);
                                                                              tr.appendChild(td);             
                                                        
                                                                         
                                                                         tbody.appendChild(tr);                                       						                                       						    
                                         						}
                                         							//加页尾
                                  						    	   var tfoot=document.createElement("TFOOT");
                                      							
                                      							   var td=document.createElement("TD");                                        							      
                                      							        td.setAttribute('colspan', '5');
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
                                         						var InfoArray=new Array("浙江省高校计算机等级考试上机监考教师名单",                             								                            								
                                          								printTable);
                                          						
                                          					    						    									    
                                          					    CreatePrintPage(InfoArray);			
                                          							
                                          						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                          						LODOP.PREVIEW(); 
                                         						
                                         						// CreatePrintPage(printTable);	
                                         						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                         						//LODOP.PREVIEW();                       					                         						
                                         		    			
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
                                              	pageSize:30,
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
                                            	
                                            	   //得到语种combobox的列表
                                            	  	StudentController.getLanguageList(function(data){
                                            			if(data){
                                            				comboLanguageStore.loadData(data);
                                            			}
                                            	  	});
                                            	  	
                                            	passstudentstore.load({
                                            		params : {
                                            			filter : passstudent_currentSearchFilter,
                                            			start : 0,
                                            			limit : 30
                                            		},
                                            		callback:function(records,operation,success){
                                            			if(records=="")Ext.MessageBox.alert('提示', "您还没有编排上机监考教师!");}
                                            	});
                                            }                                    


function printOperateExaminerTeacherPageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [form,passstudentgrid],
		renderTo :Ext.getBody()
	});
	passStudentGridInit();
}