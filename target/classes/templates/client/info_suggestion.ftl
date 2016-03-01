<!DOCTYPE html>
<html lang="zh-CN">
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
<!-- js -->
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>
<script type="text/javascript" src="/client/js/index.js"></script>
<script>
function suggestionSubmit(){
	var content = $("#content").val();
	
	$.ajax({
        type:"post",
        url:"/suggestion/submit",
        data:{"content":content},
        success:function(data){
          if (data.code == 1)
          {
              alert(data.msg);
          }
          else{
        	  alert(data.msg);
        	  location.reload();
          }
        }
    });
}
</script>
</head>
<body>

  <!-- 头部 -->
  <header class="login-head">
    <a class="points-back" href="javascript:history.back(-1);"></a>
    <p class="c333">意见或建议</p>
  </header>
  <!-- 头部 END -->

  <!-- 意见反馈 -->
  <article class="opinion">
    <form>
      <div class="title">填写意见或建议</div>
      <textarea id="content" name="content"></textarea>
      <input type="button" onclick="javascript:suggestionSubmit();" value="提 交">
    </form>
  </article>
  <!-- 意见反馈 END -->

  <div class="clear h50"></div>

  <!-- 底部 -->
  <footer class="index-foot">
	<#include "/client/common_footer.ftl" />
  </footer>
  <!-- 底部 END -->

</body>
</html>