var gridColumnList=[];
var gridRecordList=[];
var parentinstitutionnum = "";

var statusLable = new Ext.form.Label({
	id : "statusLable",
	text : " ",
	width : 100,
	autoShow : true,
	autoWidth : true,
	autoHeight : true
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

var sm=new Ext.grid.CheckboxSelectionModel({
	  listeners:{'beforerowselect': function( SelectionModel, rowIndex, keepExisting,record ) {
		           if(record.data.institutionnum==""){   //用户状态不正常
		        	   this.deselectRow(rowIndex );
		              return false;  //不能进行选择
		           }else{   
		               return true;   
		           }
		        }}

		 });

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
		
		i=5;
		gridColumnList[0]=new Ext.grid.RowNumberer();
		gridColumnList[1]=sm;
		gridColumnList[2]={id:"institutionum",header:"学校代码",dataIndex:"institutionnum",width:80,sortable:true};
		gridColumnList[3]={id:"institutionname",header:"学校名称",dataIndex:"institutionname",width:150,sortable:true};
		gridColumnList[4]={id:"currentState",header:"学校当前状态",dataIndex:"currentState",width:100,sortable:true};
		
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
			store : statisticsStore,
		 loadMask :true,
			stripeRows :true,
			autoScroll:true,
			viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
			columns : gridColumnList,
			sm:sm,
			tbar:[{
		        text: '确认所选学校',
		        iconCls : 'edit',
		        scope: this,
		        handler:function(){
		        	InstitutionList="";
		        	if(!statisticsGrid.getSelectionModel().hasSelection()){
		        		Ext.MessageBox.alert('提示','请选择待确认的学校！');
		        		return;
		        	}
		        	var alterMessage="将要确认以下学校:<br>";
		        	var selectArray=statisticsGrid.getSelectionModel().getSelections( );
		        	var institutionList=[];
		        	for(i=0;i<selectArray.length;i++){
		        		alterMessage=alterMessage+selectArray[i].data.institutionname+"<br>";
		        		institutionList[i]=selectArray[i].data;
		        	}
	        		Ext.MessageBox.confirm('提示', alterMessage,
                        function(button) {
                           if (button == 'yes') {
                        	   var tempMask = new Ext.LoadMask(Ext.getBody(), {msg:"正在提交..."});
                        	   InstitutionController.cityManagerSetCheckPass(institutionList,function(data){
                       				tempMask.hide();
                       				var jsonData=Ext.util.JSON.decode(data);
                       				if(jsonData.success==true)
                       				{
                       					Ext.MessageBox.alert('提示',jsonData.errors.info,function(){
                       						statisticsGrid.getSelectionModel().clearSelections();
                       						statisticsStore.load({
                       							params : {
                       								institutionnum : parentinstitutionnum,
                       								category : "school"
                       							}
                       						});
                       					});
                       				}
                       				else
                       				{
                       					Ext.MessageBox.alert('提示',jsonData.errors.info);
                       				}
                        	   });
                           }
                        });
		     }
		    },'-',{
			        text: '市考试院确认',
			        iconCls : 'edit',
			        scope: this,
			        handler:function(){
			        	Ext.MessageBox.confirm('提示', "确定对所管理学校进行全部确认？",function(button) {
		                     if (button == 'yes') {
		                    	  InstitutionController.cityManagerSelfCheckPass(function(data){
		                    		  var jsonData=Ext.util.JSON.decode(data);
		                    		  if(jsonData.success==true)
	                       				{
	                       					Ext.MessageBox.alert('提示',jsonData.errors.info,function(){
	                       						statisticsGrid.getSelectionModel().clearSelections();
	                       						statisticsStore.load({
	                       							params : {
	                       								institutionnum : parentinstitutionnum,
	                       								category : "school"
	                       							}
	                       						});
	                       					});
	                       					InstitutionController.getCityManagerStatus(function(data){
	                   			   			 var jsonData=Ext.util.JSON.decode(data);
	                   			   			 statusLable.getEl().update(jsonData.errors.info);
	                   			   		});
	                       				}
	                       				else
	                       				{
	                       					Ext.MessageBox.alert('提示',jsonData.errors.info);
	                       				}
		                    	  });
		                      }
		                      else
		                      {
		                       			Ext.MessageBox.alert('提示',jsonData.errors.info);
		                       }
		                   });
			        }
		    },'->',statusLable
	    ],
			listeners: {
				afterRender : function() {
					statisticsStore.load({
						params : {
							institutionnum : parentinstitutionnum,
							category : "school"
						}
					});
				}
			}
		 });
		InstitutionController.getCityManagerStatus(function(data){
			 var jsonData=Ext.util.JSON.decode(data);
			 statusLable.getEl().update(jsonData.errors.info);
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