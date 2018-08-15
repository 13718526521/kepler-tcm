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
					'<button class="btn btn-primary" id="file_del_'+_num+'" onclick="delFile('+_num+')" style="float: right;">删除</button>',
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
				$(this).parent().parent().find("input").attr("placeholder","无");
				$(this).parent().parent().find("input:first").val('');
				$(this).parent().find("input").attr("style","display: none;");
			}else if( e == ".jar" ){
				$(this).parent().parent().find("input").attr("readOnly",true);
				$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
				$(this).parent().parent().find("input").attr("placeholder","无");
				$(this).parent().parent().find("input:first").val('');
				$(this).parent().find("input").attr("style","display: none;");
			}else{
				$(this).parent().parent().find("input").attr("readOnly",false);
				$(this).parent().parent().find("input").attr("style","");
				$(this).parent().parent().find("input").attr("placeholder","请填写服务器保存路径");
				$(this).parent().parent().find("input:first").val('');
				$(this).parent().find("input").attr("style","display: none;");
			}
		});
	});
	
	$(".submit").click(function(){
		var entry_class=$("#entryClass").val();
		if(entry_class == '' || entry_class == "")
			return;
		for (var int = 0; int <= _num; int++) {
			var f=$("#newfile"+int).val();
			if(f==''){
				return;
			}
		}
		var file_arr=[];
		$("#field").find("tr").each(function(){
			file_arr.push($(this).find("td:first input").val());
		});
		var server=GetQueryString("agentName");
		var form = new FormData(document.getElementById("pluginFormedit"));
		form.append("agentAndServer", server);
		form.append("fileArray", file_arr);
		$.ajax({
			 url:util.agent().baseUrl + "/plugin/uploadedit",
			 type:"post",
			 data:form,
			 processData:false,
			 contentType:false,
			 success:function(data){
				 console.log(data);
				 
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
	   			arr.push(
	   					'<tr id="field_list'+i+'">',
	   						'<td><input type="text" name="className" class="form-control" placeholder="'+data[i].Path+'" value="'+data[i].Path+'" readonly style="background-color: rgb(210,210,210);"></td>',
	   						'<td>',
	   						'<label for="file'+i+'" class="btn btn-primary" style="width: 100px;">请选择文件</label> ',
	   						'<input type="file" id="file'+i+'" name="file" style="display: none;" placeholder="'+data[i].File+'" value="'+data[i].File+'"/> ',
	   						'<span id="plugin_fileName" style="margin-left: 10px;">'+data[i].File+'</span>',
	   						'<button class="btn btn-primary" id="file_del_'+i+'" onclick="del_listFile('+i+')" style="float: right;">删除</button>',
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
						$(this).parent().parent().find("input").attr("placeholder","无");
						$(this).parent().parent().find("input:first").val('');
						$(this).parent().find("input").attr("style","display: none;");
					}else if( e == ".jar" ){
						$(this).parent().parent().find("input").attr("readOnly",true);
						$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
						$(this).parent().parent().find("input").attr("placeholder","无");
						$(this).parent().parent().find("input:first").val('');
						$(this).parent().find("input").attr("style","display: none;");
					}else{
						$(this).parent().parent().find("input").attr("readOnly",false);
						$(this).parent().parent().find("input").attr("style","");
						$(this).parent().parent().find("input").attr("placeholder","请填写服务器保存路径");
						$(this).parent().parent().find("input:first").val('');
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

function del_listFile(i){
	$("#field_list"+i).remove();
}


function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

