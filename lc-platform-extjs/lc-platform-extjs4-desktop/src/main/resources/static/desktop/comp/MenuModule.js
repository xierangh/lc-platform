/*!
* Ext JS Library 4.0
* Copyright(c) 2006-2011 Sencha Inc.
* licensing@sencha.com
* http://www.sencha.com/license
*/

Ext.define('desktop.comp.MenuModule', {
    extend: 'desktop.view.Module',
    item:null,
    init : function(){
    	var me = this;
    	var item = me.item;
    	me.launcher = {
			id:item.id,
            text: item.text,
            //iconCls:item.iconCls,
            icon:contextPath + '/desktop/images/16x16/bogus.png',
            handler : me.createWindow,
            scope: me
        }
		if(item.leaf == false){
			me.launcher.handler=function() {return false;};
			me.launcher.menu = {items: []};
			me.setChildModules(me.launcher.menu.items,item.children);
		}
    },
    setChildModules:function(parent,children){
    	for(var i=0;i<children.length;i++){
    		var item = children[i];
    		var launcher = {
				id:item.id,
				text: item.text,
	            //iconCls:item.iconCls,
				icon:contextPath + '/desktop/images/16x16/bogus.png',
	            handler : this.createWindow,
	            scope: this
    		};
    		if(item.leaf == false){
    			launcher.handler=function() {return false;};
    			launcher.menu = {items: []};
    			this.setChildModules(launcher.menu.items,item.children);
    		}
    		parent.push(launcher);
    	}
    },
    createWindow : function(src){
        var desktop = this.app.getDesktop();
        console.log(src.id);
        var win = desktop.getWindow('menu_' + src.id);
        if(!win){
            win = desktop.createWindow({
                id: 'menu_' + src.id,
                title:src.text,
                width:640,
                height:480,
                html : '',
                //iconCls: 'bogus',
                icon:contextPath + '/desktop/images/16x16/bogus.png',
                animCollapse:false,
                constrainHeader:true
            });
        }
        win.show();
        return win;
    }
});