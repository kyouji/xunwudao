<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道-第三方登陆</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<script src="/client/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script>
var seed=60;    //60秒  
var t1=null; 
$(document).ready(function(){
	alert("请先绑定您的手机号！");

	$("#agree_check").click(
		function(){
			$("#agree_check").toggleClass("checked");
			
			if($("#reg_submit").attr("disabled")=="disabled")
			{
				$("#reg_submit").removeAttr("disabled","disabled");
				$("#reg_submit").css("background-color","#72d377");
			}
			else{
				$("#reg_submit").attr("disabled","disabled");
				$("#reg_submit").css("background-color","#999999");
			}
		}
	);
	
	//注册提交
	$("#reg_submit").bind("click", function(){
		var mobile=$("#txt_regMobile").val();
		var mCode=$("#txt_regMcode").val();
		
		var rfCode=$("#txt_rfCode").val();
		var openid=$("#txt_openid").val();
		var unionid=$("#txt_unionid").val();
		var qqOpenid=$("#txt_qqOpenid").val();
		var username=$("#txt_username").val();
		var password=$("#txt_password").val();
		var headImageUrl = $("#txt_headImageUrl").val();
		var nickname = $("#txt_nickname").val();
		var sex = $("#txt_sex").val();
		var address = $("#txt_address").val();
		var province = $("#txt_province").val();
		
		$.ajax({  
            url : "/user/set/mobile",  
            async : true,  
            type : 'POST',  
            data : {"mobile": mobile,
            			"smsCode": mCode,
            			"rfCode": rfCode,
            			"openid": openid,
            			"unionid":unionid,
            			"qqOpenid":qqOpenid,
            			"headImageUrl":headImageUrl,
            			"nickname":nickname,
            			"sex":sex,
            			"address":address,
            			"province":province,
            			"username": username,
            			"password": password},  
            success : function(data) {  
                if(data.code==1){
                	if(typeof(data.addall != "undefined")){
                		if(confirm(data.msg)){
                			changeTo(data.addall,openid,unionid,username,password);
                		}
                	}else{
                		alert(data.msg);
	                	if(typeof(data.id) != "undefined"){
	                		document.getElementById(data.id).focus;
	                	}
                	}
                }
                else if(data.code==0){
                	alert("绑定成功");
                	if(typeof(data.url) != "undefined"){
                		location.href=data.url;
                	}
                	else{
                		location.href='/user/center';
                	}
                }
             }   
        });
 
	}); 
	
	//手机验证码
    $("#smsCodeBtn").bind("click", function() {  
        
        var mob = $('#txt_regMobile').val();
        
        var re = /^1\d{10}$/;
        
        if (!re.test(mob)) {
			alert("请输入正确的手机号！");
			return;
        }
        
        //发送短信要先输入验证码
        var code=$("#txt_regCode").val();
        
        $("#smsCodeBtn").attr("disabled","disabled"); 
        $("#smsCodeBtn").css("background-color","#999999");

        $.ajax({  
            url : "/reg/smscode",  
            async : true,  
            type : 'GET',  
            data : {"mobile": mob,
            			"code":code},  
            success : function(res) {  
                if(1==res.status||0==res.status){
                     t1 = setInterval(tip, 1000);  
                }
                else if(null != res.msg)
                {
                	alert(res.msg);
                    $("#smsCodeBtn").removeAttr("disabled");
                    $("#smsCodeBtn").css("background-color","#72d377");
                }
                else{
                    alert("验证码发送失败，请再次尝试！");
                    console.log("message:"+res.message);
            		console.log("status:"+res.status);
                    $("#smsCodeBtn").removeAttr("disabled");
                    $("#smsCodeBtn").css("background-color","#72d377");
                }
            },  
            error : function(XMLHttpRequest, textStatus,  
                    errorThrown) {  
		                //alert( "error！");
		                //$("#smsCodeBtn").removeAttr("disabled");
		                 t1 = setInterval(tip, 1000);  
            }  
        });
    }); 
});

function changeTo(mobile,openid,unionid,username,password){
	var mobile = mobile;
	$.ajax({  
            url : "/user/changeTo",  
            async : true,  
            type : 'POST',  
            data : {"mobile": mobile,
            		    "openid":openid,
            		    "username":username,
            		    "password":password},  
            success : function(data) {  
                if(data.code==1){
            		alert(data.msg);
                }
                else if(data.code==0){
                	alert("修改成功");
            		location.href='/user/center';
                }
             }   
        });
}

function tip() 
{  
    seed--;  
    if (seed < 1) 
    {  
        $("#smsCodeBtn").removeAttr("disabled");   
        $("#smsCodeBtn").css("background-color","#72d377");
        seed = 60;  
        $("#smsCodeBtn").html('获取验证码');  
        var t2 = clearInterval(t1);  
    } else {  
        $("#smsCodeBtn").html(seed);  
    }  
}

</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="back" href="javascript:history.back(-1);"></a>
    <p>绑定手机号</p>
  </header>
  <!-- 头部 END -->

  <!-- 注册 -->
  <article class="personal-center">
  	<section class="common-top">
      <div class="head-pic">
        <img src="<#if headImageUrl??&&headImageUrl?length gt 0>${headImageUrl}<#else>/client/images/default.jpg</#if>" alt="头像">
      </div>
      <div class="phone-num"><#if nickname??&&nickname?length gt 0>${nickname}</#if></div>
    </section>
 </article>  
  <article class="login-form">
    <form>
      <section class="enter-info">
      	<input  id="txt_rfCode"  type="hidden" value="${rfCode!''}"/>
      	<input  id="txt_openid"  type="hidden" value="${openid!''}"/>
      	<input  id="txt_unionid"  type="hidden" value="${unionid!''}"/>
      	<input  id="txt_qqOpenid"  type="hidden" value="${qqOpenid!''}"/>
      	<input  id="txt_headImageUrl"  type="hidden" value="${headImageUrl!''}"/>
      	<input  id="txt_nickname"  type="hidden" value="${nickname!''}"/>
      	<input  id="txt_address"  type="hidden" value="${address!''}"/>
      	<input  id="txt_province"  type="hidden" value="${province!''}"/>
      	<input  id="txt_sex"  type="hidden" value="<#if sex??>${sex?c}</#if>"/>
      	
        <input class="user-name"  id="txt_regMobile"  type="tel" placeholder="请输入手机号">
              <!--验证码-->
    	<input class="password" id="txt_regCode"  type="text" placeholder="验证码"/>
        <a><img src="/verify" height=46.7px alt="验证码" onclick="this.src = '/verify?date='+Math.random();" id="yzm"/></a>
        <!-- 获取验证码 -->
        <a class="get-code" style="top:17%;" id="smsCodeBtn" href="javascript:void(0)">获取短信验证码</a>
        <input class="password" id="txt_regMcode" type="tel" placeholder="请输入收到的短信验证码">
		<input class="user-name" id="txt_username"  type="text" placeholder="用户名"/>
		<input class="password" id="txt_password"  type="text" placeholder="密码（默认123456）"/>
        <input class="btn-login" id="reg_submit" type="button"  value="确认" >
      </section>
    </form>
  </article>
  <!-- 注册 END -->

</body>
</html>