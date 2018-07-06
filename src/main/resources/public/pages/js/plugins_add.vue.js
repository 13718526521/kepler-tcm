$(function(){
	
});
$("#addfield").click(function(){
	
	$("#field_list").clone().appendTo("#field");
	
});

$("#file").change(function(){
	
	var file;
	if(this.files && this.files[0]) {
		file = this.files[0];
	} else if(this.files && this.files.item(0)) {
		file = this.files.item(0);
	}
	$(this).parent().find("span").html(file.name);
	var i=file.name.lastIndexOf(".");
	var e="";
	if(i >= 0)
	e=file.name.substring(i).toLowerCase();
	if(e == ".class" || e == ".jar" ){
		$(this).parent().parent().find("input").attr("readOnly",true);
		$(this).parent().parent().find("input").attr("placeholder","无");
		$(this).parent().parent().find("input").attr("style","background-color: rgb(210,210,210);");
		$(this).parent().find("input").attr("style","display: none;");
	}else{
		$(this).parent().parent().find("input").attr("readOnly",false);
		$(this).parent().parent().find("input").attr("placeholder"," ");
		$(this).parent().parent().find("input").attr("style"," ");
		$(this).parent().find("input").attr("style","display: none;");
	}
});

