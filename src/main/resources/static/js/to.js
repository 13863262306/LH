$(".Jump").click(function(){$(".metismenu").find("li").removeClass("active");if($(this).attr("to_data")!=null){var data="{";var value=$(this).attr("to_data").split(",");for ( var i = 0; i <value.length; i++){if(i==0){data+="\""+value[i].split("=")[0]+"\":\""+value[i].split("=")[1]+"\"";}else{data+=",\""+value[i].split("=")[0]+"\":\""+value[i].split("=")[1]+"\"";}}data+="}";if($(this).attr("to_url")!=""){addTabs($(this).attr("to_url"),JSON.parse(data));}}else{if($(this).attr("to_url")!=""){addTabs($(this).attr("to_url"));}}});function Jump(value){if($(value).attr("to_data")!=null){var data="{";var value=$(value).attr("to_data").split(",");for ( var i = 0; i <value.length; i++){if(i==0){data+="\""+value[i].split("=")[0]+"\":\""+value[i].split("=")[1]+"\"";}else{data+=",\""+value[i].split("=")[0]+"\":\""+value[i].split("=")[1]+"\"";}}data+="}";if($(value).attr("to_url")!=""){addTabs($(value).attr("to_url"),JSON.parse(data));}}else{if($(value).attr("to_url")!=""){addTabs($(value).attr("to_url"));}}};
var myPost = function (url,data,type){
	$.post(url,data,function(data,status){
		if(data.info!=null){toastr.success(data.name, data.info);  };
		if(data.error!=null){toastr.error(data.name, data.error);};
		if(data.warn!=null){toastr.warning(data.name, data.warn);};
		if(type==null||type){addTabs(replace(url));}});};
		var myPost2 = function (url,data,type){
			$.post(url,data,function(data,status){
				if(data.info!=null){toastr.success(data.name, data.info);  };
				if(data.error!=null){toastr.error(data.name, data.error);};
				if(data.warn!=null){toastr.warning(data.name, data.warn);};
				addTabs(type);});};
				
				var myPost3 = function (url,data,type){
					$.post(url,data,function(data,status){
						if(data.info!=null){toastr.success(data.name, data.info); addTabs(type); };
						if(data.error!=null){toastr.error(data.name, data.error);};
						if(data.warn!=null){toastr.warning(data.name, data.warn);};
						});};
		var myDate=function (){var myDate = new Date();var month = myDate.getMonth() + 1;var strDate = myDate.getDate();if (month >= 1 && month <= 9) {month = "0" + month;}if (strDate >= 0 && strDate <= 9) {strDate = "0" + strDate;}return myDate.getFullYear()+"-"+month+"-"+strDate;};var string2Json=function(value){var Json="";var va=value.split("");for (var i=0;i<va.length;i++) {var c=va[i];switch (c){case "\"":Json+= "\\\"";break; case "\\":Json+="\\\\";break; case "/":Json+="\\/"; break; case "\b":Json+="\\b";break;case "\f":Json+="\\f"; break;case "\n": Json+="\\n";         break;case "\r":Json+="\\r";break;case "\t":Json+="\\t";break;default:Json+=c;}} return Json;};
function plus(val){
	var dr= $(val).parent().parent().parent().parent().parent();
	var le=dr.find(".content").length+1;
var html="";
html+="<div class=\"form-group row\">";
html+=" <label class=\"col-sm-2 col-form-label\">目标"+le+":</label>";
html+="<div class=\"col-sm-10\"> <div class=\"input-group\">";
html+="<textarea type=\"text\" class=\"form-control content\" rows=\"3\" placeholder=\"目标"+le+"\"></textarea>";
html+="<span class=\"input-group-append\"> <button type=\"button\" class=\"btn btn-primary\" onclick=\"plus(this)\">Add</button> </span>";
html+="<span class=\"input-group-append\"> <button type=\"button\" class=\"btn btn-danger\" onclick=\"reduce(this)\">Del</button> </span>";
html+=" </div></div>";$(val).parent().parent().parent().parent().after(html);
dr.find(".content").each(function(index,element){
	 $(element).attr("placeholder","目标"+(index+1));
	 $(element).parent().parent().parent().children("label").text("目标"+(index+1));
});
};


var addTabs = function (url,data){var men=$("#side-menu");var yc=null;men.find("a[to_url='"+replace2(url)+"']").parents("li").each(function(){yc=$(this);yc.siblings().removeClass("active").find("ul").removeClass("in").attr("aria-expanded",false);if(yc.children("ul")){  yc.children("a").attr("aria-expanded",true);yc.children("ul").addClass("in");yc.children("ul").attr("aria-expanded",true);};yc.prop("class","active");});if(yc!=null){yc.siblings().each(function(){$(this).removeClass("active");$(this).find("a").attr("aria-expanded",false);$(this).find("ul").removeClass("in").attr("aria-expanded",false).find("li").removeClass("active");;});};$("#iframe").load(encodeURI(url),data);$.pushState(encodeURI(url),data);};var myBelonging = function (id,url,data){ $(id).load(url,data);};var myHome = function (){$("#side-menu").find("a").each(function(){if($(this).hasClass('Jump')){addTabs($(this).attr('to_url'));return false;}});};

var myFile = function (url,data,ddata,type){ 
	$.ajax({ url : url, type : 'POST', data : data,processData : false,contentType : false,success: function(data){
		if(data==="保存成功"){toastr.success(ddata, data);}else{toastr.error(ddata, data);}
		if(type==null||type){addTabs(replace(url));}
		}
	});};
	var myFile2 = function (url,data,ddata,type){ 
		$.ajax({ url : url, type : 'POST', data : data,processData : false,contentType : false,success: function(data){
			if(data==="保存成功"){toastr.success(ddata, data);}else{toastr.error(ddata, data);}
			if(type!=null){addTabs(replace(type));}
			}
		});};
function reduce(value){var dr= $(value).parent().parent().parent().parent().parent();if(dr.find(".content").length>1){$(value).parent().parent().parent().parent().remove();
dr.find(".content").each(function(index,element){
	 $(element).attr("placeholder","目标"+(index+1));
	 $(element).parent().parent().parent().children("label").text("目标"+(index+1));
 });
}};

function plusType(val){
	var templateId=$(val).parents(".WorkPlanvalue").children("input[name='templateId']");
	   var type=$("select[name='planType']");
	   var types="";
	   type.each(function(index,element){
		  if(index==0){
			  types=$(element).val();
		  }else{
			  types+=","+$(element).val();
		  }
	   });
	  $.get("/workplan/workplanType?templateId="+templateId.val()+"&type="+types,function(data,status){
		  console.log("normal");  
		  if(data!=""){
			  $(val).parent().parent().children("select[name='planType']").attr("disabled",true); 
			  $(val).removeAttr("onclick"); 
		  }
		  $(val).parents("fieldset").parent().after(data);
		  });
};
function reduceType(val){
	if($(".WorkPlanvalue").length>1){
	var id=$(val).parents(".WorkPlanvalue").children("input[name='id']");
	if(id.val()!=""){
	  $.get("/workplan/workplanDelete?workPlanId="+id.val(),function(data,status){
		  });};
		  $(val).parents("fieldset").parent().remove(); }
};
function replace(url){
	url=url.replace("Add","List");
	url=url.replace("Delete","List");
	url=url.replace("Workplan","List");
	url=url.replace("Review","List");
	url=url.replace("inset","List");
	url=url.replace("submission","List");
	url=url.replace("withdraw","List");
	url=url.replace("synchronization","userList");
	return url;
};
function replace2(url){
	url=url.split("?")[0];
	url=url.replace("Add","List");
	url=url.replace("Delete","List");
	url=url.replace("Workplan","List");
	url=url.replace("Review","List");
	url=url.replace("inset","List");
	url=url.replace("submission","List");
	url=url.replace("withdraw","List");
	url=url.replace("synchronization","userList");
	return url;
}
function validate_required(id,value)
{
 var z=id.val();
 if(z==null||z===""){
	 toastr.warning(value);
	return true;
 }else{
	 return false;
 };
};

function Verification_Weight(val){
	var regu = /^0\.?[1-9]$|^[01]$/;
	if(val!=""){
		if(!regu.test(val)){
			toastr.warning("你输入的数据格式不正确");
			return false;
		}else{
			return true;
		}
	}else{
		toastr.warning("你输入的数据不能为空");
		return false;
	}
}
function Verification_Number(val){
	var regu = /^([0-9]*|[0-9]\.[05])$/;
	if(val!=""){
		if(!regu.test(val)){
			return false;
		}else{
			return true;
		}
	}else{
		return true;
	}
}

function getCookie(name) 
{ 
    var arr,reg=new RegExp("(^| )"+name+"=([^;]*)(;|$)");
 
    if(arr=document.cookie.match(reg))
 
        return unescape(arr[2]); 
    else 
        return null; 
} 
var getBJCAkey=function(){
	var BJCAkey= false;
	var strUserList = XTXAPP.SOF_GetUserList();
	while (1) {
		var i = strUserList.indexOf("&&&");
		if (i <= 0 ) {
			break;
		}
		var strOneUser = strUserList.substring(0, i);
		var strName = strOneUser.substring(0, strOneUser.indexOf("||"));
		var strUniqueID = strOneUser.substring(strOneUser.indexOf("||") + 2, strOneUser.length);
		if(strName=='测试用户(测试)'){
			BJCAkey=true;
		}
		var len = strUserList.length;
		strUserList = strUserList.substring(i + 3,len);
	}
	return BJCAkey;
}
function GetUserList(val){
//	var cookie= $("#jing_chen").val();
//	if(cookie!=null){
//		if(!getBJCAkey()){
//			addTabs("grantAuthorization/error?url="+val);
//		}
//	 }
	}
$.extend({
    loadPage: function (url, data) {
       // console.log('loading url: ' + url)
        //$('#iframe').load(url, data);
        addTabs(url,data);
    },
    pushState: function (url, data) {
       // console.log("pushing state: " + url)
        history.pushState({urlStr: url, data: data}, "","");
    },
    popState: function () {
        window.addEventListener("popstate", function () {
            var currentState = history.state
            if (currentState != null) {
                var url = "" + currentState.urlStr
               // console.log('poping state: ' + url)
                $.loadPage(url, currentState.data)
            }
        });
    },
    refresh: function () {
        var currentState = history.state;
        if (currentState != null) {
            var loadUrl = "" + currentState.urlStr
          //  console.log('refreshing state: ' + loadUrl)
            $.loadPage(loadUrl, currentState.date)
        }else{
        	myHome();
        }
    }
}
);

 