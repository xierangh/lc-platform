/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('desktop.view.PlatformApp', {
    extend: 'desktop.view.App',
    requires: [
        'desktop.comp.AboutWindow',
        'desktop.comp.Settings',
        'desktop.view.ThemeToolbar'
    ],

    init: function() {
        // custom logic before getXYZ methods get called...
    	var me = this;
    	me.callParent();
    	//me.initTheme();
    	//me.initSearchMenuToolbar();
    	me.initDesktopToolbar();
        // now ready...
    },
    initTheme:function(){
    	//console.log('PlatformApp initTheme (12)');
    	var me = this;
    	var themeToolbar = Ext.create('desktop.view.ThemeToolbar');
 		themeToolbar.show();
 		themeToolbar.alignTo(
 			document.body,
 			Ext.optionsToolbarAlign || 'tr-tr',
 			[(Ext.getScrollbarSize().width + 4) * (Ext.rootHierarchyState.rtl ? 1 : -1),
 			-(document.body.scrollTop || document.documentElement.scrollTop)+10]
 		);
 		var constrainer = function() {
 			themeToolbar.doConstrain();
 			themeToolbar.alignTo(
 				document.body,
 				Ext.optionsToolbarAlign || 'tr-tr',
 				[(Ext.getScrollbarSize().width + 4) * (Ext.rootHierarchyState.rtl ? 1 : -1),
 	 			-(document.body.scrollTop || document.documentElement.scrollTop)+10]
 			);
 		};
 		Ext.EventManager.onWindowResize(constrainer);
 		themeToolbar.on('destroy', function() { 
 			Ext.EventManager.removeResizeListener(constrainer);
 		});
 		me.themeToolbar = themeToolbar;
    },
    initDesktopToolbar:function(){
    	//console.log('PlatformApp initDesktopToolbar (12)');
    	var me = this;
    	var desktopToolbar = Ext.create('desktop.view.DesktopToolbar',{
    		desktop:me.desktop
    	});
    	desktopToolbar.show();
    	desktopToolbar.alignTo(
 			document.body,
 			'tl',
 			[Ext.getBody().getWidth()/2-100,10]
 		);
    	var constrainer = function() {
    		desktopToolbar.doConstrain();
    		desktopToolbar.alignTo(
 				document.body,
 				'tl',
 				[Ext.getBody().getWidth()/2-100,10]
 			);
 		};
 		Ext.EventManager.onWindowResize(constrainer);
 		desktopToolbar.on('destroy', function() { 
 			Ext.EventManager.removeResizeListener(constrainer);
 		});
 		me.desktopToolbar = desktopToolbar;
    },
    initSearchMenuToolbar:function(){
    	//console.log('PlatformApp initSearchMenuToolbar (13)');
    	var me = this;
    	var searchMenuToolbar = Ext.create('desktop.view.SearchMenuToolbar',{
    		desktop:me.desktop
    	});
    	searchMenuToolbar.show();
    	searchMenuToolbar.alignTo(
 			document.body,
 			'tl',
 			[Ext.getBody().getWidth()/2+50,20]
 		);
    	var constrainer = function() {
    		searchMenuToolbar.doConstrain();
    		searchMenuToolbar.alignTo(
 				document.body,
 				'tl',
 				[Ext.getBody().getWidth()/2+50,20]
 			);
 		};
 		Ext.EventManager.onWindowResize(constrainer);
 		searchMenuToolbar.on('destroy', function() { 
 			Ext.EventManager.removeResizeListener(constrainer);
 		});
 		me.searchMenuToolbar = searchMenuToolbar;
    },
    getModules : function(){
    	var modules = [new desktop.comp.AboutWindow()];
        return modules;
    },

    getDesktopConfig: function () {
        var me = this, ret = me.callParent();
        //初始化用户主题信息
        var contextMenuItems = [];
        //if(isSuperAdmin==='true' || Ext.ux.Ajax.hasPerm('ROLE_IMAGE_SETTING')){
        //	contextMenuItems.push({ text: '背景设置', iconCls:'settings',handler: me.onSettings, scope: me });
        //}
        return Ext.apply(ret, {
            contextMenuItems: contextMenuItems,
            wallpaper: contextPath + '/desktop/wallpapers/bg.jpg',
            wallpaperStretch: false
        });
    },

    // config for the start menu
    getStartConfig : function() {
        var me = this, ret = me.callParent();
        return Ext.apply(ret, {
            title: '系统综合管理平台',
            iconCls: 'video',
            height: 300,
            toolConfig: {
                width: 100,
                items: [
                    {
                        text:'背景设置',
                        iconCls:'settings',
                        handler: me.onSettings,
                        scope: me
                    },
                    '-',
                    {
                        text:'退出系统',
                        iconCls:'logout',
                        handler: me.onLogout,
                        scope: me
                    }
                ]
            }
        });
    },

    getTaskbarConfig: function () {
        var ret = this.callParent();
        return Ext.apply(ret, {
            quickStart: [
                //{ name: '在线用户', iconCls: 'accordion', module: 'acc-win' },
                //{ name: '用户管理', iconCls: 'icon-grid', module: 'grid-win' }
            ],
            trayItems: [
                Ext.create('desktop.view.TrayClock',{
                	flex: 1
                })
            ]
        });
    },

    onLogout: function () {
        Ext.Msg.confirm('退出系统', '你确定要退出系统吗?', function(op){
        	if(op == "yes"){
        		console.log('退出系统，返回到首页');
        		var location = window.location;
				window.location.replace ("http://"+location.host+contextPath + "/j_spring_security_logout");  
        	}
        });
    },

    onSettings: function() {
    	var dlg = Ext.create('desktop.comp.Settings',{
    		 desktop: this.desktop
    	});
        dlg.show();
    }
});
