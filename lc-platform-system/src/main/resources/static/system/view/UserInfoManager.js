Ext.define('system.view.UserInfoManager',{
	extend:'Ext.tab.Panel',
	defaults: { 
    	autoScroll:true
    }, 
    plain:true,
    margin:"5 1 0 1",
    bodyStyle:{
		"top":"32px !important"
	},
  	activeTab: 0,
	initComponent:function(){
		var me = this;
		var userHeadInfoView = contextPath + '/system/users/headinfo';
		var passwordEdit = Ext.create('system.view.PasswordEdit');
		me.items = [passwordEdit,{
            title: '修改头像',
            bodyPadding:10,
            html: '<iframe scrolling="auto" frameborder="0"  src="'+userHeadInfoView+'" style="width:100%; height:99%;" allowTransparency="true" ></iframe>'
        }];
		me.callParent(arguments);
	}	
});



