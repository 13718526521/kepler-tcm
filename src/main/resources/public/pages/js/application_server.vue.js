$(function() {

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

//表格数据及分页
var vm = new Vue({
	el: "#app",
	data: {
		tabData: [],
		current: 1, //当前显示第几页
		showItem: 5,
		allpage: 13,
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
		startServer: function(agentName,serverName) {
			$(".info-modal .modal-title").text('提示信息');
			var info="代理服务器地址与端口为"+agentName+"\n\r"+"应用服务器名称为"+serverName+"。";
			$("#info").val(info); 
		},
		stopServer: function(agentName,serverName) {
			$(".info-modal .modal-title").text('提示信息');
			var info="代理服务器地址与端口为"+agentName+"\n\r"+"应用服务器名称为"+serverName+"。";
			$("#info").val(info); 
		},
		restartServer: function(agentName,serverName) {
			$(".info-modal .modal-title").text('提示信息');
			var info="代理服务器地址与端口为"+agentName+"\n\r"+"应用服务器名称为"+serverName+"。";
			$("#info").val(info); 
		},
        //修改
		dataChange: function(ind) {
			$(".new-modal .modal-title").text('应用服务器 - 修改');
				addChange.modelData = JSON.parse(JSON.stringify(vm.tabData[ind]));
				id = vm.tabData[ind].id;
				agentName = vm.tabData[ind].agentName;
				agentMemo = vm.tabData[ind].agentMemo;
				serverName = vm.tabData[ind].serverName;
				serverPort = vm.tabData[ind].serverPort;
				memo = vm.tabData[ind].memo;
				monitorPort = vm.tabData[ind].monitorPort;
				monitorInterval = vm.tabData[ind].monitorInterval;
				var html=[];
                html.push('<option value="'+id+'">',agentName,'</option>')
                $("#agentName").empty().append(html.join(''));
                $("#agentName").attr("disabled","disabled");
                $("#agentMemo").val(agentMemo);
                $("#serverName").val(serverName);
                $("#serverPort").val(serverPort);
                $("#memo").val(memo);
                $("#monitorPort").val(monitorPort);
                $("#monitorInterval").val(monitorInterval);
        },
        //删除
        dataDelete: function(id) {
        	$(".delete-modal .modal-title").text('提示信息');
			$(".delete-modal .modal-body>div:nth-child(2)").text(id);
			$(".delete-modal button[type=submit]").unbind('click').click(function() {
				alert('删除序号为'+id+'的应用服务器');
			});
        },
	}
});
$("#query").click(function() {
	application_server_query = $("#application_server_query").val();
	alert(application_server_query);
});

$(".add_btn").click(function() {
	getAgentAll();
	$(".new-modal .modal-title").text('代理服务器 - 新增');
	$("#agentName").val("");
	$("#agentName").removeAttr("disabled");
	$("#agentMemo").val("");
	$("#serverName").val("");
	$("#serverPort").val("");
	$("#memo").val("");
	$("#monitorPort").val("");
	$("#monitorInterval").val(300);
});
$('.new-modal button[type=submit]').click(
		function() {
			var sub_Data = {};
			sub_Data.agentName = $("#agentName option:selected").text();
			sub_Data.agentMemo = $("#agentMemo").val();
			sub_Data.serverName = $("#serverName").val();
			sub_Data.serverPort = $("#serverPort").val();
			sub_Data.memo = $("#memo").val();
			sub_Data.monitorPort = $("#monitorPort").val();
			sub_Data.monitorInterval = $("#monitorInterval").val();
			if (!(/^[0-9]*$/.test(sub_Data.serverPort))) {
				alert('应用服务器端口号只能为数字');
				return;
			}
			if (!(/^[0-9]*$/.test(sub_Data.monitorPort))) {
				alert('应用服务器监控端口号只能为数字');
				return;
			}
			alert('应用服务器名称为:' + sub_Data.serverName + "\r\n" + '应用服务器端口号为:'+ sub_Data.serverPort + "\r\n" + '应用服务器内容为:' + sub_Data.memo+ "\r\n" + '应用服务器监控端口号为:' + sub_Data.monitorPort+ "\r\n" + '应用服务器监控间隔为:' + sub_Data.monitorInterval);
		});
function getAgentAll(){
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/view/manage/proxy_server.json",
		async: true,
		success: function(data) {
			var html=[];
			html.push('<option value="">请选择</option>')
			for(var i in data.data){
                html.push('<option value="'+data.data[i].id+'">',data.data[i].agent+":"+data.data[i].port,'</option>')
            }
			$("#agentName").empty().append(html.join(''));
		}
	});
}
$('#agentName').change(function(){
	   var val = this.options[this.selectedIndex].value; 
	   var txt = this.options[this.selectedIndex].innerHTML;
	   if(val == null || val == "" || val == '' || txt == '请选择' || txt == "请选择"){
		   $("#agentMemo").val("");
	   }else{
		   $.ajax({
				type: "get",
				url: util.agent().baseUrl + "/view/manage/proxy_server.json",
				async: true,
				success: function(data) {
					var agentMemo='';
					for(var i in data.data){
						if(val==data.data[i].id){
							agentMemo=data.data[i].memo;
						}
					}
					$("#agentMemo").val(agentMemo);
				}
			});
	   }
});

function init(index) {
	var params = {};
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/view/manage/application_server.json",
		async: true,
		data: params,
		success: function(data) {
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
	});
}