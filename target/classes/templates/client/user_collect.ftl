<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="">
<meta name="copyright" content="" />
<meta name="description" content="">
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>我的收藏</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
<script>
function checkAll(){
    if($('#checkAll').attr('checked')== "checked")
    {
        $(".check-icon").removeClass("checked");
        $(".check-icon").removeAttr("checked");
    }
    else{
        $(".check-icon").addClass("checked");
        $(".check-icon").attr("checked","checked");
    }
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
}

//买买买
function buybuybuy()
{
    var goodsObj = $("input[name='checkIcon']");
    var idObj =  $("input[name='id']");
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
          url:"/order/buybuybuy",
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
                location.href='/order/buyC';
            }
          }
      });
}

//删除收藏
function deleteC()
{
    var goodsObj = $("input[name='checkIcon']");
    var idObj =  $("input[name='id']");
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
          url:"/user/collect/delete",
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
                location.href='/user/collect';
            }
          }
      });
}
</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="/user/center"></a>
    <p class="c333">我的收藏</p>
  </header>
  <!-- 头部 END -->

  <!-- 我的收藏 -->
  <article class="my-collection">
    <#if collect_list??>
        <#list collect_list as item>
		    <section class="collect-lists">
		      <div class="checkbox">
                <input type="checkbox" class="check-icon" id="cart_check${item_index}" name="checkIcon" onclick="javascript:cartEdit(${item_index});">
                <input type="hidden" name="id" value="${item.goodsId?c}">
		      </div>
		      <a class="right">
		        <div class="img">
		          <img src="<#if item.goodsCoverImageUri?? && item.goodsCoverImageUri?length gt 0>${item.goodsCoverImageUri}<#else>/client/images/pic_product_1.jpg</#if>" alt="产品图">
		        </div>
		        <div class="pro-descript">
		          <div class="div1">
		              ${item.goodsTitle!''}
		          </div>
		          <div class="div2">￥<#if item.goodsSalePrice??>${item.goodsSalePrice?string("0.00")}<#else>0.00</#if></div>
		        </div>
		      </a>
		    </section>
        </#list>
     </#if> 
  </article>
  <!-- 我的收藏 END -->

  <div class="clear h100"></div>

  <!-- 全选 -->
  <article class="collect-footer">
    <div class="left">
      <!-- 选中给input加类名"checked" -->
      <input type="checkbox" class="check-icon" id="checkAll" onclick="javascript:checkAll();">全选
    </div>
    <a class="btn-buy" href="javascript:buybuybuy();">购买</a>
    <a class="btn-delete" href="javascript:deleteC();">删除</a>
  </article>
  <!-- 全选 END -->

  <!-- 底部 -->
  <footer class="index-foot">
    <#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>