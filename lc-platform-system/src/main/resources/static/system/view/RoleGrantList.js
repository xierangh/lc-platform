Ext.define('system.view.RoleGrantList',{
    extend: 'Ext.grid.Panel',
	requires: [
	'system.store.Roles',
	'Ext.selection.CheckboxModel'
	],
    viewConfig: {
	   forceFit:true
    },
    verticalScroller:{
        variableRowHeight: true
    },
	initComponent: function() {
		var me = this;
		me.store = Ext.create('system.store.Roles',{
			params:{
				userIds:me.userIds
			}
		});
		
		me.collection=new Ext.util.MixedCollection();
		
		me.columns=[
	        {header: 'ID',  dataIndex: 'id',align:'center',flex: 1,hidden:true},
	        {header: '角色名字',  dataIndex: 'roleName', align:'center', flex:1},
	        {xtype: 'checkcolumn',header: '授予', dataIndex: 'grant',width:90,
	        	listeners:{
	        		checkchange:function(checkColumn, rowIndex, checked, eOpts ){
	        			if(checked){
	        				var record = me.store.getAt(rowIndex);
	        				me.collection.add(rowIndex,record.getId());
	        			}else{
	        				me.collection.removeAtKey(rowIndex);
	        			}
	        			if(me.checkchange){
	        				me.checkchange(checkColumn, rowIndex, checked, eOpts);
	        			}
	        		}
	        	}
	        }
        ];
		me.callParent(arguments);
	}
});