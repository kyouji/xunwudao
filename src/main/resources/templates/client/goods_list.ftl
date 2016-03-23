<!DOCTYPE html>
<html lang="zh-CN" class="bgc-f5">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if category??>${category.seoTitle!''}</#if>">
<meta name="copyright" content="<#if category??>${category.seoKeywords!''}</#if>" />
<meta name="description" content="<#if category??>${category.seoDescription!''}</#if>">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>套餐列表</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
<script>
function showMore()
{
	
	$(".show-more").slideDown();
	$(".look-more").attr("href","javascript:hideShow();");
	$(".look-more").html("收起");
}

function hideShow()
{
	$(".show-more").slideUp();
	$(".look-more").attr("href","javascript:showMore();");
	$(".look-more").html("查看更多");
}
</script>
</head>
<body class="bgc-f5">

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/goods/index"></a>
    <p class="c333"><#if category??>${category.title!''}</#if></p>
  </header>
  <!-- 头部 END -->

  <!-- 套餐列表 -->
  <article class="package-lists">
    <div class="tips">
      <#if category??>${category.content!''}</#if>
    </div>
    <section class="package">
	    <#if goods_list??>
	    	<#list goods_list as item>
	    		<#if item_index lt 6>
			      <a class="list" href="/goods/detail?id=${item.id?c}">
			        <!-- 所有套餐图片尺寸必须一致，否则页面布局会乱 -->
			        <img src="<#if item.coverImageUri?? && item.coverImageUri?length gt 0>${item.coverImageUri}<#else>/client/images/pic_product_1.jpg</#if>" width=100% height=120px  alt="套餐图片">
			        <div class="div1">
			          <p class="p1" style="overflow:hidden;height:16px;">${item.title!''}</p>
			          <p class="p2" style="overflow:hidden;height:16px;">${item.subTitle!''}</p>
			        </div>
			        <div class="div2"><#if item.salePrice??>￥<span>${item.salePrice?string("0.00")}</span><#else>免费</#if></div>
			      </a>
		      	</#if>
		      	<#if item_index gt 5>
			      	  <a class="list show-more" style="display:none;" href="/goods/detail?id=${item.id?c}">
				        <!-- 所有套餐图片尺寸必须一致，否则页面布局会乱 -->
				        <img src="<#if item.imgUrl?? && item.imgUrl?length gt 0>${item.imgUrl}<#else>/client/images/pic_product_1.jpg</#if>" alt="套餐图片">
				        <div class="div1">
				          <p class="p1">${item.title!''}</p>
				          <p class="p2">${item.subTitle!''}</p>
				        </div>
				        <div class="div2"><#if item.salePrice??>￥<span>${item.salePrice?string("0.00")}</span><#else>免费</#if></div>
				      </a>
			      </#if>
      		</#list>
      </#if>
      
    </section>
    <#if goods_list?? && goods_list?size gt 6><a class="look-more" href="javascript:showMore();">查看更多</a></#if>
  </article>
  <!-- 套餐列表 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>