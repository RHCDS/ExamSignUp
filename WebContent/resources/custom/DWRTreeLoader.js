
/*
 * @class Ext.tree.DWRTreeLoader
 * @createDate:2008-02-27 
 * @extends Ext.tree.DWRTreeLoader 
 */
 
Ext.tree.DWRTreeLoader = function(config) {
  Ext.tree.DWRTreeLoader.superclass.constructor.call(this, config);
};
// 进行扩展
Ext.extend(Ext.tree.DWRTreeLoader, Ext.tree.TreeLoader, {
   requestData : function(node, callback) {
    if (this.fireEvent("beforeload", this, node, callback) !== false) {

      //todo
      var callParams = this.params;
      //alert(this.params);
      //var callParams = new Array();
      var success = this.handleResponse.createDelegate(this, [node, callback], 1);
      var error = this.handleFailure.createDelegate(this, [node, callback], 1);
      //callParams.push(node.id);
      //callParams.push(node.attributes.classtype);
      //callParams.push({callback:success, errorHandler:error});
	  callParams.push({callback:success, errorHandler:error});
      //todo: do we need to set this to something else?
      this.transId=true;
      this.dataUrl.apply(this, callParams);
    } else {
      // if the load is cancelled, make sure we notify
      // the node that we are done
      if (typeof callback == "function") {
        //alert(callback);
        callback();
      }
    }
  },
    processResponse : function(response, node, callback){
    	//alert(Ext.util.JSON.encode((response)));
        try {
          for(var i = 0; i < response.length; i++){
                var n = this.createNode(response[i]);
                if(n){
                    node.appendChild(n);
                }
            }
            if(typeof callback == "function"){
                callback(this, node);
            }
        }catch(e){
            this.handleFailure(response);
        }
    },

    handleResponse : function(response, node, callback){
        this.transId = false;
        this.processResponse(response, node, callback);
        this.fireEvent("load", this, node, response);
    },

    handleFailure : function(response, node, callback){
        this.transId = false;
        this.fireEvent("loadexception", this, node, response);
        if(typeof callback == "function"){
            callback(this, node);
        }
    }
});
//扩展结束