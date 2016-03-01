<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>出错啦</title>
<link rel="shortcut icon" href="/client/images/icon.ico" />
<meta name="keywords" content="">
<meta name="description" content="">
<meta name="copyright" content="" />
<script type="text/javascript" src="/client/js/jquery-1.11.3.min.js"></script>


<script type="text/javascript">
	$(document).ready(function(){
		<#if msg?? && msg?length gt 0>
			alert("${msg!''}");
		</#if>
		<#if url?? && url?length gt 0>
			location.href='${url!''}';
		<#else>
			history.back(-1);
		</#if>
	});
</script>

</head>

<body>

</body>
</html>
