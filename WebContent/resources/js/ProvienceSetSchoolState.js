var schoolStatusRecord = Ext.data.Record.create([{
		name :'statusid'
	}, {
		name :'statusname'
	}
]);
//语种Store
var schoolStatusStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root',
         idProperty:'statusid'
      }, schoolStatusRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : InstitutionstatusController.loadStatusList
	  })
});
var schoolStatusCombo = new Ext.form.ComboBox({
	store: schoolStatusStore,
	mode: 'local',
	triggerAction: 'all',
	emptyText:'--请选择状态--',
	editable:false,
	valueField:'statusid',
	displayField:'statusname',
	listEmptyText:'没有备选状态！',
	fieldLabel:"当前状态"
});

//#################################
var  changeSchoolStatusField = [
	{columnWidth:1,layout:'form',labelWidth: 90,items:[
	         {xtype:'hidden',name: 'id',anchor:'98%'},
	         {xtype:'textfield',fieldLabel: '机构代码',name: 'schoolNum',readOnly: true,anchor:'98%',allowBlank: false},
	         {xtype:'textfield',fieldLabel: '机构名称',name: 'schoolName',readOnly: true,anchor:'98%',allowBlank: false},
	         schoolStatusCombo
	 ]}
];
var changeSchoolStatusForm = new Ext.form.FormPanel({
	 id:"changeSchoolStatusForm",
	    labelWidth: 60,
	    labelAlign : 'left',
	    layout : 'column',
	    frame:true,
	    border : false,
		bodyStyle:'padding:5',
	    items : [changeSchoolStatusField]
});

var changeSchoolStatusWindow = new Ext.Window({
  title: '修改机构状态',
  width: 340,
  height:250,
  closeAction : 'hide',
  layout: 'fit',
  border:false,
  modal: true,
  shadow: true,
  hideMode: Ext.isIE ? 'offsets' : 'display',
  plain:true,
  bodyStyle:'padding:5px;',
  buttonAlign:'center',
  items: [changeSchoolStatusForm],
  listeners: {
	  beforeshow:function(data){
  		var selectData=cityStateGrid.getSelectionModel().getSelected().data;
  		schoolStatusCombo.setValue(selectData.schoolStatusId);
  		changeSchoolStatusForm.getForm().findField('id').setValue(selectData.id);
  		changeSchoolStatusForm.getForm().findField('schoolNum').setValue(selectData.schoolNum);
  		changeSchoolStatusForm.getForm().findField('schoolName').setValue(selectData.schoolName);
  	  }
    },
    buttons:[{  
        text:"保存",  
        handler:function(){
        	var param={id:changeSchoolStatusForm.getForm().findField('id').getValue(),
        			newStatusID:schoolStatusCombo.getValue()
        			};
        	InstitutionstatusController.updateSchoolStatus(param,function(data){
        		var jsonData = Ext.util.JSON.decode(data);
    			if(jsonData.success == true) {
    				Ext.MessageBox.alert('提示',jsonData.errors.info);
    				InstitutionController.getSchoolAndCityStatusInfo(function(list){
    					cityStateStore.loadData(list);
    				});
    			} else {
    				Ext.MessageBox.alert('提示',jsonData.errors.info);
    			}
    			changeSchoolStatusWindow.hide();
        	});
        }  
    },{  
        text:"取消",  
        handler:function(){  
        	changeSchoolStatusWindow.hide();
        }  
    }]  
});      
//#################################
var cityStateRecord = Ext.data.Record.create([{
		name :'id'
	}, {
		name :'cityNameAndStatue'
	}, {
		name :'schoolNum'
	}, {
		name :'schoolName'
	}, {
		name :'schoolStatus'
	},{
		name :'schoolStatusId'
	}
]);

var cityStateStore = new Ext.data.GroupingStore({
	id : 'cityStateStore',
	reader : new Ext.data.JsonReader({
		fields : cityStateRecord
	}),
	groupField : 'cityNameAndStatue',
	sortInfo : {
		field : 'schoolNum',
		drirection : 'ASC'
	},
	groupOnSort : false
});

var cityStateGrid = new Ext.grid.GridPanel({
	id : 'SectionManage',
	store : cityStateStore,
	region: 'center',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'schoolName',
	view : new Ext.grid.GroupingView({
		sortAscText : '升序',
		sortDescText : '降序',
		groupByText : '使用当前字段进行分组',
		showGroupsText : '分组显示',
		showGroupName : true,
		hideGroupedColumn:true
	}),
	columns : [{
		id : 'schoolNum',
		header : '机构代码',
	  	dataIndex :'schoolNum',
	  	width :70,
	 	sortable :true
	},
	{
		id : 'schoolName',
		header : '机构名称',
	  	dataIndex :'schoolName',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'schoolStatus',
		header : '机构状态',
	  	dataIndex :'schoolStatus',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'cityNameAndStatue',
		header : '所属考试院信息',
	  	dataIndex :'cityNameAndStatue',
	  	width :100,
	 	sortable :true
	}
	],
     tbar : [{
         text : '修改机构状态',
         tooltip : '修改机构状态',
         iconCls : 'edit',
         onClick : function() {
        	 if(cityStateGrid.getSelectionModel().getCount()!=1){
             	Ext.MessageBox.alert('提示',"请选择一所高校！");
             }else{
            	 changeSchoolStatusWindow.show();
             }
         }
    } ] ,
     bbar : [],
        listeners:{
    	rowdblclick : function(grid){
    		changeSchoolStatusWindow.show();
    	}
    }
 });

function pageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [cityStateGrid],
		renderTo :Ext.getBody()
	});
	InstitutionController.getSchoolAndCityStatusInfo(function(list){
		cityStateStore.loadData(list);
	});
	schoolStatusStore.load();
}