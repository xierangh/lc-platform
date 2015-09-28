/**
 * 长时间没有登录导致会话失效后会进行登录认证
 */
Ext.define('system.view.Login',{
	singleton: true,
	constructor: function(config) {
		var me = this;
		me.logview = undefined;
		var runner = new Ext.util.TaskRunner();
		me.task = runner.newTask({
		     run: function () {
		    	 me.loginView.show();
		     },
		     interval: 2000,
		     repeat:1
		 });
    },
    show:function(){
    	var me = this;
    	if(me.loginView){
    		me.task.restart();
    	}else{
    		me.loginView = me.createLoginView();
    		me.loginView.show(null,function(){
    			me.usernameField.focus();
    		});
    	}
    },
    createLoginView:function(){
    	var me = this;
    	var required = '<span style="color:red;font-weight:bold" data-qtip="Required">*</span>';
		var txtPassword = Ext.create('Ext.form.field.Text',{
			columnWidth: 0.95,
			allowBlank: false,
			name:'password',
			inputType:'password',
			afterLabelTextTpl:required,
			fieldLabel:'密　　码'
		});
		
		var btnLogin = Ext.create("Ext.button.Button",{
			text:'登　　陆',
			handler:function(){
				if(loginForm.isValid()) {
					loginForm.submit({
						 url:contextPath+"/j_spring_security_check",
						 success: function(form, action) {
							 me.loginView.close();
						 },
						 failure: function(form, action,result){
				    		 txtPassword.setValue('');
						 }
					 });
				}
			}
		});
		
		me.usernameField = Ext.create("Ext.form.field.Text",{
			columnWidth:0.95,
			allowBlank:false,
			name:'username',
			afterLabelTextTpl:required,
			fieldLabel: '用户名字'
		});
		
		var loginForm = Ext.create("Ext.ux.form.Panel",{
			border:false,
			submitBtn:btnLogin,
			fieldDefaults:{
				msgTarget:'side',
				labelWidth: 65,
				margin:'0 0 5 10'
			},
			padding:'20 0 0 5',
			layout:'column',
			defaultType:'textfield',
			items: [{
				xtype:'hiddenfield',
				name:'loginType',
				value:'ajaxLogin'
				},me.usernameField,txtPassword,{
			        xtype: 'displayfield',
			        columnWidth:1,
			        value: '<span style="color:red;">由于长时间没有操作，登陆已经过期。</span>'
			    }
			]
		});
		
		var actionTool ={
			 maxHeight:50,
			 border: false,
			 layout: {
				type: 'hbox', 
				align: 'middle',
				pack: 'end'
			},
			padding:"5 17 5 0",
			defaults: {
				width: 80,
				margin:'0 0 0 10'
			},
			 items: [btnLogin]
		 };
    	var systemLogin  = Ext.create('Ext.window.Window',{
    		title: '重新登陆系统(F5)',
    		closable:false,
    		width: 360,
    		height:220,
    		bodyStyle:'background:#FFF',
    		constrain:true,
    		resizable:false,
    		modal:true,
    		items:[loginForm,actionTool],
    		listeners:{
 				close:function( panel, eOpts ){
 					me.loginView = undefined;
 				}
 			}
    	});
    	return systemLogin;
    }
});