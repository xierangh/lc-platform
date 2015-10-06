Ext.define('desktop.view.ImageDataView', {
    extend: 'Ext.view.View',
    mixins: {
        dragSelector: 'Ext.ux.DataView.DragSelector'
       ,draggable   : 'Ext.ux.DataView.Draggable'
    },
    cls: 'x-image-view',
    loadMask:false,
    dgroupName : 'desktpMenuDD',
    bindDropTarget:function(){
    	var me = this;
		var wrapElements = Ext.select(me.itemSelector);
		Ext.each(wrapElements.elements, function(el) {
			if('shortcut_-1'!=el.id && 'shortcut_-2'!=el.id){
				var target = me.createDropTarget(el.id);
				me.dropTargetList.add(el.id,target);
			}
		});
		me.desktop.initShortcut();
    },
    dropTargetList : new Ext.util.MixedCollection(),
    createDropTarget : function(id){
    	var me = this;
		return new Ext.dd.DropTarget(id,{
			ddGroup:me.dgroupName,
			notifyDrop:function( source, e, data ){
				var destId = this.id.split('_')[1];//目标对象
				var srcId = data.records[0].getId();//拖动对象
				if(destId==srcId || srcId == '-1' || srcId=='-2'){//自己不能和自己对调
					return this.dropNotAllowed;
				}
        		Ext.ux.Ajax.request({
				    url: contextPath +'/system/menus/swapMenuOrder',
				    async:false,
				    params:{
				    	destId:destId,
				    	srcId:srcId
				    },
				    tips:false,
				    success: function(response,opts,result){
				    	var destRecord = me.getStore().findRecord('menuId',destId);
						var tempMenuOrder = destRecord.get('menuOrder');
		        		destRecord.set("menuOrder",data.records[0].get('menuOrder'));
		        		data.records[0].set("menuOrder",tempMenuOrder);
		        		me.desktop.shortcuts.sort('menuOrder', 'ASC');
		        		me.bindDropTarget();
				    }
				});
				return this.dropAllowed;
			} 
		});
	},
    initComponent: function() {
    	var me = this;
    	me.store.addListener('load',function(s, records, successful, eOpts){
    		me.bindDropTarget();
    	}); 
    	me.mixins.dragSelector.init(me);
    	me.mixins.draggable.init(me, {
            ddConfig: {
                ddGroup: me.dgroupName
            }
        });
    	
    	me.callParent(arguments); 
    }
});