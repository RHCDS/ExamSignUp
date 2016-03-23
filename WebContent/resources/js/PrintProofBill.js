var passstudent_currentSearchFilter="";

var comboProfessionStore = new Ext.data.JsonStore({
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
		 items:[/*{
			  columnWidth:.25,	
			  layout:'form',				 
			  items:[
			         {
	        	     xtype:'combo',	 
	        	     id:'language',
	        	     fieldLabel:'语种',
	        	     hiddenName:'and$student-exLanguage-languagenum$=$value',
	        	     //name:'yzname',
	        	     triggerAction:'all',
	        	     store:comboLanguageStore,			        	    	 
	        	     displayField:'name',
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
	          },*/{
				  columnWidth:.25,	
				  layout:'form',				 
				  items:[{
		        	     xtype:'combo',	
		        	     id:'college',
		        	     fieldLabel:'学院',
		        	     hiddenName:'and$student-exCollege-name$=$value',
		        	    // name:'collegename',
		        	     triggerAction:'all',
		        	     store:comboCollegeStore,			        	    	 
		        	     displayField:'name',
		        	     valueField:'value',
		        	     mode:'local',
		        	     anchor:'97.5%',
		        	     editable:false,
		        	     allowBlank: false,
		        	     emptyText:'---请选择---',
		        	     listeners: {	                	  
		            	  	  select:function(){
		            	  		   var collegename=Ext.get('college').dom.value;
		            	  		   //得到班级combobox的列表
                            	   StudentController.getClassNameList(collegename,function(data){
                            			if(data){
                            				comboClassNameStore.loadData(data);
                            				Ext.get('class').dom.value='不选';
                            				
                            			}
                            	  	});
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
		          },{
				  columnWidth:.25,	
				  layout:'form',				 
				  items:[{
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


var passstudent = new Ext.data.Record.create([{
												name:'studentnum'
											},{
												name:'name'
											},{
												name:'idnum'
											},{
                                          		name:'sex',convert:function(data){if(data=="M"){return "男";}else return "女";}
                                          	},{
                                          		name:'languagenum'
                                          	},{
                                          		name:'grade'
                                          	},{
                                          		name:'lengthofyear'
                                          	},{
                                          		name:'collegename'
                                          	},{
                                          		name:'classname'
                                          	},{
                                          		name:'languagename'
                                          	},{
                                          		name:'professionname'
                                          	}
                                          ]);

                                          var passstudentstore = new Ext.data.Store( {
                                          	reader : new Ext.data.JsonReader({
                                                   totalProperty : 'totalProperty',
                                                   root : 'root'
                                                }, passstudent),
                                          	proxy : new Ext.ux.data.DWRProxy({
                                          	     dwrFunction : StudentController.getStudentByProfession_language
                                          	  })
                                          });
                                          
                                          
                                          var hiddenstore = new Ext.data.Store( {
                                            	reader : new Ext.data.JsonReader({
                                                     totalProperty : 'totalProperty',
                                                     root : 'root'
                                                  }, passstudent),
                                            	proxy : new Ext.ux.data.DWRProxy({
                                            	     dwrFunction : StudentController.getStudentByProfession_language
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
                                         	autoExpandColumn : 'professionname',
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
                                               },{
                                                 id : 'studentnum',
                                                 header : '学号',
                                                 dataIndex :'studentnum',
                                                 width :120,
                                                 sortable :true
                                             },{
                                             		id : 'languagename',
                                             		header : '语种',
                                             	  	dataIndex :'languagename',
                                             	  	width :200,
                                             	 	sortable :true
                                             	},{
                                          		id : 'idnum',
                                          		header : '身份证',
                                          	  	dataIndex :'idnum',
                                          	  	width :180,
                                          	 	sortable :true
                                          	},	{
                                          		header : '性别',
                                          	  	dataIndex :'sex',
                                          	  	width :60,
                                          	 	sortable :true
                                          	},{
                                          		id : 'collegename',
                                          		header : '学院',
                                          	  	dataIndex :'collegename',
                                          	  	width :180,
                                          	 	sortable :true
                                          	},{
                                          		id : 'classname',
                                          		header : '班级',
                                          	  	dataIndex :'classname',
                                          	  	width :120,
                                          	 	sortable :true
                                          	}, {
                                          		id : 'professionname',
                                          		header : '专业',
                                          	  	dataIndex :'professionname',
                                          	  	width :120,
                                          	 	sortable :true
                                          	}
                                          	], 
                                          	tbar:[ {
                                    			text : '校对单设计',
                                    			iconCls : 'edit',
                                    			onClick : function() {
                                    				if(form.getForm().isValid()){
                                    				//var languagename=Ext.get('language').dom.value;                                   				
                                    				var collegename=Ext.get('college').dom.value;
                                    				var classname=Ext.get('class').dom.value;
                                    				
                                    				
                                    				
                                    				//判断用户是否选中记录
                                					if (passstudentgrid.getSelectionModel().hasSelection()) {
                                						
                                					    LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印校对单");
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
                                						        td.setAttribute('width', '50');
                                						    var text=document.createTextNode("学号");
                                						        td.appendChild(text);
                                						        tr.appendChild(td);
                                						    var td=document.createElement("TD");
                                						        td.setAttribute('width', '50');
                                						    var text=document.createTextNode("姓名");
                                						        td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                                                td.setAttribute('width', '70');
                                    					    var text=document.createTextNode("身份证");
                                    						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                                                td.setAttribute('width', '40');
                                    					    var text=document.createTextNode("性别");
                                    						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                      //      var td=document.createElement("TD");
                                        			   //     var text=document.createTextNode("年级");
                                        				//		td.appendChild(text);
                                                       //         tr.appendChild(td);           
                                                       //     var td=document.createElement("TD");
                                            			//    var text=document.createTextNode("学制");
                                            			//        td.appendChild(text);
                                                       //         tr.appendChild(td);      
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '70');
                                                    			var text=document.createTextNode("语种");
                                                    			    td.appendChild(text);
                                                                    tr.appendChild(td); 
                                                         /*   var td=document.createElement("TD");
                                                			var text=document.createTextNode("专业");
                                                			    td.appendChild(text);
                                                                tr.appendChild(td); */
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '80');
                                                    		var text=document.createTextNode("签名");
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
                                							td.setAttribute('width', '65');
                                						    var text=document.createTextNode(printobject.get("studentnum"));
                                						        td.appendChild(text);
                                						        tr.appendChild(td);
                                						    var td=document.createElement("TD");
                                						    td.setAttribute('width', '65');
                                						    var text=document.createTextNode(printobject.get("name"));
                                						        td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '80');
                                    						var text=document.createTextNode(printobject.get("idnum"));
                                    						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '40');
                                    					    var text=document.createTextNode(printobject.get("sex"));
                                    						    td.appendChild(text);
                                                                tr.appendChild(td);
                                                      //      var td=document.createElement("TD");
                                        			   //     var text=document.createTextNode(printobject.get("grade"));
                                        				//		td.appendChild(text);
                                                        //        tr.appendChild(td);           
                                                       //     var td=document.createElement("TD");
                                            			//    var text=document.createTextNode(printobject.get("lengthofyear"));
                                            			//        td.appendChild(text);
                                                       //         tr.appendChild(td);   
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '200');
                                                    	        var text=document.createTextNode(printobject.get("languagename"));
                                                    			    td.appendChild(text);
                                                                    tr.appendChild(td);  
                                                         /*   var td=document.createElement("TD");
                                                	        var text=document.createTextNode(printobject.get("professionname"));
                                                			    td.appendChild(text);
                                                                tr.appendChild(td);     */
                                                            var td=document.createElement("TD");
                                                            td.setAttribute('width', '80');
                                                    	    var text=document.createTextNode(" ");
                                                    		    td.appendChild(text);
                                                                tr.appendChild(td); 
                                                                
                                                                tbody.appendChild(tr);                                       						                                       						    
                                						   }
                                						_table.appendChild(tbody); 
                                						var _div=document.getElementById("hiddentable");
                                						_div.appendChild(_table);
                                						
                                						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
                                						//alert(printTable);
                                						if(classname=="---请选择---"||classname=="不选")
                                							{
                                							
	                                							var InfoArray_college=new Array("浙江省高校计算机等级考试校对单",
	                                    								"学院:",	                                								
	                                    								collegename,                                   								                             								
	                                    								printTable);
	                                							 CreatePrintPage_college(InfoArray_college);
	                                							 LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
	                                     						LODOP.PRINT_SETUP();
	                                     						reSizePrintParam_college();
                                							
                                							}
                                						else {
                                							
                                							var InfoArray=new Array("浙江省高校计算机等级考试校对单",
                                    								"学院:",                                  					
                                    								collegename,   
                                    								"班级:",                                  							
                                    								classname,                              								
                                    								printTable);
                                    						
                                    					    						    									    
                                    					    CreatePrintPage(InfoArray);
                                    						
                                    						//CreatePrintPage(collegename,classname,printTable);	
                                     						
                                    						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                    						LODOP.PRINT_SETUP();
                                    						reSizePrintParam();
                                							
                                						}
                                						
                                						
                                		    			
                                					   }else{
                                						    Ext.MessageBox.alert('提示', "请选择一条记录!");
                                					     }						
                                    				}else{
                    			  		        		Ext.MessageBox.alert('提示',"学院是必选的查询条件！");
                    			  		        	}
                                    			}
                                    		},'-',{
                                    			text : '打印当前页',
                                    			iconCls : 'print',
                                    			onClick : function() {
                                    				if(form.getForm().isValid()){
                                        				//var languagename=Ext.get('language').dom.value;                                   				
                                        				var collegename=Ext.get('college').dom.value;
                                        				var classname=Ext.get('class').dom.value;
                                        				
                                        				//判断用户是否选中记录
                                    					if (passstudentgrid.getSelectionModel().hasSelection()) {
                                    						
                                    					    LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印校对单");
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
                                    						        td.setAttribute('width', '50');
                                    						    var text=document.createTextNode("学号");
                                    						        td.appendChild(text);
                                    						        tr.appendChild(td);
                                    						    var td=document.createElement("TD");
                                    						        td.setAttribute('width', '50');
                                    						    var text=document.createTextNode("姓名");
                                    						        td.appendChild(text);
                                                                    tr.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                    td.setAttribute('width', '70');
                                        					    var text=document.createTextNode("身份证");
                                        						    td.appendChild(text);
                                                                    tr.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                    td.setAttribute('width', '40');
                                        					    var text=document.createTextNode("性别");
                                        						    td.appendChild(text);
                                                                    tr.appendChild(td);
                                                          //      var td=document.createElement("TD");
                                            			   //     var text=document.createTextNode("年级");
                                            				//		td.appendChild(text);
                                                           //         tr.appendChild(td);           
                                                           //     var td=document.createElement("TD");
                                                			//    var text=document.createTextNode("学制");
                                                			//        td.appendChild(text);
                                                           //         tr.appendChild(td);      
                                                                    var td=document.createElement("TD");
                                                                    td.setAttribute('width', '70');
                                                        			var text=document.createTextNode("语种");
                                                        			    td.appendChild(text);
                                                                        tr.appendChild(td); 
                                                             /*   var td=document.createElement("TD");
                                                    			var text=document.createTextNode("专业");
                                                    			    td.appendChild(text);
                                                                    tr.appendChild(td); */
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '80');
                                                        		var text=document.createTextNode("签名");
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
                                    							td.setAttribute('width', '65');
                                    						    var text=document.createTextNode(printobject.get("studentnum"));
                                    						        td.appendChild(text);
                                    						        tr.appendChild(td);
                                    						    var td=document.createElement("TD");
                                    						    td.setAttribute('width', '65');
                                    						    var text=document.createTextNode(printobject.get("name"));
                                    						        td.appendChild(text);
                                                                    tr.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '80');
                                        						var text=document.createTextNode(printobject.get("idnum"));
                                        						    td.appendChild(text);
                                                                    tr.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '40');
                                        					    var text=document.createTextNode(printobject.get("sex"));
                                        						    td.appendChild(text);
                                                                    tr.appendChild(td);
                                                          //      var td=document.createElement("TD");
                                            			   //     var text=document.createTextNode(printobject.get("grade"));
                                            				//		td.appendChild(text);
                                                            //        tr.appendChild(td);           
                                                           //     var td=document.createElement("TD");
                                                			//    var text=document.createTextNode(printobject.get("lengthofyear"));
                                                			//        td.appendChild(text);
                                                           //         tr.appendChild(td);   
                                                                    var td=document.createElement("TD");
                                                                    td.setAttribute('width', '200');
                                                        	        var text=document.createTextNode(printobject.get("languagename"));
                                                        			    td.appendChild(text);
                                                                        tr.appendChild(td);  
                                                             /*   var td=document.createElement("TD");
                                                    	        var text=document.createTextNode(printobject.get("professionname"));
                                                    			    td.appendChild(text);
                                                                    tr.appendChild(td);     */
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '80');
                                                        	    var text=document.createTextNode(" ");
                                                        		    td.appendChild(text);
                                                                    tr.appendChild(td); 
                                                                    
                                                                    tbody.appendChild(tr);                                       						                                       						    
                                    						   }
                                    						_table.appendChild(tbody); 
                                    						var _div=document.getElementById("hiddentable");
                                    						_div.appendChild(_table);
                                    						
                                    						var printTable=document.getElementById("hiddentable").innerHTML;//得到这个table的html代码
                                    						//alert(printTable);
                                    						
                                    						if(classname=="---请选择---"||classname=="不选")
                                							{
                                							
	                                							var InfoArray_college=new Array("浙江省高校计算机等级考试校对单",
	                                    								"学院:",	                                								
	                                    								collegename,                                   								                             								
	                                    								printTable);
	                                							 CreatePrintPage_college(InfoArray_college);
	                                							 LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
	                                							 LODOP.PREVIEW();
	                                     						
                                							
                                							}
                                						else {
                                							
                                							var InfoArray=new Array("浙江省高校计算机等级考试校对单",
                                    								"学院:",                                  					
                                    								collegename,   
                                    								"班级:",                                  							
                                    								classname,                              								
                                    								printTable);
                                    						
                                    					    						    									    
                                    					    CreatePrintPage(InfoArray);
                                    						
                                    						//CreatePrintPage(collegename,classname,printTable);	
                                     						
                                    						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                    						LODOP.PREVIEW();
                                    						
                                						}
                                						
                                    						
                                    						
                                    						// CreatePrintPage(collegename,classname,printTable);	
                                     						
                                    					//	LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                    					//	LODOP.PREVIEW();
                                    						
                                    		    			
                                    					}else{
                                    						Ext.MessageBox.alert('提示', "请选择一条记录!");
                                    					}	
                                    				}else{
                    			  		        		Ext.MessageBox.alert('提示',"学院是必选的查询条件！");
                    			  		        	}
                                    			}
                                    		},'-',{
                                    			text : '打印全部',
                                    			iconCls : 'print',
                                    			onClick : function() {
                                    				if(form.getForm().isValid()){
                                        				//var languagename=Ext.get('language').dom.value;                                   				
                                        				var collegename=Ext.get('college').dom.value;
                                        				var classname=Ext.get('class').dom.value;
                                        				
                                    				var params = {};
                                    				   params.fliter = passstudent_currentSearchFilter;
                                    				   params.start = 0;
                                    				   params.limit = 10000;
                                    				   hiddenstore.load({params:params,callback: function(){
                                    					   
                                    					 //判断数据库中是否有符合条件的数据
                                    					   if (hiddenstore.getCount()>0) {
                                       						

                                       					    LODOP.PRINT_INITA("0mm","0mm","210mm","297mm","打印校对单");
                                       						//LODOP.SET_PRINT_PAGESIZE(1,"210mm","297mm","");
                                       						////var selections = passstudentgrid.getSelectionModel().getSelections();
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
                                       						        td.setAttribute('width', '50');
                                       						    var text=document.createTextNode("学号");
                                       						        td.appendChild(text);
                                       						     thead.appendChild(td);
                                       						    var td=document.createElement("TD");
                                       						        td.setAttribute('width', '50');
                                       						    var text=document.createTextNode("姓名");
                                       						        td.appendChild(text);
                                       						        thead.appendChild(td);
                                       						    var td=document.createElement("TD");
                                       						 td.setAttribute('width', '70');
                                     						    var text=document.createTextNode("身份证");
                                     						        td.appendChild(text);
                                     						        thead.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '40');
                                           					    var text=document.createTextNode("性别");
                                           						    td.appendChild(text);
                                           						 thead.appendChild(td);
                                                           //        var td=document.createElement("TD");
                                               			   //     var text=document.createTextNode("年级");
                                               				//		td.appendChild(text);
                                               				//		thead.appendChild(td);           
                                                           //        var td=document.createElement("TD");
                                                   			//    var text=document.createTextNode("学制");
                                                   			//        td.appendChild(text);
                                                   			//       thead.appendChild(td);     
                                           						var td=document.createElement("TD");
                                           						td.setAttribute('width', '70');
                                                       			var text=document.createTextNode("语种");
                                                       			    td.appendChild(text);
                                                       			 thead.appendChild(td); 
                                                            /*    var td=document.createElement("TD");
                                                       			var text=document.createTextNode("专业");
                                                       			    td.appendChild(text);
                                                       			 thead.appendChild(td); */
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
                                       							    tr.setAttribute('height', '10');
                                       							    tr.setAttribute('align', 'center');
                                       							var td=document.createElement("TD");
                                       							td.setAttribute('width', '65');
                                       						    var text=document.createTextNode(printobject.get("studentnum"));
                                       						        td.appendChild(text);
                                       						        tr.appendChild(td);
                                       						    var td=document.createElement("TD");
                                       						        td.setAttribute('width', '65');
                                       						    var text=document.createTextNode(printobject.get("name"));
                                       						        td.appendChild(text);
                                                                    tr.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '80');
                                              				    var text=document.createTextNode(printobject.get("idnum"));
                                              						td.appendChild(text);
                                                                    tr.appendChild(td);
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '40');
                                           					    var text=document.createTextNode(printobject.get("sex"));
                                           						    td.appendChild(text);
                                                                       tr.appendChild(td);
                                                          //         var td=document.createElement("TD");
                                               			//        var text=document.createTextNode(printobject.get("grade"));
                                               			//			td.appendChild(text);
                                                        //               tr.appendChild(td);           
                                                       //            var td=document.createElement("TD");
                                                   		//	    var text=document.createTextNode(printobject.get("lengthofyear"));
                                                   		//	        td.appendChild(text);
                                                        //               tr.appendChild(td);       
                                                                 var td=document.createElement("TD");
                                                                 td.setAttribute('width', '200');
                                                              	 var text=document.createTextNode(printobject.get("languagename"));
                                                              		 td.appendChild(text);
                                                                     tr.appendChild(td);  
                                                            /*    var td=document.createElement("TD");
                                                       	        var text=document.createTextNode(printobject.get("professionname"));
                                                       			    td.appendChild(text);
                                                                       tr.appendChild(td);     */
                                                                var td=document.createElement("TD");
                                                                td.setAttribute('width', '80');
                                                                var text=document.createTextNode(" ");
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
                                       						if(classname=="---请选择---"||classname=="不选")
                                							{
                                							
	                                							var InfoArray_college=new Array("浙江省高校计算机等级考试校对单",
	                                    								"学院:",	                                								
	                                    								collegename,                                   								                             								
	                                    								printTable);
	                                							 CreatePrintPage_college(InfoArray_college);
	                                							 LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
	                                							 LODOP.PREVIEW();
	                                     						
                                							
                                							}
                                						else {
                                							
                                							var InfoArray=new Array("浙江省高校计算机等级考试校对单",
                                    								"学院:",                                  					
                                    								collegename,   
                                    								"班级:",                                  							
                                    								classname,                              								
                                    								printTable);
                                    						
                                    					    						    									    
                                    					    CreatePrintPage(InfoArray);
                                    						
                                    						//CreatePrintPage(collegename,classname,printTable);	
                                     						
                                    						LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                    						LODOP.PREVIEW();
                                    						
                                						}
                                       						
                                       						//CreatePrintPage(collegename,classname,printTable);
                                        						
                                       						//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
                                       					//	LODOP.PREVIEW();
                                       						
                                       		    			
                                       					}else{
                                       						Ext.MessageBox.alert('提示', "没有相应记录!");
                                       					}	
                                    					   
                                    					   
                                    				   }});
                                    				}else{
                    			  		        		Ext.MessageBox.alert('提示',"学院是必选的查询条件！");
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
                                              	pageSize:30,//设置为每页30条记录
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
                                          	            this.store.load({params:params})
                                          	        }
                                          	    }
                                            })
                                          });
                                          
                                          
                                          
                                         
                                          
                                         
                                          

                                          function passStudentGridInit(){	
                                        	/*
                                          	//得到专业combobox的列表
                                          	   StudentController.getProfessionList(function(data){
                                          			if(data){
                                          				comboProfessionStore.loadData(data);
                                          			}
                                          	  	});*/
                                        	  /*
                                          	   //得到语种combobox的列表
                                          	  	StudentController.getLanguageList(function(data){
                                          			if(data){
                                          				comboLanguageStore.loadData(data);
                                          			}
                                          	  	});*/
                                          	  	
                                          	  //得到学院combobox的列表
                                 			   StudentController.getCollegeList(function(data){
                                 					if(data){
                                 						comboCollegeStore.loadData(data);
                                 					}
                                 			  	});
                                 			                                 		
                                          	  	
                                          	passstudentstore.load({
                                          		params : {
                                          			filter : passstudent_currentSearchFilter,
                                          			start : 0,
                                          			limit : 30
                                          		}
                                          	});
                                          }

                                          
                                          function printProofBillPageInit() {
                                          	new Ext.Viewport( {
                                          		layout :'border',
                                          		hideMode: Ext.isIE ? 'offsets' : 'display',
                                          		items : [form,passstudentgrid],
                                          		renderTo :Ext.getBody()
                                          	});
                                          	passStudentGridInit();
                                          }
			                                         