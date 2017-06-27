/**
 * Created by Bragg Troy on 2017/3/15.
 */
getHeader();
getNav();

$(function () {
    var noticeId = searchString("noticeId").noticeId || 0;
    ajax_get("notice",{"ID":noticeId},function(res){
        res = JSON.parse(res);
        document.title = "公告 - " +　res.name;
        $('.title').html(res.name);
        $('.time').html("时间："+ res.time);
        $('.text-content').html(res.text);
    })

    // 加载公告列表


})

getFooter();
