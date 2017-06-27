$(function () {
    // 重要：监听href变化
    $(window).on("hashchange", function() {
        reload();
    });

    var admin = {};
    window.adminscope = {};
    if(sessionStorage.admin == undefined){
        window.location.href = "m.login.html";
    }else{
        admin = JSON.parse(sessionStorage.admin);
        init_admin(admin.name);
    }
    //设置view的高度自适应
    var height = document.documentElement.clientHeight - 75;
    $("#view").css({height:height+'px','overflow-y':'auto'})

    //重新加载页面数据
    function reload() {
        var menu = $('.menu').find('a[href]');
        var link_menu,link_address;
        link_address = window.location.href.split('#')[1];
        for(var i=0; i<menu.length; i++){
            link_menu = $(menu[i]).attr('href').split('#')[1]
            if(link_menu == link_address){
                $(menu[i]).parent().click();
                break;
            }
        }


    }

    var bind_menu_event = function () {
        // 菜单的显示与收起
        $('.menu-opt').click(function () {
            $('.menu-box-inner').css('display','none')
            $('.menu-opt-open').css('display','block')
        });
        $('.menu-opt-open').click(function () {
            $('.menu-box-inner').css('display','block')
            $('.menu-opt-open').css('display','none')
        })

        // 菜单点击事件
        $('.menu').on('click',function () {

            if($(this).hasClass('title')){
            }else{

                $(this).siblings().removeClass('active');
                $(this).addClass('active')
                var link = $(this).find('a').attr('href').split('#')[1];

                // 根据不同的link获取不同的数据
                getResult(link);
            }
        })

    }
    // 根据不同的link获取不同的数据
    var getResult = function (link) {
        load('view');
        var template,url = '',
            para = {};
        switch (link){
            case 'recommend':
                url = 'recommendGoods';
                break;
            case 'goods':
                url = 'goodsByState';
                para = {'STATE':1};
                break;
            case 'down':
                url = 'goodsByState';
                para = {'STATE':0};
                break;
            case 'carousel':
                url = 'carousel';
                break;
            case 'user':
                url = 'user';
                break;

        }


        template = getView(link);

        ajax_get(url,para,function (res) {
            var result = {};
            // 数据预处理
            if(link != 'notice')
            res = JSON.parse(res);

            if(link == 'recommend'){
                for (var i=0; i<res.list.length; i++){
                    var list = res.list[i];
                    list.images = JSON.parse(list.images).slice(0, 3);
                }
                result = res;
            }
            if (link == 'down' || link == 'goods'){
                $.each(res,function (i, v) {
                    if (v.state == 3){
                        v.recommended = true;
                    }else{
                        v.recommended = false;
                    }
                })
                result.list = res;
            }
            if(link == 'carousel'){
                $.each(res,function (i, v) {
                    v.address = v.link;
                    v.link = v.link || 'javascript:void(0)'
                })
                result.list = res;
            }
            if(link == 'user'){
                $.each(res.list,function (i, v) {
                    if(!v.header){
                        v.header = "none_head.gif"
                    }
                })
                result = res;
                result.length = res.list.length;
            }


            window.adminscope[link] = res;
            // 显示数据
            init_template(template,result);
            // 显示后处理页面元素
            after_tempalte(link)

        });
    }

    // 页面事件处理
    var bind_event = function () {
        //退出
        $('.admin-exit').click(function () {
            sessionStorage.clear();
            location.href = 'm.login.html';
        });
        // 发布公告
        $(document).on('click','.notice_btn',function () {
            var name = $('#noticeName').val();
            var text = $('#editor-trigger').html();
            if(name.length <= 0){
                kealert({title:'提示',innerHTML:'请填写公告标题'});
            }else {
                ajax_post('publishNotice',{'NAME':name,'TEXT':text},function (res) {
                    var id = res;
                    keConfirm({
                        title: '提示',
                        innerHTML:'公告发布成功！',
                        confirm_hide: true,
                        before_show: function () {
                            $('.confirm').text("立即查看")
                        }
                    },function () {
                        window.open('notice.html?noticeId='+id,"_target");
                        return true;
                    })
                })
            }
        })

        // 下架
        $(document).on('click','.admin-down',function () {
            var this_ = $(this);
            var id = $(this).data('id')
            var name = $(this).data('value')
            keConfirm({innerHTML:'确定将该商品下架吗？'},function () {
                ajax_post('downThoseGoods',{'IDS':id,"TYPE":"DOWN"},function (res) {
                    var bl = true;
                    for(var i =0;i<res.list.length;i++){
                        if(res.list[i] == 0){
                            bl = false;
                        }
                    }
                    if(bl){
                        keConfirm({
                            'innerHTML':'下架成功',
                            before_show:function(){
                                $('.popup-btn .close').remove();
                            }
                        },function(){
                            this_.parentsUntil('.right-main').remove();
                            return true;
                        })
                    }
                },'json')
                return true;
            })
        })

        // 编辑轮播地址
        $(document).on('focus','.car-good',function () {
            var this_ = $(this);
            this_.on('keydown',function () {
                this_.parent().parent().parent().find('.car-confirm').css('display','inline-block')
                this_.parent().parent().parent().find('.car-check').css('display','none')
            })
        })

        // 保存轮播编辑
        $(document).on('click','.car-confirm',function () {
            var this_ = $(this);
            var ca_id = this_.data('value');
            var ca_link = this_.parent().parent().parent().find('.car-good').val();
            this_.text('保存中...')
            ajax_post('optionsCarousel',{'TYPE':'SAVE','ID':ca_id,'LINK':ca_link},function (res) {
                if(res == 1){
                    getResult('carousel')
                }
            })
        })

        // 删除轮播
        $(document).on('click','.car-delete',function () {
            var id = $(this).data('value');
            keConfirm({innerHTML:'确定删除该轮播广告吗？'},function () {
                ajax_post('optionsCarousel',{'TYPE':'DELETE','ID':id},function (res) {
                    if(res == 1){
                        getResult('carousel')

                    }
                })
                return true;
            })

        })

        // 编辑推荐
        $(document).on('click','.edit_rd',function () {
            var inner = $("#recommend-edit-template").html()
            var id = $(this).data('id');
            var o_index = $(this).data('index');
            var o_rd_text = '';
            $.each(window.adminscope.recommend.list,function (i, v) {
                if(v.info.id == id){
                    o_rd_text = v.info.recommendTitle;
                    return false;
                }
            })
            keConfirm({innerHTML:inner,before_show:function () {
                $('.rd-index').val(o_index)
                $('.rd-text').val(o_rd_text)
            }},function () {
                var new_index = $('.rd-index').val();
                var new_text = $('.rd-text').val()
                ajax_post('optionsRecommend',
                    {'TYPE':'EDIT','ID':id,'INDEX':new_index,'TEXT':new_text},
                    function (res) {
                    if(res == 1){
                        getResult('recommend');
                        hide_alert({innerHTML:'编辑成功！'})
                    }else{
                        kealert({innerHTML:'操作异常，请重试！'})
                    }
                })
                return true
            })

        })

        // 推荐
        $(document).on('click','.admin-recommend',function () {
            var inner = $("#recommend-edit-template").html()
            var goods_id = $(this).data('id');
            var goods_name = $(this).data('value');
            keConfirm({title:'新建推荐',innerHTML:inner},function () {
                var index = $('.rd-index').val();
                var text = $('.rd-text').val();
                if(!isNaN(index) && text.length > 0){
                    ajax_post('optionsRecommend',
                        {'TYPE':'ADD','ID':goods_id,'INDEX':index,'TEXT':text},
                        function (res) {
                            if(res == 1){
                                getResult('goods');
                                hide_alert({innerHTML:'成功加到推荐列表！'})
                            }else{
                                kealert({innerHTML:'操作异常，请重试！'})
                            }
                        })
                    return true
                }else{
                    if($('.warnings').length <= 0){
                        $('.rd-txt').after('<div class="warnings"><p><i class="warning"></i>格式错误</p></div>')
                    }
                }
            })

        })

        // 取消推荐
        $(document).on('click','.cancel_rd',function () {
            var rd_id = $(this).data('id');
            keConfirm({innerHTML:'确定取消该商品的推荐吗？'},function () {
                ajax_post('optionsRecommend',{'TYPE':'CANCEL','ID':rd_id},function (res) {
                    if(res == 1){
                        getResult('recommend')
                        hide_alert({innerHTML:'已取消推荐'})
                    }else{
                        kealert({innerHTML:'操作异常，请重试！'})
                    }
                    return true;
                })

            })

        })


    }

    bind_menu_event();
    bind_event();
    reload();
});
var init_admin = function (admin) {
    $(".admin-name").text("管理员："+ admin);
    var height = document.documentElement.clientHeight - 75;
    $(".menu-box-inner,.menu-opt-open").css('height',height);
}
// 获取对应的模板
var getView = function (name) {
    var id = name + "-template";
    return get_el(id).innerHTML || "";
}
// 初始化模板
var init_template = function (html, res) {

    var template = Handlebars.compile(html)
    $("#view").html(template(res))
    unload('view')
}

// 数据载入后处理方式
var after_tempalte = function (link) {
    if(link == 'notice'){
        // if(!$("#wangEditor_js"))
        // document.write('<script type="text/javascript" id="wangEditor_js" src="js/wangEditor.min.js"></script>')
        var editor = new wangEditor('editor-trigger');
        editor.create();
    }
    if(link == 'carousel'){

    }
}