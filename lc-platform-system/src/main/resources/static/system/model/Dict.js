Ext.define('system.model.Dict', {
	extend : 'Ext.ux.data.Model',
	fields : [{name:'id',type:'string'}, 'codeName', 'numberCode',
	          'codeType','dictOrder','defaultVal','dictDesc',
	          {name:'parentName',type:'string',persist:false}
	],
	idProperty:'id',
	proxy:{
        type: 'ajax',
        api: {
        	read:contextPath+'/system/dicts',
			create:contextPath+'/system/dicts/create',
			update:contextPath + '/system/dicts/update',
			destroy:contextPath+'/system/dicts/delete'
		}
	}
});