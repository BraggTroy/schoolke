$.fn.navigationTree=function(options){
    var defaults = {
        className: '.item',
        data: [],
        css:{bottom:80,right:0}
    };
    var settings = $.extend(defaults, options);

    //#region 局部变量
    var _ctlID = $(this).selector;
    var _ctlObj = $(this);
    var _data = settings.data;
    var _className =settings.className;
    var _css = settings.css;
    var _allElements = $(_className);
    //#endregion

    //#region 控制节点显示已完成图标
    /*_ctlObj.showOkIcon = function (id) {
        $('.catalog-list').find('[data-id="' + id + '"]').find('.empty').removeClass('empty').addClass('ok');
    };*/
    //#endregion
    

    //#region 主html结构定义
    var mainHtml = '<div class="side-bar">'+
                        '<em class="circle start"></em>'+
                        '<em class="circle end"></em>'+
                    '</div>';
    //#endregion

    //#region 参数异常处理
    if (_data.length == 0) {
        throw '数据源data属性不能为空.';
    }
    if (_ctlObj.length==0) {
        throw '找不到ID为:' + _ctlID.replace('#','') + '的Html元素.';
    }
    //#endregion

    //#region 引入CSS
    // addSheetFile('NavigationTree.css');


    //#endregion

    //#region拼接控件Html结构
    mainHtml += '<div class="catalog-scroller">' +
                    '<dl class="catalog-list">';
    $.each(_data, function (i, n) {
        if (n) {
            mainHtml += '<dt class="catalog-title level1" data-id="' + n.ID + '">' +
                            '<em class="pointer"></em>' +
                            '<a href="javascript:fnScrollTo(\'' + n.ID + '\')" class="title-link">' +
                                '<span class="text">' +
                                    '<span class="title-index">' + (i + 1) + '</span>' +
                                    '<span class="title-link" nslog-type="1026">'+n.NodeName+'</span>'+
                                    '<span class="empty"></span>' +
                                '</span>'+
                            '</a>' +
                        '</dt>';
            if (n.Children) {
                $.each(n.Children, function (j, m) {
                    mainHtml += '<dd class="catalog-title level2" data-id="' + m.ID + '">' +
                                    '<a href="javascript:fnScrollTo(\'' + m.ID + '\')" class="title-link">' +
                                        '<span class="text">' +
                                            '<span class="title-index">' + (i + 1) + '.' + (j + 1) + '</span>' +
                                            '<span class="title-link" nslog-type="1026">' + (m.NodeName) + '</span>' +
                                            '<span class="empty"></span>' +
                                        '</span>' +
                                    '</a>' +
                                '</dd>';
                });
            }
        }
    });
    mainHtml += '<a class="arrow" href="javascript:void(0);" style="top: 3px;"></a>' +
            '</dl>' +
        '</div>' +
        '<div class="right-wrap" style="display: none;">' +
            '<a class="go-up disable" href="javascript:void(0);"></a>' +
            '<a class="go-down disable" href="javascript:void(0);"></a>' +
        '</div>' +
        '<div class="bottom-wrap">' +
            '<a class="toggle-button" href="javascript:void(0);"></a>' +
            '<a class="gotop-button" href="javascript:void(0);"></a>' +
        '</div>';
    _ctlObj.append(mainHtml).addClass('side-catalog').css(_css);
    //#endregion

    //#region滚动条事件
    var $win = $(window);
    var winHeight = $win.height();
    $win.scroll(function () {
        var winScrollTop = $win.scrollTop();
        for (var i = _allElements.length - 1; i >= 0; i--) {
            var elmObj = $(_allElements[i]);
            //!(滚动条离顶部的距离>元素在当前视图的顶部相对偏移+元素外部高度)&&!(滚动条离顶部的距离<元素在当前视图的顶部相对偏移-window对象高度/2)
            if (!(winScrollTop > elmObj.offset().top + elmObj.outerHeight()) && !(winScrollTop < elmObj.offset().top - winHeight/2)) {
                $('.arrow').css({ top: $('[data-id="' + elmObj.attr('id') + '"]').position().top + 3 });
                return false;
            }
        }
    });
    //#endregion

    //#region置顶事件
    $('.gotop-button').bind('click', function () {
        $('body,html').animate({
            scrollTop: 0
        }, 'fast')
    });
    //#endregion

    //#region 菜单收缩展开事件
    $('.toggle-button').bind('click', function () {
        var height = $('.side-bar').height() + $('.toggle-button').height() + $('.gotop-button').height()+20;
        if ($('.side-bar').css('display') == 'none') {
            $('.side-bar').show('fast');
            $('.catalog-scroller').show('fast');
            if ($('.ScorePanel').length > 0) {
                $('.ScorePanel').animate({
                    bottom: height
                },'fast')
            }
        } else {
            $('.side-bar').hide('fast');
            $('.catalog-scroller').hide('fast');
            if ($('.ScorePanel').length > 0) {
                $('.ScorePanel').animate({
                    bottom: 100
                }, 'fast')
            }
        }
    })
    //#endregion
    
    return _ctlObj;
};

//#region 点击右侧目录树节点滚动到指定的内容区域
function fnScrollTo(id) {
    var container = $('html, body'), scrollTo = $('[id="' + id + '"]');
    container.animate({
        scrollTop: scrollTo.offset().top - 300
    }, 300);
}
//#endregion

