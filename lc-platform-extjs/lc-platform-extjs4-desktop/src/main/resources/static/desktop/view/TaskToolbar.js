Ext.define('desktop.view.TaskToolbar',{ 
    extend: 'Ext.toolbar.Toolbar', 
	rtl: false,
	height:66,
	fixed: true,
	layout:{
		 type: 'hbox',
		 pack:'end'
	},
	shadow:false,
	floating: true,
	border:false,
	cls:'ux-desktop-task-toolbar',
    initComponent : function(){ 
    	var me = this;
        me.callParent(arguments); 
    },
    afterLayout: function () {
        var me = this;
        me.callParent();
        me.el.on('contextmenu', me.onButtonContextMenu, me);
    },
    addTaskButton: function(win) {
    	var me = this;
    	//判断icon是否存在
    	var item = Ext.create('Ext.view.View', {
    		win:win,
			tpl: '<a href="#" class="nor"><img src="'+win.icon+'" /><span>'+Ext.util.Format.ellipsis(win.title, 20)+'</span></a>',
		    cls:'desktop_task_item',
		    listeners: {
		    	afterrender:function(view, eOpts ){
		    		view.el.on('click',function(){
			        	me.onWindowBtnClick(view);
			        });
                }
            }
		});
        var cmp = this.add(item);
        return cmp;
    },
    onWindowBtnClick: function (btn) {
        var win = btn.win;
        if (win.minimized || win.hidden) {
            win.show();
        } else if (win.active) {
            win.minimize();
        } else {
           win.toFront();
        }
    },
    getWindowBtnFromEl: function (el) {
        var c = this.getChildByElement(el);
        return c || null;
    },
    setActiveButton: function(btn) {
    	var me = this;
    	if(me.curItem){
			Ext.fly(me.curItem).removeCls('cur').addCls('nor');
		}
    	var list = Ext.query('.nor',btn.el.dom);
    	if(list.length==1){
    		var item = list[0];
    		Ext.fly(item).removeCls('nor').addCls('cur');
    		me.curItem = item;
    	}
    },
    removeTaskButton: function (btn) {
        var found, me = this;
        me.items.each(function (item) {
            if (item === btn) {
                found = item;
            }
            return !found;
        });
        if (found) {
            me.remove(found);
        }
        return found;
    },
    onButtonContextMenu: function (e) {
        var me = this, t = e.getTarget(), btn = me.getWindowBtnFromEl(t);
        e.stopEvent();
        if (btn) {
            me.windowMenu.theWin = btn.win;
            me.windowMenu.showBy(t);
        }
    }
}) 
