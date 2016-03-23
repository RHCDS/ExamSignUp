var cleardata="0";
var backupPanel = new Ext.Panel({
    id : 'backupPanel',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: false,
	title : '历史数据备份（第1步）',
    items:[
    {
    	xtype:'button',
    	text:'备份历史数据（第1步/共15步）',
    	pageX: 175,
    	iconCls : 'save',
    	pageY: 110,
    	width:15,
    	height:10,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		backupStudentsExcel();
    	}
    }],
    tbar:['->',{
		text:'下一步',
		iconCls : 'next',
		handler: function(){
			backupPanel.hidden = true;
			backupPanel.onHide();
			clearHistoryPanel.hidden = false;
			clearHistoryPanel.show();
			//switchForm(1,wizardItems);
			systemInitialWindow.doLayout();
		}
	}]
});
function backupStudentsExcel(){
	var f = document.getElementById('backStudentsExcel');
	f.action = '../backupStudentsExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}

function exportStudentsExcel(){
	var f = document.getElementById('exportStudentsExcel');
	f.action = '../exportStudentsExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}

var exportStudentsDBF = new Ext.Panel({
    id : 'exportStudentsDBF',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
	title : '导出考生库（第8步）',
    items:[
    {
    	xtype:'button',
    	text:'导出考生库（第8步/共15步）',
    	pageX: 175,
    	iconCls : 'save',
    	pageY: 110,
    	width:15,
    	height:10,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		exportStudentsExcel();
    	}
    }],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		exportStudentsDBF.hidden = true;
    		exportStudentsDBF.onHide();
    		editExamNumPanel.hidden=false;
    		editExamNumPanel.show();
			systemInitialWindow.doLayout();
    	}
    },'->',{
		text:'	下一步',
		iconCls : 'next',
		handler: function(){
			exportStudentsDBF.hidden = true;
			exportStudentsDBF.onHide();
			examroomImportPanel.hidden = false;
			examroomImportPanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});
function exportStudentsDbf() {
	var element = document.getElementById('exportStudentsDbf');
	element.action = '../exportStudentsDbf.do';
	element.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
	});
}
function clearHistoryData(confirm) {
	if(confirm == "yes") {
		cleardata="1";
		Ext.MessageBox.wait('正在执行操作...','请等待');
		ClearHistoryDataController.clearHistoryData(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			if(jsonData.success == true) {
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					
				});
			} else {
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					
				});
			}
		});
	}
}
var clearHistoryPanel = new Ext.Panel({
	id : 'clearHistoryPanel',
    title : '清除历史数据（第2步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'清除历史数据（第2步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'remove',
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		Ext.MessageBox.alert('提示','请确认已经备份历史数据！',function() {
    			Ext.MessageBox.confirm('提示','是否要清空历史数据？',clearHistoryData);
    		});
    	}
    }
 	
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		clearHistoryPanel.hidden=true;
    		clearHistoryPanel.onHide();
    		backupPanel.hidden=false;
    		backupPanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			clearHistoryPanel.hidden=true;
    		clearHistoryPanel.onHide();
    		importLanguagePanel.hidden=false;
    		importLanguagePanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});


var languageImportForm = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../languageXlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,
	
	items : [ {
		xtype : 'textfield',
		fieldLabel : '语种表',
		name : 'languagefile',
		inputType : 'file'//文件类型 
	} ]
});
var importLanguagePanel = new Ext.Panel({  
	title : '导入语种（第3步）',
	id : 'importLanguagePanel',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
	items: languageImportForm,
//	listeners: [function() {
//   		if(clearHistoryData=='0'){
//   			Ext.MessageBox.alert('提示','请确认已经备份历史数据！',function() {
//    			Ext.MessageBox.confirm('提示','是否要清空历史数据？',clearHistoryData);
//    		});
//   		}
//   		}],
    buttons : [ {
		text : '上传',
   		handler : function() {
   		if(cleardata == "0"){
   			Ext.MessageBox.alert('提示','清空历史数据之后，才能导入语种！',function() {
    			Ext.MessageBox.confirm('提示','是否要清空历史数据？',clearHistoryData);
    		});
   		}
//   		Ext.MessageBox.wait('提示5',clearHistoryData);	
   	//	Ext.MessageBox.wait('正在操作...','请等待');
   		else{
   			Ext.MessageBox.wait('正在操作...','请等待');
   			languageImportForm.getForm().submit({
   			success : function(form, action) {
   				var languages = Ext.util.JSON.decode(action.response.responseText).excelArray;
   				LanguageController.importLanguage(languages,function(data){
   					if(data){
   						Ext.MessageBox.hide();
   						Ext.Msg.alert('提示', '文件上传成功！');	
   					} else {
						Ext.MessageBox.hide();
						Ext.Msg.alert('提示', '上传失败！');
					}	
   				});			
   			},
   			failure : function() {
   				Ext.MessageBox.hide();
   				Ext.Msg.alert('错误', '文件上传失败');
   			}
   		});
   		}
   		}
	}],
	tbar:[{
    	text:"上一步",
    	iconCls : 'forward',
    	handler: function() {
    		importLanguagePanel.hidden=true;
    		importLanguagePanel.onHide();
    		clearHistoryPanel.hidden=false;
    		clearHistoryPanel.show();
			systemInitialWindow.doLayout();
    	}
    
    },'->', {	
		text:"下一步",
		iconCls : 'next',
		handler: function(){
			importLanguagePanel.hidden=true;
			importLanguagePanel.onHide();
    		selectStudentImportType.hidden=false;
    		selectStudentImportType.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var turnOnSignUpPanel = new Ext.Panel({
	id : 'turnOnSignUpPanel',
    title : '开启学生报名',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'开启学生报名',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		InstitutionController.turnOnSignUp(function(data) {
    			var jsonData = Ext.util.JSON.decode(data);
    			Ext.MessageBox.alert('提示',jsonData.error.info);
    		});
    	}
    }
 	
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		turnOnSignUpPanel.hidden=true;
    		turnOnSignUpPanel.onHide();
    		unSignUpStudentImportPanel.hidden=false;
    		unSignUpStudentImportPanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  完成",
		iconCls : 'ok',
		handler: function(){
			turnOnSignUpPanel.hidden=true;
			turnOnSignUpPanel.onHide();
			closeTab();
		}
	}]
});

var studentImportForm = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../xlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 80,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '考生表',
		name : 'file',
		inputType : 'file'//文件类型 
	}]
});
var unSignUpStudentImportForm = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../xlsUnSignToJson.do',//fileUploadServlet  
	width : 300,
	height : 80,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '考生表',
		name : 'file',
		inputType : 'file'//文件类型 
	}]
});
var selectStudentImportType = new Ext.Panel({
	title:'选择导入学生模式（第4步）',
	id:'selectStudentImportType',
	labelWidth:80,
	frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
	{
		xtype:'buttongroup',
		columns:2,
		items:[{
			text:'导入已完成报名学生数据',
			scale:'large',
			iconCls:'import',
			height:180,
			width:215,
			cls: 'x-btn-as-arrow',
			onClick:function(){
				selectStudentImportType.hidden=true;
	    		selectStudentImportType.onHide();
	    		studentImportPanel.hidden=false;
	    		studentImportPanel.show();
				systemInitialWindow.doLayout();
			}
		},{
			text:'导入未报名学生数据',
			scale:'large',
			iconCls:'import',
			height:180,
			width:215,
			cls: 'x-btn-as-arrow',
			onClick:function() {
				selectStudentImportType.hidden=true;
	    		selectStudentImportType.onHide();
	    		unSignUpStudentImportPanel.hidden=false;
	    		unSignUpStudentImportPanel.show();
				systemInitialWindow.doLayout();
			}
		}]
	}
    ],
    tbar:[{
    	text:"上一步",
    	iconCls : 'forward',
    	handler: function() {
    		selectStudentImportType.hidden=true;
    		selectStudentImportType.onHide();
    		importLanguagePanel.hidden=false;
    		importLanguagePanel.show();
			systemInitialWindow.doLayout();
    	}
    
    },'->', {	
		text:"下一步",
		iconCls : 'next',
		handler: function(){
			selectStudentImportType.hidden=true;
			selectStudentImportType.onHide();
			printProofBillPanel.hidden=false;
			printProofBillPanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});
var unSignUpStudentImportPanel = new Ext.Panel({  
	title : '导入未报名学生数据（第5步）',
	id : 'unSignUpStudentImportPanel',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
	items: [unSignUpStudentImportForm],
    buttons : [ {
		text : '上传',
		handler : function() {
				Ext.MessageBox.wait('正在检查数据,请等待...','提示');
				unSignUpStudentImportForm.getForm().submit({
   					success : function(form, action) {
   						var jsonData = Ext.util.JSON.decode(action.response.responseText);
           				if(jsonData.success==true){
           					Ext.MessageBox.alert('提示',jsonData.errors.info);
           				}else{
           					Ext.MessageBox.alert('提示',jsonData.errors.info);
           				}				  						
   					},
  					failure : function(form, action) {
  						jsonData = Ext.util.JSON.decode(action.response.responseText);
   						Ext.Msg.alert('文件上传失败', jsonData.errors.info,function(){
   							studentimportwin.hide();
   						});
   					}
   				});
			}
	}],
	tbar:[{
    	text:"上一步",
    	iconCls : 'forward',
    	handler: function() {
    		unSignUpStudentImportPanel.hidden=true;
    		unSignUpStudentImportPanel.onHide();
    		selectStudentImportType.hidden=false;
    		selectStudentImportType.show();
			systemInitialWindow.doLayout();
    	}
    
    },'->', {	
    	text:"  下一步",
		iconCls : 'next',
		handler: function(){
			unSignUpStudentImportPanel.hidden=true;
			unSignUpStudentImportPanel.onHide();
			turnOnSignUpPanel.hidden = false;
			turnOnSignUpPanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var studentImportPanel = new Ext.Panel({  
	title : '导入已完成报名学生数据（第5步）',
	id : 'studentImportPanel',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
	items: [studentImportForm],
    buttons : [ {
		text : '上传',
		handler : function() {
				Ext.MessageBox.wait('正在检查数据,请等待...','提示');
				studentImportForm.getForm().submit({
   					success : function(form, action) {
   						var jsonData = Ext.util.JSON.decode(action.response.responseText);
           				if(jsonData.success==true){
           					cleardata="0";
           					Ext.MessageBox.alert('提示',jsonData.errors.info);
           					studentGridInit();
           				}else{
           					Ext.MessageBox.alert('提示',jsonData.errors.info);
           				}				  						
   					},
  					failure : function(form, action) {
  						jsonData = Ext.util.JSON.decode(action.response.responseText);
   						Ext.Msg.alert('文件上传失败', jsonData.errors.info,function(){
   							studentimportwin.hide();
   						});
   					}
   				});
			}
	}],
	tbar:[{
    	text:"上一步",
    	iconCls : 'forward',
    	handler: function() {
       		studentImportPanel.hidden=true;
       		studentImportPanel.onHide();
       		selectStudentImportType.hidden=false;
       		selectStudentImportType.show();
			systemInitialWindow.doLayout();
    	}
    
    },'->', {	
		text:"下一步",
		iconCls : 'next',
		handler: function(){
			studentImportPanel.hidden=true;
			studentImportPanel.onHide();
			printProofBillPanel.hidden=false;
			printProofBillPanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var editExamNumPanel = new Ext.Panel({
	id : 'editExamNumPanel',
    title : '编排准考证号（第7步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'编排准考证号（第7步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("ExamNumEdit","jsp/ExamNumEdit.jsp","编排准考证"); 
    		
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		editExamNumPanel.hidden=true;
    		editExamNumPanel.onHide();
    		printProofBillPanel.hidden=false;
    		printProofBillPanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			editExamNumPanel.hidden=true;
			editExamNumPanel.onHide();
			exportStudentsDBF.hidden=false;
			exportStudentsDBF.show();
			systemInitialWindow.doLayout();
		}
	}]
});
var printProofBillPanel = new Ext.Panel({
	id : 'printProofBillPanel',
    title : '打印校对单（第6步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'打印校对单（第6步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("PrintProofBill","print/PrintProofBill.jsp","打印校对单");
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		printProofBillPanel.hidden=true;
    		printProofBillPanel.onHide();
    		selectStudentImportType.hidden=false;
    		selectStudentImportType.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			
			printProofBillPanel.hidden=true;
			printProofBillPanel.onHide();
			editExamNumPanel.hidden=false;
			editExamNumPanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var examroomImportForm = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../examroomXlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '考场表',
		name : 'examroomfile',
		inputType : 'file'//文件类型 
	} ]
});

var examroomImportPanel = new Ext.Panel({  
	title : '导入教室列表（第9步）',
	id : 'examroomImportPanel',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
	items: examroomImportForm,
    buttons : [ {
		text : '导入考场',
   		handler : function() {
   		Ext.MessageBox.wait('正在操作...','请等待');
   		examroomImportForm.getForm().submit({
   			success : function(form, action) {
   				var examrooms = Ext.util.JSON.decode(action.response.responseText).excelArray;
   				PhysiceexamroomController.importExamrooms(examrooms,function(data){
   					if(data){
   						Ext.MessageBox.hide();
   						Ext.Msg.alert('提示', '文件上传成功！');	
   					} else {
						Ext.MessageBox.hide();
						Ext.Msg.alert('提示', '上传失败！');
					}	
   				});			
   			},
   			failure : function() {
   				Ext.MessageBox.hide();
   				Ext.Msg.alert('错误', '文件上传失败');
   			}
   		});
   		}
	}],
	tbar:[{
    	text:"上一步",
    	iconCls : 'forward',
    	handler: function() {
    		examroomImportPanel.hidden=true;
    		examroomImportPanel.onHide();
    		exportStudentsDBF.hidden=false;
    		exportStudentsDBF.show();
			systemInitialWindow.doLayout();
    	}
    
    },'->', {	
		text:"下一步",
		iconCls : 'next',
		handler: function(){
			examroomImportPanel.hidden=true;
			examroomImportPanel.onHide();
			supervisorImportPanel.hidden=false;
			supervisorImportPanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var supervisorImportForm = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 80,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../supervisorXlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '监考人员表',
		name : 'supervisorfile',
		inputType : 'file'//文件类型 
	} ]
});

var supervisorImportPanel = new Ext.Panel({  
	title : '导入监考列表（第10步）',
	id : 'supervisorImportPanel',
	labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
	items: supervisorImportForm,
    buttons : [ {
		text : '导入监考',
   		handler : function() {
   		Ext.MessageBox.wait('正在操作...','请等待');
   		supervisorImportForm.getForm().submit({
   			success : function(form, action) {
   				var supervisors = Ext.util.JSON.decode(action.response.responseText).excelArray;
   				SupervisorController.importSupervisors(supervisors,function(data){
   					if(data){
   						Ext.MessageBox.hide();
   						Ext.Msg.alert('提示', '文件上传成功！');	
   					} else {
						Ext.MessageBox.hide();
						Ext.Msg.alert('提示', '上传失败！');
					}	
   				});			
   			},
   			failure : function() {
   				Ext.MessageBox.hide();
   				Ext.Msg.alert('错误', '文件上传失败');
   			}
   		});
   		}
	}],
	tbar:[{
    	text:"上一步",
    	iconCls : 'forward',
    	handler: function() {
    		supervisorImportPanel.hidden=true;
    		supervisorImportPanel.onHide();
    		examroomImportPanel.hidden=false;
    		examroomImportPanel.show();
			systemInitialWindow.doLayout();
    	}
    
    },'->', {	
		text:"下一步",
		iconCls : 'next',
		handler: function(){
			supervisorImportPanel.hidden=true;
			supervisorImportPanel.onHide();
			sectionManagePanel.hidden=false;
			sectionManagePanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var sectionManagePanel = new Ext.Panel({
	id : 'sectionManagePanel',
    title : '考试场次管理（第11步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'考试场次管理（第11步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("sectionManage","jsp/SectionManage.jsp","考试场次管理");
    		
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		sectionManagePanel.hidden=true;
    		sectionManagePanel.onHide();
    		supervisorImportPanel.hidden=false;
    		supervisorImportPanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			sectionManagePanel.hidden=true;
			sectionManagePanel.onHide();
			theoryExamArrangePanel.hidden=false;
			theoryExamArrangePanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var theoryExamArrangePanel = new Ext.Panel({
	id : 'theoryExamArrangePanel',
    title : '理论考场编排（第12步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'理论考场编排（第12步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("TheoryExamArrange","jsp/TheoryExamArrange.jsp","理论考场编排");
    		
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		theoryExamArrangePanel.hidden=true;
    		theoryExamArrangePanel.onHide();
    		sectionManagePanel.hidden=false;
    		sectionManagePanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			theoryExamArrangePanel.hidden=true;
			theoryExamArrangePanel.onHide();
			operateExamArrangePanel.hidden=false;
			operateExamArrangePanel.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var operateExamArrangePanel = new Ext.Panel({
	id : 'operateExamArrangePanel',
    title : '上机考场编排（第13步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'上机考场编排（第13步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("OperateExamArrange","jsp/OperateExamArrange.jsp","上机考场编排");
    		
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		operateExamArrangePanel.hidden=true;
    		operateExamArrangePanel.onHide();
    		theoryExamArrangePanel.hidden=false;
    		theoryExamArrangePanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			operateExamArrangePanel.hidden=true;
			operateExamArrangePanel.onHide();
			theorySupervisorArrange.hidden=false;
			theorySupervisorArrange.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var theorySupervisorArrange = new Ext.Panel({
	id : 'theorySupervisorArrange',
    title : '理论监考老师编排（第14步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'理论监考老师编排（第14步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("TheorySupervisorArrange","jsp/TheorySupervisorArrange.jsp","理论监考老师编排");
    		
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		theorySupervisorArrange.hidden=true;
    		theorySupervisorArrange.onHide();
    		operateExamArrangePanel.hidden=false;
    		operateExamArrangePanel.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  下一步",
		iconCls : 'next',
		handler: function(){
			theorySupervisorArrange.hidden=true;
			theorySupervisorArrange.onHide();
			operateSupervisorArrange.hidden=false;
			operateSupervisorArrange.show();
			systemInitialWindow.doLayout();
		}
	}]
});

var operateSupervisorArrange = new Ext.Panel({
	id : 'operateSupervisorArrange',
    title : '上机监考老师编排（第15步）',
    labelWidth: 80,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'center',
    layout:'fit',
    buttonAlign : 'center',
	width: 450,
    height:250,
    hidden: true,
    items:[
    {
    	xtype:'button',
    	text:'上机监考老师编排（第15步/共15步）',
    	arrowAlign: 'bottom',
    	iconCls : 'edit',
    	pageX: 175,
    	pageY: 110,
    	style: {
    		marginBottom: '10px'
    	},
    	onClick: function() {
    		tabAdd("OperateSupervisorArrange","jsp/OperateSupervisorArrange.jsp","上机监考老师编排");
    		
          }
    	}
    ],
    tbar:[{
    	text:"  上一步",
    	iconCls : 'forward',
    	handler: function() {
    		operateSupervisorArrange.hidden=true;
    		operateSupervisorArrange.onHide();
    		theorySupervisorArrange.hidden=false;
    		theorySupervisorArrange.show();
			systemInitialWindow.doLayout();
    	}
    
    }, '->',{	
		text:"  完成",
		iconCls : 'ok',
		handler: function(){
			operateSupervisorArrange.hidden=true;
			operateSupervisorArrange.onHide();
			closeTab();
			
		}
	}]
});

function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}

function tabAdd(tabId,script,tabTitle){
	tabPanel = window.top.tabPanel;
	if(tabPanel.findById(tabId)==null)
	{
		tabPanel.add({
			id:tabId,
			title : tabTitle,
	        height:500,  
	        autoScroll:true,  
	        autoWidth:true,
	        closable:true,  
	        frame:true,  
	        html:'<iframe id="'+tabId+'_Frame" src="'+script+'" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>',
	        listeners:{
                activate:function(){
                 this.getUpdater().refresh();
                },
                beforeclose:function(){
                	var frame = window.top.document.getElementById(this.id+'_Frame');
                	if(frame.scr!=null)
                		frame.src = "javascript:false";
                }
	        }
		});
	}
	window.top.viewport.doLayout(true,true);
	tabPanel.show();
	tabPanel.setActiveTab(tabId);
};

var wizardItems=[backupPanel,clearHistoryPanel,importLanguagePanel,studentImportPanel,printProofBillPanel,editExamNumPanel,
                 examroomImportPanel,supervisorImportPanel,sectionManagePanel,theoryExamArrangePanel,
                 operateExamArrangePanel,theorySupervisorArrange,operateSupervisorArrange,exportStudentsDBF
                 ,selectStudentImportType,unSignUpStudentImportPanel,turnOnSignUpPanel];
var systemInitialWindow = new Ext.Window({
    title: '系统初始化（共15步）',
    width: 470,
    height:290,
    closeAction : 'hide',
    layout: 'fit',
    border:false,
    modal: true,
	shadow: true,
	hideMode: Ext.isIE ? 'offsets' : 'display',
	plain:true,
	bodyStyle:'padding:5px;',
	buttonAlign:'center',
	items: wizardItems,
	listeners:{
		"hide": function() {
        	var temp = window.top.tabPanel.getActiveTab( );
			window.top.tabPanel.remove(temp);
        }
	}
});