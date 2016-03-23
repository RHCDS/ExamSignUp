function clearHistoryData() {
	Ext.MessageBox.alert('提示','请确认已经导出学生数据！');
	Ext.MessageBox.confirm('提示','是否要清空历史数据？',clear);
}
function clear(confirm){
	if(confirm == "yes") {
		Ext.MessageBox.wait('正在执行操作...','请等待');		
		ClearHistoryDataController.clearHistoryData(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			if(jsonData.success == true) {
				Ext.MessageBox.hide();
				Ext.MessageBox.alert('提示',jsonData.errors.info);
			} else {
				Ext.MessageBox.hide();
				Ext.MessageBox.alert('提示',jsonData.errors.info);
			}
		});
	}
}