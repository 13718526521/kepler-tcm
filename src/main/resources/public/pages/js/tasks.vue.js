var agentName;
$(function(){
	
	agentName = getUrlParam("agentName");
	
	//迭代数据库下拉框
	dataSelect1();
	dataSelect2();
	//迭代插件下拉框
	plugSelect();
	//迭代左侧菜单栏
	tasksTitle();
	
})

//获取url地址参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}

function dataSelect1(){
	
	$.ajax({
        type: "get",
        data:{"agentAndServer":agentName},
        url: util.agent().baseUrl + "/dataBaseConfig/findAll",
        success: function(data) {
            var html = [];
            
            	html.push(
                    '<label class="label-text">*数据库：</label>',
					'<select id="databaseId" class="form-control required activeType" v-model="codeTypeId" data-toggle="popover" data-placement="bottom" data-content="">',
					'<option value="">-- 请选择 --</option>'
            	)
            	for(var i in data){
            		html.push('<option value=',data[i].id,'>',data[i].name,'</option>')
            	}
            	html.push('</select>',
            			  '<div class="trangle" style="left:calc(35% + 120px)"></div>',
            			  '<label>(主要)</label>')
            			$("#dataSelect1").append(html.join(''));
            
        },
        error: function(e) {
            $(".loading").hide();
        }
    });
}

function dataSelect2(){
	
	$.ajax({
        type: "get",
        data:{"agentAndServer":agentName},
        url: util.agent().baseUrl + "/dataBaseConfig/findAll",
        success: function(data) {
            var html = [];
            
            	html.push(
					'<select id="standbyDatabaseId" class="form-control required activeType" style="margin-left: 15px;" v-model="codeTypeId" data-toggle="popover" data-placement="bottom" data-content="">',
					'<option value="">-- 请选择 --</option>'
            	)
            	for(var i in data){
            		html.push('<option value=',data[i].id,'>',data[i].name,'</option>')
            	}
            	html.push('</select>',
            			  '<div class="trangle" style="left:calc(57%)"></div>',
            			  '<label>(备用)</label>')
            			$("#dataSelect2").append(html.join(''));
            
        },
        error: function(e) {
            $(".loading").hide();
        }
    });
}

function plugSelect(){
	
	$.ajax({
        type: "get",
        data:{"agentAndServer":agentName},
        url: util.agent().baseUrl + "/plugin/query",
        success: function(data) {
            var html = [];
            
            	html.push(
            		'<label class="label-text">*引用插件：</label>',	
					'<select id="pluginId" class="form-control required activeType" v-model="codeTypeId" data-toggle="popover" data-placement="bottom" data-content="">',
					'<option value="">-- 请选择 --</option>'
            	)
            	for(var i in data.data){
            		html.push('<option value=',data.data[i].id,'>',data.data[i].PluginName,'</option>')
            	}
            	html.push('</select>',
            			  '<div class="trangle"></div>')
            			$("#plugSelect").append(html.join(''));
            
        },
        error: function(e) {
            $(".loading").hide();
        }
    });
}
//计划栏设置disable
function opt(){
	$("input[name='opt1']").attr("disabled",true);
	$("input[name='opt2']").attr("disabled",true);
	$("input[name='opt3']").attr("disabled",true);
	$("input[name='opt4']").attr("disabled",true);
}

function opt1(){
	$("input[name='opt1']").attr("disabled",false);
	$("input[name='opt2']").attr("disabled",true);
	$("input[name='opt3']").attr("disabled",true);
	$("input[name='opt4']").attr("disabled",true);
}

function opt2(){
	$("input[name='opt1']").attr("disabled",true);
	$("input[name='opt2']").attr("disabled",false);
	$("input[name='opt3']").attr("disabled",true);
	$("input[name='opt4']").attr("disabled",true);
}

function opt3(){
	$("input[name='opt1']").attr("disabled",true);
	$("input[name='opt2']").attr("disabled",true);
	$("input[name='opt3']").attr("disabled",false);
	$("input[name='opt4']").attr("disabled",true);
}

function opt4(){
	$("input[name='opt1']").attr("disabled",true);
	$("input[name='opt2']").attr("disabled",true);
	$("input[name='opt3']").attr("disabled",true);
	$("input[name='opt4']").attr("disabled",false);
}

//设置任务监控报警栏disabled
function opts(){
	$("input[name='opts1']").attr("disabled",true);
}

function opts1(){
	$("input[name='opts1']").attr("disabled",false);
}

function opts2(){
	$("input[name='opts1']").attr("disabled",true);
}
//迭代左侧菜单栏
function tasksTitle(){
	$.ajax({
        type: "get",
        data:{"agentAndServer":agentName},
        url: util.agent().baseUrl + "/tasks/findAll",
        success: function(data) {
        	
            var html = [];
            	for(var i in data){
            		html.push(
                    		'<li>',
                    		'<span class="opt-point"></span>',
        		   	            '<input value=',data[i].taskID,' value2=',data[i].state,' type="checkbox" />'
                    	)
            		if(data[i].state == 0){
            			html.push('<span class="opt-picture"><i class="fa fa-play" aria-hidden="true"></i></span>',
            					'<span class="opt-text">',data[i].taskName,'</span>',
            					'</li>')
            		}else{
            			html.push('<span class="opt-picture"><i class="fa fa-pause" aria-hidden="true"></i></span>',
            					'<span class="opt-text">',data[i].taskName,'</span>',
            					'</li>')
            		}
            		
            	}
            			$("#tasksTitle").empty().append(html.join(''));
            
        },
        error: function(e) {
            $(".loading").hide();
        }
    });
}
//新建按钮
//获取新增输入参数
function addParams(){
	var params={};
	params.taskName = $("#taskName").val();
	//获取运行状态单选框的值
	var disabled = $("input[name='disabled']:checked").val();
	if(disabled == 0){
		params.disabled = 0;
		params.autoRun = 1;
	}else{
		params.disabled = 1;
		params.autoRun = 2;
	}
	params.pluginId = $("#pluginId").val();
	//params.EntryClass = $('#pluginId').find("option:selected").attr("value2");
	params.databaseId = $("#databaseId").val();
	params.standbyDatabaseId = $("#standbyDatabaseId").val();
	//获取计划栏单选框的值
	var radio1 = $("input[name='opt']:checked").val();
	//初始化参数
	params.year = ""; 
	params.month = "";
	params.day = "";
	params.hour = "";
	params.minute = "";
	params.second = "";
	params.mxf = "";
	params.mxt = "";
	params.hour4 = "";
	params.minute4 = "";
	params.second4 = "";
	params.cron = "";
	if(radio1==1){
		params.planType = 1;
	}else if(radio1==2){
		params.planType = 2;
		params.year = $("#year").val();
		params.month = $("#month").val();
		params.day = $("#day").val();
		params.hour = $("#hour").val();
		params.minute = $("#minute").val();
		params.second = $("#second").val();
	}else if(radio1==3){
		params.planType = 3;
		params.mxf = $("#mxf").val();
	}else if(radio1==4){
		params.planType = 4;
		params.mxt = $("#mxt").val();
		params.hour4 = $("#hour4").val();
		params.minute4 = $("#minute4").val();
		params.second4 = $("#second4").val();
	}else{
		params.planType = 5;
		params.cron = $("#cron").val();
	}
	params.logType = 2;
	params.logLevel = $("#logLevel").val();
	params.logLevel2 = $("#logLevel2").val();
	params.taskTimeout = $("#taskTimeout").val();
	params.logBackNums = $("#logBackNums").val();
	params.logMaxSize = $("#logMaxSize").val();
	//获取任务监控报警栏复选框是否选中
	params.taskAlert = "";
	params.alertType = "";
	params.keepAlertTime = "";
	params.notSuccAlert = "";
	params.notSuccTime = "";
	params.failAlert = "";
	if($("input[id='taskAlert']").is(':checked')){
		params.taskAlert = $("#taskAlert").val();
		
		//获取任务监控报警栏单选框的值
		var radio2 = $("input[name='opts']:checked").val();
		if(radio2==0){
			params.alertType = 0;
		}else if(radio2==1){
			params.alertType = 1;
			params.keepAlertTime = $("#keepAlertTime").val();
		}else{
			params.alertType = 2;
		}
		if($("input[id='notSuccAlert']").is(':checked')){
			params.notSuccAlert = $("#notSuccAlert").val();
			params.notSuccTime = $("#notSuccTime").val();
		}
		if($("input[id='failAlert']").is(':checked')){
			params.failAlert = $("#failAlert").val();
		}
	}
	return params;
}
//新建提交
$(".submit").click(function(){
	var params = {};
	params = addParams();
	params.agentAndServer = agentName;
	console.log(params);
	$.ajax({
		type: "post",
		url: util.agent().baseUrl + "/tasks/add.json",
		data: params,
		async: true,
		success: function() {
			layer.msg("新建成功", {
				time: 1000
			});
	
			//init(1);
		},
		error :function(){
			layer.msg("新建失败", {
				time: 1000
			});
		}
    });
})

//启动任务
$("#start").click(function(){
		var arr = new Array();
		var arr1 = new Array();
		var suc = "";
		var err = "";
        //获取复选框选中的值
        $("#tasksTitle :checkbox:checked").each(function(i){
            arr[i] = $(this).val();
            arr1[i] = $(this).attr("value2");
        });
        console.log(arr1);
        if(arr == null || arr.length == 0){
        	layer.msg("请先选择要启动的任务", {
				time: 1000
			});
        	return ;
        }
        for(var i in arr1){
        	if(arr1[i] == 0){
        		layer.msg("存在已启动的任务", {
    				time: 1000
    			});
        		return ;
        	}
        }
        for(var j in arr){
        	$.ajax({
        		type: "post",
        		url: util.agent().baseUrl + "/tasks/startTask.json",
        		data: {"agentAndServer":agentName,"taskId":arr[j]},
        		async: false,
        		success: function(data) {
        			if(data == 0){
        				$.ajax({
        					type: "post",
        					url: util.agent().baseUrl + "/tasks/isTaskStarted.json",
        					data: {"agentAndServer":agentName,"taskId":arr[j]},
        					async: false,
        					success: function(data) {
        						if(data){
        							suc = suc + arr[j] + "~";
        						}else{
        							err = err + arr[j] + "~";
        						}
        					}
        			    });
        			}else{
        				err = err + arr[j] + "~";
        			}
        		}
            });
        }
        if(suc!=""&&suc!=null){
        	layer.msg(suc+"任务已启动", {
				time: 1000
			});
        }
        
        if(err!=""&&err!=null){
        	setTimeout(function(){
            	layer.msg(err+"启动任务失败", {
    				time: 1000
    			});
    		  },2000);
        }
        tasksTitle();
})

//停止任务
$("#stop").click(function(){
		var arr = new Array();
		var arr1 = new Array();
		var suc = "";
		var err = "";
        //获取复选框选中的值
        $("#tasksTitle :checkbox:checked").each(function(i){
            arr[i] = $(this).val();
            arr1[i] = $(this).attr("value2");
        });
        console.log(arr1);
        if(arr == null || arr.length == 0){
        	layer.msg("请先选择要停止的任务", {
				time: 1000
			});
        	return ;
        }
        for(var i in arr1){
        	if(arr1[i] == 1){
        		layer.msg("存在已停止的任务", {
    				time: 1000
    			});
        		return ;
        	}
        }
        for(var j in arr){
        	$.ajax({
        		type: "post",
        		url: util.agent().baseUrl + "/tasks/stopTask.json",
        		data: {"agentAndServer":agentName,"taskId":arr[j]},
        		async: false,
        		success: function(data) {
        			if(data == 0){
        				suc = suc + arr[j] + "~";
        			}else{
        				err = err + arr[j] + "~";
        			}
        		}
            });
        }
        if(suc!=""&&suc!=null){
        	layer.msg(suc+"任务已停止", {
				time: 1000
			});
        }
        
        if(err!=""&&err!=null){
        	setTimeout(function(){
            	layer.msg(err+"停止任务失败", {
    				time: 1000
    			});
    		  },2000);
        }
        tasksTitle();
})

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
	$.ajax({
		type: "get",
		url: util.agent().baseUrl+"/view/js/tasks.json",
		async: true,
		data: params,
		success: function(data) {
			if(data.length>0&&data!=undefined){
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



