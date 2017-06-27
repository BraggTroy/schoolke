/**
 * Created by Bragg Troy on 2017/3/15.
 */
getHeader();
getNav();
login_require();
function msg_loading() {
    $("#load_gif").css('display','inline');
}
function msg_loaded() {
    $("#load_gif").css('display','none');
}
$(function () {
    var aboutMe = function () {
        $('.msg-title-text').text('关于我的留言');
        msg_loading();
        ajax_get("getMessages",{'ID':window.user.id,'TYPE':'FOR_USER'},function (res) {
            if(res.msgList.length == 0){
                $('.msg-cont').html('<div class="no-msg">暂无消息</div>');
                msg_loaded();
                return;
            }
            var about_template = Handlebars.compile($("#aboutMsg-template").html());
            $('.msg-cont').html(about_template(res));
            msg_loaded();
        },"json")
    }

    var myMsg = function () {
        $('.msg-title-text').text('已发表的评论');
        msg_loading();
        ajax_get("getMessages",{'ID':window.user.id,'TYPE':'FOR_USER_MY'},function (res) {
            if(res.msgList.length == 0){
                $('.msg-cont').html('<div class="no-msg">暂无消息</div>');
                msg_loaded();
                return;
            }
            var about_template = Handlebars.compile($("#myMsg-template").html());
            $('.msg-cont').html(about_template(res));
            msg_loaded();
        },"json")
    }

    var bind_event = function () {
        $(document).on('click','.msg-item',function () {
            $(this).siblings().removeClass('active');
            $(this).addClass('active');

            // 关于我的留言
            if($(this).hasClass('myGoods-msg')){
                aboutMe();
            }
            // 已发布的留言，评论
            if($(this).hasClass('my-msg')){
                myMsg();
            }

        })
        // 删除
        $(document).on('click','.del_my_msg',function () {
            var msgId = $(this).data('value');
            var msg = $(this).data('text');
            keConfirm({
                title:'删除',
                innerHTML:'确定要删除“<span style="color:#666;">'+ msg +'</span>”吗？',
                confirm_hide:true,
                before_show:function () {
                }
            },function () {
                ajax_post('deleteMyMsg',{ID:msgId},function (res) {
                    if(res == 1){
                        myMsg();
                        hide_alert({title:'提示',innerHTML:'删除成功'})
                    }else{
                        hide_alert({title:'提示',innerHTML:'操作异常，请重试！'})
                    }
                },"text")
            })

        })
    }

    bind_event();
    aboutMe();
})
getFooter();