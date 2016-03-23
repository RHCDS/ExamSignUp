
var  passwordfields = [
                      {columnWidth:1,layout:'form',labelWidth: 90,items:[
                  		{xtype:'hidden',name: 'id',anchor:'98%'},
                  		{xtype:'textfield',inputType:'password',id: 'oldPassword',fieldLabel: '原始密码',maxLength: 15,name: 'oldPassword',anchor:'97.5%',allowBlank: false}
                  	]},
                  	{columnWidth:1,layout:'form',labelWidth: 90,items:[
                  		{xtype:'textfield',inputType:'password',id: 'newPassword',fieldLabel: '新密码(6-15位)',minLength: 6,maxLength: 15,name: 'newPassword',anchor:'97.5%',allowBlank: false}
                  	]},
                  	{columnWidth:1,layout:'form',labelWidth: 90,items:[
                  	    {xtype:'textfield',inputType:'password',id: 'ensurepassword',fieldLabel: '重复新密码',minLength: 6,maxLength: 15,name: 'ensurepassword',anchor:'97.5%',allowBlank: false}
                  	]}
];

var passwordForm = new Ext.FormPanel({
    id : 'passwordForm',
	labelWidth: 80,
	region : 'center',
    labelAlign : 'left',
    layout:'column',
    frame:true,
    autoHeight : true,
    border : false,
	bodyStyle:'padding:5',
    items : [passwordfields]
});

var passwordWindow = new Ext.Window({
       title: '密码设置',
       width: 350,
       height:200,
       closeAction : 'hide',
       layout: 'fit',
       border:false,
       modal: true,
       shadow: true,
       hideMode: Ext.isIE ? 'offsets' : 'display',
       plain:true,
       bodyStyle:'padding:5px;',
       buttonAlign:'center',
       items: [passwordForm],
       buttons: [{
	    text : '确认',
	    handler : function() {
	    	if(passwordForm.getForm().isValid()){
	         	if(Ext.getCmp("newPassword").getValue()!=Ext.getCmp("ensurepassword").getValue()){
	         			Ext.MessageBox.alert('提示',"新密码和确认密码不同！");
	         		}
	         	else{
	         		var oldPassword=hex_md5(Ext.getCmp("oldPassword").getValue());
	         		var newPassword=hex_md5(Ext.getCmp("newPassword").getValue());
	         		var passwordJSON=new   Object();
	         		passwordJSON.oldPassword=oldPassword;
	         		passwordJSON.newPassword=newPassword;
	         		
	         		ModifyPasswordController.setRolePassword(passwordJSON,function(data) {
    					var jsonData = Ext.util.JSON.decode(data);
    					if(jsonData.success == true) {
    						Ext.MessageBox.alert('提示',jsonData.errors.info,function(){
    							var temp = window.top.tabPanel.getActiveTab( );
    							window.top.tabPanel.remove(temp);
    						});
    					}
    					else if(jsonData.success == false) {
    						Ext.MessageBox.alert('提示',jsonData.errors.info);
    					}});
			    	}
	         }	    	
	     }
		}, {
	    text : '取消',
	    handler : function() {
	    	var temp = window.top.tabPanel.getActiveTab( );
			window.top.tabPanel.remove(temp);
	     }
		}],
		listeners:{
            "hide": function()
            {
            	var temp = window.top.tabPanel.getActiveTab( );
				window.top.tabPanel.remove(temp);
            }
        }
  });


function pageInit() {
	passwordWindow.show();
}