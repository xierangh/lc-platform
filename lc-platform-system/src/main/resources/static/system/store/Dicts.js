Ext.define('system.store.Dicts', {
    extend: 'Ext.data.TreeStore',
    model: 'system.model.Dict',
    nodeParam:'parentId',
    defaultRootId:'0'
});