//退出
$(function (){
    //初始化图标
    $(".signOut").click(function(){
        window.location.href= urlPath + '/logout';
        window.localStorage.removeItem("access_token");
    })
})