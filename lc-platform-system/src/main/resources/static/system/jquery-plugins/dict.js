/**
 * jquery dict comp
 * chenjun
**/ 
(function($){
	$.fn.extend({
		bindDict:function(options){
			var dictId = options.dictId;
			if(!dictId)return;
			var me = this;
			$.ajax({
			   url: contextPath + "/system/dicts/data",
			   data: {dictId:dictId},
			   success: function(list){
				 if(list.length==0 || list[0].defaultVal==false){
					 me.append('<option value="">(无)</option>');
				 }
			     for(var i=0;i<list.length;i++){
			    	 var item = list[i];
			    	 me.append('<option value="'+item.numberCode+'">'+item.codeName+'</option>');
			     }
			     if(options.value){
			    	 me.val(options.value);
			     }
			   }
			});
		}
	});
})(jQuery);
