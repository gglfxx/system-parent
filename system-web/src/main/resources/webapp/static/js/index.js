//退出
$(function (){
    //初始化图标
    $(".signOut").click(function(){
        window.location.href= urlPath + '/logout';
        window.localStorage.removeItem("access_token");
    })
    //菜单点击事件
    $("a[data-url]").click(function(){
        let url = $(this).attr("data-url");
        $(".card-box iframe").attr("src",url).attr("onload");
    })
})