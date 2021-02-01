let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['form', 'layer', 'table','httpAjax','laydate'], function () {
    let form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        table = layui.table,
        httpAjax = layui.httpAjax,
        $ = layui.jquery;

    var tableIns = table.render({
        elem: "#logTable",
        cols: [[
            {type: 'checkbox', width: 40},
            {type: 'numbers', title: '序号', width: "3%"},
            {field: 'id', title: '日志id', hide: true},
            {field: 'logType', title: '日志类型', width: "5%"},
            {field: 'module', title: '日志模块', width: "7%"},
            {field: 'description', title: '日志描述', width: "10%"},
            {field: 'url', title: '请求地址', width: "10%"},
            {field: 'method', title: '请求方法', width: "10%"},
            {field: 'reqParam', title: '请求参数', width: "10%"},
            {field: 'browser', title: '浏览器', width: "10%"},
            {field: 'operSys', title: '操作系统', width: "7%"},
            {field: 'version', title: '版本号', width: "7%",hide:true},
            {field: 'ip', title: '操作ip', width: "8%"},
            {field: 'username', title: '操作人', width: "7%"},
            {field: 'createTime', title: '操作时间', width: "10%"}
            ]],
        id: "logManage",
        defaultToolbar: [''],
        page: true,
        limit: 10,
        height : "full-80",
        limits: [10, 20, 30, 40, 50],
        jump: function (obj, first) {
            if (!first) {
                var param = {};
                var field = form.val("logForm");
                for (var key in field) {
                    param[key] = field[key];
                }
                tableIns.reload({
                    where: param,
                    url: urlPath + "/log/queryLog?v=" + Math.random(),
                    page: {
                        curr: obj.curr
                    },
                    headers: { //通过 request 头传递
                        token: jwtToken
                    }
                });
            }
        },
        done: function () {

        }
    });
    renderTable();
    form.on('submit(data-search-btn)', function (data) {
        renderTable();
        return false;
    });
    /*
     * 表单 清空
     */
    $("#resetbtn").click(function () {
        renderTable("reset");
    });

    function renderTable(isreset) {
        var param = {};
        var field = form.val("logForm");
        for (var key in field) {
            if (isreset) {
                param[key] = "";
            } else {
                param[key] = field[key];
            }
        }
        tableIns.reload({
            where: param,
            url: urlPath + "/log/queryLog?v=" + Math.random(),
            page: {
                curr: 1
            },
            headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('logManage');
    }
    /*
    * 时间控件
    */
    laydate.render({
        elem: '#beginTime' //指定元素
    });
    laydate.render({
        elem: '#endTime' //指定元素
    });
    form.render();
})