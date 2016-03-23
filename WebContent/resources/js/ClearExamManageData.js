function clearExamManageData() {
	Ext.MessageBox.confirm('提示','此操作将清除教室信息，监考人员信息，考试场次信息，理论/上机考场编排信息以及理论/上级监考老师编排信息,确认执行?',function(confirm) {
		if(confirm == 'yes') {
			clear();
		} else {
			closeTab();
		}
	});
}
function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}
function clear(){
	Ext.MessageBox.wait('正在执行清除考务信息，请等待...','提示');
	ClearHistoryDataController.clearExamManageData(function(data) {
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
}