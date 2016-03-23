Ext.data.DWRProxy = function(dwrinvoke){
  Ext.data.DWRProxy.superclass.constructor.call(this);
  this.dwr = dwrinvoke;
}

Ext.extend(Ext.data.DWRProxy, Ext.data.DataProxy, {
  load : function(params, reader, callback, scope, arg){
    params : params || {};
     if(this.fireEvent("beforeload", this, params) !== false)
        {
            var proxy=this;
            //dwr回调函数
             var dwrcallback = function(res){
                var result;
                try 
                {                
                    result = reader.readRecords(res);
                }catch(e)
                {
                    this.fireEvent("loadexception", this, arg, null, e);
                    callback.call(scope, null, arg, false);
                    return;
                }
                callback.call(scope, result, arg, true);
            }
            //需要将参数加入到调用参数
            var callParams = [];
            for(var i=0; i<params.length;++i)
            {
                callParams.push(params[i]);
            }
            //将回调函数加入的参数数组
            callParams.push(dwrcallback);
              this.dwr.apply(this, callParams);
        }   
  }
});

/**
* Ext.data.DWRStore实现对DWR调用后返回的记录进行存储
*    当调用的DWR方法返回的是JSON对象时，使用此存储类进行管理
*/
Ext.data.DWRStore = function(c){
    Ext.data.DWRStore.superclass.constructor.call(this, Ext.apply(c, {
        proxy: c.dwr ? new Ext.data.DWRProxy(c.dwr): undefined,
        reader: c.reader ? c.reader : new Ext.data.JsonReader(c, c.fields)
    }));
};
Ext.extend(Ext.data.DWRStore, Ext.data.Store);

