<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道-登陆</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<script src="/client/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/client/js/jquery.cookie.js"></script>
<script>
$(document).ready(function(){
			//记住密码
			if ($.cookie("savePassword") == "true") { 
		        $("#savePassword").attr("checked", true); 
		        $("#username").val($.cookie("username")); 
		        $("#password").val($.cookie("password")); 
		    } 

		    //自动登陆
		    console.log( $.cookie("autoLogin"));
		    if($.cookie("autoLogin") == "true"){
		    	$("#autoLogin").attr("checked",true);
		    	$("#btn_login").attr("value","登录中");
		    	$("#btn_login").removeClass("btn-login");
		    	$("#btn_login").addClass("auto_login_bg");
		    	setTimeout("loginSubmit()",1200);
		    }	
		});
		
function loginSubmit()
{
	if($.cookie("autoLogin") == "false"){
		$.cookie("autoLogin", "true", { expires: 45 }); 
	}
	
	var username = document.getElementById("username").value; //用户名
	var password = document.getElementById("password").value; //密码
	
    $.cookie("savePassword", "true", { expires: 45 }); // 存储一个带45天期限的 cookie 
    $.cookie("username", username, { expires: 45 }); // 存储一个带45期限的 cookie 
    $.cookie("password", password, { expires: 45}); // 存储一个带45天期限的 cookie 
	
	  $.ajax({
	      type:"post",
	      url:"/login/submit",
	      data:{"username":username,
	    	  		"password":password},
	      success:function(data){
			if (data.code == 1)
			{
				alert(data.msg);
			}
			else{
				<#if goodsId??>
			     location.href='/goods/detail/${goodsId?c}';
				<#else>
			     location.href="/user/center";
			    </#if>
			}
	      }
	  });
}

</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="back" href="javascript:history.back(-1);"></a>
    <p>登录</p>
    <a class="btn-registered" href="/reg">注册</a>
  </header>
  <!-- 头部 END -->

  <!-- 登录 -->
  <article class="login-form">
    <img class="pic-1" src="/client/images/pic_login_1.png" alt="循伍道">
      <section class="enter-info">
        <input class="user-name" id="username" type="text" placeholder="请输入用户名">
        <input class="password" id="password" type="password" placeholder="请输入6-16位登陆密码">
        <input class="btn-login" type="submit" id="btn_login" onclick="javascript:loginSubmit();" value="登 录">
        <a style="float:right;margin:5px 5px 0 0;" href="/login/password_retrieve">忘记密码</a>
      </section>
    <!-- 其他方式登录 -->
    <!-- 
    <section class="other-ways">
      <div class="title">其他方式登录</div>
      <ul>
        <li>
          <a href="#">
            <img src="/client/images/icon_login_weixin.png" alt="微信登录">
          </a>
        </li>
        <li>
          <a href="#">
            <img src="/client/images/icon_login_phone.png" alt="手机登录">
          </a>
        </li>
        <li>
          <a href="#">
            <img src="/client/images/icon_login_qq.png" alt="QQ登录">
          </a>
        </li>
      </ul>
    </section>
    -->
  </article>
  <!-- 登录 END -->

</body>
</html>