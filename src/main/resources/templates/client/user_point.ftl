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

<!--微信js-->
<script language="javascript" src="/client/js/jweixin-1.0.0.js" type="text/javascript"></script>
<script>
  /*
   * 注意：
   * 1. 所有的JS接口只能在公众号绑定的域名下调用，公众号开发者需要先登录微信公众平台进入“公众号设置”的“功能设置”里填写“JS接口安全域名”。
   * 2. 如果发现在 Android 不能分享自定义内容，请到官网下载最新的包覆盖安装，Android 自定义分享接口需升级至 6.0.2.58 版本及以上。
   * 3. 常见问题及完整 JS-SDK 文档地址：http://mp.weixin.qq.com/wiki/7/aaa137b55fb2e0456bf8dd9148dd613f.html
   *
   * 开发中遇到问题详见文档“附录5-常见错误及解决办法”解决，如仍未能解决可通过以下渠道反馈：
   * 邮箱地址：weixin-open@qq.com
   * 邮件主题：【微信JS-SDK反馈】具体问题
   * 邮件内容说明：用简明的语言描述问题所在，并交代清楚遇到该问题的场景，可附上截屏图片，微信团队会尽快处理你的反馈。
   */
 wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: 'wx106a7dad60722ce1', // 必填，公众号的唯一标识
    timestamp: ${timestamp!''}, // 必填，生成签名的时间戳
    nonceStr: '${noncestr!''}', // 必填，生成签名的随机串
    signature: '${signature!''}',// 必填，签名，见附录1
    jsApiList: ['onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'onMenuShareQZone'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

var title = "健康交给循伍道，活到100不算老。";
var desc = "<#if rfCode??>我的推荐码是：${rfCode!''}。</#if>";
var link="http://www.xwd33.com/reg?rfCode=<#if rfCode??>${rfCode!''}</#if>";
var imgUrl="http://www.xwd33.com/client/images/pic_product_1.jpg";

wx.ready(function(){    
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    
    wx.checkJsApi({
    jsApiList: ['onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'onMenuShareQZone',
                'showOptionMenu'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
    success: function(res) {
        // 以键值对的形式返回，可用的api值true，不可用为false
        // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
//       alert(JSON.stringify(res));
    }
    });
    
    
    // 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
    wx.onMenuShareAppMessage({
      title: title,
      desc: desc,
      link: link,
      imgUrl: imgUrl,
      trigger: function (res) {
        // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
       // alert('用户点击发送给朋友');
      },
      success: function (res) {
       // alert('已分享');
      },
      cancel: function (res) {
       // alert('已取消');
      },
      fail: function (res) {
       // alert(JSON.stringify(res));
      }
    });
   
    // 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
document.querySelector('#onMenuShareTimeline').onclick = function () {
    wx.onMenuShareTimeline({
      title: title+desc,
      link: link,
      imgUrl: imgUrl,
      trigger: function (res) {
        // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
       // alert('用户点击分享到朋友圈');
      },
      success: function (res) {
        alert('已分享');
      },
      cancel: function (res) {
        alert('已取消');
      },
      fail: function (res) {
        alert(JSON.stringify(res));
      }
    });
};
    // 监听“分享到QQ”按钮点击、自定义分享内容及分享结果接口
     wx.onMenuShareQQ({
      title: title,
      desc: desc,
      link: link,
      imgUrl: imgUrl,
      trigger: function (res) {
       // alert('用户点击分享到QQ');
      },
      complete: function (res) {
       // alert(JSON.stringify(res));
      },
      success: function (res) {
        alert('已分享');
      },
      cancel: function (res) {
        alert('已取消');
      },
      fail: function (res) {
        alert(JSON.stringify(res));
      }
    });
    
    // 监听“分享到QQ空间”按钮点击、自定义分享内容及分享结果接口
    wx.onMenuShareQZone({
    title: title,// 分享标题
    desc: desc,// 分享描述
    link: link, // 分享链接
    imgUrl: 'imgUrl', // 分享图标
    success: function () { 
       alert('已分享');// 用户确认分享后执行的回调函数
    },
    cancel: function () { 
        alert('已取消');// 用户取消分享后执行的回调函数
    }
});
    
    // 监听“分享到微博”按钮点击、自定义分享内容及分享结果接口
     wx.onMenuShareWeibo({
      title: title,
      desc: desc,
      link: link,
      imgUrl: imgUrl,
      trigger: function (res) {
       // alert('用户点击分享到微博');
      },
      complete: function (res) {
       // alert(JSON.stringify(res));
      },
      success: function (res) {
        alert('已分享');
      },
      cancel: function (res) {
        alert('已取消');
      },
      fail: function (res) {
        alert(JSON.stringify(res));
      }
    });
    
     document.querySelector('#showOptionMenu').onclick = function () {
    wx.showOptionMenu();
  };
});
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
      <p>我的推荐码：${rfCode!''}</p>
      <p>分享给好友，即可根据其消费获取相应的积分<a href="javascript:share();">立即分享</a><a id="showOptionMenu" >微信分享</a></p>
      <a id="onMenuShareTimeline" >微信到朋友圈</a>
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