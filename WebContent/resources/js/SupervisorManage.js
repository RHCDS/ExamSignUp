var theoryOrOperate;
//监考人员Record
var supervisor = Ext.data.Record.create([{
			name : 'id'
		}, {
			name : 'name'
		}, {
			name : 'contact'
		}, {
			name : 'exCollege'
		}, {
			name : 'supervisornum'
		},{
			name : 'primaryflag',convert:function(data){if(data==1){return '是';} else{return '否';}}
		}]);
//监考人员Store
var supervisorstore = new Ext.data.Store({
			reader : new Ext.data.JsonReader({
						totalProperty : 'totalProperty',
						root : 'root'
					}, supervisor),
			proxy : new Ext.ux.data.DWRProxy({
						dwrFunction : SupervisorController.paginationShow
					})
		});
//监考人员Panel
var supervisorgrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'supervisorgrid',
	store : supervisorstore,
	title : '监考人员表',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'contact',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [new Ext.grid.RowNumberer(), {
		id : 'supervisornum',
		header : '监考工号',
		dataIndex : 'supervisornum',
		width : 150,
		sortable : true
	}, {
		id : 'name',
		header : '监考人员姓名',
		dataIndex : 'name',
		width : 150,
		sortable : true
	},  {
		header : '学院',
		dataIndex : 'exCollege',
		width : 200,
		sortable : true
	},{
		id : 'primaryflag',
		header : '主监考',
		dataIndex: 'primaryflag',
		width : 60,
		sortable : true
	},{
		id : 'contact',
		header : '联系方式',
		dataIndex : 'contact',
		width : 150,
		sortable : true
	}],
	tbar : [{
        text: '批量导入监考',
        iconCls : 'import',
        scope: this,
        handler:function(){
        	supervisorimprot().show();
        }
    },'-',{
		text : '新增',
		tooltip : '新增监考人员',
		iconCls : 'add',
		onClick : function() {
			var newsupervisor = new supervisor({
				id : '',
				name : '',
				contact : '',
				supervisornum : '',
				primaryflag : ''
			});
			supervisorform.getForm().reset();
			supervisorWindowInit('新增监考人员').show();
			supervisorform.getForm().loadRecord(newsupervisor);
		}
	}, '-', {
		text : '修改',
		tooltip : '修改监考人员',
		iconCls : 'edit',
		onClick : function() {
			supervisorgrid.fireEvent('rowdblclick', supervisorgrid);
		}
	}, '-', {
		text : '删除',
		tooltip : '删除选中的菜单项',
		iconCls : 'remove',
		onClick : function() {
			if (supervisorgrid.getSelectionModel().hasSelection()) {
				Ext.MessageBox.confirm('提示', '你确定要删除选中的监考人员吗?',
					function(button) {
						if (button == 'yes') {
							var list = supervisorgrid.getSelectionModel().getSelections();
							var rList = [];
							for (var i = 0; i < list.length; i++) {
								rList[i] = list[i].data["id"];
							}
							SupervisorController.deleteSupervisor(rList, 
								function(data) {
									if (data) {
										Ext.MessageBox.alert('提示','删除监考人员成功!');
										var temp=Ext.getCmp("supervisorPagingToolbar");
										temp.doLoad(temp.cursor);
									} else {
										Ext.MessageBox.alert('删除监考人员失败',
												"已安排该监考，请务必先取消该监考员的安排！");
									}
							});
						}
				});
			} else {
				Ext.MessageBox.alert('提示', "请至少选择一条记录!");
			}
		}
	},'-',new Ext.form.ComboBox({
		    text:'理论上机选择',
		    fieldLabel: '理论上机监考老师选择',
		    id:'theoryOrOperateComboBox',
		    forceSelection: true,
		    editable:false,
		    listWidth: 200,
		    store: new Ext.data.SimpleStore({
		        fields: ['name', 'value'],
		        data : [['理论考试监考','theory'],['上机考试监考','operate']]
		    }),
		    valueField:'value',
		    displayField:'name',
		    typeAhead: true,
		    mode: 'local',
		    triggerAction: 'all',
		    selectOnFocus:true,
		    allowBlank:false,
		    listeners: {
		  	    afterRender:function(data){
		  	        theoryOrOperate="theory";
		  	        Ext.getCmp("theoryOrOperateComboBox").setValue("theory");
		  	        Ext.getCmp("supervisorPagingToolbar").moveFirst();
		  	    },
			  	select:function(){ 
        	  		if(theoryOrOperate==this.getValue)
        	  			return;
        	  		theoryOrOperate=this.getValue();
        	  		Ext.getCmp("supervisorPagingToolbar").moveFirst();
			  	}
		    }
	})],
	bbar : new Ext.PagingToolbar({
		id: 'supervisorPagingToolbar',
		pageSize : 20,
		store : supervisorstore,
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
			params.theoryOrOperate = theoryOrOperate;
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
				supervisorWindowInit("编辑监考员信息").show();
				supervisorform.getForm().setValues(grid.getSelectionModel()
						.getSelected().data);
			} else {
				Ext.MessageBox.alert('提示', "请选择一条信息进行编辑!");
			}

		}
	}
});
//form的内容
var supervisorfields = [{
			columnWidth : .5,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'hidden',
						name : 'id',
						anchor : '98%'
					},{
						xtype : 'hidden',
						name : 'theoryflag',
						anchor : '98%'
					},{
						xtype : 'hidden',
						name : 'operateflag',
						anchor : '98%'
					},{
						xtype : 'textfield',
						fieldLabel : '监考人编号',
						name : 'supervisornum',
						anchor : '98%',
						allowBlank : false
					}]
		}, {
			columnWidth : .5,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'textfield',
						fieldLabel : '监考人姓名',
						name : 'name',
						anchor : '98%',
						allowBlank : false
					}]
		}, {
			columnWidth : .6,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'textfield',
						fieldLabel : '学院',
						name : 'exCollege',
						anchor : '98.65%',
						allowBlank : false
					}]
		}, {
			columnWidth : .4,
			layout : 'form',
			labelWidth : 60,
			items : [{
						xtype : 'combo',
						fieldLabel : '主考官',
						name : 'primaryflag',
						allowBlank : false,
						store:['是','否'],
						editable : false,
						anchor : '95%',
						triggerAction : 'all'
					}]
		},{
			columnWidth : 1,
			layout : 'form',
			labelWidth : 90,
			items : [{
						xtype : 'textarea',
						fieldLabel : '联系方式',
						name : 'contact',
						anchor : '98.65%',
						allowBlank : true
					}]
		}];

var supervisorform = new Ext.form.FormPanel({
			labelAlign : 'left',
			layout : 'column',
			frame : true,
			border : false,
			bodyStyle : 'padding:5',
			items : [supervisorfields]
		});

var supervisorwin;

function supervisorWindowInit(title) {

	if (!supervisorwin) {
		var supervisorwin = new Ext.Window({
					title : '监考人员设置',
					width : 480,
					height : 260,
					closeAction : 'hide',
					layout : 'fit',
					border : false,
					modal : true,
					shadow : true,
					hideMode : Ext.isIE ? 'offsets' : 'display',
					plain : true,
					bodyStyle : 'padding:5px;',
					buttonAlign : 'center',
					items : [supervisorform],
					buttons : [{
						text : '保存',
						handler : function() {
							if (supervisorform.getForm().isValid()) {
								var supervisor = supervisorform.getForm()
										.getValues();
								// alert(supervisor);
								if(theoryOrOperate == "operate") {
									supervisor.theoryflag = "0";
									supervisor.operateflag = "1";
								} else {
									supervisor.theoryflag = "1";
									supervisor.operateflag = "0";
								}
								SupervisorController.saveSupervisor(supervisor,
										function(data) {
											if (data) {
												Ext.MessageBox.alert('提示',
														"保存成功！");
												supervisorwin.hide();
												var temp=Ext.getCmp("supervisorPagingToolbar");
                                                temp.doLoad(temp.cursor);
											} else
												Ext.MessageBox.alert('提示',
														"保存失败！");
										});

							} else {
								Ext.MessageBox.alert('提示', "输入信息有误！");
							}
						}

					}, {
						text : '清空',
						handler : function() {
							supervisorform.getForm().reset();
						}
					}, {
						text : '关闭',
						handler : function() {
							supervisorwin.hide();
						}
					}]
				});

	}
	supervisorwin.setTitle(title);
	return supervisorwin;

}

var supervisorimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../supervisorXlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'supervisorfile',
		inputType : 'file'//文件类型 
	} ]
});

var supervisorimportwin;

function supervisorimprot(){
	if(!supervisorimportwin){
		supervisorimportwin = new Ext.Window({
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
   	        items: supervisorimportform,
   	     buttons : [ {
   			text : '上传',
   			handler : function() {
   				Ext.MessageBox.wait('正在上传，请等待...','提示');
   				supervisorimportform.getForm().submit({
   					success : function(form, action) {
   						var supervisors = Ext.util.JSON.decode(action.response.responseText).excelArray;
   						SupervisorController.importSupervisors(supervisors,function(data){
   							if(data){
   								Ext.MessageBox.alert('提示', '文件上传成功！',function(){
   									supervisorimportwin.hide();
   									var temp=Ext.getCmp("supervisorPagingToolbar");
									temp.doLoad(temp.cursor);
   								});
   								
   							}	
   						});
   						
   					},
   					failure : function() {
   						Ext.MessageBox.alert('错误', '文件上传失败，请检查数据格式',function(){
								supervisorimportwin.hide();
   						});
   					}
   				});
   			}
   		} ]
		});
	}
	return supervisorimportwin;
}




function pageInit() {
	new Ext.Viewport({
				layout : 'border',
				hideMode : Ext.isIE ? 'offsets' : 'display',
				items : [supervisorgrid],
				renderTo : Ext.getBody()
			});
}