/**
 * Created by Bragg Troy on 2017/2/17.
 */
/*
handlebars 使用基本步骤:
var template = Handlebars.compile(ES.get('#confirm-account-template').html())
$("selector").html(template(res))

 */

// 页面中各种大小组件
/*var divObj = document.createElement("script");
divObj.src="js/common/js/jquery.min.js";
var first=document.querySelector("link")[0];//得到页面的第一个元素
document.head.insertBefore(divObj,first);*/
/*var script=document.createElement("script");
script.type="text/javascript";
script.src="jquery.js";
document.getElementsByTagName('head')[0].appendChild(script);*/

document.write('<script src="../js/common/js/jquery.min.js"></script>');
document.write('<link rel="icon" href="../images/keicon.ico">');
document.write('<script src="../js/common/js/handlebars.js"></script>');
// document.write('<script src="../js/common/js/scroll.js"></script>');


/***** 为banner-title添加分割线*****/
var bannerTitle = function (select) {
    var el = $("."+ select +"");
    el.wrap("<div style='text-align: center'></div>")
    el.before("<div class='banner-line-top' style='text-align: right;'><div class='banner-line'></div></div>")
    el.after("<div class='banner-line-top' style='text-align: left;'><div class='banner-line'></div></div>");
};
// ajax方法封装
var ajax_get = function (url,param,callback,datatype) {
    param = param || {};
    var data_type = datatype || 'text';
    $.ajax({
        type: 'GET',
        url: url,
        cache: false,
        dataType: data_type,
        data: param,
        success: function (res) {
            if (res.message) {
                keConfirm({
                    title:'提示',
                    innerHTML:res.message,
                    confirm_hide:false,
                    before_show:function(){}
                },
                function(){})
                // 如果有返回的错误信息，则弹窗提示
            } else {
                callback(res)
            }
        },
        error: function(res){
            errorUrl();

        }
    })
}
var ajax_post = function(url,param,callback,datatype){
    param = param || {};
    var data_type = datatype || 'text';
    $.ajax({
        type: 'POST',
        url: url,
        dataType: data_type,
        data: param,
        success: function (res) {
            if (res.message) {
                keConfirm({
                        title:'提示',
                        innerHTML:res.message,
                        confirm_hide:false,
                        before_show:function(){}
                    },
                    function(){})
                // 如果有返回的错误信息，则弹窗提示
            } else {
                callback(res)
            }
        },
        error: function(res){
            errorUrl();
        }
    })
}
// 错误的请求
var errorUrl = function () {
    var $error = '<div id="errorUrl" style="width: 400px;margin: 80px auto;">' +
        '<p><a href="index.html"><img src="images/logo-txt.png" alt="校园客"></a></p>' +
        '<p>抱歉！您访问的资源不存在！</p>' +
        '<p>请确认您输入的网址是否正确，如果问题持续存在，请发邮件至schoolke@qq.com与我们联系。</p>' +
        '<p><a href="index.html" style="text-decoration:underline;">返回校园客首页</a></p>' +
        '</div>'
    $('.container').html($error);
}

// 引入页面header部分，但不包括菜单
var getHeader = function () {
    var header_template;
    ajax_get('../include/header.html',{},function(data){
            header_template = $(data)[5].innerHTML;
            $("header").html(header_template);

            getCurrentUser();
            $("#exit").on("click",function(){
                login_out();
            })
        }
    );
}

// 引入页面中菜单部分
var getNav = function () {
    var nav_template;
    ajax_get("../include/header.html",{},function(data){
        nav_template = $(data)[7].innerHTML;
        // console.log(nav_template);
        window.classifyHtml = nav_template;
        $(".nav-box").html(nav_template);

        // 启动搜索事件
        searchGoods();
    })
}

// 引入footer部分
var getFooter = function() {
    var footer_template;
    ajax_get("../include/footer.html",{},function(data){
        // console.log($(data));
        footer_template = $(data)[7].innerHTML;
        $("footer").html(footer_template);
        addFixed();
    });

}

/*获取元素*/
var get_element = function(elem){
    // elem表示元素的类名或ID名
    var $class = $("."+elem+"");
    var $id = $("#"+elem+"");
    if($class.length == 0){
        return $id;
    }else{
        return $class;
    }
}

// loading加载动画 ，config表示需要调用的元素
var loading = function(config){
    var $dom = get_element(config);
    this.load = function(){
        var loadbox = $('<div class="loadbox" style="width: 100%;height: 100%;position: absolute;z-index: 900; background: rgba(0,0,0,.1);"></div>')
        var $load = $('<div class="loading"></div>');
        var $c = $('<div class="t-circle half-circle"><span class="eye"></span></div>'+
            '<div class="b-circle half-circle"></div>'+
            '<div class="s-circle s1"></div>'+
            '<div class="s-circle s2"></div>'+
            '<div class="s-circle s3"></div>'+
            '<div class="s-circle s4"></div>')
        $load.append($c);
        loadbox.append($load)
        return loadbox;
    }
    $dom.append(load());
    $dom.css({'position':'relative','height':'90px'});
}
// 去除加载动画
var loaded = function(config){
    var $dom = $('.'+config+'') || $('#'+config +'');
    $dom.find('.loadbox').remove();
    $dom.css({'position':'static','height':'auto'});
};
var mask = function(){
    var bg = $('<div class="mask-bg"></div>');
    bg.css({'position':'fixed','top':'0','bottom':'0','width':'100%','background':'rgba(193,193,194, 0.5)','z-index':'999'});
    var load = $('<div style="width: 100%;text-align: center;position:absolute;top:200px;"><img src="../../images/loading.gif"></div>')
    bg.append(load);
    $('body').append(bg);
};
var unmask = function(){
    var mask_ = $('.mask-bg');
    if(mask_.length > 0){
        mask_.remove();
    }
};
var load = function (el) {
    var dom = get_element(el);
    var width = dom.width();
    var height = dom.height();
    var mask = $('<div class="load-dom" style="background: rgba(100,100,100,0.5);"><img src="../../../images/loading2.gif"></div>');
    mask.css({'width':width,'height':height,'display':'flex','justify-content':'center','align-items':'center','position':'absolute','top':'0'});
    if(dom.find('.load-dom').length <= 0){
        dom.append(mask);
    }
}
var unload = function (el) {
    var dom = get_element(el);
    dom.find('.load-dom').remove();
}

//将查询字符串格式化
var searchString = function(config){
    var searchString = window.location.search,
        arr,
        searchArr = [];
    searchString = searchString.split('?')[1];

    if(searchString == undefined || searchString.length == 0 || searchString == ""){
        return undefined;
    }
    arr = searchString.split('&');
    for(var i=0;i<arr.length;i++){
        var str = {};
        str[arr[i].split('=')[0]] = arr[i].split('=')[1];

        if(config){
            if(str.hasOwnProperty(config)){
                return str; //如果需要某特定的查询值，则直接返回该对象
            }
        }else{
            searchArr.push(str);
            return searchArr;
        }
    }

}

// 获取当前系统时间：年-月-日
var getTime = function(){
    function format(time) {
            return time < 10 ? '0'+time : time;
    }
    var time = new Date();
    var y = time.getFullYear();
    var m = time.getMonth()+1;
    var d = time.getDate();
    // var h = time.getHours();
    // var mm = time.getMinutes();
    // var s = time.getSeconds();
    return y +'-'+ format(m) +'-'+ format(d);
};

function get_el(el) {
    return document.getElementById(el);
}
//自动居中对话框
function autoCenter(el){
    var bodyW = $(window).width();
    var bodyH = $(window).height();
    var elW = el.width();
    var elH = el.height();
    $(el).css({"left":(bodyW-elW)/2 + 'px',"top":(bodyH-elH)/2 + 'px'});
};
function dialog_move() {
    var $dialog = $('.popup');
    // var $mask = get_element(bg);
    var $move = $('.popup-title');

    if(document.attachEvent) {//ie的事件监听，拖拽div时禁止选中内容，firefox与chrome已在css中设置过-moz-user-select: none; -webkit-user-select: none;
        get_el('dialog').attachEvent('onselectstart', function() {
            return false;
        });
    }
    //声明需要用到的变量
    var mx = 0,my = 0;      //鼠标x、y轴坐标（相对于left，top）
    var dx = 0,dy = 0;      //对话框坐标（同上）
    var isDraging = false;      //不可拖动

    //鼠标按下
    $move.mousedown(function(e){
        e = e || window.event;
        mx = e.pageX;     //点击时鼠标X坐标
        my = e.pageY;     //点击时鼠标Y坐标
        dx = $dialog.offset().left;
        dy = $dialog.offset().top;
        isDraging = true;      //标记对话框可拖动
    });

    //鼠标移动更新窗口位置
    $(document).mousemove(function(e){

        if(isDraging){        //判断对话框能否拖动
            var e = e || window.event;
            var x = e.pageX;      //移动时鼠标X坐标
            var y = e.pageY;      //移动时鼠标Y坐标

            var moveX = dx + x - mx;      //移动后对话框新的left值
            var moveY = dy + y - my;      //移动后对话框新的top值
            //设置拖动范围
            var pageW = $(window).width();
            // var pageH = $(window).height();
            var dialogW = $dialog.width();
            // var dialogH = $dialog.height();
            var maxX = pageW - dialogW;       //X轴可拖动最大值
            // var maxY = pageH - dialogH;       //Y轴可拖动最大值
            moveX = Math.min(Math.max(0,moveX),maxX);     //X轴可拖动范围
            // moveY = Math.min(Math.max(0,moveY),maxY);     //Y轴可拖动范围
            //重新设置对话框的left、top
            $dialog.css({"left":moveX + 'px',"top":moveY - $(window).scrollTop() + 'px'});
        };
    });
    //鼠标离开
    $move.mouseup(function(){
        isDraging = false;
    });

}

// 需要点确定关闭的弹窗
var keConfirm = function(config,success){
    window.client_height = document.documentElement.clientHeight;
    window.client_width = document.documentElement.clientWidth;
    var defaults = {
        title:'提示',
        innerHTML:'提示',
        confirm_hide: true,
        before_show: function(){}
    }
    config = $.extend(defaults,config);
    // config = config || {};

    var $popup = $("<div class='popup'></div>");
    var $popup_title = $('<div class="popup-title" id="dialog"><span class="title">'+ config.title +'</span></div>')
    var $close_x = $('<span class="close"></span>');
        $close_x.on('click',function(){
            bg.remove();
        });
    $popup_title.append($close_x);
    $popup.append($popup_title);

    var $popup_content = $('<div class="popup-content">'+ config.innerHTML +'</div>');
    $popup.append($popup_content);

    var $popup_btn = $('<div class="popup-btn"></div>')
    var $close;

    if(config.confirm_hide){
        $close = $('<a href="javascript:;" class="btn close">取消</a>');
    }else{
        $close = $('<a href="javascript:;" class="btn close">确定</a>');
    }
    $close.on('click',function () {
        bg.remove();
    });
    $popup_btn.append($close)

    if(config.confirm_hide){
        var $confirm = $('<a href="javascript:;" class="btn confirm">确定</a>');
        $confirm.on('click',function () {
            if(success()){
                bg.remove();
            }
        });
        $popup_btn.append($confirm)
    }

    $popup.append($popup_btn);

    var bg = $('<div id="bg" style="position: fixed;width: 100%; top:0;bottom: 0;background: rgba(0,0,0,.5);z-index: 100001;"></div>');
    $('body').append(bg.append($popup));

    if (config.before_show) {
        config.before_show();
    }

    /*var height = $popup.height();
    var width = $popup.width();
    if(width>900){
        width = 900;
    }
    if(height>window.client_height){
        height = window.client_height - 50;
    }

    $popup.css({'position':'fixed','top':window.client_height/2 - height/2 - 50 +'px','z-index':'9999',
        'left':window.client_width/2 - width/2+'px','min-height':'140px','max-height':window.client_height - 100+'px','max-width':width})
    */
    $popup_content.css({'overflow-y':'auto','max-height':window.client_height-200+'px'});
    $popup.css({'max-height':window.client_height-100+'px'});
    autoCenter($popup);

    dialog_move();
};

// 自定义alert
var kealert = function(config){
    keConfirm({
        'title':config.title,
        'innerHTML':config.innerHTML,
        'confirm_hide':false,
        'before_show':function(){}
    },function(){
        return true;
    })
};


// 写入cookie
var setCookie = function(NameOfCookie, value, expiredays) {
    var exdate = new Date()
    exdate.setDate(exdate.getDate() + expiredays)
    document.cookie = NameOfCookie + "=" + escape(value) + ((expiredays == null) ? "" : "; expires=" + exdate.toGMTString())
}
// 读取cookie
var getCookie = function(NameOfCookie) {
    var c_start,c_end;
    if (document.cookie.length > 0) {
        c_start = document.cookie.indexOf(NameOfCookie + "=")
        if (c_start != -1) {
            c_start = c_start + NameOfCookie.length + 1
            c_end = document.cookie.indexOf(";", c_start)
            if (c_end == -1) c_end = document.cookie.length
            return unescape(document.cookie.substring(c_start, c_end))
        }
    }
    return null;
};
// 删除cookie
var delCookie = function(NameOfCookie) {
    var exp = new Date();
    exp.setTime(exp.getTime() + (-1 * 24 * 60 * 60 * 1000));
    var cval = getCookie(NameOfCookie);
    document.cookie = NameOfCookie + "=" + cval + "; expires=" + exp.toGMTString();
};

// 获取当前的登录账户
var getCurrentUser = function(){
    window.user = JSON.parse(getCookie("ke_user"));
    if(window.user != null || window.user != undefined && window.user.userName.length>0){
        $(".ke-userOpt").html("<label>欢迎来到校园客<a href='#'class='ke-login'>"+ window.user.userName +"</a> </label>");
        $(".ke-userOpt .ke-login").attr('href','personal.html?id='+ window.user.id+'')
        $(".ke-personal").attr('href','personal.html?id='+ window.user.id+'');
    }else{
        $(".per").css('display','none');
    }
    return window.user;
};
//退出登录
var login_out = function(){
    delCookie("ke_user");
    window.location.href = "index.html";
}
// 要求登录
var login_require = function(){
    window.user = getCurrentUser();
    var isPersonal = window.location.pathname.split("/").pop();
    if(window.user == null || window.user == undefined){
        keConfirm({
            'title':'提示',
            'innerHTML':"您尚未登录，请登录",
            'confirm_hide':true,
            'before_show':function(){
                $(document).on('click','.popup .close',function(){
                    window.location.href = "index.html";
                })
            }
        },function(){
            var url = "'"+window.location.href+"'";
            window.location.href="login.html?backURL="+url;
        });
        return false;
    }else{
        return true;
    }
};

// 自动消失的弹窗
var hide_alert = function (config) {
    var $bg;
    keConfirm({
        title: config.title,
        innerHTML: config.innerHTML,
        confirm_hide: false,
        before_show: function(){
            $bg = $('.popup').parent();
            $bg.css('background','transparent');
            $bg.on('click',function () {
                $bg.hide(800);
            })
        }
    },function () {
    });
    setTimeout(function(){
        $bg.remove();
    },800);
}

/*获取某容器内部的所有表单元素的value*/
var get_value = function(container){
    var el = get_element(container);
    var items = el.find('input,select,textarea');
    if(items.length == 0){
        if(el.prop('name') != undefined || el.prop('name') != ""){
            var k = el.attr('name');
            var v = el.val();
            var rs = {};
            rs[k] = v;
            return rs;
        }
        else{
            return "";
        }
    }
    var config = {};
    for(var i=0;i<items.length;i++){
        var key = $(items[i]).attr('name');
        var value = $(items[i]).val();
        config[key] = value;
    }
    return config;
};


//在getFooter()中调用。
//在页面添加右下角全局操作和购物车框架
var addFixed = function(){

    var isGoods = searchString("goodsId");
    // 右下角全局操作
    var $fixed_right = $('<div class="fixed-right"></div>');
    var $goodsCart = $('<div class="goodsCart"></div>');
    var $chart = $('<div class="chart"></div>');
    var $code = $('<div class="code"></div>');
    var $top = $('<div class="top"></div>');

    $fixed_right.append($goodsCart);
    if(isGoods){
        $fixed_right.append($chart)
    }
    $fixed_right.append($code);
    $fixed_right.append($top);
    $top.css("display","none")

    var $imgcode = $('<div class="imgCode"><span>扫码关注</span><img src="../../../images/qrcode.png"></div>');
    $code.append($imgcode)

    $('body').append($fixed_right);

    // 回到顶部
    $(window).scroll(function(){
        var sc = $(window).scrollTop();
        if(sc>200){
            $top.slideDown(400);
        }else{
            $top.slideUp(400);
        }
    })
    $top.click(function(){
        $('body,html').animate({scrollTop:0},400);
    })

    // 查看购物车
    $goodsCart.click(function () {
        if(!login_require()){
        }else{
            showMyCart();
        }
    })

    // 创建购物车容器
    var myCart = '<div class="myCart"></div>';
    var $cart = $('<div class="myCart-box">'+
        '<div class="closeCart">收<br>起</div>'+ myCart +
        '<div class="cart-bottom"><img src="images/search-logo-text.png"></div>'+
        '</div>');

    // 删除购物车项事件
    $cart.on('click','.delete-cart',function () {
        var wishId = $(this).data('value');
        deleteMyWish(wishId, this);
    });
    $('body').append($cart);

}

// 显示购物车
var showMyCart = function () {
    var myCart = $('.myCart')
    hideMyCart();
    // $('.myCart-box').removeClass("hide_to_right");
    // $('.closeCart').removeClass("hide_to_right2");
    // load('myCart');
    $('.myCart-box').animate({width:'320px'},400);
    $('.closeCart').animate({right:'291px'},400);
    // 获取数据
    initMyCart(myCart)
}

// 更新购物车数据
var　initMyCart = function ($myCart) {
    ajax_post("MyWishList",{'ID':window.user.id},function (res) {
        var arr = JSON.parse(res).wish;
        var cart = '';
        for(var i=0;i<arr.length;i++){
            cart += '<div class="cart-item">' +
            '<div class="cart-user-info"><a href="personal.html?id='+ arr[i].preGoods.user.id +'" target="_blank">'+
            '<img src="images/headerImages/'+ (arr[i].preGoods.user.header || "none_head.gif") +'">'+
            '<span>'+ arr[i].preGoods.user.userName +'</span>'+
            '</a>'+
            '</div>'+
            '<div class="cart-goods-info">'+
            '<div class="cart-img">'+
            '<a href="goodsDetail.html?goodsId='+ arr[i].preGoods.id +'&type='+ (arr[i].preGoods.state == 1 ? "ON" : "DOWN") +'" target="_blank">'+
            '<img src="images/goodsImages/'+ arr[i].preGoods.imageName +'">'+
            '</a>'+
            '</div>'+
            '<div class="cart-text">'+
            '<p class="cart-name">'+
            '<a href="goodsDetail.html?goodsId='+ arr[i].preGoods.id +'&type='+ (arr[i].preGoods.state == 1 ? "ON" : "DOWN") +'" target="_blank">'+
            arr[i].preGoods.goodsName +
            '</a>'+
            '</p>'+
            '<p class="scan-price">&yen;'+ arr[i].preGoods.schoolPrice +'</p>'+
            '<a href="javascript:void(0);" data-value="'+ arr[i].id +'" title="删除" class="delete-cart">X</a>'+
            '</div>'+
            '</div>'+
            '</div>';
        }

        if(cart == '' || cart.length <= 0){
            cart = '<div class="empty-cart">' +
                   '<img src="images/face_error.gif"><span>空空如也，快去逛逛吧！</span>'+
                   '</div>'
        }
        $myCart.html(cart);
    })
}

// 隐藏购物车
var hideMyCart = function () {
    var closeCart = document.getElementsByClassName('closeCart')[0];
    closeCart.onclick = function() {
        $('.myCart-box').animate({width:'0px'},400)
        $('.closeCart').animate({right:'-30px'},400)
    }
}

// 删除购物车内容
var deleteMyWish = function (wishId, dom) {
    ajax_post("deleteMyWish",{"ID":wishId},function (res) {
        if(res == 1){
            $(dom).parentsUntil('.myCart').slideUp(400,function () {
                $(dom).parentsUntil('.myCart').remove();
            });
        }else{
            kealert({title:'异常',innerHTML:'删除操作异常，请重试！'})
        }
    })
}

// 全局搜索
var searchGoods = function () {
    $("#ke-search").focus(function () {
        $(this).on('keydown',function (e) {
            if (e.keyCode == 13){
                $("#ke-submit").click();
            }
        })
    })
    $(document).on('click','#ke-submit',function () {
        var val = get_value("ke-form-left").keWords;
        if(val.length > 0){
            window.location.href = "search.html?keWords=" + val
        }
    })
}

// 分页
/*
config = {page:1,total:120,pageSize:50,}
 */
var paging = function (config , pageId) {
    var conf = config || {};
    var id = "#" + pageId || "#page-box";
    var el = $(id);
    conf.pageSize = conf.pageSize || 50;
    var pageNum = Math.floor(conf.total / conf.pageSize) + 1;

    var $li = '';
    for(var i=1; i<=pageNum; i++){
        $li += '<li class="item"><span>'+ i +'</span></li>'
    }

    var $ul = '<ul class="page-items">' +
        '<li class="item prev disabled"><span> &lt; </span><span>上一页</span></li>' +
         $li +
        '<li class="item next"><span>下一页</span><span> &gt; </span></li></ul>'

    var $total = '<div class="total">共'+ conf.total +'页，</div>'

    var $toPage = '<div class="to-page"><span>到第</span>' +
        '<input type="number" class="page-input" value="1" min="1" max="'+ pageNum +'">' +
        '<span>页</span><span class="page-submit">确定</span></div>'

    el.html($ul + $total + $toPage);
    $('.page-items .item:eq(1)').addClass('active');

    var page_event = function () {

    }



}