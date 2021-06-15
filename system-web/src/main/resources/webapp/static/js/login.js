//登陆
let urlPath = onlineSystem.contextPath();
$(function(){
    $("#login_btn").click(function(){
        const username = $("#username").val();
        const password = $("#password").val();
        //参数
        const param = {
            username: username,
            password: password
        };
        $.ajax({
            type: "post",
            url: urlPath+'/login',
            data: param,
            dataType: 'json',
            async: false,
            success: function (res) {
                let data = res.data,code = res.code,msg = res.msg;
                if (code == 0) {
                    //登陆成功后把用户信息存入缓存
                    window.localStorage.setItem("access_token",data);
                    window.location = urlPath+'/index';
                } else {
                    $(".alert").alert();
                }
            }
        })
    })
})
