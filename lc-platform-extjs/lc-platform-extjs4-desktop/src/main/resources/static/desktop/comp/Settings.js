/*!
 * Ext JS Library 4.0
 * Copyright(c) 2006-2011 Sencha Inc.
 * licensing@sencha.com
 * http://www.sencha.com/license
 */

Ext.define('desktop.comp.Settings', {
    extend: 'Ext.window.Window',
    uses: [
        'Ext.tree.Panel',
        'Ext.tree.View',
        'Ext.form.field.Checkbox',
        'Ext.layout.container.Anchor',
        'Ext.layout.container.Border',

        'desktop.view.Wallpaper',

        'desktop.module.WallpaperModel'
    ],
    layout: 'anchor',
    title: '桌面背景设置',
    modal: true,
    width: 640,
    height: 480,
    border: false,
    iconCls:'settings',
    cls:'ux-desktop-Settings',
    bodyStyle:'background:#FFF',
    initComponent: function () {
        var me = this;

        me.selected = me.desktop.getWallpaper();
        me.stretch = me.desktop.wallpaper.stretch;

        me.preview = Ext.create('widget.wallpaper');
        me.preview.setWallpaper(me.selected);
        me.tree = me.createTree();
        
        var btnStr = '<a class="btn btn-success fileinput-button" style="width: 78px !important;left: 294px;">'+
        			'<div class="ico-input">上传图片</div>'+
        			'<input type="file" id="globalBackImage" name="globalBackImage"></a>';
	
        me.buttons = [
            { html:btnStr, handler: me.onUpload,scope: me },
            { text: '删除图片', handler: me.onDelete, iconCls:'icon-remove',scope: me },
            { text: '确定', handler: me.onOK, iconCls:'icon-ok',scope: me },
            { text: '取消', handler: me.close,iconCls:'icon-cancel', scope: me }
        ];

        me.items = [
            {
                anchor: '0 -30',
                border: false,
                layout: 'border',
                items: [
                    me.tree,
                    {
                        xtype: 'panel',
                        region: 'center',
                        layout: 'fit',
                        items: [ me.preview ]
                    }
                ]
            }
            ,
            {
                xtype: 'checkbox',
                boxLabel: '拉伸图像',
                checked: me.stretch,
                listeners: {
                    change: function (comp) {
                        me.stretch = comp.checked;
                    }
                }
            }
        ];
        me.listeners = {
        		afterrender:function(win, eOpts ){ 
        			$('#globalBackImage').fileupload({
        				url:contextPath+'/uploadBackImage',
        				done:function(e,response){  
        					result = Ext.JSON.decode(response.result);
        					if(result.success){
        						var findRecord = me.tree.getRootNode().findChild('text',result.data);
        						if(findRecord){
        							me.tree.getSelectionModel().select(findRecord);
        						}else{
        							var newRecord = Ext.create('desktop.module.WallpaperModel',{
            							img:result.data, text: me.getTextOfWallpaper(result.data), iconCls: '', leaf: true 
            						});
            						 me.tree.getRootNode().appendChild(newRecord);
            						 me.tree.getSelectionModel().select(newRecord);
        						}
        					}else{
        						Ext.ux.Msg.error(result.statusText);
        					}
        					
        			    }  
        			});
        		}
        };
        me.callParent();
    },

    createTree : function() {
        var me = this;

        function child (img) {
            return { img: img, text: me.getTextOfWallpaper(img), iconCls: '', leaf: true };
        }
        me.childs = [];
        Ext.ux.Ajax.request({
        	url:contextPath+"/wallpapers",
        	async:false,
        	method:'post',
        	success: function(response,opts,result){
        		me.childs = result.data;
        	}
        });
        var tree = new Ext.tree.Panel({
            rootVisible: false,
            lines: false,
            autoScroll: true,
            width: 150,
            region: 'west',
            split: true,
            minWidth: 100,
            listeners: {
                afterrender: { fn: this.setInitialSelection, delay: 100 },
                select: this.onSelect,
                scope: this
            },
            
            store: new Ext.data.TreeStore({
                model: 'desktop.module.WallpaperModel',
                root: {
                    text:'Wallpaper',
                    expanded: true,
                    children:me.childs
                }
            })
        });

        return tree;
    },

    getTextOfWallpaper: function (path) {
        var text = path, slash = path.lastIndexOf('/');
        if (slash >= 0) {
            text = text.substring(slash+1);
        }
        var dot = text.lastIndexOf('.');
        text = Ext.String.capitalize(text.substring(0, dot));
        text = text.replace(/[-]/g, ' ');
        return text;
    },

    onOK: function () {
        var me = this;
        if (me.selected) {
        	Ext.ux.Ajax.request({
            	url:contextPath + "/setBakImage",
            	params:{
    		    	filename:me.selRecord.get('img')
    		    },
    			success: function(response,opts,result){
    				Ext.ux.Msg.show(result.statusText);
    				 var timstamp = new Date().getTime();    
    				 me.desktop.setWallpaper(contextPath + '/getBakImage?filename=' + result.data+ '&t=' + timstamp, me.stretch);
    		    }
            });
        }
        me.destroy();
    },
    onDelete:function(){
    	var me = this;
    	var array = me.tree.getSelectionModel().getSelection();
    	if(array.length ==0){
    		Ext.ux.Msg.error("选中一条数据进行删除操作");
    		return;
    	}
    	Ext.ux.Ajax.request({
        	url:contextPath + "/deleteImage",
        	params:{
		    	filename:array[0].get('img')
		    },
			success: function(response,opts,result){
				Ext.ux.Msg.show(result.statusText);
				me.tree.getRootNode().removeChild(array[0]);
		    }
        });
    	
    },
    onSelect: function (tree, record) {
        var me = this;

        if (record.data.img) {
        	var timstamp = new Date().getTime();    	
            me.selected = encodeURI(contextPath + '/getBakImage?filename=' + record.data.img+ '&t=' + timstamp);
        } else {
            me.selected = Ext.BLANK_IMAGE_URL;
        }
        me.selRecord = record;
        me.preview.setWallpaper(me.selected);
    },

    setInitialSelection: function () {
        var s = this.desktop.getWallpaper();
        console.log('setInitialSelection');
        if (s) {
           var path = this.getTextOfWallpaper(s);
            this.tree.selectPath("/Wallpaper/当前背景", 'text');
        }
    }
});
