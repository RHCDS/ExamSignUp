var student_currentSearchFilter = "";
var selectedSchool="000";
var schoolListRecord = Ext.data.Record.create([{
		name :'institutionnum'
	}, {
		name :'institutionname'
	}
]);
//语种Store
var schoolListStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root',
         idProperty:'institutionnum'
      }, schoolListRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : InstitutionController.loadManagedSchoolList
	  })
});

var signedLanguageCombo = new Ext.form.ComboBox({
	store: schoolListStore,
	mode: 'local',
	triggerAction: 'all',
	emptyText:'--请选择所管理的学校--',
	editable:false,
	valueField:'institutionnum',
	displayField:'institutionname',
	listEmptyText:'没有您管理的学校！',
	
	listeners : {
		afterRender : function(data) {
			selectedSchool="000";
			schoolListStore.load();
		},
		select:function(record,index){
			student_searchform.getForm().reset();
        	student_currentSearchFilter = "";
			if(selectedSchool==this.getValue())
				return;
			selectedSchool=this.getValue();
        	
			studentstore.load({
				params : {
					filter : student_currentSearchFilter,
					start : 0,
					limit : 20,
					institutionnum:selectedSchool
				}
			});
		}
	}
});
////////////////
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
     dwrFunction :ExamNumEditController.loadExamNumLanguageInfoByInstitutionnum
  })
});
var examNumLanguageInfoGrid = new Ext.grid.GridPanel({
	id : 'examNumLanguageInfoGrid',
	store : examNumLanguageInfoStore,
	title : '语种统计信息',
 loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'languagename',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
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
  		examNumLanguageInfoStore.load({
			params : {
				institutionnum : selectedSchool
			}
		});
  	  }}
 });
var  examNumLanguageInfoWindow = new Ext.Window({
   title: '',
    width: 450,
    height:300,
    closeAction : 'hide',
    layout: 'fit',
    bodyStyle  : 'position:relative',
	border:false,
	modal: true,
	shadow: true,
	hideMode: Ext.isIE ? 'offsets' : 'display',
    plain:true,
    bodyStyle:'padding:5px;',
    items: examNumLanguageInfoGrid});
/////////////
var student = new Ext.data.Record.create([
	{
		name:'id'
	},{
		name:'institutionName'
	},{
		name:'languageName'
	},{
		name:'name'
	},{
		name:'sex',convert:function(data){if(data=="M"){return "男";}else return "女";}
	},{
		name:'idnum'
	},{
		name:'examnum'
	}
]);

var emptystudent = new student({
	id : '',
	institutionName : '',
	languageName : '',
	name : '',
	sex : '',
	idnum : '',
	examnum : ''
});

var studentstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, student),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.ExamCollegeGetStudent
	  })
});

var studentgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentgrid',
	store :studentstore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer(),
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :200,
	 	sortable :true
	},	{
		id : 'institutionName',
		header : '学校',
	  	dataIndex :'institutionName',
	  	width :150,
	 	sortable :true
	},	{
		id : 'languageName',
		header : '语种',
	  	dataIndex :'languageName',
	  	width :150,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :200,
	 	sortable :true
	},{
		id : 'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :200,
	 	sortable :true
	}, {
		id : 'sex',
		header : '性别',
	  	dataIndex :'sex',
	  	width :50,
	 	sortable :true
	}
	],
		tbar:[signedLanguageCombo,'-',{
	        text: '查询',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	student_searchWin.show();
	        	student_searchform.getForm().reset();
	        }
	    },{
	        text : '按语种统计',
	        tooltip : '按照所选学校进行统计',
	        iconCls : 'data',
	        onClick : function(data){
	        	if(selectedSchool=="000")
	        		return;
	        	var params = {};
	        	params.institutionnum=signedLanguageCombo.getValue();
	        	examNumLanguageInfoStore.load({params:params});
	        	temp=examNumLanguageInfoStore.getTotalCount( ) ;
	  		  	examNumLanguageInfoWindow.show();
	  		  }
	    },{
	    	text: '导出所属学校考生库',
	    	tooltip: '导出所属学校考生库',
	    	iconCls: 'save',
	    	onClick: function(){
	    		exportAllSchoolDbf();
	    	}
	    }
    ],
	    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : studentstore,
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
  })
});

var progressbar = new Ext.ProgressBar({
	region : 'south',
	width : 300
});

function progressbarWaiting(){
	progressbar.wait({
		text : '正在执行，请耐心等候.....'
	});
}

var studentinfowin;

function studentInfoWinInit(title){
	if(!studentinfowin){
		studentinfowin = new Ext.Window({
   	        title: '',
   	        width: 1000,
   	        height:400,
   	        closeAction : 'hide',
   	        layout: 'fit',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        items: [studentform]
		});
	}
	loadLanguagesName();
	loadCampusList();
	studentinfowin.setTitle(title);
	return studentinfowin;
}

var student_searchform = new Ext.form.FormPanel({
	id:'student_searchform',
	region:'center',
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
	studentstore.load({
		params : {
			filter : student_currentSearchFilter,
			start : 0,
			limit : 20,
			institutionnum:selectedSchool
		}
	});
}
function exportAllSchoolDbf(){
	var f = document.getElementById('exportAllSchoolDbf');
	f.action = "../exportAllSchoolDbf.do";
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
	});
}


function studentGridInit(){
	studentstore.load({
		params : {
			filter : student_currentSearchFilter,
			start : 0,
			limit : 20,
			institutionnum:selectedSchool
		}
	});
}

function SchoolSignUpInfoInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [studentgrid],
		renderTo :Ext.getBody()
	});
	studentGridInit();
}
