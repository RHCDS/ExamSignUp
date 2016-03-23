function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}

function exportBackupStudentsXls(confirm){
	if(confirm == "yes") {
		var f = document.getElementById('exportBackupStudentsExcel');
		f.action = '../backupStudentsExcel.do';
		f.submit();
	} else {
		closeTab();
	}	
}
function backUpStudentsInfo() {
	Ext.MessageBox.confirm('提示','请先确认已经完成准考证编排操作；否则，导出的数据不全！',exportBackupStudentsXls);
}