<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta property="qc:admins" content="22270672646074336375" />
<title>循伍道</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>

</head>
<body class="index-bg">

  <!-- index -->
  <article class="index">
  	<div class="top-pic">
  		<img src="/client/images/index_top_pic_1.jpg" alt="">
  	</div>
  	<section class="content">
  		<div class="title">【公司简介】</div>
  		<div class="writing">
  			<#if info??>${info.content!''}<#else>重庆循伍道健康管理咨询有限公司致力于改变中国医疗健康领域的落后状态，引入西方家庭医生制度，创新性推出“健康管理服务＋医疗顾问服务＋健康保障”的爱心服务模式，打造了一个全国独一无二的“深度健康管理，受到了广大临床医学专家、企业和企业家以及金融和信赖。</#if>
  		</div>
  		<div class="img">
  			<img src="/client/images/index_writing_2.png" alt="">
  		</div>
  	</section>
  </article>
  <!-- index END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>