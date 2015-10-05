/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */
/**
 * @class desktop.view.Desktop
 * @extends Ext.panel.Panel
 * <p>This class manages the wallpaper, shortcuts and taskbar.</p>
 */
Ext.define('desktop.view.Desktop', {
    extend: 'Ext.panel.Panel',
    alias: 'widget.desktop',
    uses: [
        'Ext.util.MixedCollection',
        'Ext.menu.Menu',
        'Ext.view.View',
        'Ext.window.Window',
        'desktop.view.TaskBar',
        'desktop.view.Wallpaper'
    ],
    //activeWindowCls: 'ux-desktop-active-win',
    //inactiveWindowCls: 'ux-desktop-inactive-win',
    lastActiveWindow: null,
    border: false,
    html: '&#160;',
    layout: 'fit',
    xTickSize: 1,
    yTickSize: 1,
    app: null,
    /**
     * @cfg {Array|Store} shortcuts
     * The items to add to the DataView. This can be a {@link Ext.data.Store Store} or a
     * simple array. Items should minimally provide the fields in the
     * {@link desktop.module.DesktopMenu DesktopMenu}.
     */
    shortcuts: null,
    /**
     * @cfg {String} shortcutItemSelector
     * This property is passed to the DataView for the desktop to select shortcut items.
     * If the {@link #shortcutTpl} is modified, this will probably need to be modified as
     * well.
     */
    shortcutItemSelector: 'div.ux-desktop-shortcut',
    /**
     * @cfg {String} shortcutTpl
     * This XTemplate is used to render items in the DataView. If this is changed, the
     * {@link shortcutItemSelect} will probably also need to changed.
     */
    shortcutTpl: [
        '<tpl for=".">',
            '<div class="ux-desktop-shortcut {menuStatus}" id="shortcut_{menuId}">',
                '<div class="ux-desktop-shortcut-icon">',
                    '<img src="{image48}" width="48" height="48">',
                '</div>',
                '<div class="ux-desktop-shortcut-text">{menuName}</div>',
            '</div>',
        '</tpl>',
        '<div class="x-clear"></div>'
    ],
    taskbarConfig: null,
    bodyCls:'ux-desktop-body',
    windowMenu: null,
    desktopNumber:1,
    initComponent: function () {
        var me = this;
        console.log('Desktop initComponent (8) ');
        me.shortcutMenu = new Ext.menu.Menu(me.createShortcutMenu());
        me.windowMenu = new Ext.menu.Menu(me.createWindowMenu());
        
        //me.bbar = me.taskbar = new desktop.view.TaskBar(me.taskbarConfig);
        //me.taskbar.windowMenu = me.windowMenu;
        
        me.windows = new Ext.util.MixedCollection();
        me.contextMenu = new Ext.menu.Menu(me.createDesktopMenu());
        me.items = [
            { xtype: 'wallpaper', id: me.id+'_wallpaper' },
            me.createDataView()
        ];
        me.callParent();
        
        me.shortcutsView = me.items.getAt(1);
        me.shortcutsView.shortcutMenu = me.shortcutMenu;
        me.shortcutsView.on('itemclick', me.onShortcutItemClick,me);

        var wallpaper = me.wallpaper;
        me.wallpaper = me.items.getAt(0);
        if (wallpaper) {
            me.setWallpaper(wallpaper, me.wallpaperStretch);
        }
    },
    initTaskToolbar:function(){
    	var me = this;
    	var taskToolbar = Ext.create('desktop.view.TaskToolbar',{
    		width:Ext.getBody().getWidth()
    	});
    	taskToolbar.show();
    	taskToolbar.alignTo(
 			document.body,
 			Ext.optionsToolbarAlign || 'tr-tr',
 			[0,Ext.getBody().getHeight()-66]
 		);
    	var constrainer = function() {
    		taskToolbar.doConstrain();
    		taskToolbar.alignTo(
	 			document.body,
	 			Ext.optionsToolbarAlign || 'tr-tr',
	 			[0,Ext.getBody().getHeight()-66]
	 		);
 		};
 		Ext.EventManager.onWindowResize(constrainer);
 		taskToolbar.on('destroy', function() { 
 			Ext.EventManager.removeResizeListener(constrainer);
 		});
 		me.taskbar = taskToolbar;
 		me.taskbar.windowMenu = me.windowMenu;
    },
    afterRender: function () {
        var me = this;
        me.callParent();
        me.el.on('contextmenu', me.onDesktopMenu, me);
    },
    initShortcut : function() {
    	//console.log('initShortcut');
        var btnHeight = 84;
        var btnWidth = 84;
        var btnPadding = 30;
        var col = {index : 0,x : btnPadding};
        var row = {index : -1,y : btnPadding};
        var bottom;
        //var taskBarHeight = Ext.query(".ux-taskbar")[0].clientHeight + 40;
        var taskBarHeight =64;
        
        var bodyHeight = Ext.getBody().getHeight() - taskBarHeight;
        var items = Ext.query(".ux-desktop-shortcut");

        for (var i = 0, len = items.length; i < len; i++) {
            bottom = row.y + btnHeight;
            if (bodyHeight < bottom) {
                col = {index : col.index+1,x : col.x + btnWidth + btnPadding};
                row = {index : -1,y : btnPadding};
            }
        	row.index++;
        	//console.log("menuName=",items[i].innerText,"bodyHeight=",bodyHeight,"bottom=",bottom,"row=",row.index,"col=",col.index);
            Ext.fly(items[i]).setXY([col.x, row.y]);
            row.y += (btnHeight + btnPadding);
        }
        
    },
    //------------------------------------------------------
    // Overrideable configuration creation methods
    /**
     * 切换桌面菜单信息,指定父级菜单，桌面序号
     */
    switchDesktopMenu:function(menuId,desktopNumber){
    	var me = this;
    	me.shortcutsView.getEl().fadeOut({
			opacity: 0.5,
			easing: 'easeOut',
			duration: 200,
			remove: false,
			useDisplay: false,
			callback:function(){
				me.shortcuts.load({
					params:{menuId:menuId,desktopNumber:desktopNumber},
					callback: function(records, operation, success) {
				       if(success){
				    	   me.parentId = menuId;
				    	   if(desktopNumber!=-1){
								me.shortcutsView.bindDropTarget();
							}
					        me.shortcutsView.getEl().fadeOut({opacity: 1,
								easing: 'easeOut',duration: 500});
				       }
				    }
				});
			}
		});
    },
    createDataView: function () {
        var me = this;
        me.shortcuts = Ext.create('desktop.store.DesktopMenus',{
        	params:{desktopNumber:"1",menuId:"0"}
        });
        me.parentId = "0";
        return Ext.create('desktop.view.ImageDataView',{
			      overItemCls: 'x-view-over',
			      trackOver: true,
			      desktop:me,
			      itemSelector: me.shortcutItemSelector,
			      store: me.shortcuts,
			      style: {
			          position: 'absolute'
			      },
			      x: 0, y: 0,
			      tpl: new Ext.XTemplate(me.shortcutTpl),
			      listeners:{
			          resize:function(){
			        	  me.initShortcut();
			        	  if(!me.taskbar){
			        		  me.initTaskToolbar();
			        	  }
			          },
			          itemcontextmenu:me.itemcontextmenu
			      }
			    });
    },
    itemcontextmenu:function( dataview, record, item, index, e, eOpts ){
    	e.stopEvent();
    	if(record.get('menuType')!='add' && isSuperAdmin==='true'){
    		this.shortcutMenu.theRecord = record;
        	this.shortcutMenu.showAt(e.getXY());
    	}
    },
    createShortcutMenu:function(){
    	var me = this;
        return {
            defaultAlign: 'br-tr',
            items: [
                {iconCls: 'icon-update',text: '编辑菜单',disabled:isSuperAdmin!='true', handler: me.editDesktopMenu, scope: me },
                {iconCls: 'icon-remove',text: '删除菜单',disabled:isSuperAdmin!='true', handler: me.delDesktopMenu, scope: me }
            ]
        };
    },
    editDesktopMenu:function(){
    	var me = this;
    	var record = me.shortcutMenu.theRecord;
    	var id = record.getId()+"edit";
    	var win = me.getWindow(id);
        if(!win){
        	var viewIndex = Ext.create('desktop.comp.DesktopMenuEdit',{
        		menuData:record,
        		desktop:me,
        		action:'update'
        	});
            win = me.createWindow({
                id:id,
                title:'编辑('+record.get('menuName')+")菜单信息",
                width:800,
                height:600,
                icon: record.data.image48,
                animCollapse:false,
                constrainHeader:true,
                layout: 'fit',
                items: [viewIndex]
            });
        }
        if (win) {
            me.restoreWindow(win);
        }
        
    },
    delDesktopMenu:function(){
    	var me = this;
    	Ext.MessageBox.confirm('警告', '确定要进行删除操作?', function(button){
			if (button == "yes") {  
				console.log('delete:' ,me.shortcutMenu.theRecord);
				me.shortcuts.remove(me.shortcutMenu.theRecord);
				me.shortcuts.sync({
					success : function (batch,options,result){
						me.initShortcut();
					}
				}) 
			}
		});
    },
    createDesktopMenu: function () {
        var me = this, ret = {
            items: me.contextMenuItems || []
        };

        if (ret.items.length) {
            ret.items.push('-');
        }

        ret.items.push(
        		{ text: '新建文件夹', handler: me.createFolder, scope: me},
                { text: '平铺窗口', handler: me.tileWindows, scope: me, minWindows: 1 },
                { text: '级联窗口', handler: me.cascadeWindows, scope: me, minWindows: 1 })
                

        return ret;
    },
    createFolder:function(){
    	var me = this;
    	
    	$(".x-view-over").removeClass("x-view-over");
    	
    	var record = Ext.create("desktop.module.DesktopMenu",{
    		menuName:"新建文件夹",
    		menuType:"dir",
    		desktopNumber:me.desktopNumber,
    		menuOrder:0,
    		menuStatus:"x-view-over",
    		image48:contextPath + "desktop/images/folder.png",
    		parentId:me.parentId
    	});
    	me.shortcuts.add(record);
    	me.initShortcut();
    	
    	var shortcut_text = $(".x-view-over .ux-desktop-shortcut-text");
  	  	var shortcut_input = $('<input class="ux-desktop-shortcut-input" value="新建文件夹"/>');
  	  	shortcut_text.hide();
  	  	
  	  	var updateShortcutText = function(){
  	  		shortcut_text.html(shortcut_input.val());
  			shortcut_input.remove();
  			shortcut_text.show();
  			//保存到数据库中
  	  	};
  	  	
  	  	shortcut_input.blur(updateShortcutText);
  	  	shortcut_input.bind('keypress',function(event){
	         if(event.keyCode == "13"){
	        	 updateShortcutText();
	         }
	     });
  	  	$(".x-view-over").append(shortcut_input);
		var runner = new Ext.util.TaskRunner();
		var task = runner.newTask({
		     run: function () {
		    	 shortcut_input.focus().select();
		     },
		     interval: 50,
		     repeat:1
		 });
		task.start();
    },
    createWindowMenu: function () {
    	console.log('Desktop createWindowMenu (9)');
        var me = this;
        return {
            defaultAlign: 'br-tr',
            items: [
                {iconCls: 'restore',text: '还原', handler: me.onWindowMenuRestore, scope: me },
                {iconCls: 'minimize',text: '最小化', handler: me.onWindowMenuMinimize, scope: me },
                {iconCls: 'maximize',text: '最大化', handler: me.onWindowMenuMaximize, scope: me },
                '-',
                {iconCls: 'close',text: '关闭', handler: me.onWindowMenuClose, scope: me }
            ],
            listeners: {
                beforeshow: me.onWindowMenuBeforeShow,
                hide: me.onWindowMenuHide,
                scope: me
            }
        };
    },

    //------------------------------------------------------
    // Event handler methods

    onDesktopMenu: function (e) {
        var me = this, menu = me.contextMenu;
        e.stopEvent();
        if (!menu.rendered) {
            menu.on('beforeshow', me.onDesktopMenuBeforeShow, me);
        }
        menu.showAt(e.getXY());
        menu.doConstrain();
    },

    onDesktopMenuBeforeShow: function (menu) {
        var me = this, count = me.windows.getCount();

        menu.items.each(function (item) {
            var min = item.minWindows || 0;
            item.setDisabled(count < min);
        });
    },
    /**
     * 桌面图标点击处理
     */
    onShortcutItemClick: function (dataView, record,item) {
        var me = this;
        var menuId = record.data.menuId;
        var menuType = record.data.menuType;
//        var shortcut_text = $(item).find(".ux-desktop-shortcut-text");
        var shortcut_input = $(item).find(".ux-desktop-shortcut-input");
//        shortcut_text.hide();
        shortcut_input.show().focus().select();
        
        return;
        if(menuType=='dir'){//进入下一级
    		me.switchDesktopMenu(record.data.menuId,record.data.desktopNumber);
    	}else if(menuType=='return'){//返回上一级
    		me.switchDesktopMenu(record.data.parentId,record.data.desktopNumber);
    	}else{//弹出窗口信息
    		var win = me.getWindow(menuId);
            if(!win && record.data.menuVal){
            	//判断是url还是viewClass
            	var viewItem;
            	if(menuType=='viewClass' || menuType == 'add'){
            		viewItem = Ext.create(record.data.menuVal,{
                		menuData:record,
                		desktop:me
                	});
            	}else if(menuType=='url'){
            		viewItem = Ext.create("Ext.panel.Panel",{
            			html:' <iframe scrolling="auto" frameborder="0" width="100%" height="100%" src="'+record.data.menuVal+'"> </iframe>'
                	});
            	}
            	var height = record.data.showHeight || 600;
            	var width = record.data.showWidth || 800;
            	var bodyHeight = Ext.getBody().getHeight();
            	var bodyWidth = Ext.getBody().getWidth();
            	if(height>bodyHeight){
            		height = bodyHeight - 50;
            	}
            	if(width > bodyWidth){
            		width = bodyWidth - 20;
            	}
                win = me.createWindow({
                    id:menuId,
                    title:record.data.menuName,
                    width:width,
                    height:height,
                    icon: record.data.image48,
                    animCollapse:false,
                    constrainHeader:true,
                    items: [viewItem]
                });
            }
            if (win) {
                me.restoreWindow(win);
            }
    	}
    },

    onWindowClose: function(win) {
        var me = this;
        me.windows.remove(win);
        me.taskbar.removeTaskButton(win.taskButton);
    },

    //------------------------------------------------------
    // Window context menu handlers

    onWindowMenuBeforeShow: function (menu) {
        var items = menu.items.items, win = menu.theWin;
        items[0].setDisabled(win.maximized !== true && win.hidden !== true); // Restore
        items[1].setDisabled(win.minimized === true); // Minimize
        items[2].setDisabled(win.maximized === true || win.hidden === true); // Maximize
    },

    onWindowMenuClose: function () {
        var me = this, win = me.windowMenu.theWin;

        win.close();
    },

    onWindowMenuHide: function (menu) {
        Ext.defer(function() {
            menu.theWin = null;
        }, 1);
    },

    onWindowMenuMaximize: function () {
        var me = this, win = me.windowMenu.theWin;

        win.maximize();
        win.toFront();
    },

    onWindowMenuMinimize: function () {
        var me = this, win = me.windowMenu.theWin;

        win.minimize();
    },

    onWindowMenuRestore: function () {
        var me = this, win = me.windowMenu.theWin;
        me.restoreWindow(win);
    },

    //------------------------------------------------------
    // Dynamic (re)configuration methods

    getWallpaper: function () {
        return this.wallpaper.wallpaper;
    },

    setTickSize: function(xTickSize, yTickSize) {
        var me = this,
            xt = me.xTickSize = xTickSize,
            yt = me.yTickSize = (arguments.length > 1) ? yTickSize : xt;

        me.windows.each(function(win) {
            var dd = win.dd, resizer = win.resizer;
            dd.xTickSize = xt;
            dd.yTickSize = yt;
            resizer.widthIncrement = xt;
            resizer.heightIncrement = yt;
        });
    },

    setWallpaper: function (wallpaper, stretch) {
        this.wallpaper.setWallpaper(wallpaper, stretch);
        return this;
    },

    //------------------------------------------------------
    // Window management methods

    cascadeWindows: function() {
        var x = 0, y = 0,
            zmgr = this.getDesktopZIndexManager();

        zmgr.eachBottomUp(function(win) {
            if (win.isWindow && win.isVisible() && !win.maximized) {
                win.setPosition(x, y);
                x += 20;
                y += 20;
            }
        });
    },

    createWindow: function(config, cls) {
        var me = this, win, cfg = Ext.applyIf(config || {}, {
                stateful: false,
                isWindow: true,
                minimizable: true,
                maximizable: true,
                bodyStyle:'background:#FFF',
                layout: 'fit'
            });
        cls = cls || Ext.window.Window;
        win = me.add(new cls(cfg));
        win.taskButton = me.taskbar.addTaskButton(win);
        me.windows.add(win);
        //win.animateTarget = win.taskButton;
        win.on({
            activate: me.onActiveWindow,
            minimize: me.minimizeWindow,
            scope: me
        });

        win.on({
            boxready: function () {
                win.dd.xTickSize = me.xTickSize;
                win.dd.yTickSize = me.yTickSize;

                if (win.resizer) {
                    win.resizer.widthIncrement = me.xTickSize;
                    win.resizer.heightIncrement = me.yTickSize;
                }
            },
            single: true
        });

        // replace normal window close w/fadeOut animation:
        win.doClose = function ()  {
        	me.onWindowClose(win);
            win.doClose = Ext.emptyFn; // dblclick can call again...
            win.el.disableShadow();
            win.el.fadeOut({
                listeners: {
                    afteranimate: function () {
                        win.destroy();
                    }
                }
            });
        };

        return win;
    },

    getActiveWindow: function () {
        var win = null,
            zmgr = this.getDesktopZIndexManager();

        if (zmgr) {
            // We cannot rely on activate/deactive because that fires against non-Window
            // components in the stack.

            zmgr.eachTopDown(function (comp) {
                if (comp.isWindow && !comp.hidden) {
                    win = comp;
                    return false;
                }
                return true;
            });
        }

        return win;
    },

    getDesktopZIndexManager: function () {
        var windows = this.windows;
        // TODO - there has to be a better way to get this...
        return (windows.getCount() && windows.getAt(0).zIndexManager) || null;
    },

    getWindow: function(id) {
        return this.windows.get(id);
    },

    minimizeWindow: function(win) {
        win.minimized = true;
        win.hide();
    },

    restoreWindow: function (win) {
        if (win.isVisible()) {
            win.restore();
            win.toFront();
        } else {
        	console.log("show item");
        	win.show(null,function(){
        		if(win.items.length>0){
        			var item = win.items.items[0];
                    if(item.showCallback){
                    	item.showCallback();
                    }
        		}
           });
        }
        return win;
    },

    tileWindows: function() {
        var me = this, availWidth = me.body.getWidth(true);
        var x = me.xTickSize, y = me.yTickSize, nextY = y;
        me.windows.each(function(win) {
            if (win.isVisible() && !win.maximized) {
                var w = win.el.getWidth();

                // Wrap to next row if we are not at the line start and this Window will
                // go off the end
                if (x > me.xTickSize && x + w > availWidth) {
                    x = me.xTickSize;
                    y = nextY;
                }

                win.setPosition(x, y);
                x += w + me.xTickSize;
                nextY = Math.max(nextY, y + win.el.getHeight() + me.yTickSize);
            }
        });
    },
    onActiveWindow:function(activeWindow){
    	var me = this;
    	 var last = me.lastActiveWindow;
    	 if (last) {
    		 last.active = false;
    	 }
    	 activeWindow.active = true;
    	 activeWindow.minimized = false;
    	 me.lastActiveWindow = activeWindow;
    	 this.taskbar.setActiveButton(activeWindow.taskButton);
    }
});
