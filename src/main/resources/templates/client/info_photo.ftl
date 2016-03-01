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
    <p class="c333">荣誉照片墙</p>
  </header>
  <!-- 头部 END -->

  <!-- 荣誉照片墙 -->
  <article class="honor-photos">
    <!-- 这里所有的图片尺寸必须一样，否则布局会乱 -->
    <#if info_list??>
        <#list info_list as item>
		    <section class="pic-lists">
		      <img src="${item.imgUrl!''}" height=100px width=100% alt="荣誉照片">
		    </section>
	    </#list>
    </#if>
  </article>
  <!-- 荣誉照片墙 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>