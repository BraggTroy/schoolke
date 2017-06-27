
getHeader();
getFooter();
login_require();

setTimeout(function(){$(".ke-header-main").remove()},5000);

$(function(){
    var getId = searchString("id").id || 0;

    if(window.user == undefined || window.user == null){
       /* $('.edit-btn-box').remove();
        $('.done-goods').remove();*/
       console.log("无用户");
        window.noUser = true;
        window.user = {};
        window.user.id = getId;
    }else{
        if(window.user.id != getId){
            window.otherUser = true;
        }
    }

    window.scope = {};
    window.scope.current = {};
    window.scope.down = {};
    var init = function(){
        var userId;
        if(window.user == undefined || window.user.id != getId){
            userId = getId;
        }else{
            userId = window.user.id;
        }

        mask();
        ajax_get("getUsersGoodsLost",{"ID":userId},function(res){
            var goodsNum = res.on_length + res.down_length;
            window.scope.on_lenght = res.on_length;
            window.scope.down_lenght = res.down_length;

            res.user = JSON.parse(res.user);
            res.user.goodsNum = goodsNum;
            if(!res.user.header){
                console.log(1)
                res.user.header = "none_head.gif"
            }
            init_userInfo(res.user);

            // 默认初始化显示在售商品信息
            var goods_list = {} , down_list = {};
            window.scope.current.list = goods_list.list = JSON.parse(res.on_sell);
            window.scope.down.list = down_list.list = JSON.parse(res.down_sell);

            show_goodsOn(goods_list,res.on_length);
            if(window.noUser || window.otherUser){
                $('.edit,.done,.on').remove();
            }
            unmask();
            bind_event();
        },"json");
    };

    var init_userInfo = function(res){
        var template = Handlebars.compile($('#info-table-template').html());
        $(".info-table-box").html(template(res));
        var face_template = Handlebars.compile($('#face-info-template').html());
        $(".personal-face").html(face_template(res));
        if(window.noUser || window.otherUser){
            console.log(window.noUser);
            console.log(window.otherUser);
            $('.p_').text(res.userName)
            $(".info-opt").children().remove();
            $('.edit-btn-box').remove();
            $('.done-goods').remove();
        }
    };

    // 显示在售商品列表
    var show_goodsOn = function(res , num){
        if(res.list.length <= 0){
            $(".goods-list-box").html("<div class='no-res'>暂无记录</div>");
            $(".opt , .page-box").html("");
        }else {
            var template = Handlebars.compile($('#goods-list-template').html());
            $(".goods-list-box").html(template(res));
            operation(num , "show_OPT")
        }
    };
    // 显示下架商品列表
    var show_goodsDown = function(res,num){
        if(res.list.length <= 0){
            $(".goods-list-box").html("<div class='no-res'>暂无记录</div>");
            $(".opt , .page-box").html("");
        }else{
            var template = Handlebars.compile($("#down-list-template").html());
            $(".goods-list-box").html(template(res));
            operation(num , "_OPT")
        }
    };

    // 显示分页和操作标签
    var operation = function (num , show) {
        var down_all = $('<a href="javascript:;" class="btn selected-done">批量下架</a>');
        if(show == "show_OPT"){
            if(!window.noUser && !window.otherUser){
                $('.opt').html(down_all);
            }
        }
        else if(show == "_OPT"){
            $('.opt').html("");
        }

        // 批量下架点击事件
        down_all.on('click',function(){
            var items = $(".user-goods-list tbody input[type=checkbox]:checked");
            var checked_items = [];
            for(var i=0;i<items.length;i++){
                checked_items.push($(items[i]).data('value'));
            }
            if(checked_items.length == 0){
                kealert({title:'提示',innerHTML:'请至少选择一条商品数据'});
                return;
            }
            keConfirm({
                title:'提示',
                innerHTML:'确定要将这'+ checked_items.length +'件商品下架吗',
                confirm_hide: true,
                before_show:function () {}
            },function(){
                // 下架
                down_goods(checked_items);
                return true;
            })
        })
    };

    //下架操作
    var down_goods = function(goods_items){
        var lth = goods_items.length;
        goods_items = goods_items.join(",");
        ajax_post("downThoseGoods",{'IDS':goods_items,"TYPE":"DOWN"},function(res){
            var bl = true;
            for(var i =0;i<res.list.length;i++){
                if(res.list[i] == 0){
                    bl = false;
                }
            }
            if(res.list.length == lth && bl){
                keConfirm({
                    'title':'提示',
                    'innerHTML':'下架成功',
                    confirm_hide:true,
                    before_show:function(){
                        $('.popup-btn .close').remove();
                    }
                },function(){
                    init();
                    return true;
                })
            }else{
                kealert({title:'提示',innerHTML:'操作过程异常，请重试'})
                init();
            }
        },"json");
    }

    // 上架操作
    var on_goods = function(goods_items ,dom){
        goods_items = goods_items.join(",");
        mask();
        ajax_post("downThoseGoods",{'IDS':goods_items,"TYPE":"ON","TIME":getTime()},function(res){
            unmask();
            if(res.list[0] == 1){
                for(var i=0;i<window.scope.down.list.length;i++){
                    if(goods_items == window.scope.down.list[i].id){
                        window.scope.current.list.splice(0,0,window.scope.down.list[i]);
                        window.scope.down.list.splice(i,1);
                    }
                }
                $(dom).parent().parent().remove();
                kealert({
                    'title':'提示',
                    'innerHTML':'上架成功'});
            }else {
                kealert({title:'提示',innerHTML:'操作失败请重试'})
            }
        },"json")
    }


    var bind_event = function(){

        // 点击切换数据表tab
        $(".title-box>div").on('click',function(){
            $(this).siblings().removeClass('active');
            $(this).addClass('active');
            if($(this).hasClass('current-goods')){
                show_goodsOn(window.scope.current,window.scope.on_lenght);
            }
            if($(this).hasClass('done-goods')){
                show_goodsDown(window.scope.down , window.scope.down_lenght);
            }

            down_on(); // 部分元素页面加载时不存在，所以需要不断地绑定事件
        });
        var down_on = function(){
            // 点击行内下架
            $(".done").on('click',function () {
                var goods_list = [];
                goods_list.push($(this).data('value'));
                keConfirm({
                    title:'提示',
                    innerHTML:'确定下架该商品吗',
                    confirm_hide:true,
                    before_show:function(){
                    }
                },function(){
                    down_goods(goods_list)
                    return true;
                })
            });

            // 点击重新上架
            $(".on").on('click',function () {
                var goods_list = [];
                var dom = $(this);
                goods_list.push($(this).data('value'));
                keConfirm({
                    title:'提示',
                    innerHTML:'确定重新上架该商品吗',
                    confirm_hide:true,
                    before_show:function(){
                    }
                },function(){
                    on_goods(goods_list , dom);
                    return true;
                })
            })
        };
        down_on();
    };

    // 对页面已存在元素添加事件
    var bind = function(){
        //编辑
        $(".edit-btn").on('click',function(){
            $(this).siblings().css('display','block');
            $(this).css('display','none');
            $(".info-table input").removeAttr('disabled').focus(function(){
                $(this).parent().css({'border-bottom-color':'#0066ff'})
            }).blur(function(){
                $(this).parent().css({'border-bottom-color':'#ebebeb'})
            })
            $("#userName").focus();


            // 头像
            var change_head_btn = $('<span id="update-face">更换头像</span>');
            $(".img-box").append(change_head_btn);
            change_head_btn.on('click',function(){
                var face_input = $("#face-input");
                face_input.click();
                face_input.on('change',function(){
                    // 当前对象
                    var file = this.files[0];
                    var fileReader = new FileReader();
                    // 监听
                    fileReader.onload = function(e){
                        if(file.size/1024/1024 >= 5){
                            kealert({title:'提示',innerHTML:"上传头像不能大于5Mb，请重新选择图片"})
                            return;
                        }
                        $(".user-face-img").attr('src',e.target.result);
                    }
                    // 读取
                    try{
                        fileReader.readAsDataURL(file);
                    }catch (Exception){}
                })

            });

        })

        //保存
        $(".save-btn").on('click',function(){
            // var rs  = get_value("personal-info");
            var formDom = document.getElementById("userHeader");
            var formData = new FormData(formDom);

            var req = new XMLHttpRequest();
            req.open("POST", "saveUserInfo");
            //请求完成
            req.onreadystatechange = function(){
                if(this.status === 200 && this.readyState === 4){
                    var res = JSON.parse(this.responseText);

                    setCookie("ke_user",JSON.stringify(res.user));
                    $(".save-btn").css('display','none');
                    $(".edit-btn").css('display','block');
                    $(".info-table input").attr('disabled',true);
                    kealert({title:'提示',innerHTML:'资料保存成功'})
                }
            }
            //将form数据发送出去
            req.send(formData);
            //避免内存泄漏
            req = null;

        })

    }

    init();
    bind();
})
