//var comboStore = new Ext.data.ArrayStore({
//			fields:[
//			        'languagenum',
//			        'name'
//			        ]
//		});

var comboLanguageStore = new Ext.data.JsonStore({
	fields:[
	        'languagenum',
	        'languagename'
	        ]
});
		
		   var form=new Ext.form.FormPanel({
			   title:"选择统计条件",
			   region:"north",
			   frame:true,
			   width:300,
			   height:60,
			   labelAlign:'right',
			   items:[{
				 layout:'column',
				 items:[{
					  columnWidth:.3,	
					  layout:'form',	
					  labelWidth:40,
					  items:[{
			        	     xtype:'combo',
			        	     fieldLabel:'语种',
			        	     hiddenName:'language',
			        	     triggerAction:'all',
			        	     store:comboLanguageStore,			        	    	 
			     //   	     displayField:'name',
			        	     displayField:'languagename',
			        	     valueField:'languagenum',
			        	     mode:'local',
			        	     anchor:'97.5%',
			        	     editable:false,
			        	     allowBlank: false,
			        	     emptyText:'---请选择---'		        	     
			        	    }] 
			          },{
			        	  columnWidth:.2,	
			        	  layout:'form',
			        	  labelWidth:50,
						  items:[{
							  xtype:'numberfield',
							  id:'passLine',
							  fieldLabel:'及格线',
			        	      name:'passLine',
			        	      allowNegative:false,
			        	      anchor:'97.5%',
			        	      value:'60',
			        	      allowBlank: false
						  }]
					      
				          },{
				        	  columnWidth:.2,	
				        	  layout:'form',
				        	  labelWidth:50,
							  items:[{
								  xtype:'numberfield',
								  id:'excellentLine',
								  fieldLabel:'优秀线',
				        	      name:'excellentLine',
				        	      allowNegative:false,
				        	      anchor:'97.5%',
				        	      value:'85',
				        	      allowBlank: false
							  }]			      
					          },{
					        	  columnWidth:.1,	
					        	  layout:'form',
					        	  items:[{
									  xtype:'button',
									  text:'统计',
									  anchor:'97.5%',
									   handler : function() {
								        	if(form.getForm().isValid()){
								         	var summaryCondition = form.getForm().getValues();
								         	StudentController.summaryScoreByGrade(summaryCondition,function(data){ 
								     	    	if(data){
								     	    		scoreSummaryStore.loadData(data);
								     	    	}else
								     	    		Ext.MessageBox.alert('提示',"统计失败！");
								     	    	
								     	    });            	
								        	
								        	}else{
								        		Ext.MessageBox.alert('提示',"输入统计条件有误！");
								        	}
								         }
								   }]
							     }
				           ]			 
				   }]		   		   
		   });
		   
		   
		   var scoreSummaryFields= Ext.data.Record.create([{
				name : 'grade'
			}, {
				name : 'UnPassStudentCount'
			}, {
				name : 'PassStudentCount'
			},{
				name : 'PassStudentPercent'
			}, {
				name : 'ExcellentStudentCount'
			},{
				name : 'ExcellentStudentPercent'
			},{
				name : 'StudentCount'
			}]);
		   
		   
		   var scoreSummaryStore = new Ext.data.JsonStore({
				fields : scoreSummaryFields
				});
		   
		   
		   var scoreSummarygrid = new Ext.grid.GridPanel({
				region : 'center',
				id : 'scoreSummarygrid',
				store : scoreSummaryStore,
				title : '年级成绩统计表',
				loadMask : true,
				stripeRows : true,
				autoScroll : true,
//				autoExpandColumn : 'grade',
				viewConfig : {
					sortDescText : '降序',
					sortAscText : '升序',
					columnsText : '显示列',
					forceFit : false
				},
				columns : [new Ext.grid.RowNumberer(), {
							id : 'grade',
							header : '年级',
							dataIndex : 'grade',
							width : 270,
							sortable : true
						},{
							header : '不及格人数',
							dataIndex : 'UnPassStudentCount',
							width : 120,
							sortable : true
						} ,{
							header : '及格人数',
							dataIndex : 'PassStudentCount',
							width : 120,
							sortable : true
						},{
							header : '及格率',
							dataIndex : 'PassStudentPercent',
							width : 120,
							sortable : true
						}, {
							header : '优秀人数',
							dataIndex : 'ExcellentStudentCount',
							width : 120,
							sortable : true
						},{
							header : '优秀率',
							dataIndex : 'ExcellentStudentPercent',
							width : 120,
							sortable : true
						},{
							header : '总人数',
							dataIndex : 'StudentCount',
							width : 120,
							sortable : true
						}],
						tbar : [{
			     	        text: '导出年级成绩统计表',
			   	        iconCls : 'upload-icon',
			   	        scope : this,
			   				handler : function() {
			   					exportNJCJTJBXls(); 
			   				}			
			   			}] 
			});

		   function WindowInit(){
			  	StudentController.getLanguageList(function(data){
					if(data){
						comboLanguageStore.loadData(data);
					}
			  	});
			  	
			  	
			 
		   }
		   
		   
	function ScoreSummaryInfoPageInit(){
			  Ext.QuickTips.init();
			 //初始化界面
				 new Ext.Viewport({
						layout:'border',
						hideMode : Ext.isIE ? 'offsets' : 'display',
						items:[form,scoreSummarygrid],
						renderTo : Ext.getBody()
					});
				 
				 WindowInit();
		   }
	
	function exportNJCJTJBXls(){
		if(form.getForm().isValid()){
			var con_language=Ext.get('language').dom.value;//注意：使用Ext.get('hiddenValue').dom.value获取的才是combobox中需要提交的value的值
			var con_passLine=Ext.get('passLine').dom.value;
			var con_excellentLine= Ext.get('excellentLine').dom.value;
			
			
			StudentController.SetSessionParameter(con_language,con_passLine,con_excellentLine,function(data){
				if(data){
					var f = document.getElementById('exportNJCJTJBExcel');
					f.action = '../exportNJCJTJBExcel.do';
					f.submit({
						failure : function(form,action){
							var error = Ext.util.JSON.decode(action.response.responseText).error;
							Ext.MessageBox.alert('导出失败',"原因：" + error);
						}
						
					});
				}
		  	});     			
	}else{
		Ext.MessageBox.alert('提示',"请您先统计完成绩！");
	}
	}