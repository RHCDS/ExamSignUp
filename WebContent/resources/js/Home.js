var menuGroup=new Array();
var viewport=null;

function init(){ 
	frameInit();
};
function frameInit(){
	Ext.QuickTips.init();
	viewport = new Ext.Viewport({
        layout: 'border',
        items: [
        new Ext.BoxComponent({
            region: 'north',
            height: 30, // give north and south regions a height
            margin:'0 0 0 0',
            xtype: 'box',
            autoEl: {
                tag: 'div',
                html:'<table border="0" width="98%"> <td align="left" valign="middle"><font size="4px">当前登录用户:'+username+'   所属单位:'+institution+'</font></td><td align="right" valign="middle"><a onclick="window.location.href=\'/ExamSignUp/Logout.do\'" style=\"cursor:pointer;\"><font size="4px">注销</font></a></td></table>'
            }
        }), {
            region: 'west',
            id: 'westPanel', // see Ext.getCmp() below
            title: '功能菜单',
            split: true,
            width: 200,
            minSize: 175,
            maxSize: 400,
            collapsible: true,
            margins: '0 0 0 5',
            cmargins:'0 5 5 5',
            layout:'accordion',
            layoutConfit:{
            	animate:true
            },
            items: buildMenuGroup(menuGroup,menuString)
        },
        new Ext.TabPanel({
        	id:"centerTabPanel",
            region: 'center', // a center region is ALWAYS required for border layout
            deferredRender: false,
            activeTab: 0,     // first tab initially active
            resizeTabs:true,
            layoutOnTabChange:true,
            height:document.body.clientHeight,
            defaults: {autoScroll:true},
            items:[],

            initEvents : function(){  
                Ext.TabPanel.superclass.initEvents.call(this);  
                this.on('add', this.onAdd, this, {target: this});  
                this.on('remove', this.onRemove, this, {target: this});  
                this.mon(this.strip, 'mousedown', this.onStripMouseDown, this);  
                this.mon(this.strip, 'contextmenu', this.onStripContextMenu, this);  
                if(this.enableTabScroll){  
                    this.mon(this.strip, 'mousewheel', this.onWheel, this);  
                }  
                //ADD:monitor title dbclick  
                this.mon(this.strip,'dblclick',this.onTitleDbClick,this);  
            },  
           onTitleDbClick: function(e, target, o) {
               var t = this.findTargets(e);
               if (t.item.fireEvent('beforeclose', t.item) !== false) {
                   t.item.fireEvent('close', t.item);
                   this.remove(t.item);
               }
           }
        })]
    });
	viewport.setHeight(document.body.clientHeight);
	viewport.doLayout();
	Ext.getCmp('westPanel').doLayout();
	Ext.getCmp('centerTabPanel').doLayout();
}
function buildMenuGroup(topMenu,data){
		var menuGroupJSON=Ext.util.JSON.decode(data);
		for(var i=0;i<menuGroupJSON.length;i++){
			var topMenuJSON=menuGroupJSON[i];
			topMenu.push({title:topMenuJSON.name,html:''});
			var childJSON=topMenuJSON.child;
			topMenu[i].html=topMenu[i].html+"<font size=\"3px\"> <div class=\"container\">" +
					"<ul id=\"content\">";
			for(var j=0;j<childJSON.length;j++)
			{
				var temp="<li onclick=\"menuOnclick(\'"+childJSON[j].tabId+
					"\',\'"+childJSON[j].script+"\',\'"+childJSON[j].name+"\')\" onmouseover=\"this.style.backgroundColor='#DFE8F6'\" onmouseout=\"this.style.backgroundColor='#FFFFFF'\" style=\"cursor:pointer;\">"+
					childJSON[j].name+"</li>";
				topMenu[i].html=topMenu[i].html+temp;
			}
			topMenu[i].html=topMenu[i].html+"</ul></div></font>";
		}
		return topMenu;
}
function menuOnclick(tabId,script,tabTitle){
	tabPanel = Ext.getCmp('centerTabPanel');
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
                	var frame = document.getElementById(this.id+'_Frame');
                	if(frame.scr!=null)
                		frame.src = "javascript:false";
                }
	        }
		});
	}
	viewport.doLayout(true,true);
	tabPanel.show();
	tabPanel.setActiveTab(tabId);
};