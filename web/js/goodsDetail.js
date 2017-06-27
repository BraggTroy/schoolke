

getHeader();
getNav();
getFooter();

window.onload = function(){
    setTimeout(function () {
        if($('.xmeet-chat-logo').css('display') == 'none'){
            clearInterval(function_id);
        }
    },5000)

};

$(function(){

        function_id = setInterval(function () {
            console.log(1);
            if($('.xmeet-chat-logo')[0]){
                $(".xmeet-chat-logo").css({'width':'0','height':'0','display':'none'})
                $(".xmeet-chat-logo img").css({'width':'0','height':'0','display':'none'})
            }
        },100)

    window.scope = {};
    var init = function(){

        var id = searchString("goodsId");
        var goodsState = searchString("type");

        //基础信息
        ajax_get("getGoodsInfo",id,function(res){
            if(res.images.length<=0){
                $('.students-notice-inner').html('');
                // loading('students-notice-inner');
                var $err = $('<p style="text-align: center; margin: 50px auto;">' +
                    '<img src="images/face_error.gif" style="vertical-align: middle;margin-right: 20px;">这件商品可能已经被吃了</p>' +
                    '<p style="text-align: center;"><a href="javascript:history.back();" style="text-decoration: underline;">返回上一页</a> </p>')
                $('.container').append($err);
                return;
            }
            window.scope = res.goods;
            document.title = res.goods.goodsName +" - 校园客";
            res.firstImage = (res.images)[0].name;
            window.goodsScope = res;
            window.goodsScope.imagesLength = res.images.length;
            var goods_template = Handlebars.compile($("#goods-detail-template").html());
            $(".goods-detail-container").html(goods_template(res));
            $('.images-small:eq(0)').addClass('active');
            show_bigImages();

            // 状态判断
            if (res.goods.state == 0){
                $("#add-to-cart").addClass('btn-gray');
            }
        },"json")
        getMessage();
    }
    var getMessage = function () {
        ajax_get("getMessages",{'TYPE':'FOR_GOODS','ID':searchString('goodsId').goodsId},function (res) {
            if(res.msgList.length == 0){
                $('.msg-content').html("<div style='line-height:56px;text-align:center;'>暂无留言，快来抢个沙发吧！</div>")
            }else{
                var msg_template = Handlebars.compile($("#msg-list-template").html());
                $('.msg-content').html(msg_template(res));
            }

        },"json")
    }

    var bind_event = function(){

        // 小图片的效果
        $(document).on('click mouseover',".images-small-box .images-small",function(){
            $(this).siblings().removeClass('active');
            $(this).addClass('active');
            $(".image-big").attr('src',$(this).attr('src'));
        })

        /*$(document).on('mouseover mousemove',".image-big",function(e){
            var $mouseBox = $("<div class='mouse-box'></div>");
            drawImages($mouseBox,e,this);
            // drawImages($mouseBox,e,this);
        });
        $(document).on('mouseout',".image-big",function(){
            $(".image-canvas-box").css('display','none');
        });*/
        // 点击查看大图
        $(document).on('click',".image-big",function(){
            $('.show_bigImages_bg').show();
        });
        $(document).on('click','#closeShow',function () {
            $('.show_bigImages_bg').hide();
        });
        // 上一张
        $(document).on('click','.img_prev',function () {
            var idx = 0;
            var arr_img = $('.images_list li');
            var $active_img = $('.images_list li.active');
            var num = $active_img.index();
            if(num+1 == 1){
                idx = arr_img.length - 1;
            }else{
                idx = num - 1;
            }
            $(arr_img[num]).removeClass('active');
            $(arr_img[idx]).addClass('active');
            $('#currentInd').text(idx+1);
            $('#image_Preview').attr('src',$(arr_img[idx]).find('img').attr('src'))

        })
        // 下一张
        $(document).on('click','.img_next',function () {
            var idx = 0;
            var arr_img = $('.images_list li');
            var $active_img = $('.images_list li.active');
            var num = $active_img.index();
            if(num+1 == arr_img.length){
                idx = 0;
            }else{
                idx = num + 1;
            }
            $(arr_img[num]).removeClass('active');
            $(arr_img[idx]).addClass('active');
            $('#currentInd').text(idx+1);
            $('#image_Preview').attr('src',$(arr_img[idx]).find('img').attr('src'))
        })
        // 直接看
        $(document).on('click','.images_list li',function () {
            var arr_img = $('.images_list li');
            var src = $(this).find('img').attr('src');
            for(var i=0;i<arr_img.length;i++){
                if($(arr_img[i]).find('img').attr('src') == src){
                    $('#currentInd').text(i+1);
                }
            }
            $(this).siblings().removeClass('active');
            $(this).addClass('active');
            $('#image_Preview').attr('src',src)

        })


        //点击我要留言
        $(document).on('click','.sendMessage-btn',function(){
            if(window.user == undefined){
                keConfirm({
                    title:'提示',
                    innerHTML:'登录后才能发表留言，是否立即登录',
                    confirm_hide:true
                },function(){
                    var url = "'"+window.location.href+"'";
                    window.location.href="login.html?backURL="+url;
                })
                return;
            }
            var tempalte = Handlebars.compile($("#publish-msg-template").html());
            var html_ = tempalte(window.scope);
            keConfirm({
                title:'发表商品留言',
                innerHTML:html_,
                confirm_hide:true,
                before_show:function(){
                }
            },function () {
                var content = $("#msg").val();
                var goodsId = window.scope.id;
                var userId = window.user.id;
                if(content.length > 250){
                    var $long_text = $('<p class="long_text"><i class="warning"></i>内容超过限制，最多250字</p>');
                    if($('.long_text').length == 0){
                        $('.warnings').append($long_text);
                    }
                    return false;
                }else if(content.length <=0){
                    var $null_text = $('<p class="null_text"><i class="warning"></i>内容不能为空</p>');
                    if($('.null_text').length == 0){
                        $('.warnings').append($null_text);
                    }
                    return false;
                }
                else{
                    ajax_post("publishMsg",{'CONTENT':content,'GOODSID':goodsId,'USERID':userId},function(res){
                        if(res == 1){
                            //刷新留言
                            $('.popup').parent().remove();
                            kealert({title:'提示',innerHTML:'留言发表成功'});
                            setTimeout(function () {
                                $('.popup').parent().hide(150);
                            },500)
                            getMessage();
                        }else{
                            kealert({title:'提示',innerHTML:'发表失败，请重试！'});
                        }
                    },"text");

                }

            })
        })

        $('.xmeet-chat-logo').on('click',function(){

            setTimeout(function() {
                $('.nickName').attr('disabled', 'true').val(window.user.userName);
            },1000);
        });
        $(document).on('click','.chart',function () {
            $('.xmeet-chat-logo img').click();
        })

        // 点击加入意愿单
        $(document).on('click','#add-to-cart', function () {
            if($(this).hasClass('btn-gray')){
                hide_alert({title:'提示',innerHTML:'该商品已经下架'});
            }else{
                /*var config = {};
                 config.origin = $("#add-to-cart");
                 config.target = $(".goodsCart");
                 config.element = $("#cart_move");*/
                if(window.user == undefined){
                    keConfirm({
                        title:'提示',
                        innerHTML:'您尚未登录，请先登录！',
                        confirm_hide:true
                    },function(){
                        var url = "'"+window.location.href+"'";
                        window.location.href="login.html?backURL="+url;
                    })
                    return false;
                }

            function addToCartMove() {
                var el = $('<div class="cart_move"></div>');
                el.css({width:'36px','height':'36px','background':'url("../images/cartetcicon.png") -42px 0px','background-color':'#fff','position':'absolute','z-index':'1000000'})
                $(document.body).append(el);
                new parabola({
                    origin: '.add-btn',
                    target: '.goodsCart',
                    element: '.cart_move',
                    a: 0.0015,
                    time: 1000,
                    callback: function(el){
                        el.remove();
                    }
                }).move();
            }

                ajax_post("addToWish",{"GOODSID":window.scope.id, "USER":window.user.id},function (res) {
                    if(res.exist){
                        hide_alert({title:'提示',innerHTML:'您已添加过该商品！'})
                    }
                    else if(!res.exist && res.add){
                        addToCartMove();

                    }
                    else if(!res.exist && !res.add){
                        hide_alert({title:'提示',innerHTML:'加入异常，请重试！'})
                    }

                },"json")
            }
        })

    }

   var show_bigImages = function(){
        var show_template = Handlebars.compile($("#show-bigImages-template").html());
        var box = show_template(window.goodsScope);
        $('body').append(box);
       $('.show_bigImages_bg').css({height:$(document).height()});
       $('.images_list li:nth-child(1)').addClass('active');

   };

    init();
    bind_event();





})

var parabola = function(config){
    var b = 0,
        INTERVAL = 15,
        timer = null,
        x1,y1,x2,y2,originx,originy,diffx,diffy;

    this.config = config || {};
    // 起点
    this.origin = $(this.config.origin)||null;
    // 终点
    this.target = $(this.config.target)||null;
    // 运动的元素
    this.element = $(this.config.element)||null;
    // 曲线弧度
    this.a = this.config.a || 0.004;
    // 运动时间(ms)
    this.time = this.config.time || 1000;

    // 添加到购物车的动画效果
    this.inition = function(){
        x1 = this.origin.offset().left;
        y1 = this.origin.offset().top;
        x2 = this.target.offset().left;
        y2 = this.target.offset().top;
        originx = x1;
        originy = y1;
        diffx = x2-x1;
        diffy = y2-y1,
            speedx = diffx/this.time;

        // 已知a, 根据抛物线函数 y = a*x*x + b*x + c 将抛物线起点平移到坐标原点[0, 0]，终点随之平移，那么抛物线经过原点[0, 0] 得出c = 0;
        // 终点平移后得出：y2-y1 = a*(x2 - x1)*(x2 - x1) + b*(x2 - x1)
        // 即 diffy = a*diffx*diffx + b*diffx;
        // 可求出常数b的值
        b = (diffy - this.a*diffx*diffx)/diffx;
        this.element.css({
            left: x1,
            top: y1
        })
        return this;
    }

    // 确定动画方式
    this.moveStyle = function(){
        var moveStyle = 'position',
            testDiv = document.createElement('div');
        if('placeholder' in testDiv){
            ['','ms','moz','webkit'].forEach(function(pre){
                var transform = pre + (pre ? 'T' : 't') + 'ransform';
                if(transform in testDiv.style){
                    moveStyle = transform;
                }
            })
        }
        return moveStyle;
    }

    this.move = function(){
        var start = new Date().getTime(),
            moveStyle = this.moveStyle(),
            _this = this;
        timer = setInterval(function(){
            if(new Date().getTime() - start > _this.time){
                clearInterval(timer);
                _this.element.css({
                    left: x2,
                    top: y2
                })
                typeof _this.config.callback === 'function' && _this.config.callback(_this.element);
                return;
            }
            x = speedx * (new Date().getTime() - start);
            y = _this.a*x*x + b*x;
            if(moveStyle === 'position'){
                _this.element.css({
                    left: x+originx,
                    top: y+originy
                })
            }else{
                if(window.requestAnimationFrame){
                    window.requestAnimationFrame(_this.element[0].style[moveStyle] = 'translate('+x+'px,'+y+'px)');
                }else{
                    _this.element[0].style[moveStyle] = 'translate('+x+'px,'+y+'px)';
                }
            }
        },INTERVAL)
        return this;
    }

    this.inition();
}