var gridColumnList=[];
var gridRecordList=[];

var institutionnum = "";

var collegeRecord = Ext.data.Record.create([ {
	name : 'collegename'
}, {
	name : 'collegenum'
} ]);



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

function collegeSignUpInfoPageInit(institution) {
		institutionnum = institution;
		languageListStore.load({callback: function(){
		i=1;
	//	gridRecordList[0]={name:"collegenum"};
		gridRecordList[0]={name:"collegename"};
		languageListStore.each(function(record){
				gridRecordList[i]={name:record.get("languagenum")+"count"};
				i++;

		});
		gridRecordList[i]={name:"count"};
		gridRecordList[i+1]={name:"currentState"};
		
		i=2;
		gridColumnList[0]=new Ext.grid.RowNumberer();
		gridColumnList[1]={id:"collegename",header:"院系名称",dataIndex:"collegename",width:150,sortable:true};
		levelNum = "";
		languageListStore.each(function(record){
			gridColumnList[i]={id:  eval("\""+record.get("languagenum")+"count"+"\""),header: eval("\""+record.get("languagenum")+"\""),
					dataIndex: eval("\""+record.get("languagenum")+"count"+"\""),width:50,sortable:true};
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
			     dwrFunction : StudentController.calcStuForCollege
			  })
		});
		statisticsGrid=new Ext.grid.GridPanel({
			region:'center',
			id : 'statisticsGrid',
			title : '院系报名统计表',
			store : statisticsStore,
		 loadMask :true,
			stripeRows :true,
			autoScroll:true,
			//autoExpandColumn : 'institutionname',
			viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
			columns : gridColumnList,
			tbar:[{
			text: '导出院系报名统计表',
			iconCls : 'upload-icon',
			onClick : function(){
				exportYXBMTJB();
			}
			}],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : institutionnum
						//	category : "school",
						//	direct : true
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


function exportYXBMTJB(){
	var f = document.getElementById('exportYXBMTJB');
	f.action = '../exportYXBMTJB.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}





/*


var collegesignup= Ext.data.Record.create([{
			name : 'collegename'
		}, {
			name : 'sumstudentcount'
		}, {
			name : '11studentcount'
		}, {
			name : '12studentcount'
		}, {
			name : '21studentcount'
		}, {
			name : '22studentcount'
		}, {
			name : '23studentcount'
		}, {
			name : '24studentcount'
		}, {
			name : '25studentcount'
		}, {
			name : '26studentcount'
		}, {
			name : '31studentcount'
		}, {
			name : '32studentcount'
		}, {
			name : '33studentcount'
		}, {
			name : '34studentcount'
		}, {
			name : '35studentcount'
		}]);

var collegesignupstore = new Ext.data.JsonStore({
		fields : collegesignup
		});

var collegesignupsgrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'collegesignupsgrid',
	store : collegesignupstore,
	title : '院系报名统计表',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
//	autoExpandColumn : 'collegename',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [new Ext.grid.RowNumberer(), {
				id : 'collegename',
				header : '院系名称',
				dataIndex : 'collegename',
				width : 270,
				sortable : true
			}, {
				header : '11',
				dataIndex : '11studentcount',
				width : 60,
				sortable : true
			}, {
				header : '12',
				dataIndex : '12studentcount',
				width : 60,
				sortable : true
			}, {
				header : '21',
				dataIndex : '21studentcount',
				width : 60,
				sortable : true
			}, {
				header : '22',
				dataIndex : '22studentcount',
				width : 60,
				sortable : true
			}, {
				header : '23',
				dataIndex : '23studentcount',
				width : 60,
				sortable : true
			}, {
				header : '24',
				dataIndex : '24studentcount',
				width : 60,
				sortable : true
			}, {
				header : '25',
				dataIndex : '25studentcount',
				width : 50,
				sortable : true
			}, {
				header : '26',
				dataIndex : '26studentcount',
				width : 60,
				sortable : true
			}, {
				header : '31',
				dataIndex : '31studentcount',
				width : 60,
				sortable : true
			}, {
				header : '32',
				dataIndex : '32studentcount',
				width : 60,
				sortable : true
			}, {
				header : '33',
				dataIndex : '33studentcount',
				width : 60,
				sortable : true
			}, {
				header : '34',
				dataIndex : '34studentcount',
				width : 60,
				sortable : true
			}, {
				header : '35',
				dataIndex : '35studentcount',
				width : 60,
				sortable : true
			}, {
				header : '合计',
				dataIndex : 'sumstudentcount',
				width : 80,
				sortable : true
			}],
			tbar : [{
     	        text: '导出院系报名统计表',
   	        iconCls : 'upload-icon',
   	        scope : this,
   				handler : function() {
   					exportYXBMTJBXls(); 
   				}			
   			}] 
});


function collegeSignUpInfoPageInit() {
	new Ext.Viewport({
				layout : 'border',
				hideMode : Ext.isIE ? 'offsets' : 'display',
				items : [collegesignupsgrid],
				renderTo : Ext.getBody()
			});
	StudentController.getCollegeSignUpInfo(function(data){
		if(data){
			collegesignupstore.loadData(data);
		}else{
			Ext.MessageBox.alert('提示', "没有数据！");
		}
	});
}


function exportYXBMTJBXls(){
	var f = document.getElementById('exportYXBMTJBExcel');
	f.action = '../exportYXBMTJBExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}


*/