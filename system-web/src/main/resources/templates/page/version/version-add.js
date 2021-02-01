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
})
layui.use(['form', 'layer', 'laydate', 'table','httpAjax'], function () {
    let form = layui.form,
        layer = layui.layer,
        layedit = layui.layedit,
        httpAjax = layui.httpAjax,
        $ = layui.jquery;
    var index = layedit.build('description');
    //编辑器外部操作
    var active = {
        content: function(){
            return layedit.getContent(index); //获取编辑器内容
        }
        ,text: function(){
            return layedit.getText(index); //获取编辑器纯文本内容
        }
        ,selection: function(){
            return layedit.getSelection(index);
        }
    };    //查看详情或者编辑
    if(type!='add'){
        let param = {
            id: id,
        },url = urlPath + "/version/queryVersionDetatil";
        let res = httpAjax.POST(url.param);
        if(res!=null){
            let icon = res.code == 0 ? "1" : "2";
            let data = res.data;
            if (res.code == 0) {
                form.val("noticeAddForm",data);
                layedit.setContent(index, data.noticeContent,false);
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
        data.field.noticeContent = active.content();
        let param =JSON.stringify(data.field) ,
            url = urlPath + '/version/addOrUpdateVersion';
        let res = httpAjax.AXIOS_POST(url,param);
        if(res!=null){
            if(res.code === 0) {
                layer.msg(res.msg, function () {
                    var index = parent.layer.getFrameIndex(window.name);
                    parent.location.reload();
                    parent.layer.close(index);
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
