Ext.define('desktop.module.DesktopMenu', {
    extend: 'Ext.ux.data.Model',
    idProperty:'menuId',
    fields: [
       { name: 'menuName'},{name:'menuType'},{name:'menuOrder',type:'int'},
       { name: 'menuId' },{name:'desktopNumber'},
       {name:'showWidth',defaultValue:800},
       {name:'showHeight',defaultValue:600},
       {name:'permCode'},{name:'parentId'},
       {name:'menuVal'},{name:"image48",persist:false},'bz'
    ],
    proxy:{
        type: 'ajax',
        api: {
        	read:contextPath+'/system/menus',
        	create:contextPath+'/system/menus/create',
			update:contextPath + '/system/menus/update',
			destroy:contextPath+'/system/menus/delete'
		}
	}
});
