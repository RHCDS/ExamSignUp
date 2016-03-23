var student_currentSearchFilter = "";

var student = new Ext.data.Record.create([
	{
		name:'id'
	},{
		name:'exInstitution'
	},{
		name:'exLanguage'
	},{
		name:'exProfession'
	},{
		name:'exCollege'
	},{
		name:'exStudentstatus'
	},{
		name:'exCampus'
	},{
		name:'name'
	},{
		name:'password'
	},{
		name:'sex',convert:function(data){if(data=="M"){return "男";}else return "女";}
	},{
		name:'studentnum'
	},{
		name:'idnum'
	},{
		name:'ethno'
	},{
		name:'examnum'
	},{
		name:'exambatch'
	},{
		name:'grade'
	},{
		name:'classnum'
	},{
		name:'lengthofyear'
	},{
		name:'studentcategory',convert:function(data){if(data=="1"){return "普通本科";}else if(data=="2"){return "普通专科";}else return "否";}
	},{
		name:'paied',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'score'
	},{
		name:'theoryabsent',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'operateabsent',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'theoryfraud',convert:function(data){if(data=="1"){return "是";}else return "否";}
	},{
		name:'operatefraud',convert:function(data){if(data=="1"){return "是";}else return "否";} 
	}
]);

var emptystudent = new student({
	id : '',
	exInstitution : '',
	exLanguage : '',
	exProfession : '',
	exCollege : '',
	exStudentstatus : '',
	exCampus : '',
	name : '',
	password : '',
	sex : '',
	idnum : '',
	studentnum : '',
	ethno : '',
	examnum : '',
	exambatch : '',
	grade : '',
	classnum : '',
	lengthofyear : '',
	studentcategory : '',
	paied : '',
	score : '',
	theoryabsent : '',
	operateabsent : '',
	theoryfraud : '',
	operatefraud : ''
});

var studentstore = new Ext.data.Store( {
	reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
      }, student),
	proxy : new Ext.ux.data.DWRProxy({
	     dwrFunction : StudentController.paginationShow
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
	  	width :100,
	 	sortable :true
	},	{
		id : 'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :120,
	 	sortable :true
	},	{
		id : 'exLanguage',
		header : '语种',
	  	dataIndex :'exLanguage',
	  	width :200,
	 	sortable :true
	},	{
		id : 'examnum',
		header : '准考证号',
	  	dataIndex :'examnum',
	  	width :180,
	 	sortable :true
	},{
		id : 'idnum',
		header : '身份证号',
	  	dataIndex :'idnum',
	  	width :200,
	 	sortable :true
	},	{
		id : 'exProfession',
		header : '专业',
	  	dataIndex :'exProfession',
	  	width :150,
	 	sortable :true
	},	{
		id : 'exInstitution',
		header : '学校',
	  	dataIndex :'exInstitution',
	  	width :150,
	 	sortable :true
	}, {
		id : 'exCollege',
		header : '学院',
	  	dataIndex :'exCollege',
	  	width :150,
	 	sortable :true
	}, {
		id : 'exCampus',
		header : '校区',
	  	dataIndex :'exCampus',
	  	width :150,
	 	sortable :true
	}, {
		id : 'sex',
		header : '性别',
	  	dataIndex :'sex',
	  	width :50,
	 	sortable :true
	}, {
		id : 'ethno',
		header : '民族',
	  	dataIndex :'ethno',
	  	width :80,
	 	sortable :true
	}, {
		id : 'exambatch',
		header : '考试批次',
	  	dataIndex :'exambatch',
	  	width :100,
	 	sortable :true
	}, {
		id : 'grade',
		header : '年级',
	  	dataIndex :'grade',
	  	width :100,
	 	sortable :true
	}, {
		id : 'classnum',
		header : '班级',
	  	dataIndex :'classnum',
	  	width :100,
	 	sortable :true
	}, {
		id : 'lengthofyear',
		header : '学制',
	  	dataIndex :'lengthofyear',
	  	width :50,
	 	sortable :true
	}, {
		id : 'studentcategory',
		header : '学生类别',
	  	dataIndex :'studentcategory',
	  	width :100,
	 	sortable :true
	}, {
		id : 'paied',
		header : '是否缴费',
	  	dataIndex :'paied',
	  	width :100,
	 	sortable :true
	}, {
		id : 'theoryabsent',
		header : '理论缺考',
	  	dataIndex :'theoryabsent',
	  	width :100,
	 	sortable :true
	}, {
		id : 'operateabsent',
		header : '上机缺考',
	  	dataIndex :'operateabsent',
	  	width :100,
	 	sortable :true
	}, {
		id : 'theoryfraud',
		header : '理论作弊',
	  	dataIndex :'theoryfraud',
	  	width :100,
	 	sortable :true
	}, {
		id : 'operatefraud',
		header : '上机作弊',
	  	dataIndex :'operatefraud',
	  	width :100,
	 	sortable :true
	}, {
		id : 'score',
		header : '分数',
	  	dataIndex :'score',
	  	width :100,
	 	sortable :true
	}
	],
		tbar:[{
	        text: '批量导入报名考生',
	        iconCls : 'import',
	        scope: this,
	        handler:function(){
	        	studentimprot().show();
	        }
		},/*'-', {
	        text: '导出考生库EXCEL',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	exportStudentsExcel();
	        }
	    },'-', {
	        text: '导出考生库DBF',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	exportStudentsDbf();
	        }
	    },*/'-', {
	        text: '新增',
	        iconCls : 'add',
	        scope: this,
	        handler:function(){
	        	enableOfStudentbasicform();
	        	college_studentbasicform.getForm().loadRecord(emptystudent);
	        	studentInfoWinInit("新增考生信息").show();
	        }
	    },'-', {
	        text: '编辑',
	        iconCls : 'edit',
	        scope: this,
	        handler:function(){
	        	studentgrid.fireEvent('rowdblclick', studentgrid);
	        }
	    }, '-', {
	        text: '删除',
	        iconCls : 'remove',
	        scope: this,
	        handler:function(){
	        	if(studentgrid.getSelectionModel().hasSelection()){
                    Ext.MessageBox.confirm('提示', '你确定要删除选中的考生么?',
                            function(button) {
                               if (button == 'yes') {
                                  var list = studentgrid.getSelectionModel().getSelections();
                                  var rList = [];
                                  for (var i = 0; i <list.length; i++) {
                                     rList[i] = list[i].data["id"];
                                  }
                                  
                                  StudentController.deleteStudents(
                                          rList, function(data) {
                                             if (data) {
                                                Ext.MessageBox.alert('提示','删除考生成功!');
                                                studentGridInit();
                                             } else {
                                                Ext.MessageBox.alert('提示',"删除考生失败!");
                                             }
                                          });
                            		}
                            });
	        	}
	        	else
	        		Ext.MessageBox.alert('提示', "请至少选择一条记录!");
	        }
	    },'-' ,{
	        text: '查询',
	        iconCls : 'upload-icon',
	        scope: this,
	        handler:function(){
	        	student_searchWin.show();
	        	student_searchform.getForm().reset();
	        }
	    },'-' ,{
	        text: '打印校对单',
	        iconCls : 'print',
	        scope: this,
	        handler:function(){
	        	 tabAdd("PrintProofBill","print/PrintProofBill.jsp","打印校对单");
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
	        params.filter = student_currentSearchFilter;
	        params.start = start;
	        params.limit = this.pageSize;
	        if (this.fireEvent("beforechange", this, params) !== false) {
	            this.store.load({params:params});
	        }
	    }
  }),
     listeners:{
    	rowdblclick : function(grid){
    		enableOfStudentbasicform();
    		if(grid.getSelectionModel().hasSelection()){ 
    			stunum = grid.getSelectionModel().getSelected().data.studentnum;
    			studentInfoWinInit("编辑考生信息").show(); 
    			StudentController.getStudentPhotoPathByStudentNum(grid.getSelectionModel().getSelected().data.studentnum,function(filepath){
    				if(filepath==""){
    					Ext.getCmp("stuphoto").getEl().dom.src = '../resources/icons/delete.gif';
    					Ext.MessageBox.alert('提示',"没有上传照片,请通过'系统配置'中的'上传照片'功能上传照片。");
    				}
    				else {
    					Ext.getCmp("stuphoto").getEl().dom.src = filepath;
    				}
    			});
    			college_studentbasicform.getForm().setValues(grid.getSelectionModel().getSelected().data);
    		}else{
    			Ext.MessageBox.alert('提示',"请选择一条信息进行编辑!");
    		}

    	}
    }
});


var studentimportform = new Ext.form.FormPanel({
	labelAlign : 'left',
	labelWidth : 60,
	region : 'center',
	buttonAlign : 'center',
	frame : true,
	url : '../xlsToJson.do',//fileUploadServlet  
	width : 300,
	height : 200,
	fileUpload : true,

	items : [ {
		xtype : 'textfield',
		fieldLabel : '文件名',
		name : 'file',
		inputType : 'file'//文件类型 
	} ]
});



var studentimportwin;
function studentimprot(){
	if(!studentimportwin){
		studentimportwin = new Ext.Window({
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
   	        items: studentimportform,
   	     buttons : [ {
   			text : '上传',
   			handler : function() {
   				Ext.MessageBox.wait('正在检查数据,请等待...','提示');
   				studentimportform.getForm().submit({
   					success : function(form, action) {
   						var jsonData = Ext.util.JSON.decode(action.response.responseText);
           				if(jsonData.success==true){
           					Ext.MessageBox.alert('提示',jsonData.errors.info);
           					studentGridInit();
           				}else{
           					Ext.MessageBox.alert('提示',jsonData.errors.info);
           				}				  						
   					},
  					failure : function(form, action) {
  						jsonData = Ext.util.JSON.decode(action.response.responseText);
   						Ext.Msg.alert('文件上传失败', jsonData.errors.info,function(){
   							studentimportwin.hide();
   						});
   					}
   				});
   			}
   		} ]
		});
	}
	return studentimportwin;
}

var importstudentpicwin;

var importstudentpicpanel =  new Ext.form.FormPanel({	
	frame:true
		,items:{
			xtype:'awesomeuploader'
			,gridHeight:280
			,height:350
			,awesomeUploaderRoot:'/ExamSignUp/resources/awesomeuploader/'
			,listeners:{
				scope:this
				,fileupload:function(uploader, success, result){
					if(success){
						Ext.Msg.alert('File Uploaded!','A file has been uploaded!');
					}
				}
			}
		}
});

function studentPicImport(){
	if(!importstudentpicwin){
		importstudentpicwin = new Ext.Window({
			title:'照片导入'
			,closeAction:'hide'
			,width:520
			,height:350
			,layout : 'fit'
			,items:[importstudentpicpanel]
		});
	}
	return importstudentpicwin;
}

var studentinfowin;

function studentInfoWinInit(title){
	if(!studentinfowin){
		studentinfowin = new Ext.Window({
   	        title: '',
   	        width: 800,
   	        height:400,
   	        closeAction : 'hide',
   	        layout: 'fit',
   	        plain:true,
   	        bodyStyle:'padding:5px;',
   	        items: [college_studentform]
		});
	}
	loadLanguagesName();
	loadCampusList();
	studentinfowin.setTitle(title);
	return studentinfowin;
}

var student_searchform = new Ext.form.FormPanel({
	id:'student_searchform',
	region:'center',
//	autoScroll:true,
//	bodyStyle:"overflow-y:scroll;overflow-x:hidden",
	frame:true,
	labelWidth:68,
	autoHeight:true,
	items:[{
	        xtype:'fieldset',
	        title: '考生信息',
	        autoHeight:true,
	        layout:'column',
	        items :[
        		{columnWidth:.5,layout:'form',items:[
					{xtype:'textfield',fieldLabel: '考生姓名',name:'and$student-name$like$%value%',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
					{xtype:'textfield',fieldLabel: '身份证号',name:'and$student-idnum$=$value',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
				    {xtype:'textfield',fieldLabel: '考生学号',name:'and$student-studentnum$=$value',anchor:'96%'}
				 ]},
				{columnWidth:.5,layout:'form',items:[
					{xtype:'textfield',fieldLabel: '准考证号',name:'and$student-examnum$=$value',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
				    {xtype:'textfield',fieldLabel: '院系名称',name:'and$student-exCollege-name$like$%value%',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
				    {xtype:'textfield',fieldLabel: '语种名称',name:'and$student-exLanguage-name$like$%value%',anchor:'96%'}
				]},
				{columnWidth:.5,layout:'form',items:[
			        {xtype:'textfield',fieldLabel:'班级名称',name:'and$student-classnum$=$value',anchor:'96%'}                                     
				]}
			]
	    }],
	keys: [{
          key: 13,//回车
          fn: function(){
    	  	student_search();
    	  	student_searchWin.hide();
      	  }
	}]
});

var student_searchWin = new Ext.Window({
	title: '查询条件',
	width: 500,
    autoHeight:true,
    minWidth: 300,
    minHeight: 200,
    closeAction : 'hide',
    layout: 'fit',
    plain:true,
    buttonAlign:'center',
	items: [student_searchform],
	modal: true,
	shadow: true,
	buttons:[{
                text : '查询',
                handler : function() {
                	student_search();
                	student_searchWin.hide();
                }
         }, {
                text : '清空',
                handler : function() {
                	student_searchform.getForm().reset();
                	student_currentSearchFilter = "";
                }
         }, {
                text : '关闭',
                handler : function() {
                	student_searchWin.hide();
                }
         }]
});

function exportStudentsExcel(){
	var f = document.getElementById('exportStudentsExcel');
	f.action = '../exportStudentsExcel.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}

function exportStudentsDbf(){
	var f = document.getElementById('exportStudentsDbf');
	f.action = '../exportStudentsDbf.do';
	f.submit({
		failure : function(form,action){
			var error = Ext.util.JSON.decode(action.response.responseText).error;
			Ext.MessageBox.alert('导出失败',"原因：" + error);
		}
		
	});
}

function student_search(){
	student_currentSearchFilter = Ext.encode(student_searchform.getForm().getFieldValues());
	studentstore.load({
		params : {
					filter : student_currentSearchFilter,
					start : 0,
					limit : 20
		}
	});
}


function studentGridInit(){
	studentstore.load({
		params : {
			filter : student_currentSearchFilter,
			start : 0,
			limit : 20
		}
	});
}

function studentInfoManagerPageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [studentgrid],
		renderTo :Ext.getBody()
	});
	studentGridInit();
}

function tabAdd(tabId,script,tabTitle){
	tabPanel = window.top.tabPanel;
	if(tabPanel.findById(tabId)==null)
	{
		tabPanel.add({
			id:tabId,
			title : tabTitle,
	        height:500,  
	        autoScroll:true,  
	        autoWidth:true,
	        closable:true,  
	        frame:true,  
	        html:'<iframe id="'+tabId+'_Frame" src="'+script+'" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>',
	        listeners:{
                activate:function(){
                 this.getUpdater().refresh();
                },
                beforeclose:function(){
                	var frame = window.top.document.getElementById(this.id+'_Frame');
                	if(frame.scr!=null)
                		frame.src = "javascript:false";
                }
	        }
		});
	}
	window.top.viewport.doLayout(true,true);
	tabPanel.show();
	tabPanel.setActiveTab(tabId);
};
