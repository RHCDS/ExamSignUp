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
     dwrFunction :LanguageController.getTheoryLanguageList
  })
});

var statisticsRecord ;
var statisticsStore;
var statisticsGrid;

function calcPaperBagsForExamCollege(institution) {
		parentinstitutionnum = institution;
		languageListStore.load({callback: function(){
		i=2;
		gridRecordList[0]={name:"institutionnum"};
		gridRecordList[1]={name:"institutionname"};
		languageListStore.each(function(record){
			gridRecordList[i]={name:record.get("languagenum")+"bags"};
			i++;
			gridRecordList[i]={name:record.get("languagenum")+"bybags"};
			i++;
		});
		gridRecordList[i]={name:"count"};
		gridRecordList[i+1]={name:"currentState"};
		
		i=3;
		gridColumnList[0]=new Ext.grid.RowNumberer();
		gridColumnList[1]={id:"institutionum",header:"单位代码",dataIndex:"institutionnum",width:80,sortable:true};
		gridColumnList[2]={id:"institutionname",header:"单位名称",dataIndex:"institutionname",width:150,sortable:true};
//		gridColumnList[2]={id:"currentState",header:"学校当前状态",dataIndex:"currentState",width:100,sortable:true};
		levelNum = "";
		languageListStore.each(function(record){
				gridColumnList[i]={id:  eval("\""+record.get("languagenum")+"bags"+"\""),header: eval("\""+record.get("languagenum") + record.get("languagename")+"\""),
						dataIndex: eval("\""+record.get("languagenum")+"bags"+"\""),width:50,sortable:true};
				i++;
				gridColumnList[i]={id:  eval("\""+record.get("languagenum")+"bybags"+"\""),header: eval("\""+record.get("languagenum")+"备用"+"\""),
						dataIndex: eval("\""+record.get("languagenum")+"bybags"+"\""),width:50,sortable:true};
				i++;
			
		});
//		gridColumnList[i]={id:"count",header:"共计",dataIndex:"count",width:100,sortable:true};
		
		statisticsRecord= new Ext.data.Record.create(gridRecordList);
		statisticsStore = new Ext.data.Store( {
			reader : new Ext.data.JsonReader({
		         totalProperty : 'totalProperty',
		         root : 'root'
		      }, statisticsRecord),
			proxy : new Ext.ux.data.DWRProxy({
			     dwrFunction : StudentController.calcPaperBagsForExamCollege
			  })
		});
		statisticsGrid=new Ext.grid.GridPanel({
			region:'center',
			id : 'statisticsGrid',
			title : '考试袋数统计表',
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
				exportCityPaperBags();
			}
			}],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : parentinstitutionnum,
							category : "city",
							direct : true
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

function exportCityPaperBags(){
	var f = document.getElementById('exportCityPaperBags');
	f.action = '../exportCityPaperBags.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}