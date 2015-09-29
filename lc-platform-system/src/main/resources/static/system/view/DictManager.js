/**
 * 数据词典管理主界面
 */
Ext.define('system.view.DictManager',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		
		 var store = Ext.create('system.store.Dicts');
		
		var dictPanel = Ext.create('Ext.tree.Panel', {
	        flex:1,
	        loadMask: true,
	        useArrows: true,
	        rootVisible: false,
	        store: store,
	        animate: false,
	        plugins: [{
	            ptype: 'bufferedrenderer'
	        }],
	        columns: [{
	            xtype: 'treecolumn',
	            text: '字典名称',
	            flex: 2,
	            dataIndex: 'codeName'
	        },{
	            text: '字典值',
	            flex: 1,
	            dataIndex: 'numberCode'
	        },{
	            text: '字典排序',
	            width:80,
	            dataIndex: 'dictOrder'
	        }]
	    });
		
/////////////////////////表单信息////////////////////////////
		var idField = Ext.create('Ext.form.field.Hidden',{
			xtype:'hiddenfield',
			name:'id'
		});
		
		var parentIdField = Ext.create('Ext.form.field.Hidden',{
			name:'parentId',
			value:'0'
		});
		
		var parentNameField = Ext.create('Ext.form.field.Display',{
			name:'parentName',
			xtype : "displayfield",
			fieldLabel: '上级字典',
			value:'无'
		});
		
		var codeNameField = Ext.create('Ext.form.field.Text',{
			allowBlank: false,
			name:'codeName',
			fieldLabel: '字典名称'
		});
		
		var numberCodeField = Ext.create('Ext.form.field.Text',{
			name:'numberCode',
			fieldLabel: '字典代码'
		});
		
		var dictOrderField = Ext.create('Ext.form.field.Number',{
			name:'dictOrder',
			fieldLabel: '字典排序',
			value:'0'
		});
		
		var dictDescField = Ext.create('Ext.form.field.TextArea',{
			 fieldLabel: '字典描述',
			 name:'dictDesc'
		});
		
		var initDictBtn = Ext.create("Ext.button.Button",{
			text:"重置字典",
			handler:function(){
				
			}
		});
		
		var addDictBtn = Ext.create("Ext.button.Button",{
			text:"添加字典",
			handler:function(){
				
			}
		});
		
		var delDictBtn = Ext.create("Ext.button.Button",{
			text:"删除字典",
			handler:function(){
				
			}
		});
		
		var editForm = Ext.create('Ext.ux.form.Panel',{
			width: 350,
			border:false,
			layout: 'anchor',
			bodyPadding: 10,
			fieldDefaults: {
				labelWidth: 60,
				anchor: '100%'
			},
			buttons:[initDictBtn,delDictBtn,addDictBtn,{
				text:"保存信息",
				submit:true,
				handler:function(){
					if(editForm.isValid()){
						var item = editForm.getValues();
					}
				}
			}],
			defaultType: 'textfield',
			items: [idField,parentIdField,
			        parentNameField,codeNameField,
			        numberCodeField,dictOrderField,dictDescField
			]
		 });
		
		me.items = [dictPanel,editForm];
		
		me.listeners = {
			resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
				dictPanel.setHeight(height);
			}
		};
		
		me.callParent(arguments);
	}	
});



