Ext.define('Ext.ux.PermButton', {
    extend: 'Ext.button.Button',
    alias: 'widget.permbutton',
    initComponent:function(){
    	var me = this;
    	//ifAnyGranted,ifNotGranted,ifAllGranted
    	//请求是否拥有这个权限
    	me.hidden = true;
    	var ifAnyGranted = me.ifAnyGranted;
    	if(ifAnyGranted){
    		Ext.ux.Ajax.request({
    			async:false,
			    url: contextPath+'/system/users/hasPerm',
			    params: {
			        permcode: ifAnyGranted
			    },
			    success: function(response,opts,result){
			    	me.hidden = !result.data;
			    }
			});
    	}
    	me.callParent(arguments);
    }
});