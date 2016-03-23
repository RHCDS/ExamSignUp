var sudentCJimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../acceptStudentCJ.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'studentCJfile',
		inputType : 'file'//文件类型 
	} ]
});



var sudentCJimportwin;

function sudentCJimprot(){
	if(!sudentCJimportwin){
		sudentCJimportwin = new Ext.Window({
   	        title: '文件上传',
   	        width: 400,
   	        height:300,
   	        minWidth: 400,
   	        minHeight: 300,
   	        closeAction : 'hide',
   	        layout: 'border',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: sudentCJimportform,
   	     buttons : [ {
   			text : '接收成绩',
   			handler : function() {
				var temp=sudentCJimportform.getForm().getFieldValues().studentCJfile;
   				var XLSreg= /\.dbf$/;
				if(temp=="" || !(XLSreg.exec(temp) == ".dbf" )){ 
					Ext.MessageBox.alert('提示',"上传文件格式不对！");
					return;
				}
   				Ext.MessageBox.wait('正在执行，请等待...','提示');
   				sudentCJimportform.getForm().submit({
   					success : function(form, action) {
   						var accept = Ext.util.JSON.decode(action.response.responseText);
   						if(accept.success){
   	   						Ext.MessageBox.alert('提示', '接收成功！',function(){
   	   	   						var temp = window.top.tabPanel.getActiveTab( );
   	   	   						window.top.tabPanel.remove(temp);
   	   						});
   						}else{
   	   						Ext.MessageBox.alert('接收失败', accept.errors.info,function(){
   	   							var temp = window.top.tabPanel.getActiveTab( );
   	   							window.top.tabPanel.remove(temp);
   	   						});
   						}

   					},
   					failure : function(form, action) {
   						var accept = Ext.util.JSON.decode(action.response.responseText);
   						Ext.MessageBox.alert('接收失败',  accept.errors.info,function(){
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
	return sudentCJimportwin;
}

function pageInit() {
	sudentCJimprot().show();
}