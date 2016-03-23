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

/*var studentRecord = Ext.data.Record.create([ {name:'id'},{
	name : 'name'
}, {
	name : 'examnum'
}]);*/
/*//已安排的学生Store
var studentStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, studentRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : OperateExamArrangeController.loadSpecialArrangedStudent
	}),
	sortInfo:{field: "examnum",direction: "ASC"}
});*/




function pageInit() {
	Ext.QuickTips.init();
	new Ext.Viewport({
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [arrangeGrid],
		renderTo :Ext.getBody()
	});
}