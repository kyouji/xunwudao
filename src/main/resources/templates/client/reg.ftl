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
<style type="text/css">
  #popups {
    position: absolute;
    left: 0;
    top: 0;
    z-index: 10;
    display: none;
    padding: 2%;
    width: 96%;
    height: 100%;
    background-color: #fff;
  }
  #popups a {
    position: relative;
    left: 50%;
    bottom: 0;
    display: block;
    margin-left: -30px;
    width: 60px;
    height: 30px;
    line-height: 30px;
    color: #fff;
    text-align: center;
    background-color: #72d377;
  }
</style>
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
                              redirect_uri: "http://www.xwd33.com?rfCode=${rfCode!''}",
                              state: "state",
                              style: "",
                              href: ""
                            });
                            
        location.href='https://open.weixin.qq.com/connect/qrconnect?appid=${appId!''}&redirect_uri=http%3A%2F%2Fwww.xwd33.com?rfCode=${rfCode!''}&response_type=code&scope=snsapi_login&state=STATE#wechat_redirect'                    
	}
}
</script>
</head>
<body>

  <!-- 弹窗 -->
  <div id="popups">
    <pre style="line-height: 24px;font-size: 1.1em;"><#if site??&&site.registerNego??&&site.registerNego?length gt 0>${site.registerNego}<#else>如果您违反了以上约定，相关国家机关或机构可能会对您提起诉讼、罚款或采取其他制裁措施，并要求财税宝1688给予协助。造成损害的，您应依法予以赔偿，财税宝1688不承担任何责任。 您在使用本服务过程中应当遵守当地相关的法律法规，并尊重当地的道德和风俗习惯。如果您的行为违反了当地法律法规或道德风俗、宗教信仰等，您应当为此独立承担责任。您应避免因使用本服务而使财税宝1688卷入政治和公共事件，否则财税宝1688有权暂停或终止对您的服务并保留向您追偿的权利。 您同意，未经财税宝1688许可，不使用任何可能对财税宝1688产品或服务造成任何不良影响的第三方软件，如果擅自使用第三方软件给财税宝1688造成损失的，财税宝1688保留对您追偿的权利。您违反本条约定，导致任何第三方损害的，您应当独立承担责任；财税宝1688因此遭受损失的，您也应当一并赔偿。 在任何情况下，您不应轻信借款、索要密码或其他涉及财产的网络信息。涉及财产操作的，请一定先核实对方身份，并请经常留意财税宝1688有关防范诈骗犯罪的提示。 您有责任自行备份存储在本服务中的数据。如果您的服务被终止，财税宝1688可以从服务器上永久地删除您的数据且不承担任何责任。服务终止后，财税宝1688没有义务向您返还数据。</#if></pre>
    <a href="javascript:;" title="">关闭</a>
  </div>
  <!-- 弹窗 end -->
  <script type="text/javascript">
    $(function(){
      $(".service-agreement>a").click(function(){
        $("#popups").show();
        // e.stopPropagation();  //阻止冒泡事件
      });
      $("#popups a").click(function(){
        $("#popups").hide();
      });
    })
  </script>

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
        <input class="password" id="txt_regPassword" type="text" placeholder="请输入6-16位登陆密码">

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
          <a href="javascript:void(0)">
            <#--<img src="/client/images/icon_login_phone.png" alt="手机登录">-->
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