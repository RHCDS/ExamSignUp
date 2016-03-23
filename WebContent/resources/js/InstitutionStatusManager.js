var institutionStatusRecord = Ext.data.Record.create([{
		name :'id'
	}, {
		name :'remarks'
	},  {
		name :'startDateValue'
	},{
		name :'startTimeValue'
	},{
		name :'endDateValue'
	},{
		name :'endTimeValue'
	}
]);

var institutionStatusStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root',
         idProperty:'id'
      }, institutionStatusRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : InstitutionstatusController.loadinstitutionstatus
	  })
});

var institutionStatusGrid = new Ext.grid.GridPanel({
	id : 'institutionStatusGrid',
	store :  institutionStatusStore,
	title : '数据上报时段管理',
	region: 'center',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'remarks',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [{
		id : 'remarks',
		header : '时段名称',
	  	dataIndex :'remarks',
	  	width :200,
	 	sortable :true
	},
	{
		id : 'startDateValue',
		header : '开始日期',
	  	dataIndex :'startDateValue',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'startTimeValue',
		header : '开始时间',
	  	dataIndex :'startTimeValue',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'endDateValue',
		header : '结束日期',
	  	dataIndex :'endDateValue',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'endTimeValue',
		header : '结束时间',
	  	dataIndex :'endTimeValue',
	  	width :150,
	 	sortable :true
	}
	],
     tbar : [ {
                   text : '修改',
                   tooltip : '修改场次',
                   iconCls : 'edit',
                   onClick : function() {
                	  institutionStatusGrid.fireEvent('rowdblclick', institutionStatusGrid);
                   }
              }
              ] ,
        listeners:{
    	rowdblclick : function(grid){
    		if(grid.getSelectionModel().hasSelection()){
    			institutionStatusWindowInit("编辑时间段").show();
    			institutionStatusForm.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}
    	}
    }
 });
var  institutionStatusFields = [
                      {columnWidth:1,layout:'form',labelWidth: 90,items:[
                  		{xtype:'hidden',name: 'id',anchor:'98%'},
                  		{xtype:'textfield',fieldLabel: '时段名称',name: 'remarks',readOnly: true,anchor:'98%',allowBlank: false}
                  	]},
                  	{columnWidth:1,layout:'form',labelWidth: 90,items:[
                  	       {
                  	    	   xtype:'datefield',
                  	    	   fieldLabel:'时段开始日期',
                  	    	   name:'startDateValue',
                  	    	   anchor:'98%',
                  	    	   width:60,
                  	    	   format:'Y-m-d',
                  	    	   cls:"Wdate",
                  	    	   allowBlank:false
                  	       },
                  	      {
                  	    	  xtype:'timefield',
                  	    	  fieldLabel:'时段开始时间',
                  	    	  name:'startTimeValue',
                  	    	  format:'H:i',
                  	    	  anchor:'98%',
                  	    	  cls:"Wdate",
                  	    	  allowBlank:false
                  	      },
                  	     {
                	    	   xtype:'datefield',
                	    	   fieldLabel:'时段结束日期',
                	    	   name:'endDateValue',
                	    	   anchor:'98%',
                	    	   width:60,
                	    	   format:'Y-m-d',
                	    	   cls:"Wdate",
                	    	   allowBlank:false
                	       },
                	      {
                	    	  xtype:'timefield',
                	    	  fieldLabel:'时段结束时间',
                	    	  name:'endTimeValue',
                	    	  format:'H:i',
                	    	  anchor:'98%',
                	    	  cls:"Wdate",
                	    	  allowBlank:false
                	      }
                  	]}
];

 var institutionStatusForm = new Ext.form.FormPanel({
	 id:"institutionStatusForm",
    labelWidth: 60,
    labelAlign : 'left',
    layout : 'column',
    frame:true,
    border : false,
	bodyStyle:'padding:5',
    items : [institutionStatusFields]
});

var institutionStatusWindow ;

function institutionStatusWindowInit(title){
    if (!institutionStatusWindow) {
   	    	institutionStatusWindow = new Ext.Window({
   	        title: '时段设置',
   	        width: 400,
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
   	        items: [institutionStatusForm],
   	        buttons: [{
                   text : '保存',
                   handler : function() {
                   	if(institutionStatusForm.getForm().isValid()){
                    	var data = institutionStatusForm.getForm().getValues();
                    	InstitutionstatusController.updateInstitutionTime(data,function(data){
                    		var jsonData=Ext.util.JSON.decode(data);
                    		if(jsonData.success==true){
                	    		Ext.MessageBox.alert('提示',jsonData.errors.info);
                	    		institutionStatusStore.load();
                	    		institutionStatusWindow.hide();
                	    	}else{
                	    		Ext.MessageBox.alert('提示',jsonData.errors.info);
                	    	}
                	    });            	
                   	}else{
                   		Ext.MessageBox.alert('提示',"输入信息有误！");
                   	}
                   }
                          		
            }, {
                   text : '取消',
                   handler : function() {
                	   institutionStatusForm.getForm().reset();
                	   institutionStatusWindow.hide();
                   }
            }]
  	    });
   	
    }
    institutionStatusWindow.setTitle(title);
    return institutionStatusWindow;
	
}
function pageInit() {
	institutionStatusStore.load();
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [institutionStatusGrid],
		renderTo :Ext.getBody()
	});
}
