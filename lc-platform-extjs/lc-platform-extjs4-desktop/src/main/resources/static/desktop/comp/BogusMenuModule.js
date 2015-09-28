/*!
* Ext JS Library 4.0
* Copyright(c) 2006-2011 Sencha Inc.
* licensing@sencha.com
* http://www.sencha.com/license
*/

Ext.define('desktop.comp.BogusMenuModule', {
    extend: 'desktop.comp.BogusModule',

    init : function() {
        this.launcher = {
            text: '系统管理',
            iconCls: 'bogus',
            handler: function() {
                return false;
            },
            menu: {
                items: []
            }
        };

        this.launcher.menu.items.push({
            text: '用户管理',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
       
        this.launcher.menu.items.push({
            text: '角色管理',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
        this.launcher.menu.items.push({
            text: '权限管理',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
        this.launcher.menu.items.push({
            text: '菜单管理',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
        this.launcher.menu.items.push({
            text: '数据词典',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
        this.launcher.menu.items.push({
            text: '模块管理',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
        this.launcher.menu.items.push({
            text: '数据备份',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
        this.launcher.menu.items.push({
            text: '系统日志',
            iconCls:'bogus',
            handler : this.createWindow,
            scope: this
        });
        
    }
});