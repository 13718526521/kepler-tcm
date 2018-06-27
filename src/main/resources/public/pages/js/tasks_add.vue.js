$(function(){
	//新建时  隐藏按钮
	var str=location.href; 
	var sUrl = str.substring(str.length-1);
	if(sUrl==1){
		$(".addHide").hide();
	}
})

$(".submit").click(function(){
	window.location.href="tasks.html";
})
$(".reset").click(function(){
	window.location.href="tasks.html";
})