/**
 * 用户管理主界面
 */
Ext.define('system.view.UserManager',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		
		var getExtraParams = function(){
			var extraParams = {};
			var rules = [];
			var val = searchCombobox.getValue()==null?"":searchCombobox.getValue();
			rules.push({field:'username',data:val,op:'cn'});
			var treeObj = $.fn.zTree.getZTreeObj(treeid);
			var nodes = treeObj.getSelectedNodes();
			if(nodes.length>0){
				var treeObj = $.fn.zTree.getZTreeObj(treeid);
				var selNodes = treeObj.getSelectedNodes();
				var nodes = treeObj.transformToArray(selNodes);
				var deptIds = [];
				for(var i = 0 ;i<nodes.length;i++){
					deptIds.push(nodes[i].id);
				}
				rules.push({field:'dept.id',data:deptIds.join(","),op:'in'});
			}
			extraParams.filters = Ext.JSON.encode({rules:rules});
			return extraParams;
		};
		
		var list = Ext.create('system.view.UserList',{
			getExtraParams:getExtraParams
		});
		
		var searchCombobox = Ext.create('Ext.ux.form.field.SearchText',{
			store: list.store,
			emptyText: '用户搜索,多个逗号分隔'
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
						var id="system_view_UserAdd";
						var win = me.desktop.getWindow(id);
				        if(!win){
				        	var userAdd = Ext.create('system.view.UserAdd',{
				        		desktop:me.desktop,
				        		store:list.store
				        	});
				            win = me.desktop.createWindow({
				                id:id,
				                title:'添加用户',
				                width:780,
				                height:480,
				                animCollapse:false,
				                constrainHeader:true,
				                layout: 'fit',
				                icon:contextPath+"/system/images/add_user.png",
				                items: [userAdd]
				            });
				        }
				        if (win) {
				        	me.desktop.restoreWindow(win);
				        }
					}
				},{
					text:"修改",iconCls:'icon-update',
					handler:function(button){
						if(list.collection.getCount()==0){
							Ext.ux.Msg.error("请选择一条数据进行编辑操作");
				    		return;
						}
						var id="system_view_UserUpdate";
						var win = me.desktop.getWindow(id);
				        if(!win){
				        	var userUpdate = Ext.create('system.view.UserUpdate',{
				        		list:list
				        	});
				            win = me.desktop.createWindow({
				                id:id,
				                title:'编辑用户',
				                width:780,
				                height:480,
				                animCollapse:false,
				                constrainHeader:true,
				                layout: 'fit',
				                icon:contextPath+"/system/images/add_user.png",
				                items: [userUpdate]
				            });
				        }
				        if (win) {
				        	me.desktop.restoreWindow(win);
				        }
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
				},
				{text:'授予角色',iconCls:'icon-add',
					handler:function(button){
						if(list.collection.getCount()==0){
							Ext.ux.Msg.error("请选择一条数据进行授予角色操作");
				    		return;
						}
						var roleSelectIndex = Ext.create('system.view.RoleSelectIndex');
						var win = Ext.create('Ext.window.Window',{
							title:'授予角色信息',
							width: 800,
							minHeight:400,
							constrain:true,
							modal:true,
							resizable:false,
							maximizable:false,
							items:[roleSelectIndex]
						});
						roleSelectIndex.selectedHandler = function(records){
							var roleIds = [];
							for(var i = 0;i<records.length;i++){
								roleIds.push(records[i].getId());
							}
							Ext.ux.Ajax.request({
							    url: contextPath + "/system/users/role/update",
							    params: {
							        userIds:list.collection.keys.join(","),
							        roleIds:roleIds.join(","),
							        grant:true
							    },
							    success: function(response,opts,result){
							    	list.store.reload();
							    }
							});
						};
						roleSelectIndex.parent = win;
						win.show();
					}
				}
			]
		});
		
		var treeid = Ext.data.IdGenerator.get('uuid').generate();
		
		function onClick(e, treeId, treeNode) {
			list.store.load();
		}
		
		var initDeptTreeView = function(){
			var setting = {
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: onClick
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
		
		var userPanel = Ext.create('Ext.panel.Panel',{
			border:false,
			flex:1,
			margin:"0 5 5 0",
			items:[actionBar,list]
		});
		
		me.items = [deptTreePanel,userPanel];
		
		me.listeners = {
			resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
				deptTreePanel.setHeight(height-10);
				list.setHeight(height-46);
			}
		};
		
		me.callParent(arguments);
	}	
});



