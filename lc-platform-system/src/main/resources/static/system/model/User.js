Ext.define('system.model.User', {
    extend: 'Ext.ux.data.Model',
    idProperty:'userId',
    fields:[
      {name:'userId',type:'string'},
      {name:'xm',type:'string'},
      {name:'username',type:'string'},
      {name:'email',type:'string'},
      {name:'password',type:'string'},
      {name:'deptInfo',type:'string',persist:false},
      {name:'accountLocked',type:'string',convert:function(accountLocked,rec){
    	  rec.data["accountLockedStr"] = accountLocked ? "是":"否";
    	  return accountLocked;
	  }},
      {name:'accountLockedStr',persist:false},
      {name:'delStatus',type:'string',convert:function(delStatus,rec){
    	  rec.data["delStatusStr"] = delStatus ? "是":"否";
    	  return delStatus;
	  }},
	  {name:"superAdmin"},
      {name:'delStatusStr',persist:false},
      {name:'deptInfo',type:'string',persist:false},
      {name:'roleInfo',type:'string',persist:false}
    ],
    proxy:{
        type: 'ajax',
        api: {
        	read:contextPath+'/system/users',
			create:contextPath+'/system/users/create',
			update:contextPath + '/system/users/update',
			destroy:contextPath+'/system/users/delete'
		}
	}
});