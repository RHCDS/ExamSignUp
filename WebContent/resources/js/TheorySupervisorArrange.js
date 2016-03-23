//
var examroomRecord = Ext.data.Record.create([
    {
    	name:'examroomnum'
	}, {
		name : 'examroom'
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
 		name: 'supervisor'
 	}, {
 		name : 'language'
 	}, {
 		name : 'arrangeid'
 	}               
]);
//考场安排的Store
var examroomStore = new Ext.data.GroupingStore( {
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty:'examroomnum'
	}, examroomRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : TheorySupervisorArrangeController.loadArrangeInfo
	}),
	sortInfo: {
		field: 'examroomnum',
		direction: "ASC"
	},
	groupField: 'sectioninfo'
});
//panel
var examroomGrid = new Ext.grid.GridPanel({
	region:"center",
	store: examroomStore,
	columns: [new Ext.grid.RowNumberer({width:35}),{
		id: 'sectioninfo',
		header: "场次信息",
		width: 20,
		sortable: true,
		dataIndex: 'sectioninfo'
	},{
		id: 'examroom',
		header: "考场",
		width: 40,
		sortable: true,
		dataIndex: 'examroom'
	},{
		id: 'roomlocation',
		header: "教室位置",
		width: 80,
		sortable: true,
		dataIndex: 'roomlocation'
	},{
		id: 'language',
		header: "语种",
		width: 90,
		sortable: true,
		dataIndex: 'language'
	},{
		id: 'supervisor',
		header: "监考老师",
		width: 100,
		dataIndex: 'supervisor'
	},{
		header: "学生人数",
		width: 40,
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
		groupTextTpl: '{text} ({["共有"]}{[values.rs.length]}{["个考场."]})'
	}),

	frame: true,
	width: 750,
	height: 450,
	collapsible: false,
	animCollapse: false,
	title: '理论监考老师编排',
	tbar: ['->',
	    {
	    	text: '共享监考老师',
	    	tooltip: '共享监考老师',
	    	iconCls: 'edit',
	    	onClick : function() {
	    		var readyToMerger = [];
	    		var selectModel = examroomGrid.getSelectionModel();
	    	
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
	    		//判断是否只有一个考场有监考老师
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
	    		
	    		TheorySupervisorArrangeController.MergeSupervisor(readyToMerger,key,function(data) {
	    			var jsonData = Ext.util.JSON.decode(data);
	       			   if(jsonData.success == true) {
	       				   examroomStore.reload();
	       				   arrangedSupervisorStore.reload();
	       				   unarrangedSupervisorStore.reload();
	       				   Ext.MessageBox.alert('提示',jsonData.errors.info);
	       			   } else {
	       				   Ext.MessageBox.alert('提示',jsonData.errors.info);
	       				   examroomStore.reload();
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
    	   tooltip: '自动安排理论监考老师',
    	   onClick: function(data) {
    	       Ext.MessageBox.wait('正在执行操作...','请等待');
    	       if(unarrangedSupervisorStore.getCount() == 0) {
    	    	   Ext.MessageBox.alert('提示','没有监考老师可以安排！');
    	    	   return;
    	       }
       		   TheorySupervisorArrangeController.autoArrange(function(data) {
       			   var jsonData = Ext.util.JSON.decode(data);
       			   if(jsonData.success == true) {
       				   examroomStore.reload();
       				   arrangedSupervisorStore.reload();
       				   unarrangedSupervisorStore.reload();
       				   Ext.MessageBox.alert('提示',jsonData.errors.info);
       			   } else {
       				   Ext.MessageBox.alert('提示',jsonData.errors.info);
       				   examroomStore.reload();
       				   arrangedSupervisorStore.removeAll();
       				   unarrangedSupervisorStore.reload();
       			   }
       		   });	
    	   }
       }, {
    	   text: '取消所有监考安排',
    	   tooltip: '取消理论监考安排',
    	   onClick: function(data) {
    		   Ext.MessageBox.confirm('确认','取消所有监考老师安排?',cancelArrange);
    	   }
       }
    ],
	listeners : {
		afterRender : function(grid) {
			this.getColumnModel().setHidden(1,true);
			TheorySupervisorArrangeController.checkArrangeStatus(function(data) {
				if(data == "true") {
					examroomStore.load({callback: function() {
						if(examroomStore.getTotalCount() > 0) {
							examroomGrid.getSelectionModel().selectFirstRow();
							var selected = examroomGrid.getSelectionModel().getSelected().data;
							var params = {};
							params.examroomnum = selected.examroomnum;
							params.sectionid = selected.sectionid;
							if(selected.arrangeid != null){
								arrangedSupervisorStore.load({params:params});
								unarrangedSupervisorStore.load({params:{sectionid:selected.sectionid}});
							}
						}
						
					}});
				} else {
					Ext.MessageBox.alert('提示',"请先确认完成所有语种的理论考场编排！",function(){
		            	var temp = window.top.tabPanel.getActiveTab( );
						window.top.tabPanel.remove(temp);
					});
				}
			});
		},
		rowclick:function(grid){
			var selected = examroomGrid.getSelectionModel().getSelected().data;
			/*if(examroomGrid.getSelectionModel().getCount()!=1) {
				Ext.MessageBox.alert('提示',"请选择一个考场！");
			}*/
			var params = {};
			params.examroomnum = selected.examroomnum;
			params.sectionid = selected.sectionid;
			if(selected.arrangeid != null){
				arrangedSupervisorStore.load({params:params});
				unarrangedSupervisorStore.load({params:{sectionid:selected.sectionid}});
			}
			else{
				Ext.MessageBox.alert('提示',"理论考试编排错误！");
			}
			
		}
	}
});
function cancelArrange(confirm) {
	if(confirm == "yes") {
		if(examroomStore.getTotalCount()==0) {
			Ext.MessageBox.alert('提示','目前没有监考老师安排，该操作无效！');
		}else {
			Ext.MessageBox.wait('操作进行中...','请等待');
 		    TheorySupervisorArrangeController.cancelArrange( function(data) {
 		    	var jsonData = Ext.util.JSON.decode(data);
 		    	if(jsonData.success == true) {
 		    		examroomStore.reload();
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
    },{
    	name : 'primaryflag',
    	convert:function(data){
    		if(data==1) return "是";
    		else return "否";
    	}
    }
]);
var arrangedSupervisorStore = new Ext.data.GroupingStore( {
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty:'id'
	}, arrangedSupervisorRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : TheorySupervisorArrangeController.loadArrangedSupervisor
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
	columns : [new Ext.grid.RowNumberer(),
	{
		id : 'name',
		header : '姓名',
		dataIndex :'name',
	 	width :55,
		sortable :true
	},	{
		id : 'contact',
		header : '联系方式',
		dataIndex :'contact',
		width :95,
		sortable :true
	},{
		id : 'primaryflag',
		header : '主监考',
		dataIndex :'primaryflag',
		width :60,
		sortable :true
	}
	],
	tbar: [
	    {
		    text : '删除',
		    tooltip : '删除监考老师',
		    iconCls : 'remove',
		    onClick : function() {
		    	if(examroomGrid.getSelectionModel().getCount()!=1) {
		    		Ext.MessageBox.alert('提示','请选择一条考场记录！');
		    	} else {
		    		var selected = examroomGrid.getSelectionModel().getSelected().data;
		     		  var readyToDelete = [];
		     		  var temp=arrangedSupervisorGrid.getSelectionModel().getSelections();
		     		  for(var i=0;i<temp.length;i++){
		     			  readyToDelete.push(temp[i].data);
		     		  }
		     		  if(selected.arrangeid != null){	
		     			  TheorySupervisorArrangeController.deleteArrangedSupervisor(readyToDelete,selected.examroomnum,function(data){
		     				  var jsonData = Ext.util.JSON.decode(data);
		     				  if (jsonData.success == true) {
		     					  Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
		     						 arrangedSupervisorStore.reload();
		     						 examroomStore.reload();
		     						 unarrangedSupervisorStore.reload();
		     					  });
		     				  } else {
		     					  Ext.MessageBox.alert('提示',jsonData.errors.info);
		     				  }
		     			  });
		     		  }else{
		     			  Ext.MessageBox.alert('提示',"请确认已经完成所有语种理论考场编排工作，再进行此操作！");
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
		dwrFunction : TheorySupervisorArrangeController.loadUnarrangedSupervisor
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
	columns : [new Ext.grid.RowNumberer({width: 25}),
	{
		id : 'name',
		header : '姓名',
		dataIndex :'name',
	 	width :55,
		sortable :true
	},	{
		id : 'contact',
		header : '联系方式',
		dataIndex :'contact',
		width :95,
		sortable :true
	},{
		id : 'primaryflag',
		header : '主监考',
		dataIndex :'primaryflag',
		width :60,
		sortable :true
	}
	],
	tbar: [
	    {
		    text : '添加',
		    tooltip : '将监考老师安排到指定考场',
		    iconCls : 'add',
		    onClick : function() {
		    	unarrangedSupervisorGrid.fireEvent('rowdblclick', unarrangedSupervisorGrid);
		    }
	    }
	],
	listeners:{
    	rowdblclick : function(grid) {
	    	if(examroomGrid.getSelectionModel().getCount()!=1) {
	    		Ext.MessageBox.alert('提示','请选择一条考场记录！');
	    	} else if(unarrangedSupervisorGrid.getSelectionModel().getCount() !=1 ) {
	    		Ext.MessageBox.alert('提示','请选择一条可安排监考老师记录！');
	    	} else if(arrangedSupervisorGrid.getStore().getCount() == 2) { 
	    		Ext.MessageBox.alert('提示','该考场已经有两个监考老师，无需添加！');
	    	} else {
	    		var selected = examroomGrid.getSelectionModel().getSelected().data;
	     		if(selected.arrangeid != null){
	     			var readyToAdd = unarrangedSupervisorGrid.getSelectionModel().getSelected().data.id;
	     			TheorySupervisorArrangeController.arrangeSupervisor(readyToAdd,selected.examroomnum,selected.arrangeid,function(data){
     					var jsonData = Ext.util.JSON.decode(data);
     					if (jsonData.success == true) {
     						Ext.MessageBox.alert('提示',jsonData.errors.info,function() {
     							arrangedSupervisorStore.reload();
     							examroomStore.reload();
     							unarrangedSupervisorStore.reload();
     						});
     					} else {
     						Ext.MessageBox.alert('提示',jsonData.errors.info);
     					}
     				});
	     		}else{
	     			Ext.MessageBox.alert('提示',"请确认已经完成理论考试编排工作！");
	     		  }
	     	   }
	        
	    },
    	afterRender: function(data){
    		this.setHeight(examroomGrid.getHeight() - arrangedSupervisorGrid.getHeight());
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
		items : [examroomGrid,table],
		renderTo :Ext.getBody()
	});
}
