
var examfraud = Ext.data.Record.create([{
		name :'examnum'
	}, {
		name :'name'
	}, {
		name :'theoryfraud',convert:function(data){if(data==1){return "是";}else{return "否";}}
	}, {
		name :'operatefraud',convert:function(data){if(data==1){return "是";}else{return "否";}}
	}
]);

var examfraudstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, examfraud),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.fraudStudentPaginationShow
	  })
});

var examfraudgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'examfraudgrid',
	store : examfraudstore,
	title : '作弊库',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'examnum',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer(),
	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :300,
	 	sortable :true
	},	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :200,
	 	sortable :true
	},	{
		id : 'theoryfraud',
		header : '理论作弊',
	  	dataIndex :'theoryfraud',
	  	width :150,
	 	sortable :true
	},	{
		id : 'operatefraud',
		header : '上机作弊',
	  	dataIndex :'operatefraud',
	  	width :150,
	 	sortable :true
	}
	],
     tbar : [{
         text : '批量导入作弊',
         tooltip : '批量导入作弊',
         iconCls : 'import',
	     scope: this,
	     handler:function(){
	    	 studentFraudImprot().show();
	     }
     	}, '-',{
                   text : '录入作弊',
                   tooltip : '新增',
                   iconCls : 'add',
                   onClick : function() {
					   var newexamfraud = new examfraud({
						    examnum : '',
						    name : '',
						    theoryfraud : '',
						    operatefraud : ''
					   });
                       examFraudWindowInit('录入作弊信息').show();
                       examfraudform.getForm().reset();
                       studentsearchform.getForm().reset();
                       examfraudform.getForm().loadRecord(newexamfraud);
                   }
              }, '-', {
                   text : '修改',
                   tooltip : '修改作弊信息',
                   iconCls : 'edit',
                   onClick : function() {
                   	examfraudgrid.fireEvent('rowdblclick', examfraudgrid);
                   }
              }, '-', {
                   text : '删除',
                   tooltip : '删除选中的菜单项',
                   iconCls : 'remove',
                   onClick : function() {
                      if (examfraudgrid.getSelectionModel().hasSelection()) {
                         Ext.MessageBox.confirm('提示', '你确定要删除选中的作弊考生么?',
                                 function(button) {
                                    if (button == 'yes') {
                                       var list = examfraudgrid.getSelectionModel().getSelections();
                                       var rList = [];
                                       for (var i = 0; i <list.length; i++) {
                                          rList[i] = list[i].data["examnum"];
                                       }
                                       
                                       StudentController.deleteFraudStudent(
                                               rList, function(data) {
                                                  if (data) {
                                                     Ext.MessageBox.alert('提示','删除作弊考生成功!');
                                                     fraudStudentGridInit();
                                                  } else {
                                                     Ext.MessageBox.alert('提示',"删除作弊考生失败!");
                                                  }
                                               });
                                 		}
                                 });
                      } else {
                         Ext.MessageBox.alert('提示', "请至少选择一条记录!");
                      }
                   }
              }, '-', {
      	        text: '导出作弊库',
    	        iconCls : 'upload-icon',
    	        scope : this,
    				handler : function() {
    					exportFraudStudentsXls(); 
    				}
    			}
              ] ,
      	    bbar : new Ext.PagingToolbar({
      	    	pageSize:20,
      	        store : examfraudstore,
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
      		        params.start = start;
      		        params.limit = this.pageSize;
      		        if (this.fireEvent("beforechange", this, params) !== false) {
      		            this.store.load({params:params});
      		        }
      		    }
      	  }),
        listeners:{
    	rowdblclick : function(grid){
    		if(grid.getSelectionModel().hasSelection()){
    			examFraudWindowInit("编辑作弊信息").show();
    			studentsearchform.getForm().reset();
    			examfraudform.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
 });
 
function exportFraudStudentsXls(){
	var f = document.getElementById('exportFraudStudentsBySchoolExcel');
	f.action = '../exportFraudStudentsBySchoolExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
	});
}

var studentsearchfields = [
                            {
	columnWidth : 0.9,
	layout : 'form',
	labelWidth : 70,
	items : [ {
		id : 'examnumforsearch',
		xtype : 'textfield',
		fieldLabel : '准考证号',
		name : 'examnumforsearch',
		anchor : '98%'
	} ]
}, {
	columnWidth : 0.1,
	layout : 'form',
	items : [ {
		xtype : 'button',
		anchor : '98%',
		text : '查询',
		onClick : function(){
			var examnum = Ext.getCmp("examnumforsearch").getValue();
			StudentController.findFraudStudentByExamNum(examnum,function(data){
				if(data){
					examfraudform.getForm().setValues(data);
				}else{
					Ext.MessageBox.alert('提示',"未找到对应考生！");
				}
			});
		}
	} ]
}         
];

var studentsearchform = new Ext.form.FormPanel({
    labelWidth: 60,
    height : 40,
    labelAlign : 'left',
    layout : 'column',
    region : 'north',
    frame:true, 
    border : false,
    bodyStyle:'padding:5',
    items : [studentsearchfields]
});

var  examfraudfields = [
                      {columnWidth:1,layout:'form',items:[
                  		{xtype:'textfield',fieldLabel: '准考证号',name: 'examnum',anchor:'99%',allowBlank: false}
                  	]},
                  	{columnWidth:1,layout:'form',items:[
                  		{xtype:'textfield',fieldLabel: '姓名',name: 'name',anchor:'99%',allowBlank: false}
                  	]},
                  	{columnWidth:.5,layout:'form',items:[
                  	    {xtype:'combo',fieldLabel: '理论作弊',name: 'theoryfraud',store : ['是','否'],triggerAction : 'all',editable : false,anchor:'98%',allowBlank: false}
                  	 ]},
                  	{columnWidth:.5,layout:'form',items:[
                  		{fieldLabel:'上机作弊',name:'operatefraud',xtype: 'combo', store : ['是','否'],triggerAction : 'all',editable:false,allowBlank : false,anchor:'98%'}
                  	]}
];

 var examfraudform = new Ext.form.FormPanel({
    labelWidth: 70,
    labelAlign : 'left',
    layout : 'column',
    region : 'center',
    frame:true,
    border : false,
    bodyStyle:'padding:5',
    items : [examfraudfields]
});

var examfraudwin;

function examFraudWindowInit(title){
   
    if (!examfraudwin) {
   	    examfraudwin = new Ext.Window({
   	        width: 480,
   	        height:250,
   	        closeAction : 'hide',
   	        layout: 'border',
			border:false,
			modal: true,
			shadow: true,
			hideMode: Ext.isIE ? 'offsets' : 'display',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: [examfraudform,studentsearchform],
   	        buttons: [{
                   text : '保存',
                   handler : function() {
                   	if(examfraudform.getForm().isValid()){
                    	var fraudstudent = examfraudform.getForm().getValues();
                    	StudentController.saveFraudStudent(fraudstudent,function(data){
                    		var jsonData = Ext.util.JSON.decode(data);
                    		if(jsonData.success == true){
                	    		Ext.MessageBox.alert('提示',jsonData.info);
                	    		examfraudwin.hide();
                	    	}else
                	    		Ext.MessageBox.alert('提示',jsonData.info);
                	    	fraudStudentGridInit();
                	    });       
                   	
                   	}else{
                   		Ext.MessageBox.alert('提示',"输入信息有误！");
                   	}
                   }
                          		
            }, {
                   text : '清空',
                   handler : function() {
                	   examfraudform.getForm().reset();
                   }
            }, {
                   text : '关闭',
                   handler : function() {
                	   examfraudwin.hide();
                   }
            }]
  	    });
   	
    }
    examfraudwin.setTitle(title);
    return examfraudwin;
	
}

var studentFraudImportForm = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../importFraud.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,
	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'zbkfile',
		inputType : 'file'//文件类型 
	} ]
});

var studentFraudImportWin;
function studentFraudImprot(){
	if(!studentFraudImportWin){
		studentFraudImportWin = new Ext.Window({
   	        title: '文件上传',
   	        width: 480,
   	        height:360,
   	        minWidth: 400,
   	        minHeight: 360,
   	        closeAction : 'hide',
   	        layout: 'border',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: [studentFraudImportForm],
   	        buttons : [ {
   			text : '上传',
   			handler : function() {
				var temp=studentFraudImportForm.getForm().getFieldValues().zbkfile.toLowerCase();
   				var ZIPreg= /\.xls$/;
				if(temp=="" || !(ZIPreg.exec(temp) == ".xls")){ 
					Ext.MessageBox.alert('提示',"请选择xls后缀文件！");
					return;
				}
   				Ext.MessageBox.wait('正在执行，请等待...','提示');
   				studentFraudImportForm.getForm().submit({
   					success : function(form, action) {
   							var students = Ext.util.JSON.decode(action.response.responseText).excelArray;
   							StudentController.checkStudentsExists(students,function(checkInfo){
   								if(checkInfo=="true"){
   		   							StudentController.importFraud(students,function(data){
   		   	   							if(data){
   		   	   								Ext.Msg.alert('提示', '文件上报成功！',function(){
   		   	   									fraudStudentGridInit();
   		   	   									studentFraudImportWin.hide();
   		   	   								});
   		   	   								
   		   	   							}	
   		   	   						});
   								}else
   									Ext.Msg.alert('提示',checkInfo);
   							});

   					},
   					failure : function() {
   						Ext.Msg.alert('错误', '文件上传失败',function(){
   							var temp = window.top.tabPanel.getActiveTab( );
   							window.top.tabPanel.remove(temp);
   						});
   						
   					}
   				});
   			}
   		} ]
		});
	}
	return studentFraudImportWin;
}

function fraudStudentGridInit(){
	examfraudstore.load({
		params : {
			start : 0,
			limit : 20
		}
	});
}

function ExamFraudPageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [examfraudgrid],
		renderTo :Ext.getBody()
	});
	fraudStudentGridInit();
}
