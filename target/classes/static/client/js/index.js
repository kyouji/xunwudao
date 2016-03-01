/* ! jQuery v1.0 2016-1-14 */
// writer shoko

$(document).ready(function(){
  more();
  tab();
  // touchCommand();
});

// 首页底部更多按钮
function more(){
  var onOff = true;
  $("#btn-inft-more").click(function(){
    if(onOff){
      $(this).next(".hide-nav").show();
    }else{
      $(this).next(".hide-nav").hide();
    }
    onOff = !onOff;
  });
};

// 套餐详情
function tab(){
  $('#tab-ul').on('click','a',function(){
    var $self = $(this);//当前a标签
    var $active = $self.closest('li');//当前点击li
    var index = $active.prevAll('li').length;//当前索引

  $active.addClass('active').siblings('li').removeClass('active');
    $('#tab-ol').find('>li')[index==-1?'show':'hide']().eq(index).show();
  });
};



/*

// 我的收藏
window.onload = function (){
  $(".btn-backspace").height($(".lessons").height()+1+"px");
};

function touchCommand(){
  $(".swipe-collect .swipe").on('touchmove', function(ev){
    ev.preventDefault(); // 阻止冒泡事件
  });
  // 向左滑动
  $(".swipe-collect .swipe").on('swipeleft', function(ev){
    $(this).parent().addClass("selected");
    // $(".btn-backspace").css("right","0");
  });
  // 向右滑动
  $(".swipe-collect .swipe").on('swiperight', function(ev){
    $(this).parent().removeClass("selected");
    // $(".btn-backspace").css("right","-80px");
  });
  // 点击删除
  $(".btn-backspace").click(function(){
    $(this).parent().remove();
  });
};

*/
