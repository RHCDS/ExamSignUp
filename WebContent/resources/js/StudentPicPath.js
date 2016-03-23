
var studentpicpath = Ext.data.Record.create([{
	name :'id'
}, {
	name :'name'
}, {
	name :'paramvalue'
}, {
	name :'paramtype'
}, {
	name :'enumvalue'
},{
	name :'memo'
}
]);

var  studentpicpathformfields =  [
  {
	columnWidth : 1,
	layout : 'form',
	items : [ {
		fieldLabel : '考生照片路径',
		name : 'paramvalue',
		id : 'studentpicpath',
		xtype : 'textfield',
		width : 110,
		autoWidth : false,
		editable : true,
		anchor : '98%',
		allowBlank : false
	},
	{xtype:'hidden',name: 'id',anchor:'98%'},
	{xtype:'hidden',name: 'name',anchor:'98%'},
	{xtype:'hidden',name: 'paramtype',anchor:'98%'},
	{xtype:'hidden',name: 'enumvalue',anchor:'98%'},
	{xtype:'hidden',name: 'memo',anchor:'98%'}
	]}
];

var studentpicpathform = new Ext.FormPanel({
    id : 'studentbasicform',
	labelWidth: 100,
	region : 'center',
	frame : true,
    labelAlign : 'left',
    layout:'column',
    buttonAlign : 'center',
    items : [studentpicpathformfields],
    autoHeight : true,
    buttons : [{
        text : '保存',
        handler : function() {
        	if(studentpicpathform.getForm().isValid()){
         	var studentpicpath = studentpicpathform.getForm().getValues();
         	ParameterController.saveParameter(studentpicpath,function(data){ 
     	    	if(data){
     	    		Ext.MessageBox.alert('提示',"保存成功！");
     	    	}else
     	    		Ext.MessageBox.alert('提示',"保存失败！");
     	    });            	
        	
        	}else{
        		Ext.MessageBox.alert('提示',"输入信息有误！");
        	}
         }
               		
    	}]
});


function studentPicPathPageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [studentpicpathform],
		renderTo :Ext.getBody()
	});
	ParameterController.findParameterByType("考生照片路径",function(data){
		if(data){
			studentpicpathform.getForm().setValues(data);
		}else{
			var newstudentpicpath = new studentpicpath({
				id : '',
				name : '',
				paramvalue : '',
				paramtype : '考生照片路径',
				enumvalue : '',
				memo : ''
			});
			studentpicpathform.getForm().loadRecord(newstudentpicpath);
		}
	});
}