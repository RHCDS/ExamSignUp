var studentHisimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../importStuHistoryInfo.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'studenthistoryfile',
		inputType : 'file'//文件类型 
	} ]
});

var progressbar = new Ext.ProgressBar({
	region : 'south',
	width : 300
});

function progressbarWaiting(){
	progressbar.wait({
		text : '正在执行，请耐心等候.....'
	});
}

var studentHisimportwin;

function studentHisimprot(){
	if(!studentHisimportwin){
		studentHisimportwin = new Ext.Window({
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
   	        items: [studentHisimportform,progressbar],
   	     buttons : [ {
   			text : '数据导入',
   			handler : function() {
   				progressbarWaiting();
   				studentHisimportform.getForm().submit({
   					success : function(form, action) {
   						var students = Ext.util.JSON.decode(action.response.responseText).excelArray;
   						StudentController.importStuHistoryInfo(students,function(data){
   							if(data){
   								Ext.Msg.alert('提示', '文件上传成功！',function(){
   		   	   						progressbar.reset();
   		   	   						var temp = window.top.tabPanel.getActiveTab( );
   		   	   						window.top.tabPanel.remove(temp);
   		   						});
   							}	
   						});
   					},
   					failure : function() {
   						Ext.Msg.alert('错误', '数据导入失败',function(){
   	   						progressbar.reset();
   							var temp = window.top.tabPanel.getActiveTab( );
   							window.top.tabPanel.remove(temp);
   						});
   						
   					}
   				});
   			}
   		} ],
		listeners:{
            "hide": function()
            {
            	var temp = window.top.tabPanel.getActiveTab( );
				window.top.tabPanel.remove(temp);
            }
        }
		});
	}
	return studentHisimportwin;
}

function pageInit() {
	studentHisimprot().show();
}