function turnOnCheckScore(){
	Ext.MessageBox.confirm('提示','确认开启查分？',turnOn);
}
function turnOn(confirm) {
	if(confirm == 'yes') {
		InstitutionController.turnOnCheckScore(function(data) {
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
function turnOffCheckScore(){
	Ext.MessageBox.confirm('提示','确认关闭查分？',turnOff);
}
function turnOff(confirm) {
	if(confirm == 'yes') {
		InstitutionController.turnOffCheckScore(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			Ext.MessageBox.alert('提示',jsonData.error.info,function() {
				closeTab();
			});

		});
	} else {
		closeTab();
	}
	
}