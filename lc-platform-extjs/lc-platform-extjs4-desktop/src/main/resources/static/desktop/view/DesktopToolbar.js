/**
 * 桌面系统工具条设计,目前提供了头像展示，桌面翻页，退出功能
 * 默认显示在顶部居中位置
 */
Ext.define('desktop.view.DesktopToolbar',{ 
    extend: 'Ext.toolbar.Toolbar', 
	xtype:'desktopToolbar',
	rtl: false,
	floating: true,
	border:false,
	fixed: true,
	draggable: {
		constrain: true
	},
	style: 'background:none', 
	shadow:false,
	width:248,
	height:50,
	constraintInsets: '0 -' + (Ext.getScrollbarSize().width + 4) + ' 0 0',
    initComponent : function(){ 
    	var me = this;
    	me.html = '<div id="navbar" class="no_sysbtn">'+
    	'<div class="indicator_wrapper">'+
     	'<div class="indicator_container nav_current_1" id="global_indicatorContainer">'+
     	'<div class="indicator indicator_header" id="navbarHeaderImg" title="('+global_xm+')点击查看详情">'+
     	'<img src="desktop/images/48.gif" id="global_userHeadlogo" class="indicator_header_img"></div>'+
     	'<a class="indicator indicator_1" href="#" title="桌面一""><span class="indicator_icon_bg"></span><span class="indicator_icon indicator_icon_1">1</span></a>'+
     	'<a class="indicator indicator_2" href="#" title="桌面二""><span class="indicator_icon_bg"></span><span class="indicator_icon indicator_icon_2">2</span></a>'+
     	'<a class="indicator indicator_3" href="#" title="桌面三""><span class="indicator_icon_bg"></span><span class="indicator_icon indicator_icon_3">3</span></a>'+
     	'<a class="indicator indicator_4" href="#" title="桌面四""><span class="indicator_icon_bg"></span><span class="indicator_icon indicator_icon_4">4</span></a>'+
     	'<a class="indicator indicator_5" href="#" title="桌面五""><span class="indicator_icon_bg"></span><span class="indicator_icon indicator_icon_5">5</span></a>'+
     	'<a class="indicator indicator_search" href="#" title="安全退出"></a>'+
     	'<a class="indicator indicator_manage" href="#" title="所有图标"></a></div>'+
     	'</div></div>';
    	
    	//桌面点击事件处理
    	var doDesktopNumberClick = function(){
			var desktopNumber  = this.innerHTML;
			Ext.get('global_indicatorContainer').set({'class':'indicator_container nav_current_'+desktopNumber});
			var desktop = me.desktop;
			var shortcuts = desktop.shortcuts;
			if(desktop.desktopNumber == desktopNumber){
				return;
			}
			desktop.desktopNumber = desktopNumber;
			desktop.switchDesktopMenu("0",desktopNumber);
		};
    	
    	me.listeners = {
    			afterrender:function(view){
    				userHeadlogoImage = Ext.get('global_userHeadlogo');
    				userHeadlogoImage.set({'src':userHeadImage24});
    				var list = Ext.query('.indicator_container .indicator_icon',me.el.dom);
    				for(var i=0;i<list.length;i++){
    					var item = list[i];
    					item.onclick = doDesktopNumberClick;
    				}
    				
    				var array = Ext.query('.indicator_container .indicator_search',me.el.dom);
    				if(array.length==1){
    					array[0].onclick = function(e){
    						Ext.Msg.confirm('退出系统', '你确定要退出系统吗?', function(op){
    				        	if(op == "yes"){
    				        		var location = window.location;
    								window.location.replace ("http://"+location.host+contextPath + "/logout");  
    				        	}
    				        });
        				}
    				}
    				
    				var array = Ext.query('.indicator_container .indicator_manage',me.el.dom);
    				if(array.length==1){
    					array[0].onclick = function(e){
    						Ext.get('global_indicatorContainer').set({'class':'indicator_container nav_current_6'});
    						var desktop = me.desktop;
    						if(desktop.desktopNumber != -1){
    							desktop.desktopNumber = -1;
    							desktop.switchDesktopMenu("0",desktop.desktopNumber);
    						}
        				}
    				}
    				//初始化图片事件
    				Ext.get('navbarHeaderImg').on('click',  
						function(e){  
							var id = 'userInfoManager';
							var win = me.desktop.getWindow(id);
					        if(!win){
					        	var userInfoManager = Ext.create('system.view.UserInfoManager',{
					        		desktop:me.desktop
					        	});
					        	var userIcon = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAMAAABg3Am1AAAABGdBTUEAALGPC/xhBQAAACBjSFJNAAB6JgAAgIQAAPoAAACA6AAAdTAAAOpgAAA6mAAAF3CculE8AAACr1BMVEUAAACRZ4n5DP+dXZuhWaGcXpmjV6R7QXx7S3l7RHt7SXl+ALh7WHSdXJugWp+gWqChWaCgWqCaX5efWp6gWqChWaGhWaGgWqChWaGhWaGfWp+gWqCfWp+hWaGgWp+gWqB7RXt7RXt7RXt7RHt7Rnp7RXt7SXl7RHt7Rnp7RHt7RXt7THh7RHt7RHt7RHt7RXt7Rnp7RXt7RXt7RXt7Rnp7SXmhWaGgV6CkXqSwdLC4grivcq+jXaOgWKDAkcDjzuP28Pb8+fz17vXgyeC8ibyjXKOhWKGnZKfXutf8+vz////69/rTstOlYaWkX6TYutj+/v7+/f7Rr9GhW6HEl8T49fiyhLKeVp6oZajq2+rYyNiRVJG7h7v7+fv18vWXa5eNTY2gWaDRrtGykrJ6QnqPT4+iW6LfyN/Hr8d6Q3p8RXyMTYyfWJ/o1ujKtMp7Q3t7RHuLTYvm0ubJs8nZvdm/pb+gVqDJoMn9/P2ngqd5QXmNTo2zebP38fft5u2MXYzbwdvCqcKyd7Ly6PLl2uWMXIy9jL2dc525hbnk1eT8+/z6+PrZydmXbJefYJ+rhKvIssjcztzbzNvGr8aifKKBTYGUUJR/Rn9/Sn9/SX95QnmyeLKze7OVUpV6RHqfd5+acJqBTIG2f7bexd748vju5O6ocaifVp+bcZvy7fLZytmheqF9Rn2MToyiXKLczNyiZ6KUaJTm2+b39PfBp8GAS4DGmsb7+Pvk1uS4h7ilYKWFUIXo3+i0lbStb63w5vDl0eXMpsy/kL/BksHOqs7j0+OPYI/9/f3fx9/SwNKmY6br3Ovj2OODT4OsbKzz6vPv6O+KWYqsbaz06/Tv6e+LWouqaqrt4u37+vv6+frq4eqJWImnc6ecc5yccpyWapZ+SH6JSol8RHyKTIoAAADmn6l7AAAANnRSTlMAAAAAAAAAAAAAAAAAGmSt2vcHWsr6DYz1+1vJZfmt2/jcrfllyRv1W/uMB/YN+sta99uuZBo8REdhAAAAAWJLR0QAiAUdSAAAAAlwSFlzAAAASAAAAEgARslrPgAAAp1JREFUSMdjYGBgYGTi5eMXEDTDAwQF+Pl4mRgZwIBZSFhE1IwgEBURFmIGqWcRE5cgrBwEJMTFWBgYWIXEJYlTb2YmKS7EysAmRaT5YDuk2Bh4pYlXb2YmzcsgI0uKBlkZBjmsEuZAgFVCjkEem3ILSysraxtsWuQZMOPL1sLO3sHR0cnZxdUNQ1KQAVO9u4enFxh4+/hi6sDQ4ObnH+AFBYFBwYQ1mId4esFBaFg4IQ1uEZFeSCAq2paABvOYWGQNcfEJiQQ0JHmhgOSUVFu8GtLSUTVkZGZl5+DT4JeLqiEvv6CgMAePBosiVA3FmQWoOjCCtQRVQ2lKAaoOdA1l5SjqKyqrQBoKqhNxaTCvqUXWUFefCdaQlZqIy0muDcgaGgugIAsWuphJo6kZob6lNROuAxq6WFJrmxPcQe0pBQgA8TmmBjPbjs4ukPLunt7MggJ0HVg0mJn39U+YOGnylKkpBQUYOjD9AMyYbtOmz5iZWQWM5FmzUHQAQxdNg7nN7DmgAJw7fR7Yp/MXLETWAgwrNA2LFi9Zumw5WAdQvmrFytBVq9fkI+lYi6JhXbkD0LPrN0B0ZFZt3ATkbt6yFUlHAbIG223bwaG5Y+eusuVl03ZX7gFzu/bi0GC7bz80/L0PHDx0+MjRblh0HEvBqsEcOd0dD0Ti7K3CqiHthBcOcHINNg1up07j0nDmbD4WDbbnzuPScOFiJjYNly7j0nDlKpIGeGFse+36jZtYgeet23ANCkjF/bo7d+9hBXfvP4BboIhcoYQ/rMIOkJKTEkqVlVNYQAAoq6BWigR1qKqhVbsEdKhrsKNX7Hh1aGppc2A0HfDo0NHS5cTSOMnJfoRVuZ6+gTYXpDmD1vzJeYyp2tDI2MSUmweoGABLjbswGMSi2gAAACV0RVh0ZGF0ZTpjcmVhdGUAMjAxNC0wOC0wM1QwOTo1NDoyNyswODowMLuucnQAAAAldEVYdGRhdGU6bW9kaWZ5ADIwMTQtMDgtMDNUMDk6NTQ6MjcrMDg6MDDK88rIAAAATXRFWHRzb2Z0d2FyZQBJbWFnZU1hZ2ljayA2LjguOC03IFExNiB4ODZfNjQgMjAxNC0wMi0yOCBodHRwOi8vd3d3LmltYWdlbWFnaWNrLm9yZ1mkX38AAAA+dEVYdHN2Zzpjb21tZW50ACBDcmVhdGVkIHdpdGggSW5rc2NhcGUgKGh0dHA6Ly93d3cuaW5rc2NhcGUub3JnLykg/kVHswAAABh0RVh0VGh1bWI6OkRvY3VtZW50OjpQYWdlcwAxp/+7LwAAABh0RVh0VGh1bWI6OkltYWdlOjpIZWlnaHQANDczrxKupgAAABd0RVh0VGh1bWI6OkltYWdlOjpXaWR0aAA0NzM84/77AAAAGXRFWHRUaHVtYjo6TWltZXR5cGUAaW1hZ2UvcG5nP7JWTgAAABd0RVh0VGh1bWI6Ok1UaW1lADE0MDcwMzA4Njdo03AmAAAAE3RFWHRUaHVtYjo6U2l6ZQAyOC45S0JCMt4dwwAAAGJ0RVh0VGh1bWI6OlVSSQBmaWxlOi8vL2hvbWUvZnRwLzE1MjAvZWFzeWljb24uY24vZWFzeWljb24uY24vY2RuLWltZy5lYXN5aWNvbi5jbi9wbmcvMTE3NDEvMTE3NDExOC5wbmfnHDY0AAAAAElFTkSuQmCC";
					            win = me.desktop.createWindow({
					                id:id,
					                title:'用户信息',
					                width:800,
					                height:600,
					                animCollapse:false,
					                constrainHeader:true,
					                layout: 'fit',
					                icon: userIcon,
					                items: [userInfoManager]
					            });
					        }
					        if (win) {
					        	me.desktop.restoreWindow(win);
					        }
						}  
					); 
    			}
    	};
        me.callParent(arguments); 
    } 
}) 
