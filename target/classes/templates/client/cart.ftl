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
$(document).ready(function(){
//计算总价格
	var totalPrice = 0.00;
	var priceObj = $("input[name='price']");
	for(var i=0; i<priceObj.length;i++)
		{
	    		totalPrice=parseFloat(totalPrice)+parseFloat(priceObj[i].value);
		}
	
	$("#total_price").html(totalPrice.toFixed(2));

	
});

	function checkAll(){
		if($('#checkAll').attr('checked')== "checked")
		{
			$(".check-icon").removeClass("checked");
			$(".check-icon").removeAttr("checked");
		}
		else{
			$(".check-icon").addClass("checked");
			$(".check-icon").attr("checked","checked");
			$("#total_price").html("0.00");
		}
		
		setTotalPrice();
	}

	function cartEdit(index)
	{
	    $("#cart_check"+index).toggleClass("checked");
	    if($("#cart_check"+index).attr('checked') == "checked")
	    {
	        $("#cart_check"+index).removeAttr("checked");
	    }
	    else{
	        $("#cart_check"+index).attr("checked","checked");
	    }
	    
	    var checkObj = $("input[name='checkIcon']");
	    var number = 0;
	    for(var i=0; i<checkObj.length;i++)
	    {
	        if($(checkObj[i]).attr('checked') == "checked")
	        {
	            number = number +1;
	        }
	        console.log("number"+number);
	    }
	    console.log("finalnumber"+number);
	    if (number == 0)
	    {
	        $("#checkAll").removeAttr("checked");
	        $("#checkAll").removeClass("checked");
	    }
	    else if(number == checkObj.length){
	        $("#checkAll").attr("checked","checked");
	        $("#checkAll").addClass("checked");
	    }
	    setTotalPrice();
	}

function setTotalPrice()
{
	var totalPrice = 0.00;
	var checkObj = $("input[name='checkIcon']");
	for(var i=0; i<checkObj.length;i++)
		{
			if($(checkObj[i]).attr("checked") == "checked")
			{
				var price = $("#price_"+i).val();
	    		totalPrice=parseFloat(totalPrice)+parseFloat(price);
			}
		}
	
	$("#total_price").html(totalPrice.toFixed(2));
}

function cartSubmit()
{
	var goodsObj = $("input[name='checkIcon']");
	var idObj =  $("input[name='goodsId']");
	var goodsIds = new Array();
	var j = 0;
	for(var i=0; i<goodsObj.length;i++)
	{
		if($(goodsObj[i]).attr("checked") == "checked"){
    		goodsIds[j]=idObj[i].value;
    		j++;
    	}
	}
	$.ajax({
	      type:"post",
	      url:"/order/editCart",
	      traditional:true,
	      data:{"goodsIds":goodsIds},
	      success:function(data){
			if (data.code == 1)
			{
	            alert(data.msg);
	            if(data.login == 1){
	            	location.href='/login';
	            }
			}
			else{
				location.href='/order/info';
			}
	      }
	  });
}

</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <#--<a class="shop-edit" href="#">编辑</a>-->
    <p class="c333">购物车</p>
  </header>
  <!-- 头部 END -->

  <!-- 购物车 -->
  <article class="my-collection">
  <#if cart_goods_list??>
  		<#list cart_goods_list as item>
		    <section class="collect-lists" id="cart_item${item_index}">
		      <div class="checkbox">
		        <input type="checkbox" class="checked check-icon" id="cart_check${item_index}" name="checkIcon" checked="checked" onclick="javascript:cartEdit(${item_index});">
		        <input type="hidden" name="goodsId" value="${item.id?c}">
		      </div>
		      <a class="right">
		        <div class="img">
		          <img src="<#if item.goodsCoverImageUri?? && item.goodsCoverImageUri?length gt 0>${item.goodsCoverImageUri}<#else>/client/images/pic_product_1.jpg</#if>" alt="产品图">
		        </div>
		        <div class="pro-descript">
		          <div class="div1">
		            ${item.goodsTitle!''}
		          </div>
		          <div class="price">
		            <p class="p1">￥<span><#if item.price??>${item.price?string("0.00")}<#else>0.00</#if></span></p>
		            <p class="p2">x ${item.quantity!''}</p>
		            <input type="hidden" name="price" value="<#if item.price??>${item.price?string("0.00")}<#else>0.00</#if>" id="price_${item_index}"">
		          </div>
		        </div>
		      </a>
		    </section>
    	</#list>
    </#if>
  </article>
  <!-- 购物车 END -->

  <div class="clear h100"></div>

  <!-- 全选 -->
  <article class="collect-footer">
    <div class="left">
      <!-- 选中给input加类名"checked" -->
      <input type="checkbox" class="check-icon checked" id="checkAll" checked="checked" onclick="javascript:checkAll();">全选
    </div>
    <div class="total-amount">
      合计:<strong>￥<span id="total_price"></span></strong>
    </div>
    
    <a class="btn-buy" href="javascript:cartSubmit();">结算</a>
   
    <!-- <a class="btn-delete" href="#">删除</a> -->
  </article>
  <!-- 全选 END -->

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>