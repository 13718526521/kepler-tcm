var server,dbName,dbDriver,dbUrl,dbUser,dbPass;
$(function() {
	
	server = localStorage.server;
	
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
		allpage: 1,
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
			//if(index == this.current) return;
			this.current = index;

			// 取消全选
			if($("#app").find("thead input").is(':checked')){
				$("#app").find("thead input").attr("checked",false);
				$("#app").find("thead input").parent().removeClass("active");
			}
			// 取消tr选中的背景状态
			if($("#app").find("tbody>tr").hasClass("active"))$("#app").find("tbody>tr").removeClass("active");
			
			//这里可以发送ajax请求
			init(index);
		},
		Check: function(){
			layer.msg("连接成功", {
				time: 1000
			});
		},
        //修改
        Updatask: function(ind,dbId) {
        	$(".new-modal .modal-title").text('修改');
        	
        	$("#dbName").val(vm.tabData[ind].dbName);
        	$("#dbDriver").val(vm.tabData[ind].dbDriver);
        	$("#dbUrl").val(vm.tabData[ind].dbUrl);
        	$("#dbUser").val(vm.tabData[ind].dbUser);
        	$("#dbPass").val(vm.tabData[ind].dbPass);
        },
        //删除
        tabDelete: function(dbId) {
			$(".delete-modal .modal-title").text('是否删除？');
			//确定删除
			$(".delete-modal button[type=submit]").click(function() {
				$(".loading").show();
				$.ajax({
					type: "put",
					url: util.agent().baseUrl + "/dataBaseConfig/remove.json",
					data: {"dbId" : dbId,"agentAndServer" : server},
					async: true,
					success: function() {
						layer.msg("删除成功", {
							time: 1000
						});

						init(1);
					}
				});
			});

		},
	}
});

//新增
$("#dataBase").click(function(){
	$(".new-modal .modal-title").text('新增');
	
	$("#dbName").val("");
	$("#dbDriver").val("");
	$("#dbUrl").val("");
	$("#dbUser").val("");
	$("#dbPass").val("");
	
	$(".new-modal button[type=submit]").click(function() {
		$(".loading").show();
		var data = {};
		dbName = $("#dbName").val();
		dbDriver = $("#dbDriver").val();
		dbUrl = $("#dbUrl").val();
		dbUser = $("#dbUser").val();
		dbPass = $("#dbPass").val();
		data.dbName = dbName;
		data.dbDriver = dbDriver;
		data.dbUrl = dbUrl;
		data.dbUser = dbUser;
		data.dbPass = dbPass;
		data.agentAndServer=server;
		console.log(server);
		console.log(data);
		$.ajax({
			type: "post",
			url: util.agent().baseUrl + "/dataBaseConfig/add.json",
			data: data,
			async: true,
			success: function() {
				layer.msg("删除成功", {
					time: 1000
				});

				init(1);
			}
		});
	});
})

function init(index) {
	var params = {};
	params.pageNum = index-1;
    params.pageSize = $(".pagleft input").val();
    params.agentAndServer=server;
	$.ajax({
		type: "get",
		url: util.agent().baseUrl+"/dataBaseConfig/pages.json",
		async: true,
		data: params,
		success: function(data) {
			if(data.error!=null&&data.error!=undefined){
				$(".loading").hide();
	            layer.msg("请求异常", {
	                time : 1500
	            });
			}
			if(data.length>0&&data!=undefined){
				console.log(data);
				vm.tabData = data;
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