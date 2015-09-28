Ext.define('system.model.Dept', {
	extend : 'Ext.ux.data.Model',
	fields : [{name:'id',type:'string'}, 'deptName', 'deptCode', 
	   'deptOrder','bz',
	   {name:'iconCls',persist:false},
	   {name:'parentId',type:'string'},
	   {name:'text',persist:false},
	   {name:'parentName',persist:false}
	],
	idProperty:'id',
	proxy:{
        type: 'ajax',
        api: {
        	read:contextPath+'/system/depts',
			create:contextPath+'/system/depts/create',
			update:contextPath + '/system/depts/update',
			destroy:contextPath+'/system/depts/delete'
		}
	}
});