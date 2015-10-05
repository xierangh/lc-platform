Ext.define('system.store.Menus', {
    extend: 'Ext.ux.data.Store',
    model: 'system.model.Menu',
    remoteSort:false,
    autoLoad:true,
	sorters:[{
        property : 'menuOrder',
        direction: 'ASC'
    }]
});