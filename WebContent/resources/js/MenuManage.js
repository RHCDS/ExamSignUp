var MainMenu = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'menuid'
}, {
	name : 'showname'
}, {
	name : 'onclickscript'
}, {
	name : 'parentmenu'
}, {
	name : 'rolename'
}, {
	name : 'sortindex'
} ]);

var ExMainmenu = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'menuid'
}, {
	name : 'showname'
}, {
	name : 'onclickscript'
}, {
	name : 'exMainmenu'
}, {
	name : 'exRole'
}, {
	name : 'sortindex'
}, {
	name : 'exMainmenus'
} ]);

var menustore = new Ext.data.GroupingStore({
	id : 'GroupStore',
	reader : new Ext.data.JsonReader({
		fields : MainMenu
	}),
	groupField : 'parentmenu',
	sortInfo : {
		field : 'sortindex',
		drirection : 'ASC'
	},
	groupOnSort : false
});

var parentmenu = Ext.data.Record.create([ {
	name : 'id'
}, {
	name : 'menuid'
}, {
	name : 'showname'
}, {
	name : 'rolename'
}, {
	name : 'sortindex'
} ]);

var parentmenustore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty : 'menuid'
	}, parentmenu),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : MainMenuController.loadParentMenu
	})
});
var parentmenunameRecord = Ext.data.Record.create([ {
	name : 'showname'
}, {
	name : 'menuid'
} ]);
var parentmenunameStore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idproperty : 'menuid'
	}, parentmenunameRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : MainMenuController.loadParentMenuName
	})
});

var menugrid = new Ext.grid.GridPanel({
	id : 'menugrid',
	region : 'center',
	title : '菜单表',
	height : 300,
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'showname',
	columns : [ new Ext.grid.RowNumberer(), {
		header : "ID",
		width : 30,
		sortable : true,
		dataIndex : 'id',
		hidden : true
	}, {
		id : 'menuid',
		header : "菜单ID",
		width : 160,
		sortable : true,
		dataIndex : 'menuid'
	}, {
		id : 'showname',
		header : "菜单名称",
		Width : 200,
		sortable : true,
		dataIndex : 'showname'
	}, {
		header : "上级菜单",
		width : 160,
		sortable : true,
		dataIndex : 'parentmenu'
	}, {
		header : " 菜单脚本",
		width : 160,
		sortable : true,
		dataIndex : 'onclickscript'
	}, {
		header : " 角色",
		width : 60,
		sortable : true,
		dataIndex : 'rolename'
	}, {
		header : "序号",
		width : 60,
		sortable : true,
		dataIndex : 'sortindex'
	} ],
	store : menustore,
	view : new Ext.grid.GroupingView({
		sortAscText : '升序',
		sortDescText : '降序',
		groupByText : '使用当前字段进行分组',
		showGroupsText : '分组显示',
		showGroupName : true
	}),
	tbar : [
			{
				text : '新增',
				tooltip : '新增菜单项',
				iconCls : 'add',
				onClick : function() {
					var newmenu = new MainMenu({
						id : '',
						menuid : '',
						showname : '',
						onclickscript : '',
						parentmenu : '',
						rolename : '',
						sortindex : '999'
					});
					menuform.getForm().reset();
					menuWindowInit('新增菜单').show();
					menuform.getForm().loadRecord(newmenu);
				}
			},
			'-',
			{
				text : '修改',
				tooltip : '修改菜单项设置',
				iconCls : 'edit',
				onClick : function() {
					menugrid.fireEvent('rowdblclick', menugrid);
				}
			},
			'-',
			{
				text : '删除',
				tooltip : '删除选中的菜单项',
				iconCls : 'remove',
				onClick : function() {
					if (menugrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的菜单项么?',
							function(button) {
								if (button == 'yes') {
									var list = menugrid.getSelectionModel().getSelections();
									var rList = [];
									for ( var i = 0; i < list.length; i++) {
										rList[i] = list[i].data["id"];
									}
									MainMenuController.deleteMainMenu(rList, 
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
												initMenuGrid();
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
			} ],
	listeners : {
		rowdblclick : function(grid) {
			if (grid.getSelectionModel().hasSelection()) {
				menuWindowInit("编辑菜单信息").show();
				menuform.getForm().setValues(
					grid.getSelectionModel().getSelected().data);
			} else {
				Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
			}

		}
	}
});

var menuform = new Ext.form.FormPanel({
	baseCls : 'x-plain',
	labelWidth : 60,
	frame : true,
	defaultType : 'textfield',
	items : [ {
		fieldLabel : 'id',
		name : 'id',
		readOnly : true,
		hidden : true,
		hideLabel : true
	},
	{
		xtype : 'combo',
		fieldLabel : '一级菜单',
		name : 'parentmenu',
		mode : 'local',
		displayField : 'showname',
		allowBlank : false,
		store : parentmenunameStore,
		triggerAction : 'all',
		anchor : '100%', // anchor width by percentage
		listeners : {
			afterRender : function() {
				parentmenunameStore.load();
			}
		}
	}, {
		fieldLabel : '菜单ID',
		name : 'menuid',
		allowBlank : false,
		anchor : '100%' // anchor width by percentage
	}, {
		fieldLabel : '菜单名称',
		name : 'showname',
		allowBlank : false,
		anchor : '100%' // anchor width by percentage
	}, {
		xtype : 'combo',
		fieldLabel : '角色',
		name : 'rolename',
		displayField : 'rolename',
		store : [ '考生', '高校', '市考试院', '省考试院' ],
		allowBlank : false,
		triggerAction : 'all',
		anchor : '100%' // anchor width by percentage
	}, {
		fieldLabel : '菜单脚本',
		xtype : 'textarea',
		name : 'onclickscript',
		anchor : '100%' // anchor width by percentage
	}, {
		xtype : 'numberfield',
		allowBlank : false,
		fieldLabel : '序号',
		name : 'sortindex',
		anchor : '100%' // anchor width by percentage

	} ]
});

var menuwin;

function menuWindowInit(title) {

	if (!menuwin) {
		var menuwin = new Ext.Window({
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
			items : menuform,

			buttons : [
			{
				text : '保存',
				handler : function() {
					if (menuform.getForm().isValid()) {
						var mainmenu = menuform.getForm().getValues();
						MainMenuController.saveMainMenu(mainmenu,
							function(data) {
								if (data) {
									Ext.MessageBox.alert('提示',"保存成功！");
									menuwin.hide();
								} else
									Ext.MessageBox.alert('提示',"保存失败！");
									initMenuGrid();
								});
					} else {
						Ext.MessageBox.alert('提示', "输入信息有误！");
					}
			     }

			}, {
				text : '清空',
				handler : function() {
					menuform.getForm().reset();
				}
			}, {
				text : '关闭',
				handler : function() {
					menuwin.hide();
				}
			} ]
	    });

	}
	menuwin.setTitle(title);
	return menuwin;
}

var parentmenugrid = new Ext.grid.GridPanel(
		{
			id : 'parentmenugrid',
			title : '父级菜单表',
			region : 'south',
			height : 200,
			store : parentmenustore,
			autoExpandColumn : 'shownamep',
			columns : [ new Ext.grid.RowNumberer(), {
				header : "ID",
				width : 30,
				sortable : true,
				dataIndex : 'id',
				hidden : true
			}, {
				id : 'menuid',
				header : "菜单ID",
				width : 160,
				sortable : true,
				dataIndex : 'menuid'
			}, {
				id : 'shownamep',
				header : "菜单名称",
				Width : 200,
				sortable : true,
				dataIndex : 'showname'
			}, {
				header : " 角色",
				width : 60,
				sortable : true,
				dataIndex : 'rolename'
			}, {
				header : "序号",
				width : 60,
				sortable : true,
				dataIndex : 'sortindex'
			} ],
			tbar : [
			{
				text : '新增',
				tooltip : '新增菜单项',
				iconCls : 'add',
				onClick : function() {
					var newmenu = new MainMenu({
						id : '',
						menuid : '',
						showname : '',
						onclickscript : '',
						parentmenu : '',
						rolename : '',
						sortindex : '999'
					});
					parentmenuform.getForm().reset();
					parentMenuWindowInit('新增菜单').show();
					parentmenuform.getForm().loadRecord(newmenu);
				}
			},
			'-',
			{
				text : '修改',
				tooltip : '修改菜单项设置',
				iconCls : 'edit',
				onClick : function() {
					parentmenugrid.fireEvent('rowdblclick',parentmenugrid);
				}
			},
			'-',
			{
				text : '删除',
				tooltip : '删除选中的菜单项',
				iconCls : 'remove',
				onClick : function() {
					if (parentmenugrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示','你确定要删除选中的菜单项么?',
							function(button) {
								if (button == 'yes') {
									var list = parentmenugrid.getSelectionModel().getSelections();
									var rList = [];
									for ( var i = 0; i < list.length; i++) {
										rList[i] = list[i].data["id"];
									}
									MainMenuController.deleteMainMenu(rList,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示','删除菜单项成功!');
											} else {
												Ext.MessageBox.alert('提示',"删除菜单项失败,请先删除该菜单下的子菜单！");
											}
											parentmenustore.reload();
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
						parentMenuWindowInit("编辑菜单信息").show();
						parentmenuform.getForm().setValues(
								grid.getSelectionModel().getSelected().data);
					} else {
						Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
					}

				}
			}
		});

parentmenufields = [ {
	columnWidth : 1,
	layout : 'form',
	items : [ {
		xtype : 'hidden',
		name : 'id',
		anchor : '98%'
	}, {
		xtype : 'textfield',
		fieldLabel : '菜单ID',
		name : 'menuid',
		anchor : '99%',
		allowBlank : false
	} ]
}, {
	columnWidth : 1,
	layout : 'form',
	items : [ {
		xtype : 'textfield',
		fieldLabel : '菜单名称',
		name : 'showname',
		anchor : '99%',
		allowBlank : false
	} ]
}, {
	columnWidth : .5,
	layout : 'form',
	items : [ {
		xtype : 'combo',
		fieldLabel : '角  色',
		name : 'rolename',
		store : [ '考生', '高校', '市考试院', '省考试院' ,'管理员'],
		editable : false,
		triggerAction : 'all',
		anchor : '98%',
		allowBlank : false
	} ]
}, {
	columnWidth : .5,
	layout : 'form',
	items : [ {
		fieldLabel : '序  号',
		name : 'sortindex',
		xtype : 'numberfield',
		editable : false,
		allowBlank : false,
		anchor : '98%'
	} ]
} ];

var parentmenuform = new Ext.form.FormPanel({
	labelWidth : 60,
	labelAlign : 'left',
	layout : 'column',
	frame : true,
	border : false,
	bodyStyle : 'padding:5',
	items : [ parentmenufields ]
});

var parentmenuwin;

function parentMenuWindowInit(title) {

	if (!parentmenuwin) {
		var parentmenuwin = new Ext.Window({
			title : '',
			width : 480,
			height : 200,
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
			items : [ parentmenuform ],

			buttons : [
			{
				text : '保存',
				handler : function() {
					if (parentmenuform.getForm().isValid()) {
						var mainmenu = parentmenuform.getForm().getValues();
						MainMenuController.saveParentMenu(mainmenu,function(data) {
							var jsonData = Ext.util.JSON.decode(data);
							if (jsonData.success == true) {
								Ext.MessageBox.alert('提示',jsonData.errors.info);
								parentmenuwin.hide();
							} else
								Ext.MessageBox.alert('提示',jsonData.errors.info);
							initParentMenuGrid();
							parentmenuwin.hide();
						});

					} else {
						Ext.MessageBox.alert('提示', "输入信息有误！");
					}
				}

			}, {
				text : '清空',
				handler : function() {
					parentmenuform.getForm().reset();
				}
			}, {
				text : '关闭',
				handler : function() {
					parentmenuwin.hide();
				}
			} ]
		});
	}
	parentmenuwin.setTitle(title);
	return parentmenuwin;
}
// 获取菜单表信息
function initMenuGrid() {
	MainMenuController.findAllMenuByGrouping(function(list) {
		if (list) {
			menustore.loadData(list);
		}
	});
}
// 获取父级菜单表信息
function initParentMenuGrid() {
	/*
	 * MainMenuController.findParentMenu(function(list){ if(list){
	 * parentmenustore.loadData(list); } });
	 */
	parentmenustore.load();
}

// 菜单配置页面初始化
function MenuManagePageInit() {
	new Ext.Viewport({
		layout : 'border',
		hideMode : Ext.isIE ? 'offsets' : 'display',
		items : [ menugrid, parentmenugrid ],
		renderTo : Ext.getBody()
	});
	initMenuGrid();
	initParentMenuGrid();
}
