
var student_currentSearchFilter = "";
var importFraudform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../importFraud.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'zbkfile',
		inputType : 'file'//文件类型 
	} ]
});



var importFraudwin;

function importFraud(){
	if(!importFraudwin){
		importFraudwin = new Ext.Window({
   	        title: '文件上传',
   	        width: 400,
   	        height:300,
   	        minWidth: 400,
   	        minHeight: 300,
   	        closeAction : 'hide',
   	        layout: 'fit',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        items: importFraudform,
   	        buttonAlign:'center',
   	     buttons : [ {
   			text : '上传作弊库',
   			handler : function() {
   				Ext.MessageBox.wait('正在执行，请等待...','提示');
   				importFraudform.getForm().submit({
   					success : function(form, action) {
   							var students = Ext.util.JSON.decode(action.response.responseText).excelArray;
   							StudentController.importFraud(students,function(data){
   	   							if(data){
   	   								Ext.MessageBox.alert('提示', '文件上报成功！',function(){
   	   									importFraudwin.hide();
   	   								});
   	   							}	
   							});
   					},
   					failure : function() {
   						Ext.MessageBox.alert('错误', '文件上传失败',function(){
   	   						
   							var temp = window.top.tabPanel.getActiveTab( );
   							window.top.tabPanel.remove(temp);
   						});
   						
   					}
   				});
   			}
   		} ],
		listeners:{
            "hide": function()
            {
            	pageInit();
            }
        }
		});
	}
	return importFraudwin;
}

function importFraudInit() {
	importFraud().show();
}

//////////////////////////////////////////////////
///////////////上报表///////////////////////
/////////////////////////////////////////////////

var studentfraud = Ext.data.Record.create([{
	name :'examnum'
}, {
	name :'name'
}, {
	name :'theoryfraud',convert:function(data){if(data==1){return "是";}else{return "否";}}
}, {
	name :'operatefraud',convert:function(data){if(data==1){return "是";}else{return "否";}}
}
]);
var studentfraudstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, studentfraud),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.fraudStudentPaginationShow
	  })
});

var studentfraudgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentfraudgrid',
	store : studentfraudstore,
	title : '作弊库',
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
	  	width :200,
	 	sortable :true
	},	{
		id : 'theoryfraud',
		header : '理论作弊',
	  	dataIndex :'theoryfraud',
	  	width :150,
	 	sortable :true
	},	{
		id : 'operatefraud',
		header : '上机作弊',
	  	dataIndex :'operatefraud',
	  	width :150,
	 	sortable :true
	}
	],
    
	bbar : new Ext.PagingToolbar({
		pageSize:20,
		store : studentfraudstore,
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

function pageInit() {
	new Ext.Viewport({
			layout : 'border',
			hideMode : Ext.isIE ? 'offsets' : 'display',
			items : [studentfraudgrid],
			renderTo : Ext.getBody()
	});
	studentfraudstore.load({
		params : {
			start : 0,
			limit : 20
		}
	});
	
}







