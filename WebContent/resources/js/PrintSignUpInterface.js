var xxmc="";

//var comboLanguageStore = new Ext.data.ArrayStore({
//	fields:[
//	        'languagenum',
//	        'name'
//	        ]
//});

var comboLanguageStore = new Ext.data.JsonStore({
	fields:[
	        'languagenum',
	        'languagename'
	        ]
});

var printForm=new Ext.form.FormPanel({
	   title:"选择打印条件",
	   region:"center",
	   frame:true,
	   width:300,
	   height:60,
	   labelWidth:40,
	   labelAlign:'right',
	   items:[{		
    	     xtype:'combo',
    	     fieldLabel:'语种',
    	     id:'language',
    	     name:'language',
    	     triggerAction:'all',
    	     store:comboLanguageStore,	
    	     displayField:'languagename',
    //	     displayField:'name',
    	     valueField:'languagename',
    	     mode:'local',
    	     editable:false,
    	     allowBlank: false,
    	     emptyText:'---请选择---'
    	    }],			      	                  
		   tbar:[ {
				text : '报名册封面设计',
				iconCls : 'edit',
				onClick : function() {
					//先检查表单数据是不是合法
					if(printForm.getForm().isValid()){					
						    var yz=Ext.getCmp("language").getValue();//得到语种
						    var myDate=new Date();
						    var year=myDate.getFullYear();//得到完整的年份
						    var month=myDate.getMonth();//获得当前月份（0---11,0代表1月）
						    var upYearOrNor="春季";
							if(month>=6)upYearOrNor="秋季";//判断是上半年还是下半年	
						    var mydate=year+"年"+upYearOrNor;
						    
						    var InfoArray=new Array("浙江省高校计算机等级考试\n",
						   		    xxmc,
									yz,
									"报\n\n\n名\n\n\n册",
									xxmc+"教务处",
									mydate);
						    						    									    
						    CreatePrintPage(InfoArray);
							//LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
							LODOP.PRINT_SETUP();
							reSizePrintParam();
		        	}else{
		        		Ext.MessageBox.alert('提示',"输入打印条件有误！");
		        	}
					
				}
			},'-',{
				text : '打印报名册封面',
				iconCls : 'print',
				onClick : function() {			
					//先检查表单数据是不是合法
					if(printForm.getForm().isValid()){					
						    var yz=Ext.getCmp("language").getValue();//得到语种
						    
						    var myDate=new Date();
						    var year=myDate.getFullYear();//得到完整的年份
						    var month=myDate.getMonth();//获得当前月份（0---11,0代表1月）
						    var upYearOrNor="春季";
							if(month>=6)upYearOrNor="秋季";//判断是上半年还是下半年	
						    var mydate=year+"年"+upYearOrNor;
						    
						    var InfoArray=new Array("浙江省高校计算机等级考试\n",
						   		    xxmc,
									yz,
									"报\n\n\n名\n\n\n册",
									xxmc+"教务处",
									mydate);
						    						    									    
						    CreatePrintPage(InfoArray);
							LODOP.SET_PREVIEW_WINDOW(0,0,0,0,0,"");
							LODOP.PREVIEW();																	
		        	}else{
		        		Ext.MessageBox.alert('提示',"输入打印条件有误！");
		        	}
					
					
				}
			},'-',{
				text : '选择默认打印机',
				 iconCls : 'print',
			        scope: this,
			        handler:function(){
			    			var returnPrinter=LODOP.SELECT_PRINTER();
			    			if(returnPrinter<0){
			    				return;
			    			}else{
			    				//currentPrinter=returnPrinter;
			    				LODOP.SET_PRINTER_INDEX(returnPrinter);
			    			}
			        }
			}
	    ]
});

function Init(){	
	
	   //得到语种combobox的列表
	  	StudentController.getLanguageList_without_languagenum(function(data){
			if(data){
				comboLanguageStore.loadData(data);
			}
	  	});
	  	//得到学校名称
	  	StudentController.getSchoolName(function(data){
			if(data){				
				xxmc=data;			
			}
	  	});  	  	
}



function printSignUpInterfacePageInit() {
	new Ext.Viewport( {
		layout :'border',
		hideMode: Ext.isIE ? 'offsets' : 'display',
		items : [printForm],
		renderTo :Ext.getBody()
	});
	Init();
}