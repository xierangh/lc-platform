Ext.define('desktop.view.ThemeToolbar',{ 
    extend: 'Ext.toolbar.Toolbar', 
	xtype:'themeToolbar',
	rtl: false,
	cls:'ui-theme-toolbar',
	floating: true,
	border:false,
	fixed: true,
	draggable: {
		constrain: true
	},
	shadow:false,
	width:145,
	height:30,
	constraintInsets: '0 -' + (Ext.getScrollbarSize().width + 4) + ' 0 0',
    initComponent : function(){ 
    	var me = this;
    	var store = Ext.create('Ext.data.Store', {
			fields: ['value', 'name'],
			data : [
				{ value: 'blue', name: '蓝色' },
				{ value: 'green', name: '绿色' },
				{ value:'orange',name:'橙色'},
				{ value:'red',name:'红色'}
			]
		});
    	var setThemeCss = function(value){
    		var helpLink = Ext.query('link')[0];
			helpLink.href = themePath + '/packages/ext-theme-'+value+'/build/resources/ext-theme-'+value+'-all.css';
			var date = new Date();
			date.setMonth(date.getMonth()+1);
			Ext.util.Cookies.set("themeTypeValue",value,date);
    	};
    	var themeTypeValue = Ext.util.Cookies.get("themeTypeValue");
    	if(themeTypeValue==null){
    		themeTypeValue = "bule";
    	}
    	var record = store.findRecord("value",themeTypeValue);
    	if(record==null){
    		themeTypeValue = "blue";
    	}
		var themeTypeComBox = Ext.create('Ext.form.field.ComboBox',{
			width: 110,
			cls:'ui-theme-combo',
			displayField:'name',
			valueField: 'value',
			store:store,
			value:themeTypeValue,
			listeners: {
				select: function(combo) {
					var theme = combo.getValue();
					setThemeCss(theme);
				}
			}
		});
		setThemeCss(themeTypeValue);
    	me.items = [themeTypeComBox];
        me.callParent(arguments); 
    } 
}) 
