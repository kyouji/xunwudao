<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="<#if site??>${site.seoKeywords!''}</#if>">
<meta name="description" content="<#if site??>${site.seoDescription!''}</#if>">
<meta name="copyright" content="<#if site??>${site.copyright!''}</#if>" />
<meta name="viewport" content="width=device-width,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<title>循伍道-注册</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/common.css"/>
<link rel="stylesheet" type="text/css" href="/client/css/main.css"/>
<script src="/client/js/jquery-1.11.3.min.js" type="text/javascript"></script>
<script>
var seed=60;    //60秒  
var t1=null; 
$(document).ready(function(){
	$("#agree_check").click(
		function(){
			$("#agree_check").toggleClass("checked");
			
			if($("#reg_submit").attr("disabled")=="disabled")
			{
				$("#reg_submit").removeAttr("disabled","disabled");
				$("#reg_submit").css("background-color","#72d377");
			}
			else{
				$("#reg_submit").attr("disabled","disabled");
				$("#reg_submit").css("background-color","#999999");
			}
		}
	);
	
	//注册提交
	$("#reg_submit").bind("click", function(){
		var mobile=$("#txt_regMobile").val();
		var mCode=$("#txt_regMcode").val();
		var rfCode=$("#txt_regRfcode").val();
		var password=$("#txt_regPassword").val();
		var code=$("#txt_regCode").val();
		
		$.ajax({  
            url : "/reg",  
            async : true,  
            type : 'POST',  
            data : {"mobile": mobile,
            			"smsCode": mCode,
            			"rfCode": rfCode,
            			"password": password,
            			"code": code},  
            success : function(data) {  
                if(data.code==1){
                	alert(data.msg);
                	if(typeof(data.id) != "undefined"){
                		document.getElementById(data.id).focus;
                	}
                }
                else if(data.code==0){
                	alert(data.msg);
                	if(typeof(data.url) != "undefined"){
                		location.href=data.url;
                	}
                	else{
                		location.href='/user/center';
                	}
                	console.log("url:"+data.url);
                }
             }   
        });
 
	}); 
	
	//手机验证码
    $("#smsCodeBtn").bind("click", function() {  
        
        var mob = $('#txt_regMobile').val();
        
        var re = /^1\d{10}$/;
        
        if (!re.test(mob)) {
			alert("请输入正确的手机号！");
			return;
        }
        
        //发送短信要先输入验证码
        var code=$("#txt_regCode").val();
        
        $("#smsCodeBtn").attr("disabled","disabled"); 
        $("#smsCodeBtn").css("background-color","#999999");

        $.ajax({  
            url : "/reg/smscode",  
            async : true,  
            type : 'GET',  
            data : {"mobile": mob,
            			"code":code},  
            success : function(res) {  
            console.log("message:"+res.message);
            console.log("status:"+res.status);
                if(1==res.status||0==res.status){
                    //alert("验证码已发送，请耐心等待！");
                     t1 = setInterval(tip, 1000);  
                }
                else if(null != res.msg)
                {
                	alert(res.msg);
                    $("#smsCodeBtn").removeAttr("disabled");
                    $("#smsCodeBtn").css("background-color","#72d377");
                }
                else{
                    alert("验证码发送失败，请再次尝试！");
                    console.log("message:"+res.message);
            		console.log("status:"+res.status);
                    $("#smsCodeBtn").removeAttr("disabled");
                    $("#smsCodeBtn").css("background-color","#72d377");
                }
            },  
            error : function(XMLHttpRequest, textStatus,  
                    errorThrown) {  
		                //alert( "error！");
		                //$("#smsCodeBtn").removeAttr("disabled");
		                 t1 = setInterval(tip, 1000);  
            }  
        });
    }); 
});

function tip() 
{  
    seed--;  
    if (seed < 1) 
    {  
        $("#smsCodeBtn").removeAttr("disabled");   
        $("#smsCodeBtn").css("background-color","#72d377");
        seed = 60;  
        $("#smsCodeBtn").html('获取验证码');  
        var t2 = clearInterval(t1);  
    } else {  
        $("#smsCodeBtn").html(seed);  
    }  
}

function weixin(){
	//判断是否微信打开
	var ua = navigator.userAgent.toLowerCase();
	
	if(ua.match(/MicroMessenger/i)=="micromessenger") 
	{
		location.href="/weixin/login";
	}
	else{
		var obj = new WxLogin({
                              id:"login_container", 
                              appid: "${appId!''}", 
                              scope: "snsapi_login", 
                              redirect_uri: "http://www.xwd33.com",
                              state: "state",
                              style: "",
                              href: ""
                            });
                            
        location.href='https://open.weixin.qq.com/connect/qrconnect?appid=${appId!''}&redirect_uri=http%3A%2F%2Fwww.xwd33.com&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect'                    
	}
}
</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="back" href="javascript:history.back(-1);"></a>
    <p>注册</p>
  </header>
  <!-- 头部 END -->

  <!-- 注册 -->
  <article class="login-form">
    <form>
      <section class="enter-info">
      <#--
      <input type="button" class="select-btn selected" id="referer_aru" value="有推荐人" >
      <input type="button" class="select-btn" id="referer_nai" value="无推荐人" >
      -->
        <input class="user-name"  id="txt_regMobile"  type="tel" placeholder="请输入您的手机号">
              <!--验证码-->
    	<input class="password" id="txt_regCode"  type="text" placeholder="验证码"/>
        <a><img src="/verify" height=46.7px alt="验证码" onclick="this.src = '/verify?date='+Math.random();" id="yzm"/></a>
        <!-- 获取验证码 -->
        <a class="get-code" id="smsCodeBtn" href="javascript:void(0)">获取短信验证码</a>
        <input class="password" id="txt_regMcode" type="tel" placeholder="请输入收到的短信验证码">
        <input class="password" id="txt_regRfcode" type="tel" placeholder="若有推荐人请输入推荐码" value="<#if rfCode??&&rfCode?length gt 0>${rfCode}</#if>">
        <input class="password" id="txt_regPassword" type="password" placeholder="请输入6-16位登陆密码">

        <!-- 服务协议 -->
        <div class="service-agreement">
          <!-- 选中给Input加类名"checked" -->
          <input type="checkbox" id="agree_check" class="checked">
          我已经阅读并同意
          <a href="#">循伍道用户协议</a>
        </div>
        <input class="btn-login" id="reg_submit" type="button"  value="验证并注册" >
      </section>
    </form>
        <!-- 其他方式登录 -->
    <section class="other-ways" style="margin-top:2%;">
      <div class="title">其他方式登录</div>
      <ul>
        <li>
          <a href="javascript:weixin();">
            <img src="/client/images/icon_login_weixin.png" alt="微信登录">
            <#--<a href="http://weixin.qq.com/r/OEzUzGjEzTWyrSyv9xkq"><img src="/client/images/icon_login_phone.png"></a>-->
          </a>
        </li>
        <li>
          <a href="/login/mobile?goodsId=<#if goodsId??>${goodsId?c}</#if>">
            <img src="/client/images/icon_login_phone.png" alt="手机登录">
          </a>
        </li>
        <li>
          <a href="/qq/login?rfCode=<#if rfCode??&&rfCode?length gt 0>${rfCode}</#if>">
            <img src="/client/images/icon_login_qq.png" alt="QQ登录">
          </a>
        </li>
      </ul>
    </section>
  </article>
  <!-- 注册 END -->

</body>
</html>