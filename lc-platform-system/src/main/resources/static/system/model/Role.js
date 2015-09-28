Ext.define('system.model.Role', {
    extend: 'Ext.ux.data.Model',
    idProperty:'id',
    fields:[
      {name:'id'},
      {name:'roleName',type:'string'},
      {name:'roleDesc',type:'string'},
      {name:'grant'}
    ],
    proxy:{
        type: 'ajax',
        api: {
        	read:contextPath+'/system/roles',
			create:contextPath+'/system/roles/create',
			update:contextPath + '/system/roles/update',
			destroy:contextPath+'/system/roles/delete'
		}
	}
});