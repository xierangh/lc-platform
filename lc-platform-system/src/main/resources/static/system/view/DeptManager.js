/**
 * 部门管理主界面
 */
Ext.define('system.view.DeptManager',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		var store = Ext.create("system.store.Depts");
		var treeid = Ext.data.IdGenerator.get('uuid').generate();
		
		function onClick(e, treeId, treeNode) {
			addDeptBtn.enable();
			delDeptBtn.enable();
			var zTree = $.fn.zTree.getZTreeObj(treeid);
			var nodes = zTree.getSelectedNodes();
			var node = nodes[0];
			var parentNode = node.getParentNode();
			var record = Ext.create("system.model.Dept",node.data);
			if(parentNode==null){
				record.set("parentName","无");
			}else{
				record.set("parentName",parentNode.name);
			}
			store.add(record);
			editForm.loadRecord(record);
		}
		
		function zTreeOnDrop(event, treeId, treeNodes, targetNode, moveType) {
			if(moveType!='inner')return;
			var targetDeptId = targetNode ? targetNode.id : "0";
			var deptIds = [];
			for(var i = 0;i<treeNodes.length;i++){
				deptIds.push(treeNodes[i].id);
			}
			Ext.ux.Ajax.request({
			    url:contextPath+'/system/depts/move',
			    params: {deptIds:deptIds.join(","),targetDeptId:targetDeptId},
			    success: function(response,opt,result){
			    	Ext.ux.Msg.show(result.statusText);
			        editForm.getForm().reset(true);
			    }
			});
		};
		
		var initDeptTreeView = function(){
			var setting = {
    				data: {
    					simpleData: {
    						enable: true
    					}
    				},
    				callback: {
    					onClick: onClick,
    					onDrop: zTreeOnDrop
    				},
    				edit: {
    					enable: true,
    					showRemoveBtn: false,
    					showRenameBtn: false,
    					drag: {
    						prev: false,
    						next: false
    					}
    				}
    		};
    		var zNodes;
    		Ext.ux.Ajax.request({
			    url:contextPath+'/system/depts/tree',
			    async:false,
			    success: function(response){
			    	zNodes = Ext.JSON.decode(response.responseText);
			    }
			});
    		$.fn.zTree.init($("#"+treeid), setting, zNodes);
		}
		
		var deptTreePanel = Ext.create('Ext.panel.Panel', {
			width: 230,
			bodyStyle: {
				'overflow': 'auto',
				 'border-color': '#c5c5c5 !important;'
	        },
		    html:  '<ul id="'+treeid+'" class="ztree" style="margin-top:0;"></ul>',
		    listeners: {
		    	afterrender: function(th){
		    		initDeptTreeView();
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
			fieldLabel: '上级部门',
			value:'无'
		});
		
		var deptNameField = Ext.create('Ext.form.field.Text',{
			allowBlank: false,
			name:'deptName',
			fieldLabel: '部门名称'
		});
		
		var deptCodeField = Ext.create('Ext.form.field.Text',{
			name:'deptCode',
			fieldLabel: '部门编号'
		});
		
		var deptOrderField = Ext.create('Ext.form.field.Number',{
			name:'deptOrder',
			fieldLabel: '部门排序',
			value:'0'
		});
		
		var deptBzField = Ext.create('Ext.form.field.TextArea',{
			 fieldLabel: '部门描述',
			 name:'bz'
		});
		
		var clearEditForm = function(){
			idField.setValue('');
			deptNameField.setValue('');
			deptCodeField.setValue('');
			deptOrderField.setValue('0');
			deptBzField.setValue('');
			deptNameField.focus();
		};
		
		var addDeptBtn = Ext.create("Ext.button.Button",{
			text:"添加子部门",
			iconCls:'icon-add',
			disabled:true,
			handler:function(){
				editForm.getForm().reset(true);
				var zTree = $.fn.zTree.getZTreeObj(treeid);
				var nodes = zTree.getSelectedNodes();
				var node = nodes[0];
				parentIdField.setValue(node.id);
				parentNameField.setValue(node.name);
				deptNameField.focus();
			}
		});
		
		var delDeptBtn = Ext.create("Ext.button.Button",{
			text:"删除部门",
			disabled:true,
			iconCls:'icon-remove',
			handler:function(){
				var zTree = $.fn.zTree.getZTreeObj(treeid);
				var nodes = zTree.getSelectedNodes();
				var deptIds = [];
				for(var i = 0;i<nodes.length;i++){
					deptIds.push(nodes[i].id);
				}
				Ext.ux.Ajax.request({
				    url:contextPath+'/system/depts/delete',
				    params: {deptIds:deptIds.join(",")},
				    success: function(response,opt,result){
				        editForm.getForm().reset(true);
				        initDeptTreeView();
				        addDeptBtn.disable();
						delDeptBtn.disable();
				    }
				});
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
			defaultType: 'textfield',
			items: [idField,parentIdField,
			        parentNameField,deptNameField,
			        deptCodeField,deptOrderField,deptBzField
			]
		 });
		
		var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			width: 350,
			margin:"5 0 0 0",
			items: [
			        "->",
				delDeptBtn,addDeptBtn,{
					text:"保存信息",
					iconCls:'icon-save',
					handler:function(){
						if(editForm.isValid()){
							var item = editForm.getValues();
							if(item.id ==""){
								var record = Ext.create('system.model.Dept',item);
								store.add(record);
							}else{
								editForm.updateRecord();
							}
							store.sync({
								success:function(batch,options,result){
									var operation = batch.operations[0];
									var action = operation.action;
									initDeptTreeView();
									var zTree = $.fn.zTree.getZTreeObj(treeid);
									if(action == 'create'){
						    			clearEditForm();
						    			if(item.parentId!="0"){
						    				var node = zTree.getNodeByParam("id", item.parentId);
							    			zTree.selectNode(node);
						    			}
						    		}else{
						    			var node = zTree.getNodeByParam("id", item.id);
						    			zTree.selectNode(node);
						    		}
								}
							});
						}
					}
				}
			]
		});
		
		var editPanel = Ext.create("Ext.panel.Panel",{
			border:false,
			items:[editForm,actionBar]
		});
		
		me.items = [deptTreePanel,editPanel];
		
		me.listeners = {
			resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
				deptTreePanel.setHeight(height);
			}
		};
		
		me.callParent(arguments);
	}	
});



