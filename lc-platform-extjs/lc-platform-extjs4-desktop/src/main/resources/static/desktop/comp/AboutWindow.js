/*!
* Ext JS Library 4.0
* Copyright(c) 2006-2011 Sencha Inc.
* licensing@sencha.com
* http://www.sencha.com/license
*/

// From code originally written by David Davis (http://www.sencha.com/blog/html5-video-canvas-and-ext-js/)

Ext.define('desktop.comp.AboutWindow', {
    extend: 'desktop.view.Module',
//
//    uses: [
//        'desktop.view.Video'
//    ],

    id:'desktop_about',
    windowId: 'about-window',
    tipWidth: 160,
    tipHeight: 96,

    init : function(){
        this.launcher = {
            text: '关于系统',
            iconCls:'about'
        }
    },

    createWindow : function(){
        var me = this, desktopComp = me.app.getDesktop(),
        win = desktopComp.getWindow(me.windowId);
        if (!win) {
            win = desktopComp.createWindow({
                id: me.windowId,
                title: '关于系统',
                width: 740,
                height: 480,
                iconCls: 'about',
                animCollapse: false,
                border: false,
                layout: 'fit',
                html:'系统综合管理平台',
                items: [
                ]
            });
        }
        return win;
    },

    onTooltipRender: function (tip) {
        // get the canvas 2d context
        var el = tip.body.dom, me = this;
        me.ctx = el.getContext && el.getContext('2d');
    },

    renderPreview: function() {
        var me = this;

        if ((me.tip && !me.tip.isVisible()) || !me.videoEl) {
            return;
        }

        if (me.ctx) {
            try {
                me.ctx.drawImage(me.videoEl, 0, 0, me.tipWidth, me.tipHeight);
            } catch(e) {};
        }

        // 20ms to keep the tooltip video smooth
        Ext.Function.defer(me.renderPreview, 20, me);
    }
});
