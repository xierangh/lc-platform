Ext.define('system.view.RoleSelectIndex',{
    extend: 'Ext.ux.panel.Panel',
   	border:false,
	initComponent: function() {
		var me = this;
		var getExtraParams = function(){
			var extraParams = {};
			var val = searchCombobox.getValue()==null?"":searchCombobox.getValue();
			var rules = [];
			rules.push({field:'roleName',data:val,op:'cn'});
			extraParams.filters = Ext.JSON.encode({rules:rules});
			return extraParams;
		};
		
		var list = Ext.create('system.view.RoleList',{
			height:320,
			margin:"0 0 0 1",
			getExtraParams:getExtraParams
		});
		
		var searchCombobox = Ext.create('Ext.ux.form.field.SearchText',{
			store: list.store,
			emptyText: '角色名,多个逗号分隔'
		});
		
		var searchTool = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			items: [
				{
					xtype:'displayfield',
					value:'关键字:'
				},
				searchCombobox,
				{
					text: '搜索',iconCls:'icon-search',
					handler:function(button){
						list.store.loadPage(1);
					}
				},
				{
					text: '重置',iconCls:'icon-reset',
					handler:function(button){
						searchCombobox.setValue("");
						list.collection.clear();
						list.store.loadPage(1);
					}
				},{
					 text: '确定',
					 xtype: 'button',
					 iconCls:'icon-ok',
					 handler: function(button,e){
						var records = list.getSelectionModel().getSelection();
						if(me.selectedHandler){
							me.selectedHandler(records);
						}
						if(me.parent){
							me.parent.close();
						}
					 }
				 }
			]
		});	
		me.items = [searchTool,list];
		me.callParent(arguments);
	}
});