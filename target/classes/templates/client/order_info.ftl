<!DOCTYPE html>
<html lang="zh-CN" class="bgc-f5">
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
$(document).ready(function(){
//判断支付方式
	var ua = navigator.userAgent.toLowerCase();
	if(ua.match(/MicroMessenger/i)=="micromessenger") {
		$("#weixin").css("display","block");
		$("#zhifubao").css("display","none");
		$("#payTypeTitle").val("微信");
	}else{
		$("#zhifubao").css("display","block");
		$("#weixin").css("display","none");
		$("#payTypeTitle").val("支付宝");
	}

//初始化总价格
	var totalPrice = 0.00;
	var priceObj = $("input[name='price']");
	for(var i=0; i<priceObj.length;i++)
		{
	    		totalPrice=parseFloat(totalPrice)+parseFloat(priceObj[i].value);
		}
	$(".total-price").html(totalPrice.toFixed(2));
	$("#original_price").html("￥"+totalPrice.toFixed(2));
	
	
	//选择性别样式控制
	$(".sex-choose").click(function(){
		$(".sex-choose").removeClass("checked");
		$(".sex-choose").removeAttr("checked");
		
		$(this).addClass("checked");
		$(this).attr("checked","checked");
	});
	
	
	//提交表单控制 关键项输入了才能提交
	//$(".nece").change(function(){
	$('.nece').on("change input", function () {
		//var inputObj = $("input[class='nece']");
		var inputObj = document.getElementsByClassName("nece");
		var a = 0  									//必选项是否输入的标识
		for(var i=0;i < inputObj.length;i++){
			if($.trim(inputObj[i].value) == "")
			a++;
		}
		
		if(a==0){
			$(".package-foot").css("background-color","#ff9900");
			$(".package-foot").attr("onclick","javascript:orderSubmit();");
		}
		else{
			$(".package-foot").css("background-color","#666666");
			$(".package-foot").attr("onclick","javascript:void(0);");
		}
		
	});
});

//编辑数量
function red(index)
{
	var quantity = $("#goods_quantity_"+index).val();
	var newQuantity = parseInt(parseInt(quantity)-1);
	if(newQuantity < 1)
	{
		newQuantity = 1;
	} 
	$("#goods_quantity_"+index).attr("value",newQuantity);
	setTotalPrice();
}
function add(index)
{
	var quantity = $("#goods_quantity_"+index).val();
	var newQuantity = parseInt(parseInt(quantity)+1);
	$("#goods_quantity_"+index).attr("value",newQuantity);
	
	setTotalPrice();
}

function deleteGoods(index)
{
	$("#goods_"+index).remove();
	setTotalPrice();
	
	var priceObj = $("input[name='price']");
	if(priceObj.length == 0)
	{
		alert("订单中没有商品了");
		location.href='/goods/index';
	}
}
function setTotalPrice()
{

	var totalPrice = 0.00;
	var priceObj = $("input[name='price']");
	var pointD = parseFloat($("#pointD").html());
	for(var i=0; i<priceObj.length;i++)
		{
			var goodsprice = $("#price_"+i).val();
			var quantity = $("#goods_quantity_"+i).val();
			console.log("goodsprice:"+goodsprice);
			if(typeof(goodsprice) != "undefined"){
				var price = parseFloat(goodsprice)*parseInt(quantity);
    			totalPrice=parseFloat(totalPrice)+parseFloat(price);
	    	}
		}
	
	$(".total-price").html((totalPrice-pointD).toFixed(2));
	$("#original_price").html("￥"+totalPrice.toFixed(2));  //初始价格 
	
}

function usePoint()
{
	var totalPoints = parseFloat($("#totalPoints").val());  //积分
	var totalPrice = parseFloat($(".total-price").html());  //订单总价

	//扣积分
	if($("#is_point_used").val()==0)
	{
		if(totalPrice > totalPoints/100){
			//抵扣部分价格
			var newPrice = totalPrice-(totalPoints/100);
			
			$("#pointD").html((totalPoints/100).toFixed(2));
			$("#point_useable").html("<span id='used_point'>"+totalPoints+"</span>分已抵扣<span>￥"+(totalPoints/100).toFixed(2)+"<span>");
			//更新积分，防止重复刷
			totalPoints = 0;
			$("#totalPoints").val(totalPoints);
		}
		else{
			//抵扣全部价格
			var newPrice = 0.00;
			totalPoints = totalPoints - totalPrice*100;
			$("#totalPoints").val(totalPoints);
			$("#pointD").html(totalPrice.toFixed(2));
			$("#point_useable").html("<span id='used_point'>"+totalPrice*100+"</span>分已抵扣<span>￥"+totalPrice+"<span>");
		}
		
		$("#is_point_used").val(1);
		$(".total-price").html(parseFloat(newPrice).toFixed(2));
	}
	//返回未扣状态
	else{
		totalPoints = <#if user?? &&user.totalPoints??>${user.totalPoints?c}<#else>0</#if>;
	
		//余额抵扣
		var pointD = totalPoints-parseInt($("#used_point").val());
		$("#pointD").html("0.00");

	
		//返回积分
		$("#point_useable").html("<span>"+totalPoints+"</span>分可用");
		$("#totalPoints").val(totalPoints);

		$("#is_point_used").val(0);
		setTotalPrice();
	}	
}


	//提交表单
	function orderSubmit(){
		/*基本信息*/
		var realName = $("#realName").val();  //姓名
		var sex = $("input[name='sex']:checked").val(); //性别
		var areaId = $("#areaId").val(); //区域id
		var address = $("#address").val();
		var mobile = $("#mobile").val();
		var idCard = $("#idCard").val();
		var serviceTime = $("#serviceTime").val();
		var userRemarkInfo = $("#userRemarkInfo").val();
		var payTypeTitle = $("#payTypeTitle").val();
		
		
		/*商品信息*/
		var idObj = $("input[name='cartGoodsId']"); //购物车商品id
		var quantityObj = $("input[name='quantity']"); //商品数量
		var cartGoodsIds = new Array();
		var quantities = new Array();
		var pointD = parseFloat($("#pointD").html()).toFixed(2);
		
		for(var i=0; i<idObj.length;i++)
		{
	    	cartGoodsIds[i]=idObj[i].value;
		}
		for(var i=0; i<quantityObj.length;i++)
		{
	    	quantities[i]=quantityObj[i].value;
		}
	
	    $.ajax({
		      type:"post",
		      traditional:true,
		      url:"/order/submit",
		      data:{"realName":realName,
		      			"sex":sex,
		      			"areaId":areaId,
		      			"address":address,
		      			"mobile":mobile,
		      			"idCard":idCard,
		      			"serviceTime":serviceTime,
		      			"userRemarkInfo":userRemarkInfo,
		      			"cartGoodsIds":cartGoodsIds,
		      			"quantities":quantities,
		      			"payTypeTitle":payTypeTitle,
		      			"pointD":pointD},
		      success:function(data){
				if (data.code == 1)
				{
					alert(data.msg);
				}
				else{
					if(typeof(data.orderNumber) != "undefined" && typeof(data.money) != "undefined"){
						if(data.payType == 0){
							location.href='/user/pay/alipay/recharge?money='+data.money+"&orderNumber="+data.orderNumber;
						}else if(data.payType == 1){
							location.href='/weixin/pay/getOpen?orderNumber='+data.orderNumber;
						}
						
					}
					
					//location.href="/user/order/list";
				}
		      }
		  });
	}
</script>
</head>
<body class="bgc-f5">

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="javascript:history.back(-1);"></a>
    <p class="c333">确认订单</p>
  </header>
  <!-- 头部 END -->

  <!-- 确认订单 -->
  <article class="confirm-order">
    <!-- 套餐详情 -->
    <section class="sec1">
    <#if buy_goods_list??>
    	<#list buy_goods_list as item>
	      <article class="package" id="goods_${item_index}">
	        <div class="top">
	          <div class="left">
	            <div class="img">  
	              <img src="<#if item.goodsCoverImageUri?? && item.goodsCoverImageUri?length gt 0>${item.goodsCoverImageUri}<#else>/client/images/pic_product_1.jpg</#if>" alt="${item.title!''}">
	            </div>
	          </div>
	          <div class="right">
	            <div class="div1">${item.goodsTitle!''}</div>
	            <div class="div2">￥<span><#if item.price??>${item.price?string("0.00")}<#else>0.00</#if></span></div>
	            <input type="hidden" name="price" value="<#if item.price??>${item.price?string("0.00")}<#else>0.00</#if>" id="price_${item_index}">
	            <input type="hidden" name="cartGoodsId" value="${item.id?c}" >
	            <a class="btn-close" href="javascript:deleteGoods(${item_index});"></a>
	          </div>
	        </div>
	        <!-- 数量选择 -->
	        <div class="choose-num">
	          <div class="num">数量</div>
	          <div class="numbers">
	            <a class="less" onclick="javascript:red(${item_index});">-</a>
	            <input type="text" name="quantity" id="goods_quantity_${item_index}" value="<#if item.quantity??>${item.quantity?c}<#else>0</#if>">
	            <a class="add" onclick="javascript:add(${item_index});">+</a>
	          </div>
	        </div>
	      </article>
      </#list>
  </#if>
      <!-- 收货人姓名 -->
      <article class="receiver-name">
        <label><span>*</span>姓名</label>
        <div class="right">
          <input class="inp1 nece" type="text" placeholder="体检人姓名" name="realName" id="realName" value="<#if user??>${user.realName!''}</#if>">
          <div class="gender">
            <strong>
              <!-- 选中给Input加类名"checked" -->
              <input type="checkbox" name="sex" class="sex-choose checked" value="true" <#if !user?? || user??&&!user.sex?? || user??&&user.sex??&&user.sex>checked="checked" </#if>> 先生
            </strong>
            <strong>
              <!-- 选中给Input加类名"checked" -->
              <input type="checkbox" name="sex" class="sex-choose" value="false" <#if user??&&user.sex??&&!user.sex>checked="checked" </#if>> 女士
            </strong>
          </div>
        </div>
      </article>
      <!-- 收货人地址 -->
      <article class="receiver-name">
        <label><span>*</span>体检地点</label>
        <div class="right">
          <select name="areaId" id="areaId" class="nece">
            <option value=''>请选择区域</option>
            <#if area_list??>
            	<#list area_list as item>
            		<option value="${item.id?c}" <#if user?? && user.areaId?? && user.areaId == item.id>selected="selected"</#if> >${item.title!''}</option>
            	</#list>
            </#if>		
          </select>
          <input class="inp2 nece" type="text" placeholder="请输入地址的详细信息" id="address" value="<#if user??>${user.address!''}</#if>">
        </div>
      </article>
      <!-- 手机号码 -->
      <article class="receiver-phone">
        <label><span>*</span>手机号码</label>
        <input class="right-inp nece" type="tel" placeholder="联系您的电话" id="mobile" value="<#if user??>${user.mobile!''}</#if>">
      </article>
      <!-- 身份证 -->
      <article class="receiver-phone">
        <label><span>*</span>身份证</label>
        <input class="right-inp nece" type="tel" placeholder="体检人身份证号码" id="idCard">
      </article>
      <!-- 体检时间 -->
      <article class="receiver-phone">
        <label><span>*</span>体检时间</label>
        <input class="right-inp nece" type="date" id="serviceTime">
      </article>
      
      <!-- 经办人 -->
      <#--
      <article class="receiver-phone">
        <label>经办人</label>
        <input class="right-inp" type="text" id="operator">
      </article>
      -->
      <!-- 留言 -->
      <article class="receiver-phone">
        <label>留言</label>
        <input class="right-inp" type="text" id="userRemarkInfo">
      </article>
      <!-- 支付方式 -->
      <article class="receiver-phone">
      	 <input type="hidden" id="payTypeTitle" value="">
         <label>支付方式：</label>
        <p id="zhifubao" class="img1 active">
          <img src="/client/images//iconfont-zhifubao.png" width=48 height=22 alt="支付宝支付">
        </p>
        <p id="weixin" class="img1 img2">
          <img src="/client/images//iconfont-weixinzhifu.png" alt="微信支付">
        </p>
      </article>
    </section>
    

    <!-- 使用积分 -->
    <a class="use-points" href="javascript:usePoint();">
    	<#--用户初始积分-->
    	<input type="hidden" value="<#if user??&&user.totalPoints??>${user.totalPoints?c}<#else>0</#if>" id="totalPoints">
    	<#--用户是否使用积分 【0】不使用；【1】使用-->
    	<input type="hidden" value="0" id="is_point_used">
      <label>使用积分<span>分</span></label>
      <div class="right" id="point_useable">
        <span><#if user??&&user.totalPoints??>${user.totalPoints?c}<#else>0</#if></span> 分可用
      </div>
    </a>
    <!-- 使用积分 END -->

    <!-- 商品清单 -->
    <section class="product-listing">
      <div class="title">商品清单</div>
      <#if buy_goods_list??>
    	<#list buy_goods_list as item>
	      <div class="div1">
	        <strong class="str1">${item.goodsTitle!''}</strong>
	        <strong class="str2">x <#if item.quantity??>${item.quantity?c}</#if></strong>
	        <strong class="str2">￥<#if item.price??>${(item.price*item.quantity)?string("0.00")}<#else>免费</#if></strong>
	      </div>
      	</#list>
      </#if>
      <div class="div2">
        <p class="p1">
          <strong class="str1"></strong>
          <strong class="str2">订单金额</strong>
          <strong class="str2" id="original_price"></strong>
        </p>
        <p class="p1">
          <strong class="str1"></strong>
          <strong class="str2">余额抵扣</strong>
          <strong class="str2">-￥<span id="pointD">0.00</span></strong>
        </p>
        <#--
        <p class="p1">
          <strong class="str1"></strong>
          <strong class="str2">运费</strong>
          <strong class="str2">￥0.00</strong>
        </p>
        -->
      </div>
      <div class="div3">
        <strong class="str1">还需支付</strong>
        <strong class="str2 total-price"></strong>
      </div>
    </section>
    <!-- 商品清单 END -->

  </article>
  <!-- 确认订单 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="package-foot" style="background-color:#666666;">
    还需支付￥<span class="total-price"></span>，确认下单
  </footer>
  <!-- 底部 END -->

</body>
</html>