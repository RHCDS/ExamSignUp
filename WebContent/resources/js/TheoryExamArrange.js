var selectedLanguageNum="0";
var signedLanguageRecord = Ext.data.Record.create([{
		name :'languagenum'
	}, {
		name :'languagename'
	}
]);
//语种Store
var signedLanguagesectionStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root',
         idProperty:'languagenum'
      }, signedLanguageRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : TheoryExamArrangeController.loadSignedLanguage
	  })
});

var signedLanguageCombo = new Ext.form.ComboBox({
	store: signedLanguagesectionStore,
	mode: 'local',
	triggerAction: 'all',
	emptyText:'--请选择语种--',
	editable:false,
	valueField:'languagenum',
	displayField:'languagename',
	listEmptyText:'没有已报名语种！',
	
	listeners : {
		afterRender : function(data) {
			selectedLanguageNum="0";
			signedLanguagesectionStore.load();
		},
		select:function(record,index){
			var params = {};
			
			if(selectedLanguageNum==this.getValue())
				return;
			selectedLanguageNum=this.getValue();
			params.languagenum=selectedLanguageNum;
			arrangeStore.load({params:params,
				callback:function(data){
					if(arrangeStore.getTotalCount()>0){
						arrangeGrid.getSelectionModel().selectFirstRow();
						arrangeGrid.fireEvent("rowclick");
						
					} else {
						arrangeGrid.getSelectionModel().clearSelections();
						studentStore.removeAll();
						examroomStore.removeAll();
					}
			}});
			var sectionnum;
			if(arrangeStore.getCount()==0) {
				sectionnum = 0;
			} else {
				sectionnum = arrangeStore.getAt(0).data.sectionnum;
			}
			TheoryExamArrangeController.getStatisticsByLang(selectedLanguageNum,sectionnum,function(data) {
				statisticsLable.getEl().update(data);
			});
			
		}
	}
});
//按照校区和语种划分的容量详细信息
var statisticsByCampusRecord = Ext.data.Record.create([{
	name :'id'
},{
	name :'campusname'
}, {
	name :'arrangedNum'
}, {
	name :'unArrangeCount'
}, {
	name :'capacity'
}
]);

var statisticsByCampusStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'id'
  }, statisticsByCampusRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction : TheoryExamArrangeController.getStatisticsByLangAndCampus
  })
});
var  statisticsByCampusGrid = new Ext.grid.GridPanel({
	id : 'statisticsByCampusGrid',
	store : statisticsByCampusStore,
	title : '按校区分类统计信息',
	loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'capacity',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [
	{
		id :'campusname',
		header : '校区名称',
	  	dataIndex :'campusname',
	  	width :100,
	 	sortable :true
	}, {
		id :'arrangedNum',
		header : '已安排学生数',
	  	dataIndex :'arrangedNum',
	  	width :100,
	 	sortable :true
	}, {
		id :'unArrangeCount',
		header : '未安排学生数',
	  	dataIndex :'unArrangeCount',
	  	width :100,
	 	sortable :true
	}, {
		id :'capacity',
		header : '已分配的教室余量',
	  	dataIndex :'capacity',
	  	width :120,
	 	sortable :true
	}
	]
});
var  statisticsByCampusWindow = new Ext.Window({
    title: '详细信息',
    width: 470,
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
    items: statisticsByCampusGrid});

//考场的Record
var examroomRecord = Ext.data.Record.create([
   {name: 'examroomnum'},
   {name: 'info'},
   {name: 'campus'},
   {name: 'capacity'}
]);
//各个考场的store
var examroomStore = new Ext.data.GroupingStore(
	{
		reader: new Ext.data.JsonReader({
			totalProperty : 'totalProperty',
			root : 'root',
			idProperty:'examroomnum'
		},examroomRecord),
		proxy : new Ext.ux.data.DWRProxy({
		     dwrFunction : TheoryExamArrangeController.getArrangedExamroom
		 }),
		sortInfo: {
			field: 'examroomnum',
			direction: "ASC"
		}
	}
);
// addExamroomRecord
var addExamroomStore = new Ext.data.GroupingStore(
		{
			reader: new Ext.data.JsonReader({
				totalProperty : 'totalProperty',
				root : 'root',
				idProperty:'examroomnum'
			},examroomRecord),
			proxy: new Ext.ux.data.DWRProxy({
				dwrFunction : TheoryExamArrangeController.getUnarrangedExamroom
			}),
			sortInfo: {
				field: 'examroomnum',
				direction: "ASC"
			}
		}
);


var examroomGrid = new Ext.grid.GridPanel({
	frame:true,
	height:300,
	width:230,
	id : 'examroomGrid',
	store : examroomStore,
	title : '已安排考场',
    loadMask :false,
	stripeRows :true,
	autoScroll:true,
	collapsible: false,
	animCollapse: false,
	autoExpandColumn : 'info',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width:28}),
	{
		id : 'examroomnum',
	  	dataIndex :'examroomnum',
	 	width: 0
	},	{
		id : 'info',
		header : '考场号',
	  	dataIndex :'info',
	  	width :60
	}, {
		id: 'campus',
		header: '校区',
		dataIndex: 'campus',
		sortable: false,
		width: 60
	}, {
		id: 'capacity',
		header: '容量',
		dataIndex: 'capacity',
		sortable: true,
		width: 40
	}
	],
    tbar : [{
        text : '添加',
        tooltip : '添加考场',
        iconCls : 'add',
        onClick : function() {
            if(arrangeGrid.getSelectionModel().getCount()!=1){
            	Ext.MessageBox.alert('提示',"请选择一条教室记录！");
            }else{
            	var params = {};
            	params.languagenum=signedLanguageCombo.getValue();
            	if(specialCampus.getValue()==true) {
            		params.arrangeid = arrangeGrid.getSelectionModel().getSelected().data.id;
            	}
            	else {
            		params.arrangeid = "";
            	}
            	addExamroomStore.load({params: params,callback: function(data) {
            		addExamroomWindow.show();
            	}});
            	
            }
        }
    }, '-', {
    	text : '删除',
    	tooltip : '删除考场',
    	iconCls : 'remove',
    	onClick : function() {
    		if(examroomGrid.getSelectionModel().getCount()==0){
    			Ext.MessageBox.alert('提示',"没有选择考场!");
    		} else {
    			var selected = arrangeGrid.getSelectionModel().getSelected().data;
    			var sectionnum = selected.sectionnum;
    			var readyToDelete = [];
    			var temp=examroomGrid.getSelectionModel().getSelections();
    			for(var i=0;i<temp.length;i++){
    				readyToDelete.push(temp[i].data);
    			}
    			if(selected.id != null){	
    				TheoryExamArrangeController.deleteArrangedExamroom(readyToDelete,function(data){
    					var jsonData = Ext.util.JSON.decode(data);
    					if (jsonData.success == true) {
    						Ext.MessageBox.alert('提示',jsonData.info);
    						examroomStore.reload();
    						addExamroomStore.reload();
    						studentStore.reload();
    						arrangeStore.reload();
    						TheoryExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),sectionnum,function(data) {
    							statisticsLable.getEl().update(data);
    						});
    					} else {
    						Ext.MessageBox.alert('提示','删除失败！');
    					}
    				});
    			} else {
    				Ext.MessageBox.alert('提示',"ArrangeId不存在!");
    			}
    		}
    	}
    }
    ] ,
        listeners:{
    	rowdblclick : function(data){
    		
    	},
    	afterRender: function(data){
    		this.getColumnModel().setHidden(1,true);
    	}
    }
 });
var specialCampus=new Ext.form.Checkbox({ 
	boxLabel: "只显示教室所在校区的考场", 
	name: "specialCampus",
	inputValue: 1 ,
	listeners: {
		check: function() {
			var params = {};
        	params.languagenum=signedLanguageCombo.getValue();
        	if(specialCampus.getValue()==true) {
        		params.arrangeid = arrangeGrid.getSelectionModel().getSelected().data.id;
        	}
        	else {
        		params.arrangeid = "";
        	}
        	addExamroomStore.load({params: params});
		}
	}
});
var addExamroomGrid = new Ext.grid.GridPanel({
	frame : true,
	width : 600,
	height: 300,
	id : 'addExamroomGrid',
	store : addExamroomStore,
	loadMask : false,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'info',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [ new Ext.grid.RowNumberer({width:30}), 
       	{
       		id : 'examroomnum',
       		header: 'examroomnum',
       	  	dataIndex :'examroomnum',
       	 	sortable :true
       	},	{
       		id : 'info',
       		header : '考场号',
       	  	dataIndex :'info',
       	  	width :120,
       	 	sortable :true
       	}, {
       		id: 'campus',
       		header: '校区',
       		dataIndex: 'campus',
       		sortable: false,
       		width: 80
       	}, {
       		id: 'capacity',
       		header: '容量',
       		dataIndex: 'capacity',
       		sortable: true
       	}
	],
	tbar : [specialCampus],
	bbar :[
	  {
		  	text : '保存选择项',
	        tooltip : '保存已经选择的考场',
	        onClick : function() {
	     	   if(addExamroomGrid.getSelectionModel().getCount()==0){
	     		   Ext.MessageBox.alert('提示',"没有选择考场!");
	     	   }else if(addExamroomGrid.getSelectionModel().getCount()!=1) {
	     		  Ext.MessageBox.alert('提示',"请选择一个考场！");
	     	   }else if(arrangeGrid.getSelectionModel().getSelected().data.surplus<addExamroomGrid.getSelectionModel().getSelected().data.capacity) {
	     		   Ext.MessageBox.alert('提示','教室的余量不足，无法安排所选考场!');
	     	   }
	     	   else{
	     		  var selected = arrangeGrid.getSelectionModel().getSelected().data;
	     		 var sectionnum =selected.sectionnum;
	     		  var examroomnum = addExamroomGrid.getSelectionModel().getSelected().data.examroomnum;
	     		  if(selected.id != null){	
	     			  TheoryExamArrangeController.arrangeByExamroom(selected.id,examroomnum,function(data){
	     				  var jsonData = Ext.util.JSON.decode(data);
	     				  if (jsonData.success == true) {
	     					  Ext.MessageBox.alert('提示',jsonData.info,function() {
	     						  studentStore.reload();
	     						  addExamroomStore.reload();
	     						  examroomStore.reload();
	     						  arrangeStore.reload();
	     						  addExamroomGrid.getSelectionModel().clearSelections();
	     					  });
	     					  TheoryExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),sectionnum,function(data) {
	     						 statisticsLable.getEl().update(data);
	     					  });
	     				  } else {
	     					  Ext.MessageBox.alert('提示','操作失败！');
	     				  }
	     			  });
	     		  }else{
	     			  Ext.MessageBox.alert('提示',"ArrangeId不存在!");
	     		  }
	     	   }
	        }
   }],
   
	listeners : {
		rowdblclick : function(data) {
		},
		afterRender : function(data) {
			this.getColumnModel().setHidden(1,true);
		},
		rowclick: function(data) {
		}
	}
});

var addExamroomWindow = new Ext.Window({
	title: '添加考场',
	width: 800,
	height: 450,
	closeAction: 'hide',
	layout: 'fit',
	bodyStyle  : 'position:relative',
	modal: true,
	shadow: true,
	hideMode : Ext.isIE ? 'offsets' : 'display',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : [addExamroomGrid]
});
//arrange的record
var arrangeRecord = Ext.data.Record.create([ {name:'id'},{
	name : 'sectionid'
}, {
	name : 'sectioninfo'
}, {
	name : 'physicexamroomid'
}, {
	name : 'roomlocation'
}, {
	name : 'capacity'
}, {
	name : 'campusname'
}, {
	name: 'sectionnum'
}, {
	name: 'examrooms'
}, {
	name: 'surplus'
}
]);
//arrange的Store
var arrangeStore = new Ext.data.GroupingStore( {
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty:'id'
	}, arrangeRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : TheoryExamArrangeController.loadArrangeInfo
	}),
	sortInfo: {
		field: 'campusname',
		direction: "ASC"
	},
	groupField: 'sectioninfo'
});
//场次
var selectSection='0';
var sectionRecord = Ext.data.Record.create([{
	name :'sectionnum'
}, {
	name :'sectioninfo'
}, {
	name: 'id'
}
]);
function loadSectionStore() {
	sectionStore.load({
		callback:
			function(data){
			if(sectionStore.getTotalCount()>0){
				sectionCombo.setValue(sectionStore.getAt(selectSection).get("sectionnum"));
				sectionCombo.fireEvent("select");
			}
		}
	});
}
var sectionStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'id'
  }, sectionRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction : TheoryExamArrangeController.loadAllSections
  })
});

var sectionCombo = new Ext.form.ComboBox({
	store: sectionStore,
	mode: 'local',
	triggerAction: 'all',
	emptyText:'--请选择场次--',
	editable:false,
	valueField:'sectionnum',
	displayField:'sectioninfo',
	listEmptyText:'尚未添加场次信息！',
	width:250,

	listeners : {
		afterRender : function(data) {
			selectSection="0";
			sectionStore.load({
				callback:
				function(data){
					if(sectionStore.getTotalCount()>0){
						sectionCombo.setValue(sectionStore.getAt(0).get("sectionnum"));
						sectionCombo.fireEvent("select");
					}

				}});
		
		},
		select:function(record,index){
			selectSection=this.getValue();
			Ext.MessageBox.wait('数据加载...','请等待');
			TheoryExamArrangeController.loadtheoryExamroom(selectSection,selectedLanguageNum,function(data){
				var jsonData=Ext.util.JSON.decode(data);
				var selector=Ext.getCmp("theorySelector");
				if(jsonData==null)
					return;
				availableSelectItem.loadData(jsonData.unselected);
				haveSelectedItem.loadData(jsonData.selected);
				selector.fromMultiselect.view.refresh();
				selector.toMultiselect.view.refresh();
				Ext.MessageBox.hide();
   		});
		}
	}
});

var availableSelectItem= new Ext.data.ArrayStore({
    data: [],
    fields: ['value','text','flag'],
    sortInfo: {
		field: 'text',
		direction: "ASC"
	}
});
var haveSelectedItem =  new Ext.data.ArrayStore({
    data: [],
    fields: ['value','text','flag'],
    sortInfo: {
		field: 'text',
		direction: "ASC"
	}
});

var addTheoryExamroomPanel = new Ext.form.FormPanel({ 
	labelWidth: 75,
	hidden:false,
	width: 600,
    height:300,
    frame : true,
	items:[{
        xtype: 'itemselector',
        name: 'theorySelector',
        id:'theorySelector',
        fieldLabel: '请选择教室',
        imagePath: '../resources/images/',
        multiselects: [{
        	legend:"可安排教室",
            width: 250,
            height: 200,
            store: availableSelectItem,
            displayField: 'text',
            valueField: 'value'
        },{
        	legend:"已安排教室",
            width: 250,
            height: 200,
            store: haveSelectedItem,
            displayField: 'text',
            valueField: 'value',
            tbar:[{
                text: '清除已安排项',
                handler:function(){
                	addTheoryExamroomPanel.getForm().findField('theorySelector').reset();
                }
            }]
        }]
    }],
	tbar:[sectionCombo],
	bbar:['->',{
		text:"保存",
		iconCls : 'save',
		handler: function(){
			Ext.MessageBox.wait("正在执行...","提示"); 
			if(checkSection() == false) {
				Ext.MessageBox.alert('提示','此语种已经安排了一个场次，若要安排本场次，请取消其他场次的安排，再进行操作！');
			} else {
				var result = new Array();
				var range=availableSelectItem.getRange();
				var sectionnum ;
				if(arrangeStore.getCount()==0) {
					sectionnum = 0;
				} else {
					sectionnum = arrangeStore.getAt(0).data.sectionnum;
				}
				for(var i=0;i<range.length;i++){
					if(range[i].data.flag=="old")
						result.push(range[i].data);
				}
				
				var range=haveSelectedItem.getRange();
				for(var i=0;i<range.length;i++){
					if(range[i].data.flag=="new")
						result.push(range[i].data);
				}
				TheoryExamArrangeController.savetheoryExamroom(selectSection,selectedLanguageNum,result,function(data){
					TheoryExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),sectionnum,function(data) {
						statisticsLable.getEl().update(data);
					});
					var jsonData=Ext.util.JSON.decode(data);
					if(jsonData.success==true)
					{
						
						Ext.MessageBox.alert('提示',jsonData.errors.info,function(){
							sectionCombo.fireEvent("select");
							arrangeStore.reload();
						});
					}
					else
					{
						Ext.MessageBox.alert('提示',jsonData.errors.info);
					}
				});
				
			}
		}
	}],
	listeners: {
   	  afterRender:function(data){
   		selectSection="0";
 	}}
});
//判断要保存的是否为同一场次
function checkSection() {
	if(arrangeGrid.getStore().getTotalCount() !== 0) {
		var oldSectionnum = arrangeGrid.getStore().getAt(0).get("sectionnum");
		if(oldSectionnum != selectSection) {
			return false;
		} else {
			return true;
		}
	}
	return true;
}
var arrangeWindow = new Ext.Window({
	title : '编辑场次包含的理论教室信息',
	width : 700	,
	height : 400,
	closeAction : 'hide',
	layout : 'fit',
	bodyStyle  : 'position:relative',
	border:false,
	modal: true,
	shadow: true,
	hideMode : Ext.isIE ? 'offsets' : 'display',
			plain : true,
			bodyStyle : 'padding:5px;',
			buttonAlign : 'center',
			items : [addTheoryExamroomPanel]
});
//统计标签
var statisticsLable = new Ext.form.Label({
	id : "statisticsLable",
	text : " ",
	width : 100,
	autoShow : true,
	autoWidth : true,
	autoHeight : true
});
//Arrange的Panel
var arrangeGrid = new Ext.grid.GridPanel({
	region:"center",
	store: arrangeStore,
	columns: [new Ext.grid.RowNumberer(),{
		id: 'sectioninfo',
		header: "场次信息",
		width: 100,
		sortable: true,
		dataIndex: 'sectioninfo'
	},{
		id: 'roomlocation',
		header: "考场位置",
		width: 100,
		sortable: true,
		dataIndex: 'roomlocation'
	},
	{
		header: "包含考场",
		width: 50,
		dataIndex: 'examrooms'
	},
	{
		header: "总量",
		width: 40,
		sortable: true,
		dataIndex: 'capacity'
	},
	{
		header: "余量",
		width: 40,
		sortable: true,
		dataIndex: 'surplus'
	},
	{
		header: "所在校区",
		width: 40,
		sortable: true,
		dataIndex: 'campusname'
	}],

	view: new Ext.grid.GroupingView({
		forceFit: true,
		groupTextTpl: '{text} ({["共有"]}{[values.rs.length]}{["个教室."]})'
	}),
	frame: true,
	width: 750,
	height: 450,
	collapsible: false,
	animCollapse: false,
	title: '理论考试编排',
	tbar: [signedLanguageCombo
	   	,'->',statisticsLable,'-',{
	        text : '详细信息',
	        tooltip : '按照校区分类的教室安排情况',
	        iconCls : 'data',
	        onClick : function(data){
	        	var params = {};
	  		  	params.languagenum = selectedLanguageNum;
	  		  	if(arrangeStore.getCount()==0) {
	  		  		params.sectionnum = 0;
	  		  	} else {
	  		  		params.sectionnum = arrangeStore.getAt(0).data.sectionnum;
	  		  	}
	  		  	statisticsByCampusStore.load({params:params});
	  		  	statisticsByCampusWindow.show();
	        }
	    }],
	bbar: [{
        text : '新增理论考试教室',
        tooltip : '新增教室',
        iconCls : 'edit',
        onClick : function(data){
        	sectionCombo.fireEvent("afterRender");
        	addTheoryExamroomPanel.getForm().reset();
        	arrangeWindow.show();
        }
    },'-', {
    	text: '自动编排',
    	tooltip: '自动编排所有考场',
    	onClick: function(data) {
    		if(arrangeStore.getCount()==0) {
    			Ext.MessageBox.alert('提示','添加教室后，再进行此操作!');
    		} else {
    			Ext.MessageBox.wait('正在执行操作...','请等待');
        		var languagenum = selectedLanguageNum;
        		
        		var sectionnum = arrangeStore.getAt(0).data.sectionnum;
        		TheoryExamArrangeController.autoArrange(languagenum,sectionnum,function(data) {
        			var jsonData = Ext.util.JSON.decode(data);
        			if(jsonData.success == true) {
        				arrangeStore.reload();
        				examroomStore.reload();
        				studentStore.reload();
        				TheoryExamArrangeController.getStatisticsByLang(selectedLanguageNum,sectionnum,function(data) {
        					statisticsLable.getEl().update(data);
        				});
        				Ext.MessageBox.hide();
        				Ext.MessageBox.alert('提示',jsonData.info.errors);
        				
        			} else {
        				Ext.MessageBox.hide();
        				Ext.MessageBox.alert('提示',jsonData.info.errors);
        				
        			}
        		});	
    		}
    		
    	}
    },'->', {
    	text: '取消所有安排',
    	tooltip: '取消所有理论考场安排',
    	onClick: function(data) {
    		 Ext.MessageBox.confirm('确认','取消所有考生安排?',cancelArrange);
    	}
    }
	],
	listeners : {
		afterRender : function(data) {
			this.getColumnModel().setHidden(1,true);
			var params = {};
			params.languagenum=selectedLanguageNum;
			this.store.load({params:params});
			
			signedLanguagesectionStore.load({
				callback:
					function(data){
					if(signedLanguagesectionStore.getTotalCount()>0){
						signedLanguageCombo.setValue(signedLanguagesectionStore.getAt(0).get("languagenum"));
						signedLanguageCombo.fireEvent("select");
					}
				}});
		},
		rowclick:function(data){
			var params = {};
			var selected = arrangeGrid.getSelectionModel().getSelected().data;
			if(selected.id != null){
				params.arrangeid=selected.id;
				params.languagenum=signedLanguageCombo.getValue();
				studentStore.load({params:params});
				examroomStore.load({params: {arrangeid:selected.id}});
			}
			else{
				studentStore.removeAll();
				examroomStore.removeAll();
			}
		}
	}
});
//取消安排函数
function cancelArrange(confirm) {
	if(confirm == "yes") {
		if(arrangeStore.getTotalCount()==0) {
			Ext.MessageBox.alert('提示','目前没有考场安排，该操作无效！');
		}else {
			Ext.MessageBox.wait('操作进行中...','请等待');
			var languagenum = selectedLanguageNum;
			var sectionnum;
			if(arrangeStore.getCount()==0) {
				sectionnum = 0;
			} else {
				sectionnum = arrangeStore.getAt(0).data.sectionnum;
			}
 		    TheoryExamArrangeController.cancelArrange(languagenum,function(data) {
 		    	var jsonData = Ext.util.JSON.decode(data);
 		    	if(jsonData.success == true) {
 		    		arrangeStore.reload();
 		    		studentStore.removeAll();
 		    		examroomStore.reload();
 		    		TheoryExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),sectionnum,function(data) {
 		    			statisticsLable.getEl().update(data);
 		    		});
 		    		Ext.MessageBox.hide();
 		    		Ext.MessageBox.alert('提示',jsonData.info.errors);
 				
 		    	}
 		    	else {
 		    		Ext.MessageBox.hide();
 		    		Ext.MessageBox.alert('提示','操作失败');
 		    	}
 		   });
		}
	}
}
var studentRecord = Ext.data.Record.create([ {name:'id'},{
	name : 'name'
}, {
	name : 'examnum'
}]);

var studentStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, studentRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : TheoryExamArrangeController.loadSpecialArrangedStudent
	}),
	sortInfo: {
		field: 'examnum',
		direction: 'ASC'
	}
});

var studentGrid = new Ext.grid.GridPanel({
	region:'east',
	frame:true,
	width:230,
	id : 'studentGrid',
	store : studentStore,
	title : '已安排考生',
    loadMask :false,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'examnum',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width:30}),
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :60,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :150,
	 	sortable :true
	}
	],
    listeners:{
    	rowdblclick : function(data){
    	},
    	afterRender: function(data){
    		this.setHeight(arrangeGrid.getHeight()-examroomGrid.getHeight());
    	}
    }
 });

var table = new Ext.Panel({
    title: '',
    layout:'table',
    region:'east',
    width:230,
    defaults: {
        // applied to each contained panel
        //bodyStyle:'padding:20px'
    	
    },
    layoutConfig: {
        // The total column count must be specified here
        columns: 1
    },
    items : [examroomGrid,studentGrid]
});
function pageInit() {
	Ext.QuickTips.init();
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [arrangeGrid,table],
		renderTo :Ext.getBody()
	});
}