let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
$.ajax({
    url:urlPath+"/menu/queryMenu?menuType=menu",
    type:"POST",
    dataType:"json",
    headers:{
        token:jwtToken
    },
    success:function(res){
        let data = res.data;
        let html = $(".navBar").html();
        //显示左侧菜单
        if(html == ''){
            $(".navBar").html(navBar(data)).height($(window).height()-245);
            $(window).resize(function(){
                $(".navBar").height($(window).height()-245);
            })
        }
    }
})
//加载左侧菜单
function navBar(strData){
    let data;
    if(typeof(strData) == "string"){
        data = JSON.parse(strData); //部分用户解析出来的是字符串，转换一下
    }else{
        data = strData;
    }
    let html = '<ul class="nav flex-column mb-2">';
    //折叠事件下标
    let index = 0;
    for(let i=0; i<data.length; i++){
        if(data[i].spread){
            html += '<li class="nav-item layui-nav-itemed">';
        }else{
            html += '<li class="nav-item">';
        }
        if(data[i].children != undefined && data[i].children.length > 0){
            index++;
            html += '<a class="nav-link" href="#collapse'+index+'" data-toggle="collapse" role="button"  aria-expanded="false" aria-controls="#collapse'+index+'">';
            /*if(data[i].icon !== undefined && data[i].icon != ''){
                if(data[i].icon.indexOf("icon-") !== -1){
                    html += '<span data-feather="'+data[i].icon+'" data-icon="'+data[i].icon+'"></span>'
                }else{
                    html += '<span data-icon="'+data[i].icon+'">'+data[i].icon+'</span>'
                }
            }*/
            html += '<span data-feather="'+data[i].icon+'"></span>'
            html += '<cite>'+data[i].title+'</cite>';
            html += '<span class="layui-nav-more"></span>';
            html += '</a>';
            html += '<dl class="m-3 collapse multi-collapse" id="collapse'+index+'">';
            for(var j=0;j<data[i].children.length;j++){
                if(data[i].children[j].target == "_blank"){
                    html += '<dd><a class="nav-link" href="javascript:;" data-url="'+data[i].children[j].href+'" target="'+data[i].children[j].target+'">';
                }else{
                    html += '<dd><a class="nav-link" href="javascript:;" data-url="'+data[i].children[j].href+'">';
                }
                if(data[i].children[j].icon != undefined && data[i].children[j].icon != ''){
                    if(data[i].children[j].icon.indexOf("icon-") != -1){
                        html += '<i class="layui-icon '+data[i].children[j].icon+'" data-icon="'+data[i].children[j].icon+'"></i>';
                    }else{
                        html += '<i class="layui-icon" data-icon="'+data[i].children[j].icon+'">'+data[i].children[j].icon+'</i>';
                    }
                }
                html += '<cite>'+data[i].children[j].title+'</cite></a></dd>';
            }
            html += "</dl>";
        }else{
            if(data[i].target == "_blank"){
                html += '<a class="nav-link" href="javascript:;" data-url="'+data[i].href+'" target="'+data[i].target+'">';
            }else{
                html += '<a class="nav-link" href="javascript:;" data-url="'+data[i].href+'">';
            }
            if(data[i].icon != undefined && data[i].icon != ''){
                if(data[i].icon.indexOf("icon-") != -1){
                    html += '<i class="layui-icon '+data[i].icon+'" data-icon="'+data[i].icon+'"></i>';
                }else{
                    html += '<i class="layui-icon" data-icon="'+data[i].icon+'">'+data[i].icon+'</i>';
                }
            }
            html += '<cite>'+data[i].title+'</cite></a>';
        }
        html += '</li>';
    }
    html += '</ul>';
    return html;
}
