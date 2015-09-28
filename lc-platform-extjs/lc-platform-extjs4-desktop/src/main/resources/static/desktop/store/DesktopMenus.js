Ext.define('desktop.store.DesktopMenus', {
    extend: 'Ext.ux.data.Store',
    model: 'desktop.module.DesktopMenu',
    remoteSort:false,
    autoLoad:true,
	sorters:[{
        property : 'menuOrder',
        direction: 'ASC'
    }]
});