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
			me.append('<option value="">所有</option>');
			$.ajax({
			   url: contextPath + "/system/dicts/data",
			   data: {dictId:dictId},
			   success: function(list){
			     for(var i=0;i<list.length;i++){
			    	 var item = list[i];
			    	 me.append('<option value="'+item.numberCode+'">'+item.codeName+'</option>');
			     }
			   }
			});
		}
	});
})(jQuery);
