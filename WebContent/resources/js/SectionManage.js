var theoryOrOperate="theory";
var sectionRecord = Ext.data.Record.create([{
		name :'id'
	}, {
		name :'sectionnum'
	}, {
		name :'theoryflag',convert:function(data){if(data==1){return "是";}else{return "否";}}
	}, {
		name :'operateflag',convert:function(data){if(data==1){return "是";}else{return "否";}}
	}, {
		name :'dateValue'
	},{
		name :'timeValue'
	}
]);

var sectionStore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root',
         idProperty:'sectionnum'
      }, sectionRecord),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : SectionController.loadPageDate
	  })
});

var sectionGrid = new Ext.grid.GridPanel({
	id : 'SectionManage',
	store : sectionStore,
	title : '场次安排',
	region: 'center',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
//	autoExpandColumn : 'dateValue',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [{
		id : 'sectionnum',
		header : '场次序号',
	  	dataIndex :'sectionnum',
	  	width :80,
	 	sortable :true
	},
	{
		id : 'dateValue',
		header : '开始日期',
	  	dataIndex :'dateValue',
	  	width :200,
	 	sortable :true
	},
	{
		id : 'timeValue',
		header : '开始时间',
	  	dataIndex :'timeValue',
	  	width :200,
	 	sortable :true
	},
	{
		id : 'theoryflag',
		header : '是否理论考试',
	  	dataIndex :'theoryflag',
	  	width :100,
	 	sortable :true
	},
	{
		id : 'operateflag',
		header : '是否上机考试',
	  	dataIndex :'operateflag',
	  	width :100,
	 	sortable :true
	}
	],
     tbar : [{
                   text : '新增',
                   tooltip : '新增场次',
                   iconCls : 'add',
                   onClick : function() {
					   var newSection = {
					   		id : '',
					   		sectionnum : '',
					   		theoryflag : '',
					   		operateflag : '',
					   		starttime : '',
					   		exArrangements : '',
					   		exInstitution:''
					   };
                       sectionForm.getForm().reset();
                       sectionWindowInit('新增场次').show();
                       choose();
                       sectionForm.getForm().loadRecord(newSection);
                      
                       
                  }
              }, '-', {
                   text : '修改',
                   tooltip : '修改场次',
                   iconCls : 'edit',
                   onClick : function() {
                   	sectionGrid.fireEvent('rowdblclick', sectionGrid);
                   }
              }, '-', {
                   text : '删除',
                   tooltip : '删除选中的菜单项',
                   iconCls : 'remove',
                   onClick : function() {
                      if (sectionGrid.getSelectionModel().hasSelection()) {
                         Ext.MessageBox.confirm('提示', '你确定要删除选中的场次么?',
                                 function(button) {
                                    if (button == 'yes') {
                                       var list = sectionGrid.getSelectionModel().getSelections();
                                       var rList = [];
                                       for (var i = 0; i <list.length; i++) {
                                          rList[i] = list[i].data["id"];
                                       }
                                       SectionController.deleteSections(
                                               rList, function(data) {
                                                  if (data) {
                                                     Ext.MessageBox.alert('提示','删除场次成功!');
                                                     var temp=Ext.getCmp("SectionPagingToolbar");
                                                     temp.doLoad(temp.cursor);
                                                  } else {
                                                     Ext.MessageBox.alert('删除场次失败!',
                                                    		 			"请务必先删除已安排该场次的上机和理论的考场编排!");
                                                  }
                                               });
                                 		}
                                 });
                      } else {
                         Ext.MessageBox.alert('提示', "请至少选择一条记录!");
                      }
                   }
              }, '-',
            	  new Ext.form.ComboBox({
            	  text:'理论上机场次选择',
                  fieldLabel: '理论上机场次选择',
                  id:'theoryOrOperateComboBox',
                  forceSelection: true,
                  editable:false,
                  listWidth: 200,
                  store: new Ext.data.SimpleStore({
                      fields: ['name', 'value'],
                      data : [['理论场次','theory'],['上机场次','operate']]
                      }),
                  valueField:'value',
                  displayField:'name',
                  typeAhead: true,
                  mode: 'local',
                  triggerAction: 'all',
                  selectOnFocus:true,
                  allowBlank:false,
                  listeners: {
                	  afterRender:function(data){
                		  theoryOrOperate="theory";
                		  Ext.getCmp("theoryOrOperateComboBox").setValue("theory");
                		  Ext.getCmp("SectionPagingToolbar").moveFirst();
                	  },
            	  	  select:function(){
            	  		  
            	  		if(theoryOrOperate==this.getValue)
            	  			return;
            	  		theoryOrOperate=this.getValue();
            	  		Ext.getCmp("SectionPagingToolbar").moveFirst();
            	  	  }
                  }
                  })
              ] ,
      	    bbar : new Ext.PagingToolbar({
      	    	id:"SectionPagingToolbar",
      	    	pageSize:20,
      	        store : sectionStore,
      	        displayInfo : true,
      	        firstText:'首页',
      	        lastText:'尾页',
      	        prevText:'上一页',
      	        nextText:'下一页',
      	        refreshText:'刷新',
      	        displayMsg : '显示第 {0} 条到 {1} 条记录，一共 {2} 条',
      	        emptyMsg : "没有记录",
      	        doLoad:function(start) {
      		        var params = {};
      		        params.start = start;
      		        params.limit = this.pageSize;
      		        params.type =theoryOrOperate;
      		        if (this.fireEvent("beforechange", this, params) !== false) {
      		            this.store.load({params:params});
      		        }
      		    }
      	  }),
        listeners:{
    	rowdblclick : function(grid){
    		if(grid.getSelectionModel().hasSelection()){
    			sectionWindowInit("编辑场次信息").show();
    			sectionForm.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
	
 });


var data = [
            ['1', '1'],
           ['2', '2'],
           ['3', '3'],
           ['4', '4'],
           ['5', '5'],
           ['6', '6'],
           ['7', '7'],
           ['8', '8'],
           ['9', '9'],
           ['10', '10'],
           ['11', '11'],
           ['12', '12'],
           ['13', '13'],
           ['14', '14'],
           ['15', '15'],
           ['16', '16'],
           ['17', '17'],
           ['18', '18'],
           ['19', '19'],
           ['20', '20'],
           ['21', '21'],
           ['22', '22'],
           ['23', '23'],
           ['24', '24'],
           ['25', '25'],
           ['26', '26'],
           ['27', '27'],
           ['28', '28'],
           ['29', '29'],
           ['30', '30'],
           ['31', '31'],
           ['32', '32'],
           ['33', '33'],
           ['34', '34'],
           ['35', '35'],
           ['36', '36'],
           ['37', '37'],
           ['38', '38'],
           ['39', '39'],
           ['40', '40'],
           ['41', '41'],
           ['42', '42'],
           ['43', '43'],
           ['44', '44'],
           ['45', '45'],
           ['46', '46'],
           ['47', '47'],
           ['48', '48'],
           ['49', '49'],
           ['50', '50']
];

var store=new Ext.data.SimpleStore({    //创建一个下拉框需要的数据源
    fields: ['name', 'value'],           //一个是选择项的值，一个是显示的值
    data: [
           ['1', '1'],
           ['2', '2'],
           ['3', '3'],
           ['4', '4'],
           ['5', '5'],
           ['6', '6'],
           ['7', '7'],
           ['8', '8'],
           ['9', '9'],
           ['10', '10'],
           ['11', '11'],
           ['12', '12'],
           ['13', '13'],
           ['14', '14'],
           ['15', '15'],
           ['16', '16'],
           ['17', '17'],
           ['18', '18'],
           ['19', '19'],
           ['20', '20'],
           ['21', '21'],
           ['22', '22'],
           ['23', '23'],
           ['24', '24'],
           ['25', '25'],
           ['26', '26'],
           ['27', '27'],
           ['28', '28'],
           ['29', '29'],
           ['30', '30'],
           ['31', '31'],
           ['32', '32'],
           ['33', '33'],
           ['34', '34'],
           ['35', '35'],
           ['36', '36'],
           ['37', '37'],
           ['38', '38'],
           ['39', '39'],
           ['40', '40'],
           ['41', '41'],
           ['42', '42'],
           ['43', '43'],
           ['44', '44'],
           ['45', '45'],
           ['46', '46'],
           ['47', '47'],
           ['48', '48'],
           ['49', '49'],
           ['50', '50']
          
           ]
});









function choose(){

	 store.loadData(data);	
	 var total = sectionStore.getTotalCount();
	 sectionStore.sort('sectionnum', 'ASC');

	 if(theoryOrOperate == 'theory')
	 {
		 while(total != 0)
		 {
			 var theory = sectionStore.getAt(total-1).get('theoryflag') ;
			 if(theory =='是')
			 {
				 var index =  sectionStore.getAt(total-1).get('sectionnum') ;
				 store.removeAt(index-1);
			 }
			 total--;

		 }
		 //	Ext.MessageBox.alert('提示','nihoa1');
	 }

	 if(theoryOrOperate == 'operate')
	 {
		 while(total != 0)
		 {
			 var operate = sectionStore.getAt(total-1).get('operateflag') ;
			 if(operate =='是')
			 {
				 var index =  sectionStore.getAt(total-1).get('sectionnum');
				 store.removeAt(index-1);
			 }
			 total--;
		 }
		 //Ext.MessageBox.alert('提示','nihoa2');
	 }
	
}




var  sectionFields = [

                    
                      {
                    	columnWidth:1,layout:'form',labelWidth: 90,items:[
                  		{xtype:'hidden',name: 'id',anchor:'98%'},
                  		{xtype:'hidden',name: 'theoryflag',anchor:'98%'},
                  		{xtype:'hidden',name: 'operateflag',anchor:'98%'},
                  	//	{xtype:'textfield',fieldLabel: '场次序号',name: 'sectionnum',anchor:'99%',allowBlank: false, regex:/^[0-9]*$/,regexText:'只能输入数字'}
                  		{xtype:'combo',store:store,fieldLabel: '场次序号',
                  		    valueField:'value',
                            displayField:'name',
                            typeAhead: true,
                            name: 'sectionnum',
                            mode: 'local',
                            triggerAction: 'all',
                            selectOnFocus:true,
                            anchor:'98%',
                            allowBlank:false }                 	                 		
                  		]},
                  	{columnWidth:1,layout:'form',labelWidth: 90,items:[
                  	       {
                  	    	   xtype:'datefield',
                  	    	   fieldLabel:'场次开始日期',
                  	    	   name:'dateValue',
                  	    	   anchor:'98%',
                  	    	   width:60,
                  	    	   format:'Y-m-d',
                  	    	   cls:"Wdate",
                  	    	   allowBlank:false
                  	       },
                  	      {
                  	    	 xtype:'timefield',
                  	    	  fieldLabel:'场次开始时间',
                  	    	  name:'timeValue',
                  	    	  format:'H:i',
                  	    	  anchor:'98%',
                  	    	  cls:"Wdate",
                  	    	  allowBlank:false
                  	      }
                  	]}
];

 var sectionForm = new Ext.form.FormPanel({
	 id:"sectionId",
    labelWidth: 60,
    labelAlign : 'left',
    layout : 'column',
    frame:true,
    border : false,
	bodyStyle:'padding:5',
    items : [sectionFields]
});

var sectionWindow ;

function sectionWindowInit(title){
    if (!sectionWindow) {
   	    var sectionWindow = new Ext.Window({
   	        title: '场次设置',
   	        width: 350,
   	        height:200,
   	        closeAction : 'hide',
   	        layout: 'fit',
			border:false,
			modal: true,
			shadow: true,
			hideMode: Ext.isIE ? 'offsets' : 'display',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: [sectionForm],
   	        buttons: [{
                   text : '保存',
                   handler : function() {
                   	if(sectionForm.getForm().isValid()){
                    	var section = sectionForm.getForm().getValues();
                    	if(theoryOrOperate=="theory"){
                    		section.theoryflag="1";
                    		section.operateflag="0";
                   		}else{
                   			section.theoryflag="0";
                   			section.operateflag="1";
                   		}
                    	SectionController.saveSection(section,function(data){
                    		var jsonData=Ext.util.JSON.decode(data);
                    		if(jsonData.success==true){
                	    		Ext.MessageBox.alert('提示',jsonData.errors.info);
                	    		sectionWindow.hide();
                	    		var temp=Ext.getCmp("SectionPagingToolbar");
                                temp.doLoad(temp.cursor);
                	    	}else{
                	    		Ext.MessageBox.alert('提示',jsonData.errors.info);
                	    	}
                	    });            	
                   	}else{
                   		Ext.MessageBox.alert('提示',"输入信息有误！");
                   	}
                   }
                          		
            }, {
                   text : '清空',
                   handler : function() {
                	   var newSection = {
					   		id : '',
					   		sectionnum : '',
					   		theoryflag : '',
					   		operateflag : '',
					   		starttime : '',
					   		exArrangements : '',
					   		exInstitution:''
					   };
					   if(theoryOrOperate=="theory"){
						    newSection.theoryflag="1";
                  			newSection.operateflag="0";
                  		}else{
                  			newSection.theoryflag="0";
                  			newSection.operateflag="1";
                  		}
					   sectionForm.getForm().reset();
                       sectionForm.getForm().loadRecord(newSection);
                   }
            }, {
                   text : '关闭',
                   handler : function() {
                	   sectionForm.getForm().reset();
                	   sectionWindow.hide();
                   }
            }]
  	    });
   	
    }
    sectionWindow.setTitle(title);
    return sectionWindow;
	
}



function pageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [sectionGrid],
		renderTo :Ext.getBody()
	});

	
}
