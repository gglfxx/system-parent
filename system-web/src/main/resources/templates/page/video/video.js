let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['laypage', 'layer','httpAjax','upload'], function(){
    let laypage = layui.laypage,
        httpAjax = layui.httpAjax,
        layer = layui.layer,
        upload = layui.upload,
        $ = layui.$;
    let limit = 20;
    //总页数低于页码总数
    laypage.render({
        elem: 'page',
        count: 50,
        limit:20,
        jump: function(obj, first) {
            let pageNo = obj.curr,pageSize = obj.limit;
            //首次不执行
            if (!first) {
                renderVideo(pageNo,pageSize);
            }
        }
    });
    //初始化视频
    renderVideo(1,limit);
    function  renderVideo(pageNo,pageSize){
        let param = {
            resType: 'video',
            pageNo:pageNo,
            pageSize:pageSize
        }, url = urlPath+"/image/findImages";
        let res = httpAjax.GET(url,param);
        if(res!=null){
            let data = res.data;
            if (res.code == 0) {
                let html = null;
                if(data!=null&&data.length>0){
                    for(let i=0;i<data.length;i++){
                        html+= "<div class='layui-col-md2 layui-col-sm4'>";
                        let url = data[i].url,
                            description = data[i].description;
                        html+="<div class='video-container'>";
                        html+="<a href='javascript:;'>";
                        html+="<img  src='"+url+"'/>";
                        html+="<a href='javascript:;'>";
                        html+="<div class='video-text'>";
                        html+="<p class='info'>'"+description+"'</p>";
                        html+="<div class='price'>";
                        html+="<b>￥79</b>";
                        html+="<span class='flow'>";
                        html+="<i class='layui-icon layui-icon-rate'></i>433"+"</span>";
                        html+="</div></div></a></div></div>";
                    }
                    $("#video").html(html);
                }
            }
        }
    }

    //打开新页面
    $(".uploadVideo").click(function(){
        alert(11111);
    })
    //全选
    form.on('checkbox(selectAll)', function(data){
        var child = $("#Images li input[type='checkbox']");
        child.each(function(index, item){
            item.checked = data.elem.checked;
        });
        form.render('checkbox');
    });

    //通过判断是否全部选中来确定全选按钮是否选中
    form.on("checkbox(choose)",function(data){
        const child = $(data.elem).parents('#Images').find('li input[type="checkbox"]');
        const childChecked = $(data.elem).parents('#Images').find('li input[type="checkbox"]:checked');
        if(childChecked.length == child.length){
            $(data.elem).parents('#Images').siblings("blockquote").find('input#selectAll').get(0).checked = true;
        }else{
            $(data.elem).parents('#Images').siblings("blockquote").find('input#selectAll').get(0).checked = false;
        }
        form.render('checkbox');
    })
    form.render();
});