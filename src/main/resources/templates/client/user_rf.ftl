<!DOCTYPE html>
<html lang="zh-CN" class="bgc-f5">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="">
<meta name="copyright" content="" />
<meta name="description" content="">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<style>
  .my-share {
    margin-top: 3%;
    width: 100%;
  }
  .my-share a {
    margin-bottom: 3%;
    padding: 2%;
    width: 96%;
    height: 40px;
    background: #fff url(/client/images/icon_next_1.png) no-repeat 98% center;
    -webkit-background-size: auto 20px;
            background-size: auto 20px;
  }
  .my-share a .img {
    float: left;
    width: 40px;
    height: 40px;
    background-color: #ddd;
    border-radius: 50%;
  }
  .my-share a .img img {
    width: 100%;
    height: 100%;
    border-radius: 50%;
  }
  .my-share a .name {
    float: left;
    margin-left: 2%;
    line-height: 40px;
    font-size: 1.2em;
  }
</style>
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script>
function showTwo(id){
	//$(".two").css("display","none");
	if($("#two_"+id).css("display") =="block"){
		$("#two_"+id).slideUp();
	}
	else{
	    //$(".two").slideUp();
	    $("#two_"+id).slideDown();
	}
}
</script>
</head>
<body class="bgc-f5">

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/user/center"></a>
    <p class="c333">我的分享</p>
  </header>
  <!-- 头部 END -->

  <!-- 我的分享 -->
  <article class="my-share">
    <#if one_list?? && one_list?size gt 0>
        <#list one_list as one>
		    <a href="javascript:showTwo(${one.id?c});">
		      <div class="img">
		        <img src="<#if one.headImageUrl??&&one.headImageUrl?length gt 0>${one.headImageUrl}<#else>/client/images/default.jpg</#if>" alt="头像">
		      </div>
		      <div class="name"><#if one.nickname??&&one.nickname?length gt 0>${one.nickname}<#else>${one.realName!''}</#if>（${one.mobile!''}）</div>
		      <div style="line-height:40px;font-size:1.1em;margin-right:20px;float:right;">消费&nbsp;<span style="color:#B92222;"><#if one.spend??>${one.spend?string("0.00")}<#else>0</#if></span></div>
		    </a>
		    <div id="two_${one.id?c}" class="two" style="display:none;">
			    <#if ("two_list_" + one.id)?eval??>
				    <#assign two_list = ("two_list_" + one.id)?eval>
				    <#list two_list as two>
	                    <a style="margin-left:10%" href="javascript:void(0)">
			              <div class="img">
			                <img src="<#if two.headImageUrl??&&two.headImageUrl?length gt 0>${two.headImageUrl}<#else>/client/images/default.jpg</#if>" alt="头像">
			              </div>
			              <div class="name"><#if two.nickname??&&two.nickname?length gt 0>${two.nickname}<#else>${two.realName!''}</#if>（${two.mobile!''}）</div>
			              <div style="line-height:40px;font-size:1.1em;margin-right:32px;float:right;"><span style="color:#B92222;"><#if one.spend??>${one.spend?string("0.00")}</#if></span></div>
			            </a>
				    </#list>
			    </#if>
		    </div>
		</#list>
	<#else>
	<h1>没有成功的分享哦~</h1>	
	</#if>	    
  </article>
  <!-- 我的分享 END -->

</body>
</html>