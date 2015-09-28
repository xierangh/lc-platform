Ext.define('system.view.RoleList',{
    extend: 'Ext.grid.Panel',
	requires: [
	'system.store.Roles',
	'Ext.selection.CheckboxModel'
	],
	xtype: 'cell-editing',
	width:'100%',
    viewConfig: {
	   forceFit:true
    },
    verticalScroller:{
        variableRowHeight: true
    },
	initComponent: function() {
		var me = this;
		me.cellEditing = new Ext.grid.plugin.CellEditing({
            clicksToEdit: 2
        });
		me.plugins=[me.cellEditing];
		me.collection=new Ext.util.MixedCollection();
		me.store = Ext.create('system.store.Roles',{
			listeners:{
				beforeload:function( store, operation, eOpts ){
					if(me.getExtraParams){
						store.proxy.extraParams = me.getExtraParams();
					}
				}
			}
		});
		
		me.columns=[
	     	{xtype: 'rownumberer',width:40,header:'序号'},
	        {header: 'ID',  dataIndex: 'id',align:'center',flex: 1,hidden:true},
	        {header: '角色名字',  dataIndex: 'roleName', align:'center', width:110,
	        	editor: {
                    xtype: 'textfield',
                    allowBlank: false
                }
	        },
	        {header: '角色描述',  dataIndex: 'roleDesc',align:'center',  flex: 1,
	        	editor: {
                    xtype: 'textfield'
                }
	        }
	      ];
		
		var pageSizePager = Ext.create('Ext.ux.PageSizePager');
		me.bbar = Ext.create('Ext.ux.toolbar.Paging',{
			dock : 'bottom',
			displayInfo: true,
			list:me,
			plugins:[pageSizePager]
		});
		me.selModel = Ext.create('Ext.ux.selection.CheckboxModel',{collection:me.collection});
		me.callParent(arguments);
	}
});