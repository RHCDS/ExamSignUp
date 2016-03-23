var studentimportwin;
 var stunum ="";

var languagestore = new Ext.data.ArrayStore({
	
		fields : ['id',
		          'languagename'
		          ]
});

var campusstore = new Ext.data.ArrayStore({
	fields:[
	        'id',
	        'campusname'
	        ]
});

var  studentfields = [
    {columnWidth:.3,layout:'form',items:[
		{xtype:'hidden',name: 'id',anchor:'95%'},
		{xtype:'textfield',id:'name',fieldLabel: '考生姓名',name: 'name',anchor:'97.5%',allowBlank: false}
	]},
	{columnWidth:.3,layout:'form',items:[
		{id:'studentnum',xtype:'textfield',id:'studentnum',fieldLabel: '学   号',name: 'studentnum',anchor:'97.5%',allowBlank: false}
	]},{columnWidth:.4,layout:'form',items:[
	     {xtype:'textfield',fieldLabel: '身份证号',id:'idnum',name: 'idnum',anchor:'97.5%',maxLength:18,validator : idNumCheck, allowBlank: false}
	 ]},
	{columnWidth:.3,layout:'form',items:[
		{fieldLabel:'性    别',id:'sex',name:'sex',xtype: 'combo', store:['男','女'],editable:false,triggerAction : 'all',anchor:'97.5%',allowBlank: false}
	]},
	{columnWidth:.3,layout:'form',items:[
		{fieldLabel: '民   族',id:'ethno',name: 'ethno',xtype: 'combo',editable:false,triggerAction : 'all', store : ['汉','蒙古','回','藏','维吾尔','苗','彝','壮','布依','朝鲜','满','侗','瑶','白','土家','哈尼','哈萨克','傣','黎','傈傈','佤','畲','高山','拉祜','水','东乡','纳西','景颇','柯尔克孜','土','达斡尔','仫佬','羌','布朗','撒拉','毛南','仡佬','锡伯','阿昌','普米','塔吉克','怒','乌兹别克','俄罗斯','鄂温克','德昂','保安','裕固','京','塔塔尔','独龙','鄂伦春','赫哲','门巴','珞巴','基诺'], anchor:'97.5%'}
	]},
	{columnWidth:.4,layout:'form',items:[
		{xtype:'combo',fieldLabel: '考试语种',id:'exLanguage',name: 'exLanguage',store:languagestore,mode:'local',lazyInit:false,displayField:'languagename', valueField:'id',editable:false,triggerAction : 'all',emptyText:'-请选择-',anchor:'97.5%',allowBlank: false}
	]},
	{columnWidth:.3,layout:'form',items:[
		{xtype:'combo',store: campusstore,id:'exCampus',mode: 'local',valueField:'id',displayField:'campusname',triggerAction:'all',fieldLabel: '校区',editable:false,name: 'exCampus',allowBlank: false,anchor:'97.5%'}
	]},
	{columnWidth:.3,layout:'form',items:[
		{xtype:'textfield',id:'exCollege',fieldLabel: '学院',name: 'exCollege',anchor:'97.5%',allowBlank:false}
	]},
	{columnWidth:.4,layout:'form',items:[
		{xtype:'textfield',id:'exProfession',fieldLabel: '专业',name: 'exProfession',anchor:'97.5%',allowBlank:false}
	]},
	{columnWidth:.3,layout:'form',items:[
		{xtype:'textfield',id:'grade',fieldLabel: '年级',name: 'grade',anchor:'97.5%'}
	]},
	{columnWidth:.3,layout:'form',items:[
		{xtype:'textfield',id:'classnum',fieldLabel: '行政班',name: 'classnum',anchor:'97.5%',allowBlank:false}
	]},
	{columnWidth:.4,layout:'form',items:[
	    {xtype:'textfield',id:'lengthofyear',fieldLabel: '学制',name: 'lengthofyear',anchor:'97.5%'}
	]},
	{columnWidth:.3,layout:'form',items:[
	    {xtype:'combo',id:'studentcategory',fieldLabel: '考生类别',name: 'studentcategory',store:['普通本科','普通专科','其他'],editable:false,triggerAction : 'all',anchor:'97.5%',allowBlank:false}
	]},
	{columnWidth:.3,layout:'form',items:[
	    {xtype:'combo',id:'paied',fieldLabel: '是否缴费',store:['是','否'],editable:false,triggerAction : 'all',name: 'paied',anchor:'97.5%',allowBlank:false}
	]}	
	
];
//高校用户
var college_studentbasicform = new Ext.FormPanel({
    id : 'college_studentbasicform',
	width : 600,
    labelWidth: 70,
	region : 'center',
    labelAlign : 'left',
    layout:'column',
    items : [studentfields]
});
//学生用户
var studentbasicform = new Ext.FormPanel({
    id : 'studentbasicform',
	width : 600,
    labelWidth: 70,
	region : 'center',
    labelAlign : 'left',
    layout:'column',
    items : [studentfields]
});
//高校用户
var  college_studentphotofields =  [
	{
		columnWidth : 1,
		layout : 'form',
		labelWidth : 0,
		items : [ {
			fieldLabel : '',
			name : 'stuphoto',
			id : 'stuphoto',
			xtype : 'box',
			width : 133.3,
			height : 160,
			autoWidth : false,
			editable : false,
			anchor : '90%',
			autoEl : {
				tag : 'img'
			}
		}]
	}
];
//学生用户
var  studentphotofields =  [
  {
	columnWidth : 1,
	layout : 'form',
	labelWidth : 0,
	items : [ {
		fieldLabel : '',
		name : 'photo',
		id : 'photo',
		xtype : 'box',
		width : 133.3,
		height : 160,
		autoWidth : false,
		editable : false,
		anchor : '90%',
		autoEl : {
			tag : 'img'
		}
	}]},
	{
		columnWidth : 1,
		layout : 'form',
		labelWidth : 0.01,
		autoWidth : true,
		items : [{
			fieldLabel : '',
			name : 'importphoto',
			xtype : 'textfield',
			inputType : 'file',
			anchor : '96%'
		}]
	}
];

//高校用户
var college_studentphotoform = new Ext.form.FormPanel({
	labelAlign : 'left',
	width : 170,
	height : 210,
	fileUpload : false,
	autoHeight : true,
	region : 'east',
	items : [ college_studentphotofields],
	buttonAlign : 'center'
});

//学生用户
var studentphotoform = new Ext.form.FormPanel({
	labelAlign : 'left',
	width : 170,
	url : '../importStudentPhoto.do',//importstudentphotoServlet 
	height : 210,
	fileUpload : true,
	autoHeight : true,
	region : 'east',
	items : [ studentphotofields],
	buttonAlign : 'center',
	buttons : [ {
		text : '上传',
		id : 'uploadpic',
		handler : function() {
			studentphotoform.getForm().submit({
				success : function(form, action) {
					
					StudentController.getStudentPhotoPathByFileName(Ext.util.JSON.decode(action.response.responseText).fileName,function(data){
						Ext.getCmp("photo").getEl().dom.src = data;
						Ext.Msg.alert('提示', '文件上传成功！');
					});
				},
				failure : function(form, action) {
					var errors = Ext.util.JSON.decode(action.response.responseText).errors.info;
					Ext.Msg.alert('错误', errors);
				}
			});
		}
	} ]
});
//学生用户
var studentform = new Ext.Panel({
    id : 'studentform',
    title : '报名信息',
	labelWidth: 80,
	height:400,
	autoScroll : true,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'south',
    layout:'border',
    buttonAlign : 'center',
    items : [studentphotoform,studentbasicform],
    buttons : [{
        text : '保存',
        handler : function() {
        	//enableOfStudentbasicform();
        	Ext.MessageBox.wait("正在保存...","提示");
        	if(studentbasicform.getForm().isValid()){
        		var student = {
        				classnum : '',
        				lengthofyear : '',
        				grade : '',
        				ethno : ''
        		};
        		enableOfStudentbasicform();
        		student = studentbasicform.getForm().getValues();
        		stunum = Ext.getCmp("studentnum").getValue();
        		readOnlyOfStudentbasicform();
        		checkSignUpStatus();
      			StudentController.getStudentPhotoPathByStudentNum(stunum,function(filepath){
    				if(filepath==""){
    					Ext.getCmp("photo").getEl().dom.src = '../resources/icons/delete.gif';
    					Ext.MessageBox.alert('提示',"请先上传规格正确的照片！");
    				}else{
    		         	StudentController.saveStudent(student,function(data){ 
    		     	    	if(data){
    		     	    		Ext.MessageBox.alert('提示',"保存成功！");
    		     	    	}else
    		     	    		Ext.MessageBox.alert('提示',"保存失败！");
    		     	    });
    		         //	readOnlyOfStudentbasicform();
    				}
    			});
           	
        	}else{
        		Ext.MessageBox.alert('提示',"输入信息有误！");
        	}
        	
         }  		
    	}, {
        text : '清空',
        id : 'clearstuinfo',
        handler : function() {
        	//enableOfStudentbasicform();
        	studentphotoform.getForm().reset();
        	studentbasicform.getForm().reset();
        	studentbasicform.getForm().reset();
         }
    	}]
});

//高校用户

var college_studentform = new Ext.Panel({
    id : 'college_studentform',
    title : '报名信息',
	labelWidth: 80,
	height:400,
	autoScroll : true,
    frame : true,
    bodyStyle:'padding:5',
    labelAlign : 'left',
    region : 'south',
    layout:'border',
    buttonAlign : 'center',
    items : [college_studentphotoform,college_studentbasicform],
    buttons : [{
        text : '保存',
        handler : function() {
        	//enableOfStudentbasicform();
        	Ext.MessageBox.wait("正在执行...","提示");
        	if(college_studentbasicform.getForm().isValid()){
        		var student = {
        				classnum : '',
        				lengthofyear : '',
        				grade : '',
        				ethno : ''
        		};
        		enableOfStudentbasicform();
        		student = college_studentbasicform.getForm().getValues();
        		stunum = Ext.getCmp("studentnum").getValue();
        		StudentController.saveStudent(student,function(data){ 
        			if(data){
        				Ext.MessageBox.alert('提示',"保存成功！");
        			}else {
        				Ext.MessageBox.alert('提示',"保存失败！");
        			}
    		        //	readOnlyOfStudentbasicform();
    			});
        	}else{
        		Ext.MessageBox.alert('提示',"输入信息有误！");
        	}
        	
         }  		
    	}, {
        text : '清空',
        id : 'clearstuinfo',
        handler : function() {
        	//enableOfStudentbasicform();
        	college_studentbasicform.getForm().reset();
        	college_studentbasicform.getForm().reset();
         }
    	}]
});

var studentacceptance = new Ext.Panel({
	title : '考试承诺书',
	region : 'north',
	frame:true,
	height : 220,
	items :[
		    {columnWidth:1,  
		     height:300,      	
			 layout:'form',
			 items:[{xtype:'textarea',
				     labelWidth:0,
				     name:'kSCNS',
				     readOnly:true,
				     width:'800',
				     height:'300',
				     anchor:'96%',
				     value:'                                                                       浙江省高校计算机等级考试考生诚信承诺书\n\n本人自愿参加高校计算机等级考试，现郑重承诺： \n一、本人坚决遵守高校计算机等级考试有关报名规定，不弄虚作假，不伪造、使用假证明、假证书。如有违反，自愿按规定接受处理。\n二、本人报名时所提供的个人信息是真实、准确、完整的，如因个人信息错误、失真、缺失造成不良后果，责任由本人承担。\n三、本人坚决服从考场工作人员和监考教师管理,自觉遵守考试纪律，考试诚实守信，不违规，不作弊。 \n四、本人已阅读并且透彻地理解了高校计算机等级考试的有关规定和纪律要求，愿意在考试中自觉遵守这些规定，保证按规定的程序和要求参加考试，如有违反，自愿按《国家教育考试违规处理办法》有关条款接受处理。'
			}]
		    }]
});

var signUpKnow = new Ext.Panel({
	title : '报名须知',
	region : 'center',
	frame:true,
	autoScroll : true,
	height : 505,
	items :[
		    {columnWidth:1,  
		     height:505,      	
			 layout:'form',
			 items:[{xtype:'textarea',
				     labelWidth:0,
				     name:'bmxz',
				     readOnly:true,
				     width:'800',
				     height:'480',
				     anchor:'95%',
				     value:'                                                浙江省高校计算机等级考试报名须知\n\n\n请认真阅读以下信息： \n\n一、首先请您仔细阅读考生承诺书，否则您有违背承诺书的行为，后果自负。\n\n二、报名条件\n\n（一）本省全日制普通高等院校专科、本科、研究生在校生；\n\n（二）本省各类成人高等院校专科、本科在校生；\n\n（三）报考学生须修完所报级别教学大纲规定内容；\n\n（四）本校已设考点的在校生原则上不得跨校参加报名考试；\n\n（五）各考点学校不得接受社会考生报考；\n\n（六）不设考点学校的学生报考，须与考点学校商定、签订协议，并报我院社考处备案后，方可组织学生统一到考点学校报名。\n\n三、填写报名信息时，您只需要选择考试语种以及上传个人照片即可，其它信息系统已经自动给出。\n\n四、上传的照片必须是本人近期照片，如果不能清晰辨认容貌或与本人相貌不符的，将不能参加考试。 \n\n五、照片要求存储为jpg格式，图象文件名为*.jpg，其中*为考生学号。容量不得大于100KB。成像要求：成像区上下要求头上部空1/10，\n头部占7/10，肩部占1/5，左右各空1/10。采集的图象大小最小为192×144（高×宽）。成像区大小为48mm×33mm（高×宽）。\n\n六、请按照规定及时交费，不交费后果自负。'
			}]
		    }]
});
function loadLanguagesName(){
	LanguageController.getLanguagesName(function(list){
		if(list.length>0){
			languagestore.loadData(list);
		}
	});
}

function loadCampusList(){
  	CampusController.getCampusList(function(data){
		if(data){
			campusstore.loadData(data);
		}
  	});
}

function readOnlyOfStudentbasicform(){
	Ext.getCmp("name").setDisabled (true);
	Ext.getCmp("studentnum").setDisabled (true);
	Ext.getCmp("idnum").setDisabled (true);
	//Ext.getCmp("sex").setDisabled (true);
	Ext.getCmp("ethno").setDisabled (true);
	//Ext.getCmp("exLanguage").setDisabled (true);
	Ext.getCmp("exCampus").setDisabled (true);
	Ext.getCmp("exCollege").setDisabled (true);
	Ext.getCmp("exProfession").setDisabled (true);
	Ext.getCmp("grade").setDisabled (true);
	Ext.getCmp("uploadpic").setDisabled(true);
	Ext.getCmp("classnum").setDisabled (true);
	Ext.getCmp("lengthofyear").setDisabled (true);
	Ext.getCmp("studentcategory").setDisabled (true);
	Ext.getCmp("paied").setDisabled (true);
	Ext.getCmp("clearstuinfo").setDisabled (true);
}

function enableOfStudentbasicform(){
	Ext.getCmp("name").setDisabled(false);
	Ext.getCmp("studentnum").setDisabled(false);
	Ext.getCmp("uploadpic").setDisabled(false);
	Ext.getCmp("idnum").setDisabled(false);
	Ext.getCmp("sex").setDisabled(false);
	Ext.getCmp("ethno").setDisabled(false);
	Ext.getCmp("exCampus").setDisabled(false);
	Ext.getCmp("exCollege").setDisabled (false);
	Ext.getCmp("exProfession").setDisabled (false);
	Ext.getCmp("grade").setDisabled (false);
	Ext.getCmp("exLanguage").setDisabled (false);
	Ext.getCmp("classnum").setDisabled (false);
	Ext.getCmp("lengthofyear").setDisabled (false);
	Ext.getCmp("studentcategory").setDisabled (false);
	Ext.getCmp("paied").setDisabled (false);
	Ext.getCmp("clearstuinfo").setDisabled (false);
} 

function loadStudentAllInfo()
{
	StudentController.getStudentAllInfo(function(data){
		studentbasicform.getForm().setValues(data);
	});
	
}

function StudentSignUpPageInit(studentnum) {
	stunum = studentnum;
	loadLanguagesName(); 
	loadCampusList();
	checkSignUpStatus();
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [signUpKnow,studentform],
		renderTo :Ext.getBody()
	});
	loadStudentAllInfo();
	readOnlyOfStudentbasicform();
	hiddWindow.show(); 

}

var student_searchform = new Ext.form.FormPanel({
	id:'student_searchform',
	region:'center',
//	autoScroll:true,
//	bodyStyle:"overflow-y:scroll;overflow-x:hidden",
	frame:true,
	labelWidth:0,
	labelAlign:'center',
	autoScroll : true, 
	autoHeight : true,
	items :[
		    {columnWidth:1,  
		     height:390,      	
			 layout:'form',
			 items:[{xtype:'textarea',
				     name:'kSCNS',
				     readOnly:true,
				     width:'380',
				     height:'380',
				     anchor:'96%',
				     value:'                            浙江省高校计算机等级考试考生诚信承诺书\n\n\n本人自愿参加浙江省高校计算机等级考试，现郑重承诺： \n\n一、本人坚决遵守计算机等级考试有关报名规定，不弄虚作假，不伪造、使用假证明、假证书。如有违反，自愿按规定接受处理。\n\n二、本人报名时所提供的个人信息是真实、准确、完整的，如因个人信息错误、失真、缺失造成不良后果，责任由本人承担。\n\n三、本人坚决服从考场工作人员和监考教师管理,自觉遵守考试纪律，考试诚实守信，不违规，不作弊。 \n\n四、本人已阅读并且透彻地理解了计算机等级考试的有关规定和纪律要求，愿意在考试中自觉遵守这些规定，保证按规定的程序和要求参加考试，如有违反，自愿按《国家教育考试违规处理办法》有关条款接受处理。'
			}]
		    }]
});

var agree = false;
var hiddWindow = new Ext.Window({
	title: '考生承诺书',
    width: 620,
    height:420,
    closeAction : 'hide',
    layout: 'fit',
    autoScroll : true, 
    plain:true,
//    border:false,
    buttonAlign:'center',
    bodyStyle:'padding:5px;',
    hideMode: Ext.isIE ? 'offsets' : 'display',
	items: [student_searchform],
	modal: true,
	shadow: true,
	buttons:[{
                text : '同意',
                anchor:'96%',
                handler : function() {
                	Ext.MessageBox.confirm("确认","确定认真阅读过考生承诺书?",function(btn){				
    					if(btn=="yes"){   						
    						//student_search();
    						agree = true;
    						hiddWindow.hide();
    						}
    										
    				});		
                	
                }
         }],
         listeners : {
        	 "beforehide" : function(){
        		 if(!agree){
            		 Ext.MessageBox.alert('提示',"请先认真阅读承诺书，并点击同意!");
        		 }
        		 return agree;
        	 }
         }
});
function checkSignUpStatus() {
	InstitutionController.checkSignUpStatus(function(data){
		var jsonData = Ext.util.JSON.decode(data);
		if(jsonData.signupflag == "On") {
			Ext.getCmp("exLanguage").setDisabled(false);
			Ext.getCmp("sex").setDisabled(false);
			Ext.getCmp("uploadpic").setDisabled(false);
		} else {
			Ext.getCmp("exLanguage").setDisabled(true);
			Ext.getCmp("uploadpic").setDisabled(true);
		}
	});
}
function signUpPageInit() { 
	loadLanguagesName();
	loadCampusList();
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [studentacceptance,studentform],
		renderTo :Ext.getBody()
	});
	
	
}