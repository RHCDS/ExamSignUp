var college = Ext.data.Record.create([{
			name : 'id'
		}, {
			name : 'name'
		}, {
			name : 'remark'
		}, {
			name : 'collegenum'
		}]);

var collegestore = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						totalProperty : 'totalProperty',
						root : 'root'
					}, college),
			proxy : new Ext.ux.data.DWRProxy({
						dwrFunction : CollegeController.paginationShow
					})
		});

var collegegrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'collegegrid',
	store : collegestore,
	title : '院系表',
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
				id : 'collegenum',
				header : '院系代号',
				dataIndex : 'collegenum',
				width : 300,
				sortable : true
			}, {
				id : 'name',
				header : '院系名',
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
				tooltip : '新增院系',
				iconCls : 'add',
				onClick : function() {
					var newcollege = new college({
								id : '',
								institutionid : '',
								name : '',
								remark : '',
								collegenum : ''
							});
					collegeform.getForm().reset();
					collegeWindowInit('新增院系').show();
					collegeform.getForm().loadRecord(newcollege);
				}
			}, '-', {
				text : '修改',
				tooltip : '修改院系信息',
				iconCls : 'edit',
				onClick : function() {
					collegegrid.fireEvent('rowdblclick', collegegrid);
				}
			}, '-', {
				text : '删除',
				tooltip : '删除选中的院系',
				iconCls : 'remove',
				onClick : function() {
					if (collegegrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的院系么?',
								function(button) {
									if (button == 'yes') {
										var list = collegegrid
												.getSelectionModel()
												.getSelections();
										var rList = [];
										for (var i = 0; i < list.length; i++) {
											rList[i] = list[i].data["id"];
										}

										CollegeController.deleteCollege(
												rList, function(data) {
													if (data) {
														Ext.MessageBox.alert(
																'提示',
																'删除院系成功!');
													} else {
														Ext.MessageBox.alert(
																'删除院系失败!',
																"请务必先删除隶属该院系的所有考生！");
													}
													collegeGridInit();
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
				store : collegestore,
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
				collegeWindowInit("编辑院系信息").show();
				collegeform.getForm().setValues(grid.getSelectionModel()
						.getSelected().data);
			} else {
				Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
			}

		}
	}
});

var collegefields = [{
			columnWidth : .5,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'hidden',
						name : 'id',
						anchor : '98%'
					}, {
						xtype : 'textfield',
						fieldLabel : '院系代号',
						name : 'collegenum',
						anchor : '98%',
						allowBlank : false
					}]
		}, {
			columnWidth : .5,
			layout : 'form',
			labelWidth : 60,
			items : [{
						xtype : 'textfield',
						fieldLabel : '院系名',
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

var collegeform = new Ext.form.FormPanel({
			labelAlign : 'left',
			layout : 'column',
			frame : true,
			border : false,
			bodyStyle : 'padding:5',
			items : [collegefields]
		});

var collegewin;

function collegeWindowInit(title) {

	if (!collegewin) {
		var collegewin = new Ext.Window({
					title : '院系设置',
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
					items : [collegeform],
					buttons : [{
						text : '保存',
						handler : function() {
							if (collegeform.getForm().isValid()) {
								var college = collegeform.getForm()
										.getValues();
								CollegeController.saveCollege(college,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示',
														"保存成功！");
												collegewin.hide();
											} else
												Ext.MessageBox.alert('提示',
														"保存失败！");
											collegeGridInit();
										});

							} else {
								Ext.MessageBox.alert('提示', "输入信息有误！");
							}
						}

					}, {
						text : '清空',
						handler : function() {
							collegeform.getForm().reset();
						}
					}, {
						text : '关闭',
						handler : function() {
							collegewin.hide();
						}
					}]
				});

	}
	collegewin.setTitle(title);
	return collegewin;

}

function collegeGridInit() {
	collegestore.load({
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
				items : [collegegrid],
				renderTo : Ext.getBody()
			});
	collegeGridInit();
}