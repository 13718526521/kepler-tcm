$(function() {
	var vm = new Vue({
		el : "#app",
		data : {
			tabData : '',
			checkAll : false,
			checkModel : []
		},
		methods : {
			goto: function(index) {
	            this.current = index;
	        },
			dataChange : function(ind) {
				 $(".new-modal .modal-title").text('代理服务器 - 修改');
				addChange.modelData = JSON.parse(JSON.stringify(vm.tabData[ind]));
				agent = vm.tabData[ind].agent;
				port = vm.tabData[ind].port;
				memo = vm.tabData[ind].memo;
				$("#agent").val(agent);
				$("#port").val(port);
				$("#memo").val(memo);
			},
			dataDelete: function(id) {
				 $(".delete-modal .modal-title").text('提示信息');
				 $(".delete-modal .modal-body>div:nth-child(2)").text(id);
				  $(".delete-modal button[type=submit]").unbind('click').click(function() {
					  alert('删除序号为'+id+'的代理服务器');
				  });
			},
			dbConnect: function(id,agent,port) {
				$(".connect-modal .modal-title").text('提示信息');
				var info="代理服务器地址为"+agent+","+"服务器端口号为"+port+"。";
				$("#info").val(info); 
			}
		}
	});
	$.ajax({
		url : util.agent().baseUrl + "/view/manage/proxy_server.json",
		async : true,
		success : function(data) {
			$(".loading").hide();
			vm.tabData = data.server;
		},
		error : function() {
		}
	});
});
$('.new-modal button[type=submit]').click(
		function() {
			var sub_Data = {};
			sub_Data.agent = $("#agent").val();
			sub_Data.port = $("#port").val();
			sub_Data.memo = $("#memo").val();
			if (!(/^[0-9]*$/.test(sub_Data.port))) {
				alert('服务器代理端口号只能为数字');
				return;
			}
			alert('服务器地址为:' + sub_Data.agent + "\r\n" + '服务器代理端口号为:'+ sub_Data.port + "\r\n" + '服务器内容为:' + sub_Data.memo);
		});
$(".newSource").click(function() {
	$(".new-modal .modal-title").text('代理服务器 - 新增');
	$("#agent").val("");
	$("#port").val("");
	$("#memo").val("");
});
$(".query").click(function() {
	proxy_server_query = $("#proxy_server_query").val();
	alert(proxy_server_query);
});

$(".tabRefresh").click(function() {
	alert('正在刷新');
});
