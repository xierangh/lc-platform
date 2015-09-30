/**
 * 数据词典管理主界面
 */
Ext.define('system.view.DictManager',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		
		var store = Ext.create('system.store.Dicts');
		var currNode;
		var parentNode;
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
	        }],
	        listeners:{
	        	itemclick:function( view, record, item, index, e, eOpts ){
	        		currNode = record;
	        		parentNode = currNode.parentNode;
	        		currNode.expand();
	        		addDictBtn.enable();
	        		if(record.get("parentId")=='0'){
	        			resetDictBtn.enable();
	        			saveDictBtn.disable();
	        		}else{
	        			resetDictBtn.disable();
	        			saveDictBtn.enable();
	        		}
	        		if(record.get("codeType")==2){
	        			delDictBtn.enable();
	        		}else{
	        			delDictBtn.disable();
	        		}
	        		var parentName = record.parentNode.get("codeName");
	        		record.set("parentName",parentName);
	        		editForm.loadRecord(record);
	        	}
	        }
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
		
		var defaultValField = Ext.create('Ext.form.field.Checkbox',{
			name:'defaultVal',
			boxLabel  : '默认值',
			inputValue:'true',
			xtype:'checkbox'
		});
		
		var editForm = Ext.create('Ext.ux.form.Panel',{
			border:false,
			width:370,
			layout: 'anchor',
			bodyPadding: 10,
			fieldDefaults: {
				labelWidth: 60,
				anchor: '100%'
			},
			defaultType: 'textfield',
			items: [idField,parentIdField,
			        parentNameField,codeNameField,
			        numberCodeField,dictOrderField,dictDescField,
			        defaultValField
			]
		 });
		
		var addDictBtn = Ext.create("Ext.button.Button",{
			text:"添加字典",
			iconCls:'icon-add',
			disabled:true,
			handler:function(){
				editForm.getForm().reset(true);
				parentIdField.setValue(currNode.getId());
				parentNameField.setValue(currNode.get("codeName"));
				codeNameField.focus();
				saveDictBtn.enable();
			}
		});
		
		var delDictBtn = Ext.create("Ext.button.Button",{
			text:"删除字典",
			disabled:true,
			iconCls:'icon-remove',
			handler:function(){
				Ext.ux.Ajax.request({
				    url:contextPath+'/system/dicts/delete',
				    params: {
				    	id:currNode.getId()
				    },
				    success: function(response,opt,result){
				    	editForm.getForm().reset(true);
				    	saveDictBtn.disable();
				    	delDictBtn.disable();
				    	addDictBtn.disable();
				    	store.reload({node:parentNode});
				    }
				});
			}
		});
		
		var resetDictBtn = Ext.create("Ext.button.Button",{
			text: '重置字典',iconCls:'icon-reset',disabled:true,
			handler:function(button){
				
			}
		});
		
		var saveDictBtn = Ext.create("Ext.button.Button",{
			text: '保存字典',iconCls:'icon-save',disabled:true,
			handler:function(button){
				if(editForm.isValid()){
					var item = editForm.getValues();
					Ext.ux.Ajax.request({
					    url:contextPath+'/system/dicts/create',
					    params: {
					    	id:item.id,
					    	codeName:item.codeName,
					    	numberCode:item.numberCode,
					    	dictOrder:item.dictOrder,
					    	dictDesc:item.dictDesc,
					    	defaultVal:defaultValField.checked,
					    	parentId:item.parentId
					    },
					    success: function(response,opt,result){
					    	idField.setValue(result.data);
					    	if(item.id==""){
					    		store.reload({node:currNode});
					    	}else{
					    		store.reload({node:parentNode});
					    	}
					    }
					});
				}
			}
		});
		
		var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			margin:"5 0 0 0",
			items: [
				resetDictBtn,delDictBtn,addDictBtn,
				saveDictBtn
			]
		});
		
		var editPanel = Ext.create("Ext.panel.Panel",{
			border:false,
			items:[editForm,actionBar]
		});
		
		me.items = [dictPanel,editPanel];
		
		me.listeners = {
			resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
				dictPanel.setHeight(height);
			}
		};
		
		me.callParent(arguments);
	}	
});



