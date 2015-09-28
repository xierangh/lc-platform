Ext.define('desktop.comp.DesktopMenuEdit',{
    extend: 'Ext.panel.Panel',
   	border:false,
   	action:'create',
	initComponent: function() {
		var me = this;
		var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
		var saveButton = Ext.create('Ext.button.Button',{
			text:'保存',iconCls:'icon-update',
			handler:function(button){
				if(menuEditForm.isValid()) {
					var desktopMenu = Ext.create("desktop.module.DesktopMenu");
					var values = menuEditForm.getValues();
					var desktopNumber = me.menuData.get('desktopNumber');
					var url = values.menuId == ""?desktopMenu.proxy.api.create:desktopMenu.proxy.api.update;
					menuEditForm.submit({
						url: url,
						success: function(form, action) {
							Ext.ux.Msg.show(action.result.statusText);
							var data = action.result.data;
							if(me.action=='create'){
								var record = Ext.create('desktop.module.DesktopMenu',data);
								me.desktop.shortcuts.add(record);
								menuEditForm.getForm().reset(true);
							}else if(me.action=='update'){
								if(data.image48!=null){
									me.menuData.set('image48',data.image48);
								}
								me.menuData.set('desktopNumber',data.desktopNumber);
								me.menuData.set('menuName',data.menuName);
								me.menuData.set('menuOrder',data.menuOrder);
								me.menuData.set('menuVal',data.menuVal);
								me.menuData.set('permCode',data.permCode);
								me.menuData.set('menuType',data.menuType);
								me.menuData.set('showWidth',data.showWidth);
								me.menuData.set('showHeight',data.showHeight);
								me.menuData.set('bz',data.bz);
								if(desktopNumber!=data.desktopNumber){
									me.desktop.shortcuts.suspendEvents();
									me.desktop.shortcuts.clearFilter();
									me.desktop.shortcuts.resumeEvents();
									me.desktop.shortcuts.filter([{
							            fn: function(record) {
							                return record.get('desktopNumber') == desktopNumber || record.get('menuType')=='add';
							            }
							        }]);
								}
							}
							me.desktop.shortcuts.sort('menuOrder', 'ASC');
							me.desktop.shortcutsView.bindDropTarget();
						}
					});
				}
			}
		});
		
		var menuNameField = Ext.create('Ext.form.field.Text',{
			columnWidth: 0.45,
			allowBlank: false,
			name:'menuName',
			afterLabelTextTpl: required,
			fieldLabel: '菜单名字'
		});
		
		
		
		var permCodeField = Ext.create('Ext.form.field.Text',{
			width:278,
			name:'permCode'
		});
		
		var permSelectCallback = function(records){
				permCodeField.setValue(records[0].get('permCode'));
		};
		
		var fieldcontainer = Ext.create('Ext.form.FieldContainer',{
			columnWidth: 0.95,
            fieldLabel: '权限代码',
            layout:'hbox',
            margin:'0 0 5 0',
            isValid:function(){
            	return true;
            },
            items: [permCodeField,{
                xtype: 'button',
                text:'选择权限',
                margin:'0 0 0 5',
                handler:function(){
                	var systemPermSelectIndex = Ext.create('permission.view.SystemPermSelectIndex');
    				var win = Ext.create('Ext.ux.window.Window',{
    					title:'权限基本信息',
    					width: 700,
    					minHeight:400,
    					modal:true,
    					items:[systemPermSelectIndex]
    				});
    				systemPermSelectIndex.parent = win;
    				systemPermSelectIndex.callback = permSelectCallback;
    				win.show();
                }
            }]
		});
		
		
		var menuEditForm = Ext.create('Ext.ux.form.Panel',{
			autoScroll:true,
			submitBtn:saveButton,
			bodyStyle:'border-top: 1px solid #c5c5c5 !important;',
			bodyPadding: 10,
			border:false,
			fieldDefaults: {
				msgTarget: 'side',
				labelWidth: 62
			},
			padding:'0 0 0 0',
			layout:'column',
			defaultType: 'textfield',
			items: [
			      {
			    	  xtype:'hiddenfield',
			    	  name:'menuId'
			      }, 
			      menuNameField,{
					columnWidth: 0.5,
					name : 'desktopNumber',
					margin:'0 0 5 10',
					value:me.desktop.desktopNumber,
					fieldLabel: '&nbsp;&nbsp;桌面序号',
					xtype:'numberfield',
					minValue: 1,
					maxValue:5
				},{
					columnWidth: 0.45,
					fieldLabel: '菜单类型',
					name : 'menuType',
					displayField: 'name',
					valueField: 'value',
					store: Ext.create('Ext.data.Store', {
						fields: ['value', 'name'],
						data : [
							{ value: 'viewClass', name: 'class' },
							{ value:'url',name:'url'}
						]
					}),
					editable:false,
					value: "viewClass",
					xtype:'combo'
				},{
					columnWidth: 0.5,
					name:'menuVal',
					margin:'0 0 5 10',
					fieldLabel:'&nbsp;&nbsp;&nbsp;菜&nbsp;单&nbsp;值'
				},{
					columnWidth: 0.45,
					xtype:'numberfield',
					fieldLabel:'序　　号',
					name:'menuOrder',
					allowBlank: false,
					minValue: 1,
					maxValue:120
				},{
					columnWidth: 0.5,
					name:'menuImage48x84',
					margin:'0 0 5 10',
					xtype: "filefield",
					buttonText: '选择图标',
					fieldLabel:'图标48x48',
					vtype:'image'
				},{
					columnWidth: 0.45,
					xtype:'numberfield',
					fieldLabel:'显示宽度',
					name:'showWidth',
					allowBlank: false,
					value:800,
					minValue: 800,
					maxValue:1900
				},{
					columnWidth: 0.5,
					name:'showHeight',
					margin:'0 0 5 10',
					xtype: "numberfield",
					fieldLabel:'显示高度',
					allowBlank: false,
					value:600,
					minValue: 600,
					maxValue:1000
				},fieldcontainer,{
					columnWidth: 0.95,
					fieldLabel:'备　　注',
					xtype:'textarea',
					name:'bz'
				}
			],
			dockedItems: [{
			    xtype: 'toolbar',
			    items: [
					saveButton,{
						text: '重置',iconCls:'icon-reset',
						handler:function(button){
							if(me.action=='update'){
								menuEditForm.loadRecord(me.menuData);
							}else{
								menuEditForm.getForm().reset(true);
							}
						}
					}
			    ]
			}]
		 });
		
		if(me.action=='update'){
			menuEditForm.loadRecord(me.menuData);
		}
		
		me.items = [menuEditForm];
		
		me.callParent(arguments);
	}
});