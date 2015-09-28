Ext.define('Ext.ux.data.Model', {
    extend: 'Ext.data.Model',
  	constructor: function(data, id, raw, convertedData) {
  		var me = this;
  		if(me.proxy!='ajax'){
  			var proxy = me.proxy;
  	  		if(!proxy.isInit){
  	  			var writer =  Ext.create('Ext.ux.data.writer.Json',{
	  	  			writeAllFields: false,
		            expandData:true
  	  			});
  	  	  	   proxy.writer = proxy.writer || writer;
  	  	  	   proxy.idParam = proxy.idParam || me.idProperty;
  	  	       var listeners = {
  	  	            exception: function(proxy, response, operation){
	  	  	            var message = {};
	  		        	var responseText = response.responseText;
	  		        	var index = responseText.indexOf('loginForm');
	  		        	if(index==-1){
	  		        		message = Ext.JSON.decode(responseText);
	  		        	} 
	  		        	if(index!=-1 || message.status==-2){
	  		        		system.view.Login.show();
	  		        	}else{
	  		        		Ext.ux.Msg.error(message.statusText);
	  		        	}
  	  	            }
  	  	        };
  	  	       if(proxy.listeners){
  	  	    	   Ext.applyIf(proxy.listeners,listeners);
  	  	       }else{
  	  	    	   Ext.applyIf(proxy, {listeners:listeners});
  	  	       }
  	  	       proxy.isInit = true;
  	  		}
  		}
		me.callParent(arguments);
	}
});