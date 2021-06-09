/**封装ajax请求*/
layui.define(["jquery"],function(exports){
    let $ = layui.jquery,loadIndex,
        httpAjax = {
            //get请求
            GET : function(url,param) {
                let result = null;
                $.ajax({
                    url: url,
                    data: param,
                    headers: {
                        token:jwtToken
                    },
                    type: "GET",
                    async: false,
                    dataType: 'json',
                    success: function (res) {
                        if(res!=null){
                            console.log(res);
                            let code = res.code;
                            if(code==1001){
                                window.location.href=urlPath+"/login.html";
                            }else{
                                result = res;
                            }
                        }
                    }
                });
                return result;
            },
            POST : function(url,param) {
                let result = null;
                $.ajax({
                    url: url,
                    data: param,
                    headers: {
                        token:jwtToken
                    },beforeSend:function(){
                        loadIndex= layer.msg('正在提交', {icon: 16, shade: 0.3, time:0});
                    },
                    complete: function () {
                        layer.close(loadIndex);
                    },
                    type: "POST",
                    async: false,
                    dataType: 'json',
                    success: function (res) {
                        if(res!=null){
                            console.log(res);
                            let code = res.code;
                            if(code==1001){
                                window.location.href=urlPath+"/login.html";
                            }else{
                                result = res;
                            }
                        }
                    }
                });
                return result;
            },
            //post请求
            AXIOS_POST : function(url,param) {
                let result = null;
                $.ajax({
                    contentType:"application/json",
                    url: url,
                    data: param,
                    headers: {
                        token:jwtToken
                    },beforeSend:function(){
                        loadIndex= layer.msg('正在提交', {icon: 16, shade: 0.3, time:0});
                    },
                    complete: function () {
                        layer.close(loadIndex);
                    },
                    type: "POST",
                    async: false,
                    dataType: 'json',
                    success: function (res) {
                        if(res!=null){
                            let code = res.code;
                            if(code==1001){
                                window.location.href=urlPath+"/login.html";
                            }else{
                                result = res;
                            }
                        }
                    }
                });
                return result;
            }
        };
    exports("httpAjax",httpAjax);
})