var student_currentSearchFilter = "";


var student = new Ext.data.Record.create([
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
		name:'exStudentstatus'
	},{
		name:'exCampus'
	},{
		name:'name'
	},{
		name:'password'
	},{
		name:'sex',convert:function(data){if(data=="M"){return "男";}else return "女";}
	},{
		name:'studentnum'
	},{
		name:'idnum'
	},{
		name:'ethno'
	},{
		name:'examnum'
	},{
		name:'exambatch'
	},{
		name:'grade'
	},{
		name:'classnum'
	},{
		name:'lengthofyear'
	},{
		name:'studentcategory',convert:function(data){if(data=="1"){return "普通本科";}else if(data=="2"){return "普通专科";}else return "否";}
	},{
		name:'paied',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'score'
	},{
		name:'theoryabsent',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'operateabsent',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'theoryfraud',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'operatefraud',convert:function(data){if(data=="1"){return "是";}else return "否";} 
	}
]);


var basicDatastore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, student),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.paginationShowNoOperateAbsence
	  })
});


var operateAbsenceDatastore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, student),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.paginationShowOperateAbsence
	  })
});


/*var chooseLanguage=new Ext.form.FormPanel({  
    id:'chooseLanguage',  
    autoWidth:true,
   // title:"chooseLanguage",
    heigth:180,  
    frame:true,  
    region:'north',
    layout:'column',
   // html:'这里放置页头内容' ,
    items:[{  
    	xtype:"textfield",  
        fieldLabel:"姓名",  
        allowBlank:false,  
        width:120},
        {
			xtype:'button',
//			y:70,
			font:'25px',
			text:'>>',
			color:'red',
			scale:'large',

		//	height:180,
			width:80

		}],
        buttonAlign:'center',  
        buttons:[{  
            text:'提交'  
        },{  
            text:'重置'  
        }]  
}); */


var chooseLanguage=new Ext.form.FormPanel({
	   title:"选择统计条件",
	   region:"north",
	   id:"chooseLanguage",
	   name:"chooseLanguage",
	   frame:true,
	   width:300,
	   height:75,			  
	   labelAlign:'right',
	   items:[{
		 layout:'column',
		 items:[{
			  columnWidth:.3,	
			  layout:'form',	
			  labelWidth:100,
			  items:[{
	        	     xtype:'textfield',
	        	    id:"search",
	        	     fieldLabel:'准考证后六位：',
	        	     name:'and$student-examnum$=$value',
	        	 //    hiddenName:'language',
	        	 //    triggerAction:'all',
	        	//     store:comboLanguageStore,
	        	 //    displayField:'languagename',
	        	     valueField:'search',
	        	     heigth:'50',
	        	     mode:'local',
	        	     anchor:'97.5%',
	        	     editable:false,
	        	//     allowBlank: false,
	        	 //    emptyText:'---请选择---'		        	     
	        	    }] 
	          }/*,{
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
			          }*/,{
			        	  columnWidth:.1,	
			        	  layout:'form',
			        	  items:[{
							  xtype:'button',
							  text:'查询',
							  anchor:'97.5%',
							  width:'40',
							  scale: 'medium',
							   handler : function() {
								   student_search();
								   student_currentSearchFilter = "";
						         }
						   }]
					     }
					     ,{
				        	  columnWidth:.1,	
				        	  layout:'form',
				        	  items:[{
								  xtype:'button',
								  text:'清空2',
								  anchor:'97.5%',
								  width:40,
								  scale: 'medium',
							//	  heigth:'60',
							//	  scale:'middle',
								   handler : function() {
							        	if(form.getForm().isValid()){
							         	var summaryCondition = form.getForm().getValues();
							         	StudentController.summaryScore(summaryCondition,function(data){ 
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



//操作面板
var operationPanel=new Ext.form.FormPanel({  
    id:'operationPanel',  
   // autoWidth:true,
 //   title:"operationPanel",
  //  heigth:180,  
    width:80,  
    frame:true,  
    margins : '200 5 200 5',
    region:'center', 
    bodyStyle:'margin:10 10 10 10',
    split:true,
  //  html:'这里放置页头内容' ,
    buttonAlign : 'center',
    layout:'column',
  //  columns: 1,
    items:[{
          // 	xtype:'buttongroup',
		columns:1,
	//	padding:20,
		 buttonAlign : 'center',
		 bodyStyle:'margin:10 10 10 10',
		items:[{
			xtype:'button',
//			y:70,
			font:'25px',
			text:'>>',
			color:'red',
			scale:'large',

		//	height:180,
			width:80

		},{
			xtype:'button',
			text:'导11入未报名学生数据',
		//	font-size:12px,
		//	padding:10,
			scale:'large',
			margin:'10 10 10 10'

		//	height:180,
		//	width:215,

		}]
    }]
}); 



var basicData = new Ext.grid.GridPanel({
	title:"所有上机未缺考考生",
	region:'west',
	id : 'basicData',
	store :basicDatastore,
    loadMask :true,
    margins : '20 5 5 5',
    width : 560,
	stripeRows :true,
	autoScroll:true,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width:28}),
	{
		id : 'name',
		header : '姓名2',
	  	dataIndex :'name',
	  	width :100,
	 	sortable :true
	},{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :180,
	 	sortable :true
	},{
		id : 'operateabsent',
		header : '上机是否缺考',
	  	dataIndex :'operateabsent',
	  	width :180,
	 	sortable :true
	}
	],
		tbar:[{
	        text: '批量导入报名考生3',
	        iconCls : 'import',
	        scope: this,
	        handler:function(){
	        	studentimprot().show();
	        }
		},/*'-', {
	        text: '导出考生库EXCEL',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	exportStudentsExcel();
	        }
	    },'-', {
	        text: '导出考生库DBF',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	exportStudentsDbf();
	        }
	    },*/'-',{
	        text: '打印校对单',
	        iconCls : 'print',
	        scope: this,
	        handler:function(){
	        	 tabAdd("PrintProofBill","print/PrintProofBill.jsp","打印校对单");
	        }
	    }
	   
    ],
	    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : basicDatastore,
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
	        params.filter = student_currentSearchFilter;
	        params.start = start;
	        params.limit = this.pageSize;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  }),
     listeners:{
    	rowdblclick : function(grid){
    		//enableOfStudentbasicform();
    		if(grid.getSelectionModel().hasSelection()){ 
    		//	stunum = grid.getSelectionModel().getSelected().data.studentnum;
    			
    			StudentController.setOperateAbsence(grid.getSelectionModel().getSelected().data.examnum,function(filepath){
    				if(filepath==""){
    			//	Ext.getCmp("stuphoto").getEl().dom.src = '../resources/icons/delete.gif';
    					Ext.MessageBox.alert('提示shiban',filepath);
    				}
    				else {
    					Ext.MessageBox.alert('提示',"成功");
    					basicDataGridInit();
    					basicDatastore.reload();
    					operateAbsenceDatastore.reload();
    					basicDataGridInit();
    				//	operateAbsenceDataGridInit();
    				}
    			});
    		//	college_studentbasicform.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
});



//
//var chooseLanguage=new Ext.form.FormPanel({  
//    id:'chooseLanguage',  
//    autoWidth:true,
//    title:"chooseLanguage",
//    heigth:180,  
//    frame:true,  
//    region:'north',  
//    html:'这里放置页头内容'  
//}); 




var operateAbsenceData = new Ext.grid.GridPanel({
	title:"所有上机缺考考生",
	region:'east',
	id : 'operateAbsenceData',
	store :operateAbsenceDatastore,
    loadMask :true,
    width : 460,
    margins : '5 5 5 5',
	stripeRows :true,
	autoScroll:true,
	padding:20,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width:28}),
	{
		id : 'name',
		header : '姓名1',
	  	dataIndex :'name',
	  	width :100,
	 	sortable :true
	},{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :180,
	 	sortable :true
	},{
		id : 'operateabsent',
		header : '上机是否缺考',
	  	dataIndex :'operateabsent',
	  	width :180,
	 	sortable :true
	}
	],
		tbar:[{
	        text: '导出Excel',
	        iconCls : 'import',
	        scope: this,
	        handler:function(){
	        //	studentimprot().show();
	        	exportStudentsOperateAbsentExcel();
	        }
		},/*'-', {
	        text: '导出考生库EXCEL',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	exportStudentsExcel();
	        }
	    },'-', {
	        text: '导出考生库DBF',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	exportStudentsDbf();
	        }
	    },*/'-' ,{
	        text: '导出DBF',
	        iconCls : 'print',
	        scope: this,
	        handler:function(){
	        	// tabAdd("PrintProofBill","print/PrintProofBill.jsp","打印校对单");
	        }
	    }
	   
    ],
	    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : operateAbsenceDatastore,
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
	        params.filter = student_currentSearchFilter;
	        params.start = start;
	        params.limit = this.pageSize;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  }),
     listeners:{
    	rowdblclick : function(grid){
    	//	enableOfStudentbasicform();
    		if(grid.getSelectionModel().hasSelection()){ 
    			StudentController.setOperateNoAbsence(grid.getSelectionModel().getSelected().data.examnum,function(filepath){
    				if(filepath==""){
    			//	Ext.getCmp("stuphoto").getEl().dom.src = '../resources/icons/delete.gif';
    					Ext.MessageBox.alert('提示shiban',filepath);
    				}
    				else {
    					Ext.MessageBox.alert('提示',"成功");
    					basicDataGridInit();
    					basicDatastore.reload();
    					operateAbsenceDatastore.reload();
    					basicDataGridInit();
    				//	operateAbsenceDataGridInit();
    				}
    			});
    		//	college_studentbasicform.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
});






















/*
function student_search(){
	student_currentSearchFilter = Ext.encode(student_searchform.getForm().getFieldValues());
	basicDatastore.load({
		params : {
					filter : student_currentSearchFilter,
					start : 0,
					limit : 20
		}
	});
}*/



function basicDataGridInit(){
	basicDatastore.load({
		params : {
			filter : student_currentSearchFilter,
			start : 0,
			limit : 20
		}
	});
}


function operateAbsenceDataGridInit(){
	operateAbsenceDatastore.load({
		params : {
			filter : student_currentSearchFilter,
			start : 0,
			limit : 20
		}
	});
}

function student_search(){
	student_currentSearchFilter = Ext.encode(chooseLanguage.getForm().getFieldValues());
	basicDatastore.load({
		params : {
					filter : student_currentSearchFilter,
					start : 0,
					limit : 20
		}
	});
}


function exportStudentsOperateAbsentExcel(){
	var f = document.getElementById('exportStudentsOperateAbsentExcel');
	f.action = '../exportStudentsOperateAbsentExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}





function pageInit() {
//	Ext.QuickTips.init();
	new Ext.Viewport({
		layout :'border',
		padding:20,
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [chooseLanguage,basicData,operationPanel,operateAbsenceData],
		renderTo :Ext.getBody()
	});
	basicDataGridInit();
	operateAbsenceDataGridInit();
}