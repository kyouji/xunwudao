<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
<script type="text/javascript" src="/client/js/jquery.cookie.js"></script>
<script>
		function logout()
		{
	        $.cookie("autoLogin", null, { path: '/' }); 
			location.href='/logout';
		}
		
		<#if QQ??>
			$(document).ready(function(){
				if(confirm("欢迎使用QQ登陆循伍道，要现在绑定手机吗？"))
				{
					location.href="/user/change/mobile";
				}
			});
		</#if>
		<#if WX??>
			$(document).ready(function(){
				if(confirm("欢迎使用微信登陆循伍道，要现在绑定手机吗？"))
				{
					location.href="/user/change/mobile";
				}
			});
		</#if>
</script>		
</head>
<body>

  <!-- 个人中心 -->
  <article class="personal-center">
    <section class="common-top">
      <div class="head-pic">
        <img src="<#if user??&&user.headImageUrl??&&user.headImageUrl?length gt 0>${user.headImageUrl}<#else>/client/images/default.jpg</#if>" alt="头像">
      </div>
      <div class="phone-num"><#if user??><#if user.nickname??&&user.nickname?length gt 0>${user.nickname}<#else>${user.mobile!''}</#if></#if></div>
      <a class="switch-accounts" href="javascript:logout();">切换账号</a>
    </section>
    <ul class="menu">
      <li><a href="/user/info">个人信息</a></li>
      <li><a href="/user/point">会员积分</a></li>
      <li><a href="/user/order/list">我的订单</a></li>
      <li><a href="/user/rf">我的分享</a></li>
      <li><a href="/user/collect">我的收藏</a></li>
    </ul>
  </article>
  <!-- 个人中心 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>