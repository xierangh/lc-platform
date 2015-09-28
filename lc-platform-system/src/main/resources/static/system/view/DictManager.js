/**
 * 数据词典管理主界面
 */
Ext.define('system.view.DictManager',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		
		 var store = Ext.create('system.store.Dicts', {
	        lazyFill: true
	    });
		
		var dictPanel = Ext.create('Ext.tree.Panel', {
	        width: 600,
	        height: 400,
	        collapsible: true,
	        loadMask: true,
	        useArrows: true,
	        rootVisible: false,
	        store: store,
	        animate: false,
	        plugins: [{
	            ptype: 'bufferedrenderer'
	        }],
	        columns: [{
	            xtype: 'treecolumn', //this is so we know which column will show the tree
	            text: 'codeName',
	            flex: 2.5,
	            sortable: true,
	            dataIndex: 'codeName'
	        },{
	            text: 'numberCode',
	            flex: 1,
	            dataIndex: 'numberCode',
	            sortable: true
	        }]
	    });
		me.items = [dictPanel];
		me.callParent(arguments);
	}	
});



