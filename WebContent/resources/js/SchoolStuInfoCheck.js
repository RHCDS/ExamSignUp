var schoolStudentInfoCheckRecord= Ext.data.Record.create([{
	name : 'languagename'
}, {
	name : 'count'
}]);

var schoolStudentInfoCheckStore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
        totalProperty : 'totalProperty',
        root : 'root'
     }, schoolStudentInfoCheckRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.loadExamNumLanguageInfoByInstitutionnum
	  })
});

var statusLable = new Ext.form.Label({
	id : "statusLable",
	text : " ",
	width : 100,
	autoShow : true,
	autoWidth : true,
	autoHeight : true
});

var schoolStudentInfoCheckGrid = new Ext.grid.GridPanel({
region : 'center',
id : 'schoolStudentInfoCheckGrid',
store : schoolStudentInfoCheckStore,
title : '审核结果统计表',
loadMask : true,
stripeRows : true,
autoScroll : true,
viewConfig : {
sortDescText : '降序',
sortAscText : '升序',
columnsText : '显示列',
forceFit : false
},
columns : [new Ext.grid.RowNumberer(), {
		id : 'languagename',
		header : '语种名称',
		dataIndex : 'languagename',
		width : 300,
		sortable : true
	}, {
		header : '报名人数（人）',
		dataIndex : 'count',
		width : 300,
		sortable : true
	}],
	tbar : [{
		text : '确认本校报名数据',
		tooltip : '确认本校报名数据',
		iconCls : 'edit',
		onClick : function() {
			Ext.MessageBox.confirm('提示','您已经确定考试数据？',function clear(confirm){
				if(confirm == "yes") {
						Ext.MessageBox.wait('正在执行，请等待...','提示');
						InstitutionstatusController.schoolCheckOK(function(data){
							var jsonData=Ext.util.JSON.decode(data);
							if(jsonData.success==true)
							{
								Ext.MessageBox.alert('提示',jsonData.errors.info);
							}
							else
							{
								Ext.MessageBox.alert('提示',jsonData.errors.info);
							}
							InstitutionstatusController.getCurrenStatus(function(data){
								var jsonData=Ext.util.JSON.decode(data);
								statusLable.getEl().update(jsonData.errors.info);
							});
						});
				}
				else{
					return;
				}});
	}},'->',statusLable]
});
function initPage(){
	new Ext.Viewport({
		layout : 'border',
		hideMode : Ext.isIE ? 'offsets' : 'display',
		items : [schoolStudentInfoCheckGrid],
		renderTo : Ext.getBody()
	});
	InstitutionstatusController.getCurrenStatus(function(data){
		var jsonData=Ext.util.JSON.decode(data);
		statusLable.getEl().update(jsonData.errors.info);
	});
	schoolStudentInfoCheckStore.load();
}







