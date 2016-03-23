var selectedModelNum = 9999;

var  modelSelectRecord = Ext.data.Record.create([{
	name :'modelNum'
}, {
	name :'modelName'
}
]);
//语种Store
var  modelSelectStore = new Ext.data.Store( {
reader : new Ext.data.JsonReader({
     totalProperty : 'totalProperty',
     root : 'root',
     idProperty:'modelNum'
  }, modelSelectRecord),
proxy : new Ext.ux.data.DWRProxy({
     dwrFunction : UploadPicturesController.loadSelectedModel
  })
});
var modelSelectCombo = new Ext.form.ComboBox({
	store: modelSelectStore,
	mode: 'local',
	triggerAction: 'all',
	emptyText:'--请选择模式--',
	editable:false,
	valueField:'modelNum',
	displayField:'modelName',
	listEmptyText:'没有待选模式！',
	
	listeners : {
		afterRender : function(data) {
			selectedModelNum=9999;
			modelSelectStore.load({callback:function(){
				modelSelectCombo.setValue(modelSelectStore.getAt(0).get("modelNum"));
				modelSelectCombo.fireEvent("select");
			}});
		},
		select:function(record,index){
		     selectedModelNum=this.getValue();
		     var params = {};
		     params.start = 0;
		     params.limit = 20;
		     params.model=selectedModelNum;
		     if (studentgrid.fireEvent("beforechange", this, params) !== false) {
		    	 studentgrid.store.load({params:params});
		     }
		}
	}
});
var pictureImportForm = new Ext.form.FormPanel({
		labelAlign : 'left',
		labelWidth : 60,
		region : 'center',
		buttonAlign : 'center',
		frame : true,
		url : '../uploadFile.do',//fileUploadServlet  
		width : 300,
		height : 200,
		fileUpload : true,
		items : [ {
			xtype : 'textfield',
			fieldLabel : '文件名',
			name : 'file',
			inputType : 'file'//文件类型 
		}, {  
            xtype:'textfield',  
            allowBlank:false,  
            inputType:'text',  
            name:'subdir',
            value:"tmp",
            hidden : true
        } ]
	});
pictureImportWin = new Ext.Window({
	        title: '文件上传',
	        width: 480,
	        height:360,
	        minWidth: 400,
	        minHeight: 360,
	        closeAction : 'hide',
	        region:"center",
	        layout: 'border',
	        plain:true,
	        bodyStyle:'padding:5px;',
	        buttonAlign:'center',
	        items: [pictureImportForm],
	        buttons : [ {
			text : '上传',
			handler : function() {
				var temp=pictureImportForm.getForm().getFieldValues().file.toLowerCase();
   				var XLSreg= /\.jpg$/;
   				var ZIPreg= /\.zip$/;
				if(temp=="" || !(XLSreg.exec(temp) == ".jpg" || ZIPreg.exec(temp) == ".zip")){ 
					Ext.MessageBox.alert('提示',"请选择单个jpg文件，或者将包含有jpg图片的文件夹压缩为zip文件上传！");
					return;
				}
				Ext.MessageBox.wait('正在上传文件...','请等待');
				pictureImportForm.getForm().submit({
					success : function(form, action) {
						Ext.MessageBox.wait('完成上传,后台正在处理...','请等待');
						UploadPicturesController.processTheUploadedPictures("tmp",function(data){
							var jsonData = Ext.util.JSON.decode(data);
							if(jsonData.success==true){
								Ext.MessageBox.alert("提示",jsonData.errors.info);
							}else{
								Ext.MessageBox.alert("提示",jsonData.errors.info);
							}
							studentgrid.store.reload();
						});
						pictureImportWin.hide();
					},
					failure : function() {
						Ext.MessageBox.alert("提示","上传失败");
						pictureImportWin.hide();
					}
				});
			}
		} ]
	});
var student = new Ext.data.Record.create([
      	{
      		name:'id'
      	},{
      		name:'name'
      	},{
      		name:'studentnum'
      	},{
      		name:'idnum'
      	},{				
      		name:'examnum'
      	},{
      		name:'hasPicture',convert:function(data){if(data=="1"){return "是";}else return "否";}
      	},{
      		name:'exCollege'
      	},{
      		name:'classname'
      	}
 ]);
var studentstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, student),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : UploadPicturesController.checkPicturesByModel
	  })
});
var studentgrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentgrid',
	store :studentstore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	columns : [new Ext.grid.RowNumberer({width:28}),
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :200,
	 	sortable :true
	},	{
		id : 'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :120,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :200,
	 	sortable :true
	},{
		id : 'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :200,
	 	sortable :true
	},{
		id: 'exCollege',
		header: '学院',
		dataIndex: 'exCollege',
		width: 200,
		sortable:true
	},{
		id: 'classname',
		header: '班级',
		dataIndex: 'classname',
		width: 150,
		sortable:true
	}],
		tbar:[modelSelectCombo,'-',{
	        text: '导入照片',
	        iconCls : 'import',
	        scope: this,
	        handler:function(){
	        	pictureImportWin.show();
	        }
		},{
	        text: '导出照片',
	        iconCls : 'export-icon',
	        scope: this,
	        handler:function(){
	        	Ext.MessageBox.wait('正在准备下载...','请等待');
	        	UploadPicturesController.packageAllPictures(function(data){
	        		var jsonData = Ext.util.JSON.decode(data);
					if(jsonData.success==true){
						Ext.MessageBox.alert("提示",jsonData.errors.info);
						window.open(jsonData.errors.address);
					}else{
						Ext.MessageBox.alert("提示",jsonData.errors.info);
					}
	        	});
	        }
		},{
			text: '清空多余照片',
	        iconCls : 'import',
	        scope: this,
	        handler:function(){
	        	Ext.MessageBox.confirm('提示', '您确定要清空多余照片?',
                        function(button) {
                           if (button == 'yes') {
                        	   Ext.MessageBox.wait('后台正在处理...','请等待');
                        	   UploadPicturesController.clearSurplusPictures(function(data){
                        		 var jsonData = Ext.util.JSON.decode(data);
       							if(jsonData.success==true){
       								Ext.MessageBox.alert("提示",jsonData.errors.info);
       							}else{
       								Ext.MessageBox.alert("提示",jsonData.errors.info);
       							}
       							studentgrid.store.reload();
                        	   });
                        	}
                        });
	        }	
		},{
			text: '清空照片',
	        iconCls : 'import',
	        scope: this,
	        handler:function(){
	        	Ext.MessageBox.confirm('提示', '您确定要清空全部照片?',
                        function(button) {
                           if (button == 'yes') {
                        	   Ext.MessageBox.wait('后台正在处理...','请等待');
                        	   UploadPicturesController.clearAllPictures(function(data){
                        		 var jsonData = Ext.util.JSON.decode(data);
       							if(jsonData.success==true){
       								Ext.MessageBox.alert("提示",jsonData.errors.info);
       							}else{
       								Ext.MessageBox.alert("提示",jsonData.errors.info);
       							}
       							studentgrid.store.reload();
                        	   });
                        	}
                        });
	        }
		}
    ],
	    bbar : new Ext.PagingToolbar({
    	pageSize:20,
        store : studentstore,
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
	        params.model=selectedModelNum;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  }),
     listeners:{
    	rowdblclick : function(grid){
    	}
    }
});
function pageInit(){
	 new Ext.Viewport( {
			layout :'border',
			hideMode: Ext.isIE ? 'offsets' : 'display',
			items : [studentgrid],
			renderTo :Ext.getBody()
		});
}