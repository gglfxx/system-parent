//项目地址
let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken(),
//主键ID
id = onlineSystem.getUrlParam("id"),
//类型
type = onlineSystem.getUrlParam("type");
layui.config({
    base: "layui/",
    version: new Date().getTime()
}).use(['form', 'layer','table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table = layui.table,
        $ = layui.jquery;

    //查看详情或者编辑
    if(type!='add'){
        $.ajax({
            url: urlPath + "/role/queryRoleDetatil",
            data: {
                "id": id
            },
            type: "post",
            async: false,
            dataType: 'json',
            headers: { //通过 request 头传递
                token: jwtToken
            },
            success: function (res) {
                var icon = res.code == 0 ? "1" : "2";
                if (res.code == 0) {
                    form.val("roleAddForm",res.data);
                }else{
                    layer.alert(res.msg, {
                        icon: icon,
                        closeBtn: 0
                    });
                }
            }
        });
    }
    //提交
    form.on('submit(saveBtn)', function (data) {
        data.field.id = id;
        $.ajax({
            contentType:"application/json",
            type: "post",
            url: urlPath + '/role/addOrUpdateRole',
            data: JSON.stringify(data.field),
            dataType: 'json',
            async: false,
            headers: { //通过 request 头传递
                token: jwtToken
            },
            success: function (data) {
                if (data.code == 0) {
                    layer.msg(data.msg, function () {
                        var index = parent.layer.getFrameIndex(window.name);
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
    form.render();
})
//关闭窗口
function iframeClose() {
    var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
    parent.layer.close(index); //再执行关闭
}
//字符过滤
function characterFilter(s){
    var pattern = new RegExp("[`~!@#$^&*()=|{}':;',\\[\\].<>/?~！@#￥……&*（）——|{}【】‘；：'。，、？]")
    var rs = "";
    for (var i = 0; i < s.length; i++) {
        rs = rs+s.substr(i, 1).replace(pattern, '');
    }
    return rs;
}
