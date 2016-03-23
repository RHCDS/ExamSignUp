var profession = Ext.data.Record.create([{
			name : 'id'
		}, {
			name : 'name'
		}, {
			name : 'institutionid'
		}, {
			name : 'remark'
		}, {
			name : 'professionnum'
		}]);

var professionstore = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						totalProperty : 'totalProperty',
						root : 'root'
					}, profession),
			proxy : new Ext.ux.data.DWRProxy({
						dwrFunction : ProfessionController.paginationShow
					})
		});

var professiongrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'professiongrid',
	store : professionstore,
	title : '专业表',
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
				id : 'professionnum',
				header : '专业代号',
				dataIndex : 'professionnum',
				width : 300,
				sortable : true
			}, {
				id : 'name',
				header : '专业名',
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
				tooltip : '新增专业',
				iconCls : 'add',
				onClick : function() {
					var newprofession = new profession({
								id : '',
								institutionid : '',
								name : '',
								remark : '',
								professionnum : ''
							});
					professionform.getForm().reset();
					professionWindowInit('新增专业').show();
					professionform.getForm().loadRecord(newprofession);
				}
			}, '-', {
				text : '修改',
				tooltip : '修改专业信息',
				iconCls : 'edit',
				onClick : function() {
					professiongrid.fireEvent('rowdblclick', professiongrid);
				}
			}, '-', {
				text : '删除',
				tooltip : '删除选中的专业',
				iconCls : 'remove',
				onClick : function() {
					if (professiongrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的专业么?',
								function(button) {
									if (button == 'yes') {
										var list = professiongrid
												.getSelectionModel()
												.getSelections();
										var rList = [];
										for (var i = 0; i < list.length; i++) {
											rList[i] = list[i].data["id"];
										}

										ProfessionController.deleteProfession(
												rList, function(data) {
													if (data) {
														Ext.MessageBox.alert(
																'提示',
																'删除专业成功!');
													} else {
														Ext.MessageBox.alert(
																'删除专业失败!',
																"请务必先删除隶属该专业的所有考生！");
													}
													professionGridInit();
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
				store : professionstore,
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
				professionWindowInit("编辑专业信息").show();
				professionform.getForm().setValues(grid.getSelectionModel()
						.getSelected().data);
			} else {
				Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
			}

		}
	}
});

var professionfields = [{
			columnWidth : .5,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'hidden',
						name : 'id',
						anchor : '98%'
					}, {
						xtype : 'textfield',
						fieldLabel : '专业代号',
						name : 'professionnum',
						anchor : '98%',
						allowBlank : false
					}]
		}, {
			columnWidth : .5,
			layout : 'form',
			labelWidth : 60,
			items : [{
						xtype : 'textfield',
						fieldLabel : '专业名',
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

var professionform = new Ext.form.FormPanel({
			labelAlign : 'left',
			layout : 'column',
			frame : true,
			border : false,
			bodyStyle : 'padding:5',
			items : [professionfields]
		});

var professionwin;

function professionWindowInit(title) {

	if (!professionwin) {
		var professionwin = new Ext.Window({
					title : '专业设置',
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
					items : [professionform],
					buttons : [{
						text : '保存',
						handler : function() {
							if (professionform.getForm().isValid()) {
								var profession = professionform.getForm()
										.getValues();
								ProfessionController.saveProfession(profession,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示',
														"保存成功！");
												professionwin.hide();
											} else
												Ext.MessageBox.alert('提示',
														"保存失败！");
											professionGridInit();
										});

							} else {
								Ext.MessageBox.alert('提示', "输入信息有误！");
							}
						}

					}, {
						text : '清空',
						handler : function() {
							professionform.getForm().reset();
						}
					}, {
						text : '关闭',
						handler : function() {
							professionwin.hide();
						}
					}]
				});

	}
	professionwin.setTitle(title);
	return professionwin;

}

function professionGridInit() {
	professionstore.load({
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
				items : [professiongrid],
				renderTo : Ext.getBody()
			});
	professionGridInit();
}