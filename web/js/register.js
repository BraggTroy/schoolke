$(document).ready(function(){
    var code;
    // bind(".item input","focus","blur");
    function infoHtml(type,text){
        //  显示提示信息的html
        if(type == "warning")
            return '<i style="background-position: -17px 0;"></i><span style="color: red;">'+ text +'</span>'
        if(type == "success")
            return '<i style="background-position: 0 -17px;"></i><span style="color: #90b71b;">'+ text +'</span>'
        if(type == "weak")
            return '<i style="background-position: -17px -34px;"></i><span>'+ text +'</span>'
        if(type == "mid")
            return '<i style="background-position: -34px -17px;"></i><span>'+ text +'</span>'
        if(type == "powerful")
            return '<i style="background-position: -34px -34px;"></i><span>'+ text +'</span>'
        if(type == "")
            return '<i></i><span>'+ text +'</span>'
    }
    // 显示提示信息
    var showInfos = function(type,text,dom){
        var parent = $(dom).parentsUntil("#register_form");

        parent.find('.info').html(infoHtml(type,text))
    }

    /*// 正则表达式检测
    var reg_text = function(){
        var user_reg = /^[\u4e00-\u9fa5]*\w*[\u4e00-\u9fa5]*\w*$/;
    }*/

    // 验证码
    var createCode = function () {
        var ranNum = Math.ceil(Math.random() * 25)
        var ranNum2 = Math.ceil(Math.random() * 25)
        var ranNum3 = Math.ceil(Math.random() * 25)
        var ranNum_ = Math.ceil(Math.random() * 9)
        code = ''+String.fromCharCode(65+ranNum)+''+String.fromCharCode(97+ranNum3)+''+String.fromCharCode(48+ranNum_)+''+String.fromCharCode(65+ranNum2);

        $("#code_show").text(code)
    }

    // 密码等级显示
    var checkPasswordLevel = function(pwd){
        if(/^[a-zA-Z]{6,10}$/.test(pwd) || /^[0-9]{6,20}$/.test(pwd)){
            showInfos("weak","有被盗风险,建议使用字母、数字和符号两种及以上组合",$("#password"));
            return;
        }
        if(/^[a-zA-Z]{11,}$/.test(pwd) || /^[0-9]{11,}$/.test(pwd) || /^[a-zA-Z]{1,}[0-9]{1,}$/){
            showInfos("mid","安全强度适中，可以使用三种以上的组合来提高安全强度",$("#password"));
        }
        if(/^\w*\W{1,}\w*$/.test(pwd) || /^[a-zA-Z0-9]*_\w*$/.test(pwd)){
            console.log(1)
            showInfos("powerful","你的密码很安全",$("#password"));
        }
    }


    var bind_event = function () {
        var input_dom = $(".box input");
        // 表单获取焦点事件
        input_dom.on("focus",function(){
            if($(this).val() == ""){
                $(this).parentsUntil("#register_form").find('.info').html($(this).attr("default"));
            }
        })
        // 空表单失去焦点
        input_dom.on('blur',function(){
            if($(this).val() == ""){
                $(this).parentsUntil("#register_form").find('.info').html("");
            }
        })

        // 用户名检测
        $("#userName").on('blur',function(){
            check_userName();
        })

        // 设置密码检测
        $("#password").on('keydown keyup',function (event) {
            var pwd = $(this).val();
            if( event.keyCode == 8 && pwd.length < 6){
                showInfos("","建议使用字母、数字和符号两种以上的组合，6-20个字符",$("#password"));

            }
            if(pwd.length >= 6){
                checkPasswordLevel(pwd);
            }
        });
        $("#password").on('blur',function () {
            check_password();
        });
        $("#pwd").on('blur',function(){
            check_pwd();
        });

        $("#contact,#school").on('blur',function () {
            if($(this).val() != ""){
                $(this).parentsUntil("#register_form").find('.info').html("");
            }
        })

        $("#code").on("blur",function(){
            check_code();
        })

        //点击切换验证码
        $("#code_show").on('click',function(){
                createCode();
        })

        // 提交表单事件
        $("#submit").on('click',function(){
            var bl_u = check_userName();
            var bl_p = check_password();
            var bl_ = check_pwd();
            var bl_c = check_code();
            var bl_a = check_agreement();


            if(bl_u && bl_p && bl_ && bl_c && bl_a){
                mask();
                var result = get_value("register_form");
                result.time = getTime();

                //提交
                ajax_post("registerNewUser",result,function(res){
                    window.user = res;
                    setCookie('ke_user',JSON.stringify(res),7);
                    window.location.href = "personal.html?id="+ window.user.id +"";
                },"json")
            }

        })

        document.onkeydown = function(e){
            if(e.keyCode == 13){
                e.preventDefault();
            }
        }

        //显示注册协议

        $("#access_text").on('click',function(){
            // ajax_get("getRules.do",{type:'REGISTER'},function(res){
            var text = $("#register-rules").html()
                kealert({
                    title:'用户注册协议',
                    innerHTML: text
                });
            // },"json")
        })
    }

    // 检测字段的合法性
    var check_userName = function(){
        var userName = $("#userName").val();
        var user_reg = /^[\u4e00-\u9fa5]*\w*[\u4e00-\u9fa5]*\w*$/;

        if(userName.length<4 && userName.length >= 0){
            //提示信息
            showInfos("warning","用户名需4-20个字符",$("#userName"));
            return false;
        }
        if(user_reg.test(userName)) {
            if (userName != "" || userName.length >= 4) {
                /*ajax_get("searchUserName", {"USERNAME": userName}, function (res) {
                    if (res == 1) {
                        showInfos("warning", "该用户名已被占用", $("#userName"));
                        return false;
                    } else if (res == 0) {
                        showInfos("success", "该用户名可用", $("#userName"));
                        return true;
                    }
                }, "text")*/
                var bl = false;
                ajax_get("searchUserName",{"USERNAME": userName},function (res) {
                    if (res == 1) {
                        showInfos("warning", "该用户名已被占用", $("#userName"));

                    } else if (res == 0) {
                        showInfos("success", "该用户名可用", $("#userName"));
                        bl = true;
                    }
                })
                return bl;
            }
        }else{
            showInfos("warning", "用户名格式不正确，仅支持中文、字母、数字、“_”的组合", $("#userName"));
            return false;
        }
    }

    var check_password = function () {
        var pwd = $("#password").val();
        if(pwd.length < 6){
            showInfos("warning","密码长度6-20个字符",$("#password"));
            return false;
        }else{
            return true;
        }
    }

    var check_pwd = function(){
        var pwd = $("#pwd").val();
        if(pwd != $("#password").val() && $("#password").val().length > 0){
            showInfos('warning','两次密码不一致',$("#pwd"));
            return false;
        }else if($("#password").val().length > 0){
            showInfos('success','两次密码一致',$("#pwd"));
            return true;
        }
    }

    var check_code = function () {
        if($("#code").val().toLowerCase() == code.toLowerCase()){
            showInfos("success","验证码正确",$("#code"));
            return true;
        }else{
            showInfos("warning","验证码错误",$("#code"));
            return false;
        }
    }

    var check_agreement = function(){
        if($("#access").prop("checked") == true){
            return true;
        }else{
            kealert({
                title:'提示',
                innerHTML:'请先同意《校园客用户注册协议》'
            });
            return false;
        }
    }

    bind_event();
    createCode();

});

