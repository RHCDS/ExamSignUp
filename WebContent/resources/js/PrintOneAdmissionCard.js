var studentAdmissionInfo_currentSearchFilter = "";

var comboCollegeStore = new Ext.data.JsonStore({
	fields:[
	        'value',
	        'name'
	        ]
});

var comboLanguageStore = new Ext.data.ArrayStore({
	fields:[
	        'languagenum',
	        'name'
	        ]
});



var comboProfessionStore = new Ext.data.ArrayStore({
	fields:[
	        'professionnum',
	        'name'
	        ]
});


var studentAdmissionInfo = new Ext.data.Record.create([
                                                   	{
                                                   		name:'exLanguage'
                                                   	},{
                                                   		name:'exProfession'
                                                   	},{
                                                   		name:'exCollege'
                                                   	},{
                                                   		name:'name'
                                                   	},{
                                                   		name:'studentnum'
                                                   	},{
                                                   		name:'idnum'
                                                   	},{
                                                   		name:'examnum'
                                                   	},{
                                                        name:'logicExamroomNum'
                                                    },{
                                                    	name:'theoryroomlocation'
                                                    },{
                                                    	name:'theorystarttime'
                                                    },{
                                                    	name:'operateroomlocation'
                                                    },{
                                                    	name:'operatestarttime'
                                                    },{
                                                    	name:'theoryendTime'
                                                    },{
                                                    	name:'operateendTime'
                                                    },{
                                                    	name:'exCampus'                                                   	                                                   	                                          
                                                   	},{
                                                    	name:'operateseat'                                                    	                                                   	                                          
                                                   	},{
                                                    	name:'classnum'                                                    	                                                   	                                          
                                                   	}
                                                   	
          ]);







var studentAdmissionInfostore = new Ext.data.Store( {
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getOneStudentAdmissionInfo
       })
    });



var studentAdmissionInfostore_hidden = new Ext.data.Store( {
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getOneStudentAdmissionInfo
       })
    });

/*
var studentAdmissionInfostore_hidden = new Ext.data.Store( {
    reader : new Ext.data.JsonReader({
         totalProperty : 'totalProperty',
         root : 'root'
       }, studentAdmissionInfo),
    proxy : new Ext.ux.data.DWRProxy({
       dwrFunction : StudentController.getStudentAdmissionInfo
       })
    });

*/





var sm=new Ext.grid.CheckboxSelectionModel();

var studentAdmissionInfoGrid = new Ext.grid.GridPanel({
	region:'center',
	id : 'studentadmissioncardgrid',
	store :studentAdmissionInfostore,
    loadMask :true,
    width : 5000,
	stripeRows :true,
	autoScroll:true,
	autoExpandColumn : 'name',
	viewConfig: {sortDescText: '降序',sortAscText: '升序',columnsText: '显示列',forceFit:false},
	//sm:sm,
	columns : [//new Ext.grid.RowNumberer(),
	        //  sm,
	{
		id : 'name',
		header : '姓名',
	  	dataIndex :'name',
	  	width :120,
	 	sortable :true
	},{
		id : 'exCollege',
		header:'学院',
	  	dataIndex:'exCollege',
	  	width :120,
	 	sortable :true
	},	{
		id : 'exProfession',
		header : '专业',
	  	dataIndex :'exProfession',
	  	width :100,
	 	sortable :true
	},	{
		id : 'classnum',
		header : '班级',
	  	dataIndex :'classnum',
	  	width :100,
	 	sortable :true
	},	{
		id : 'exLanguage',
		header : '语种',
	  	dataIndex :'exLanguage',
	  	width :150,
	 	sortable :true
	},	{
		id : 'studentnum',
		header : '学号',
	  	dataIndex :'studentnum',
	  	width :150,
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
		id : 'exCampus',
		header : '校区名称',
	  	dataIndex :'exCampus',
	  	width :120,
	 	sortable :true
	
	},{
		id : 'logicExamroomNum',
		header : '考场号',
	  	dataIndex :'logicExamroomNum',
	  	width :120,
	 	sortable :true
	},	{
		id : 'theoryroomlocation',
		header : '理论考试地址',
	  	dataIndex :'theoryroomlocation',
	  	width :200,
	 	sortable :true
	},{
		id : 'theorystarttime',
		header : '理论考试开始时间',
	  	dataIndex :'theorystarttime',
	  	width :220,
	 	sortable :true
	},{
		id : 'theoryendTime',
		header : '理论结束时间',
	  	dataIndex :'theoryendTime',
	  	width :120,
	 	sortable :true
	},{
		id : 'operateroomlocation',
		header : '上机考试地址',
	  	dataIndex :'operateroomlocation',
	  	width :120,
	 	sortable :true
	},{
		id : 'operatestarttime',
		header : '上机考试开始时间',
	  	dataIndex :'operatestarttime',
	  	width :220,
	 	sortable :true
	},{
		id : 'operateendTime',
		header : '上机结束时间',
	  	dataIndex :'operateendTime',
	  	width :120,
	 	sortable :true
	
	},{
		id : 'operateseat',
		header : '上机座位号',
	  	dataIndex :'operateseat',
	  	width :120,
	 	sortable :true
	
	}
	],

	
	 bbar : new Ext.PagingToolbar({
		  	  pageSize:30,//设置为每页30条记录
		      store : studentAdmissionInfostore,
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
			        params.filter = studentAdmissionInfo_currentSearchFilter;
			        params.start = start;
			        params.limit = this.pageSize;
			        if (this.fireEvent("beforechange", this, params) !== false) {
			            this.store.load({params:params});
			        }
			    }
	 })	 
	 
});












//js获取项目根路径，如： http://localhost:8080/uimcardprj
function getRootPath()
{    //获取当前网址，如： http://localhost:8080/uimcardprj/share/meun.jsp    
	var curWwwPath=window.document.location.href;    
	//获取主机地址之后的目录，如： uimcardprj/share/meun.jsp   
	var pathName=window.document.location.pathname;    
	var pos=curWwwPath.indexOf(pathName);    
	//获取主机地址，如： http://localhost:8080    
	var localhostPaht=curWwwPath.substring(0,pos);    
	//获取带"/"的项目名，如：/uimcardprj   
	var projectName=pathName.substring(0,pathName.substr(1).indexOf('/')+1);    
	return (localhostPaht+projectName);
	}

function studentAdmissionInfoGridInit(){	
	studentAdmissionInfostore.load({
		params : {
			filter : studentAdmissionInfo_currentSearchFilter,
			start : 0,
			limit : 20
		}
	});
	
	StudentController.getSchoolName(function(data){
		if(data){				
			xxmc=data;			
		}
		else{
   			Ext.MessageBox.alert('提示', "得不到学校名称！");
   		}
  	}); 
    Ext.MessageBox.confirm('提示', '请先确定已经有考场信息？',
            function(button) {
               if (button == 'yes') {
            	   window.open("../print/printTest.jsp");
            		}
            });
}





function printOneAdmissionCardPageInit() {
	
	
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [studentAdmissionInfoGrid],
		renderTo :Ext.getBody()
	});
	
	studentAdmissionInfoGridInit();

	
}
