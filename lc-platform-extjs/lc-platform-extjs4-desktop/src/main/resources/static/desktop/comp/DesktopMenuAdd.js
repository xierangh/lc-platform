Ext.define('desktop.comp.DesktopMenuAdd',{
    extend: 'Ext.panel.Panel',
   	border:false,
	initComponent: function() {
		var me = this;
		
		var menuNameField = Ext.create('Ext.form.field.Text',{
			allowBlank: false,
			name:'menuName',
			fieldLabel: '应用名称',
			columnWidth: 0.5
		});
		
		var menuValField = Ext.create('Ext.form.field.Text',{
			name:'menuVal',
			fieldLabel: '应用地址',
			columnWidth: 1
		});
		
		var showWidthField = Ext.create('Ext.form.field.Number',{
			fieldLabel:'显示宽度',
			name:'showWidth',
			allowBlank: false,
			value:800,
			minValue: 800,
			maxValue:1900,
			columnWidth: 0.5
		});
		
		var showHeightField = Ext.create('Ext.form.field.Number',{
			fieldLabel:'显示高度',
			name:'showHeight',
			allowBlank: false,
			value:600,
			minValue: 600,
			maxValue:1000,
			columnWidth: 0.5
		});
		var menuImage48x84 = Ext.create('Ext.form.field.File',{
			name:'menuImage48x84',
			fieldLabel:'图标48x48',
			vtype:'image',
			columnWidth: 1,
			buttonText: '选择图标'
		});
		
		var menuTypeField = Ext.create('Ext.form.field.Hidden',{
			xtype:'hiddenfield',
			name:'menuType',
			value:'url'
		});
		
		var parentIdField = Ext.create('Ext.form.field.Hidden',{
			xtype:'hiddenfield',
			name:'parentId',
			value:me.menuData.get("parentId")
		});
		
		var desktopNumberField = Ext.create('Ext.form.field.Hidden',{
			xtype:'hiddenfield',
			name:'desktopNumber',
			value:me.menuData.get("desktopNumber")
		});
		
		var editForm = Ext.create('Ext.ux.form.Panel',{
			width: 450,
			border:false,
			layout:'column',
			bodyPadding: 10,
			fieldDefaults: {
				labelWidth: 65,
				margin:'5 0 0 0'
			},
			defaultType: 'textfield',
			items: [desktopNumberField,parentIdField,menuTypeField,menuNameField,menuValField,showWidthField,
			        showHeightField,menuImage48x84,{
						fieldLabel:'备　　注',
						xtype:'textarea',
						name:'bz',
						columnWidth: 1
					}]
		 });
		
		var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			width: 450,
			margin:"5 0 0 0",
			items: [
			        "->",
				{
					text:"重置表单",
					iconCls:'icon-reset',
					handler:function(){
						editForm.getForm().reset(true);
					}
				},{
					text:"保存信息",
					iconCls:'icon-save',
					handler:function(){
						if(editForm.isValid()) {
							editForm.submit({
								url:contextPath + "/system/menus/create",
								success: function(form, action) {
									var record = Ext.create("system.model.Menu",editForm.getValues());
									record.setId(action.result.data);
									me.desktop.shortcuts.add(record);
									me.desktop.initShortcut();
								}
							});
						}
					}
				}
			]
		});
		
		me.items = [editForm,actionBar];
		
		me.showCallback = function(){
			menuNameField.focus();
		}
		
		me.callParent(arguments);
	}
});