
$(function () {
    if(window.admin != undefined){
        window.location.href = "m.index.html"
    }
    var bind_event = function () {
        var error = $('.error span');
        $("#submit").on('click',function () {
            var para = get_value('form');
            if(para.userName.length == 0 || para.password.length == 0){
                error.text("用户名和密码不能为空！");
                error.parent().css('display','inline-block');
                return false;
            }
            $(this).val("登录中...");
            ajax_post("Login",para,function (res) {
                if(res != "null"){
                    sessionStorage.admin = res;
                    window.location.href = "m.index.html";
                }else{
                    $("#submit").val("登 " + " " + " 录");
                    error.text("用户名或密码错误！");
                    error.parent().css('display','inline-block');
                }

            })
        })
        if($("#userName").focus() || $("#password").focus()){
            document.onkeydown = function(){
                if (event.keyCode == 13){
                    $("#submit").click();
                }
            }
        }

    }


    bind_event();
})
