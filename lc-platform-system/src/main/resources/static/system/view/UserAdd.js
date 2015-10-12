/**
 * 添加用户信息编辑主界面
 */
Ext.define('system.view.UserAdd',{
	extend:'Ext.panel.Panel',
	layout:'hbox',
	initComponent:function(){
		var me = this;
		
		var userIdField = Ext.create('Ext.form.field.Hidden',{
			name:'userId'
		});
		
		var usernameField = Ext.create('Ext.form.field.Text',{
			columnWidth: 0.4,
			name:'username',
			fieldLabel: '登录名字',
			validator: function(obj){
				var reg=new RegExp(/^[A-Za-z0-9]{1,20}$/ig);
				if(reg.test(obj)){
					var result = true;
					Ext.ux.Ajax.request({
					    url:contextPath+'/system/users/checkedUserName',
					    async:false,
					    params: {
					        username: usernameField.getValue(),
					    },
					    success: function(response){
					    	result = response.responseText=="true"?true:response.responseText;
					    }
					});
					return result;
				}
				return '只能是字母,数字的组合,长度20以内';
			}
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
				if(email!=''){
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
					fieldLabel:'密　　码',
					value:'123456',
					allowBlank:false
				}
			]
		 });
		
		var treeid = Ext.data.IdGenerator.get('uuid').generate();
		
		function onClick(e, treeId, treeNode) {
			
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
		
		
		var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			items: [
			    '->', 
				{
					text:"清空信息",iconCls:'icon-reset',
					handler:function(button){
						editForm.getForm().reset(true);
						roleGrantList.store.load();
						initDeptTreeView();
					}
				},{
					text:"保存信息",iconCls:'icon-save',
					handler:function(button){
						if(editForm.isValid()){
							var item = editForm.getValues();
							var roleIds = roleGrantList.collection.items;
							var zTree = $.fn.zTree.getZTreeObj(treeid);
							var nodes = zTree.getCheckedNodes(true);
							if(nodes.length==0){
								Ext.ux.Msg.error("请勾选部门信息");
								return;
							}
							var deptIds = [];
							for(var i = 0 ;i<nodes.length;i++){
								deptIds.push(nodes[i].id);
							}
							 Ext.ux.Ajax.request({
							    url: contextPath + "/system/users/create",
							    params: {
							        roleIds:roleIds,
							        deptIds:deptIds,
							        username:item.username,
							        xm:item.xm,
							        email:item.email,
							        password:item.password
							    },
							    success: function(response,opts,result){
							    	me.store.reload();
							    	editForm.getForm().reset(true);
									roleGrantList.store.load();
									initDeptTreeView();
							    }
							});
						}
					}
				}
			]
		});
		
		var roleGrantList = Ext.create("system.view.RoleGrantList",{
			height:280
		});
		
		var leftPanel = Ext.create("Ext.panel.Panel",{
			border:false,
			margin:"5 0 5 5",
			flex:1,
			items:[editForm,actionBar,roleGrantList]
		});
		
		me.items = [leftPanel,deptTreePanel];
		
		me.showCallback = function(){
			usernameField.focus();
		}
		
		me.listeners = {
				resize:function(panel, width, height, oldWidth, oldHeight, eOpts ){
					deptTreePanel.setHeight(height-10);
					roleGrantList.setHeight(height-128);
				}
			};
		
		me.callParent(arguments);
	}	
});



