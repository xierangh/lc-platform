Ext.define('system.store.Roles', {
    extend: 'Ext.ux.data.Store',
    model: 'system.model.Role',
	sorters:[{
        property : 'roleName',
        direction: 'ASC'
    }]
});