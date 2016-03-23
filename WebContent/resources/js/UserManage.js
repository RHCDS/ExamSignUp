var parentinstitutionnum = "";

var schooluser = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'institution'
}, {
	name : 'institutionnum'
},{
	name : 'loginname'
}, {
	name : 'password'
}, {
	name : 'name'
}, {
	name : 'contact'
} ,{
	name : 'parentinstitution'
}]);



var schooluserstore = new Ext.data.GroupingStore({
	id : 'schooluserstore',
	reader : new Ext.data.JsonReader({
		fields : schooluser
	}),
	groupField : 'parentinstitution',
	sortInfo : {
		field : 'loginname',
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

var schoolusergrid = new Ext.grid.GridPanel({
	id : 'schoolusergrid',
	region : 'center',
	title : '高校用户表',
//	height : 300,
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'institution',
	columns : [ new Ext.grid.RowNumberer(), {
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
		id : 'institution',
		header : "学校名称",
		width : 160,
		sortable : true,
		dataIndex : 'institution'
	}, {
		id : 'loginname',
		header : "登录帐号",
		Width : 250,
		sortable : true,
		dataIndex : 'loginname'
	}, {
		header : " 联系人",
		width : 200,
		dataIndex : 'name'
	}, {
		header : " 联系电话",
		width : 200,
		dataIndex : 'contact'
	}, {
		header : "隶属考试院",
		width : 150,
		sortable : true,
		dataIndex : 'parentinstitution'
	} ],
	store : schooluserstore,
	view : new Ext.grid.GroupingView({
		sortAscText : '升序',
		sortDescText : '降序',
		groupByText : '使用当前字段进行分组',
		showGroupsText : '分组显示',
		showGroupName : true
	}),
	tbar : [
			{
				text : '新增用户',
				tooltip : '新增',
				iconCls : 'add',
				onClick : function() {
					var newschooluser = new schooluser({
						id : '',
						institution : '',
						loginname : '',
						password : '',
						name : '',
						contact : '',
						parentinstitution : ''
					});
					schooluserform.getForm().reset();
					schooluserWindowInit('新增高校用户').show();
					schooluserform.getForm().loadRecord(newschooluser);
				}
			},
			'-',
			{
				text : '修改',
				tooltip : '修改',
				iconCls : 'edit',
				onClick : function() {
					schoolusergrid.fireEvent('rowdblclick', schoolusergrid);
				}
			},
			'-',
			{
				text : '删除',
				tooltip : '删除选中的菜单项',
				iconCls : 'remove',
				onClick : function() {
					if (schoolusergrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的菜单项么?',
							function(button) {
								if (button == 'yes') {
									var list = schoolusergrid.getSelectionModel().getSelections();
									var rList = [];
									for ( var i = 0; i < list.length; i++) {
										rList[i] = list[i].data["id"];
									}
									UserController.deleteUsers(rList, 
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
											} else {
												Ext.MessageBox.alert('提示',"删除菜单项失败!");
											}
											refreshSchoolUserGrid();
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
				schooluserWindowInit("编辑高校用户信息").show();
				schooluserform.getForm().setValues(
					grid.getSelectionModel().getSelected().data);
			} else {
				Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
			}

		},
		afterRender : function() {
			UserController.getAllUserByType("school",function(list){
				if(list.length>0){
					schooluserstore.loadData(list);
				}else{
					Ext.MessageBox.alert('提示', "没有任何高校用户!");
				}
			});
}
	}
	
});

function refreshSchoolUserGrid(){
	UserController.getAllUserByType("school",function(list){
		if(list.length>0){
			schooluserstore.loadData(list);
		}else{
			Ext.MessageBox.alert('提示', "没有任何高校用户!");
		}
	});
}

var schooluserfields = [{	
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
		name : 'parentinstitution',
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
	}, {
		xtype : 'combo',
		fieldLabel : '高校名称',
		name : 'institution',
		mode : 'local',
		displayField : 'institutionname',
		allowBlank : false,
		editable : false,
		store : institutionStore,
		triggerAction : 'all',
		anchor : '98%' // anchor width by percentage
//		listeners : {
//			afterRender : function() {
//				parentmenunameStore.load();
//			}
//		}
	}, {
		fieldLabel : '登录帐号',
		name : 'loginname',
		xtype : 'textfield',
		allowBlank : false,
		anchor : '98%' // anchor width by percentage
	}, {
		fieldLabel : '密      码',
		name : 'password',
		id : 'password',
		xtype : 'textfield',
		allowBlank : false,
		anchor : '98%' // anchor width by percentage
	}, {
		fieldLabel : '联系人（多个）',
		name : 'name',
		xtype : 'textfield',
		allowBlank : false,
		anchor : '98%' // anchor width by percentage
	}, {
		xtype : 'textarea',
		allowBlank : false,
		fieldLabel : '联系电话（多个）',
		name : 'contact',
		anchor : '98%' // anchor width by percentage

	}]
} ];

var schooluserform = new Ext.form.FormPanel({
	labelWidth : 110,
	labelAlign : 'left',
	layout : 'column',
	frame : true,
	border : false,
	bodyStyle : 'padding:5',
	items : [schooluserfields]
});

var schooluserwin;

function schooluserWindowInit(title) {

	if (!schooluserwin) {
		var schooluserwin = new Ext.Window({
			title : '菜单项设置',
			width : 480,
			height : 360,
			minWidth : 400,
			minHeight : 360,
			closeAction : 'hide',
			layout : 'fit',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : [schooluserform],

			buttons : [
			{
				text : '保存',
				handler : function() {
					if (schooluserform.getForm().isValid()) {
						var schooluser = schooluserform.getForm().getValues();
						var password=hex_md5(Ext.getCmp("password").getValue());
						schooluser.password = password;
						UserController.saveUser(schooluser,"school",
							function(data) {
								if (data=="保存成功！") {
									Ext.MessageBox.alert('提示',"保存成功！");
									schooluserwin.hide();
									refreshSchoolUserGrid();
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
					schooluserform.getForm().reset();
				}
			}, {
				text : '关闭',
				handler : function() {
					schooluserwin.hide();
				}
			} ]
	    });

	}
	schooluserwin.setTitle(title);
	return schooluserwin;
}

function schoolUserManageInit(institution){
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [schoolusergrid],
		renderTo :Ext.getBody()
	});
	parentinstitutionnum = institution;
}

///////////////////////////////////////////////////
/////////////////市地考试院用户管理//////////////////////
///////////////////////////////////////////////////
///////////////////////////////////////////////////

var cityuser = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'role'
}, {
	name : 'institution'
}, {
	name : 'loginname'
}, {
	name : 'password'
}, {
	name : 'name'
}, {
	name : 'contact'
} ]);

var cityuserstore = new Ext.data.JsonStore({
	fields : cityuser
});

var cityusergrid = new Ext.grid.GridPanel(
		{
			id : 'cityusergrid',
			title : '市地考试院用户表',
			region : 'center',
			store : cityuserstore,
			loadMask : true,
			stripeRows : true,
			autoScroll : true,
			viewConfig : {
				sortDescText : '降序',
				sortAscText : '升序',
				columnsText : '显示列',
				forceFit : false
			},
			autoExpandColumn : 'institution',
			columns : [ new Ext.grid.RowNumberer(), {
				header : "ID",
				width : 30,
				sortable : true,
				dataIndex : 'id',
				hidden : true
			}, {
				id : 'institution',
				header : "考试院名称",
				width : 160,
				sortable : true,
				dataIndex : 'institution'
			}, {
//				id : 'shownamep',
				header : "登录帐号",
				Width : 200,
				sortable : true,
				dataIndex : 'loginname'
			}, {
				header : " 联系人",
				width : 150,
				dataIndex : 'name'
			}, {
				header : "联系电话",
				width : 150,
				dataIndex : 'contact'
			} ],
			tbar : [
			{
				text : '新增',
				tooltip : '新增菜单项',
				iconCls : 'add',
				onClick : function() {
					var newcityuser = new cityuser({
						id : '',
						institution : '',
						loginname : '',
						password : '',
						name : '',
						contact : ''
					});
					cityuserform.getForm().reset();
					cityuserWindowInit('新增用户').show();
					cityuserform.getForm().loadRecord(newcityuser);
				}
			},
			'-',
			{
				text : '修改',
				tooltip : '修改菜单项设置',
				iconCls : 'edit',
				onClick : function() {
					cityusergrid.fireEvent('rowdblclick',cityusergrid);
				}
			},
			'-',
			{
				text : '删除',
				tooltip : '删除选中的菜单项',
				iconCls : 'remove',
				onClick : function() {
					if (cityusergrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示','你确定要删除选中的菜单项么?',
							function(button) {
								if (button == 'yes') {
									var list = cityusergrid.getSelectionModel().getSelections();
									var rList = [];
									for ( var i = 0; i < list.length; i++) {
										rList[i] = list[i].data["id"];
									}
									UserController.deleteUsers(rList,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
											} else {
												Ext.MessageBox.alert('提示',"删除菜单项失败,请先删除该菜单下的子菜单！");
											}
											cityUserGridInit();
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
						cityuserWindowInit("编辑用户").show();
						cityuserform.getForm().setValues(
								grid.getSelectionModel().getSelected().data);
					} else {
						Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
					}

				},
				afterRender : function() {
					UserController.getAllUserByType("city",function(list){
						if(list.length>0){
							cityuserstore.loadData(list);
						}else{
							Ext.MessageBox.alert('提示', "没有任何考试院用户!");
						}
					});
					}
			}
});

cityuserfields = [ {
	columnWidth : 1,
	layout : 'form',
	items : [ {
		xtype : 'hidden',
		name : 'id',
		anchor : '98%'
	}, {
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
		},  {
			fieldLabel : '登录帐号',
			name : 'loginname',
			xtype : 'textfield',
			allowBlank : false,
			anchor : '98%' // anchor width by percentage
		}, {
			fieldLabel : '密      码',
			name : 'password',
			xtype : 'textfield',
			id : 'passwordforcity',
			allowBlank : false,
			anchor : '98%' // anchor width by percentage
		}, {
			fieldLabel : '联系人（多个）',
			name : 'name',
			xtype : 'textfield',
			allowBlank : false,
			anchor : '98%' // anchor width by percentage
		}, {
			xtype : 'textarea',
			allowBlank : false,
			fieldLabel : '联系电话（多个）',
			name : 'contact',
			anchor : '98%' // anchor width by percentage
		} ]
}];

var cityuserform = new Ext.form.FormPanel({
	labelWidth : 110,
	labelAlign : 'left',
	layout : 'column',
	frame : true,
	border : false,
	bodyStyle : 'padding:5',
	items : [ cityuserfields ]
});

var cityuserwin;

function cityuserWindowInit(title) {

	if (!cityuserwin) {
		var cityuserwin = new Ext.Window({
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
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : [ cityuserform ],
			buttons : [
			{
				text : '保存',
				handler : function() {
					if (cityuserform.getForm().isValid()) {
						var cityuser = cityuserform.getForm().getValues();
						var password=hex_md5(Ext.getCmp("passwordforcity").getValue());
						cityuser.password = password;
						cityuser.parentinstitution = "浙江省考试院";
						UserController.saveUser(cityuser,"cityManager",function(data) {
							if (data=="保存成功！") {
								Ext.MessageBox.alert('提示',"保存成功！");
								cityuserwin.hide();
								cityUserGridInit();
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
					cityuserform.getForm().reset();
				}
			}, {
				text : '关闭',
				handler : function() {
					cityuserwin.hide();
				}
			} ]
		});
	}
	cityuserwin.setTitle(title);
	return cityuserwin;
}

function cityUserGridInit() {
		UserController.getAllUserByType("city",function(list){
			if(list.length>0){
				cityuserstore.loadData(list);
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
function cityUserPageInit(institution) {
	new Ext.Viewport({
		layout : 'border',
		hideMode : Ext.isIE ? 'offsets' : 'display',
		items : [cityusergrid],
		renderTo : Ext.getBody()
	});
	parentinstitutionnum = institution;
//	initMenuGrid();
//	initParentMenuGrid();
}
