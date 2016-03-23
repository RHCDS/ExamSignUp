var student_currentSearchFilter = "";

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
				    {xtype:'textfield',fieldLabel: '考生学号',name:'and$student-studentnum$=$value',anchor:'96%'}
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
	ExamNumEditStore.load({
		params : {
					filter : student_currentSearchFilter,
					start : 0,
					limit : 20
		}
	});
}

var examNumRecord = Ext.data.Record.create([{
		name :'id'
	}, {
		name :'name'
	}, {
		name :'studentnum'
	}, {
		name :'idnum'
	}, {
		name :'examnum'
	},{
		name:'campusname'
	},{
		name:'languagename'
	},{
		name:'paied',convert:function(data){if(data==1){return "已缴费";}else{return "未缴费";}}
	}
]);

var ExamNumEditStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root',
         idProperty:'id'
      }, examNumRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction :ExamNumEditController.loadPageDate
	  })
});

function switchForm(num,wizards)
{
	var i;
	for(i=0;i<wizards.length;i++)
	{
		wizards[i].hidden=true;
		wizards[i].hide();
		if(i==num)
		{
			wizards[i].hidden=false;
			wizards[i].show();
		}
	}
}
//=========================
var flagstore = new Ext.data.JsonStore({
	fields:['name','value'],
	data:[{name:'否',value:'reset'},{name:'是',value:'append'}]
});
var  examNumEditParamField = [
	{columnWidth:1,layout:'form',labelWidth: 100,items:[
        {xtype:'textfield',fieldLabel: '考试期次',readOnly:true,
        	name: 'examtime',anchor:'98%',allowBlank: false},
	    {xtype:'textfield',fieldLabel: '学校代码',readOnly:true,
	    	name: 'institutionnum',anchor:'98%',allowBlank: false},
        {xtype:'textfield',fieldLabel: '单个考场容量(人)',name: 'capacity',anchor:'98%',allowBlank: false,readOnly:true},
	    {	
        	xtype:'combo',
	    	fieldLabel:'增量编排',
	    	store:flagstore,
	    	editable: false,
	    	submitValue:true,
	    	hiddenName:'appendflag',
	    	forceSelection:true,
	    	valueField:'value',
	    	displayField:'name',
	    	typeAhead: true,
	    	name: 'appendflag',
	    	mode: 'local',
	    	triggerAction: 'all',
	    	anchor:'98%',
	    	allowBlank:false
	    }
	]}
 ];
var examNumEditParamForm = new Ext.form.FormPanel({
	id : 'examNumEditParamForm',
	title : '确认编排信息',
	hidden : true,
	width : 600,
	height : 300,
	labelWidth : 60,
	labelAlign : 'left',
	layout : 'column',
	frame : true,
	border : false,
	items : [ examNumEditParamField ],
	listeners : {
		afterRender : function(data) {
			ExamNumEditController.getArrangeInfo(function(data) {
				examNumEditParamForm.getForm().setValues(data);
			});
		}
	},
	bbar : [ '->', {
		text : "下一步",
		handler : function() {
			switchForm(1, wizardItems);
			examNumEditWindow.doLayout();
		}
	} ]
});
// =========================
var examNumLanguageInfoRecord = Ext.data.Record.create([{
	name :'languagename'
}, {
	name :'count'
}
]);

var examNumLanguageInfoStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'id'
  }, examNumLanguageInfoRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction :ExamNumEditController.loadExamNumLanguageInfo
  })
});
var examNumLanguageInfoGrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'examNumLanguageInfoStore',
	hidden:true,
	width: 600,
    height:300,
	store : examNumLanguageInfoStore,
	title : '考试报名情况确认',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'languagename',
	frame:true,
    border : false,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',forceFit:false},
	columns : [new Ext.grid.RowNumberer(),
	{
		id :'languagename',
		header : '语种名称',
	  	dataIndex :'languagename',
	  	width :100,
	 	sortable :true
	}, {
		id :'count',
		header : '报名人数',
	  	dataIndex :'count',
	  	width :100,
	 	sortable :true
	}],
	listeners: {
  	  afterRender:function(data){
  		examNumLanguageInfoStore.load();
  	  }
	},
	bbar:['->',{
		text:"上一步",
		handler: function(){
			switchForm(0,wizardItems);
			examNumEditWindow.doLayout();
		}}
	,{
		text:"开始编排",
		handler: function(){
			Ext.MessageBox.wait('考生准考证正在编排中，请耐心等待','提示');
			param=examNumEditParamForm.getForm().getValues();
			ExamNumEditController.startArrange(param,function(data){
				var jsonData=Ext.util.JSON.decode(data);
				if(jsonData.success==true)
				{
					Ext.MessageBox.alert('提示',jsonData.errors.info,function(){
						examNumEditWindow.hide();
						Ext.getCmp("ExamNumEditPagingToolbar").moveFirst();
					});
				}
				else
				{
					Ext.MessageBox.alert('提示',jsonData.errors.info);
				}
			});
		}
	}]
 });
//=========================
//wizardItems=[formItemSelector,examNumEditParamForm,examNumLanguageInfoGrid];
//wizardItems=[formItemSelector,examNumLanguageInfoGrid];
wizardItems=[examNumEditParamForm,examNumLanguageInfoGrid];
var examNumEditWindow = new Ext.Window({
	title: '准考证编排',
	width: 620,
	height:340,
	closeAction : 'hide',
	layout: 'fit',
	bodyStyle  : 'position:relative',
	border:false,
	modal: true,
	shadow: true,
	hideMode: Ext.isIE ? 'offsets' : 'display',
	plain:true,
	bodyStyle:'padding:5px;',
	buttonAlign:'center',
	items: wizardItems});


var ExamNumEditGrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'ExamNumEditGrid',
	store : ExamNumEditStore,
	title : '准考证安排表',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'paied',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer(),
	{
		id :'name',
		header : '考生姓名',
	  	dataIndex :'name',
	  	width :100,
	 	sortable :true
	}, {
		id :'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :100,
	 	sortable :true
	}, {
		id :'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :200,
	 	sortable :true
	}, {
		id :'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :200,
	 	sortable :true
	},{
		id:'campusname',
		header : '校区名称',
	  	dataIndex :'campusname',
	  	width :100,
	 	sortable :true
	},{
		id:'languagename',
		header : '语种名称',
	  	dataIndex :'languagename',
	  	width :200,
	 	sortable :true
	},{
		id:'paied',
		header : '是否缴费',
	  	dataIndex :'paied',
	  	width :200,
	 	sortable :true
	}
	],
     tbar : [{
                   text : '编排准考证',
                   tooltip : '编排准考证',
                   iconCls : 'edit',
                   onClick : function() {
                	   ExamNumEditController.validateBeforeArrange(function(data){
                		var jsonData=Ext.util.JSON.decode(data);
                   		if(jsonData.success==true){
                   			Ext.MessageBox.alert('数据校验成功',jsonData.errors.info,function(btn, text){
                   			    if (btn == 'ok'){
                   			    	switchForm(0,wizardItems);
                   			    	examNumEditWindow.doLayout();
                   			    	examNumEditWindow.show();}
                   			});
               	    	}else{
               	    		Ext.MessageBox.alert('提示',jsonData.errors.info);
               	    	}
                	   });
                   }
              },{
            	  text : '导出考生库(Excel)',
                  tooltip : '导出考生库',
                  iconCls : 'upload-icon',
                  onClick : function() {
                	  exportStudentsXls();
                  }
              },{
            	  text : '导出考生库(DBF)',
                  tooltip : '导出考生库',
                  iconCls : 'upload-icon',
                  onClick : function() {
                	  exportStudentsDBF();
                  }
              },{
            	  text : '查询',
                  tooltip : '查询',
                  iconCls : 'upload-icon',
                  onClick : function() {
      	        	student_searchWin.show();
    	        	student_searchform.getForm().reset();
                  }
              }] ,
      bbar : new Ext.PagingToolbar({
    	  id:"ExamNumEditPagingToolbar",
    	  pageSize:20,
    	  store : ExamNumEditStore,
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
      listeners: {
      	  afterRender:function(data){
      		  Ext.getCmp("ExamNumEditPagingToolbar").moveFirst();
      	  }
        }
 });

function exportStudentsXls(){
	var f = document.getElementById('exportStudentsExcel');
	f.action = '../exportStudentsExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}

function exportStudentsDBF(){
	var f = document.getElementById('exportStudentsDbf');
	f.action = '../exportStudentsDbf.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
	});
}

var examrommwin ;



function pageInit() {
	
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [ExamNumEditGrid],
		renderTo :Ext.getBody()
	});
}