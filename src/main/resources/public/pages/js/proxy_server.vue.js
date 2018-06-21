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
		//测试连接
		dataConnect: function(id,agent,port) {
			$(".connect-modal .modal-title").text('提示信息');
			var info="代理服务器地址为:"+agent+"\r\n"+"服务器端口号为:"+port;
			$("#info").val(info); 
		},
        //修改
		dataChange: function(ind) {
			$(".new-modal .modal-title").text('代理服务器 - 修改');
				addChange.modelData = JSON.parse(JSON.stringify(vm.tabData[ind]));
				agent = vm.tabData[ind].agent;
				port = vm.tabData[ind].port;
				memo = vm.tabData[ind].memo;
				$("#agent").val(agent);
				$("#port").val(port);
				$("#memo").val(memo);
        },
        //删除
        dataDelete: function(id) {
        	$(".delete-modal .modal-title").text('提示信息');
			$(".delete-modal .modal-body>div:nth-child(2)").text(id);
			$(".delete-modal button[type=submit]").unbind('click').click(function() {
				alert('删除序号为'+id+'的代理服务器');
			});
        },
	}
});
$("#query").click(function() {
	proxy_server_query = $("#proxy_server_query").val();
	alert(proxy_server_query);
});

$(".add_btn").click(function() {
	$(".new-modal .modal-title").text('代理服务器 - 新增');
		$("#agent").val("");
		$("#port").val("");
		$("#memo").val("");
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

function init(index) {
	var params = {};
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/view/manage/proxy_server.json",
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