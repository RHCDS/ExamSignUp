var gridColumnList=[];
var gridRecordList=[];

var parentinstitutionnum = "";

var parentinstitutionRecord = Ext.data.Record.create([ {
	name : 'institutionname'
}, {
	name : 'institutionnum'
} ]);

var parentinstitutionStore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root'
	}, parentinstitutionRecord),
	proxy : new Ext.ux.data.DWRProxy({
		dwrFunction : InstitutionController.loadChildInstitution
	})
});

var parentinstitutionCombo = new Ext.form.ComboBox({
	
	store: parentinstitutionStore,
	mode: 'local',
	triggerAction: 'all',
	emptyText:'--请选择市地考试院--',
	editable:false,
	valueField:'institutionnum',
	displayField:'institutionname',
	listEmptyText:'没有市地考试院！',
	
	listeners : {
		afterRender : function() {
			parentinstitutionStore.load({
				params : {
					institutionnum : parentinstitutionnum
				}
			});
		},
		select:function(){
			if(parentinstitutionnum==this.getValue())
				return;
			parentinstitutionnum=this.getValue();
			statisticsStore.load({
				params : {
					institutionnum : parentinstitutionnum,
					category : "school"
				}
			});
		  	}
	}
});
var languageListRecord =new Ext.data.Record.create([{
	name :'languagename'
}, {
	name :'languagenum'
}
]);

var languageListStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'languagenum'
  }, languageListRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction :LanguageController.loadlanguageList
  })
});

var statisticsRecord ;
var statisticsStore;
var statisticsGrid;

function SchoolStateManageInit(institution) {
		parentinstitutionnum = institution;
		languageListStore.load({callback: function(){
		i=2;
		gridRecordList[0]={name:"institutionnum"};
		gridRecordList[1]={name:"institutionname"};
		languageListStore.each(function(record){
			gridRecordList[i]={name:record.get("languagenum")};
			i++;
		});
		gridRecordList[i]={name:"count"};
		gridRecordList[i+1]={name:"currentState"};
		
		i=2;
		gridColumnList[0]={id:"institutionum",header:"单位代码",dataIndex:"institutionnum",width:80,sortable:true};
		gridColumnList[1]={id:"institutionname",header:"单位名称",dataIndex:"institutionname",width:150,sortable:true};
//		gridColumnList[2]={id:"currentState",header:"学校当前状态",dataIndex:"currentState",width:100,sortable:true};
		
		languageListStore.each(function(record){
			gridColumnList[i]={id:record.get("languagenum"),header:record.get("languagenum")+record.get("languagename"),
					dataIndex:record.get("languagenum"),width:100,sortable:true};
			i++;
		});
		gridColumnList[i]={id:"count",header:"共计",dataIndex:"count",width:100,sortable:true};
		
		statisticsRecord= new Ext.data.Record.create(gridRecordList);
		statisticsStore = new Ext.data.Store( {
			reader : new Ext.data.JsonReader({
		         totalProperty : 'totalProperty',
		         root : 'root'
		      }, statisticsRecord),
			proxy : new Ext.ux.data.DWRProxy({
			     dwrFunction : StudentController.statisticAllLanguageBySchool
			  })
		});
		statisticsGrid=new Ext.grid.GridPanel({
			region:'center',
			id : 'statisticsGrid',
			title : '考生数据统计表',
			store : statisticsStore,
		 loadMask :true,
			stripeRows :true,
			autoScroll:true,
			//autoExpandColumn : 'institutionname',
			viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
			columns : gridColumnList,
			tbar:[parentinstitutionCombo],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : parentinstitutionnum,
							category : "city"
						}
					});
				}
			}
		 });
		new Ext.Viewport( {
			layout :'border',
			hideMode: Ext.isIE ? 'offsets' : 'display',
			items : [statisticsGrid],
			renderTo :Ext.getBody()
		});
	}
	});
}