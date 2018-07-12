$(function() {
	
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
    
});

var initFunc = function(){
	return {
		dataManage: function(agentName,serverName,started){
			if(started=='true'){
				window.location.href="plugins.html?server="+agentName+"@"+serverName;		
			}else{
				window.location.href="server.html";
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
	            	  }
	              },
	      		error: function() {
	      			alert('请求失败');
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
	            	  }
	              },
	      		error: function() {
	      			alert('请求失败');
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
	            	  }
	              },
	      		error: function() {
	      			alert('请求失败');
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
				if (!(/^[0-9]*$/.test(sub_Data.serverPort))) {
					alert('应用服务器端口号只能为数字');
					return;
				}
				if (!(/^[0-9]*$/.test(sub_Data.monitorPort))) {
					alert('应用服务器监控端口号只能为数字');
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
		            	  }
		              },
		      		error: function() {
		      			alert('请求失败');
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
        	  }
         },
 		error: function() {
 			alert('请求失败');
		}
         });
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
        	  }
         },
 		error: function() {
 			alert('请求失败');
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
						href_flag="javascript:return false;";
					}
        		 str +='<tr>'+
							'<td>'+'<a href="'+href_flag+'">'+'<i class="'+i_class_flag+'" style="margin-right: 5px;color: '+i_color_flag+';">'+'</i>'+tabData[i].serverName+'</a>'+'</td>'+
							'<td>'+tabData[i].port+'</td>'+
							'<td>'+tabData[i].memo+'</td>'+
							'<td>'+tabData[i].monitorPort+'</td>'+
							'<td>'+tabData[i].monitorInterval+'秒'+'</td>'+
							'<td>'+
							'<span class="fa fa-arrows-alt dataManage" onclick="initFunc.dataManage(\''+agentName+'\',\''+tabData[i].serverName+'\',\''+tabData[i].started+'\')" title="管理" style=" padding-right: 5px;">'+'</span>'+
							'<span class="fa fa-play-circle startServer" onclick="initFunc.startServer(\''+agentName+'\',\''+tabData[i].serverName+'\')" title="启动" data-toggle="modal" style=" padding-right: 5px;">'+'</span>'+
							'<span class="fa fa-stop stopServer" onclick="initFunc.stopServer(\''+agentName+'\',\''+tabData[i].serverName+'\')" title="停止" data-toggle="modal" style=" padding-right: 5px;">'+'</span>'+
							'<span class="fa fa-repeat restartServer" onclick="initFunc.restartServer(\''+agentName+'\',\''+tabData[i].serverName+'\')" title="重启" data-toggle="modal" style=" padding-right: 5px;">'+'</span>'+
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
 			alert('请求失败');
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
			if (!(/^[0-9]*$/.test(sub_Data.port))) {
				alert('服务器代理端口号只能为数字');
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
            	  }
              },
      		error: function() {
      			alert('请求失败');
    		}
          });
		});
function init(index) {
	$.ajax({
		type: "get",
		url: util.agent().baseUrl + "/agent/querystate",
		async: true,
		success: function(data) {
			var arr = new Array();
			var i_class,i_color;
			if(data.CODE==0){
				arr=data.data;
				html ='';
				str='';
				for (var i in arr){
					if(arr[i].state_code=='0'){
						i_class="fa fa-check";
						i_color="green";
						btn_flag='';
					}else{
						i_class="fa fa-remove";
						i_color="red";
						btn_flag='disabled="disabled"';
					}
					if(arr[i].id=='1'){
						html='<dl>'+
								'<dt class="active">'+
									'<h4 class="activity-title">'+
										'<i class="'+i_class+'" style="margin-right: 10px;color: '+i_color+';">'+
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
											'<a href="#accordion_'+arr[i].id+'" aria-expanded="false" aria-controls="accordion_'+arr[i].id+'" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'" class="accordion-title accordionTitle js-accordionTrigger">'+
												'隐藏'+
											'</a>'+
												'<div class="ope-btn">'+
													'<button  type="button" class="btn btn-blue agent-edit-btn" data-toggle="modal" data-target=".agent-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'" style="margin-right:5px;">'+
														'<span class="glyphicon glyphicon-edit" style="padding-right:5px;">'+
														'</span>'+
															'修改'+
													'</button>'+
														'<button  type="button" class="btn btn-blue agent-delete-btn" data-toggle="modal" data-target=".agent-delete-modal" value="'+arr[i].q+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'">'+
															'<span class="glyphicon glyphicon-trash" style="padding-right:5px;">'+
															'</span>'+
																'删除'+
															'</button>'+
												'</div>'+
													'<div class="opt-btn2">'+
														'<button  type="button" '+btn_flag+' class="btn btn-blue application-add-btn" data-toggle="modal" data-target=".application-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'">'+
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
										'<i class="'+i_class+'" style="margin-right: 10px;color: '+i_color+';">'+
										'</i>'+
										'<span>'+
											'代理服务器'+
										'</span>'+
										'<span style="margin-left: 10px;"><font color="orange">'+arr[i].agentName+'</font></span>'+'<span style="margin-left: 10px;">'+
											'-'+
										'</span>'+
										'<span><font color="teal">'+arr[i].memo+'</font></span>'+
											'</h4>'+
												'<a href="#accordion_'+arr[i].id+'"  aria-expanded="false" aria-controls="accordion_'+arr[i].id+'" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'" class="accordion-title accordionTitle js-accordionTrigger">'+
													'显示'+
												'</a>'+
													'<div class="ope-btn">'+
														'<button  type="button" class="btn btn-blue agent-edit-btn" data-toggle="modal" data-target=".agent-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'" style="margin-right:5px;">'+
															'<span class="glyphicon glyphicon-edit" style="padding-right:5px;">'+
																'</span>'+
																	'修改'+
														'</button>'+
																'<button  type="button" class="btn btn-blue agent-delete-btn" data-toggle="modal" data-target=".agent-delete-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'">'+
																	'<span class="glyphicon glyphicon-trash" style="padding-right:5px;">'+
																	'</span>'+
																		'删除'+
																'</button>'+
															'</div>'+
																'<div class="opt-btn2">'+
																	'<button  type="button" '+btn_flag+' class="btn btn-blue application-add-btn" data-toggle="modal" data-target=".application-new-modal" value="'+arr[i].id+'" value2="'+arr[i].agentName+'" value3="'+arr[i].memo+'" value4="'+arr[i].state_message+'" value5="'+arr[i].state_code+'">'+
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
						agentedit(oldAgent,edit_agent,edit_port,edit_memo);
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
								if (!(/^[0-9]*$/.test(sub_Data.serverPort))) {
									alert('应用服务器端口号只能为数字');
									return;
								}
								if (!(/^[0-9]*$/.test(sub_Data.monitorPort))) {
									alert('应用服务器监控端口号只能为数字');
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
						            	  }
						              },
						      		error: function() {
						      			alert('请求失败');
						    		}
						          });
							});
				});
			}else{
				alert('暂时没有数据');
			}
		},
		error:function(){
			alert('请求失败');
        }
	});
}
