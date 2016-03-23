var selectedLanguageNum="0";
//语言Record
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
	     dwrFunction : OperateExamArrangeController.loadSignedLanguage
	  })
});
//语种ComboBox
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
		afterRender : function(data) {       //渲染结束后的事件
			selectedLanguageNum="0";
			signedLanguagesectionStore.load(); 
		},
		select:function(record,index){     
			var params = {};
			if(selectedLanguageNum==this.getValue())
				return;
			selectedLanguageNum=this.getValue();
			params.languagenum=selectedLanguageNum;
			arrangeStore.load({params:params,                 //获取语种的考场安排信息
				callback:function(data){
					if(arrangeStore.getTotalCount()>0){
						arrangeGrid.getSelectionModel().selectFirstRow();
						arrangeGrid.fireEvent("rowclick");
					} else {
						arrangeGrid.getSelectionModel().clearSelections();
						studentStore.removeAll();
						unarrangedStudentStore.removeAll();
					}
			}});
			unarrangedStudentStore.load({params:params});
			OperateExamArrangeController.getStatisticsByLang(selectedLanguageNum,function(data) {
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
     dwrFunction : OperateExamArrangeController.getStatisticsByLangAndCampus
  })
});
var  statisticsByCampusGrid = new Ext.grid.GridPanel({
	id : 'statisticsByCampusStore',
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
		header : '尚未安排学生数',
	  	dataIndex :'unArrangeCount',
	  	width :100,
	 	sortable :true
	}, {
		id :'capacity',
		header : '已分配的教室容量',
	  	dataIndex :'capacity',
	  	width :100,
	 	sortable :true
	}
	]
});
var  statisticsByCampusWindow = new Ext.Window({
    title: '详细信息',
    width: 450,
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

//考场安排Record
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
	name : 'surplus'
}, {
	name: 'examrooms'
}
]);
//考场安排的Store
var arrangeStore = new Ext.data.GroupingStore( {
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty:'id'
	}, arrangeRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : OperateExamArrangeController.loadArrangeInfo
	}),
	sortInfo: {
		field: 'campusname',
	direction: "ASC"
	},
	groupField: 'sectioninfo'
});

var selectSection='0';
//场次的Record
var sectionRecord = Ext.data.Record.create([{
	name :'sectionnum'
}, {
	name :'sectioninfo'
}
]);
//场次的Store
var sectionStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'id'
  }, sectionRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction : OperateExamArrangeController.loadAllSections
  })
});
//场次的ComboBox
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
				//通过回调函数，将页面的Combo默认值设为第一个section的sectionnum
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
			OperateExamArrangeController.loadOperateExamroom(selectSection,selectedLanguageNum,function(data){
				var jsonData=Ext.util.JSON.decode(data);
				var selector=Ext.getCmp("operateSelector");
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
//可安排教室Store
var availableSelectItem= new Ext.data.ArrayStore({
    data: [],
    fields: ['value','text','flag'],
    sortInfo: {
		field: 'text',
		direction: "ASC"
	}
});
//已安排教室Store
var haveSelectedItem =  new Ext.data.ArrayStore({
    data: [],
    fields: ['value','text','flag'],
    sortInfo: {
		field: 'text',
		direction: "ASC"
	}
});

//安排教室选项
var addOperateExamroomPanel = new Ext.form.FormPanel({ 
	labelWidth: 75,
	hidden:false,
	width: 600,
    height:300,
    frame : true,
    layout : 'border',
	items:[{
        xtype: 'itemselector',
        name: 'operateSelector',
        id:'operateSelector',
        fieldLabel: '请选择上机教室',
        imagePath: '../resources/images/',
        multiselects: [{
        	legend:"可安排上机教室",
            width: 250,
            height: 200,
            store: availableSelectItem,
            displayField: 'text',
            valueField: 'value'
        },{
        	legend:"已安排上机教室",
            width: 250,
            height: 200,
            store: haveSelectedItem,
            displayField: 'text',
            valueField: 'value',
            tbar:[{
                text: '清除已安排项',
                handler:function(){
                	addOperateExamroomPanel.getForm().findField('operateSelector').reset();
                }
            }]
        }]
    }],
	tbar:[sectionCombo],
	bbar:[{text:"注意：请避免同一“上机”教室中安排多个考试语种！"},'->',{	
		text:"保存",
		iconCls : 'save',
		handler: function(){
			Ext.MessageBox.wait("正在执行...","提示");
			var result = new Array();
			var range=availableSelectItem.getRange();
			for(var i=0;i<range.length;i++){
				if(range[i].data.flag=="old")
					result.push(range[i].data);
			}
			var range=haveSelectedItem.getRange();
			for(var i=0;i<range.length;i++){
				if(range[i].data.flag=="new")
					result.push(range[i].data);
			}
			OperateExamArrangeController.saveOperateExamroom(selectSection,selectedLanguageNum,result,function(data){
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
			OperateExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),function(data) {
				   statisticsLable.getEl().update(data);
			});
		}
	}],
	listeners: {
   	  afterRender:function(data){
   		selectSection="0";
 	}}
});

var arrangeWindow = new Ext.Window({
	title : '编辑场次包含的上机教室信息',
	width : 700,
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
			items : [addOperateExamroomPanel]
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
		header: "教室位置",
		width: 100,
		sortable: true,
		dataIndex: 'roomlocation'
	},
	{
		header: "总量",
		width: 40,
		sortable: true,
		dataIndex: 'capacity'
	},{
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
	title: '上机考试编排',
	tbar: [signedLanguageCombo
	,'->',statisticsLable,'-',{
        text : '详细信息',
        tooltip : '按照校区分类的上机排情况',
        iconCls : 'data',
        onClick : function(data){
        	var params = {};
  		  	params.languagenum = selectedLanguageNum;
  		  	statisticsByCampusStore.load({params:params});
  		  statisticsByCampusWindow.show();
  		  }
    }],
    bbar: [
       {
    	   text : '新增上机教室',
    	   tooltip : '新增上机考场',
    	   iconCls : 'edit',
    	   onClick : function(data){
    		   sectionCombo.fireEvent("afterRender");
    		   arrangeWindow.show();
    	   }
       },'-',
       {
    	   text:  '自动安排',
    	   tooltip: '给考生安排教室',
    	   onClick: function(data){
    		   Ext.MessageBox.wait('操作进行中...','请等待');
    		   var languagenum = selectedLanguageNum;
    		   OperateExamArrangeController.autoArrange(languagenum,function(data) {
    			   var jsonData = Ext.util.JSON.decode(data);
    			   if(jsonData.success == true) {
    				   arrangeStore.reload();
    				   studentStore.reload();
    				   unarrangedStudentStore.reload();///////////////////
    				   OperateExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),function(data) {
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
    	   
       },'->',
       {
    	   text: '取消所有考生安排',
    	   tooltip: '取消考生安排',
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
			if(arrangeGrid.getSelectionModel().getCount()!=1) {
				Ext.MessageBox.alert('提示',"请选择一个教室");
			}
			else{
				if(selected.id != null){
					params.arrangeid=selected.id;
					params.languagenum=signedLanguageCombo.getValue();
					studentStore.load({params:params});
				}
				else{
					Ext.MessageBox.alert('提示',"ArrangeId 不存在！");
				}
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
 		    OperateExamArrangeController.cancelArrange(languagenum,function(data) {
 			var jsonData = Ext.util.JSON.decode(data);
 			if(jsonData.success == true) {
 				arrangeStore.reload();
 				studentStore.removeAll();
 				unarrangedStudentStore.reload();
 				OperateExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),function(data) {
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
//已安排的学生Store
var studentStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, studentRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : OperateExamArrangeController.loadSpecialArrangedStudent
	}),
	sortInfo:{field: "examnum",direction: "ASC"}
});
var unarrangedStudentStore = new Ext.data.Store({
	reader: new Ext.data.JsonReader({
		totalPropery: 'totalProperty',
		root: 'root'
	},studentRecord),
	proxy: new Ext.ux.data.DWRProxy({
		dwrFunction: OperateExamArrangeController.loadSpecialUnarrangedStudent
	}),
	sortInfo: {
		field: 'examnum',
		direction: 'ASC'
	}
});
//未安排考生panel
var unarrangedStudentGrid = new Ext.grid.GridPanel({
	height: 340,
	width:230,
	id: 'unarrangedStudentGrid',
	store: unarrangedStudentStore,
	title: '未安排考生',
	stripeRows :true,
	autoScroll:true,
	collapsible: false,
	animCollapse: false,
	autoExpandColumn : 'examnum',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width: 38}),
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :80,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :120,
	 	sortable :true
	}
	],
	tbar: [
	    {
		    text : '添加至教室',
		    tooltip : '将考生添加至选中教室',
		    iconCls : 'add',
		    onClick : function() {
		    	if(arrangeGrid.getSelectionModel().getCount()!=1) {
		    		Ext.MessageBox.alert('提示','请选择一条教室记录！');
		    	} else {
		    		var selected = arrangeGrid.getSelectionModel().getSelected().data;
		     		  var readyToArrange = [];
		     		  var temp=unarrangedStudentGrid.getSelectionModel().getSelections();
		     		  for(var i=0;i<temp.length;i++){
		     			  readyToArrange.push(temp[i].data);
		     		  }
		     		  var remain = selected.surplus;
		     		  
		     		  if(selected.id != null){
		     			  if(remain<temp.length) {
		     				 Ext.MessageBox.alert('提示','要安排的学生人数(' + temp.length + ')过多，该教室的余量(' + remain + ')不足！');
		     			  }else {
		     				 OperateExamArrangeController.saveArrangedStudent(readyToArrange,selected.id,function(data){
			     				  var jsonData = Ext.util.JSON.decode(data);
			     				  if (jsonData.success == true) {
			     					  Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
			     						 arrangeStore.reload();
			     						 studentStore.reload();
			     						 unarrangedStudentGrid.getSelectionModel().clearSelections();
			     						 unarrangedStudentStore.reload();
			     					  });
			     				  } else {
			     					  Ext.MessageBox.alert('提示',jsonData.errors.info);
			     				  }
			     				  OperateExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),function(data) {
			    					   statisticsLable.getEl().update(data);
			   					  });
			     			  });
		     				 
		     			  }
		     			  
		     		  }else{
		     			  Ext.MessageBox.alert('提示',"ArrangeId不存在!");
		     		  }
		     	   }
		        
		    }
	    }
	],
	listeners:{
    	rowdblclick : function(data){
    	},
    	afterRender: function(data){
    		
    	}
    }
});
//已安排的学生Panel
var studentGrid = new Ext.grid.GridPanel({
	height:380,
	width:230,
	id : 'studentGrid',
	store : studentStore,
	title : '已安排考生',
	stripeRows :true,
	autoScroll:true,
	collapsible: false,
	animCollapse: false,
	autoExpandColumn : 'examnum',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width: 25}),
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :80,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :120,
	 	sortable :true
	}
	],
	tbar: [
	   {
		   text : '删除',
           tooltip : '将考生从教室中删除',
           iconCls : 'remove',
           onClick : function() {
        	   if(studentGrid.getSelectionModel().getCount()==0){
         		   Ext.MessageBox.alert('提示',"没有选择学生!");
         	   }else{
         		  var selected = arrangeGrid.getSelectionModel().getSelected().data;
         		  var readyToDelete = [];
         		  var temp=studentGrid.getSelectionModel().getSelections();
         		  for(var i=0;i<temp.length;i++){
         			 readyToDelete.push(temp[i].data);
         		  }
         		  if(selected.id != null){	
         			  OperateExamArrangeController.deleteArrangedStudent(readyToDelete,function(data){
         				  var jsonData = Ext.util.JSON.decode(data);
         				  if (jsonData.success == true) {
         					 Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
         						  	arrangeStore.reload();
		     						 studentStore.reload();
		     						 unarrangedStudentGrid.getSelectionModel().clearSelections();
		     						 unarrangedStudentStore.reload();
         					  });
         				  } else {
         					  Ext.MessageBox.alert('提示',jsonData.errors.info);
         				  }
         				  OperateExamArrangeController.getStatisticsByLang(signedLanguageCombo.getValue(),function(data) {
         					  statisticsLable.getEl().update(data);
         				  });
         			  });
         		  }else{
         			  Ext.MessageBox.alert('提示',"ArrangeId不存在!");
         		  }
         	   }
           }
		}
    ],

    listeners:{
    	rowdblclick : function(data){
    	},
    	afterRender: function(data){
    		this.setHeight(arrangeGrid.getHeight() - unarrangedStudentGrid.getHeight());
    	}
    }
 });

var selectedCountLable = new Ext.form.Label({
	id : "selectedCountLable",
	text : "dddd",
	width : 100,
	autoShow : true,
	autoWidth : true,
	autoHeight : true
});
var specialCampus=new Ext.form.Checkbox({ 
	boxLabel: "只显示考场所在校区的学生", 
	name: "specialCampus",
	inputValue: 1 ,
	listeners:{
		check:function(data){
			selectedExamroomNum="0";
			var params={};
			params.languagenum=signedLanguageCombo.getValue();
			if(specialCampus.getValue()==true){
				if(arrangeGrid.getSelectionModel().getCount()!=1){
         		   Ext.MessageBox.alert('提示',"请选择一条考场编排记录！");
         	   }else{
         		  params.arrangeid=arrangeGrid.getSelectionModel().getSelected().data.id;
         	   }
			}else{
				params.arrangeid="";
			}
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
    items : [unarrangedStudentGrid,studentGrid]
});
function pageInit() {
	Ext.QuickTips.init();
	new Ext.Viewport({
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [arrangeGrid,table],
		renderTo :Ext.getBody()
	});
}