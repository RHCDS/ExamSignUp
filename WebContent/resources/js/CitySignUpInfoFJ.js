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

function SchoolStateManageInit(institution,ByLanguage) {
		parentinstitutionnum = institution;
		languageListStore.load({callback: function(){
		i=2;
		gridRecordList[0]={name:"institutionnum"};
		gridRecordList[1]={name:"institutionname"};
		var levelNum = "";
		languageListStore.each(function(record){
			if(!(record.get("languagenum").substr(0,1)==levelNum)){
				gridRecordList[i]={name:record.get("languagenum").substr(0,1)};
				i++;
			}
			levelNum = record.get("languagenum").substr(0,1);
		});
		gridRecordList[i]={name:"count"};
		gridRecordList[i+1]={name:"currentState"};
		
		i=3;
		gridColumnList[0]=new Ext.grid.RowNumberer();
		gridColumnList[1]={id:"institutionum",header:"学校代码",dataIndex:"institutionnum",width:80,sortable:true};
		gridColumnList[2]={id:"institutionname",header:"学校名称",dataIndex:"institutionname",width:150,sortable:true};
//		gridColumnList[2]={id:"currentState",header:"学校当前状态",dataIndex:"currentState",width:100,sortable:true};
		levelNum = "";
		languageListStore.each(function(record){
			if(!(record.get("languagenum").substr(0,1)==levelNum)){
				var listheader = "";
				var levelname = record.get("languagenum").substr(0,1);
				switch(record.get("languagenum").substr(0,1)){
					case "1" : 
						listheader = "一级";
						break;
					case "2" : 
						listheader = "二级";
						break;
					case "3" : 
						listheader = "三级";
						break;
					case "4" : 
						listheader = "四级";
						break;
					default : 
						listheader = "五级";
				}
				gridColumnList[i]={id:  eval("\""+levelname+"\""),header: eval("\""+listheader+"\""),
						dataIndex: eval("\""+levelname+"\""),width:100,sortable:true};
				i++;
			}
			levelNum = record.get("languagenum").substr(0,1);
		});
		gridColumnList[i]={id:"count",header:"共计",dataIndex:"count",width:100,sortable:true};
		
		statisticsRecord= new Ext.data.Record.create(gridRecordList);
		statisticsStore = new Ext.data.Store( {
			reader : new Ext.data.JsonReader({
		         totalProperty : 'totalProperty',
		         root : 'root'
		      }, statisticsRecord),
			proxy : new Ext.ux.data.DWRProxy({
			     dwrFunction : StudentController.AllProvinceStuInfoSum
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
			tbar:[{
			text: '导出统计表',
			iconCls : 'upload-icon',
			onClick : function(){
				exportCitySignUpInfoFJ();
			}
			}],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : parentinstitutionnum,
							category : "school",
							isByLanguage : ByLanguage
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

function exportCitySignUpInfoFJ(){
	var f = document.getElementById('exportCitySignUpInfoFJ');
	f.action = '../exportCitySignUpInfoFJ.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}