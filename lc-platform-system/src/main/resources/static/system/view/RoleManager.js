/**
 * 角色管理主界面
 */
Ext.define('system.view.RoleManager',{
	extend:'Ext.panel.Panel',
	 layout: {
        type: 'hbox',
        align: 'stretch'
    },
	initComponent:function(){
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
			height:230,
			getExtraParams:getExtraParams,
			listeners:{
				selectionchange:function( record, selected , eOpts ){
					initDeptTreeView();
				}
			}
		});
		var searchCombobox = Ext.create('Ext.ux.form.field.SearchText',{
			store: list.store,
			emptyText: '角色搜索,多个逗号分隔'
		});
		
		var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			margin:"5 0 0 0",
			items: [
				{
					xtype:'displayfield',
					value:'关键字:'
				},
				searchCombobox,{
					text: '搜索',iconCls:'icon-search',
					handler:function(button){
						list.store.loadPage(1);
					}
				},{
					text: '重置',iconCls:'icon-reset',
					handler:function(button){
						searchCombobox.setValue("");
						list.collection.clear();
						list.store.loadPage(1);
					}
				},{
					text:"添加",iconCls:'icon-add',
					handler:function(button){
						var record = Ext.create("system.model.Role",{
							roleName:"新角色编辑"
						});
						
				        list.getStore().insert(0, record);
				        list.cellEditing.startEditByPosition({
				            row: 0, 
				            column: 0
				        });
					}
				},{
					text:"保存",iconCls:'icon-save',
					handler:function(button){
						list.store.sync({
							success:function(batch,options,result){
								var operation = batch.operations[0];
								var action = operation.action;
								list.store.reload();
							}
						});
					}
				},
				{text:'删除',iconCls:'icon-remove',
					handler:function(button){
						if(list.collection.getCount()==0){
							Ext.ux.Msg.error("请选择一条数据进行删除操作");
				    		return;
						}
						Ext.ux.Ajax.request({
						    url: list.getStore().batchDestroy,
						    params: {
						        ids: list.collection.keys
						    },
						    success: function(response,opts,result){
						    	list.collection.clear();
						        list.store.load();
						    }
						});
					}
				}
			]
		});
		
		var treeid = Ext.data.IdGenerator.get('uuid').generate();
		
		function zTreeOnCheck(event, treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeid);
		    console.log(treeNode.tId + ", " + treeNode.name + "," + treeNode.checked);
		    var childNodes = zTree.transformToArray(treeNode);
		    var nodes = new Array();
		    for(var i = 0;i<childNodes.length;i++){
		    	var childNode = childNodes[i];
		    	if(!childNode.isParent){
		    		nodes[i] = childNode.id;
		    	}else if(childNode.data.permCode!=""){
		    		nodes[i] = childNode.data.permCode;
		    	}
		    }
		   var parentNode = treeNode.getParentNode();
		   while(parentNode!=null){
			   if(parentNode.data.permCode!="" && parentNode.checked == treeNode.checked){
				   nodes.push(parentNode.data.permCode);
			   }
			   parentNode = parentNode.getParentNode();
		   }
		    Ext.ux.Ajax.request({
			    url: contextPath + "/system/roles/perms/update",
			    params: {
			        roleIds:list.collection.keys,
			        permIds:nodes,
			        grant:treeNode.checked
			    },
			    tips:false,
			    success: function(response,opts,result){
			    	
			    }
			});
		};
		
		function zTreeBeforeCheck(treeId, treeNode) {
			if(list.collection.getCount()==0){
				Ext.ux.Msg.error("请勾选角色后在进行该操作");
	    		return false;
			}
		    return true;
		};
		
		
		
		var initDeptTreeView = function(){
			var roleIds = ["-1"];
			if(list.collection.keys.length>0){
				roleIds = list.collection.keys;
			}
			var setting = {
    				data: {
    					simpleData: {
    						enable: true
    					}
    				},
    				check:{
    					enable:true
    				},
    				callback:{
    					onCheck: zTreeOnCheck,
    					beforeCheck: zTreeBeforeCheck
    				}
    		};
    		var zNodes;
    		Ext.ux.Ajax.request({
			    url:contextPath+'/system/perms/tree',
			    async:false,
			    params: {
			        roleIds:roleIds
			    },
			    success: function(response){
			    	zNodes = Ext.JSON.decode(response.responseText);
			    }
			});
    		$.fn.zTree.init($("#"+treeid), setting, zNodes);
		}
		
		var permTreelPanel = Ext.create('Ext.panel.Panel', {
		    autoScroll:true,
		    width:230,
			margin:5,
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
		
		var tabPanel = Ext.create("Ext.tab.Panel",{
			margin:"5 0 0 5",
			plain:true,
			border:false,
			bodyStyle:{
				"top":"32px !important"
			},
			items:[{
				title:"角色授予信息",
				html:"",
				border:false
			}]
		});
		
		var leftPanel = Ext.create("Ext.panel.Panel",{
			border:false,
			flex:1,
			items:[actionBar,list,tabPanel]
		});
		
		me.items = [leftPanel,permTreelPanel];
		
		me.listeners = {
			resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
				permTreelPanel.setHeight(height-50);
			}
		};
		me.callParent(arguments);
	}	
});



