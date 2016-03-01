<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>用户选择</title>
<script type="text/javascript" src="/mag/js/jquery-1.10.2.min.js"></script>
<script type="text/javascript" src="/mag/js/layout.js"></script>
<script type="text/javascript" src="/mag/js/jquery.lazyload.min.js"></script>
<script type="text/javascript" src="/mag/js/lhgdialog.js?skin=idialog"></script>
<link href="/mag/style/style.css" rel="stylesheet" type="text/css">
<link href="/mag/style/pagination.css" rel="stylesheet" type="text/css">

<script type="text/javascript">
    //窗口API
    var api = frameElement.api, W = api.opener;
    api.button({
        name: '确定',
        focus: true,
        callback: function () {
            submitForm();
            return false;
        }
    }, {
        name: '取消'
    });
    

    //页面加载完成执行
    $(function () {
        if ($(api.data).length > 0) {
            var parentObj = $(api.data).parent().parent(); //取得节点父对象            
            //开始赋值
            $("#txtItemtdFinance_username").val($(parentObj).find("input[id='username']").val()); 
            $("#txtItemtdFinance_id").val($(parentObj).find("input[id='id']").val()); 
            $("#txtItemtdFinance_realName").val($(parentObj).find("input[id='realName']").val()); 
            $("#txtItemtdFinance_money").val($(parentObj).find("input[id='money']").val()); 
        }
    });

    //创建促销赠品窗口
    function show_goods_select_dialog(obj) {
        var objNum = arguments.length;
        var zengpinDialog = $.dialog({
            id: 'zengpinhDialogId',
            lock: true,
            max: false,
            min: false,
            title: "促销赠品",
            content: 'url:/Verwalter/goods/list/dialog/gift',
            width: 800,
            height: 550
        });
        //如果是修改状态，将对象传进去
        if (objNum == 1) {
            zengpinDialog.data = obj;
        }
    }
    
    //删除促销赠品节点
    function del_goods_gift(obj) {
        $(obj).parent().parent().remove();
    }
    
    //提交表单处理
    function submitForm() {
        //验证表单
        if ($("#txtItemtdFinance_username").val() == ""
          || $("#txtItemtdFinance_id").val() == "" 
          || $("#txtItemtdFinance_realName").val() == ""
          || $("#txtItemtdFinance_money").val() == "") {
            W.$.dialog.alert('请填写完整信息！', function () { $("#txtItemtdFinance_username").focus(); }, api);
            return false;
        }
        
           var reg = new RegExp("^[0-9]+(.[0-9]{2})?$");  
	       var obj1 = document.getElementById("txtItemtdFinance_id");  
	       var obj2 = document.getElementById("txtItemtdFinance_realName");  
	       var obj3 = document.getElementById("txtItemtdFinance_money");  
	    if(!reg.test(obj1.value) ){  
            W.$.dialog.alert('请输入数字(最多2位小数)！', function () { $("#txtItemtdFinance_id").focus(); }, api);
            return false;
        }
	    if(!reg.test(obj2.value) ){  
            W.$.dialog.alert('请输入数字(最多2位小数)！', function () { $("#txtItemtdFinance_realName").focus(); }, api);
            return false;
        }
        if(!reg.test(obj3.value) ){  
            W.$.dialog.alert('请输入数字(最多2位小数)！', function () { $("#txtItemtdFinance_money").focus(); }, api);
            return false;
        }   
        //添加或修改
        if ($(api.data).length > 0) {
        
            var parentObj = $(api.data).parent().parent();
		 
            parentObj.find("input[id='title']").val($("#txtItemtdFinance_username").val());
            parentObj.find("input[id='amount']").val($("#txtItemtdFinance_id").val());
            parentObj.find("input[id='price']").val($("#txtItemtdFinance_realName").val());
            parentObj.find("input[id='money']").val($("#txtItemtdFinance_money").val());
        } else {
		 
            var trHtml = '<tr class="td_c">'
            + '<td><input name="priceItemList[${total!'0'}].id" type="hidden" value="" />'
            + '<input type="text" name="stockList[${total!'0'}].sortId" class="td-input" value="99" style="width:90%;" /></td>'
            + '<td><input type="text" id="title" name="stockList[${total!'0'}].title" class="td-input" value="' + $("#txtItemtdFinance_username").val() + '" style="width:90%;" /></td>'
        //zhangji
            + '<td><input type="text" id="salePrice" name="stockList[${total!'0'}].amount" class="td-input" value="' + $("#txtItemtdFinance_id").val() + '" style="width:90%;" /></td>'
            + '<td><input type="text" id="realSalePrice" name="stockList[${total!'0'}].price" class="td-input" value="' + $("#txtItemtdFinance_realName").val() + '" style="width:90%;" /></td>'
            + '<td><input type="text" id="stockPrice" name="stockList[${total!'0'}].money" class="td-input" value="' + $("#txtItemtdFinance_money").val() + '" style="width:90%;" /></td>'
            + '<td>'
            + '<i class="icon"></i>'
            + '<a title="编辑" class="img-btn edit operator" onclick="show_goods_gift_dialog(this);">编辑</a>'
            + '<a title="删除" class="img-btn del operator" onclick="del_goods_gift(this);">删除</a>'
            + '</td>'
            + '</tr>'
            //如果是窗口调用则添加到窗口
            if (!api.get('dialogChannel') || !api.get('dialogChannel')) {
                $("#var_box_gift", W.document).append(trHtml);
                $("#totalGift", W.document).val(parseInt($("#totalGift", W.document).val())+1);
            } else {
                $("#var_box_gift", api.get('dialogChannel').document).append(trHtml);
                $("#totalGift", W.document).val(parseInt($("#totalGift", W.document).val())+1);
            }
        }
        api.close();
    }
    
    $(function () {
        $(".itemzengpin_select").click(function () {

            var itemzengpin_title = $(this).attr("itemzengpin_title");
            var itemzengpin_id = $(this).attr("itemzengpin_id");


            $("#txtItemtdFinance_username").val(itemzengpin_title);
            $("#txtItemtdFinance_Id").val(itemzengpin_id);
        });
    });
    
    function multi()
    {
    	var multi_amount = $("#txtItemtdFinance_id").val();
    	var multi_price = $("#txtItemtdFinance_realName").val();
    	
    	var reg = new RegExp("^[0-9]+(.[0-9]{2})?$"); 
    	var reg2 = new RegExp("^[0-9]+(.[0-9]{2})?$");   
    	 
		if(reg2.test(multi_amount) && reg.test(multi_price)){  
    		var multi_total =  multi_amount*multi_price;
    		$("#txtItemtdFinance_money").val(multi_total);
    	}
    	else{console.log('111')};
    }
</script>
</head>

<body>
<div class="div-content">
    <dl>
      <dt>用户名<b style="color:red;">*</b></dt>
      <dd>
        <input type="text" id="txtItemtdFinance_username" class="input normal">
        <span class="Validform_checktip">*</span>
      </dd>
    </dl>
    <dl>
      <dt>ID<b style="color:red;">*</b></dt>
      <dd>
        <input type="text" id="txtItemtdFinance_id" class="input normal" onchange="javascript:multi();"> 
        <span class="Validform_checktip">*</span>
      </dd>
    </dl>
    <dl>
      <dt>姓名<b style="color:red;">*</b></dt>
      <dd>
        <input type="text" id="txtItemtdFinance_realName" class="input normal" onchange="javascript:multi();"> 元
        <span class="Validform_checktip">*</span>
      </dd>
    </dl>
    <dl>
      <dt>金额<b style="color:red;">*</b></dt>
      <dd>
        <input type="text" id="txtItemtdFinance_money" class="input normal"> 元
        <span class="Validform_checktip">*</span>
      </dd>
    </dl>
</div>

</body>
</html>