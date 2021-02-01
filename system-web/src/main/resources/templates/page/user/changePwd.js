let urlPath = onlineSystem.contextPath();
layui.use(['form','layer','laydate','table','laytpl'],function(){
    let form = layui.form,
        layer = layui.layer,
        $ = layui.jquery,
        table = layui.table;

    let userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'));
    if(userInfo){
        $('input[name="username"]').attr("value",userInfo.username);
    }
    //添加验证规则
    form.verify({
        oldPwd : function(value, item){
            if(value != "123456"){
                return "密码错误，请重新输入！";
            }
        },
        newPwd : function(value, item){
            let reg = /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,12}$/;
            if(value&&!reg.test(value)){
                return "新密码设置长度6-12位，必须为数字和字母组合，请重新输入！";
            }
        },
        confirmPwd : function(value, item){
            if(!new RegExp($("#oldPwd").val()).test(value)){
                return "两次输入密码不一致，请重新输入！";
            }
        }
    })
    //提交
    form.on('submit(changePwd)', function (data) {
        $.ajax({
            contentType:"application/json",
            type: "post",
            url: urlPath + '/user/changePwd',
            data: JSON.stringify(data.field),
            dataType: 'json',
            async: false,
            success: function (data) {
                if (data.code == 0) {
                    layer.msg(data.msg, function () {
                        let index = parent.layer.getFrameIndex(window.name);
                        parent.location.reload();
                        parent.layer.close(index);
                    });
                } else {
                    layer.alert(data.msg, {
                        icon: 2,
                        closeBtn: 0,
                    });
                }
            }
        });
        return false;
    });
    //控制表格编辑时文本的位置【跟随渲染时的位置】
    $("body").on("click",".layui-table-body.layui-table-main tbody tr td",function(){
        $(this).find(".layui-table-edit").addClass("layui-"+$(this).attr("align"));
    });

})