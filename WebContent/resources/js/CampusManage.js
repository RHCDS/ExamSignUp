var campus = Ext.data.Record.create([{
			name : 'id'
		}, {
			name : 'name'
		}, {
			name : 'remark'
		}, {
			name : 'campusnum'
		}]);

var campusstore = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						totalProperty : 'totalProperty',
						root : 'root'
					}, campus),
			proxy : new Ext.ux.data.DWRProxy({
						dwrFunction : CampusController.paginationShow
					})
		});

var campusgrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'campusgrid',
	store : campusstore,
	title : '校区表',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'remark',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [new Ext.grid.RowNumberer(), {
				id : 'campusnum',
				header : '校区代码',
				dataIndex : 'campusnum',
				width : 300,
				sortable : true
			}, {
				id : 'name',
				header : '校区名称',
				dataIndex : 'name',
				width : 120,
				sortable : true
			}, {
				id : 'remark',
				header : '备注',
				dataIndex : 'remark',
				width : 150,
				sortable : true
			}],
	tbar : [{
				text : '新增',
				tooltip : '新增校区',
				iconCls : 'add',
				onClick : function() {
					var newcampus = new campus({
								id : '',
								exInstitution : '',
								name : '',
								remark : '',
								campusnum : ''
							});
					campusform.getForm().reset();
					campusWindowInit('新增校区').show();
					campusform.getForm().loadRecord(newcampus);
				}
			}, '-', {
				text : '修改',
				tooltip : '修改校区信息',
				iconCls : 'edit',
				onClick : function() {
					campusgrid.fireEvent('rowdblclick', campusgrid);
				}
			}, '-', {
				text : '删除',
				tooltip : '删除选中的校区',
				iconCls : 'remove',
				onClick : function() {
					if (campusgrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的校区么?',
								function(button) {
									if (button == 'yes') {
										var list = campusgrid
												.getSelectionModel()
												.getSelections();
										var rList = [];
										for (var i = 0; i < list.length; i++) {
											rList[i] = list[i].data["id"];
										}

										CampusController.deleteCampus(
												rList, function(data) {
													if (data) {
														Ext.MessageBox.alert(
																'提示',
																'删除校区成功!');
													} else {
														Ext.MessageBox.alert(
																'删除校区失败!',
																"请务必先删除隶属该校区的考生和考场！");
													}
													campusGridInit();
												});
									}
								});
					} else {
						Ext.MessageBox.alert('提示', "请至少选择一条记录!");
					}

				}
			}],
	bbar : new Ext.PagingToolbar({
				pageSize : 20,
				store : campusstore,
				displayInfo : true,
				firstText : '首页',
				lastText : '尾页',
				prevText : '上一页',
				nextText : '下一页',
				refreshText : '刷新',
				displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
				emptyMsg : "没有记录",
				doLoad : function(start) {
					var params = {};
					params.start = start;
					params.limit = this.pageSize;
					if (this.fireEvent("beforechange", this, params) !== false) {
						this.store.load({
									params : params
								});
					}
				}
			}),
	listeners : {
		rowdblclick : function(grid) {
			if (grid.getSelectionModel().hasSelection()) {
				campusWindowInit("编辑校区信息").show();
				campusform.getForm().setValues(grid.getSelectionModel()
						.getSelected().data);
			} else {
				Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
			}

		}
	}
});

var campusfields = [{
			columnWidth : .5,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'hidden',
						name : 'id',
						anchor : '98%'
					}, {
						xtype : 'numberfield',
						fieldLabel : '校区代码（1位）',
						name : 'campusnum',
						maxLength:1,
						anchor : '98%',
						allowBlank : false
					}]
		}, {
			columnWidth : .5,
			layout : 'form',
			labelWidth : 60,
			items : [{
						xtype : 'textfield',
						fieldLabel : '校区名称',
						name : 'name',
						anchor : '98%',
						allowBlank : false
					}]
		}, {
			columnWidth : 1,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'textarea',
						fieldLabel : '备注',
						name : 'remark',
						anchor : '98.65%'
					}]
		}];

var campusform = new Ext.form.FormPanel({
			labelAlign : 'left',
			layout : 'column',
			frame : true,
			border : false,
			bodyStyle : 'padding:5',
			items : [campusfields]
		});

var campuswin;

function campusWindowInit(title) {

	if (!campuswin) {
		var campuswin = new Ext.Window({
					title : '校区设置',
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
					items : [campusform],
					buttons : [{
						text : '保存',
						handler : function() {
							if (campusform.getForm().isValid()) {
								var campus = campusform.getForm()
										.getValues();
								CampusController.saveCampus(campus,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示',
														"保存成功！");
												campuswin.hide();
											} else
												Ext.MessageBox.alert('提示',
														"保存失败！");
											campusGridInit();
										});

							} else {
								Ext.MessageBox.alert('提示', "输入信息有误！");
							}
						}

					}, {
						text : '清空',
						handler : function() {
							campusform.getForm().reset();
						}
					}, {
						text : '关闭',
						handler : function() {
							campuswin.hide();
						}
					}]
				});

	}
	campuswin.setTitle(title);
	return campuswin;

}

function campusGridInit() {
	campusstore.load({
				params : {
					start : 0,
					limit : 20
				}
			});
}

function pageInit() {
	new Ext.Viewport({
				layout : 'border',
				hideMode : Ext.isIE ? 'offsets' : 'display',
				items : [campusgrid],
				renderTo : Ext.getBody()
			});
	campusGridInit();
}