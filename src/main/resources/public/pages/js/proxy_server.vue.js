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
		dataConnect: function(id,agentName) {
			$(".connect-modal .modal-title").text('提示信息');
			var agent=agentName.split(":")[0];
			var port=agentName.split(":")[1];
			 $.ajax({
	              type: "get",
	              url: util.agent().baseUrl + "/agent/connect",
	              data: {"agent":agent,"port":port},
	              success: function(data) {
	            	  console.log(data);
	            	  if(data.CODE==0){
	            		  var info=data.MESSAGE;
	            	  }else{
	            		  var info=data.MESSAGE;
	            	  }
	            	  $("#info").val(info); 
	              },
	      		error: function() {
	    		}
		   });
		},
		addApplication: function(id,agentName,memo){
			$(".application-modal .modal-title").text('应用服务器 - 新增');
			var html=[];
            html.push('<option value="'+id+'">',agentName,'</option>')
            $("#agentName").empty().append(html.join(''));
            $("#agentMemo").val(memo);
		},
		/*		
		    dataLog: function(id,agentName,memo){
			$(".info-modal .modal-title").text('日志信息');
			var info="代理服务器地址与端口为"+agentName+"\n\r"+"代理服务器说明为"+memo+"。";
			$("#loginfo").val(info);
			
		},*/
        //修改
		dataChange: function(ind) {
			$(".new-modal .modal-title").text('代理服务器 - 修改');
				addChange.modelData = JSON.parse(JSON.stringify(vm.tabData[ind]));
				oldAgent=vm.tabData[ind].agentName
			    agent=vm.tabData[ind].agentName.split(":")[0];
			    port=vm.tabData[ind].agentName.split(":")[1];
				memo = vm.tabData[ind].memo;
				$("#agent").val(agent);
				$("#port").val(port);
				$("#memo").val(memo);
				$(".new-modal button[type=submit]").unbind('click').click(function() {
					var edit_agent=$("#agent").val();
					var edit_port=$("#port").val();
					var edit_memo=$("#memo").val();
				 $.ajax({
		              type: "get",
		              url: util.agent().baseUrl + "/agent/edit",
		              data: {"oldagent":oldAgent,"agent":edit_agent,"port":edit_port,"memo":edit_memo},
		              success: function(data) {
		            	  if(data.CODE==1){
		         			 layer.msg("修改失败", { 
		         			 time: 1000
		         			 });
		            	  }
		            	  $("#addChange .close").click();
		                  init(1);
		              },
		      		error: function() {
		    		}
			          });
				});
        },
        //删除
        dataDelete: function(id,agentName) {
        	$(".delete-modal .modal-title").text('提示信息');
			$(".delete-modal .modal-body>div:nth-child(2)").text(id);
			$(".delete-modal button[type=submit]").unbind('click').click(function() {
				 $.ajax({
		              type: "get",
		              url: util.agent().baseUrl + "/agent/delete",
		              data: {"agentName":agentName},
		              success: function(data) {
		            	  $(".delete-modal .close").click();
		                  init(1);
		              },
		      		error: function() {
		    		}
			          });
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

$('.new-modal button[type=submit]').unbind('click').click(
		function() {
			var sub_Data = {};
			sub_Data.agent = $("#agent").val();
			sub_Data.port = $("#port").val();
			sub_Data.memo = $("#memo").val();
			if (!(/^[0-9]*$/.test(sub_Data.port))) {
				alert('服务器代理端口号只能为数字');
				return;
			}
		  $.ajax({
              type: "get",
              url: util.agent().baseUrl + "/agent/add",
              data: {"agent":sub_Data.agent,"port":sub_Data.port,"memo":sub_Data.memo},
              success: function(data) {
            	  $("#addChange .close").click();
                  init(1);
              },
      		error: function() {
    		}
          });
		});

function init(index) {
	var params = {};
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/agent/query",
		async: true,
		data: params,
		success: function(data) {
            vm.tabData = data.data;
            /*  
            	for(var i in data.data){
            	vm.tabData[i].id++;}
            */
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