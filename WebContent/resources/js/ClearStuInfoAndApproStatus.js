function clearHistoryData() {
		Ext.MessageBox.confirm('提示','是否要清空历史数据？',clear);

}
function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}
function clear(confirm){
	if(confirm == "yes") {
		Ext.MessageBox.wait('正在执行操作...','请等待');
		ClearHistoryDataController.clearProvinceStuInfo(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			if(jsonData.success == true) {
				Ext.MessageBox.hide();
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					closeTab();
				});
			} else {
				Ext.MessageBox.hide();
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					closeTab();
				});
			}
		});
	} else {
		closeTab();
	}
}