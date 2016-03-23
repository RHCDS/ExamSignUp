var newsListRecord = new Ext.data.Record.create([
    {name: 'id'},
    {name: 'title'},
    {name: 'publisher'},
    {name: 'begindate'},
    {name: 'enddate'}
]);
var newsListStore = new Ext.data.Store({
	reader : new Ext.data.JsonReader({
		totalProperty : 'totalProperty',
		root : 'root',
		idProperty : 'id'
	}, newsListRecord),
	proxy: new Ext.ux.data.DWRProxy({
		dwrfunction: NewsManageController.loadNewsByInstitution
	})
});
var newsGrid = new Ext.grid.GridPanel({
	id : 'NewsManage',
	store : newsListStore,
	title : '新闻管理',
	region: 'center',
    loadMask :true,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'title',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [
	    new Ext.grid.RowNumberer({width:30}),
	 {
		id : 'title',
		header : '新闻标题',
	  	dataIndex :'title',
	  	width :200,
	 	sortable :true
	},
	{
		id : 'publisher',
		header : '发布人',
	  	dataIndex :'publisher',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'begindate',
		header : '开始时间',
	  	dataIndex :'begindate',
	  	width :150,
	 	sortable :true
	},
	{
		id : 'enddate',
		header : '结束时间',
	  	dataIndex :'enddate',
	  	width :100,
	 	sortable :true
	}
	],
     tbar : [{
                   text : '新增',
                   tooltip : '新增新闻',
                   iconCls : 'add',
                   onClick : function() {
					   var newNews = {
					   		id : '',
					   		title : '',
					   		publisher: '',
					   		begindate : '',
					   		enddate : ''
					   };
                       newsForm.getForm().reset();
                       newsWindowInit('新增新闻').show();
                       dataIndex.getForm().loadRecord(newNews);
                   }
              }, '-', {
                   text : '修改',
                   tooltip : '修改新闻',
                   iconCls : 'edit',
                   onClick : function() {
                   	newsGrid.fireEvent('rowdblclick', newsGrid);
                   }
              }, '-', {
                   text : '删除',
                   tooltip : '删除选中的新闻',
                   iconCls : 'remove',
                   onClick : function() {
                      if (newsGrid.getSelectionModel().hasSelection()) {
                         Ext.MessageBox.confirm('提示', '你确定要删除选中的菜单项么?',
                             function(button) {
                                if (button == 'yes') {
                                   var list = newsGrid.getSelectionModel().getSelections();
                                   var rList = [];
                                   for (var i = 0; i <list.length; i++) {
                                      rList[i] = list[i].data["id"];
                                   }
                                   NewsManageController.deleteNews(
                                      rList, function(data) {
                                          if (data) {
                                              Ext.MessageBox.alert('提示','删除菜单项成功!');
                                              newsListStore.reload();
                                                    // var temp=Ext.getCmp("SectionPagingToolbar");
                                        //             temp.doLoad(temp.cursor);
                                          } else {
                                             Ext.MessageBox.alert('提示',"删除菜单项失败!");
                                          }
                                      });
                                  }
                          });
                      } else {
                         Ext.MessageBox.alert('提示', "请至少选择一条记录!");
                      }
                   }
              }
        ],
        listeners:{
    	rowdblclick : function(grid){
    		if(grid.getSelectionModel().hasSelection()){
    			newsWindowInit("修改新闻信息").show();
    			newsForm.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
 });
var  newsFields = [
    {columnWidth:1,layout:'form',labelWidth: 90,items:[
        {xtype:'hidden',name: 'id',anchor:'98%'},
        {xtype:'textfield',fieldLabel: '新闻标题',name: 'title',anchor:'98%'},
        {xtype:'textfield',fieldLabel: '发布人',name: 'publisher',anchor:'98%'},
    ]},
    {columnWidth:1,layout:'form',labelWidth: 90,items:[
        {
            xtype:'datefield',
            fieldLabel:'开始时间',
            name:'begindate',
            anchor:'98%',
            width:60,
            format:'Y-m-d',
            cls:"Wdate",
            minValue: new Date(),
            allowBlank:false
        },
        {
            xtype:'datefield',
            fieldLabel:'结束时间',
            name:'enddate',
            width:60,
            format:'Y-m-d',
            anchor:'98%',
            cls:"Wdate",
            allowBlank:false,
            minValue: 'begindate'
        }
    ]}
];
var newsForm = new Ext.form.FormPanel({
   id:"id",
   labelWidth: 60,
   labelAlign : 'left',
   layout : 'column',
   frame:true,
   border : false,
   bodyStyle:'padding:5',
   items : [newsFields]
});

var newsWindow ;

function newsWindowInit(title){
    if (!newsWindow) {
   	    var newsWindow = new Ext.Window({
   	        title: '新闻设置',
   	        width: 350,
   	        height:230,
   	        closeAction : 'hide',
   	        layout: 'fit',
			border:false,
			modal: true,
			shadow: true,
			hideMode: Ext.isIE ? 'offsets' : 'display',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        buttonAlign:'center',
   	        items: [newsForm],
   	        buttons: [{
                   text : '保存',
                   handler : function() {
                   	if(newsForm.getForm().isValid()){
                    	var news = newsForm.getForm().getValues();
                    	NewsManageController.saveNews(news,function(data){
                    		var jsonData=Ext.util.JSON.decode(data);
                    		if(jsonData.success==true){
                	    		Ext.MessageBox.alert('提示',jsonData.errors.info);
                	    		newsWindow.hide();
                	    	//	var temp=Ext.getCmp("SectionPagingToolbar");
                            //    temp.doLoad(temp.cursor);
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
                	   var newNews = {
					   		id : '',
					   		title : '',
					   		publisher : '',
					   		begindate : '',
					   		enddate : '',
	
					   };
					   newsForm.getForm().reset();
                       newsForm.getForm().loadRecord(newNews);
                   }
            }, {
                   text : '关闭',
                   handler : function() {
                	  newsForm.getForm().reset();
                	   newsWindow.hide();
                   }
            }]
  	    });
   	
    }
    newsWindow.setTitle(title);
    return newsWindow;
}
function pageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [newsGrid],
		renderTo :Ext.getBody()
	});
}