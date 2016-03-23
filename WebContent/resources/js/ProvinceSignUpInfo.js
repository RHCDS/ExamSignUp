var parentinstitutionnum = "";
var selectedSchool = "000";
var parentinstitutionRecord = Ext.data.Record.create([ {
	name : 'institutionname'
}, {
	name : 'institutionnum'
} ]);

var parentinstitutionStore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root'
	}, parentinstitutionRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : InstitutionController.loadChildInstitution
	})
});


var institutionRecord = Ext.data.Record.create([ {
	name : 'institutionname'
}, {
	name : 'institutionnum'
} ]);

var institutionStore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root'
	}, institutionRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : InstitutionController.loadChildInstitution
	})
});
var studentgridofprvince = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentgridofprvince',
	title : '考生表',
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
/*		tbar:[signedCityCombo,'-',signedCityCombo,'-',{
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
	    }
    ],*/
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

var conditionfields = [{
	columnWidth : 0.13,
	anchor: '99%',
	layout : 'form',
	items : [{
		xtype : 'button',
		text : '导出所有学校考生库',
		handler : function(){
			exportAllSchoolDbf();
		}
	}]
	},{
	columnWidth : 0.25,
	layout : 'form',
	anchor: '99%',
	items : [{
		xtype : 'combo',
		fieldLabel : '市地考试院',
		name : 'signedCityCombo',
		id : 'signedCityCombo',
		mode : 'local',
		valueField : 'institutionnum',
		displayField : 'institutionname',
		allowBlank : false,
		editable : false,
		store : parentinstitutionStore,
		triggerAction : 'all',
		anchor : '98%', // anchor width by percentage
		listeners : {
			afterRender : function() {
				parentinstitutionStore.load({
					params : {
						institutionnum : parentinstitutionnum
					}
				});
			},
			select:function(){
				selectedSchool="000";
				if(parentinstitutionnum==this.getValue())
					return;
				parentinstitutionnum=this.getValue();
				institutionStore.load({
					params : {
						institutionnum : parentinstitutionnum
					}
				});
				
			  	}
			}
		}]
	},{
		columnWidth : 0.25,
		anchor: '99%',
		layout : 'form',
		labelWidth : 60,
		items : [{
			xtype : 'combo',
			fieldLabel : '学  校',
			id: 'signedSchoolCombo',
			name : 'signedSchoolCombo',
			mode : 'local',
			valueField : 'institutionnum',
			displayField : 'institutionname',
			allowBlank : false,
			editable : false,
			store : institutionStore,
			triggerAction : 'all',
			anchor : '98%', // anchor width by percentage
			listeners : {
				select:function(){
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
		}]
	},{
		columnWidth : 0.05,
		anchor: '99%',
		layout : 'form',
		items : [{
			xtype : 'button',
			text : '查询',
			handler : function(){
	        	student_searchWin.show();
	        	student_searchform.getForm().reset();
			}
		}]
	},{
		columnWidth : 0.07,
		anchor: '99%',
		layout : 'form',
		items : [{
			xtype : 'button',
			text : '语种统计',
			handler : function(){
	        	if(selectedSchool=="000")
	        		return;
	        	var params = {};
	        	params.institutionnum=signedLanguageCombo.getValue();
	        	examNumLanguageInfoStore.load({params:params});
	        	temp=examNumLanguageInfoStore.getTotalCount( ) ;
	  		  	examNumLanguageInfoWindow.show();
			}
		}]
	},{
		columnWidth : 0.1,
		anchor: '99%',
		layout : 'form',
		items : [{
			xtype : 'button',
			text : '导出考生库(DBF)',
			handler : function(){
				if(selectedSchool=="000")
	        		return;
				exportOneSchoolDbf();
			}
		}]
	}
];


var conditionform = new Ext.form.FormPanel({
	labelWidth : 80,
	labelAlign : 'left',
	region : 'north',
	layout : 'column',
	frame : true,
	height: 45,
	border : false,
	bodyStyle : 'padding:5',
	items : [ conditionfields ]
});
function exportOneSchoolDbf(){
	var f = document.getElementById('exportOneSchoolDbf');
	f.action = "../exportOneSchoolDbf.do?school=" + selectedSchool;
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
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

function provinceSignUpInfoInit(institution) {
	parentinstitutionnum = institution;
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [conditionform,studentgridofprvince],
		renderTo :Ext.getBody()
	});
//	alert(parentinstitutionnum);
	studentGridInit();
}

