var campusstore = new Ext.data.ArrayStore({
	fields:[
	        'id',
	        'name'
	        ]
});

var examroom = Ext.data.Record.create([{
		name :'id'
	}, {
		name :'exInstitution'
	},  {
		name :'exCampus'
	}, {
		name :'roomlocation'
	}, {
		name :'capacity'
	}, {
		name :'theoryflag',convert:function(data){if(data==1){return "是";}else{return "否";}}
	},{
		name :'operateflag',convert:function(data){if(data==1){return "是";}else{return "否";}}
	}
]);

var examroomstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, examroom),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : PhysiceexamroomController.paginationShow
	  })
});

var examroomgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'examroomgrid',
	store : examroomstore,
	title : '教室表',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
//	autoExpandColumn : 'roomlocation',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width:30}),
	{
		id : 'roomlocation',
		header : '教室位置',
	  	dataIndex :'roomlocation',
	  	width :300,
	 	sortable :true
	},	{
		
		header : '校区',
	  	dataIndex :'exCampus',
	  	width :200,
	 	sortable :true
	},{
		id : 'capacity',
		header : '教室容量（人）',
	  	dataIndex :'capacity',
	  	width :120,
	 	sortable :true
	},	{
		id : 'theoryflag',
		header : '理论教室',
	  	dataIndex :'theoryflag',
	  	width :150,
	 	sortable :true
	},	{
		id : 'operateflag',
		header : '上机教室',
	  	dataIndex :'operateflag',
	  	width :150,
	 	sortable :true
	}
	],
     tbar : [{
               text : '批量导入教室',
               tooltip : 'examroomimprot',
               iconCls : 'import',
               onClick : function() {
            	   examroomimprot().show();
               }
          },'-',{
                   text : '新增',
                   tooltip : '新增教室',
                   iconCls : 'add',
                   onClick : function() {
					   var newexamroom = new examroom({
					   		id : '',
					   		exInstitution : '',
					   		exCampus : '',
					   		roomlocation : '',
					   		capacity : '',
					   		theoryflag : '',
					   		operateflag : ''
					   });
			           examroomform.getForm().reset();
			           examrommWindowInit('新增教室').show();
			           examroomform.getForm().loadRecord(newexamroom);                 
                   }
              }, '-', {
                   text : '修改',
                   tooltip : '修改教室',
                   iconCls : 'edit',
                   onClick : function() {
                   	examroomgrid.fireEvent('rowdblclick', examroomgrid);
                   }
              }, '-', {
                   text : '删除',
                   tooltip : '删除选中的菜单项',
                   iconCls : 'remove',
                   onClick : function() {
                      if (examroomgrid.getSelectionModel().hasSelection()) {
                         Ext.MessageBox.confirm('提示', '你确定要删除选中的教室么?',
                                 function(button) {
                                    if (button == 'yes') {
                                       var list = examroomgrid.getSelectionModel().getSelections();
                                       var rList = [];
                                       for (var i = 0; i <list.length; i++) {
                                          rList[i] = list[i].data["id"];
                                       }
                                       
                                       PhysiceexamroomController.deletePhysicexamroom(
                                               rList, function(data) {
                                                  if (data) {
                                                     Ext.MessageBox.alert('提示','删除教室成功!');
                                                     examroomGridInit();
                                                  } else {
                                                     Ext.MessageBox.alert('删除考场失败!',
                                                    		 "教室已被安排，请务必先删除已安排该教室的上机和理论的考场编排!");
                                                  }
                                               });
                                 		}
                                 });
                      } else {
                         Ext.MessageBox.alert('提示', "请至少选择一条记录!");
                      }
                   }
              }] ,
      	    bbar : new Ext.PagingToolbar({
      	    	pageSize:20,
      	        store : examroomstore,
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
      		        if (this.fireEvent("beforechange", this, params) !== false) {
      		            this.store.load({params:params})
      		        }
      		    }
      	  }),
        listeners:{
    	rowdblclick : function(grid){
    		if(grid.getSelectionModel().hasSelection()){
    			examrommWindowInit("编辑教室信息").show();
    			examroomform.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
 });
 


var  examroomfields = [
                      {columnWidth:1,layout:'form',items:[
                  		{xtype:'hidden',name: 'id',anchor:'98%'},
                  		{xtype:'textfield',fieldLabel: '教室位置',name: 'roomlocation',anchor:'99%',allowBlank: false}
                  	]},
                  	{columnWidth:.5,layout:'form',items:[
                  	    {xtype:'combo',store: campusstore,mode: 'local',valueField:'id',displayField:'name',triggerAction:'all',fieldLabel: '隶属校区',editable:false,name: 'exCampus',anchor:'98%',allowBlank: false}
                  	]},
                  	{columnWidth:.5,layout:'form',labelWidth:75,items:[
                  		{xtype:'numberfield',fieldLabel: '容量（人）',name: 'capacity',anchor:'98%',allowBlank: false}
                  	]},
                  	{columnWidth:.5,layout:'form',items:[
                  	    {xtype:'combo',fieldLabel: '考试类型',name: 'theoryoroperate',store : ['理论','上机'],editable : false,anchor:'98%',triggerAction : 'all',allowBlank: false}
                  	]}
];

 var examroomform = new Ext.form.FormPanel({
    labelWidth: 60,
    labelAlign : 'left',
    layout : 'column',
    frame:true,
    border : false,
    bodyStyle:'padding:5',
    items : [examroomfields]
});

var examrommwin ;

function examrommWindowInit(title){
  	CampusController.getCampusList(function(data){
		if(data){
			campusstore.loadData(data);
		}
  	});
    if (!examrommwin) {
   	    var examrommwin = new Ext.Window({
   	        title: '教室设置',
   	        width: 480,
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
   	        items: [examroomform],
   	        buttons: [{
                   text : '保存',
                   handler : function() {
                   	if(examroomform.getForm().isValid()){
                    	var examroom = examroomform.getForm().getValues();
                    	PhysiceexamroomController.savePhysicexamroom(examroom,function(data){
            					var jsonData=Ext.util.JSON.decode(data);
            					if(jsonData.success == true) {
            						Ext.MessageBox.alert('提示',"保存成功！");
                    	    		examrommwin.hide();
            					}
            					else if(jsonData.success == false) {
            						Ext.MessageBox.alert('提示',"保存失败！");
                        	    	examroomGridInit();
            					}
            					examroomstore.reload();
                	    });            	
                   	
                   	}else{
                   		Ext.MessageBox.alert('提示',"输入信息有误！");
                   	}
                   }
                          		
            }, {
                   text : '清空',
                   handler : function() {
                	   examroomform.getForm().reset();
                   }
            }, {
                   text : '关闭',
                   handler : function() {
                	   examrommwin.hide();
                   }
            }]
  	    });
   	
    }
    examrommwin.setTitle(title);
    return examrommwin;
	
}

var examroomimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../examroomXlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'examroomfile',
		inputType : 'file'//文件类型 
	} ]
});



var examroomimportwin;

function examroomimprot(){
	if(!examroomimportwin){
		examroomimportwin = new Ext.Window({
   	        title: '文件上传',
   	        width: 480,
   	        height:360,
   	        minWidth: 400,
   	        minHeight: 360,
   	        closeAction : 'hide',
   	        layout: 'border',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: examroomimportform,
   	     buttons : [ {
   			text : '上传',
   			handler : function() {
   				Ext.MessageBox.wait('正在上传，请等待...','提示');
   				examroomimportform.getForm().submit({
   					success : function(form, action) {
   						var examrooms = Ext.util.JSON.decode(action.response.responseText).excelArray;
   						PhysiceexamroomController.importExamrooms(examrooms,function(data){
   							if(data){
   								Ext.MessageBox.alert('提示', '文件上传成功！',function(){
   									examroomimportwin.hide();
   									examroomGridInit();
   								});
   								
   								 
   							}	
   						});
   						
   					},
   					failure : function() {
   						Ext.MessageBox.alert('错误', '文件上传失败，请检查数据格式',function(){
   							examroomimportwin.hide();
   						});
   						
   					}
   				});
   			}
   		} ]
		});
	}
	return examroomimportwin;
}

function examroomGridInit(){
	examroomstore.load({
		params : {
			start : 0,
			limit : 20
		}
	});
}

function ExamroomPageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [examroomgrid],
		renderTo :Ext.getBody()
	});
	examroomGridInit();
}
