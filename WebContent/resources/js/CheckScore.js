
var studentRecord = Ext.data.Record.create([{
		name :'examnum'
	}, {
		name :'name'
	}, {
		name :'college'
	}, {
		name : 'score'
	}, {
		name : 'newscore'
	}, {
		name: 'remark'
	}
]);

var studentStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, studentRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.checkScoreStudentPaginationShow
	  })
});

var studentGrid = new Ext.grid.GridPanel({
	region:'center',
	layout: 'fit',
	id : 'studentGrid',
	store : studentStore,
	title : '复查考生列表',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'examnum',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer(),
	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :300,
	 	sortable :true
	},	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :150,
	 	sortable :true
	},	{
		id : 'college',
		header : '学院',
	  	dataIndex :'college',
	  	width :200,
	 	sortable :true
	},	{
		id : 'score',
		header : '成绩',
	  	dataIndex :'score',
	  	width :150,
	 	sortable :true
	}
	],
    tbar : [{
    	text : '复查考生录入',
    	tooltip : '新增',
    	iconCls : 'add',
    	onClick : function() {
    		var newstudent = new studentRecord({
    			examnum : '',
    			name : '',
    			score : '',
    			newscore : ''
    		});
    		inputWindowInit('复查考生信息').show();
    		studentform.getForm().reset();
    		studentsearchform.getForm().reset();
    		studentform.getForm().loadRecord(newstudent);
    	}
		},'-', {
			text : '删除',
			tooltip : '删除选中的菜单项',
			iconCls : 'remove',
			onClick : function() {
				if (studentGrid.getSelectionModel().hasSelection()) {
					Ext.MessageBox.confirm('提示', '你确定要删除选中的菜单项么?',
						function(button) {
							if (button == 'yes') {
								var list = studentGrid.getSelectionModel().getSelections();
								var rList = [];
								for (var i = 0; i <list.length; i++) {
									rList[i] = list[i].data["examnum"];
								}      
								StudentController.deleteCheckScoreStudent(
										rList, function(data) {
											if (data) {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
												StudentGridInit();
											} else {
												Ext.MessageBox.alert('提示',"删除菜单项失败!");
											}
										});
							}
					});
				} else {
					Ext.MessageBox.alert('提示', "请至少选择一条记录!");
				}
			}
		}, '-', {
			text: '导出复查考生',
			iconCls : 'upload-icon',
			scope : this,
			handler : function() {
				exportCheckScoreStudentsXls(); 
			}
		}
	] ,
	bbar : new Ext.PagingToolbar({
		pageSize:20,
		store : studentStore,
		displayInfo : true,
		firstText:'首页',
		lastText:'尾页',
		prevText:'上一页',
		nextText:'下一页',
		refreshText:'刷新',
		displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
		emptyMsg : "没有记录",
		doLoad:function(start) {
			var params = {};
			params.start = start;
			params.limit = this.pageSize;
			if (this.fireEvent("beforechange", this, params) !== false) {
				this.store.load({params:params});
			}
		}
	})
 });
 
function exportCheckScoreStudentsXls(){
	var f = document.getElementById('exportCheckScoreExcel');
	f.action = '../exportCheckScoreExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
	});
}

var studentsearchfields = [
                            {
	columnWidth : 0.9,
	layout : 'form',
	labelWidth : 70,
	items : [ {
		id : 'examnumforsearch',
		xtype : 'textfield',
		fieldLabel : '准考证号',
		name : 'examnumforsearch',
		anchor : '98%'
	} ]
}, {
	columnWidth : 0.1,
	layout : 'form',
	items : [ {
		xtype : 'button',
		anchor : '98%',
		text : '查询',
		onClick : function(){
			var examnum = Ext.getCmp("examnumforsearch").getValue();
			StudentController.findCheckScoreStudentByExamNum(examnum,function(data){
				if(data){
					studentform.getForm().setValues(data);
				}else{
					Ext.MessageBox.alert('提示',"未找到对应考生！");
				}
			});
		}
	} ]
}         
];

var studentsearchform = new Ext.form.FormPanel({
    labelWidth: 60,
    height : 40,
    labelAlign : 'left',
    layout : 'column',
    region : 'north',
    frame:true, 
    border : false,
    bodyStyle:'padding:5',
    items : [studentsearchfields]
});

var  studentfields = [
	{columnWidth:1,layout:'form',items:[
        {xtype:'textfield',fieldLabel: '准考证号',name: 'examnum',anchor:'99%',allowBlank: false}
	]},
	{columnWidth:1,layout:'form',items:[
        {xtype:'textfield',fieldLabel: '姓名',name: 'name',anchor:'99%',allowBlank: false}
    ]}
];

 var studentform = new Ext.form.FormPanel({
    labelWidth: 70,
    labelAlign : 'left',
    layout : 'column',
    region : 'center',
    frame:true,
    border : false,
    bodyStyle:'padding:5',
    items : [studentfields]
});

var checkScorewin;

function inputWindowInit(title){
   
    if (!checkScorewin) {
   	    checkScorewin = new Ext.Window({
   	        width: 480,
   	        height:250,
   	        closeAction : 'hide',
   	        layout: 'border',
			border:false,
			modal: true,
			shadow: true,
			hideMode: Ext.isIE ? 'offsets' : 'display',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: [studentform,studentsearchform],
   	        buttons: [{
                   text : '保存',
                   handler : function() {
                   	if(studentform.getForm().isValid()){
                    	var student = studentform.getForm().getValues();
                    	StudentController.saveCheckScoreStudent(student,function(data){
                    		var jsonData = Ext.util.JSON.decode(data);
                	    	if(jsonData.sucess == "true"){
                	    		Ext.MessageBox.alert('提示',jsonData.info);
                	    		checkScorewin.hide();
                	    	}else {
                	    		Ext.MessageBox.alert('提示',jsonData.info);
                	    	}
                	    	StudentGridInit();
                	    });            	
                   	
                   	}else{
                   		Ext.MessageBox.alert('提示',"输入信息有误！");
                   	}
                   }
                          		
            }, {
                   text : '清空',
                   handler : function() {
                	   studentform.getForm().reset();
                   }
            }, {
                   text : '关闭',
                   handler : function() {
                	   checkScorewin.hide();
                   }
            }]
  	    });
   	
    }
    checkScorewin.setTitle(title);
    return checkScorewin;
	
}

function StudentGridInit(){
	studentStore.load({
		params : {
			start : 0,
			limit : 20
		}
	});
}

function PageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [studentGrid],
		renderTo :Ext.getBody()
	});
	StudentGridInit();
}
