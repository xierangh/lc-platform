Ext.define('Ext.ux.data.Store', {
    extend: 'Ext.data.Store',
	pageSize:25,
	remoteSort:true,
	autoLoad:true,
    constructor: function(config) {
    	var me = this;
    	var config = config || {};
		var extraParams = config.params || {};
		var modelClass = me.model;
		var modelEntity = Ext.create(modelClass);
		var proxy = config.proxy || me.proxy || Ext.clone(modelEntity.proxy);
		
		if(proxy=='ajax'){
			proxy = {
				type: 'ajax',
		        idParam: modelEntity.idProperty,
		        api:{}
			};
		}
		me.readUrl = config.readUrl || me.readUrl || proxy.api.read;
		me.createUrl = config.createUrl || me.createUrl || proxy.api.create;
		me.updateUrl = config.updateUrl || me.updateUrl || proxy.api.update;
		me.destroyUrl = config.destroyUrl || me.destroyUrl || proxy.api.destroy;
		me.batchCreate = config.batchCreateUrl || me.batchCreate || proxy.api.read+'/batchCreate';
		me.batchUpdate = config.batchUpdateUrl || me.batchUpdate || proxy.api.read+'/batchUpdate';
		me.batchDestroy = config.batchDestroyUrl || me.batchDestroy || proxy.api.read+'/batchDelete';
		Ext.apply(proxy,{
			 	extraParams:extraParams,
				actionMethods: {read:'POST'},
				enablePaging: true,
				api: {
		        	read:me.readUrl,
					create:me.createUrl,
					update:me.updateUrl,
					destroy:me.destroyUrl
				},
				reader: {
				    type: 'json',
					root: 'rows',
					totalProperty: 'records',
					messageProperty:'statusText'
			    }
			}
		 );
		
		var dynParamFun = config.dynParamFun;
		
		if(dynParamFun){
			var listeners = me.listeners || {};
			var currBeforeload = listeners.beforeload || undefined;
			var beforeload = function( store, operation, eOpts ){
				me.proxy.extraParams = dynParamFun();
				if(currBeforeload){
					currBeforeload(store, operation, eOpts);
				}
			};
			listeners.beforeload = beforeload;
			me.listeners = listeners;
		}
		
		 var listeners = {
	        exception: function(proxy, response, operation){
	        	if(response.status==0){
	        		Ext.ux.Msg.error("连接服务器失败,网络不是很稳定");
	        		return;
				}
	        	var message = {};
	        	var responseText = response.responseText;
	        	var index = responseText.indexOf('loginForm');
	        	if(index==-1){
	        		message = Ext.JSON.decode(responseText);
	        	} 
	        	if(index!=-1 || message.status==-2){
	        		me.rejectChanges();
	        		system.view.Login.show();
	        	}else{
	        		Ext.ux.Msg.error(message.statusText);
	        	}
	        }
		 };
		 if(proxy.listeners){
			 Ext.apply(proxy.listeners,listeners);
		 }else{
			 proxy.listeners = listeners;
		 }
		 config.proxy = proxy;	
		 me.callParent([config]);
    },
    sync:function(options){
    	var me = this;
    	if(me.getNewRecords().length>1){
    		me.proxy.api.create = me.batchCreate;
		}else{
			me.proxy.api.create = me.createUrl;
		}
    	if(me.getUpdatedRecords().length>1){
    		me.proxy.api.update = me.batchUpdate;
    	}else{
    		me.proxy.api.update = me.updateUrl;
    	}
    	if(me.getRemovedRecords().length>1){
    		me.proxy.api.destroy = me.batchDestroy;
    	}else{
    		me.proxy.api.destroy = me.destroyUrl;
    	}
    	
    	var currSuccess = options.success || undefined;
    	var currFailure = options.failure || undefined;
    	
    	var aopSuccess = function(batch,options){
    		var operation = batch.operations[0];
    		try{
    			var responseText = operation.response.responseText;
            	var index = responseText.indexOf('loginForm');
            	var result;
            	if(index==-1){
            		result = Ext.JSON.decode(responseText);
            		var status = result.status;
            		if(status>=0){
            			Ext.ux.Msg.show(result.statusText);
            			if(currSuccess){
            				currSuccess(batch,options,result);
            			}
            		}else if(status==-2){
            			system.view.login.show();
            		}else{
            			Ext.ux.Msg.error(result.statusText);
        	    		if(currFailure){
    						currFailure(operation.response);
    					}
            		}
            	}else{
            		system.view.login.show();
            	}
    		}catch(err){
    			if(currSuccess){
					currSuccess(batch,options);
				}
    		}
		};
		var aopFailure = function(batch,options){
			me.rejectChanges();
			if(currFailure){
				currFailure(batch,options);
			}
		};
		options.success = aopSuccess;
		options.failure = aopFailure;
    	return this.callParent([options]);
    }
});