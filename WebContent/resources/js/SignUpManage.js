function turnOnSignUp(){
	Ext.MessageBox.alert('提示','开启报名后，考生可以任意的修改考试语种！',function() {
		Ext.MessageBox.confirm('提示','确认开启报名？',turnOn);
	});
}
function turnOn(confirm) {
	if(confirm == 'yes') {
		InstitutionController.turnOnSignUp(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			Ext.MessageBox.alert('提示',jsonData.error.info,function() {
				closeTab();
			});

		});
	}else {
		closeTab();
	}
}
function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}
function turnDownSignUp(){
	Ext.MessageBox.alert('提示','关闭报名后，考生不可以自行选择语种！',function() {
		Ext.MessageBox.confirm('提示','确认关闭报名？',turnDown);
	});
}
function turnDown(confirm) {
	if(confirm == 'yes') {
		InstitutionController.turnDownSignUp(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			Ext.MessageBox.alert('提示',jsonData.error.info,function() {
				closeTab();
			});

		});
	} else {
		closeTab();
	}
	
}