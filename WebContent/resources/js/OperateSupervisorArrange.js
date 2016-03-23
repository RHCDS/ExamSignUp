//
var arrangeRecord = Ext.data.Record.create([
    {
    	name:'id'
	}, {           
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
 		name: 'examrooms'
 	}, {
 		name: 'supervisor'
 	}, {
 		name : 'language'
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
		dwrFunction : OperateSupervisorArrangeController.loadArrangeInfo
	}),
	sortInfo: {
		field: 'roomlocation',
		direction: "ASC"
	},
	groupField: 'sectioninfo'
});
//panel
var arrangeGrid = new Ext.grid.GridPanel({
	region:"center",
	store: arrangeStore,
	columns: [new Ext.grid.RowNumberer(),{
		id: 'sectioninfo',
		header: "场次信息",
		width: 20,
		sortable: true,
		dataIndex: 'sectioninfo'
	},{
		id: 'roomlocation',
		header: "教室位置",
		width: 80,
		sortable: true,
		dataIndex: 'roomlocation'
	},{
		id: 'language',
		header: "语种",
		width: 80,
		sortable: true,
		dataIndex: 'language'
	},{
		id: 'supervisor',
		header: "监考老师",
		width: 80,
		dataIndex: 'supervisor'
	},{
		header: "学生人数",
		width: 30,
		sortable: true,
		dataIndex: 'capacity'
	},{
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
	title: '上机监考老师编排',
	tbar: ['->',
	{
    	text: '共享监考老师',
    	tooltip: '共享监考老师',
    	iconCls: 'edit',
    	onClick : function() {
    		var readyToMerger = [];
    		var selectModel = arrangeGrid.getSelectionModel();
    	
    		if(selectModel.getCount() == 1 || selectModel.getCount() == 0) {
    			Ext.MessageBox.alert('提示','请选择两个或两个以上的考场，可以通过按住CTRL键选择多个考场！');
    			return;
    		}
    		var temp = selectModel.getSelections();
    		var length = temp.length;
    		//判断是否只有一个考场有监考老师
    		var flag = true;
    		var sectionid = temp[0].data.sectionid;
    		for(var i=1;i<length;i++) {
    			if(sectionid != temp[i].data.sectionid) {
    				flag = false;
    			}
    		}
    		if(flag == false) {
    			Ext.MessageBox.alert('提示','请确认进行操作的考场都是一个场次！');
    			return;
    		}
 
    		var count = 0;
    		var key;
    		for(var i=0;i<length;i++) {
    			if(temp[i].data.supervisor.length != 0) {
    				count ++;
    				key = i;
    			}
    		}
    		if(count == 0) {
    			Ext.MessageBox.alert('提示','请确认所选考场中有一个考场有监考老师安排');
    			return;
    		}
    		if(count > 1) {
    			Ext.MessageBox.alert('提示','请确认所选考场中只有一个考场有监考老师安排');
    			return;
    		}
    		readyToMerger.push(temp[0].data);
    		var sectionid = temp[0].data.sectionid;
    		var physicexamroomid = temp[0].data.physicexamroomid;
    		for(var i=1;i<length;i++) {
    			var data = temp[i].data;
    			if(data.sectionid != sectionid) {
    				Ext.MessageBox.alert('提示','请确认要共享监考老师的考场都是一个场次，否则无法共享！');
    				return ;
    			}
    			if(data.physicexamroomid != physicexamroomid) {
    				Ext.MessageBox.alert('提示','请确认要共享监考老师的教室位置是相同的，否则无法共享！');
    				return;
    			}
    			readyToMerger.push(data);
    		}
    		
    		OperateSupervisorArrangeController.MergeSupervisor(readyToMerger,key,function(data) {
    			var jsonData = Ext.util.JSON.decode(data);
       			if(jsonData.success == true) {
       				arrangeStore.reload();
       				arrangedSupervisorStore.reload();
       				unarrangedSupervisorStore.reload();
       				Ext.MessageBox.alert('提示',jsonData.errors.info);
       			} else {
       				Ext.MessageBox.alert('提示',jsonData.errors.info);
       				arrangeStore.reload();
       				arrangedSupervisorStore.reload();
       				unarrangedSupervisorStore.reload();
       			}
    		});
    	}
    }
	],
    bbar: [
	{
		text: '自动安排',
		tooltip: '自动安排上机监考老师',
		onClick: function(data) {
			Ext.MessageBox.wait('正在执行操作...','请等待');
			OperateSupervisorArrangeController.autoArrange(function(data) {
				var jsonData = Ext.util.JSON.decode(data);
				if(jsonData.success == true) {
					arrangeStore.reload();
       				arrangedSupervisorStore.reload();
       				unarrangedSupervisorStore.reload();
					Ext.MessageBox.alert('提示',jsonData.errors.info);
				} else {
					Ext.MessageBox.alert('提示',jsonData.errors.info);
					arrangeStore.reload();
					arrangedSupervisorStore.reload();
       				unarrangedSupervisorStore.reload();
				}
			});	
		}
	},{
		text: '取消所有监考安排',
		tooltip: '取消上机监考安排',
		onClick: function(data) {
			Ext.MessageBox.confirm('确认','取消所有监考老师安排?',cancelArrange);
		}
	}
    ],
	listeners : {
		afterRender : function(grid) {
			this.getColumnModel().setHidden(1,true);
			OperateSupervisorArrangeController.checkArrangeStatus(function(data) {
				if(data == "true") {
					arrangeStore.load();
				} else {
					Ext.MessageBox.alert('提示',"请先确认完成所有语种的上机考场编排！",function(){
		            	var temp = window.top.tabPanel.getActiveTab( );
						window.top.tabPanel.remove(temp);
					});
				}
			});
		},
		rowclick:function(grid){
			var selected = arrangeGrid.getSelectionModel().getSelected().data;
			if(arrangeGrid.getSelectionModel().getCount()!=1) {
				return;
			}
			else{
				if(selected.id != null){
					arrangedSupervisorStore.load({params:{arrangeid:selected.id}});
					unarrangedSupervisorStore.load({params:{sectionid:selected.sectionid}});
				}
				else{
					Ext.MessageBox.alert('提示',"ArrangeId 不存在！");
				}
			}
		}
	}
});
function cancelArrange(confirm) {
	if(confirm == "yes") {
		if(arrangeStore.getTotalCount()==0) {
			Ext.MessageBox.alert('提示','目前没有考场安排，该操作无效！');
		}else {
			Ext.MessageBox.wait('操作进行中...','请等待');
 		    OperateSupervisorArrangeController.cancelArrange( function(data) {
 		    	var jsonData = Ext.util.JSON.decode(data);
 		    	if(jsonData.success == true) {
 		    		arrangeStore.reload();
 					arrangedSupervisorStore.removeAll();
 					unarrangedSupervisorStore.reload();
 					Ext.MessageBox.hide();
 					Ext.MessageBox.alert('提示',jsonData.errors.info);
 		    	}
 		    	else {
 		    		Ext.MessageBox.hide();
 		    		Ext.MessageBox.alert('提示','操作失败');
 		    	}
 		    });
		}
	}
}
var arrangedSupervisorRecord = Ext.data.Record.create([
    {
    	name: 'id'
    },{
    	name: 'name'
    },{
    	name: 'contact'
    }
]);
var arrangedSupervisorStore = new Ext.data.GroupingStore( {
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty:'id'
	}, arrangedSupervisorRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : OperateSupervisorArrangeController.loadArrangedSupervisor
	})
});

var arrangedSupervisorGrid = new Ext.grid.GridPanel({
	height: 200,
	width: 230,
	id: 'arrangedSupervisorGrid',
	store: arrangedSupervisorStore,
	title: '监考老师',
	stripeRows :true,
	autoScroll:true,
	collapsible: false,
	animCollapse: false,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width: 30}),
	{
		id : 'name',
		header : '姓名',
		dataIndex :'name',
	 	width :80,
		sortable :true
	},	{
		id : 'contact',
		header : '联系方式',
		dataIndex :'contact',
		width :120,
		sortable :true
	}
	],
	tbar: [
	    {
		    text : '删除',
		    tooltip : '删除监考老师',
		    iconCls : 'remove',
		    onClick : function() {
		    	if(arrangeGrid.getSelectionModel().getCount()!=1) {
		    		Ext.MessageBox.alert('提示','请选择一条教室记录！');
		    	} else {
		    		var selected = arrangeGrid.getSelectionModel().getSelected().data;
		     		  var readyToDelete = [];
		     		  var temp=arrangedSupervisorGrid.getSelectionModel().getSelections();
		     		  for(var i=0;i<temp.length;i++){
		     			  readyToDelete.push(temp[i].data);
		     		  }
		     		  if(selected.id != null){	
		     			  OperateSupervisorArrangeController.deleteArrangedSupervisor(readyToDelete,selected.id,function(data){
		     				  var jsonData = Ext.util.JSON.decode(data);
		     				  if (jsonData.success == true) {
		     					  Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
		     						 arrangedSupervisorStore.reload();
		     						 arrangeStore.reload();
		     						 unarrangedSupervisorStore.reload();
		     					  });
		     				  } else {
		     					  Ext.MessageBox.alert('提示',jsonData.errors.info);
		     				  }
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
    		
    	}
    }
});
var unarrangedSupervisorStore = new Ext.data.GroupingStore( {
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty:'id'
	}, arrangedSupervisorRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : OperateSupervisorArrangeController.loadUnarrangedSupervisor
	})
});
var unarrangedSupervisorGrid = new Ext.grid.GridPanel({
	height: 340,
	width: 230,
	id: 'unarrangedSupervisorGrid',
	store: unarrangedSupervisorStore,
	title: '可安排监考老师',
	stripeRows :true,
	autoScroll:true,
	collapsible: false,
	animCollapse: false,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width: 30}),
	{
		id : 'name',
		header : '姓名',
		dataIndex :'name',
	 	width :80,
		sortable :true
	},	{
		id : 'contact',
		header : '联系方式',
		dataIndex :'contact',
		width :120,
		sortable :true
	}
	],
	tbar: [
	    {
		    text : '添加',
		    tooltip : '将监考老师安排到指定教室',
		    iconCls : 'add',
		    onClick : function() {
		    	unarrangedSupervisorGrid.fireEvent('rowdblclick', unarrangedSupervisorGrid);
		    }
	    }
	],
	listeners:{
    	rowdblclick : function() {
	    	if(arrangeGrid.getSelectionModel().getCount()!=1) {
	    		Ext.MessageBox.alert('提示','请选择一条教室记录！');
	    	} else if(unarrangedSupervisorGrid.getSelectionModel().getCount() !=1 ) {
	    		Ext.MessageBox.alert('提示','请选择一条可安排监考老师记录！');
	    	} else {
	    		var selected = arrangeGrid.getSelectionModel().getSelected().data;
	     		if(selected.id != null){
	     			var readyToAdd = unarrangedSupervisorGrid.getSelectionModel().getSelected().data.id;
	     			OperateSupervisorArrangeController.arrangeSupervisor(readyToAdd,selected.id,function(data){
     					var jsonData = Ext.util.JSON.decode(data);
     					if (jsonData.success == true) {
     						Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
     							arrangedSupervisorStore.reload();
     							arrangeStore.reload();
     							unarrangedSupervisorStore.reload();
     						});
     					} else {
     						Ext.MessageBox.alert('提示',jsonData.errors.info);
     					}
     				});
	     		}else{
	     			Ext.MessageBox.alert('提示',"ArrangeId不存在!");
	     		  }
	     	   }
	        
	    },
    	afterRender: function(data){
    		this.setHeight(arrangeGrid.getHeight() -arrangedSupervisorGrid.getHeight());
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
    items : [arrangedSupervisorGrid,unarrangedSupervisorGrid]
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
