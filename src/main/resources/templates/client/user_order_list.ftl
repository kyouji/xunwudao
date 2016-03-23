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
  #tab-ul-2 {
    width: 100%;
    height: 40px;
    background-color: #fff;
  }
  #tab-ul-2 li {
    position: relative;
    float: left;
    width: 33.33%;
    height: 40px;
  }
  #tab-ul-2 li a {
    width: 100%;
    height: 40px;
    line-height: 40px;
    font-size: 1.1em;
    text-align: center;
    -webkit-box-sizing: border-box;
            box-sizing: border-box;
  }
  #tab-ul-2 li a span {
    position: absolute;
    left: 72%;
    top: 10px;
    display: block;
    width: 5px;
    height: 5px;
    background-color: #16ac1d;
    border-radius: 50%;
  }
  #tab-ul-2 li.active a {
    color: #16ac1d;
    border-bottom: 2px solid #16ac1d;
  }

  #tab-ol-2 {padding: 3% 0 2% 0;}
  #tab-ol-2 li {display: none;} 
  #tab-ol-2 li:nth-child(1) {display: block;}
  #tab-ol-2 li article {
    margin-bottom: 2%;
    padding: 2%;
    width: 96%;
    background-color: #fff;
  }
  #tab-ol-2 li article .sec1 {
    width: 100%;
    height: 30px;
    line-height: 30px;
    color: #999;
    border-bottom: 1px solid #ddd;
  }
  #tab-ol-2 li article .sec1 .div1 {float: left;}
  #tab-ol-2 li article .sec1 .div2 {float: right;}
  #tab-ol-2 li article .sec1 .pending {color: #16ac1d;}
  #tab-ol-2 li article .sec2 {
    margin-top: 2%;
    width: 100%;
  }
  #tab-ol-2 li article .sec2 a {
    overflow: hidden;
    margin-bottom: 5px;
    padding: 2%;
    width: 96%;
    background-color: #f5f5f5;
  }
  #tab-ol-2 li article .sec2 a .img {
    float: left;
    width: 60px;
    height: 60px;
    background-color: #fff;
  }
  #tab-ol-2 li article .sec2 a .img img {
    display: block;
    width: 100%;
    height: 100%;
  }
  #tab-ol-2 li article .sec2 a .right {
    float: left;
    margin-left: 2%;
    width: 77%;
    height: 60px;
  }
  #tab-ol-2 li article .sec2 a .right p {
    width: 100%;
    height: 24px;
    line-height: 24px;
  }
  #tab-ol-2 li article .sec2 a .right p.p1 {
    font-size: 1.2em;
  }
  #tab-ol-2 li article .sec2 a .right p.p1 .sp1 {
    overflow:hidden;
    float: left;
    width: 70%;
    -webkit-text-overflow:ellipsis;
            text-overflow:ellipsis;
    white-space:nowrap;
  }
  #tab-ol-2 li article .sec2 a .right p.p1 .sp2 {
    float: right;
    width: 30%;
    text-align: right;
  }
  #tab-ol-2 li article .sec2 a .right p.p2 {color: #999;}
  #tab-ol-2 li article .sec3 {
    width: 100%;
    height: 30px;
    line-height: 30px;
    text-align: right;
    border-bottom: 1px solid #ddd;
  }
  #tab-ol-2 li article .sec4 {
    overflow: hidden;
    margin-top: 5px;
    width: 100%;
  }
  #tab-ol-2 li article .sec4 a {
    float: right;
    width: 80px;
    height: 30px;
    line-height: 30px;
    font-size: 1.2em;
    color: #16ac1d;
    text-align: center;
    border: 1px solid #16ac1d;
    border-radius: 3px;
  }
</style>
<!-- js -->

<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript">
  $(function(){
    $('#tab-ul-2').on('click','a',function(){
      var $self = $(this);//当前a标签
      var $active = $self.closest('li');//当前点击li
      var index = $active.prevAll('li').length;//当前索引

    $active.addClass('active').siblings('li').removeClass('active');
      $('#tab-ol-2').find('>li')[index==-1?'show':'hide']().eq(index).show();
    });
  });
</script>
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

//支付
function pay(orderMoney,orderNumber){
	var ua = navigator.userAgent.toLowerCase();
	
	if(ua.match(/MicroMessenger/i)=="micromessenger") 
	{
		if(orderMoney > 0){
			location.href='/weixin/pay?orderNumber='+orderNumber;
		}else{
			location.href='/user/pay/free?money=0.00&orderNumber='+orderNumber;
		}
	}
	else
	{
		if(orderMoney > 0){
			location.href='/user/pay/alipay/recharge?money='+orderMoney+'&orderNumber='+orderNumber;
		}else{
			location.href='/user/pay/free?money=0.00&orderNumber='+orderNumber;
		}		
	}
}

function finishServ(orderNumber,state){
	var orderNumber = orderNumber;
	var state = state;
	$.ajax({
	      type:"post",
	      url:"/order/confirm",
	      data:{"orderNumber":orderNumber,
	      		   "state":state},
	      success:function(data){
			if (data.code == 1)
			{
	            alert(data.msg);
	            if(data.login==1)
	            {
	            	location.href='/login';
	            }
			}
			else{
				alert(data.msg)
				location.href='/user/order/list';
			}
	      }
	  });
}
</script>
</head>
<body class="bgc-f5">

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/user/center"></a>
    <p class="c333">我的订单</p>
  </header>
  <!-- 头部 END -->

  <!-- 我的订单 -->
  <article class="my-orders">
    <ul id="tab-ul-2">
      <li><a href="javascript:void(0)">待付款</a></li>
      <li><a href="javascript:void(0)">待服务</a></li>
      <li><a href="javascript:void(0)">已完成</a></li>
    </ul>
    <ol id="tab-ol-2">
      <!-- 待付款 -->
      <li>
      	<#if order_list??>
      		<#list order_list as order>
      			<#if order.statusId?? && order.statusId == 2>
			        <article>
			          <section class="sec1">
			            <div class="div1">订单号<span>${order.orderNumber!''}</span></div>
			            <div class="div2 pending">待付款</div>
			          </section>
			          <#assign totalPrice = 0.00>
			          <#assign totalQuantity = 0>
				          <section class="sec2">
          			          <#if order.orderGoodsList??>
			          			<#list order.orderGoodsList as goods>
						            <a>
						              <div class="img">
						                <img src="${goods.goodsCoverImageUri!''}">sasas
						              </div>
						              <div class="right">
						                <p class="p1">
						                  <span class="sp1">${goods.goodsTitle!''}</span>
						                  <span class="sp2"><#if goods.price??>￥${goods.price?string("0.00")}<#else>免费</#if></span>
						                </p>
						                <p class="p2">x<#if goods.quantity??>${goods.quantity?c}</#if></p>
						                <#if goods.price?? && goods.quantity??>
						                	<#assign totalPrice = totalPrice + goods.price*goods.quantity>
						                	<#assign totalQuantity = totalQuantity+goods.quantity>
						                </#if>
						              </div>
						            </a>
				          		</#list>
				         	</#if> 
				          </section>
			          <section class="sec3">包含所有套餐共<span>${totalQuantity?c}</span>份，合计：￥<span>${totalPrice?string("0.00")}</span></section>
			          <section class="sec4">
			          	<#--
			            <a href="<#if totalPrice?? && totalPrice gt 0>/user/pay/alipay/recharge?money=${totalPrice?string("0.00")}&orderNumber=${order.orderNumber!''}<#else>/user/pay/free?money=0.00&orderNumber=${order.orderNumber!''}</#if>">确认付款</a>
			            -->
			            <div class="div1">下单时间&nbsp;&nbsp;<span>${order.orderTime?string("yyyy-MM-dd HH:mm:ss")}</span></div>
			            <a href="javascript:pay('${totalPrice?string("0.00")}','${order.orderNumber!''}');">去付款</a>
			            <a href="/order/cancel?orderNumber=${order.orderNumber!''}&state=${order.statusId!''}">取消订单</a>
			          </section>
			        </article>
			        <HR style="FILTER: alpha(opacity=100,finishopacity=0,style=3)" width="100%" color=#B6B1C3 SIZE=3>
		        </#if>
		    </#list>
		</#if>        
      </li>
      <!-- 已付款 -->
      <li>
        <#if order_list??>
            <#list order_list as order>
                <#if order.statusId?? && order.statusId == 4>
                    <article>
                      <section class="sec1">
                        <div class="div1">订单号<span>${order.orderNumber!''}</span></div>
                        <div class="div2 pending">待服务</div>
                      </section>
                      <#assign totalPrice = 0.00>
                      <#assign totalQuantity = 0>
                          <section class="sec2">
                              <#if order.orderGoodsList??>
                                <#list order.orderGoodsList as goods>
                                    <a>
                                      <div class="img">
                                        <img src="${goods.goodsCoverImageUri!''}">sasas
                                      </div>
                                      <div class="right">
                                        <p class="p1">
                                          <span class="sp1">${goods.goodsTitle!''}</span>
                                          <span class="sp2"><#if goods.price??>￥${goods.price?string("0.00")}<#else>免费</#if></span>
                                        </p>
                                        <p class="p2">x<#if goods.quantity??>${goods.quantity?c}</#if></p>
                                        <#if goods.price?? && goods.quantity??>
                                            <#assign totalPrice = totalPrice + goods.price*goods.quantity>
                                            <#assign totalQuantity = totalQuantity+goods.quantity>
                                        </#if>
                                      </div>
                                    </a>
                                </#list>
                            </#if> 
                          </section>
                      <section class="sec3">包含所有套餐共<span>${totalQuantity?c}</span>份，合计：￥<span>${totalPrice?string("0.00")}</span></section>
                      <section class="sec4">
                        <a href="javascript:finishServ('${order.orderNumber!''}','${order.statusId!''}')";>确认服务</a>
                      </section>
                    </article>
                </#if>
            </#list>
        </#if>        
      </li>
      <!-- 已完成 -->
      <li>
        <#if order_list??>
            <#list order_list as order>
                <#if order.statusId?? && order.statusId == 6>
                    <article>
                      <section class="sec1">
                        <div class="div1">订单号<span>${order.orderNumber!''}</span></div>
                        <div class="div2 pending">已完成</div>
                      </section>
                      <#assign totalPrice = 0.00>
                      <#assign totalQuantity = 0>
                          <section class="sec2">
                              <#if order.orderGoodsList??>
                                <#list order.orderGoodsList as goods>
                                    <a>
                                      <div class="img">
                                        <img src="${goods.goodsCoverImageUri!''}">sasas
                                      </div>
                                      <div class="right">
                                        <p class="p1">
                                          <span class="sp1">${goods.goodsTitle!''}</span>
                                          <span class="sp2"><#if goods.price??>￥${goods.price?string("0.00")}<#else>免费</#if></span>
                                        </p>
                                        <p class="p2">x<#if goods.quantity??>${goods.quantity?c}</#if></p>
                                        <#if goods.price?? && goods.quantity??>
                                            <#assign totalPrice = totalPrice + goods.price*goods.quantity>
                                            <#assign totalQuantity = totalQuantity+goods.quantity>
                                        </#if>
                                      </div>
                                    </a>
                                </#list>
                            </#if> 
                          </section>
                      <section class="sec3">包含所有套餐共<span>${totalQuantity?c}</span>份，合计：￥<span>${totalPrice?string("0.00")}</span></section>
                      <section class="sec4">
                      </section>
                    </article>
                </#if>
            </#list>
        </#if>
        </li>
    </ol>
  </article>
  <!-- 我的订单 END -->

</body>
</html>