function clearHistoryData() {
	Ext.MessageBox.alert('提示','请确认已经备份历史数据！',function() {
		Ext.MessageBox.confirm('提示','此操作会将考务管理信息一并删除，是否要清空历史数据？',clear);
	});
	 
}
function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}
function clear(confirm){
	if(confirm == "yes") {		
		ClearHistoryDataController.clearHistoryData(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			if(jsonData.success == true) {
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					closeTab();
				});
			} else {
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					closeTab();
				});
			}
		});
	} else {
		closeTab();
	}
}