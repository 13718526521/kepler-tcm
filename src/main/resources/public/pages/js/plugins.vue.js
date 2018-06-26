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
		//重新载入
		dataReload: function(id,pluginName,entryClass) {
			$(".reload-modal .modal-title").text('提示信息');
			var info="插件名称为:"+pluginName+"\r\n"+"插件的入口类为:"+entryClass;
			$("#info").val(info); 
		},
        //修改
		dataChange: function(ind) {
			$(".new-modal .modal-title").text('插件 - 修改');
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
				$("#plugin_fileName").text(plugin_fileName);
        },
        //删除
        dataDelete: function(id) {
        	$(".delete-modal .modal-title").text('提示信息');
			$(".delete-modal .modal-body>div:nth-child(2)").text(id);
			$(".delete-modal button[type=submit]").unbind('click').click(function() {
				alert('删除序号为'+id+'的插件');
			});
        },
	}
});
$("#query").click(function() {
	plugin_query = $("#plugin_query").val();
	alert(plugin_query);
});

$(".add_btn").click(function() {
	$(".new-modal .modal-title").text('插件 - 新增');
		$("#pluginid").val("");
		$("#pluginName").val("");
		$("#entryClass").val("");
		$("#pluginMemo").val("");
		$("#plugin_fileName").text("");
});

$('.new-modal button[type=submit]').click(
		function() {
			var sub_Data = {};
			sub_Data.pluginid = $("#pluginid").val();
			sub_Data.pluginName = $("#pluginName").val();
			sub_Data.entryClass = $("#entryClass").val();
			sub_Data.pluginMemo = $("#pluginMemo").val();
			sub_Data.plugin_fileName = $("#plugin_fileName").text();
			alert('插件序号为:' + sub_Data.pluginid + "\r\n" + '插件名称为:'+ sub_Data.pluginName + "\r\n" + '入口类为:' + sub_Data.entryClass+ "\r\n" + '说明为:' + sub_Data.pluginMemo+ "\r\n" + '上传文件名称为:' + sub_Data.plugin_fileName);
		});

$("#file").change(function(){
	var t_files = this.files;
    var str='';
    for (var i = 0, len = t_files.length; i < len; i++) {
        str +=t_files[i].name + '  ';
    };
    $(this).parent().find("span").html(str);
});

function init(index) {
	var params = {};
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/view/manage/plugins.json",
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