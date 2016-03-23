Ext.grid.CustomGridPanel = function(c){
 Ext.grid.CustomGridPanel.superclass.constructor.call(this, c);
};

Ext.extend(Ext.grid.CustomGridPanel, Ext.grid.GridPanel, {
 // 选择下一行记录,如果没有下一行记录则选择上一行记录,会刷新表格
   selectNext: function(){
     var grid_ = this;
     var selModel = grid_.getSelectionModel();
     var nextSel = null;
     if(selModel.hasNext()){
      selModel.selectNext();
     }else if(selModel.hasPrevious()){
      selModel.selectPrevious();
     }else{
      return null;
     }
     nextSel = selModel.getSelected();
     grid_.getView().refresh();
     return nextSel;
//    if(nextSel){selModel.selectRecords([nextSel]);}
   },
   refreshGrid:function(record){
	        var grid_ = this;
			grid_.getView().refresh();
			grid_.getSelectionModel().selectRecords([record]);
   },
  // 根据已经选择的排序信息,插入一条记录,并选择插入的记录
   addSorted: function(record,dwr,dwrOpts,func){
     var grid_ = this;
     var result = grid_.invokeDwr(record,null,dwr,dwrOpts,func);
     if(result){
     grid_.getStore().addSorted(record);
		if(!func)
		{
			grid_.refreshGrid();
		}
    }
    return result;
   },
  // 根据已经选择的排序信息,插入一条记录,并选择插入的记录
   addSubSorted: function(record,parentID,dwr,dwrOpts,func){
     var grid_ = this;
     var result = grid_.invokeDwr(record,parentID,dwr,dwrOpts,func);
     if(result){
		grid_.getStore().addSorted(record);
		if(!func)
		{
			grid_.refreshGrid();
		}
    }
    return result;
   },
   // 在最后面插入一条记录,并选择插入的记录
   insertLast: function(record,dwr,dwrOpts,func){
		var grid = this;
		var index = grid.getStore().getCount();
		return grid.insert(index,record,dwr,dwrOpts,func);
   },
   // 在给定索引上插入一条记录,并选择插入的记录
   insert: function(index,record,dwr,dwrOpts,func){
     var grid_ = this;
     var result = grid_.invokeDwr(record,null,dwr,dwrOpts,func);
     if(result){
	    grid_.getStore().insert(index, record);
		if(!func)
		{
			grid_.refreshGrid();
		}
	  }
	  return result;
   },
   
   invokeDwr: function(record, plusobj, dwr, dwrOpts, func){
     var flag = true;
    if(dwr){
        var dwrObj = {async: false};
        if(dwrOpts){
         dwrObj = Ext.apply(dwrObj, dwrOpts);
        }
		if (func)
		{
			dwrObj = func;
		}
        dwrObj.errorHandler = function(text,e){
          flag = false;
          Ext.CustomUtil.error(text);
        };
//     dwr.apply(this, [record.data, dwrObj]);
	if (!plusobj)
	{
		dwr(record.data, dwrObj);
	}
	else
	{
		dwr(record.data, plusobj, dwrObj);
	}
     return flag;
    } else {
     return false;
    }
   },
   // 删除一条记录,选择下一条可以删除的记录
   removeRecord: function(record, dwr, dwrOpts,func){
    var grid = this;
    var err = false;
    err = ! grid.invokeDwr(record, null, dwr, dwrOpts,func);
    if(err){return;}
    var next = grid.selectNext();
    grid.getStore().remove(record);
    if(next){grid.getSelectionModel().selectRecords([next]);}
   },
   // 获取选择的单一记录
   getSelected: function(){
    return this.getSelectionModel().getSelected();
   }
});

