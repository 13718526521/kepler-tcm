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
		start: function(){
			layer.msg("已启动", {
				time: 1000
			});
		},
		stop: function(){
			layer.msg("已停止", {
				time: 1000
			});
		},
		termination: function(){
			layer.msg("已终止", {
				time: 1000
			});
		},
        //修改
		updatask: function(ind) {
        	
        	
        	window.location.href="tasks_add.html?a=2";
        },
        //删除
        tabDelete: function() {
			$(".delete-modal .modal-title").text('是否删除？');
			//确定删除
			$(".delete-modal button[type=submit]").click(function() {
				$(".loading").show();
				/*$.ajax({
					type: "put",
					url: util.agent().baseUrl + "/custom/update.json",
					data: {"isDeleted" : 2,"id" : id},
					async: true,
					success: function() {
						layer.msg("删除成功", {
							time: 1000
						});

						init(1);
					}
				});*/
			});

		},
	}
});

function init(index) {
	var params = {};
	console.log(util.agent().baseUrl);
	$.ajax({
		type: "get",
		url: util.agent().baseUrl+"/view/js/tasks.json",
		async: true,
		data: params,
		success: function(data) {
			if(data.length>0&&data!=undefined){
				vm.tabData = data;
        		vm.current = index;
                console.log(vm.allpage);
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
// 提交
$(function() {
	
	init(1);
    
});


var initFunc = function(){
	return {
		// 鼠标悬浮于表单元素，提示信息
		formEleHover: function(){
			$(".required").hover(function() {
		        if($(this).val() == '') {
		            $(this).popover('show');
		        }
		    }, function() {
		        $(this).popover('hide');
		    });
		},
		// 单选按钮
		iradioSquare: function(){
			$(".blacklistCon .iradio_square").click(function(e){
				 e.preventDefault();
				 if(!$(this).hasClass("selected")) $(this).addClass("selected").siblings(".selected").removeClass("selected");
			 });
		},
		// 添加
		addDlFunc: function(add_dl_index,add_a_href){
			var dl_html = '';

			    dl_html = '<dl>'+
							    '<dt class="active">'+
									'<h4 class="activity-title"><span class="activity-ico icon_4g"></span><span>上传文件测试'+add_dl_index+'</span></h4>'+
									'<a href="#accordion'+add_dl_index+'"  aria-expanded="false" aria-controls="accordion'+add_dl_index+'" class="accordion-title accordionTitle js-accordionTrigger">完成</a>	'+
								'</dt>'+
								'<dd class="accordion-content accordionItem is-collapsed active"  id="accordion'+add_dl_index+'" aria-hidden="true">'+
									'<div class="form-content" id="formContent'+add_dl_index+'">'+
									   	'<form>'+
										    '<div class="form-inline">'+
												'<div class="form-group form-group-2">'+
													'<label class="control-label"><img src="../../icons/img_must.png" style="margin-right: 6px;"/>外呼活动id：</label>'+
													'<input class="form-control required" type="text" ata-container="body" data-toggle="popover" data-placement="right" data-content="请输入活动id" placeholder="请输入活动id"/>'+
												'</div>'+
												'<div class="form-group form-group-2">'+
													'<label class="control-label"><img src="../../icons/img_must.png" style="margin-right: 6px;"/>文件上传时间：</label>'+
													'<input class="form-control required" type="text" id="startTime'+add_dl_index+'" style="background: url(\'../../icons/btn_rl.png\') no-repeat right 6px center;background-color: #fff;background-size: auto 60%" onFocus="WdatePicker({dateFmt:\'yyyyMMdd\',maxDate:\'#F{$dp.$D(\\\'startTime'+add_dl_index+'\\\')}\'})" ata-container="body" data-toggle="popover" data-placement="right" data-content="请选择有效日期" placeholder="请选择有效日期"/>'+
												'</div>'+
												'<div class="clear"></div>	'+
											'</div>	'+
											'<div class="form-inline">'+
												'<div class="form-group form-group-2">'+
													'<label class="control-label"><img src="../../icons/img_must.png" style="margin-right: 6px;"/>接口秘钥：</label>'+
													'<input class="form-control required" type="text" ata-container="body" data-toggle="popover" data-placement="right" data-content="请输入接口秘钥" placeholder="请输入接口秘钥"/>'+
												'</div>'+
												'<div class="form-group form-group-2 blacklistCon">'+
													'<label class="control-label"><img src="../../icons/img_must.png" style="margin-right: 6px;"/>上传文件名时间：</label>'+
													'<div style="height: 38px;padding: 8px 12px;line-height: 1.42857143;display: inline-block">'+
														'<label class="iradio_square iradio_square_1 selected"  title="以传入的upDate时间命名"><input value="1" type="radio" style="display: none;"></label><span class="radio-txt">输入时间</span>'+
														'<label class="iradio_square iradio_square_1" title="以当前系统时间命名"><input value="0" type="radio" style="display: none;"></label><span class="radio-txt">当前系统时间</span>'+
													'</div>'+
												'</div>'+
												'<div class="clear"></div>	'+
											'</div>'+
											'<div class="form-inline">'+
											    '<div class="form-group form-group-1">'+
													'<label class="control-label"><img src="../../icons/img_must.png" style="margin-right: 6px;"/>响应结果：</label>'+
												    '<textarea class="form-control required" ata-container="body" data-toggle="popover" data-placement="right" data-content="请填写响应结果" placeholder="请填写响应结果..."></textarea>'+
												'</div>'+
											    '<div class="clear"></div>	'+
											'</div>'+
											'<div class="form-inline sum-con">'+
												'<button style="margin-right:0px" class="btn btn-blue ope-btn" type="button" id="submitBtn'+add_dl_index+'">提交</button>'+
											'</div>'+
										'</form>'+
									'</div>'+
								'</dd>'+
								'<div class="clear"></div>	'+
					     '</dl>';

			  // 关闭之前已打开的文件项
			 if($(".content-body .accordion dt>a").attr("href") != add_a_href){
				 $(".content-body .accordion dt>a").html("修改");
				 $(".content-body .accordion dt>a").parent("dt").removeClass("active");
				 $(".content-body .accordion dt>a").parent("dt").siblings("dd").removeClass("active");
			 }
			 $(".content-body .accordion").append(dl_html);

			initFunc.formEleHover();
		    initFunc.iradioSquare();

		    // 提交
			 $(".sum-con>button").click(function(){
				 initFunc.submitFunc();
			 });
		},
		// 提交
		submitFunc: function(){
			console.log(111)
		}
	}
}();
function getNowFormatDate() {
    var date = new Date();
    var seperator1 = "-";
    var year = date.getFullYear();
    var month = date.getMonth() + 1;
    var strDate = date.getDate();
    if (month >= 1 && month <= 9) {
        month = "0" + month;
    }
    if (strDate >= 0 && strDate <= 9) {
        strDate = "0" + strDate;
    }
    var currentdate = year + "" + month + "" + strDate;
    return currentdate;
}