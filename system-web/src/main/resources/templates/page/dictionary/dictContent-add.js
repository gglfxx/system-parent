//项目地址
let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken(),
//主键ID
    id = onlineSystem.getUrlParam("id"),
    //主键ID
    dictId = onlineSystem.getUrlParam("dictId"),
//类型
    type = onlineSystem.getUrlParam("type");
var loadIndex;
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['form', 'layer','httpAjax'], function () {
    let form = layui.form,
        layer = layui.layer,
        httpAjax = layui.httpAjax,
        $ = layui.jquery;

    //查看详情或者编辑
    if(type!='add'){
        let url = urlPath + "/dict/queryDictContentDetail",
            param={
                id:id
            };
        let res = httpAjax.POST(url,param);
        if(res!=null){
            var icon = res.code == 0 ? "1" : "2";
            if (res.code == 0) {
                form.val("dictContentAddForm",res.data);
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
        data.field.dictId = dictId;
        let url = urlPath + '/dict/addOrUpdateDictContent';
        let res = httpAjax.AXIOS_POST(url,JSON.stringify(data.field));
        if(res!=null){
            if (res.code === 0) {
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
    form.render();
})
//关闭窗口
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
