var collegesignup= Ext.data.Record.create([{
			name : 'examnum'
		}, {
			name : 'languagename'
		},{
			name : 'stuname'
		}, {
			name : 'score'
		}, {
			name : 'newscore'
		}, {
			name : 'status'
		}
]);

var collegesignupstore = new Ext.data.JsonStore({
	fields : collegesignup
	});

var reasonfields = [{
	columnWidth : 1,
	layout : 'form',
	labelWidth : 60,
	items : [{
				xtype : 'textarea',
				fieldLabel : '复查理由',
				name : 'reason',
				id: 'reason',
				height : 480,
				anchor : '98.65%',
				allowBlank : true
			}]
}];

var reasonform = new Ext.FormPanel({
    id : 'reasonform',
	region : 'center',
	frame:true,
	autoHeight:true,
	layout : 'column',
	border : false,
	bodyStyle : 'padding:5',
    labelAlign : 'left',
    items : [reasonfields]
});
var reasonWindow = new Ext.Window( {
	title: '复查申请',
	width: 700,
       height:500,
       closeAction : 'hide',
       layout: 'border',
       plain:true,
       buttonAlign:'center',
       bodyStyle:'padding:5px;',
       items: [reasonform],
       buttons: [{
           text : '提交申请',
           handler : function() {
        	   data = collegesignupsgrid.getSelectionModel().getSelected().data;
        	   if(data.newscore != null) {
        		   Ext.MessageBox.alert('提示',"已提交过申请，不允许多次申请！");
        	   } else if(data.score == 0) {
        		   Ext.MessageBox.alert('提示',"成绩尚未登出，不允许复查！");
        	   } else {
        		   if(reasonform.getForm().isValid()){
            		   var reason = reasonform.getForm().getValues();
            		   StudentController.reCheck(reason,function(data){ 
            			   if(data){
            				   Ext.MessageBox.alert('提示',"申请成功！");
            			   }else
            				   Ext.MessageBox.alert('提示',"申请失败！");
            		   });        
            	   }else{
            		   Ext.MessageBox.alert('提示',"输入信息有误！");
            	   }
        	   }
           }		
       }]
});
var collegesignupsgrid = new Ext.grid.GridPanel({
	region : 'center',
	id : 'collegesignupsgrid',
	store : collegesignupstore,
	title : '成绩表',
	loadMask : true,
	stripeRows : true,
	autoScroll : true,
	autoExpandColumn : 'examnum',
	viewConfig : {
		sortDescText : '降序',
		sortAscText : '升序',
		columnsText : '显示列',
		forceFit : false
	},
/*	tbar: ['-',
	    {
	    	text : '复查申请',
	    	tooltip : '复查申请',
	    	iconCls : 'import',
	    	onClick : function() {
	    		reasonWindow.show();
	    	}
	    }
	],*/
	columns : [new Ext.grid.RowNumberer(), {
				id : 'examnum',
				header : '准考证号',
				dataIndex : 'examnum',
				width : 200,
				sortable : true
			}, {
				header : '姓名 ',
				dataIndex : 'stuname',
				width : 200,
				sortable : true
			},{
				header : '语种 ',
				dataIndex : 'languagename',
				width : 200,
				sortable : true
			}, {
				header : '成绩',
				dataIndex : 'score',
				width : 100,
				sortable : true
			}/*, {
				header : '复查成绩',
				dataIndex : 'newscore',
				width : 100,
				sortable : true
			}, {
				header : '复查状态',
				dataIndex : 'status'
			}*/
			]
});
function closeTab() {
	var temp = window.top.tabPanel.getActiveTab( );
	window.top.tabPanel.remove(temp);
}

function PageInit() {
	InstitutionController.checkCheckScoreStatus(function(data) {
		var jsonData = Ext.util.JSON.decode(data);
		if(jsonData.checkScoreflag != "On"){
			Ext.MessageBox.alert('提示','当前还不能进行查分！',function() {
				closeTab();
			});
		} else {
			new Ext.Viewport({
				layout : 'border',
				hideMode : Ext.isIE ? 'offsets' : 'display',
				items : [collegesignupsgrid],
				renderTo : Ext.getBody()
			});
			StudentController.getScoreInfo(function(data){
				if(data){
					collegesignupstore.loadData(data);
				}else{
					Ext.MessageBox.alert('提示', "成绩未公布！",function(){
						var temp = window.top.tabPanel.getActiveTab();
						window.top.tabPanel.remove(temp);
						});
				}
			});
		}
	});
}