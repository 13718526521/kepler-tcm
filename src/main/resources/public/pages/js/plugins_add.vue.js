$(function(){
	var _num = 1;
	$("#addfield").click(function(){
		_num++
		/*$("#field_list").clone(true).appendTo("#field");*/
		var arr=[];
		arr.push(
				'<tr id="field_list'+_num+'">',
					'<td><input type="text" name="className" class="form-control" placeholder="无" readonly style="background-color: rgb(210,210,210);"></td>',
					'<td>',
					'<label for="file'+_num+'" class="btn btn-primary" style="width: 100px;">请选择文件</label> ',
					'<input type="file" id="file'+_num+'" name="file" style="display: none;"/> ',
					'<span id="plugin_fileName" style="margin-left: 10px;">未选择任何文件</span>',
					'<button class="btn btn-primary" id="file_del_'+_num+'" onclick="delFile('+_num+')">删除</button>',
					'</td>',
				'</tr>'
		)
		$("#field").append(arr.join(''));
		$("#file"+_num).change(function(){
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
			if(e == ".class"){
				$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().parent().find("input").attr("placeholder","无");
				$(this).parent().find("input").attr("style","display: none;");
			}else if( e == ".jar" ){
				$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().parent().find("input").attr("placeholder","无");
				$(this).parent().find("input").attr("style","display: none;");
			}else{
				$(this).parent().parent().find("input").attr("readOnly",false);
				$(this).parent().parent().find("input").attr("style","");
				$(this).parent().parent().find("input").attr("placeholder","请填写服务器保存路径");
				$(this).parent().find("input").attr("style","display: none;");
			}
		});
	});
	
	/*$("#addfield").click();*/
	
	$(".submit").click(function(){
		var flag=$("#pluginid").attr("checked");
		if(flag==undefined){
			var id=$("#pluginid").val();
			if(id == '' || id == "")
			return;
		}
		for (var i = 1; i <= _num; i++) {
			var f=$("#file"+i).val();
			if(f==''){
				return;
			}
		}
		var entry_class=$("#entryClass").val();
		if(entry_class == '' || entry_class == "")
			return;
		var server=GetQueryString("agentName");
		var form = new FormData(document.getElementById("pluginForm"));
		if(flag==undefined){
			form.append("auto_plugin_id", "1");
		}else{
			form.append("auto_plugin_id", "0");
		}
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
           		  window.location.href="plugins.html?agentName="+server;
           	  }
			 },
	      	 error: function() {
	      	 }
		 });
	});

	$(".reset").click(function(){
		var server=GetQueryString("agentName");
		window.location.href="plugins.html?agentName="+server;
	});
	
	$("#auto_pluginid").click(function(){
		var flag=$("#pluginid").attr("checked");
		if(flag==undefined){
			$("#pluginid").attr("readOnly",true);
			$("#pluginid").attr("checked","checked");
			$("#pluginid").attr("style","background-color: rgb(210,210,210);");
		}else{
			$("#pluginid").attr("readOnly",false);
			$("#pluginid").removeAttr("checked");
			$("#pluginid").attr("style","");
		}
	});
	
});
$("#file1").change(function(){
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
	if(e == ".class"){
		$(this).parent().parent().find("input").attr("readOnly",true);
		$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
		$(this).parent().parent().find("input").attr("placeholder","无");
		$(this).parent().find("input").attr("style","display: none;");
	}else if( e == ".jar" ){
		$(this).parent().parent().find("input").attr("readOnly",true);
		$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
		$(this).parent().parent().find("input").attr("placeholder","无");
		$(this).parent().find("input").attr("style","display: none;");
	}else{
		$(this).parent().parent().find("input").attr("readOnly",false);
		$(this).parent().parent().find("input").attr("style","");
		$(this).parent().parent().find("input").attr("placeholder","请填写服务器保存路径");
		$(this).parent().find("input").attr("style","display: none;");
	}
});
function delFile(_num){
	$("#field_list"+_num).remove();
}
function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}