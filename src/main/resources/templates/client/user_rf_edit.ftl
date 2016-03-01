<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道-手机登陆</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<script src="/client/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script>

</script> 
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="back" href="javascript:history.back(-1);"></a>
    <p>修改手机号</p>
  </header>
  <!-- 头部 END -->

  <!-- 注册 -->
  <article class="login-form">
    <form>
      <section class="enter-info">
        <input class="user-name"  id="txt_regMobile"  type="tel" placeholder="请输入新的手机号">
              <!--验证码-->
    	<input class="password" id="txt_regCode"  type="text" placeholder="验证码"/>
        <a><img src="/verify" height=46.7px alt="验证码" onclick="this.src = '/verify?date='+Math.random();" id="yzm"/></a>
        <!-- 获取验证码 -->
        <a class="get-code" style="top:24%;" id="smsCodeBtn" href="javascript:void(0)">获取短信验证码</a>
        <input class="password" id="txt_regMcode" type="tel" placeholder="请输入收到的短信验证码">

        <input class="btn-login" id="reg_submit" type="button"  value="确认" >
      </section>
    </form>
  </article>
  <!-- 注册 END -->

</body>
</html>