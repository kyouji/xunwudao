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
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="javascript:history.back(-1);"></a>
    <p class="c333">关于我们</p>
  </header>
  <!-- 头部 END -->

  <!-- 个人中心 -->
  <article class="personal-center">
    <ul class="menu">
      <li><a href="/info/profile">公司简介</a></li>
      <li><a href="/info/photo">荣誉照片墙</a></li>
      <li><a href="/info/doctor">健康管理师</a></li>
      <#if site??&&site.telephone??&&site.telephone?length gt 0><li><a href="tel:${site.telephone!''}">联系我们：${site.telephone!''}</a></li></#if>
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