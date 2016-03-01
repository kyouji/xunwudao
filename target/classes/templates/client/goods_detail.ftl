<!DOCTYPE html>
<html lang="zh-CN" class="bgc-f5">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if tdGoods??>${tdGoods.seoKeywords!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="description" content="<#if tdGoods??>${tdGoods.seoDescription!''}</#if>">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<style>
  .packagede-foot {
    position: fixed;
    left: 0;
    bottom: 0;
    z-index: 2;
    width: 100%;
    height: 50px;
    background-color: #fafafa;
  }
  .packagede-foot a {
    float: right;
    width: 30%;
    height: 50px;
    line-height: 50px;
    font-size: 1.2em;
    color: #fff;
    text-align: center;
  }
  .packagede-foot a.a1 {background-color: #ff9402;}
  .packagede-foot a.a2 {background-color: #ff5000;}
  table,tr,td{border: 1px #333333 solid;}
</style>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
<script>
$(document).ready(function(){
	$(".img1").click(function(){
		$(".img1").removeClass("active");
		$(this).addClass("active");
	});
	
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		$("#weixin").style("display","block");
		$("#zhifubao").style("display","none");
	}else{
		$("#zhifubao").style("display","block");
		$("#weixin").style("display","none");
	}
});
function cart()
{
	var id = <#if tdGoods??>${tdGoods.id?c}<#else>null</#if>;
	var catId = <#if tdGoods??>${tdGoods.categoryId?c}<#else>null</#if>;
	$.ajax({
	      type:"post",
	      url:"/cart/init",
	      data:{"id":id,
	      			"catId":catId},
	      success:function(data){
			if (data.code == 1)
			{
	            alert(data.msg);
			}
			else{
				alert(data.msg);
				location.href='/goods/list?catId='+data.catId;
			}
	      }
	  });
}
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
<script>
//添加收藏
function collectAdd(id){
    var id=id;
    $.ajax({  
        url : "/user/collect/add",  
        async : true,  
        type : 'POST',  
        data : {"goodsId": id},  
        success : function(data) {  
            if(data.code==1){
                alert(data.msg);
                if(data.login==1){
                    location.href='/login';
                }
            }
            else if(data.code==0){
                alert(data.msg);
                $("#collect_add").css("display","none");
                $("#collect_remove").css("display","block");
            }
         }   
    });
}
//取消收藏
function collectRemove(id){
    var id=id;
    $.ajax({  
        url : "/user/collect/remove",  
        async : true,  
        type : 'POST',  
        data : {"goodsId": id},  
        success : function(data) {  
            if(data.code==1){
                alert(data.msg);
                if(data.login==1){
                    location.href='/login';
                }
            }
            else if(data.code==0){
                alert(data.msg);
                $("#collect_remove").css("display","none");
                $("#collect_add").css("display","block");
            }
         }   
    });
}
</script>
</head>
<body class="bgc-f5">

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/goods/list?catId=${tdGoods.categoryId!''}"></a>
    <p class="c333">套餐详情</p>
  </header>
  <!-- 头部 END -->

  <!-- 套餐详情 -->
  <article class="package-details">
    <img class="top-pic-1" src="<#if tdGoods.coverImageUri?? && tdGoods.coverImageUri?length gt 0>${tdGoods.coverImageUri}<#else>/client/images/pic_product_1.jpg</#if>" alt="套餐图片">
    <section class="sec1">
      <div class="left">
        <p class="p1">${tdGoods.title!''}</p>
        <p class="p1" style="font-size:1em;">${tdGoods.subTitle!''}</p>
        <s class="p2">门市价：<#if tdGoods.marketPrice??>￥<span>${tdGoods.marketPrice?string("0.00")}</span><#else><span>免费</span></#if></s>
        <p class="p3">活动价：<#if tdGoods.salePrice??>￥<span>${tdGoods.salePrice?string("0.00")}</span><#else><span>免费</span></#if></p>
      </div>
      
      <a class="right" href="javascript:share();">分享</a>

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
    summary:"健康交给循伍道，活到100不算老，注册享优惠，<#if rfCode??>我的推荐码是：${rfCode!''}。</#if>【${tdGoods.title!''}】"
}
</script>
<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_m.js" charset="utf-8"></script>
<!-- JiaThis Button END -->















    </section>

    <!-- 可用积分 -->
    <section class="available-points">
      <div class="left">可用积分：<span><#if user??&&user.totalPoints??>${user.totalPoints?c!''}<#else>0</#if></span> </div>
      <#--<div class="right">未使用</div>-->
            <a id="collect_add" href="javascript:collectAdd(${tdGoods.id?c});" style="float: right;
                                                      display:<#if !collected??||collected?? && collected ==0>block<#else>none</#if>;
													  width: 15%;
													  text-align: center;
													  background: url(.) no-repeat center top;
													  -webkit-background-size: auto 20px;
													  background-size: auto 20px;
													  border-left: 1px solid #ddd;
													  -webkit-box-sizing: border-box;
													  box-sizing: border-box;">收藏</a>
              <a id="collect_remove" href="javascript:collectRemove(${tdGoods.id?c});" style="float: right;
                                                      display:<#if collected?? && collected ==1>block<#else>none</#if>;
                                                      width: 15%;
                                                      text-align: center;
                                                      background: url(.) no-repeat center top;
                                                      -webkit-background-size: auto 20px;
                                                      background-size: auto 20px;
                                                      border-left: 1px solid #ddd;
                                                      -webkit-box-sizing: border-box;
                                                      box-sizing: border-box;">已收藏</a>
    </section>

    <!-- 支付方式 -->
    <section class="pay-ways">
      <div class="div1">
        运费：<span>免运费</span>
      </div>
      <div class="div2">
        <label>支付：</label>
        <p id="zhifubao" class="img1 active">
          <img src="/client/images//iconfont-zhifubao.png" alt="支付宝支付">
        </p>
        <p id="weixin" class="img1 img2">
          <img src="/client/images//iconfont-weixinzhifu.png" alt="微信支付">
        </p>
      </div>
      <div class="div1">
        销量：<span><#if tdGoods.soldNumber??>${tdGoods.soldNumber?c}<#else>0</#if></span>
      </div>
    </section>

    <!-- 套餐详情 -->
    <section class="sec3">
      <ul id="tab-ul">
        <li class="active"><a href="###">套餐详情</a></li>
        <li><a href="###">成交记录</a></li>
      </ul>
      <ol id="tab-ol">
        <!-- 套餐详情 -->
        <li style="display: block;">
        	${tdGoods.detail!''}
        </li>
        <!-- 成交记录 -->
        <li>
        <#if order_goods_list??>
        <#list order_goods_list as item>
        	<#if item.time??>
        		${item.time?string("yyyy年MM月dd日")}&nbsp;&nbsp;
	           ×${item.quantity!''}
	           <#if item.username??>
	           	<span style="float:right;margin-right:20%;">${item.username[0..3]?default("")}*****<#if item.username?length gt 10>${item.username[9..10]?default("")}</#if></span><br>
	        	</#if>
        	</#if>
        </#list>
        </#if>
        </li>
      </ol>
    </section>

  </article>
  <!-- 套餐详情 END -->

  <div class="clear h50"></div>
  
  <footer class="packagede-foot">
    <a class="a2" href="/order/buy?gid=${tdGoods.id?c}">立即购买</a>
    <a class="a1" href="javascript:cart();">加入购物车</a>
  </footer>
  <!-- 底部 -->

  <!-- 底部 END -->

</body>
</html>