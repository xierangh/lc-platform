Ext.define('Ext.ux.Ajax', {
    extend: 'Ext.data.Connection',
    singleton: true,
    autoAbort : false,
    waitMsg:"正在处理请求,请耐心等待",
    hasPerm:function(permcode){
    	var hasPerm = false;
    	Ext.ux.Ajax.request({
			async:false,
		    url: contextPath+'/system/users/hasPerm',
		    params: {
		        permcode: permcode
		    },
		    success: function(response,opts,result){
		    	hasPerm = result.data;
		    }
		});
    	return hasPerm;
    },
    request : function(options) {
    	var me = this;
    	var currSuccess = options.success || undefined;
    	var currFailure = options.failure || undefined;
    	var tips = options.tips == undefined ?true:options.tips;
    	//var mask = new Ext.LoadMask(Ext.getBody(), {msg:me.waitMsg});
    	
    	var aopSuccess = function(response,opts){
			//mask.hide();
    		try{
    			var responseText = response.responseText;
            	var index = responseText.indexOf('loginForm');
            	var result;
            	if(index==-1){
            		result = Ext.JSON.decode(responseText);
            		if(result.status == undefined && currSuccess){
            			currSuccess(response,opts,result);
            		}else if(result.status>=0){
            			if(tips){
            				Ext.ux.Msg.show(result.statusText);
            			}
            			if(currSuccess){
            				currSuccess(response,opts,result);
            			}
            		}else if(result.status==-2){
            			system.view.Login.show();
            		}else{
            			Ext.ux.Msg.error(result.statusText);
    		    		if(currFailure){
    						currFailure(response,opts,result);
    					}
            		}
            	}else{
            		system.view.Login.show();
            	}
    		}catch(err){
    			if(currSuccess){
					currSuccess(response,opts);
				}
    		}
		};
		var aopFailure = function(response,opts){
			//mask.hide();
			if(response.status==0){
				Ext.ux.Msg.error("连接服务器失败,网络不是很稳定");
			}
			if(currFailure){
				currFailure(response,opts);
			}
		};
		options.success = aopSuccess;
		options.failure = aopFailure;
		options.timeout = options.timeout || 600000;
		//mask.show();
    	me.callParent([options]);
    }
});