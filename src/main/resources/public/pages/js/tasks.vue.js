var agentName,tasksTitleData,aTaskId,aTaskName,type,types;
$(function(){
	type=0;
	agentName = getUrlParam("agentName");
	var a = agentName.split("@");
	var html = [];
	html.push('<h4 class="activity-title"><span class="activity-ico icon_4g"></span><span>',a[1],'</span></h4>');
	$("#serverName").append(html.join(''));
	//迭代数据库下拉框
	dataSelect1();
	dataSelect2();
	//迭代插件下拉框
	plugSelect();
	//迭代左侧菜单栏
	tasksTitle(); 
	
	/*$("#newTask").hide();
	$("#listenter").show();*/
	
	$("#logTask").hide();
	$("#newTask").hide();
	$("#listenter").show();
	taskRun();
	init(1);
})

$("#selection").click(function(){
	var taskIds = [];
	var liList = $("#tasksTitle li");
	if($(this).val()=="0"){
		for(var i = 0; i<liList.length; i++){
			if(liList.eq(i).find("input[type='checkbox']").prop("checked")==false){
				$(this).text("取消");
				liList.eq(i).find("input[type='checkbox']").prop("checked",true);
			}
		}
		$(this).val("1");
	}else{
		for(var i = 0; i<liList.length; i++){
			if(liList.eq(i).find("input[type='checkbox']").prop("checked")==true){
				$(this).text("全选");
				liList.eq(i).find("input[type='checkbox']").prop("checked",false);
			}
		}
		$(this).val("0");
	}
	
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
        async: false,
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
            					/*'<span onclick="edit()" class="opt-text">',data[i].taskName,'</span>',*/
            					'<a id=',data[i].taskID,' value=',data[i].taskName,' href="javascript:void(0)" class="opt-text">',data[i].taskName,'</a>',
            					'</li>')
            		}else{
            			html.push('<span class="opt-picture"><i class="fa fa-pause" aria-hidden="true"></i></span>',
            					/*'<span onclick="edit()" class="opt-text">',data[i].taskName,'</span>',*/
            					'<a id=',data[i].taskID,' value=',data[i].taskName,' href="javascript:void(0)" class="opt-text">',data[i].taskName,'</a>',
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


//抽一个方法
function refresh(agentName,aTaskId){
	$.ajax({
        type: "get",
        data:{"agentAndServer":agentName,"taskId":aTaskId},
        url: util.agent().baseUrl + "/tasks/getTask",
        success: function(data) {
        	tasksTitleData = data;
            var html = [];
            	html.push(
            		'	<table border="1" cellspacing="3" cellpading="3" width="400px" style="border-collapse: collapse;">    ',
			   	    '    <tbody>                                                                                              ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">任务名称:</td>                                                     ',
			   	    '             <td>',data.taskInfo[0][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">引用插件:</td>                                                     ',
			   	    '             <td>',data.taskInfo[1][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">任务启动时间:</td>                                                 ',
			   	    '             <td>',data.taskInfo[2][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">最后运行时间:</td>                                                 ',
			   	    '             <td>',data.taskInfo[3][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">运行次数:</td>                                                     ',
			   	    '             <td>',data.taskInfo[4][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">成功:</td>                                                         ',
			   	    '             <td>',data.taskInfo[5][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">失败:</td>                                                         ',
			   	    '             <td>',data.taskInfo[6][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">等待:</td>                                                         ',
			   	    '             <td>',data.taskInfo[7][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '         <tr class="runMessageTr">                                                                       ',
			   	    '             <td class="runMessageTd">忽略:</td>                                                         ',
			   	    '             <td>',data.taskInfo[8][1],'</td>                                                                             ',
			   	    '         </tr>                                                                                           ',
			   	    '    </tbody>                                                                                             ',
			   	    '</table>                                                                                                 '
            	)
            			$("#runMessage").empty().append(html.join(''));
            
        },
        error: function(e) {
            $(".loading").hide();
        }
    });
}

//......
$("#tasksTitle").on("click","li>a",function(){
	if($(this).parent("li").find("input[type='checkbox']").prop("checked")==false){
		$(this).parent("li").find("input[type='checkbox']").prop("checked",true);
	}else{
		$(this).parent("li").find("input[type='checkbox']").prop("checked",false);
	}
	aTaskId = $(this).attr("id");
	aTaskName = $(this).attr("value");
	var html1 = [];
	html1.push("<h4 class='task-title' style='font-size: 15px;'>[",aTaskId,"]&nbsp",aTaskName,"</h4>");
	$("#taskIdName").empty().append(html1.join(''));
	$("#logTask").show();
	$("#listenter").hide();
	$("#newTask").hide();
	$("#paramsDeploy").hide();
	$("#runLogs").hide();
	$("#editTask").hide();
	$("#runMessage").show();
	/*if(tasksTitleData!=null&&tasksTitleData!=""&&tasksTitleData!="undefined"&&aTaskId == tasksTitleData.taskID){
		console.log(333);
		return ;
	}*/
	refresh(agentName,aTaskId);
	
	//定时器  实时查看监控数据
	setInterval(function(){
		refresh(agentName,aTaskId);
	},5000)
})

//运行信息
function message(){
	$("#paramsDeploy").hide();
	$("#runLogs").hide();
	$("#newTask").hide();
	$("#runMessage").show();
	
}
//运行日志
function logger(){
	$("#paramsDeploy").hide();
	$("#newTask").hide();
	$("#runMessage").hide();
	$("#runLogs").show();
	log1();
}

function log1(){
	
	$("#logWriter").show();
	types=0;
	initLogWriter(1);
}

function log2(){
	
	$("#logWriter").show();
	types=1;
	initLogWriter(1);
}

var vm1 = new Vue({
	el: "#runLogs",
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

			//这里可以发送ajax请求
			initLogWriter(index);
		}
	}
});

function initLogWriter(index){
	var params = {};
	params.pageNum = index-1;
    params.pageSize = $(".pagleft input").val();
    params.pageNo = 0;
    params.types = types;
    params.taskId = aTaskId;
    params.agentAndServer=agentName; 
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/tasks/getTaskLog.json",
		data: params,
		async: false,
		success: function(data) {
			vm1.tabData = data.data;               
            vm1.showItem = 5;
            vm1.allpage = data.totalPages;
            vm1.totalNum = data.totalCount;
            var html = [];
        	for(var i in data.data){
        		html.push('<tr class="runMessageTr">',
	                         '<td class="runMessageTd">',data.data[i],'</td>',
   	                     '</tr>'
   	             )
        	}
        			$("#tBody").empty().append(html.join(''));
			
		},
		error :function(){
			
		}
    });
}

//参数配置
function paramsDeploy(){
	var params = {};
	params.taskId = aTaskId;
	params.agentAndServer=agentName;
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/tasks/getTaskConfig.json",
		data: params,
		async: false,
		success: function(data) {
			if(data!=null&&data.length>0){
				$("#runMessage").hide();
				$("#newTask").hide();
				$("#runLogs").hide();
				$("#paramsDeploy").show();
				var html = [];
				for(var i in data){
	        		html.push('<tr class="runMessageTr">',
  	                           '   <td>',data[i][0],'</td>',
  	                           '   <td class="runMessageTd">',
   	                           '   <textarea data-name="param_'+data[i][0]+'"  style="width:300px;" value="'+data[i][1]+'"/>',
   	                           '   <a href="javascript:void(0)" class="opt-text">展开多行</a>',
   	                           '  </td>',
   	                           '  <td>',data[i][2],'</td>',
   	                        '</tr>')
	        	}
	        			$("#paramsConfig").empty().append(html.join(''));
			}else{
				layer.msg("该任务未配置参数", {
					time: 1000
				});
			}
			
		}
    });
}

$("#paramsConfig").on("click","a",function(){
	var heig = $(this).siblings().css("height");;
	if(heig == "60px"){
		$(this).siblings().css("height","37px");
	}else{
		$(this).siblings().css("height","60px");
	}
	
})

//保存参数按钮
$("#submitConfig").click(function(){
	var params = "{";
	var params_arr = [];
	var input_arr = $("#paramsDeploy table tbody tr input");
	var name_temp , value_temp;
	for(var i=0; i<input_arr.length; i++){
		name_temp = input_arr.eq(i).attr("data-name");
		value_temp = input_arr.eq(i).val();
		params_arr.push('"'+name_temp+'"'+":"+'"'+value_temp+'"');
	}
	/*params.taskId = aTaskId;
    params.agentAndServer=agentName;*/ 
    for(var i=0; i<params_arr.length; i++){
    	if(i==0){
    		params = params + params_arr[i];
    	}else{
    		params = params + "," + params_arr[i];
    	}
    }
    params = params + ',' +'"taskId":'+'"'+aTaskId +'"'+ ',' + '"agentAndServer":'+'"'+agentName +'"';
    params = params + "}";
    params = JSON.parse(params);
    
	$.ajax({
		type: "post",
		url: util.agent().baseUrl + "/tasks/saveConfigProperty.json",
		data: params,
		async: false,
		success: function(data) {
			if(data == 0){
				layer.msg("保存成功", {
					time: 1000
				});
			}else{
				layer.msg("保存失败", {
					time: 1000
				});
			}
			
			
		},
		error :function(){
			
		}
    });
	
})


//任务属性
function property(){
	type = 1;
	$("#runMessage").hide();
	$("#paramsDeploy").hide();
	$("#runLogs").hide();
	$("#newTask").show();
	$("#taskName").val(tasksTitleData.taskProperty.taskName);
	$("#pluginId").val(tasksTitleData.taskProperty.pluginId);
	$("#databaseId").val(tasksTitleData.taskProperty.databaseId);
	$("#standbyDatabaseId").val(tasksTitleData.taskProperty.standbyDatabaseId);
	$("#year").val(tasksTitleData.taskProperty.year);
	$("#month").val(tasksTitleData.taskProperty.month);
	$("#day").val(tasksTitleData.taskProperty.day);
	$("#hour").val(tasksTitleData.taskProperty.hour);
	$("#minute").val(tasksTitleData.taskProperty.minute);
	$("#second").val(tasksTitleData.taskProperty.second);
	$("#mxf").val(tasksTitleData.taskProperty.mxf);
	$("#mxt").val(tasksTitleData.taskProperty.mxt);
	$("#hour4").val(tasksTitleData.taskProperty.hour4);
	$("#minute4").val(tasksTitleData.taskProperty.minute4);
	$("#second4").val(tasksTitleData.taskProperty.second4);
	$("#cron").val(tasksTitleData.taskProperty.cron);
	$("#logLevel").val(tasksTitleData.taskProperty.logLevel);
	$("#logLevel2").val(tasksTitleData.taskProperty.logLevel2);
	$("#taskTimeout").val(tasksTitleData.taskProperty.taskTimeout);
	$("#logBackNums").val(tasksTitleData.taskProperty.logBackNums);
	$("#logMaxSize").val(tasksTitleData.taskProperty.logMaxSize);
	$("#taskAlert").val(tasksTitleData.taskProperty.taskAlert);
	$("#keepAlertTime").val(tasksTitleData.taskProperty.keepAlertTime);
	$("#notSuccAlert").val(tasksTitleData.taskProperty.notSuccAlert);
	$("#notSuccTime").val(tasksTitleData.taskProperty.notSuccTime);
	$("#failAlert").val(tasksTitleData.taskProperty.failAlert);
	console.log(tasksTitleData.taskProperty);
	var dis = $("input[name='disabled']:checked").val();
	if(tasksTitleData.taskProperty.disabled == "0"){
		$("input.runDisabled[value='0']").prop("checked",true);
	}else{
		$("input.runDisabled[value='1']").prop("checked",true);
	}
	if(tasksTitleData.taskProperty.planType == "1"){
		$("input.planTypeTime[value='1']").prop("checked",true);
	}else if(tasksTitleData.taskProperty.planType == "2"){
		$("input.planTypeTime[value='2']").prop("checked",true);
	}else if(tasksTitleData.taskProperty.planType == "3"){
		$("input.planTypeTime[value='3']").prop("checked",true);
	}else if(tasksTitleData.taskProperty.planType == "4"){
		$("input.planTypeTime[value='4']").prop("checked",true);
	}else{
		$("input.planTypeTime[value='5']").prop("checked",true);
	}
	
	if(tasksTitleData.taskProperty.alertType == "0"){
		$("input.alertType[value='0']").prop("checked",true);
	}else if(tasksTitleData.taskProperty.alertType == "1"){
		$("input.alertType[value='1']").prop("checked",true);
	}else{
		$("input.alertType[value='2']").prop("checked",true);
	}
	
	if(tasksTitleData.taskProperty.taskAlert != null && tasksTitleData.taskProperty.taskAlert != ""){
		$("input.taskAlert[value='1']").prop("checked",true);
	}
	
	if(tasksTitleData.taskProperty.notSuccAlert != null && tasksTitleData.taskProperty.notSuccAlert != ""){
		$("input.notSuccAlert[value='1']").prop("checked",true);
	}
	
	if(tasksTitleData.taskProperty.failAlert != null && tasksTitleData.taskProperty.failAlert != ""){
		$("input.failAlert[value='1']").prop("checked",true);
	}
}

//新建按钮
$("#newAdd").click(function(){
	type = 0;
	$("#logTask").hide();
	$("#listenter").hide();
	$("#newTask").show();
	$("#taskName").val("");
	$("#pluginId").val("");
	$("#databaseId").val("");
	$("#standbyDatabaseId").val("");
	$("#year").val("");
	$("#month").val("");
	$("#day").val("");
	$("#hour").val("");
	$("#minute").val("");
	$("#second").val("");
	$("#mxf").val("");
	$("#mxt").val("");
	$("#hour4").val("");
	$("#minute4").val("");
	$("#second4").val("");
	$("#cron").val("");
	$("#logLevel").val("INFO");
	$("#logLevel2").val("ERROR");
	$("#taskTimeout").val("0");
	$("#logBackNums").val("");
	$("#logMaxSize").val("");
	$("#taskAlert").val("1");
	$("#keepAlertTime").val("");
	$("#notSuccAlert").val("");
	$("#notSuccTime").val("10");
	$("#failAlert").val("");
	$("input.taskAlert[value='1']").prop("checked",true);
})

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
		console.log(radio2);
		if(radio2==0){
			params.alertType = 0;
			params.keepAlertTime = "";
		}else if(radio2==1){
			params.alertType = 1;
			params.keepAlertTime = $("#keepAlertTime").val();
		}else{
			params.alertType = 2;
			params.keepAlertTime = "";
		}
		if($("input[id='notSuccAlert']").is(':checked')){
			params.notSuccAlert = $("#notSuccAlert").val();
			params.notSuccTime = $("#notSuccTime").val();
		}
		if($("input[id='failAlert']").is(':checked')){
			params.failAlert = $("#failAlert").val();
		}
	}
	console.log(params.alertType);
	return params;
}
//新建修改提交
$(".submit").click(function(){
	
	var params = {};
	params = addParams();
	params.agentAndServer = agentName;
	params.taskID = aTaskId;
	//校验
	
	
	if(type == 0){
		$.ajax({
			type: "post",
			url: util.agent().baseUrl + "/tasks/add.json",
			data: params,
			async: false,
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
	}else{
		$.ajax({
			type: "post",
			url: util.agent().baseUrl + "/tasks/edit.json",
			data: params,
			async: false,
			success: function() {
				layer.msg("修改成功", {
					time: 1000
				});
		
				//init(1);
			},
			error :function(){
				layer.msg("修改失败", {
					time: 1000
				});
			}
	    });
	}
	
	tasksTitle();
	$("#newTask").hide();
	$("#logTask").hide();
	$("#listenter").show();
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
        						console.log(data);
        						if(data){
        							suc = suc + arr[j] + "~";
        							$("#tasksTitle li input[value="+arr[j]+"]").attr("value2","0");
        							$("#tasksTitle li input[value="+arr[j]+"]").parent("li").find("span.opt-picture i").removeClass("fa-pause").addClass("fa-play");
        							
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
        //tasksTitle();
        
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
        				$("#tasksTitle li input[value="+arr[j]+"]").attr("value2","1");
        				$("#tasksTitle li input[value="+arr[j]+"]").parent("li").find("span.opt-picture i").removeClass("fa-play").addClass("fa-pause");
        				
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
       // tasksTitle();
})

//删除任务
$("#remove").click(function(){
		var arr = new Array();
		var suc = "";
		var err = "";
        //获取复选框选中的值
        $("#tasksTitle :checkbox:checked").each(function(i){
            arr[i] = $(this).val();
        });
        if(arr == null || arr.length == 0){
        	layer.msg("请先选择要删除的任务", {
				time: 1000
			});
        	return ;
        }
        for(var j in arr){
        	$.ajax({
        		type: "post",
        		url: util.agent().baseUrl + "/tasks/remove.json",
        		data: {"agentAndServer":agentName,"taskId":arr[j]},
        		async: false,
        		success: function(data) {
        			if(data == 0){
        				suc = suc + arr[j] + "~";
        				$("#tasksTitle li input[value="+arr[j]+"]").parent("li").remove();
        			}else{
        				err = err + arr[j] + "~";
        			}
        		}
            });
        }
        if(suc!=""&&suc!=null){
        	layer.msg("任务已删除", {
				time: 1000
			});
        }
        
        if(err!=""&&err!=null){
        	setTimeout(function(){
            	layer.msg(err+"删除任务失败", {
    				time: 1000
    			});
    		  },2000);
        }
        //tasksTitle();
})

//监控任务
$("#monitor").click(function(){
	$("#logTask").hide();
	$("#newTask").hide();
	$("#listenter").show();
	taskRun();
	init(1);
})

//任务运行监控迭代
function taskRun(){
	var agentName1 = agentName.split("@");
	var aName = agentName1[0];
	var sName = agentName1[1];
	$.ajax({
        type: "get",
        data:{"agentName":aName,"serverName":sName},
        url: util.agent().baseUrl + "/server/memoInfo",
        async: false,
        success: function(data) {
            var html = [];
        		html.push(
        			'	<div class="form-group form-group-1">                                                ',
					'		<label class="control-label">当前的内存使用情况：</label>                        ',
					'	    <div class="form-control">                                                       ',
					'	        总共 <span>',data.data.total,'</span> K, 使用 <span>',data.data.use,'</span> K, 剩余 <span>',data.data.free,'</span> K',
					'	    </div>                                                                           ',
					'	</div>                                                                               ',
					'	<div class="form-group form-group-1">                                                ',
					'		<label class="control-label">2小时来的内存情况：</label>                         ',
					'	    <div class="form-control">                                                       ',
					'	        最小内存 <span>',data.data.min,'</span> K, 最大内存 <span>',data.data.max,'</span> K                  ',
					'	    </div>                                                                           ',
					'    </div>                                                                              ',
					'<div class="clear"></div>		                                                         '
        		)
        			$("#taskRunTop").empty().append(html.join(''));
        },
        error: function(e) {
            $(".loading").hide();
        }
    });
}
//定时器  实时查看监控数据
setInterval(function(){
	taskRun();
	init(1);
},5000)

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
		}
	}
});

function init(index) {
	var params = {};
	params.pageNum = index-1;
    params.pageSize = 10;
    params.agentAndServer=agentName;
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/tasks/pages",
		async: true,
		data: params,
		success: function(data) {
			if(data.error!=null&&data.error!=undefined){
				$(".loading").hide();
	            layer.msg("请求异常", {
	                time : 1500
	            });
			}
			vm.tabData = data.data;               
            vm.showItem = 5;
            vm.allpage = data.totalPages;
            vm.totalNum = data.totalCount;
            if(data.data!=null){
            	if(data.data.length>0&&data!=undefined){
    				vm.current = index;
            		$("#app .no-data").remove();
            	}
            }else{
        		vm.current = 0;
        		$("#app table>tbody").empty();
        		$("#app .no-data").remove();
        		$("#app .pagleft").before('<div class="no-data"><p></p><h4>暂时没有数据</h4></div>');
     	    }
		}
	});
}



