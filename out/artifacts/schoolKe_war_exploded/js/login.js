
$(function(){
    window.scope = {};
    var backURL = decodeURI(window.location.search).split("'")[1];
    // var backURL = searchString("backURL");
    window.scope.back = backURL;
    if($("#loginName").focus() || $("#password").focus()){
        document.onkeydown = function(){
            if (event.keyCode == 13){
                $("#login-submit").click();
            }
        }
    }
   $("#login-submit").on('click',function () {
       check_password();
   })
});
var check_password = function(){

    var $name = $("#loginName");
    var $password = $("#password");

    if ($name.val() == ""){
        info_show(null,"用户名不能为空",$name);
        return false;
    }
    if($password.val() == ""){
        info_show(null,"密码不能为空",$password);
        return false;
    }
    $("#login-submit").text("登录中...").css('background','#f40').attr('disabled','true');
    ajax_post("userLogin",{NAME:$name.val(),PASSWORD:$password.val()},function(res){
        if(res == "0"){
            info_show(false,"用户名或密码不正确",$password);
            $password.val("").focus();
            $("#login-submit").html("登&nbsp;&nbsp;&nbsp;&nbsp;录").css('background','#f10215').removeAttr("disabled");
        }
        else{
            var result = JSON.stringify(res);
            if($("#login-remember").prop("checked")){
                setCookie("ke_user",result,7);
            }else{
                setCookie("ke_user",result,0.5);
            }
            // window.user = res;
            if(window.scope.back == undefined){
                window.location.href = "index.html";
            }else{
                window.location.href = window.scope.back;
            }

        }
    },"json")
};
var info_show = function(type,text,el){
    $('.msg-wrap').css('visibility','visible');
    $('.msg').text(text);
    if(type == null){
        $(el).css('border-color','#cccccc').focus();
        $(el).prev().css({'border-color':'#cccccc','background-position':'0 -48px'});
    }
    if(type == false){
        $(el).css('border-color','#f10215');
        $(el).prev().css({'border-color':'#f10215','background-position':'0 -96px'});
    }
}
