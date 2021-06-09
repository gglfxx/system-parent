//项目地址
let urlPath = onlineSystem.contextPath();
//初始化四季背景图
let images = {'spring':'images/spring.jpg','summer':'images/summer.jpg','autumn':'images/autumn.jpg','winter':'images/winter.jpg'};
layui.config({
    base: "layui/",
    version: new Date().getTime()
}).use(['form', 'layer'], function () {
    let form = layui.form,
    layer = layui.layer,
        $ = layui.jquery;
    /// 登录过期的时候，跳出iframe框架
    if (top.location != self.location)
        top.location.href = urlPath+"/login.html";
    $(document).ready(
        function() {
            $.getJSON(urlPath+"/config/changeImage",function (res){
                let code = res.code;
                let data = res.data;
                let image = null;
                if(code==0){
                    image = images[data];
                    if(image){
                        $("body").css({"backgroundImage":"url('"+image+"')",
                            "background-size":"cover","-moz-background-size":"cover","-webkit-background-size":"cover"});
                    }
                }
            })
        });
    // 进行登录操作
    form.on('submit(login)', function (data) {
        $.ajax({
            type: "post",
            url: urlPath + '/login',
            data: data.field,
            dataType: 'json',
            async: false,
            success: function (res) {
                let data = res.data,code = res.code,msg = res.msg;
                if (code == 0) {
                    layer.msg('登录成功', function () {
                        //登陆成功后把用户信息存入缓存
                        window.localStorage.setItem("access_token",data);
                        window.location = urlPath+'/index';
                    });
                } else {
                    layer.alert(msg, {
                        icon: 2,
                        closeBtn: 0,
                    }, function (index) {
                        layer.close(index);
                    });
                }
            }
        });
        return false;
    });
})
