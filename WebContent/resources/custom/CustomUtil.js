Ext.CustomUtil = {
      // 调用DWR
      invokeDwr: function(dwr, params, dwrOpts){
        if(!dwr){return false;}
        var r = true;
       var dwrObj = {};
       if(dwrOpts){
        dwrObj = Ext.apply(dwrObj, dwrOpts);
       }
       dwrObj.errorHandler = function(text,e){
        r = false;
        Ext.CustomUtil.error(text);
       };
       var p = [];
       if(params){
           if(Ext.isArray(params)){
            Ext.each(params, function(param){p.push(param);});
           }else{
               p.push(params);
           }
       }
       p.push(dwrObj);
       dwr.apply(this, p);
       return r;
      },
      
   success: function(msg, config){
    var c = {
       title: '提示信息',
       msg: msg, 
       icon: Ext.MessageBox.INFO,
       minWidth: 200,
       buttons: {ok: '确定'}
   };
   if(config){
     c = Ext.apply(c,config);
   }
   Ext.MessageBox.show(c);
   },
   
   error: function(msg, config){
    var c = {
      title: '错误信息',
      msg: msg, 
      buttons: Ext.MessageBox.OK,
      icon: Ext.MessageBox.ERROR,
      minWidth: 200,
      buttons: {ok: '确定'}
     };
     
     if(config){
     c = Ext.apply(c,config);
   }
   Ext.MessageBox.show(c);
   },
   
   confirm: function(msg, config){
    var c = {title: '确认信息', minWidth: 200, buttons: {yes: '确认', no: '取消'}, icon: Ext.MessageBox.QUESTION};
    if(config){
     c = Ext.apply(c, config);
    }
    c.msg = msg;
    Ext.MessageBox.show(c);
   },
   
   prompt: function(msg, config){
     var c = {title: '输入信息', minWidth: 300, minHeight: 150};
     if(config){
     c = Ext.apply(c,config);
  }
  c.msg = msg;
    Ext.MessageBox.prompt(c);
   }
};

