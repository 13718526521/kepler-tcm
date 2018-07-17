$(function(){
	var _num = 0;
	$("#addfield").click(function(){
		_num++
		/*$("#field_list").clone(true).appendTo("#field");*/
		var arr=[];
		arr.push(
				'<tr id="field_list1">',
					'<td><input type="text" name="className" class="form-control" placeholder="无" readonly style="background-color: rgb(210,210,210);"></td>',
					'<td>',
					'<label for="file'+_num+'" class="btn btn-primary" style="width: 100px;">请选择文件</label> ',
					'<input type="file" id="file'+_num+'" name="file" style="display: none;"/> ',
					'<span id="plugin_fileName" style="margin-left: 10px;">未选择任何文件</span>',
					'</td>',
				'</tr>'
		)
		$("#field").append(arr.join(''));
		$("#file"+_num).change(function(){
			console.log(_num)
			var file;
			if(this.files && this.files[0]) {
				file = this.files[0];
			} else if(this.files && this.files.item(0)) {
				file = this.files.item(0);
			}
			$(this).parent().find("span").html(file.name);
			var i=file.name.lastIndexOf(".");
			var e="";
			if(i >= 0)
			e=file.name.substring(i).toLowerCase();
			var server=GetQueryString("server");
			serverName=server.split("@")[1];
			var class_path="../servers/"+serverName+'/plugins/classes';
			var jar_path="../servers/"+serverName+'/plugins/lib';
			var path="../servers/"+serverName+'/conf';
			if(e == ".class"){
				$(this).parent().parent().find("input").attr("placeholder",class_path);
				$(this).parent().parent().find("input").attr("value",class_path);
				/*$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().find("input").attr("style","display: none;");*/
			}else if( e == ".jar" ){
				$(this).parent().parent().find("input").attr("placeholder",jar_path);
				$(this).parent().parent().find("input").attr("value",jar_path);
				/*$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().find("input").attr("style","display: none;");*/
			}else{
				$(this).parent().parent().find("input").attr("placeholder",path);
				$(this).parent().parent().find("input").attr("value",path);
				/*$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().find("input").attr("style","display: none;");*/
			}
		});
	});
	
	$("#addfield").click();
	
	$(".submit").click(function(){
		var server=GetQueryString("server");
		var form = new FormData(document.getElementById("pluginForm"));
		form.append("agentAndServer", server);
		 $.ajax({
			 url:util.agent().baseUrl + "/plugin/upload",
			 type:"post",
			 data:form,
			 processData:false,
			 contentType:false,
			 success:function(data){
           	  if(data.CODE==1){
           		  alert(data.MESSAGE);
           	  }else{
           		  layer.msg("添加成功", { time: 1000 });
           		  window.location.href="plugins.html?server="+server;
           	  }
			 },
	      	 error: function() {
	      		 alert('请求失败');
	      	 }
		 });
	});

	$(".reset").click(function(){
		var server=GetQueryString("server");
		window.location.href="plugins.html?server="+server;
	});
	
});
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}