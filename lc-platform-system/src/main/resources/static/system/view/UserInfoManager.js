Ext.define('system.view.UserInfoManager',{
	extend:'Ext.tab.Panel',
	defaults: { 
    	autoScroll:true
    }, 
    plain:true,
    margin:"5 1 0 1",
  	activeTab: 0,
	initComponent:function(){
		var me = this;
		var userHeadInfoView = contextPath + '/system/users/headinfo';
		me.items = [{
            title: '修改头像',
            bodyPadding:10,
            html: '<iframe scrolling="auto" frameborder="0"  src="'+userHeadInfoView+'" style="width:100%; height:99%;" allowTransparency="true" ></iframe>'
        }];
		me.callParent(arguments);
	}	
});



