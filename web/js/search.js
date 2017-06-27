
getHeader();
getNav();
getFooter();

$(function () {
    var key = decodeURI(searchString('keWords').keWords);

    $("#ke-search, .search-again-input").val(key);
    load("search-result");
    var query_result = function (keywords) {
        ajax_get("search",{'keWords':keywords},function (res) {
            if(res.search.length <=0){
                var $err = $('<div style="text-align: center; margin: 50px auto;">' +
                    '<img src="images/face_error.gif" style="vertical-align: middle;margin-right: 20px;">很遗憾，没有相关商品信息！' +
                    '<div style="text-align: center;"><a href="publishGoods.html" target="_blank" style="text-decoration: underline;">还是我来发布一件商品吧……</a></div></div>')
                $("#search-result").html($err);
            }else{
                $("#ke-search, .search-again-input").val(res.keWords);
                var search_template = Handlebars.compile($("#search-goods-template").html());
                $("#search-result").html(search_template(res));
            }
            unload("search-result");
        },'json')
    }

    var bind_event = function () {
        $(".search-again-input").focus(function () {
            $(this).on('keydown',function (e) {
                if (e.keyCode == 13){
                    $(".search-again-btn").click();
                }
            })
        })
        $('.search-again-btn').on('click',function () {
            var key_words_again = $('.search-again-input').val();
            if(key_words_again.length > 0){
                window.location.href = "search.html?keWords=" + key_words_again
            }
        })
    }

    query_result(key);
    bind_event();
})
