var parentinstitutionnum = "";

var school = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'higherInstitution'
}, {
	name : 'institutionnum'
},{
	name : 'name'
}, {
	name : 'category'
}, {
	name : 'contact'
}, {
	name : 'remark'
} ,{
	name : 'approvalstatus'
}]);



var schoolstore = new Ext.data.GroupingStore({
	id : 'schoolstore',
	reader : new Ext.data.JsonReader({
		fields : school
	}),
	groupField : 'higherInstitution',
	sortInfo : {
		field : 'institutionnum',
		drirection : 'ASC'
	},
	groupOnSort : false
});


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

var schoolgrid = new Ext.grid.GridPanel({
	id : 'schoolgrid',
	region : 'center',
	title : '高校表',
//	height : 300,
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'name',
	columns : [ new Ext.grid.RowNumberer({width:28}), {
		header : "ID",
		width : 30,
		sortable : true,
		dataIndex : 'id',
		hidden : true
	}, {
		header : "学校代码",
		width : 80,
		sortable : true,
		dataIndex : 'institutionnum'
	},{
		id : 'name',
		header : "学校名称",
		width : 160,
		sortable : true,
		dataIndex : 'name'
	},  {
		header : " 联系方式（多人）",
		width : 200,
		dataIndex : 'contact'
	}, {
		header : " 备注",
		width : 200,
		dataIndex : 'remark'
	}, {
		header : "隶属考试院",
		width : 150,
		sortable : true,
		dataIndex : 'higherInstitution'
	} ],
	store : schoolstore,
	view : new Ext.grid.GroupingView({
		sortAscText : '升序',
		sortDescText : '降序',
		groupByText : '使用当前字段进行分组',
		showGroupsText : '分组显示',
		showGroupName : true
	}),
	tbar : [
			{
				text : '新增高校',
				tooltip : '新增',
				iconCls : 'add',
				onClick : function() {
					var newschool = new school({
						id : '',
						institution : '',
						loginname : '',
						password : '',
						name : '',
						contact : '',
						parentinstitution : ''
					});
					schoolform.getForm().reset();
					schoolWindowInit('新增高校').show();
					schoolform.getForm().loadRecord(newschool);
				}
			},
			'-',
			{
				text : '修改',
				tooltip : '修改',
				iconCls : 'edit',
				onClick : function() {
					schoolgrid.fireEvent('rowdblclick', schoolgrid);
				}
			},
			'-',
			{
				text : '删除',
				tooltip : '删除选中的菜单项',
				iconCls : 'remove',
				onClick : function() {
					if (schoolgrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的菜单项么?',
							function(button) {
								if (button == 'yes') {
									var list = schoolgrid.getSelectionModel().getSelections();
									var rList = [];
									for ( var i = 0; i < list.length; i++) {
										rList[i] = list[i].data["id"];
									}
									InstitutionController.deleteInstitutions(rList, 
										function(data) {
											if (data=="删除成功！") {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
											} else {
												Ext.MessageBox.alert('删除失败！',data);
											}
											refreshSchoolGrid();
									});
								}
						});
					} else {
						Ext.MessageBox.alert('提示', "请至少选择一条记录!");
					}
					
				}
			} ],
	listeners : {
		rowdblclick : function(grid) {
			if (grid.getSelectionModel().hasSelection()) {
				schoolWindowInit("编辑高校信息").show();
				schoolform.getForm().setValues(
					grid.getSelectionModel().getSelected().data);
			} else {
				Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
			}

		},
		afterRender : function() {
			InstitutionController.getInstitutionByType("school", function(list){
				if(list.length>0){
					schoolstore.loadData(list);
				}else{
					Ext.MessageBox.alert('提示', "没有任何高校!");
				}
			});
}
	}
	
});

function refreshSchoolGrid(){
	InstitutionController.getInstitutionByType("school", function(list){
		if(list.length>0){
			schoolstore.loadData(list);
		}else{
			Ext.MessageBox.alert('提示', "没有任何高校!");
		}
	});
}

var schoolfields = [{	
	columnWidth : 1,
	layout : 'form',
	items : [{
		xtype : 'hidden',
		name : 'id',
		anchor : '98%'
	},
	{
		xtype : 'combo',
		fieldLabel : '隶属考试院',
		name : 'higherInstitution',
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
			}
		}
	},{
		fieldLabel : '学校代码',
		name : 'institutionnum',
		xtype : 'textfield',
		allowBlank : false,
		anchor : '98%' // anchor width by percentage
	}, {
		fieldLabel : '学校名称',
		name : 'name',
		id : 'name',
		xtype : 'textfield',
		allowBlank : false,
		anchor : '98%' // anchor width by percentage
	}, {
		fieldLabel : '联系方式（多个）',
		name : 'contact',
		xtype : 'textfield',
		allowBlank : true,
		anchor : '98%' // anchor width by percentage
	}, {
		xtype : 'textarea',
		allowBlank : true,
		fieldLabel : '备    注',
		name : 'remark',
		anchor : '98%' // anchor width by percentage

	}]
} ];

var schoolform = new Ext.form.FormPanel({
	labelWidth : 110,
	labelAlign : 'left',
	layout : 'column',
	frame : true,
	border : false,
	bodyStyle : 'padding:5',
	items : [schoolfields]
});

var schoolwin;

function schoolWindowInit(title) {

	if (!schoolwin) {
		var schoolwin = new Ext.Window({
			title : '菜单项设置',
			width : 480,
			height : 360,
			closeAction : 'hide',
			layout : 'fit',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : [schoolform],

			buttons : [
			{
				text : '保存',
				handler : function() {
					if (schoolform.getForm().isValid()) {
						var school = schoolform.getForm().getValues();
						InstitutionController.saveInstitution(school,"school",
							function(data) {
								if (data=="保存成功！") {
									Ext.MessageBox.alert('提示',"保存成功！");
									schoolwin.hide();
									refreshSchoolGrid();
								} else
									Ext.MessageBox.alert('提示',data);
								
								
								});
					} else {
						Ext.MessageBox.alert('提示', "输入信息有误！");
					}
			     }

			}, {
				text : '清空',
				handler : function() {
					schoolform.getForm().reset();
				}
			}, {
				text : '关闭',
				handler : function() {
					schoolwin.hide();
				}
			} ]
	    });

	}
	schoolwin.setTitle(title);
	return schoolwin;
}

function schoolManageInit(institution){
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [schoolgrid],
		renderTo :Ext.getBody()
	});
	parentinstitutionnum = institution;
}

///////////////////////////////////////////////////
/////////////////市地考试院管理//////////////////////
///////////////////////////////////////////////////
///////////////////////////////////////////////////

var cityinstitution = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'higherInstitution'
}, {
	name : 'institutionnum'
},{
	name : 'name'
}, {
	name : 'category'
}, {
	name : 'contact'
}, {
	name : 'remark'
} ,{
	name : 'approvalstatus'
} ]);

var cityinstitutionstore = new Ext.data.JsonStore({
	fields : cityinstitution
});

var cityinstitutiongrid = new Ext.grid.GridPanel(
		{
			id : 'cityinstitutiongrid',
			title : '市地考试院表',
			region : 'center',
			store : cityinstitutionstore,
			loadMask : true,
			stripeRows : true,
			autoScroll : true,
			viewConfig : {
				sortDescText : '降序',
				sortAscText : '升序',
				columnsText : '显示列',
				forceFit : false
			},
			autoExpandColumn : 'name',
			columns : [ new Ext.grid.RowNumberer(), {
				header : "ID",
				width : 30,
				sortable : true,
				dataIndex : 'id',
				hidden : true
			}, {
				header : "考试院代码",
				width : 80,
				sortable : true,
				dataIndex : 'institutionnum'
			},{
				id : 'name',
				header : "考试院名称",
				width : 160,
				sortable : true,
				dataIndex : 'name'
			},  {
				header : " 联系方式（多人）",
				width : 200,
				dataIndex : 'contact'
			}, {
				header : " 备注",
				width : 200,
				dataIndex : 'remark'
			}],
			tbar : [
			{
				text : '新增',
				tooltip : '新增菜单项',
				iconCls : 'add',
				onClick : function() {
					var newcityinstitution = new cityinstitution({
						id : '',
						institution : '',
						loginname : '',
						password : '',
						name : '',
						contact : '',
						higherInstitution : '浙江省考试院'
					});
					cityinstitutionform.getForm().reset();
					cityinstitutionWindowInit('新增考试院').show();
					cityinstitutionform.getForm().loadRecord(newcityinstitution);
				}
			},
			'-',
			{
				text : '修改',
				tooltip : '修改菜单项设置',
				iconCls : 'edit',
				onClick : function() {
					cityinstitutiongrid.fireEvent('rowdblclick',cityinstitutiongrid);
				}
			},
			'-',
			{
				text : '删除',
				tooltip : '删除选中的菜单项',
				iconCls : 'remove',
				onClick : function() {
					if (cityinstitutiongrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示','你确定要删除选中的菜单项么?',
							function(button) {
								if (button == 'yes') {
									var list = cityinstitutiongrid.getSelectionModel().getSelections();
									var rList = [];
									for ( var i = 0; i < list.length; i++) {
										rList[i] = list[i].data["id"];
									}
									InstitutionController.deleteInstitutions(rList,
										function(data) {
											if (data=="删除成功！") {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
											} else {
												Ext.MessageBox.alert('删除菜单项失败',data);
											}
											cityInstitutionGridInit();
									});
								}
							});
					} else {
						Ext.MessageBox.alert('提示', "请至少选择一条记录!");
					}
				}
			} ],
			listeners : {
				rowdblclick : function(grid) {
					if (grid.getSelectionModel().hasSelection()) {
						cityinstitutionWindowInit("编辑考试院信息").show();
						cityinstitutionform.getForm().setValues(
								grid.getSelectionModel().getSelected().data);
					} else {
						Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
					}

				},
				afterRender : function() {
					InstitutionController.getInstitutionByType("city",function(list){
						if(list.length>0){
							cityinstitutionstore.loadData(list);
						}else{
							Ext.MessageBox.alert('提示', "没有任何考试院用户!");
						}
					});
					}
			}
});

cityinstitutionfields = [ {
	columnWidth : 1,
	layout : 'form',
	items : [ {
		xtype : 'hidden',
		name : 'id',
		anchor : '98%'
	}, /*{
		xtype : 'combo',
		fieldLabel : '考试院名称',
		name : 'institution',
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
			}
		}
		}, */ {
			fieldLabel : '考试院代码',
			name : 'institutionnum',
			xtype : 'textfield',
			allowBlank : false,
			anchor : '98%' // anchor width by percentage
		}, {
			fieldLabel : '考试院名称',
			name : 'name',
			xtype : 'textfield',
			allowBlank : false,
			anchor : '98%' // anchor width by percentage
		}, {
			fieldLabel : '联系人（多个）',
			name : 'contact',
			xtype : 'textfield',
			allowBlank : true,
			anchor : '98%' // anchor width by percentage
		}, {
			xtype : 'textarea',
			allowBlank : true,
			fieldLabel : '备注',
			name : 'remark',
			anchor : '98%' // anchor width by percentage
		} ]
}];

var cityinstitutionform = new Ext.form.FormPanel({
	labelWidth : 110,
	labelAlign : 'left',
	layout : 'column',
	frame : true,
	border : false,
	bodyStyle : 'padding:5',
	items : [ cityinstitutionfields ]
});

var cityinstitutionwin;

function cityinstitutionWindowInit(title) {

	if (!cityinstitutionwin) {
		var cityinstitutionwin = new Ext.Window({
			title : '',
			width : 480,
			height : 300,
			closeAction : 'hide',
			layout : 'fit',
			border : false,
			modal : true,
			shadow : true,
			hideMode : Ext.isIE ? 'offsets' : 'display',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : [ cityinstitutionform ],
			buttons : [
			{
				text : '保存',
				handler : function() {
					if (cityinstitutionform.getForm().isValid()) {
						var cityinstitution = cityinstitutionform.getForm().getValues();
						cityinstitution.higherInstitution = "浙江省考试院";
						InstitutionController.saveInstitution(cityinstitution,"city",function(data) {
							if (data=="保存成功！") {
								Ext.MessageBox.alert('提示',"保存成功！");
								cityinstitutionwin.hide();
								cityInstitutionGridInit();
							} else
								Ext.MessageBox.alert('提示',data);
							
						});

					} else {
						Ext.MessageBox.alert('提示', "输入信息有误！");
					}
				}

			}, {
				text : '清空',
				handler : function() {
					cityinstitutionform.getForm().reset();
				}
			}, {
				text : '关闭',
				handler : function() {
					cityinstitutionwin.hide();
				}
			} ]
		});
	}
	cityinstitutionwin.setTitle(title);
	return cityinstitutionwin;
}

function cityInstitutionGridInit() {
	InstitutionController.getInstitutionByType("city",function(list){
		if(list.length>0){
			cityinstitutionstore.loadData(list);
		}else{
			Ext.MessageBox.alert('提示', "没有任何考试院用户!");
		}
	});
}
// 获取父级菜单表信息
//function initParentMenuGrid() {
//	/*
//	 * schooluserController.findParentMenu(function(list){ if(list){
//	 * parentschooluserstore.loadData(list); } });
//	 */
//	cityuserstore.load();
//}

// 菜单配置页面初始化
function cityInstitutionPageInit(institution) {
	new Ext.Viewport({
		layout : 'border',
		hideMode : Ext.isIE ? 'offsets' : 'display',
		items : [cityinstitutiongrid],
		renderTo : Ext.getBody()
	});
	parentinstitutionnum = institution;
//	initMenuGrid();
//	initParentMenuGrid();
}
