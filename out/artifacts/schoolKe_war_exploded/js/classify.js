
getHeader();
getNav();
getFooter();
$(document).ready(function(){

    var type = searchString("type").type;
    var words = decodeURI(searchString("words").words);
    var classifyId = 0;
    // 横向大菜单点击
    if(type == 'menu'){
        //输出该大分类
        //和大分类下的全部小分类
        //和该大分类下的全部商品
        ajax_get("getClassify",{},function (res) {

            var childClassify = [];
            for(var i=0;i<res.length;i++){
                if(res[i].name == words){
                    classifyId = res[i].id
                }
            }
            for(var j=0;j<res.length;j++) {
                if(classifyId == res[j].parentId){
                    childClassify.push(res[j])
                }
            }
            var menu_template = Handlebars.compile($("#classify-menu-template").html());
            $('.classify-menu-box').html(menu_template({'menu':words,'parentId':classifyId,'classify':childClassify}))
            query_result({'ID':classifyId,'CODE':'ALL'});
        },"json")
    }

    // 纵向小菜单点击
    if(type == 'navList'){
        //输出该小分类
        ajax_get("getClassify",{},function (res) {

            // var childClassify = [];
            for(var i=0;i<res.length;i++){
                if(res[i].name == words){
                    classifyId = res[i].id;
                }
            }
            console.log(classifyId);

            var menu_template = Handlebars.compile($("#small-template").html());
            $('.classify-menu-box').html(menu_template({'menu':words}))
            query_result({'ID':classifyId,'CODE':''});
        },"json")
    }

    var query_result = function(param){
        ajax_post("goodsByClassify",param,function(res){
            if(res.list.length <=0){
                var $err = $('<div style="text-align: center; margin: 50px auto;">' +
                    '<img src="images/face_error.gif" style="vertical-align: middle;margin-right: 20px;">很遗憾，没有相关商品信息！' +
                    '<div style="text-align: center;"><a href="publishGoods.html" target="_blank" style="text-decoration: underline;">还是我来发布一件商品吧……</a></div></div>')
                $("#search-result").html($err);
                return;
            }
            var goods_template = Handlebars.compile($("#goods-template").html());
            $("#search-result").html(goods_template(res));
            $(".search-num").text("共"+ res.list.length +"件商品");
            paging({page:1,total:115},'page-box')

            // $('.progressBar').css('width','0');
        },"json")
    }
    var bind_event = function () {
        $(document).on('click','.items a',function () {
            $(this).parent().siblings().removeClass('title');
            $(this).parent().addClass('title');

            var id = $(this).data('value');
            var param = {};

            if(id == 0){
                param = {'ID':classifyId,'CODE':'ALL'}
            }else{
                param = {'ID':id,'CODE':''}
            }

            query_result(param);
        })
    }

    bind_event();
});