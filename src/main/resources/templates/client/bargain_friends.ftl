<!DOCTYPE html>
<html>
    <head>
        <style type="text/css">
            html,body{width:100%;height: 100%;}
        </style>
        <title><#if site??>${site.seoTitle!''}-</#if>车有同盟</title>
        <meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
        <meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
        <meta name="copyright" content="$<#if site??>{site.copyright!''}</#if>" />
        <meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
        <meta charset="utf-8">
        <title></title>
        
        <link rel="stylesheet" type="text/css" href="/activity/css/base.css"/>
        <link rel="stylesheet" type="text/css" href="/activity/css/friends.css"/>
        <script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
        <script src="/client/js/jweixin-1.0.0.js" type="text/javascript"></script>
       <#--> <script src="/activity/js/rich_lee.js" type="text/javascript"></script> -->
<script type="text/javascript">
$(document).ready(function(){
    $("#bangkan").click(function(){
        var openId = $("#openId").val();
        var mobile = $("#participantMobile").val();        
        $.ajax({
                type: "post",
                url: "/bargain/bangkan",
                data: { "openId": openId, "mobile": mobile},
                dataType: "json",
                success: function (data) {
                    if (data.code == 0) {   
                        $("#currentPrice").val(data.currentPrice);
                        $("#cutPrices").text(data.cutPrice.toFixed(2));                   
                        $("#cutpriceshow").css("display", "block"); 
                                           
                    }else if (data.code == 2) {
                         $("#showshare").css("display", "block");
                         $("#cutpriceshare").text(data.cutPrice.toFixed(2));
                    }
                    else if (data.code == 3) {
                         $("#bangkanshare").css("display", "block");
                    }
                    else if (data.code == 4) {
                         share();
                    }
                     else {
                        alert(data.msg);
                    }
                }
            });
    });
});

function cutPriceshow(){
    window.location.reload();
}

function share(){
    $("#cutpriceshow").css("display", "none");
    $("#showshare").css("display", "none");    
    $("#bangkanshare").css("display", "none");
    $("#sharewindow").css("display", "block"); 
}

function activityDesshow(){    
    $("#Activitydescription").css("display", "block");
}

function activityDeshide(){
    $("#Activitydescription").css("display", "none");
}

wx.config({
    debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
    appId: 'wx24ca6020b7040665', // 必填，公众号的唯一标识
    timestamp: ${timestamp!''}, // 必填，生成签名的时间戳
    nonceStr: '${noncestr!''}', // 必填，生成签名的随机串
    signature: '${signature!''}',// 必填，签名，见附录1
    jsApiList: ['onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'onMenuShareQZone'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
});

var title = "还有谁？  想全年免费洗车！";
var desc = "砍50次洗车";
var link="http://www.cytm99.com/bargain/friends?mobile=<#if participant??>${participant.mobile!''}</#if>";
var imgUrl="http://www.cytm99.com/activity/img/share.png";

wx.ready(function(){    
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。
    
    wx.checkJsApi({
    jsApiList: ['onMenuShareTimeline',
                'onMenuShareAppMessage',
                'onMenuShareQQ',
                'onMenuShareWeibo',
                'onMenuShareQZone'], // 需要检测的JS接口列表，所有JS接口列表见附录2,
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
    wx.onMenuShareTimeline({
      title: title,
      link: link,
      imgUrl: imgUrl,
      trigger: function (res) {
        // 不要尝试在trigger中使用ajax异步请求修改本次分享的内容，因为客户端分享操作是一个同步操作，这时候使用ajax的回包会还没有返回
       // alert('用户点击分享到朋友圈');
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
       // alert('已分享');
      },
      cancel: function (res) {
       // alert('已取消');
      },
      fail: function (res) {
       // alert(JSON.stringify(res));
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
       // alert('已分享');
      },
      cancel: function (res) {
       // alert('已取消');
      },
      fail: function (res) {
       // alert(JSON.stringify(res));
      }
    });
});
</script> 
    </head>
    <script type="text/javascript">
        
    </script>
    <body>
        <div class="win_share" style="display:none" id="sharewindow">
            <div class="win_share_box">
                <img src="/activity/img/win_share.png"  />
            </div>
        </div>
        <div class="win" id="cutpriceshow">
            <dl class="win_box">
                <dt>
                    <a id="closewindow" href="javascript:cutPriceshow();" style="float:right; padding-right:10px">X</a>
                </dt>
                <dd>
                    <div id="cutPricebackground"><span id="cutPrices"></span></div>
                    <input type="button" name="" id="" onclick="javascript:share();" value="找盆友帮TA砍" />
                </dd>
            </dl>
            
        </div>
        
         <div class="win" id="showshare" >
            <dl class="win_box" style="height:150px">
                <dt>
                    <a id="closewindow" href="javascript:cutPriceshow();" style="float:right; padding-right:10px">X</a>
                </dt>
                <dd>
                    <p>
                        <span>减&nbsp;<b id = "cutpriceshare">123.12</b></span><br>
                        <span>"快、准、狠"</span><br>
                        <span>分享它，看谁比你强</span> 
                    </p>
                    <input type="button" name="" id="" onclick="javascript:share();" value="找盆友帮TA砍" />
                </dd>
            </dl>
            
        </div>
        
        <div class="win" id="bangkanshare" >
            <dl class="win_box" style="height:150px">
                <dt>
                    <a id="closewindow" href="javascript:cutPriceshow();" style="float:right; padding-right:10px">X</a>
                </dt>
                <dd>
                    <p>
                        
                        <span>添加关注、参加活动</span><br>
                        <span>点击右上角查看公众号</span> 
                    </p>
                    <input type="button" name="" id="" onclick="javascript:share();" value="找盆友帮TA砍" />
                </dd>
            </dl>
            
        </div>
        
         <div class="win" id="Activitydescription" >
            <dl class="win_box" style="height:300px">
                <dt>
                    <a id="closewindow" href="javascript:activityDeshide();" style="float:right; padding-right:10px">X</a>
                </dt>
                <dd style="text-align:left">
                    <p>
                        <span >1、   邀请好友来砍价，砍到0元即可享受全年50次免费洗车。</span><br />
    2、  每人可砍价一次，快邀请好友来帮忙砍价，帮砍金额都是随机的，拼人品，拼人气。<br />
    3、  砍到理想价格，不忍心再砍了，车友可以砍到的实时价格下单支付购买，下单时可选择任一线下同盟店服务。<br />
    4、  关注车有同盟公众号，填写真实姓名和电话号码报名参与。<br />
    5、  一旦系统发现作弊，将自动取消购买资格哦。 
                    </p>
              
                </dd>
            </dl>
            
        </div>
        
        <div class="friends_banner">
             <div class="perd" style="width:<#if participant?? && participant.currentPrice??>${(2000 - participant.currentPrice)/20}%</#if>">
                    </div>
                    <#if participant?? && participant.currentPrice??> 
                        <#if participant.currentPrice < 1000 && participant.currentPrice gt 500> 
                            <span></span>
                        <#elseif participant.currentPrice < 500 && participant.currentPrice gt 0>       
                            <span></span>
                            <span></span>
                        <#elseif participant.currentPrice == 0>
                            <span></span>
                            <span></span>
                            <span></span>
                        </#if>
                    <#else>
                        <span></span>   
                        <span></span>
                        <span></span>
                    </#if>             
        </div>
        <section class="friends_content">
            <p>已有${totalrecords!'0'}位好友帮助<a><#if participant??>&nbsp;${participant.trueName!''}&nbsp;</#if></a>砍价成功</p>
            <p>当前价格为<span id ="currentPrice"><#if participant?? && participant.currentPrice??>${participant.currentPrice?string("0.00")}</#if></span>元</p>
            <div class="pers">
                <dl class="dl01">
                    <dt>￥2000</dt>
                    <dd>￥1000</dd>
                    <dd>￥500</dd>
                    <dd>￥0</dd>
                </dl>
                <div class="pers_box">
                    <img src="/activity/img/friend_per.png"  />
                </div>
                <dl class="dl02">
                <#-->    <dt>总共剩余<span><#if bargain_setting??>${bargain_setting.totalLeftNumber!''}</#if></span>张</dt>
                    <dd>今日剩余<div><span>00</span>张</div></dd>
                    <dd>今日剩余<div><span>00</span>张</div></dd>
                    <dd>今日剩余<div><span>00</span>张</div></dd> -->
                    <span style="font-size:12px; text-align:center">活动截止日期：2015年12月13日，活动期间任意金额下单支付均有效</span>
                </dl>
            </div>
            <input type="hidden" id="openId" value="${openId!''}">
            <input type="hidden" id="participantMobile" value="<#if participant??>${participant.mobile!''}</#if>">
            <div class="friend_btn">
                <input class="friend_fu" type="button" name="bangkan" id="bangkan" value="" />
                <input type="button" name="" id="" value="" onclick="window.location.href='/bargain/login'"/>
            </div>
            <div class="btn_text">
                <P>共有<span><#if bargain_setting??>${bargain_setting.totalParticipant!''}</#if></span>人参与砍价</P>
                <a href="javascript:activityDesshow();">活动说明</a>
            </div>
            <dl class="dl03">
                <dt>
                    <span>亲友团</span>
                    <span>砍掉价格</span>
                    <span>剩余价格</span>
                </dt>
               <#if bargain_record_page??>
                    <#list bargain_record_page.content as item>
                        <dd>
                            <span>
                                <img src="${item.headerUrl!''}" width="26" height="26" />
                                <p style="height:20px; overflow:hidden">${item.nickName!''}</p>
                            </span>
                            <span><s><#if item.cutPrice??>${item.cutPrice?string("0.00")}</#if>元</s></span>
                            <span>
                                <#if item.newPrice??>${item.newPrice?string("0.00")}</#if>元
                            </span>
                        </dd>
                    </#list>
                </#if>
            </dl>
        </section>
    </body>
</html>
