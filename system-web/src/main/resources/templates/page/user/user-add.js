//项目地址
let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken(),
//主键ID
id = onlineSystem.getUrlParam("id"),
//类型
type = onlineSystem.getUrlParam("type");
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['form', 'layer','laydate','httpAjax'], function () {
    let form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        httpAjax = layui.httpAjax,
        $ = layui.jquery;

    //查看详情或者编辑
    if(type!='add'){
        let url = urlPath + "/user/queryUserDetail",
            param={
                id:id
            };
        let res = httpAjax.POST(url,param);
        if(res!=null){
            let icon = res.code == 0 ? "1" : "2";
            if (res.code == 0) {
                form.val("userAddForm",res.data);
            }else{
                layer.alert(res.msg, {
                    icon: icon,
                    closeBtn: 0
                });
            }
        }
    }
    //提交
    form.on('submit(saveBtn)', function (data) {
        data.field.id = id;
        let url = urlPath + '/user/addOrUpdateUser';
        let res = httpAjax.AXIOS_POST(url,JSON.stringify(data.field));
        if(res!=null){
            if(res.code === 0) {
                layer.msg(res.msg, function () {
                    parent.location.reload();
                    onlineSystem.closeIframe();
                });
            } else {
                layer.alert(res.msg, {
                    icon: 2,
                    closeBtn: 0,
                });
            }
        }
        return false;
    });
    //满足身份证格式就带出出生日期和性别
    $('#idNum').bind('input propertychange',function(){
        var idNum = $(this).val();
        var reg = /(^\d{15}$)|(^\d{17}(x|X|\d)$)/;
        if(reg.test(idNum)){
            var idNumV = onlineSystem.idNumVentify(idNum);
            $("input[name=sex][value='"+idNumV.sex+"']").attr("checked", true);
            $(".birthday").val(idNumV.birthday);
        }else{
            var sex = 1;
            $("input[name=sex][value='"+sex+"']").attr("checked", true);
            $(".birthday").val('');
        }
        form.render();
    });
    /*
    * 时间控件
    */
    laydate.render({
        elem : '.birthday' //指定元素
    });
    form.render();
})
//关闭窗口
function iframeClose() {
    onlineSystem.closeIframe();
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
