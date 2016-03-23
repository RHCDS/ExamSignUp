var gridColumnList=[];
var gridRecordList=[];

var institutionnum = "";

var gradeRecord = Ext.data.Record.create([ {
	name : 'gradename'
} ]);

//var parentcollegeStore = new Ext.data.Store({
//	reader : new Ext.data.JsonReader({
//		totalProperty : 'totalProperty',
//		root : 'root'
//	}, parentcollegeRecord),
//	proxy : new Ext.ux.data.DWRProxy({
//		dwrFunction : InstitutionController.loadChildInstitution
//	})
//});

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

function gradeSignUpInfoPageInit(institution) {
		institutionnum = institution;
		languageListStore.load({callback: function(){
		i=1;
		gridRecordList[0]={name:"gradename"};
		languageListStore.each(function(record){
				gridRecordList[i]={name:record.get("languagenum")+"count"};
				i++;
		});
		gridRecordList[i]={name:"count"};
		gridRecordList[i+1]={name:"currentState"};
		
		i=2;
		gridColumnList[0]=new Ext.grid.RowNumberer();
		gridColumnList[1]={id:"gradename",header:"年级",dataIndex:"gradename",width:40,sortable:true};
		levelNum = "";
		languageListStore.each(function(record){
			gridColumnList[i]={id:  eval("\""+record.get("languagenum")+"count"+"\""),header: eval("\""+record.get("languagenum")+"\""),
					dataIndex: eval("\""+record.get("languagenum")+"count"+"\""),width:50,sortable:true};
			i++;
			
		});

		
		statisticsRecord= new Ext.data.Record.create(gridRecordList);
		statisticsStore = new Ext.data.Store( {
			reader : new Ext.data.JsonReader({
		         totalProperty : 'totalProperty',
		         root : 'root'
		      }, statisticsRecord),
			proxy : new Ext.ux.data.DWRProxy({
			     dwrFunction : StudentController.calcStuForGrade
			  })
		});
		statisticsGrid=new Ext.grid.GridPanel({
			region:'center',
			id : 'statisticsGrid',
			title : '年级报名统计表',
			store : statisticsStore,
		 loadMask :true,
			stripeRows :true,
			autoScroll:true,
			//autoExpandColumn : 'institutionname',
			viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
			columns : gridColumnList,
			tbar:[{
			text: '导出年级报名统计表',
			iconCls : 'upload-icon',
			onClick : function(){
				exportNJBMTJB();
			}
			}],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : institutionnum
						
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





function exportNJBMTJB(){
	var f = document.getElementById('exportNJBMTJB');
	f.action = '../exportNJBMTJB.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}




















/*
var collegesignup = Ext.data.Record.create([{
			name : 'grade'
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
	title : '年级报名统计表',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
//	autoExpandColumn : 'grade',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
	columns : [new Ext.grid.RowNumberer(), {
				id : 'grade',
				header : '年级',
				dataIndex : 'grade',
				width : 240,
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
				width : 60,
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
     	        text: '导出年级报名统计表',
   	        iconCls : 'upload-icon',
   	        scope : this,
   				handler : function() {
   					exportNJBMTJBXls(); 
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
	StudentController.getGradeSignUpInfo(function(data){
		if(data){
			collegesignupstore.loadData(data);
		}else{
			Ext.MessageBox.alert('提示', "没有数据！");
		}
	});
}


function exportNJBMTJBXls(){
	var f = document.getElementById('exportNJBMTJBExcel');
	f.action = '../exportNJBMTJBExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}


*/