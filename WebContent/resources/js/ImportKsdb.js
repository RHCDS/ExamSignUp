
var student_currentSearchFilter = "";
var KSDBimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../importStudentToKSY.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'ksdbfile',
		inputType : 'file'//文件类型 
	} ]
});

var KSDBimportwin;

function KSDBimprot(){
	if(!KSDBimportwin){
		KSDBimportwin = new Ext.Window({
   	        title: '文件上传',
   	        width: 400,
   	        height:300,
   	        minWidth: 400,
   	        minHeight: 300,
   	        closeAction : 'hide',
   	        layout: 'fit',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: KSDBimportform,
   	     buttons : [ {
   			text : '上传报名库',
   			handler : function() {
   				var temp=KSDBimportform.getForm().getFieldValues().ksdbfile;
   				var reg= /\.xls$/;
				if(temp=="" || reg.exec(temp) != ".xls"){
					Ext.MessageBox.alert('提示',"请选择xls文件！");
					return;
				}
   				Ext.MessageBox.confirm('提示','此操作会清空历史上报数据，是否继续？',function clear(confirm){
   					if(confirm == "yes") {
   						Ext.MessageBox.wait('正在执行，请等待...','提示');
   		   				KSDBimportform.getForm().submit({
   		   					success : function(form, action) {
   		   							var students = Ext.util.JSON.decode(action.response.responseText).excelArray;
   		   							StudentController.importKsdb(students,function(data){
   		   								var jsonData=Ext.util.JSON.decode(data);
   		   								if(jsonData.success==true)
   		   								{
   		   									Ext.MessageBox.alert('提示',jsonData.errors.info,function(){
   		   									KSDBimprot.hide();
   		   									});
   		   								}
   		   								else
   		   								{
   		   									Ext.MessageBox.alert('错误',jsonData.errors.info);
   		   								}
   		   	   						});
   		   					},
   		   					failure : function(form, action) {
   		   						Ext.MessageBox.alert('错误', Ext.util.JSON.decode(action.response.responseText).msg,function(){
   		   							var temp = window.top.tabPanel.getActiveTab( );
   		   							window.top.tabPanel.remove(temp);
   		   						});
   		   						
   		   					}
   		   				});
   					} else {
   						return;
   					}
   				});
   			}
   		} ],
		listeners:{
            "hide": function()
            {
            	unapprovaldataPageInit();
            }
        }
		});
	}
	return KSDBimportwin;
}

function KSDBimprotInit() {
	KSDBimprot().show();
}

//////////////////////////////////////////////////
///////////////上报数据表///////////////////////
/////////////////////////////////////////////////


var unapprovaldata= Ext.data.Record.create([{
	name : 'examnum'
},{
	name : 'idnum'
}, {
	name : 'name'
},{
	name : 'exLanguage'
}, {
	name : 'sex',convert:function(data){if(data=="M"){return "男";}else return "女";}
}]);


var unapprovaldatastore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, unapprovaldata),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.paginationShowForApproval
	  })
});

var unapprovaldatagrid = new Ext.grid.GridPanel({
region : 'center',
id : 'unapprovaldatagrid',
store : unapprovaldatastore,
title : '已上报考生名单',
loadMask : true,
stripeRows : true,
autoScroll : true,
autoExpandColumn : 'exLanguage',
viewConfig : {
sortDescText : '降序',
sortAscText : '升序',
columnsText : '显示列',
forceFit : false
},
columns : [new Ext.grid.RowNumberer(), {
		id : 'exLanguage',
		header : '语种名称',
		dataIndex : 'exLanguage',
		width : 150,
		sortable : true
	}, {
		header : '准考证号',
		dataIndex : 'examnum',
		width : 200,
		sortable : true
	}, {
		header : '身份证号',
		dataIndex : 'idnum',
		width : 200,
		sortable : true
	}, {
		header : '姓名',
		dataIndex : 'name',
		width : 150,
		sortable : true
	}, {
		header : '性别',
		dataIndex : 'sex',
		width : 80,
		sortable : true
	}],
    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : unapprovaldatastore,
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
	        params.filter = student_currentSearchFilter;
	        params.start = start;
	        params.limit = this.pageSize;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  }),
  tbar:[{
      text: '查询',
      iconCls : 'upload-icon',
      scope: this,
      handler:function(){
      	student_searchWin.show();
      	student_searchform.getForm().reset();
      }
  }]
});

var student_searchform = new Ext.form.FormPanel({
	id:'student_searchform',
	region:'center',
//	autoScroll:true,
//	bodyStyle:"overflow-y:scroll;overflow-x:hidden",
	frame:true,
	labelWidth:68,
	autoHeight:true,
	items:[{
	        xtype:'fieldset',
	        title: '考生信息',
	        autoHeight:true,
	        layout:'column',
	        items :[
        		{columnWidth:.5,layout:'form',items:[
					{xtype:'textfield',fieldLabel: '考生姓名',name:'and$student-name$like$%value%',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
					{xtype:'textfield',fieldLabel: '身份证号',name:'and$student-idnum$=$value',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
					{xtype:'textfield',fieldLabel: '准考证号',name:'and$student-examnum$=$value',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
				    {xtype:'textfield',fieldLabel: '考场号',name:'and$student-examnum$like$__________value%',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
				    {xtype:'textfield',fieldLabel: '语种代码',name:'and$student-exLanguage-languagenum$=$value',anchor:'96%'}
				]}
			]
	    }],
	keys: [{
          key: 13,//回车
          fn: function(){
    	  	student_search();
    	  	student_searchWin.hide();
      	  }
	}]
});

var student_searchWin = new Ext.Window({
	title: '查询条件',
	width: 500,
    autoHeight:true,
    minWidth: 300,
    minHeight: 200,
    closeAction : 'hide',
    layout: 'fit',
    plain:true,
    buttonAlign:'center',
	items: [student_searchform],
	modal: true,
	shadow: true,
	buttons:[{
                text : '查询',
                handler : function() {
                	student_search();
                	student_searchWin.hide();
                }
         }, {
                text : '清空',
                handler : function() {
                	student_searchform.getForm().reset();
                	student_currentSearchFilter = "";
                }
         }, {
                text : '关闭',
                handler : function() {
                	student_searchWin.hide();
                }
         }]
});

function student_search(){
	student_currentSearchFilter = Ext.encode(student_searchform.getForm().getFieldValues());
	unapprovaldatastore.load({
		params : {
					filter : student_currentSearchFilter,
					start : 0,
					limit : 20
		}
	});
}


function unapprovaldataPageInit() {
new Ext.Viewport({
		layout : 'border',
		hideMode : Ext.isIE ? 'offsets' : 'display',
		items : [unapprovaldatagrid],
		renderTo : Ext.getBody()
	});
unapprovaldatastore.load({
	params : {
		filter : student_currentSearchFilter,
		start : 0,
		limit : 20
	}
});
}