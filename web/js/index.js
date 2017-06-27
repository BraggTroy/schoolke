/**
 * Created by Bragg Troy on 2017/2/19.
 */

getCurrentUser();
getFooter();
bannerTitle("banner-title");

$(document).ready(function () {
    // loaded('goods-list')

    var user_init = function () {
        if(window.user != null && window.user != undefined){
            var user_template = Handlebars.compile($("#user-template").html());
            var res = window.user;
            if(!res.header){
                res.header = "none_head.gif";
            }
            $('.face').html(user_template(res));
        }
    }
    var notice_init = function () {
        ajax_get("noticeList",{},function (res) {
            var list = res.list.slice(0,6);
            var notice_item = '';
            for (var i=0; i<list.length; i++){
                notice_item += '<a href="notice.html?noticeId='+ list[i].id +'" target="_blank">'+ list[i].name +'</a>'
                $('.notice-l').html(notice_item);
            }

        },"json")
    }
    // 最新发布
    var theNew_init = function () {
        loading('goods-list');
        // setTimeout(function () {
            ajax_get("getTheNewGoods",{},function(res){
                loaded('goods-list');
                var result = {};
                result.list = res;
                var theNew_template = Handlebars.compile($("#goods-scan-template").html())
                $('.goods-list').html(theNew_template(result))
            },"json")
        // },10000)
    };

    // 推荐
    var recommend_init = function () {
        loading('recommend-box');
        ajax_get("recommendGoods", {}, function (res) {
            loaded('recommend-box');

            res = JSON.parse(res);
            for (var i=0; i<res.list.length; i++){
                var list = res.list[i];
                list.images = JSON.parse(list.images).slice(0, 3);
            }
            res.list = res.list.slice(0,4)
            var rd_template = Handlebars.compile($("#recommend-template").html())
            $(".recommend-box").html(rd_template(res));



        })
    }


    user_init();
    theNew_init();
    recommend_init();
    notice_init();
});

window.onload = function(){
    $(".nav-box .arrow").remove();


    var index=0;
    var timer=null;
    var pic=$("#pic li");
    var num=$("#num li");
    var flash=$("#flash");
    var left=$("#left");
    var right=$("#right");
    //单击左箭头
    left.on('click',function(){
        index--;
        if (index<0) {
            index=num.length-1
        };
        changeOption(index);
    })
    //单击右箭头
    right.on('click',function(){
        index++;
        if (index>=num.length) {
            index=0
        };
        changeOption(index);
    })
    //鼠标划在窗口上面，停止计时器
    flash.on('mouseover',function(){
        clearInterval(timer);
    })
    //鼠标离开窗口，开启计时器
    flash.on('mouseout',function(){
        timer=setInterval(run,2000)
    });
    //鼠标划在页签上面，停止计时器，手动切换
    for(var i=0;i<num.length;i++){
        num[i].id=i;
        $(num[i]).on('mouseover',function(){
            clearInterval(timer);
            changeOption(this.id);
        })
    }
    //定义计时器
    timer=setInterval(run,3000)
    //封装函数run
    function run(){
        index++;
        if (index>=num.length) {
            index=0
        }
        changeOption(index);
    }
    //封装函数changeOption
    function changeOption(curindex){
        // console.log(index)
        for(var j=0;j<num.length;j++){
            pic[j].style.display="none";
            num[j].className="";
        }
        pic[curindex].style.display="block";
        num[curindex].className="active";
        index=curindex;
    }
}
