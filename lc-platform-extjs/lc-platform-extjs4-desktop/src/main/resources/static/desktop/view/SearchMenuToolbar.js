Ext.define('desktop.view.SearchMenuToolbar',{ 
    extend: 'Ext.toolbar.Toolbar', 
	xtype:'searchMenuToolbar',
	rtl: false,
	floating: true,
	border:false,
	fixed: true,
	shadow:false,
	style: 'background:none', 
	width:166,
	height:38,
	constraintInsets: '0 -' + (Ext.getScrollbarSize().width + 4) + ' 0 0',
    initComponent : function(){ 
    	var me = this;
    	me.html = '<div class="pagelet_search_bar" style="display: block;">'+
    		'<input class="pagelet_search_input" onblur="if(this.value ==\'\'){this.value=\'关键词搜索...\'};" onfocus="if(this.value == \'关键词搜索...\'){this.value=\'\'};" value="关键词搜索...">'+
    		'<input class="pagelet_search_button" title="搜索">'
    		'</div>';
    	me.draggable = {
    		constrain: true,
    		listeners:{
    			mousedown:function( item, e, eOpts ){
    				var list = Ext.query('.pagelet_search_input',me.el.dom);
    				if(list.length==1){
    					var item = list[0];
    					item.focus();
    				}
    			}
    		}
    	};
        me.callParent(arguments); 
    } 
}) 
