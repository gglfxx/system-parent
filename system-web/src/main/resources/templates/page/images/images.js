let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['flow','form','layer','upload','element','httpAjax'],function(){
    let flow = layui.flow,
        form = layui.form,
        layer = layui.layer,
        upload = layui.upload,
        element = layui.element,
        httpAjax = layui.httpAjax,
        $ = layui.jquery;
    //流加载图片
    let imgNums = 15;  //单页显示图片数量
    loadImage();
    function loadImage(){
        flow.load({
            elem: '#Images', //流加载容器
            done: function(page, next){ //加载下一页
                let param = {
                    pageNo:page,pageSize:imgNums,resType:'image'
                },url = urlPath+"/image/findImages";
                let res = httpAjax.GET(url,param);
                if(res!=null){
                    //模拟插入
                    const imgList = [],data = res.data;
                    const maxPage = imgNums*page < data.length ? imgNums*page : data.length;
                    setTimeout(function(){
                        for(var i=imgNums*(page-1); i<maxPage; i++){
                            imgList.push('<li><img layer-src="'+ data[i].url +'" src="'+ data[i].url +'" alt="'+data[i].title+'" layer-id ="'+ data[i].id +'"><div class="operate"><div class="check"><input type="checkbox" name="belle" lay-filter="choose" lay-skin="primary" title="'+data[i].title+'"></div><i class="layui-icon img_del">&#xe640;</i></div></li>');
                        }
                        next(imgList.join(''), page < (data.length/imgNums));
                        form.render();
                    }, 500);
                }
            }
        });
    }
    //设置图片的高度
    $(window).resize(function(){
        $("#Images li img").height($("#Images li img").width());
    })
    //创建监听事件
    var xhrOnProgress = function (fun) {
        xhrOnProgress.onprogress = fun; //绑定监听
        //使用闭包实现监听绑
        return function () {
            //通过$.ajaxSettings.xhr();获得XMLHttpRequest对象
            var xhr = $.ajaxSettings.xhr();
            //判断监听函数是否为函数
            if (typeof xhrOnProgress.onprogress !== 'function')
                return xhr;
            //如果有监听函数并且xhr对象支持绑定时就把监听函数绑定上去
            if (xhrOnProgress.onprogress && xhr.upload) {
                xhr.upload.onprogress = xhrOnProgress.onprogress;
            }
            return xhr;
        }
    }
    //进度条初始化
    element.init();
    //多图片上传
    upload.render({
        elem: '.uploadNewImg',
        url: urlPath+'/image/uploadImage',
        multiple: true,
        data:{resType:'image'},
        progress: function(e , percent) {
            //之后直接执行下面代码，
            var percent = e + '%' //获取进度百分比
            element.progress('progressBar',percent);
        },
        before: function(obj){
            $("#progress").show();
            //预读本地文件示例，不支持ie8
            obj.preview(function(index, file, result){
                $('#Images').prepend('<li><img layer-src="'+ result +'" src="'+ result +'" alt="'+ file.name +'" class="layui-upload-img"><div class="operate"><div class="check"><input type="checkbox" name="belle" lay-filter="choose" lay-skin="primary" title="'+file.name+'"></div><i class="layui-icon img_del">&#xe640;</i></div></li>')
                //设置图片的高度
                $("#Images li img").height($("#Images li img").width());
                form.render("checkbox");
            });
        },
        done: function(res){
            //上传完毕
            const code = res.code;
            const msg = res.msg;
            const icon = code==0?1:2;
            layer.alert(msg, {
                icon: icon,
                closeBtn: 0,
            }, function (index) {
                location.reload();
                layer.close(index);
            });
        }
    });

    //弹出层
   $("body").on("click","#Images img",function(){
       showImg();
    })
    //旋转标志
    var transFlag = false;
//展示图片
    function showImg(){
        layer.photos({//此处目前存在bug 导致单击后无法在弹层中打开预览  下面会讲解如何处理
            photos: '#Images'
            ,anim: 0,
            tab:function(){
                num = 0;
                var height = $(".layui-layer.layui-layer-page.layui-layer-photos").height();
                var min_height = 100;
                if(height>700){
                    min_height = 50;
                }
                $("#layui-layer-photos").parent().after('<div class="icon" style="position:absolute;width: 100%;z-index: 19891081;text-align: center;cursor: pointer;">\n' +
                    '\t\t<img src="'+urlPath+'/images/xz.png" style="width:50px;height:50px;">\n' +
                    '\t</div>');
                $(".icon").css("top","calc( 100% - "+min_height+"px)");
            },
            end:function(){
                $(".icon").remove();
                transFlag = false;
            }
        });
    }
//旋转图片
    $(document).on("click", ".icon img", function(e) {
        var imagep = $(".layui-layer-phimg").parent().parent();
        var image = $(".layui-layer-phimg").parent();
        var h = image.height();
        var w = image.width();
        num = (num+90)%360;
        if(w>1000){
            transFlag = true;
        }
        if(transFlag){
            if(num==90||num==270){
                h = h*0.5;
                w = w*0.5;
            }else{
                h = h*2;
                w = w*2;
            }
            imagep.css("top", (window.innerHeight - h) / 2);
            imagep.css("left", (window.innerWidth - w) / 2);
            image.height(h);
            image.width(w);
            imagep.height(h);
            imagep.width(w);
        }
        $(".layui-layer.layui-layer-page.layui-layer-photos").css('transform','rotate('+num+'deg)');
    });
//放大
    $(document).on("mousewheel DOMMouseScroll", ".layui-layer-phimg img", function(e) {
        var delta = (e.originalEvent.wheelDelta && (e.originalEvent.wheelDelta > 0 ? 1 : -1)) || // chrome & ie
            (e.originalEvent.detail && (e.originalEvent.detail > 0 ? -1 : 1)); // firefox
        var imagep = $(".layui-layer-phimg").parent().parent();
        var image = $(".layui-layer-phimg").parent();
        var h = image.height();
        var w = image.width();
        if(delta > 0) {
            h = h * 1.05;
            w = w * 1.05;
        } else if(delta < 0) {
            if(h > 100) {
                h = h * 0.95;
                w = w * 0.95;
            }
        }
        imagep.css("top", (window.innerHeight - h) / 2);
        imagep.css("left", (window.innerWidth - w) / 2);
        image.height(h);
        image.width(w);
        imagep.height(h);
        imagep.width(w);
    });
    //删除单张图片
    $("body").on("click",".img_del",function(){
        const _this = $(this);
        const parent = _this.parents("li");
        const image = parent.children("img");
        const id = image.attr("layer-id");
        layer.confirm('确定删除图片"'+_this.siblings().find("input").attr("title")+'"吗？',{icon:3, title:'提示信息'},function(index){
            //删除走后台
            $.getJSON(urlPath+"/image/deleteImages",{ids:id},function(res){
                const code = res.code;
                const msg = res.msg;
                const icon = code == 0?1:2;
                layer.alert(msg, {
                    icon: icon,
                    closeBtn: 0,
                },function (index){
                    layer.close(index);
                    if(code==0){
                        location.reload();
                    }
                });
            });
            layer.close(index);
        });
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

    //批量删除
    $(".batchDel").click(function(){
        const $checkbox = $('#Images li input[type="checkbox"]');
        const $checked = $('#Images li input[type="checkbox"]:checked');
        var ids = [];
        if($checkbox.is(":checked")){
            layer.confirm('确定删除选中的图片？',{icon:3, title:'提示信息'},function(index){
                var index = layer.msg('删除中，请稍候',{icon: 16,time:false,shade:0.8});
                setTimeout(function(){
                    //删除数据
                    $checked.each(function(){
                        const id = $(this).parents("li").children("img").attr("layer-id")
                        $(this).parents("li").hide(1000);
                        setTimeout(function(){$(this).parents("li").remove();},950);
                        ids.push(id);
                    })
                    $.getJSON(urlPath+"/image/deleteImages",{ids:ids.join(",")},function(res){
                        layer.close(index);
                        const code = res.code;
                        const msg = res.msg;
                        const icon = code == 0?1:2;
                        layer.alert(msg, {
                            icon: icon,
                            closeBtn: 0,
                        },function (index){
                            layer.close(index);
                            if(code==0){
                                location.reload();
                            }
                        });
                    });
                    $('#Images li input[type="checkbox"],#selectAll').prop("checked",false);
                    form.render();
                },2000);
            })
        }else{
            layer.msg("请选择需要删除的图片");
        }
    })

})