//退出
$(function (){
    //初始化图标
    feather.replace();
    $(".signOut").click(function(){
        window.location.href= urlPath + '/logout';
        window.localStorage.removeItem("access_token");
    })
    $(".collapse a").bind('click',function (){
        let title = $(this).find("cite").text();
        let url = $(this).attr("data-url");
        $(".main-children-page iframe").attr("src",url).attr("onload","titleBySidebar('"+title+"')");
    })
})