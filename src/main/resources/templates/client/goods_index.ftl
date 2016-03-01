<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if category??>${category.seoTitle!''}</#if>">
<meta name="copyright" content="<#if category??>${category.seoKeywords!''}</#if>" />
<meta name="description" content="<#if category??>${category.seoDescription!''}</#if>">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>商城</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
</head>
<body>

  <!-- 商城 -->
  <article class="mall">
  	<#if category_list??>
  		<#list category_list as item>
		    <section class="channel-lists">
			      <img src="<#if item.imgUrl?? && item.imgUrl?length gt 0>${item.imgUrl}<#else>/client/images/pic_product_1.jpg</#if>" alt="健康频道">
			      <a href="/goods/list?catId=${item.id?c}">点击进入${item.title!''}</a>
		    </section>
	    </#list>
    </#if>
  </article>
  <!-- 商城 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>