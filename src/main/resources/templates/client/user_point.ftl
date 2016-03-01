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
<script>
//分享
function share(){
	if($(".jiathis_style_m").css("display") == "none"){
		$(".jiathis_style_m").css("display","block");
	}
	else{
		$(".jiathis_style_m").css("display","none");
	}
}
</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/user/center"></a>
    <p class="c333">会员积分</p>
  </header>
  <!-- 头部 END -->

  <!-- 会员积分 -->
  <article class="member-points">
    <!-- 可用积分 -->
    <div class="available-points">当前可用积分&nbsp;<span><#if user?? && user.point??>${user.point?c}<#else>0</#if></span></div>
    <div class="get-points">
      <p>如何获得积分：</p>
      <p>分享本APP给好友获取相应的积分<a href="javascript:share();">立即分享</a></p>
      
      <!-- JiaThis Button BEGIN -->
<div class="jiathis_style_m" style="display:none;">
<a class="jiathis_button_qzone"></a>
<a class="jiathis_button_tsina"></a>
<a class="jiathis_button_tqq"></a>
<a class="jiathis_button_weixin"></a>
</div>
<script type="text/javascript" >
var jiathis_config={
	url:"http://www.xwd33.com/info/app?rfCode=${rfCode!''}",
    summary:"健康交给循伍道，活到100不算老，注册享优惠，<#if rfCode??>我的推荐码是：${rfCode!''}。</#if>"
}
</script>
<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_m.js" charset="utf-8"></script>
<!-- JiaThis Button END -->
    </div>
  </article>
  <!-- 会员积分 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>