
var student_currentSearchFilter = "";
var importAbsentform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../importAbsence.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'qkkfile',
		inputType : 'file'//文件类型 
	} ]
});



var importAbsentwin;

function importAbsent(){
	if(!importAbsentwin){
		importAbsentwin = new Ext.Window({
   	        title: '文件上传',
   	        width: 400,
   	        height:300,
   	        minWidth: 400,
   	        minHeight: 300,
   	        closeAction : 'hide',
   	        layout: 'fit',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        items: importAbsentform,
   	        buttonAlign:'center',
   	     buttons : [ {
   			text : '上传缺考库',
   			handler : function() {
				var temp=importAbsentform.getForm().getFieldValues().qkkfile.toLowerCase();
   				var ZIPreg= /\.xls$/;
				if(temp=="" || !(ZIPreg.exec(temp) == ".xls")){ 
					Ext.MessageBox.alert('提示',"请选择xls后缀文件！");
					return;
				}
   				Ext.MessageBox.wait('正在执行，请等待...','提示');
   				importAbsentform.getForm().submit({
   					success : function(form, action) {
   							var students = Ext.util.JSON.decode(action.response.responseText).excelArray;
   							StudentController.importAbsent(students,function(data){
   	   							if(data){
   	   								Ext.Msg.alert('提示', '文件上报成功！',function(){
   	   									importAbsentwin.hide();
   	   								});
   	   								
   	   							}	
   	   						});
   					},
   					failure : function() {
   						Ext.Msg.alert('错误', '文件上传失败',function(){
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
	return importAbsentwin;
}

function importAbsentInit() {
	importAbsent().show();
}

//////////////////////////////////////////////////
///////////////上报表///////////////////////
/////////////////////////////////////////////////

var studentAbsent = Ext.data.Record.create([{
	name :'examnum'
}, {
	name :'name'
}, {
	name :'theoryabsent',convert:function(data){if(data==1){return "是";}else{return "否";}}
}, {
	name :'operateabsent',convert:function(data){if(data==1){return "是";}else{return "否";}}
}
]);
var studentAbsentstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, studentAbsent),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.absentStudentPaginationShow
	  })
});

var studentAbsentgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentAbsentgrid',
	store : studentAbsentstore,
	title : '缺考库',
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
		id : 'theoryabsent',
		header : '理论缺考',
	  	dataIndex :'theoryabsent',
	  	width :150,
	 	sortable :true
	},	{
		id : 'operateabsent',
		header : '上机缺考',
	  	dataIndex :'operateabsent',
	  	width :150,
	 	sortable :true
	}
	],
    
	bbar : new Ext.PagingToolbar({
		pageSize:20,
		store : studentAbsentstore,
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
			items : [studentAbsentgrid],
			renderTo : Ext.getBody()
	});
	studentAbsentstore.load({
		params : {
			start : 0,
			limit : 20
		}
	});
	
}







