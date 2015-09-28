Ext.define('system.store.Users', {
    extend: 'Ext.ux.data.Store',
    model: 'system.model.User',
	sorters:[{
        property : 'username',
        direction: 'ASC'
    }]
});