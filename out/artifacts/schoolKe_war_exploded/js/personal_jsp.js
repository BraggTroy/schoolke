
$(function () {
    var menuChange = function (content,menu_text) {
        var el = $('.'+content);
        el.siblings().css('display','none')
        el.css('display','block')

        $('.cont_title').html(menu_text)
    }

/* {
* page:1, 当前页
* pageSize:50,
* total:115  数据总量
* } 默认每页50条
* */
    paging({page:1,total:115},'page-box')

    $('.menu_li').on('click',function () {
        var this_ = $(this);
        var menu_text = this_.text();
        this_.siblings().removeClass('active')
        this_.addClass('active')
        // 个人中心菜单点击
        if(this_.hasClass('msg')){
            menuChange('msg_box',menu_text)
        }
        if(this_.hasClass('person')){
            menuChange('person_box',menu_text)
        }
        if(this_.hasClass('my')){
            menuChange('my_box',menu_text)
        }

        // 后台管理菜单点击
        if(this_.hasClass('agent')){
            menuChange('agent_box',menu_text);
            paging({page:1,total:115},'page-box')
        }
        if(this_.hasClass('ordinary')){
            menuChange('ordinary_box',menu_text);
            paging({page:1,total:115},'page-box')
        }
    })
})

// 分页方法封装
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

    var $total = '<div class="total">共'+ conf.total +'条，</div>'

    var $toPage = '<div class="to-page"><span>到第</span>' +
        '<input type="number" class="page-input" value="1" min="1" max="'+ pageNum +'">' +
        '<span>页</span><span class="page-submit">确定</span></div>'

    el.html($ul + $total + $toPage);
    $('.page-items .item:eq(1)').addClass('active');

    var page_event = function () {

    }


}
