$(function() {
	
	 $(".required").hover(function() {
			if($(this).val() == '') {
				$(this).popover('show');
			}
		}, function() {
			$(this).popover('hide');
		});
	
	init(1);
	
	$(".accordion").on("click","dt>a",function(e){
		 var hrefN = $(this).attr("href").slice(1),
		 current_dl=$(this).parent().parent(),
		 val2=$(this).attr("value2"),
		 val3=$(this).attr("value3"),
		 hrefIndex = hrefN.split("accordion")[1];
		 if(!$(this).parent("dt").hasClass("active")){
	            /*页面显示切换*/
			 	current_dl.find("dd").remove();
	            $(this).html("隐藏");
	            $(this).parent("dt").addClass("active");
	            current_dl.append(formData(val2,val3,hrefIndex));
	            $(this).parent("dt").siblings("dd").addClass("active");
	            

	            $(this).parent("dt").parent().siblings("dl").find("dt>a").html("显示");
	            $(this).parent("dt").parent().siblings("dl").removeClass("active");
	            $(this).parent("dt").parent().siblings("dl").find("dt").removeClass("active");
	            $(this).parent("dt").parent().siblings("dl").find("dd").removeClass("active");
		 }else{
			    $(this).html("显示");
			    $(this).parent("dt").removeClass("active");
	            $(this).parent("dt").parent().find("dd").removeClass("active");
		 }
	 });
	
	
	
	$.ajax({
        type: "get",
        url: util.agent().baseUrl + "/agent/query",
        data: {},
        async: true,
        success: function(data) {
        	if(data.CODE==0){
        		arr=data.data;
	        	for (var i in arr){
	        		id=arr[i].id;
	        		agentName=arr[i].agentName;
					agent=agentName.split(":")[0];
					port=agentName.split(":")[1];
					agentconnect(id,agent,port);
	        	}
        	}else{
        	}
        },
		error: function() {
		}
    });
	
});



var initFunc = function(){
	return {
		dataManage: function(agentName,serverName,started){
			if(started=='true'){
				window.location.href="database_config.html?agentName="+agentName+"@"+serverName;		
			}else{
				alert('应用服务器尚未启动，请先启动！');
			}
		},
		taskManage: function(agentName,serverName,started){
			if(started=='true'){
				window.location.href="tasks.html?agentName="+agentName+"@"+serverName;		
			}else{
				alert('应用服务器尚未启动，请先启动！');
			}
		},
		pluginManage: function(agentName,serverName,started){
			if(started=='true'){
				window.location.href="plugins.html?agentName="+agentName+"@"+serverName;		
			}else{
				alert('应用服务器尚未启动，请先启动！');
			}
		},
		startServer: function(agentName,serverName){
			 $.ajax({
	              type: "post",
	              url: util.agent().baseUrl + "/server/start",
	              data: {"agentName":agentName,"serverName":serverName},
	              success: function(data) {
	            	  if(data.CODE==1){
	            		 alert(data.MESSAGE);
	            	  }else{
		         		 layer.msg("启动成功", { time: 1000 });
		         		 init(1);
	            	  }
	              },
	      		error: function() {
	    		}
	          });
		},
		stopServer: function(agentName,serverName){
			 $.ajax({
	              type: "post",
	              url: util.agent().baseUrl + "/server/stop",
	              data: {"agentName":agentName,"serverName":serverName},
	              success: function(data) {
	            	  if(data.CODE==1){
	            		 alert(data.MESSAGE);
	            	  }else{
		         		 layer.msg("停止成功", { time: 1000 });
		         		init(1);
	            	  }
	              },
	      		error: function() {
	    		}
	          });
		},
		restartServer: function(agentName,serverName){
			 $.ajax({
	              type: "post",
	              url: util.agent().baseUrl + "/server/restart",
	              data: {"agentName":agentName,"serverName":serverName},
	              success: function(data) {
	            	  if(data.CODE==1){
	            		 alert(data.MESSAGE);
	            	  }else{
		         		 layer.msg("重启成功", { time: 1000 });
		         		init(1);
	            	  }
	              },
	      		error: function() {
	    		}
	          });
		},
		dataChange: function(agentName,agentmemo,serverName,port,memo,monitorPort,monitorInterval){
			$(".application-new-modal .modal-title").text('应用服务器 - 修改');
			$("#agentName").val(agentName);
            $("#agentMemo").val(agentmemo);
            $("#serverName").attr("disabled","disabled");
            $("#serverName").val(serverName);
            $("#serverPort").val(port);
            $("#applicationmemo").val(memo);
            $("#monitorPort").val(monitorPort);
            $("#monitorInterval").val(monitorInterval);
            $('.application-new-modal button[type=submit]').unbind('click').click(function() {
            	var sub_Data = {};
				sub_Data.agentName = $("#agentName").val();
				sub_Data.serverName = $("#serverName").val();
				sub_Data.autoRestart = $("#autoRestart").val();
				sub_Data.monitorInterval = $("#monitorInterval").val();
				sub_Data.serverPort = $("#serverPort").val();
				sub_Data.monitorPort = $("#monitorPort").val();
				sub_Data.memo = $("#applicationmemo").val();
				if(sub_Data.serverPort=='' || sub_Data.serverPort==""){
					$("#serverPort").attr("data-content","必填项不能为空");
			        $("#serverPort").popover('show');
			        setTimeout(function () {
			            $("#serverPort").popover('hide');
			        }, 3000);
			        return;
				}
				if(sub_Data.monitorPort=='' || sub_Data.monitorPort==""){
					$("#monitorPort").attr("data-content","必填项不能为空");
			        $("#monitorPort").popover('show');
			        setTimeout(function () {
			            $("#monitorPort").popover('hide');
			        }, 3000);
			        return;
				}
				if (!(/^[0-9]*$/.test(sub_Data.serverPort))) {
			        $("#serverPort").attr("data-content","应用服务器端口号只能为数字");
			        $("#serverPort").popover('show');
			        setTimeout(function () {
			            $("#serverPort").popover('hide');
			        }, 3000);
			        return;
				}
				if (!(/^[0-9]*$/.test(sub_Data.monitorPort))) {
					 $("#monitorPort").attr("data-content","应用服务器监控端口号只能为数字");
				     $("#monitorPort").popover('show');
			         setTimeout(function () {
			            $("#monitorPort").popover('hide');
			         }, 3000);
			         return;
				}
				if(sub_Data.serverPort<1 || sub_Data.serverPort>65535){
					$("#serverPort").attr("data-content","应用服务器端口号必须为1~65535");
				    $("#serverPort").popover('show');
			        setTimeout(function () {
			            $("#serverPort").popover('hide');
			        }, 3000);
					return;
				}
				if(sub_Data.monitorPort<1 || sub_Data.monitorPort>65535){
					$("#monitorPort").attr("data-content","应用服务器监控端口号必须为1~65535");
				    $("#monitorPort").popover('show');
			        setTimeout(function () {
			            $("#monitorPort").popover('hide');
			        }, 3000);
					return;
				}
				 $.ajax({
		              type: "post",
		              url: util.agent().baseUrl + "/server/edit",
		              data: {"agentName":sub_Data.agentName,"serverName":sub_Data.serverName,"autoRestart":sub_Data.autoRestart,"monitorInterval":sub_Data.monitorInterval,"serverPort":sub_Data.serverPort,"monitorPort":sub_Data.monitorPort,"memo":sub_Data.memo},
		              success: function(data) {
		            	  if(data.CODE==1){
		            		 alert(data.MESSAGE);
		            	     setTimeout(function () {
		            	    	 $("#application-addChange .close").click();
		            	    	 }, 1000);  
		            	  }else{
			         		 layer.msg("修改成功", { time: 1000 });
			         		 $("#application-addChange .close").click();
			         		init(1);
		            	  }
		              },
		      		error: function() {
		    		}
		          });
            });
		}
	}
}();

function agentedit(oldAgent,edit_agent,edit_port,edit_memo){
	 $.ajax({
         type: "post",
         url: util.agent().baseUrl + "/agent/edit",
         data: {"oldagent":oldAgent,"agent":edit_agent,"port":edit_port,"memo":edit_memo},
         success: function(data) {
       	  if(data.CODE==1){
        		 alert(data.MESSAGE);
        	     setTimeout(function () {
        	    	 $("#agent-addChange .close").click();
        	    	 }, 1000);  
        	  }else{
         		 layer.msg("修改成功", { time: 1000 });
         		 $("#agent-addChange .close").click();
         		 init(1);
        	  }
         },
 		error: function() {
		}
         });
}
function agentconnect(id,agent,port){
	$.ajax({
        type: "post",
        url: util.agent().baseUrl + "/agent/connect",
        data: {"agent":agent,"port":port},
        success: function(data) {
        	if(data.CODE==1){
        		$("#agent_flag_"+id).attr("class","fa fa-remove");
        		$("#agent_flag_"+id).attr("style","margin-right: 10px;color:red;");
        		$("#agent_flag_"+id).attr("value",data.MESSAGE);
        	}else{
        		$("#agent_flag_"+id).attr("class","fa fa-check");
        		$("#agent_flag_"+id).attr("style","margin-right: 10px;color:green;");
        		$("#agent_btn_"+id).removeAttr("disabled");
        	}
        },
		error: function() {
		}
    });
}

function agentconnectmsg(id,agentName){
	var str=document.getElementById("agent_flag_"+id).className;
	if(str=='fa fa-remove'){
		 val=$("#agent_flag_"+id).attr("value");
		 alert(val);
	}else if(str=='fa fa-server'){
		alert('未验证连接状态');
	}else{
		alert('连接成功');
	}
}

function agentdelete(agentName){
	 $.ajax({
         type: "post",
         url: util.agent().baseUrl + "/agent/delete",
         data: {"agentName":agentName},
         success: function(data) {
       	  if(data.CODE==1){
       		  alert(data.MESSAGE);
        	      setTimeout(function () {
        	    	  $("#agent-deleteChange .close").click();
        	    	  }, 1000);  
        	  }else{
         		 layer.msg("删除成功", { time: 1000 });
         		  $("#agent-deleteChange .close").click();
         		 init(1);
        	  }
         },
 		error: function() {
		}
         });
}

$(".agent-add-btn").click(function() {
	$(".agent-new-modal .modal-title").text('代理服务器 - 新增');
		$("#agent").val("");
		$("#port").val("");
		$("#memo").val("");
});
function formData(agentName,agentmemo,hrefIndex){
	var str='';
    var tabData;
	 $.ajax({
         type: "post",
         url: util.agent().baseUrl + "/server/querystate",
         data: {"agentName":agentName},
         async: false,
         success: function(data) {
        	 tabData = data.data;
        	 str = '<dd class="accordion-content accordionItem is-collapsed active"  id="accordion'+hrefIndex+'" aria-hidden="true">'+
	 			'<div class="form-content" id="formcontent'+hrefIndex+'">'+
	 				'<div id="app'+hrefIndex+'">'+
	 					'<table style="font-size:13px;width: 100%;" border="0" class="table table-hover  tab-own" cellpadding="0" cellspacing="0">'+
	 						'<thead>'+
	 							'<tr>'+
	 								'<th>'+'应用服务器名称'+'</th>'+
	 								'<th>'+'应用服务器端口'+'</th>'+
	 								'<th>'+'应用服务器说明'+'</th>'+
	 								'<th>'+'应用服务器监控端口'+'</th>'+
	 								'<th>'+'应用服务器监控间隔'+'</th>'+
	 								'<th>'+'操作'+'</th>'+
								'</tr>'+
							'</thead>'+
						'<tbody>';
        	 for (var i in tabData){
					if(tabData[i].started==true){
						i_class_flag="fa fa-play";
						i_color_flag="green";
						href_flag="tasks.html?agentName="+agentName+"@"+tabData[i].serverName;
					}else{
						i_class_flag="fa fa-stop";
						i_color_flag="red";
						/*href_flag="javascript:return false;";*/
						href_flag="javascript:alert('应用服务器尚未启动，请先启动！');";
					}
        		 str +='<tr>'+
							'<td>'+'<a href="'+href_flag+'">'+'<i class="'+i_class_flag+'" style="margin-right: 5px;color: '+i_color_flag+';">'+'</i>'+tabData[i].serverName+'</a>'+'</td>'+
							'<td>'+tabData[i].port+'</td>'+
							'<td>'+tabData[i].memo+'</td>'+
							'<td>'+tabData[i].monitorPort+'</td>'+
							'<td>'+tabData[i].monitorInterval+'秒'+'</td>'+
							'<td>'+
							'<span class="fa fa-tasks taskManage" onclick="initFunc.taskManage(\''+agentName+'\',\''+tabData[i].serverName+'\',\''+tabData[i].started+'\')" title="任务管理" style=" padding-right: 5px;color:rgb(0,49,79);">'+'</span>'+
							'<span class="fa fa-database dataManage" onclick="initFunc.dataManage(\''+agentName+'\',\''+tabData[i].serverName+'\',\''+tabData[i].started+'\')" title="数据库配置" style=" padding-right: 5px;color:rgb(186,40,53);">'+'</span>'+
							'<span class="fa fa-plug pluginManage" onclick="initFunc.pluginManage(\''+agentName+'\',\''+tabData[i].serverName+'\',\''+tabData[i].started+'\')" title="插件管理" style=" padding-right: 5px;color:rgb(203,203,56);">'+'</span>'+
							'<span class="fa fa-play-circle startServer" onclick="initFunc.startServer(\''+agentName+'\',\''+tabData[i].serverName+'\')" title="启动" data-toggle="modal" style=" padding-right: 5px;color:rgb(3,22,52);">'+'</span>'+
							'<span class="fa fa-stop stopServer" onclick="initFunc.stopServer(\''+agentName+'\',\''+tabData[i].serverName+'\')" title="停止" data-toggle="modal" style=" padding-right: 5px;color:rgb(153,102,202);">'+'</span>'+
							'<span class="fa fa-repeat restartServer" onclick="initFunc.restartServer(\''+agentName+'\',\''+tabData[i].serverName+'\')" title="重启" data-toggle="modal" style=" padding-right: 5px;color:rgb(254,102,101);">'+'</span>'+
							'<span class="fa fa-edit dataChange" onclick="initFunc.dataChange(\''+agentName+'\',\''+agentmemo+'\',\''+tabData[i].serverName+'\',\''+tabData[i].port+'\',\''+tabData[i].memo+'\',\''+tabData[i].monitorPort+'\',\''+tabData[i].monitorInterval+'\')" title="修改" data-toggle="modal" data-target=".application-new-modal" style="color: green;padding-right: 5px;">'+'</span>'+
						'</td>'+
					'</tr>';
        	 }
				str+='</tbody>'+
				'</table>'+				
			'</div>'+
		'</div>'+
	'</dd>';
         },
 		error: function() {
		}
     });
return str;
};
$('.agent-new-modal button[type=submit]').unbind('click').click(
		function() {
			var sub_Data = {};
			sub_Data.agent = $("#agent").val();
			sub_Data.port = $("#port").val();
			sub_Data.memo = $("#memo").val();
			for(var i = 0; i < $("#agent-addChange .required").length; i++) {
				if($("#agent-addChange .required").eq(i).val() == '') {
					$("#agent-addChange .required").attr("data-content","必填项不能为空");
		            $("#agent-addChange .required").eq(i).focus().popover('show');
		            setTimeout(function () {
		                $("#agent-addChange .required").eq(i).popover('hide');
		            }, 1500);
		            return;
				}
			}
			if (!(/^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$/.test(sub_Data.agent))) {
				$("#agent").attr("data-content","请填写有效服务器代理地址");
		        $("#agent").popover('show');
		        setTimeout(function () {
		            $("#agent").popover('hide');
		        }, 3000);
		        return;
			}
	    	if (!(/^[0-9]*$/.test(sub_Data.port))) {
		        $("#port").attr("data-content","服务器代理端口号只能为数字");
		        $("#port").popover('show');
		        setTimeout(function () {
		            $("#port").popover('hide');
		        }, 3000);
		        return;
			}
			if(sub_Data.port<1 || sub_Data.port>65535){
				$("#port").attr("data-content","服务器代理端口号必须为1~65535");
			    $("#port").popover('show');
		        setTimeout(function () {
		            $("#port").popover('hide');
		        }, 3000);
				return;
			}
		  $.ajax({
              type: "post",
              url: util.agent().baseUrl + "/agent/add",
              data: {"agent":sub_Data.agent,"port":sub_Data.port,"memo":sub_Data.memo},
              success: function(data) {
            	  if(data.CODE==1){
            		 alert(data.MESSAGE);
            	     setTimeout(function () {
            	    	 $("#agent-addChange .close").click();
            	    	 }, 1000);  
            	  }else{
	         		 layer.msg("添加成功", { time: 1000 });
	         		 $("#agent-addChange .close").click();
	         		 init(1);
            	  }
              },
      		error: function() {
    		}
          });
		});
function init(index) {
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/agent/query",
		async: true,
		success: function(data) {
			var arr = new Array();
			if(data.CODE==0){
				arr=data.data;
				html ='';
				for (var i in arr){
					if(arr[i].id=='1'){
						html='<dl>'+
								'<dt class="active">'+
									'<h4 class="activity-title">'+
										'<i class="fa fa-server" id="agent_flag_'+arr[i].id+'" style="margin-right: 10px;color:rgb(42,195,193);" onclick=agentconnectmsg(\''+arr[i].id+'\',\''+arr[i].agentName+'\')>'+
										'</i>'+
										'<span>'+
											'代理服务器'+
										'</span>'+
										'<span style="margin-left: 10px;"><font color="orange">'+arr[i].agentName+'</font></span>'+
											'<span style="margin-left: 10px;">'+
												'-'+
											'</span>'+
											'<span><font color="teal">'+arr[i].memo+'</font></span>'+
											'</h4>'+
											'<a href="#accordion_'+arr[i].id+'" aria-expanded="false" aria-controls="accordion_'+arr[i].id+'" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" class="accordion-title accordionTitle js-accordionTrigger">'+
												'隐藏'+
											'</a>'+
												'<div class="ope-btn">'+
													'<button  type="button" class="btn btn-blue agent-edit-btn" data-toggle="modal" data-target=".agent-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" style="margin-right:5px;">'+
														'<span class="glyphicon glyphicon-edit" style="padding-right:5px;">'+
														'</span>'+
															'修改'+
													'</button>'+
														'<button  type="button" class="btn btn-blue agent-delete-btn" data-toggle="modal" data-target=".agent-delete-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" >'+
															'<span class="glyphicon glyphicon-trash" style="padding-right:5px;">'+
															'</span>'+
																'删除'+
															'</button>'+
												'</div>'+
													'<div class="opt-btn2">'+
														'<button  type="button" id="agent_btn_'+arr[i].id+'" disabled="disabled" class="btn btn-blue application-add-btn" data-toggle="modal" data-target=".application-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" >'+
															'<span class="glyphicon glyphicon-plus" style="padding-right:5px;">'+'</span>'+'添加应用服务器'+'</button>'+
													'</div>'+
											'</dt>'+
									'<div class="clear">'+
							'</div>';
						html += formData(arr[i].agentName,arr[i].memo,'_1');
						html += '</dl>';
					}else{
						html +='<dl>'+
								'<dt class="">'+
									'<h4 class="activity-title">'+
										'<i class="fa fa-server" id="agent_flag_'+arr[i].id+'" style="margin-right: 10px;color:rgb(42,195,193);" onclick=agentconnectmsg(\''+arr[i].id+'\',\''+arr[i].agentName+'\')>'+
										'</i>'+
										'<span>'+
											'代理服务器'+
										'</span>'+
										'<span style="margin-left: 10px;"><font color="orange">'+arr[i].agentName+'</font></span>'+'<span style="margin-left: 10px;">'+
											'-'+
										'</span>'+
										'<span><font color="teal">'+arr[i].memo+'</font></span>'+
											'</h4>'+
												'<a href="#accordion_'+arr[i].id+'"  aria-expanded="false" aria-controls="accordion_'+arr[i].id+'" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" class="accordion-title accordionTitle js-accordionTrigger">'+
													'显示'+
												'</a>'+
													'<div class="ope-btn">'+
														'<button  type="button" class="btn btn-blue agent-edit-btn" data-toggle="modal" data-target=".agent-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" style="margin-right:5px;">'+
															'<span class="glyphicon glyphicon-edit" style="padding-right:5px;">'+
																'</span>'+
																	'修改'+
														'</button>'+
																'<button  type="button" class="btn btn-blue agent-delete-btn" data-toggle="modal" data-target=".agent-delete-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" >'+
																	'<span class="glyphicon glyphicon-trash" style="padding-right:5px;">'+
																	'</span>'+
																		'删除'+
																'</button>'+
															'</div>'+
																'<div class="opt-btn2">'+
																	'<button  type="button" id="agent_btn_'+arr[i].id+'" disabled="disabled" class="btn btn-blue application-add-btn" data-toggle="modal" data-target=".application-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" >'+
																		'<span class="glyphicon glyphicon-plus" style="padding-right:5px;">'+
																		'</span>'+
																			'添加应用服务器'+
																		'</button>'+
																'</div>'+
														'</dt>'+
												'<div class="clear">'+
										'</div>'+
								'<dd>'+
						'</dd>'+
				'</dl>';
					}
					}
				$(".accordion").empty().append(html);
				$(".accordion .agent-edit-btn").click(function() {
					$(".agent-new-modal .modal-title").text('代理服务器 - 修改');
					oldAgent=$(this).attr("value2");
					agent=oldAgent.split(":")[0];
					port=oldAgent.split(":")[1];
					memo=$(this).attr("value3");
					$("#agent").val(agent);
					$("#port").val(port);
					$("#memo").val(memo);
					$(".agent-new-modal button[type=submit]").unbind('click').click(function() {
						var edit_agent=$("#agent").val();
						var edit_port=$("#port").val();
						var edit_memo=$("#memo").val();
						if(edit_agent=='' || edit_agent==""){
							$("#agent").attr("data-content","必填项不能为空");
					        $("#agent").popover('show');
					        setTimeout(function () {
					            $("#agent").popover('hide');
					        }, 3000);
					        return;
						}
						if(edit_port=='' || edit_port==""){
							$("#port").attr("data-content","必填项不能为空");
					        $("#port").popover('show');
					        setTimeout(function () {
					            $("#port").popover('hide');
					        }, 3000);
					        return;
						}
						if (!(/^((25[0-5]|2[0-4]\d|[01]?\d\d?)\.){3}(25[0-5]|2[0-4]\d|[01]?\d\d?)$/.test(edit_agent))) {
							$("#agent").attr("data-content","请填写有效服务器代理地址");
					        $("#agent").popover('show');
					        setTimeout(function () {
					            $("#agent").popover('hide');
					        }, 3000);
					        return;
						}
						if (!(/^[0-9]*$/.test(edit_port))) {
					        $("#port").attr("data-content","服务器代理端口号只能为数字");
					        $("#port").popover('show');
					        setTimeout(function () {
					            $("#port").popover('hide');
					        }, 3000);
					        return;
						}
						if(edit_port<1 || edit_port>65535){
							$("#port").attr("data-content","服务器代理端口号必须为1~65535");
						    $("#port").popover('show');
					        setTimeout(function () {
					            $("#port").popover('hide');
					        }, 3000);
							return;
						}
						agentedit(oldAgent,edit_agent,edit_port,edit_memo);
						init(1);
					});
				});
				$(".accordion .agent-delete-btn").click(function() {
					$(".agent-delete-modal .modal-title").text('提示信息');
					id=$(this).attr("value");
					agentName=$(this).attr("value2");
					value=id+" "+"("+agentName+")"+" ";
					$(".agent-delete-modal .modal-body>div:nth-child(2)").text(value);
					$(".agent-delete-modal button[type=submit]").unbind('click').click(function() {
						agentdelete(agentName);
						init(1);
					});
				});
				$(".accordion .application-add-btn").click(function() {
					$(".application-new-modal .modal-title").text('应用服务器 - 新增');
					$("#agentName").val($(this).attr("value2"));
					$("#agentMemo").val($(this).attr("value3"));
					$("#serverName").removeAttr("disabled");
					$("#serverName").val("");
					$("#serverPort").val("");
					$("#applicationmemo").val("");
					$("#monitorPort").val("");
					$("#monitorInterval").val(300);
					$('.application-new-modal button[type=submit]').unbind('click').click(
							function() {
								var sub_Data = {};
								sub_Data.agentName = $("#agentName").val();
								sub_Data.serverName = $("#serverName").val();
								sub_Data.autoRestart = $("#autoRestart").val();
								sub_Data.monitorInterval = $("#monitorInterval").val();
								sub_Data.serverPort = $("#serverPort").val();
								sub_Data.monitorPort = $("#monitorPort").val();
								sub_Data.memo = $("#applicationmemo").val();
								for(var i = 0; i < $("#application-addChange .required").length; i++) {
									if($("#application-addChange .required").eq(i).val() == '') {
										$("#application-addChange .required").attr("data-content","必填项不能为空");
							            $("#application-addChange .required").eq(i).focus().popover('show');
							            setTimeout(function () {
							                $("#application-addChange .required").eq(i).popover('hide');
							            }, 1500);
							            return;
									}
								}
								if (!(/^[0-9a-zA-Z_]{1,}$/.test(sub_Data.serverName))) {
									$("#serverName").attr("data-content","请填写有效应用服务器");
							        $("#serverName").popover('show');
							        setTimeout(function () {
							            $("#serverName").popover('hide');
							        }, 3000);
							        return;
								}
								if (!(/^[0-9]*$/.test(sub_Data.serverPort))) {
							        $("#serverPort").attr("data-content","服务器端口号只能为数字");
							        $("#serverPort").popover('show');
							        setTimeout(function () {
							            $("#serverPort").popover('hide');
							        }, 3000);
							        return;
								}
								if (!(/^[0-9]*$/.test(sub_Data.monitorPort))) {
							        $("#monitorPort").attr("data-content","服务器监控端口号只能为数字");
							        $("#monitorPort").popover('show');
							        setTimeout(function () {
							            $("#monitorPort").popover('hide');
							        }, 3000);
							        return;
								}
								if(sub_Data.serverPort<1 || sub_Data.serverPort>65535){
									$("#serverPort").attr("data-content","服务器端口号必须为1~65535");
								    $("#serverPort").popover('show');
							        setTimeout(function () {
							            $("#serverPort").popover('hide');
							        }, 3000);
									return;
								}
								if(sub_Data.monitorPort<1 || sub_Data.monitorPort>65535){
									$("#monitorPort").attr("data-content","服务器监控端口号必须为1~65535");
								    $("#monitorPort").popover('show');
							        setTimeout(function () {
							            $("#monitorPort").popover('hide');
							        }, 3000);
									return;
								}
								 $.ajax({
						              type: "post",
						              url: util.agent().baseUrl + "/server/add",
						              data: {"agentName":sub_Data.agentName,"serverName":sub_Data.serverName,"autoRestart":sub_Data.autoRestart,"monitorInterval":sub_Data.monitorInterval,"serverPort":sub_Data.serverPort,"monitorPort":sub_Data.monitorPort,"memo":sub_Data.memo},
						              success: function(data) {
						            	  if(data.CODE==1){
						            		 alert(data.MESSAGE);
						            	     setTimeout(function () {
						            	    	 $("#application-addChange .close").click();
						            	    	 }, 1000);  
						            	  }else{
							         		 layer.msg("添加成功", { time: 1000 });
							         		 $("#application-addChange .close").click();
							         		init(1);
						            	  }
						              },
						      		error: function() {
						    		}
						          });
							});
				});
			}else{
				$(".accordion").empty().append('<div class="no-data" style="width:calc(100% - 20px);"><p></p><h4>暂时没有数据</h4></div>');
			}
		},
		error:function(){
        }
	});
}


setInterval(function(){
	$.ajax({
        type: "get",
        url: util.agent().baseUrl + "/agent/query",
        data: {},
        success: function(data) {
        	if(data.CODE==0){
        		arr=data.data;
	        	for (var i in arr){
	        		id=arr[i].id;
	        		agentName=arr[i].agentName;
					agent=agentName.split(":")[0];
					port=agentName.split(":")[1];
					agentconnect(id,agent,port);
	        	}
        	}else{
        	}
        },
		error: function() {
		}
    });
	init(1);
}, 60000);
