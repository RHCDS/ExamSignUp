var clear_bit="0";
var language = Ext.data.Record.create([{
			name : 'id'
		}, {
			name : 'name'
		}, {
			name :'theoryflag',convert:function(data){if(data==1){return "是"}else{return "否"}}
		},{
			name :'operateflag',convert:function(data){if(data==1){return "是"}else{return "否"}}
		}, {
			name : 'languagenum'
		},{
			name : 'operatelength'
		},{
			name : 'theorylength'
		}
		]);
 
var languagestore = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						totalProperty : 'totalProperty',
						root : 'root'
					}, language),
			proxy : new Ext.ux.data.DWRProxy({
						dwrFunction : LanguageController.paginationShow
					})
		});



function clearHistoryData(confirm) {
	if(confirm == "yes") {
		clear_bit="1";
		Ext.MessageBox.wait('正在执行操作...','请等待');
		ClearHistoryDataController.clearHistoryData(function(data) {
			var jsonData = Ext.util.JSON.decode(data);
			if(jsonData.success == true) {
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					
				});
			} else {
				Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
					
				});
			}
		});
	}
}

var languagegrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'languagegrid',
	store : languagestore,
	title : '语种表',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'name',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [new Ext.grid.RowNumberer(), {
				id : 'languagenum',
				header : '语种号',
				dataIndex : 'languagenum',
				width : 100,
				sortable : true
			}, {
				id : 'name',
				header : '语种名',
				dataIndex : 'name',
				width : 120,
				sortable : true
			}, {
				id : 'theoryflag',
				header : '理论考试',
			  	dataIndex :'theoryflag',
			  	width :150,
			 	sortable :true
			},	{
				id : 'operateflag',
				header : '上机考试',
			  	dataIndex :'operateflag',
			  	width :150,
			 	sortable :true
			},	{
				header : '理论考试时间（分钟）',
			  	dataIndex :'theorylength',
			  	width :150,
			 	sortable :true
			},	{
				header : '上机考试时间（分钟）',
			  	dataIndex :'operatelength',
			  	width :150,
			 	sortable :true
			}],
	tbar : [{
        text: '批量导入语种',
        tooltip : '导入语种',
        iconCls : 'import',
        scope: this,
        handler:function(){
        	if(clear_bit == "0")
        	{
       			Ext.MessageBox.alert('提示','清空历史数据之后，才能导入语种！',function() {
        			Ext.MessageBox.confirm('提示','是否要清空历史数据？',clearHistoryData);
        		});
       		}

       		else
       		{
       			languageimprot().show();
       		};

        }
    },'-',{
				text : '新增',
				tooltip : '新增语种',
				iconCls : 'add',
				onClick : function() {
					var newlanguage = new language({
								id : '',
								theoryflag : '',
								name : '',
								operateflag : '',
								languagenum : '',
								theorylength : '',
								operatelength : ''
							});
					languageform.getForm().reset();
					languageWindowInit('新增语种').show();
					languageform.getForm().loadRecord(newlanguage);
				}
			}, '-', {
				text : '修改',
				tooltip : '修改语种信息',
				iconCls : 'edit',
				onClick : function() {
					languagegrid.fireEvent('rowdblclick', languagegrid);
				}
			}, '-', {
				text : '删除',
				tooltip : '删除选中的语种',
				iconCls : 'remove',
				onClick : function() {
					if (languagegrid.getSelectionModel().hasSelection()) {
						Ext.MessageBox.confirm('提示', '你确定要删除选中的语种么?',
								function(button) {
									if (button == 'yes') {
										var list = languagegrid
												.getSelectionModel()
												.getSelections();
										var rList = [];
										for (var i = 0; i < list.length; i++) {
											rList[i] = list[i].data["id"];
										}

										LanguageController.deleteLanguage(
												rList, function(data) {
													if (data) {
														Ext.MessageBox.alert(
																'提示',
																'删除成功!');
													} else {
														Ext.MessageBox.alert(
																'提示',
																"删除失败!");
													}
													languageGridInit();
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
				store : languagestore,
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
				languageWindowInit("编辑语种信息").show();
				languageform.getForm().setValues(grid.getSelectionModel()
						.getSelected().data);
			} else {
				Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
			}

		}
	}
});

var languagefields = [{
				columnWidth : .5,
				layout : 'form',
				items : [{xtype : 'hidden',name : 'id',anchor : '98%'}, 
				         {xtype : 'textfield',fieldLabel : '语种代码',name : 'languagenum',
							anchor : '98%',allowBlank : false}]
					}, {
				columnWidth : .5,
				layout : 'form',
				items : [{xtype : 'textfield',fieldLabel : '语种名称',name : 'name',anchor : '98%',
						allowBlank : false}]
					}, {
				columnWidth:.5,
				layout:'form',
				items:[{xtype:'combo',fieldLabel: '理论考试',name: 'theoryflag',triggerAction : 'all',store : ['是','否'],
						editable : false,anchor:'98%',allowBlank: false}
				      ]},{
				columnWidth:.5,
				layout:'form',
				items:[{fieldLabel:'上机考试',name:'operateflag',xtype: 'combo',triggerAction : 'all', store : ['是','否'],
						editable:false,allowBlank : false,anchor:'98%'}
				      ]}, 
				      {
							columnWidth : .5,
							layout : 'form',
							items : [{xtype : 'textfield',fieldLabel : '理论时间',name : 'theorylength',anchor : '98%',
									allowBlank : false}]
					 }, 
					 {
									columnWidth : .5,
									layout : 'form',
									items : [{xtype : 'textfield',fieldLabel : '上机时间',name : 'operatelength',anchor : '98%',
											allowBlank : false}]
					}
		];
var languageform = new Ext.form.FormPanel({
			labelAlign : 'left',
			layout : 'column',
			frame : true,
			labelWidth : 60,
			border : false,
			bodyStyle : 'padding:5',
			items : [languagefields]
		});

var languagewin;

var languageimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../languageXlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'languagefile',
		inputType : 'file'//文件类型 
	} ]
});

function languageimprot(){
	if(!languagewin){
		languagewin = new Ext.Window({
   	        title: '文件上传',
   	        width: 480,
   	        height:360,
   	        minWidth: 400,
   	        minHeight: 360,
   	        closeAction : 'hide',
   	        layout: 'border',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: languageimportform,
   	     buttons : [ {
   			text : '上传',
   			handler : function() {
   				Ext.MessageBox.wait('正在上传，请等待...','提示');
   				languageimportform.getForm().submit({
   					success : function(form, action) {
   						var language = Ext.util.JSON.decode(action.response.responseText).excelArray;
   						LanguageController.importLanguage(language,function(data){
   							if(data){
   								Ext.MessageBox.alert('提示', '文件上传成功！',function(){
   									languagewin.hide();
   									var temp=Ext.getCmp("supervisorPagingToolbar");
									temp.doLoad(temp.cursor);
   								});
   								
   							}	
   						});
   						
   					},
   					failure : function() {
   						Ext.MessageBox.alert('错误', '文件上传失败，请检查数据格式',function(){
   							languagewin.hide();
   						});
   					}
   				});
   			}
   		} ]
		});
	}
	return languagewin;
}

function languageWindowInit(title) {

	if (!languagewin) {
		var languagewin = new Ext.Window({
					title : '语种设置',
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
					items : [languageform],
					buttons : [{
						text : '保存',
						handler : function() {
							if (languageform.getForm().isValid()) {
								var language = languageform.getForm()
										.getValues();
								LanguageController.saveLanguage(language,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示',
														"保存成功！");
												languagewin.hide();
											} else
												Ext.MessageBox.alert('提示',
														"保存失败！");
											languageGridInit();
										});

							} else {
								Ext.MessageBox.alert('提示', "输入信息有误！");
							}
						}

					}, {
						text : '清空',
						handler : function() {
							languageform.getForm().reset();
						}
					}, {
						text : '关闭',
						handler : function() {
							languagewin.hide();
						}
					}]
				});

	}
	languagewin.setTitle(title);
	return languagewin;

}

function languageGridInit() {
	languagestore.load({
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
				items : [languagegrid],
				renderTo : Ext.getBody()
			});
	languageGridInit();
}