let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
$.ajax({
    async:false,
    url:urlPath+"/menu/queryMenu?menuType=menu",
    type:"POST",
    dataType:"json",
    headers:{
        token:jwtToken
    },
    success:function(res){
        let data = res.data;
        let html = $("#sidebar-menu").html();
        //显示左侧菜单
        if(html == ''){
            $("#sidebar-menu").html(navBar(data));
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
    let html = '<ul>';
    for(let i=0; i<data.length; i++){
        if(data[i].children != undefined && data[i].children.length > 0){
            html += '<li class="has_sub">';
            html += '<a href="javascript:;" class="waves-effect">';
            html += '<i class="zmdi '+data[i].icon+'"></i>'
            html += '<span>'+data[i].title+'</span>';
            html += '<span class="menu-arrow"></span>';
            html += '</a>';
            html += '<ul class="list-unstyled">';
            for(let j=0;j<data[i].children.length;j++){
                html += '<li>';
                html +='<a href="javascript:;" data-url="'+data[i].children[j].href+'">'+data[i].children[j].title+'</a></li>';
            }
            html += "</ul>";
        }else{
            html += '<li>';
            html +='<a href="javascript:;" class="waves-effect" data-url="'+data[i].href+'">';
            html +='<i class="zmdi '+data[i].icon+'"></i>';
            html +='<span>'+data[i].title+'</span> </a>';
        }
        html += '</li>';
    }
    html += '</ul>';
    html +='<div class="clearfix"></div>';
    return html;
}
