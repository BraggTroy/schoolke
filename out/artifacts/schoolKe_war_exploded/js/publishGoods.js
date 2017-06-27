
getHeader();
$(".ke-header-main").remove();
getFooter();
login_require();
$(function(){

    var goodsId = searchString("id") || 0;
    var type = searchString("type") || "";
    window.update = {};


    // 分类信息的处理
    var classifyResult = function(){
        var parentClassify = [],
            childClassify = [];
        ajax_get("getClassify",{},function(res){
            window.update.classify = res;
            for (var i=0; i<res.length; i++){
                if(res[i].parentId == 0){
                    parentClassify.push(res[i]);
                }else{
                    childClassify.push(res[i]);
                }
            }
            select_init(parentClassify,childClassify);
        },"json");

        var select_init = function(parent_Classify,child_Classify){
            var $select_p = $("#classify_p");
            var option_p = "";
            for(var i=0;i<parent_Classify.length;i++){
                option_p += "<option value='"+ parent_Classify[i].id +"'>"+ parent_Classify[i].name +"</option>"
            }
            $select_p.html(option_p);
            child_select_init($select_p.val() , child_Classify);
            $select_p.on('change',function () {
                var parent_id = this.value;
                child_select_init(parent_id , child_Classify);
            })
        }
        var child_select_init = function(parent_id , child_Classify){
            var $classify_c = $("#classify_c");
            $classify_c.html("");
            var option_ = "";
            for(var i=0;i<child_Classify.length;i++){
                if(parent_id == child_Classify[i].parentId){
                    option_ += "<option value='"+ child_Classify[i].id +"'>"+ child_Classify[i].name +"</option>"
                }
            }
            $classify_c.html(option_);
        }


    }
    classifyResult();

    var click_times = 0;
    // 用于判断是否是新建，若新建则为null，否则为编辑状态
    var img_result = []; //用于记录图片数量


    // 如果是编辑则载入数据
    if(goodsId.id != 0 && type.type == "UPDATE"){
        console.log("编辑状态");
        ajax_get("getGoodsInfo",{goodsId:goodsId.id},function(res){
            //判断该商品是否属于当前用户
            if(window.user.id != res.goods.user.id){
                keConfirm({
                    title:'警告',
                    innerHTML:'没有权限',
                    confirm_hide:true,
                    before_show:function(){
                        $('.popup-btn .close').remove();
                    }
                },function(){
                    window.close();
                });
                return;
            }
            set_images(res.images);
            set_value(res.goods);

        },"json")

        $("#publish-btn").text("确定保存")
    }
    // 编辑状态还原图片
    var set_images = function (images) {
        click_times = images.length;
        console.log(click_times)
        for(var i=0;i<images.length;i++){
            img_result.push(i);
            //显示图片
            var $img_ele = $('<div class="img-item"><img src="../images/goodsImages/'+ images[i].name +'"></div>');
            var $a_ele = $('<a href="javascript:;" class="delete-img" data-value="'+ images[i].id +'" data-index="'+ i +'">删除</a>');
            $img_ele.append($a_ele);
            $('.images').append($img_ele);

            // 添加删除点击事件，传递参数为被删除的id
            $a_ele.on('click',function(){
                var this_ = $(this)
                var order = $(this).data('index');
                var del_id = $(this).data('value');
                console.log(del_id);
                // del_arr中保存原商品图片编辑后删除的图片的ID
                ajax_post("deleteGoodsImage",{"DELID":del_id},function(res){
                    if(res == 1){
                        click_times = click_times -1;
                        img_result.splice(order,1);

                        data_reset();
                        // 显示删除
                        this_.parent().remove();
                    }else{

                    }
                },"text");

            });


        }




    }
    // 编辑状态还原基础信息
    var set_value = function (goods) {
        $("#goodsName").val(goods.goodsName);
        $("#schoolPrice").val(goods.schoolPrice);
        $("#originPrice").val(goods.originPrice);
        $("#sell").val(goods.sell);
        $("#remark").val(goods.remark);
        var classify = window.update.classify;
        $("#classify_p").val(goods.classify.parentId);
        $("#classify_c").children().remove();

        var option_ = "";
        for(var i=0;i<classify.length;i++){
            if(classify[i].parentId == goods.classify.parentId){
                option_ += '<option value="'+ classify[i].id +'">'+ classify[i].name +'</option>'
            }
        }
        $("#classify_c").html(option_);
        $("#classify_c").val(goods.classify.id);
    }


    var bind_events = function(){
        // 点击上传图片按钮
        $("#add-img-btn").on('click',function(){
            if(img_result.length >= 5){
                $("#add-img-btn").addClass('btn-gray');
                kealert({title:'提示',innerHTML:"最多上传5张图片"});
                return;
            } else{
                $("#add-img-btn").removeClass('btn-gray');
            }

            var file_input = $(".file_input_upload");
            file_input.attr('name','pic'+ click_times);
            file_input.click();

            file_input.on('change',function(){

                // 当前对象
                var file = this.files[0];
                var fileReader = new FileReader();
                // 监听
                fileReader.onabort = function(){
                    alert("图片读取中断，请重试")
                }
                fileReader.onerror = function(){
                    alert("图片读取失败，请重试")
                }

                fileReader.onload = function(e){
                    console.log("图片读取成功");
                    $("#add-img-btn").html("点击添加图片");
                    if(file.size/1024/1024 >= 10){
                        kealert({title:'提示',innerHTML:"每张图片不能大于10Mb，请重新选择图片"})
                        return;
                    }
                    img_result.push(click_times);
                    console.log(img_result);

                    // 输出显示
                    var img_ele = $('<div class="img-item"><img src="'+ e.target.result +'"></div>');
                    var a_ele = $('<a href="javascript:;" class="delete-img" data-index="'+ click_times +'" data-value="pic'+ click_times +'">删除</a>');

                        a_ele.on('click',function(){
                            var id = $(this).data('value');
                            var order = $(this).data('index');

                            console.log(id);
                            var img_input = $("#img-form input[type=file]");
                            for(var i=0;i<img_input.length;i++){
                                if(id == $(img_input[i]).attr("name")){
                                    // input删除
                                    $(img_input[i]).remove();
                                }
                            }
                            // 显示删除
                            $(this).parent().remove();
                            // 数据重置

                            img_result.splice(order,1);
                            click_times = click_times - 1;
                            data_reset();
                        });
                    img_ele.append(a_ele);
                    $('.images').append(img_ele);

                    var copy_file_input = file_input.clone();
                    file_input.removeClass('file_input_upload');
                    $("#img-form").append(file_input);
                    $("body").append(copy_file_input);

                    click_times = click_times + 1;
                    console.log("添加click_times:"+click_times);
                };
                // 读取
                try{
                    console.log("准备读取-")
                    fileReader.readAsDataURL(file);
                    console.log('读取。。。')
                    $("#add-img-btn").html("读取中...");
                }catch (Exception){
                    console.log(Exception.name +":"+ Exception.message);
                }
            })
        })

        // 显示协议信息
        $('.read-rules').on('click',function(){
            // ajax_get('getRules.do',{type:'PUBLISH'},function(res){
                var text = $("#publish-rules").html()
                keConfirm({
                    title:'校园客发布信息守则',
                    innerHTML: text,
                    confirm_hide:false,
                    before_show:function(){}
                },function () {
                })
            // },"json");
        });

        // 验证提交
        $('#publish-btn').on('click',function () {
            var text_result = check_form();
            console.log(text_result);
            if(text_result == false){
                return;
            }
            if(!$(".agree-rules").prop("checked")){
                kealert({title:'提示',innerHTML:'请先同意《校园客发布信息守则》'});
                return;
            }

            // 新建
            if(type.type == "NEW"){
                new_goods("publishGoods",text_result);
            }
            // 更新
            if(type.type == "UPDATE"){
                update_goods("updateGoods",text_result);
            }
        })
    }
    var data_reset = function () {
        var a_index = $('.img-item a');
        for(var i=0; i<a_index.length; i++){
            $(a_index[i]).data('index', i);
        }

        if("NEW" == type.type){
            var img_input = $("#img-form input[type=file]");
            for(var i=img_input.length-1;i>=0;i--){
                $(img_input[i]).attr('name','pic'+i);
            }
        }

        if("UPDATE" == type.type){

        }
    }

    var check_form = function () {
        var text_result = {};
        if(img_result.length == 0){
            kealert({title:'提示',innerHTML:'请至少上传一张商品图片'});
            return false;
        }
        /*var input_text = Array.apply(null,$('.publish-inner input[type=number]'))
        var textarea_text =  Array.apply(null,$('.publish-inner textarea'))
        var form_text = input_text.concat(textarea_text);*/
        var form_text = $('.publish-inner input[type=number],.publish-inner textarea,.publish-inner select');
        // 为空判断
        for(var i=0;i<=form_text.length;i++){
            var val = $(form_text[i]).val();
            var key = $(form_text[i]).attr('name');

            if(val == ''){
                var title = $(form_text[i]).prev().text();
                kealert({
                    title: '提示',
                    innerHTML: title+'不能为空'
                })
                return false;
            }else{
                text_result[key] = val;
            }
        }
        text_result.publishTime = getTime();
        text_result.picNum = click_times;
        text_result.userId = window.user.id;
        return text_result;
    }

    var new_goods = function(url,param){
        ajax_post(url,param,function(returnId){
            if(returnId != 0){//success
                $("#img-form").append($("<input type='text' name='imagesNum' value='"+ click_times +"'/>"));
                $("#img-form").append($("<input type='text' name='goodsId' value='"+ returnId +"'/>"));

                var formDom = document.getElementById("img-form");
                var formData = new FormData(formDom);
                // console.log(formData)
                var req = new XMLHttpRequest();
                req.open("POST", "updateImages");
                req.onreadystatechange = function() {
                    if (this.status === 200 && this.readyState === 4) {
                        var res = JSON.parse(this.responseText);
                        for(var i=0;i<click_times;i++){
                            if(res.result[i] == 1){
                                keConfirm({
                                    title:'提示',
                                    innerHTML:'商品信息发布成功',
                                    confirm_hide:true,
                                    before_show:function(){}
                                },function(){
                                    window.location.href = "goodsDetail.html?goodsId="+returnId+"&type=ON";
                                })
                            }
                        }
                    }
                };
                //将form数据发送出去
                req.send(formData);
                //避免内存泄漏
                req = null;
            }
        },'text');
    }
    var update_goods = function(url,param){
        param.goodsId = goodsId.id;
        ajax_post(url,param,function(res){
            if(res == 1){
                kealert({title:'提示',innerHTML:'编辑成功！'})
            }else{
                kealert({title:'提示',innerHTML:'编辑失败请重试！'})
            }
        },"text")
    }

    bind_events();
});





