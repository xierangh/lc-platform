/**
 * 用户信息编辑主界面
 */
Ext.define('system.view.UserUpdate',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		var records = me.list.collection.items;
		me.userIds = me.list.collection.keys.join(",");
		
		var userIdField = Ext.create('Ext.form.field.Hidden',{
			name:'userId'
		});
		
		var usernameField = Ext.create('Ext.form.field.Display',{
			columnWidth: 0.4,
			name:'username',
			fieldLabel: '登录名字'
		});
		var emailField = Ext.create('Ext.form.field.Text',{
			columnWidth: 0.4,
			vtype:'email',
			name : 'email',
			fieldLabel: '邮　　箱',
			allowBlank: false,
			validator: function(obj){
				var result = true;
				var email = emailField.getValue();
				if(email!='' && email!=records[0].get("email")){
					Ext.ux.Ajax.request({
					    url:contextPath+'/system/users/checkedEmail',
					    async:false,
					    params: {
					        email: emailField.getValue(),
					    },
					    success: function(response){
					    	result = response.responseText=="true"?true:response.responseText;
					    }
					});
				}
				return result;
			}
		});
		
		var delStatusbox = Ext.create('Ext.form.field.Checkbox',{
			name:'delStatus',
			boxLabel  : '删除账户',
			columnWidth: 0.25,
			xtype:'checkbox',
			margin:'0 0 5 10'
		});
		
		var editForm = Ext.create('Ext.ux.form.Panel',{
			autoScroll:true,
			flex:1,
			bodyStyle: {
			    'border-color': '#c5c5c5 !important;'
			},
			bodyPadding: 10,
			fieldDefaults: {
				labelWidth: 60
			},
			layout:'column',
			defaultType: 'textfield',
			items: [userIdField,usernameField,{
					columnWidth: 0.5,
					name:'xm',
					margin:'0 0 5 10',
					fieldLabel: '真实姓名',
				},emailField,{
					columnWidth: 0.5,
					inputType: 'password',
					name:'password',
					margin:'0 0 5 10',
					fieldLabel:'密　　码'
				},delStatusbox
			]
		 });
		
		var treeid = Ext.data.IdGenerator.get('uuid').generate();
		
		function zTreeBeforeCheck(treeId, treeNode) {
			var zTree = $.fn.zTree.getZTreeObj(treeid);
			var nodes = zTree.getCheckedNodes(true);
			if(nodes.length==1 && treeNode.checked){
				Ext.ux.Msg.error("该用户至少拥有一个部门信息");
				return false;
			}
		    return true;
		};
		
		function zTreeOnCheck(event, treeId, treeNode) {
			Ext.ux.Ajax.request({
			    url: contextPath + "/system/users/dept/update",
			    params: {
			        userIds:me.userIds,
			        deptId:treeNode.id,
			        grant:treeNode.checked
			    },
			    tips:false,
			    success: function(response,opts,result){
			    	me.list.store.reload();
			    }
			});
		}
		
		var initDeptTreeView = function(){
			var setting = {
					check:{
						enable:true,
						chkboxType:{ "Y" : "", "N" : "" }
					},
    				data: {
    					simpleData: {
    						enable: true
    					}
    				},
    				callback: {
    					onCheck: zTreeOnCheck,
    					beforeCheck: zTreeBeforeCheck
    				}
    		};
    		var zNodes;
    		Ext.ux.Ajax.request({
			    url:contextPath+'/system/depts/tree',
			    async:false,
			    params:{
			    	userIds:me.userIds
			    },
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
		
		
		var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			items: [
			    '->', 
					{
					text:"保存信息",iconCls:'icon-save',
					handler:function(button){
						if(editForm.isValid()){
							var item = editForm.getValues();
							 Ext.ux.Ajax.request({
							    url: contextPath + "/system/users/update",
							    params: {
							        userId:me.userIds,
							        username:item.username,
							        xm:item.xm,
							        email:item.email,
							        password:item.password,
							        delStatus:delStatusbox.checked
							    },
							    success: function(response,opts,result){
							    	me.list.store.reload();
							    }
							});
						}
					}
				}
			]
		});
		
		var roleGrantList = Ext.create("system.view.RoleGrantList",{
			height:280,
			userIds:me.userIds,
			checkchange:function(checkColumn, rowIndex, checked, eOpts){
				var record = roleGrantList.store.getAt(rowIndex);
				 Ext.ux.Ajax.request({
				    url: contextPath + "/system/users/role/update",
				    params: {
				        userIds:me.userIds,
				        roleIds:record.getId(),
				        grant:checked
				    },
				    tips:false,
				    success: function(response,opts,result){
				    	me.list.store.reload();
				    }
				});
			}
		});
		
		var leftPanel = Ext.create("Ext.panel.Panel",{
			border:false,
			margin:"5 0 5 5",
			flex:1,
			items:[editForm,actionBar,roleGrantList]
		});
		
		if(records.length==1){
			editForm.loadRecord(records[0]);
		}else{
			editForm.hide();
			actionBar.hide();
		}
		
		me.items = [leftPanel,deptTreePanel];
		
		me.showCallback = function(){
			usernameField.focus();
		}
		
		me.listeners = {
				resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
					deptTreePanel.setHeight(height-10);
					roleGrantList.setHeight(height-155);
				}
			};
		
		me.callParent(arguments);
	}	
});



