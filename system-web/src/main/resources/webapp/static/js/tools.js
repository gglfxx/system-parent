let onlineSystem = {
    //项目地址
    contextPath: function () {
        //获取请求协议
        let httpHead = window.location.protocol;
        //获取域名
        let domainName = window.location.host;
        //获取端口号
        let httpPost = window.location.port;
        //获取项目全路径
        let httpName = document.location.pathname;
        let index = httpName.substr(1).indexOf("/");
        httpName = httpName.substr(0, index + 1);
        let urlPath = httpHead + "//" + domainName + httpName;
        return urlPath;
    },
    //获取身份证的性别 出生年月 年龄
    idNumVentify: function analyzeIDCard(IDCard) {
        var sexAndAge = {};
        //获取用户身份证号码
        var userCard = IDCard;
        //如果身份证号码为undefind则返回空
        if (!userCard) {
            return sexAndAge;
        }
        //获取性别
        if (parseInt(userCard.substr(16, 1)) % 2 == 1) {
            sexAndAge.sex = '1'
        } else {
            sexAndAge.sex = '2'
        }
        //获取出生年月日
        var yearBirth = userCard.substring(6, 10);
        var monthBirth = userCard.substring(10, 12);
        var dayBirth = userCard.substring(12, 14);
        //获取当前年月日并计算年龄
        var myDate = new Date();
        var monthNow = myDate.getMonth() + 1;
        var dayNow = myDate.getDay();
        var age = myDate.getFullYear() - yearBirth;
        if (monthNow < monthBirth
            || (monthNow == monthBirth && dayNow < dayBirth)) {
            age--;
        }
        sexAndAge.birthday = yearBirth + '-' + monthBirth + "-" + dayBirth;
        //得到年龄
        sexAndAge.age = age;
        //返回性别和年龄
        return sexAndAge;
    },
    //获取参数
    getUrlParam: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
        var r = window.location.search.substr(1).match(reg);  //匹配目标参数
        if (r != null) return unescape(r[2]);
        return null; //返回参数值
    },
    //获取token
    getToken:function (name){
        return this.isNull(window.localStorage.getItem("access_token"))?"":window.localStorage.getItem("access_token");
    },
    //打开页面
    btnPageToShow: function (content, title, btn) {
        layer.open({
            type: 2,
            content: content,
            area: ['100%', '100%'],
            maxmin: false,
            btn: btn,
            title: title
        })
    },
    //关闭layui窗口
    closeIframe:function(){
        let index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
        parent.layer.close(index); //再执行关闭
    },
    /*
    * 半页面
    */
    btnDivToShow: function (content, title) {
        layer.open({
            type: 2,
            content: content,
            area: ['750px', '300px'],
            maxmin: false,
            title: title
        });
    },
    //判空
    isNull:function(value){
        if(null==value||undefined ==value||""==value){
            return true
        }
        return false;
    },
    //数字转换中文
    numConvertToCn:function (value){
        let arr1 = new Array('零', '一', '二', '三', '四', '五', '六', '七', '八', '九');
        let arr2 = new Array('', '十', '百', '千', '万', '十', '百', '千', '亿', '十', '百',
            '千', '万', '十', '百', '千', '亿');//可继续追加更高位转换值
        if (!num || isNaN(num)) {
            return "零";
        }
        let english = num.toString().split("")
        let result = "";
        for (let i = 0; i < english.length; i++) {
            let des_i = english.length - 1 - i;//倒序排列设值
            result = arr2[i] + result;
            let arr1_index = english[des_i];
            result = arr1[arr1_index] + result;
        }
        //将【零千、零百】换成【零】 【十零】换成【十】
        result = result.replace(/零(千|百|十)/g, '零').replace(/十零/g, '十');
        //合并中间多个零为一个零
        result = result.replace(/零+/g, '零');
        //将【零亿】换成【亿】【零万】换成【万】
        result = result.replace(/零亿/g, '亿').replace(/零万/g, '万');
        //将【亿万】换成【亿】
        result = result.replace(/亿万/g, '亿');
        //移除末尾的零
        result = result.replace(/零+$/, '')
        //将【零一十】换成【零十】
        //result = result.replace(/零一十/g, '零十');//貌似正规读法是零一十
        //将【一十】换成【十】
        result = result.replace(/^一十/g, '十');
        return result;
    }
}