Ext.define('system.view.UserList',{
    extend: 'Ext.grid.Panel',
	requires: [
	'system.store.Users',
	'Ext.selection.CheckboxModel'
	],
	width:'100%',
    viewConfig: {
	   forceFit:true
    },
    verticalScroller:{
        variableRowHeight: true
    },
	initComponent: function() {
		var me = this;
		me.collection=new Ext.util.MixedCollection();
		me.store = Ext.create('system.store.Users',{
			listeners:{
				beforeload:function( store, operation, eOpts ){
					if(me.getExtraParams){
						store.proxy.extraParams = me.getExtraParams();
					}
					var sorters = me.store.sorters.items;
					for(var i = 0;i<sorters.length;i++){
						var sorter = sorters[i];
						if(sorter.property=="accountLockedStr"){
							sorter.property="accountLocked";
						}
						if(sorter.property=="delStatusStr"){
							sorter.property="delStatus";
						}
					}
				}
			}
		});
		var renderer = function(value,metaData,record){
			var superAdmin = record.get('superAdmin');
			var delStatus = record.get('delStatus');
			if(delStatus){
				return '<span style="color:#C0C0C0;" >' + value + '</span>';
			}
			if(superAdmin){
				return '<span style="color:red;">' + value + '</span>';
			}
			if(metaData.cellIndex==6){
				metaData.tdAttr = 'data-qtip="'+value+'"';
			}
			if(metaData.cellIndex==5){
				metaData.tdAttr = 'data-qtip="'+value+'"';
			}
			return value;
		};
		me.columns= [
			{xtype: 'rownumberer',width:40,header:'序号'},
			{header: 'ID',  dataIndex: 'userId',align:'center',flex: 1,hidden:true,renderer:renderer},
			{header: '登录名',  dataIndex: 'username',align:'center',  width: 80,renderer:renderer},
			{header: '姓名',  dataIndex: 'xm',align:'center',  flex: 1,renderer:renderer},
			{header: '电子邮箱',  dataIndex: 'email',align:'center',  width:150,renderer:renderer},
			{header: '部门信息',  dataIndex: 'deptInfo',align:'center',  flex: 1,renderer:renderer},
			{header: '角色信息',  dataIndex: 'roleInfo',align:'center',  flex: 1,renderer:renderer},
			{header: '是否锁定',  dataIndex: 'accountLockedStr',align:'center',  width:80,renderer:renderer},
			{header: '是否删除',  dataIndex: 'delStatusStr',align:'center',  width:80,renderer:renderer}       
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