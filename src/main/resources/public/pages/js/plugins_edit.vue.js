$(function(){
	var _num = 0;
	var server=GetQueryString("agentName");
	var id=GetQueryString("id");
	GetProperty(server,id);
	
	$("#addfield").click(function(){
		_num++
		var arr=[];
		arr.push(
				'<tr id="field_new_list'+_num+'">',
					'<td><input type="text" name="className" class="form-control" placeholder="无" readonly style="background-color: rgb(210,210,210);"></td>',
					'<td>',
					'<label for="newfile'+_num+'" class="btn btn-primary" style="width: 100px;">请选择文件</label> ',
					'<input type="file" id="newfile'+_num+'" name="file" style="display: none;"/> ',
					'<span id="plugin_fileName" style="margin-left: 10px;">未选择任何文件</span>',
					'<button class="btn btn-primary" id="file_del_'+_num+'" onclick="delFile('+_num+')">删除</button>',
					'</td>',
				'</tr>'
		)
		$("#field").append(arr.join(''));
		$("#newfile"+_num).change(function(){
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
			if(e == ".class"){
				$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().find("input").attr("style","display: none;");
			}else if( e == ".jar" ){
				$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().find("input").attr("style","display: none;");
			}else{
				$(this).parent().parent().find("input").attr("readOnly",false);
				$(this).parent().parent().find("input").attr("placeholder","请填写服务器保存路径");
				$(this).parent().parent().find("input").attr("style","");
				$(this).parent().find("input").attr("style","display: none;");
			}
		});
	});
	
	$(".submit").click(function(){
		if(_num != 0 ){
			for (var i = 1; i <= _num; i++) {
				var f=$("#newfile"+i).val();
				if(f==''){
					return;
				}
			}
		}
		var entry_class=$("#entryClass").val();
		if(entry_class == '' || entry_class == "")
			return;
		var server=GetQueryString("agentName");
		var form = new FormData(document.getElementById("pluginFormedit"));
		form.append("agentAndServer", server);
		 $.ajax({
			 url:util.agent().baseUrl + "/plugin/uploadedit",
			 type:"post",
			 data:form,
			 processData:false,
			 contentType:false,
			 success:function(data){
	       	  if(data.CODE==1){
	       		  alert(data.MESSAGE);
	       	  }else{
	       		  layer.msg("修改成功", { time: 1000 });
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

});

function GetProperty(server,id){
	$.ajax({
	    type: "get",
	    url: util.agent().baseUrl + "/plugin/getpropertybyid",
	    data: {"agentAndServer":server,"id":id},
	    success: function(data) {
	  	  if(data.CODE==1){
	  		  alert(data.MESSAGE);
	   	  }else{
	   		$("#pluginid").val(data.data.id);
	   		$("#pluginName").val(data.data.pluginName);
	   		$("#entryClass").val(data.data.entryClass);
	   		$("#pluginMemo").val(data.data.pluginMemo);
	   		var data=data.fileList;
	   		var arr=[];
	   		for (var i in data){
				var f=data[i].lastIndexOf(".");
				var e="";
				var val='';
				if(f >= 0)
				e=data[i].substring(f).toLowerCase();
				var server=GetQueryString("agentName");
				serverName=server.split("@")[1];
				var path="../servers/"+serverName+"/plugins"+"/"+id;
				if(e == ".class" ){
					val="无";
				}else if(e == ".jar" ){
					val="无";
				}else{
					val=path;
				}
	   			arr.push(
	   					'<tr id="field_list'+i+'">',
	   						'<td><input type="text" name="className" class="form-control" placeholder="'+val+'" value="'+val+'" readonly style="background-color: rgb(210,210,210);"></td>',
	   						'<td>',
	   						'<label for="file'+i+'" class="btn btn-primary" style="width: 100px;">请选择文件</label> ',
	   						'<input type="file" id="file'+i+'" name="file" style="display: none;" placeholder="'+val+'" value="'+val+'"/> ',
	   						'<span id="plugin_fileName" style="margin-left: 10px;">'+data[i]+'</span>',
	   						'</td>',
	   					'</tr>'
	   			)
	   		}
	   		$("#field").append(arr.join(''));
	   		for (var int = 0; int <= i; int++) {
		   		$("#file"+int).change(function(){
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
						$(this).parent().find("input").attr("style","display: none;");
					}else if( e == ".jar" ){
						$(this).parent().parent().find("input").attr("readOnly",true);
						$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
						$(this).parent().find("input").attr("style","display: none;");
					}else{
						$(this).parent().parent().find("input").attr("readOnly",false);
						$(this).parent().parent().find("input").attr("placeholder","请填写服务器保存路径");
						$(this).parent().parent().find("input").removeAttr("value");
						$(this).parent().parent().find("input").attr("style","");
						$(this).parent().find("input").attr("style","display: none;");
					}
				});
			}
	   	  }
	    },
		error: function() {
		}
	});
}
function delFile(_num){
	$("#field_new_list"+_num).remove();
}

function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

