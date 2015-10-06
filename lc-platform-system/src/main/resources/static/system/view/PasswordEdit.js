Ext.define('system.view.PasswordEdit', {
    extend: 'Ext.panel.Panel',
    title:'修改密码',
    initComponent: function() {
    	var me = this;
    	
    	
    	var oldPasswordField = Ext.create('Ext.form.field.Text',{
			allowBlank: false,
			name:'oldPassword',
			inputType: 'password',
			fieldLabel: '原始密码'
		});
    	
    	var newPasswordField = Ext.create('Ext.form.field.Text',{
			allowBlank: false,
			name:'newPassword',
			inputType: 'password',
			fieldLabel: '新密码'
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
			items: [oldPasswordField,newPasswordField
			]
		 });
    	
    	var actionBar = Ext.create('Ext.toolbar.Toolbar', {
			border:0,
			width: 350,
			items: [
			        "->",{
					text:"保存信息",
					iconCls:'icon-save',
					handler:function(){
						if(editForm.isValid()){
							Ext.ux.Ajax.request({
							    url:contextPath+'/system/users/updatePassword',
							    params: {oldPassword:oldPasswordField.getValue(),newPassword:newPasswordField.getValue()},
							    success: function(response,opt,result){
							        editForm.getForm().reset(true);
							    }
							});
						}
					}
				}
			]
		});
    	
    	me.items = [editForm,actionBar];
    	
    	me.callParent(arguments); 
    }
});