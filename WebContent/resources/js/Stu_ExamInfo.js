var studentexaminfo= Ext.data.Record.create([{
	name : 'examnum'
}, {
	name : 'languagename'
}, {
	name : 'logicexamnum'
}, {
	name : 'stuname'
}, {
	name : 'operatedate'
}, {
	name : 'operatetime'
}, {
	name : 'theorydate'
}, {
	name : 'theoryroomlocation'
}, {
	name : 'theorytime'
}, {
	name : 'operateroomlocation'
}]);

var studentexaminfostore = new Ext.data.JsonStore({
	fields : studentexaminfo
});

var studentexaminfosgrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'studentexaminfosgrid',
	store : studentexaminfostore,
	title : '考场信息',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [new Ext.grid.RowNumberer(),{
		header : '准考证号',
		dataIndex : 'examnum',
		width : 120,
		sortable : true
	},{
		header : '姓名 ',
		dataIndex : 'stuname',
		width : 120,
		sortable : true
	},{
		header : '语种 ',
		dataIndex : 'languagename',
		width : 100,
		sortable : true
	},{
		header : '考场号',
		dataIndex : 'logicexamnum',
		width : 60,
		sortable : true
	},{
		header : '考试地点(理论)',
		dataIndex : 'theoryroomlocation',
		width : 100,
		sortable : true 
	},{
		header : '考试日期(理论)',
		dataIndex : 'theorydate',
		width : 100,
		sortable : true
	},{
		header : '开考时间(理论)',
		dataIndex : 'theorytime',
		width : 100,
		sortable : true
	},{
		header : '考试地点(上机)',
		dataIndex : 'operateroomlocation',
		width : 100,
		sortable : true
	}, {
		header : '考试日期(上机)',
		dataIndex : 'operatedate',
		width : 100,
		sortable : true
	},{
		header : '开考时间(上机)',
		dataIndex : 'operatetime',
		width : 100,
		sortable : true
	}]
});

function PageInit(studentidnum) {
	StudentController.getStuExamInfo(studentidnum,function(data){
		if(data){
			studentexaminfostore.loadData(data);
		}else{
			Ext.MessageBox.alert('提示', "还未安排考场！",function(){
            	var temp = window.top.tabPanel.getActiveTab();
				window.top.tabPanel.remove(temp);
			});
		}
	});
	new Ext.Viewport({
				layout : 'border',
				hideMode : Ext.isIE ? 'offsets' : 'display',
				items : [studentexaminfosgrid],
				renderTo : Ext.getBody()
	});
}