function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}

function exportCJDbf(confirm){
	if(confirm == "yes") {
		var f = document.getElementById('exportOneSchoolCJDbf');
		f.action = '../exportOneSchoolCJDbf.do';
		f.submit();
	} else {
		closeTab();
	}	
}
function exportCJ() {
	Ext.MessageBox.confirm('提示','导出成绩后，请先进行查看。若有误，请联系上一级管理单位。',exportCJDbf);
}