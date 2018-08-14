var server,agentName,serverName,pluginName;
$(function() {
	
	server=GetQueryString("agentName");
	agentName=server.split("@")[0];
	serverName=server.split("@")[1];

    $(".pagleft input").on("change", function() {
        this.value = this.value.replace(/[^0-9]+/, '');
        if(this.value <= 0) {
            this.value = 1;
        }
        init(1);
    });

    $(".required").hover(function() {
		if($(this).val() == '') {
			$(this).popover('show');
		}
	}, function() {
		$(this).popover('hide');
	});
    
	init(1);
});

function GetQueryString(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
	var r = window.location.search.substr(1).match(reg);
	if (r != null)
		return unescape(r[2]);
	return null;
}

//表格数据及分页
var vm = new Vue({
	el: "#app",
	data: {
        tabData: [],
        current: 0, //当前显示第几页
        showItem: 0,
        allpage: 0,
        totalNum: 0,
        checkAll:false,
        checkModel:[]
	},
	computed: {
		pages: function() {
			var pag = [];
			if(this.current < this.showItem) { //如果当前的激活的项 小于要显示的条数
				//总页数和要显示的条数那个大就显示多少条
				var i = Math.min(this.showItem, this.allpage);
				while(i) {
					pag.unshift(i--);
				}
			} else { //当前页数大于显示页数了
				var middle = this.current - Math.floor(this.showItem / 2), //从哪里开始
					i = this.showItem;
				if(middle > (this.allpage - this.showItem)) {
					middle = (this.allpage - this.showItem) + 1
				}
				while(i--) {
					pag.push(middle++);
				}
			}
			return pag;
		}
	},
	methods: {
		goto: function(index) {
			this.current = index;
			//这里可以发送ajax请求
			init(index);
		},
		//重新载入
		dataReload: function(id) {
			 $.ajax({
	              type: "post",
	              url: util.agent().baseUrl + "/plugin/reload",
	              data: {"agentAndServer":server,"id":id},
	              success: function(data) {
	            	  if(data.CODE==1){
	            		  alert(data.MESSAGE);
	             	  }else{
	 	         		 layer.msg("重载成功", { time: 1000 });
	 	         		 init(1);
	             	  }
	              },
	      		error: function() {
	    		}
			 });
/*			$(".reload-modal .modal-title").text('提示信息');
			var info="插件名称为:"+pluginName+"\r\n"+"插件的入口类为:"+entryClass;
			$("#info").val(info); */
		},
        //修改
		dataChange: function(ind,pluginName,entryClass) {
			window.location.href="plugins_edit.html?agentName="+server+"&id="+ind;
/*			$(".new-modal .modal-title").text('插件 - 修改');
				addChange.modelData = JSON.parse(JSON.stringify(vm.tabData[ind]));
				pluginid = vm.tabData[ind].id;
				pluginName = vm.tabData[ind].pluginName;
				entryClass = vm.tabData[ind].entryClass;
				pluginMemo = vm.tabData[ind].pluginMemo;
				plugin_fileName = vm.tabData[ind].plugin_fileName;
				$("#pluginid").val(pluginid);
				$("#pluginName").val(pluginName);
				$("#entryClass").val(entryClass);
				$("#pluginMemo").val(pluginMemo);
				$("#plugin_fileName").text(plugin_fileName);*/
        },
        //删除
        dataDelete: function(id) {
        	$(".delete-modal .modal-title").text('提示信息');
			$(".delete-modal .modal-body>div:nth-child(2)").text(id);
			$(".delete-modal button[type=submit]").unbind('click').click(function() {
				 $.ajax({
		              type: "post",
		              url: util.agent().baseUrl + "/plugin/delete",
		              data: {"agentAndServer":server,"id":id},
		              success: function(data) {
		            	  if(data.CODE==1){
		            		  alert(data.MESSAGE);
		             	  }else{
		 	         		 layer.msg("删除成功", { time: 1000 });
		 	         		  $("#myModel .close").click();
		 	         		  init(1);
		             	  }
		              },
		      		error: function() {
		    		}
				 });
			});
        },
        Verification: function(c) {
        	return c.length == 0?'<span class="fa fa-check" style="color:green;"></span>':'<span class="fa fa-remove" style="color:red;"></span>'
        }
	}
});
$("#query").click(function() {
	pluginName = $("#plugin_query").val();
	init(1);
});

$(".add_btn").click(function() {
	window.location.href="plugins_add.html?agentName="+server;
});

/*$('.new-modal button[type=submit]').click(
		function() {
			var sub_Data = {};
			sub_Data.pluginid = $("#pluginid").val();
			sub_Data.pluginName = $("#pluginName").val();
			sub_Data.entryClass = $("#entryClass").val();
			sub_Data.pluginMemo = $("#pluginMemo").val();
			sub_Data.plugin_fileName = $("#plugin_fileName").text();
			alert('插件序号为:' + sub_Data.pluginid + "\r\n" + '插件名称为:'+ sub_Data.pluginName + "\r\n" + '入口类为:' + sub_Data.entryClass+ "\r\n" + '说明为:' + sub_Data.pluginMemo+ "\r\n" + '上传文件名称为:' + sub_Data.plugin_fileName);
});*/

$("#file").change(function(){
	var t_files = this.files;
    var str='';
    for (var i = 0, len = t_files.length; i < len; i++) {
        str +=t_files[i].name + '  ';
    };
    $(this).parent().find("span").html(str);
});

function init(index) {
	$("#plugin_query").val("");
	var pageNum=index-1;
    var pageSize = $(".pagleft input").val();
    $(".loading").show();
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/plugin/querypage",
		async: true,
		data: {"agentAndServer":server,"pluginName":pluginName,"pageNum":pageNum,"pageSize":pageSize},
		success: function(data) {
			if(data.CODE==1){
				alert(data.MESSAGE);
			}else{
				$(".loading").hide();
				vm.tabData = data.data;
	            vm.current = index;
	            vm.showItem = 5;
	            vm.allpage = data.totalPages;
				vm.totalNum = data.totalCount;
				if(data.data.length>0){
					vm.current = index;
	        		$("#app .no-data").remove();
				}else{
					vm.current = 0;
	        		$("#app table>tbody").empty();
	        		$("#app .no-data").remove();
	        		$("#app .pagleft").before('<div class="no-data"><p></p><h4>暂时没有数据</h4></div>');
				}
			}
		},
		error: function() {
		}
	});
}