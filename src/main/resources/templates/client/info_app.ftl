<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<head>
<meta name="keywords" content="">
<meta name="copyright" content="" />
<meta name="description" content="">
<meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<!--网页左上角小图标-->
<!-- <link rel="shortcut icon" href="/client/images/icon_logo.ico" /> -->
<title>循伍道</title>
<!-- css -->
<link rel="stylesheet" type="text/css" href="/client/css/pcdw.css"/>
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script>
function pcReg(){
	$(".download").css("display","none");
	$(".dengl").css("display","block");
}
function pcApp(){
	$(".dengl").css("display","none");
	$(".download").css("display","block");
}
</script>

<script>
var seed=60;    //60秒  
var t1=null; 
$(document).ready(function(){
	$("#agree_check").click(
		function(){
			$("#agree_check").toggleClass("active");
			
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
                	pcApp();
                	/*
                	if(typeof(data.url) != "undefined"){
                		location.href=data.url;
                	}
                	else{
                		location.href='/user/center';
                	}
                	*/
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
        //$("#smsCodeBtn").css("background-color","#999999");

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
                    //$("#smsCodeBtn").css("background-color","#72d377");
                }
                else{
                    alert("验证码发送失败，请再次尝试！");
                    console.log("message:"+res.message);
            		console.log("status:"+res.status);
                    $("#smsCodeBtn").removeAttr("disabled");
                    //$("#smsCodeBtn").css("background-color","#72d377");
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

</script>
</head>
<body>

  <h1>&nbsp;</h1>
  <div class="download">
    <img class="img1" src="/client/images/pc_pic_1.png" alt="" />
    <div class="right">
      <img src="/client/images/pc_pic_2.png" alt="" />
      <a  title="" <#if site??>href="/download/app?name=${site.androidDownload!''}"</#if>>
        <img src="/client/images/pc_pic_3.png" alt="" />
        <span>
          <img src="<#if site??>${site.androidQrCode!''}</#if>" alt="二维码" />
        </span>
      </a>
      <#--
      <a href="###" title="">
        <img src="/client/images/pc_pic_4.png" alt="" />
        <span>
          <img src="/client/images/qr_code.png" alt="二维码" />
        </span>
      </a>
      -->
      <a href="javascript:pcReg();" title="立即注册成为会员">
        <img src="/client/images/pc_pic_5.png" alt="" />
      </a>
    </div>
  </div>
  
  
  <div class="dengl" style="display:none;">
  <div class="close-btn">
  <a style="  float: right;bottom: 30px; left: 25px;  position: relative;" href="javascript:pcApp();">X</a>
  </div>
    <form>
      <div class="div1">会员注册</div>
      <span>手&nbsp;&nbsp;机：</span>
      <input class="ip-tx" type="text" id="txt_regMobile" placeholder="手机号码" />
      <span>密&nbsp;&nbsp;码：</span>
      <input class="ip-tx" type="password" id="txt_regPassword" placeholder="6-16位登陆密码" />
      <span>推&nbsp;&nbsp;荐&nbsp;&nbsp;码：</span>
      <input class="ip-tx" id="txt_regRfcode" type="text" placeholder="分享链接中的推荐码" value="<#if rfCode??>${rfCode!''}</#if>"/>
      	        <!--验证码-->
      <span ><a ><img src="/verify" height=32 alt="验证码" onclick="this.src = '/verify?date='+Math.random();" id="yzm"/></a></span>
      <input class="ip-tx" id="txt_regCode"  type="text" placeholder="验证码"/>
      <div class="security-code">
        <input type="text" id="txt_regMcode" placeholder="手机验证码" />
        <a class="sms" href="###" id="smsCodeBtn">发送手机验证码</a>
        

    	
        
      </div>
      <div class="div2">
        <input id="agree_check" class="active" type="checkbox" />
        我已经阅读并同意循伍道用户协议
      </div>
      <input class="btn-zhuc" type="button" id="reg_submit" value="注册" />
    </form>
  </div>

</body>
</html>