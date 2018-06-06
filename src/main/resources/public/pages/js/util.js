/**
 * 工具方法
 */
var util = new Object();

(function(t_util){
	
	function agent() {
		return {
			baseUrl  : window.location.protocol + '//' + window.location.host + '/'
					+ (window.location.pathname.split('/')[1] ?
							window.location.pathname.split('/')[1] : window.location.pathname.split('/')[2]) ,
			protocol : window.location.protocol,
			host : window.location.host,
			contextPath : (window.location.pathname.split('/')[1] ?
					window.location.pathname.split('/')[1] : window.location.pathname.split('/')[2]),
			
		};
	}
	/**
	 * 获取当前请求cookie 参数 
	 * name 为cookie key 
	 */
	function getCookie(name){
		var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
		if(arr=document.cookie.match(reg))
			return unescape(arr[2]);
		else
			return null;
	}
	
	t_util.getCookie = getCookie;
	
	t_util.agent = agent;
	
	/**
	 * 为请求字符串添加随机码，处理ie中请求只访问一次的问题
	 * @author wangsp
	 * @param url 需要处理的URL
	 */
	function warpRandom(url){
		if(url){
			if(/\?+/.test(url)){
				url = url+"&math="+Math.random();
			}else{
				url = url+"?math="+Math.random();
			}
			return url;
		}
		return null;
	}
	t_util.warpRandom = warpRandom;
	/**
	 * 取中间的毫秒数，再转换成js的Date类型
	 * @author wangsp
	 * @param date 要解析的日期
	 */
	function dateFormatter(date){
		if(typeof(date) !="undefined" && date != null){
			if(date instanceof Date){
				var y = date.getFullYear();
				var m = date.getMonth()+1;
				var d = date.getDate();
				return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d);
			}else if(typeof(date) == "number" || typeof(date) == "string"){
				var value = date+"";
				var date1 = new Date(parseInt(value.replace("/Date(", "").replace(")/", ""), 10));
                //月份为0-11，所以+1，月份小于10时补个0       
                var month = date1.getMonth() + 1 < 10 ? "0" + (date1.getMonth() + 1) : date1.getMonth() + 1;
                var currentDate = date1.getDate() < 10 ? "0" + date1.getDate() : date1.getDate();
                return date1.getFullYear() + "-" + month + "-" + currentDate;
			}else{
				return "";
			}			
		}		
	}
	t_util.dateFormatter = dateFormatter;
	/**
	 * 字符串解析为日期
	 * @author wangsp
	 * @param s 要解析的日期字符串
	 */
	function dateParser(s){
		if (!s) return new Date();
		var ss = s.split('-');
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)){
			return new Date(y,m-1,d);
		} else {
			return new Date();
		}
	}
	
	t_util.dateParser = dateParser;
	/**
	 * 时间解析为字符串
	 * @author wangsp
	 * @param 要解析的时间
	 */
	function datetimeFormatter(date){
		var y = date.getFullYear();
		var m = date.getMonth()+1;
		var d = date.getDate();
		var h = date.getHours();
		var mm = date.getMinutes();
		var s = date.getSeconds();
		return y+'-'+(m<10?('0'+m):m)+'-'+(d<10?('0'+d):d)+' '+(h<10?('0'+h):h)+':'+(mm<10?('0'+mm):mm)+':'+(s<10?('0'+s):s);
	}
	t_util.datetimeFormatter = datetimeFormatter;
	/**
	 * 字符串解析为时间
	 * @author wangsp
	 * @param s 要解析的时间字符串
	 */
	function datetimeParser(s){
		if (!s) return new Date();
		var d = s.split(' ');
		var ss = d[0].split("-");
		var dd = d[1].split(":");
		var y = parseInt(ss[0],10);
		var m = parseInt(ss[1],10);
		var d = parseInt(ss[2],10);
		var h = parseInt(dd[0],10);
		var mm = parseInt(dd[1],10);
		var si = parseInt(dd[2],10);
		if (!isNaN(y) && !isNaN(m) && !isNaN(d)&&!isNaN(h) && !isNaN(mm) && !isNaN(si)){
			return new Date(y,m-1,d,h,mm,si);
		} else {
			return new Date();
		}
	}
	t_util.datetimeParser = datetimeParser;
	
	/**
	 * 字符串解析为时间
	 * @author tbh
	 * @param d 要解析的时间字符串 2017-04-20 11:47:24.0 返回2017-04-20 11:47:24
	 */
	function dateymdhms(d) {
		return d.substr(0,19);
	}
	t_util.dateymdhms = dateymdhms;
	/**
	 * 将文件大小转换成对应的带单位的格式
	 */
	function fileSizeFormatter(val){
		var value = Number(val);
		if(value >= 1073741824){
			return Number(value/1024/1024/1024).toFixed(2)+" GB";
		}else if(value >= 1048576){
			return Number(value/1024/1024).toFixed(2)+" MB";
		}else if(value >= 1024) {
			return Number(value/1024).toFixed(2)+" KB"
		}else if(value == null){
			return "0 Bytes";
		}else{
			return value+" Bytes";
		}
		return value;
	}
	t_util.fileSizeFormatter = fileSizeFormatter;

	
	/**
	 * 替换所有匹配字符串方法
	 * @author wangsp
	 * @param source 原字符串
	 * @param rex 要匹配的正则表达式
	 * @param newChar 替换成的字符串
	 */
	function replaceAll(source,rex,newChar){
		while(source.search(rex)!=-1){
			source = source.replace(rex,newChar);
		}
		return source;
	}
	t_util.replaceAll = replaceAll;
	
	/**
	 * 字符串过长时的截断方法,并加上提示完整信息的标题
	 * @author wangsp
	 * @param str 原字符串
	 * @param len 字符串长度
	 * @param extStr 切去多余长度，替换的字符串
	 */
	function subTextWithTitle(str,len,extStr){
		if(!str){
			return "";
		}
		var resStr="";
		if(str.length+2<len){
			resStr = str;
		}else{
			if(extStr){
				resStr = str.substr(0,len)+extStr;
			}else{
				resStr = str.substr(0,len)+"...";
			}
		}
		return "<span title='"+str+"'>"+resStr+"</span>";
	}
	t_util.subTextWithTitle = subTextWithTitle;
	
	/**
	 * 阻止其他组件事件方法，主要避免组件的冒泡触发
	 * @author wangsp
	 * @param e 触发的事件
	 */
	function stopFn(e){
		if (e && e.stopPropagation) {
			e.stopPropagation();
		} else {
			window.event.cancelBubble = true;
		}
	}
	t_util.stopFn = stopFn;
	
	//设置ajax请求超时方法
	$(document).ajaxError(function(event,request, settings){
		if(request.responseText.indexOf("登陆失效")!=-1){
			top.tipAndReLoad();
		}
	});
	
	/**
	 * 当前loadPage页面加载方法
	 * @author wangsp
	 * @param url 网页链接
	 * @param divId 放入网页的divId
	 * @param callBack 回调方法
	 * @param async 回调方法
	 */
	function loadPage(url, divId, callBack,async) {
		$.ajax({
			url : url,
			async: (async ? async : false),
			dataType : "html",
			success : function(data) {
				$("#" + divId).empty();
				$("#" + divId).append(data);
				$("#" + divId).find(".main").width($("#" + divId).width());
				$("#" + divId).find(".main").height($("#" + divId).height());
				$("#" + divId).find(".progressBar").hide();
				$("#" + divId).css("background-color", "#222222");
				if(callBack){
					callBack.apply();
				}
			}
		});
	}
	t_util.loadPage = loadPage;
	/**
	 * ajax 请求超时
	 * tym170517
	 */
	function ajaxTimeout() {
		$.ajaxSetup({
			contentType:"application/x-www-form-urlencoded;charset=utf-8",
			complete:function(XMLHttpRequest,textStatus){
			//通过XMLHttpRequest取得响应头，sessionstatus 
			var sessionstatus=XMLHttpRequest.getResponseHeader("sessionstatus"); 
				if(sessionstatus != "effective"){
					var refererUrl = document.referrer;
					if(refererUrl.indexOf("view/portals")){
						//window.top.location.href= agent().baseUrl+"/view/portals/login.html"
					}else{
						//window.top.location.href= agent().baseUrl+"/login.html"
					}
					
				}
			}
		});
	}
	t_util.ajaxTimeout = ajaxTimeout;
})(util);

//
////皮肤数据
//var skin = new Vue({
//	el: "#skinApp",
//	data: {
//		skinData: [{ name: 'Cerulean', value: '../base/bootstrap/bootstrap-skin/bootstrap-cerulean.css' },
//			{ name: 'Cyborg', value: '../base/bootstrap/bootstrap-skin/bootstrap-cyborg.css' },
//			{ name: 'Flatly', value: '../base/bootstrap/bootstrap-skin/bootstrap-flatly.css' },
//			{ name: 'Journal', value: '../base/bootstrap/bootstrap-skin/bootstrap-journal.css' },
//			{ name: 'Lumen', value: '../base/bootstrap/bootstrap-skin/bootstrap-lumen.css' },
//			{ name: 'Paper', value: '../base/bootstrap/bootstrap-skin/bootstrap-paper.css' },
//			{ name: 'Readable', value: '../base/bootstrap/bootstrap-skin/bootstrap-readable.css' },
//			{ name: 'Sandstone', value: '../base/bootstrap/bootstrap-skin/bootstrap-sandstone.css' },
//			{ name: 'Simplex', value: '../base/bootstrap/bootstrap-skin/bootstrap-simplex.css' },
//			{ name: 'Slate', value: '../base/bootstrap/bootstrap-skin/bootstrap-slate.css' },
//			{ name: 'Solar', value: '../base/bootstrap/bootstrap-skin/bootstrap-solar.css' },
//			{ name: 'Spacelab', value: '../base/bootstrap/bootstrap-skin/bootstrap-spacelab.css' },
//			{ name: 'United', value: '../base/bootstrap/bootstrap-skin/bootstrap-united.css' }
//		]
//	}
//});