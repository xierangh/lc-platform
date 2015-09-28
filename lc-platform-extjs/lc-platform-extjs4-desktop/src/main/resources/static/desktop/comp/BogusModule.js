/*!
* Ext JS Library 4.0
* Copyright(c) 2006-2011 Sencha Inc.
* licensing@sencha.com
* http://www.sencha.com/license
*/

Ext.define('desktop.comp.BogusModule', {
    extend: 'desktop.view.Module',

    init : function(){
        this.launcher = {
            text: 'Window',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        }
    },

    createWindow : function(src){
        var desktop = this.app.getDesktop();
        console.log(src.id);
        var win = desktop.getWindow('bogus' + src.id);
        if(!win){
            win = desktop.createWindow({
                id: 'bogus' + src.id,
                title:src.text,
                width:640,
                height:480,
                html : '',
                iconCls: 'bogus',
                animCollapse:false,
                constrainHeader:true
            });
        }
        win.show();
        return win;
    }
});