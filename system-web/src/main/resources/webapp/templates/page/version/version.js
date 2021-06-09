let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['form', 'layer', 'table'], function () {
    var form = layui.form,
        layer = layui.layer,
        table = layui.table,
        $ = layui.jquery;

    var tableIns = table.render({
        elem: "#versionTable",
        cols: [[
            {type: 'checkbox', width: 40},
            {type: 'numbers', title: '序号', width: "5%"},
            {field: 'id', title: 'id', hide: true},
            {field: 'systemVersion', title: '系统版本', width: "10%"},
            {field: 'versionType', title: '版本类型', width: "10%"},
            {field: 'description', title: '版本描述', width: "35%"},
            {field: 'createUser', title: '创建人', width: "10%"},
            {field: 'createTime', title: '创建时间', width: "15%"},
            {field: '', title: '操作', toolbar: "#tableBar", align: 'left'},
        ]],
        id: "versionManage",
        toolbar: '#versionBar',
        defaultToolbar: [''],
        page: true,
        limit: 10,
        limits: [10, 20, 30, 40, 50],
        jump: function (obj, first) {
            if (!first) {
                var param = {};
                var field = form.val("versionForm");
                for (var key in field) {
                    param[key] = field[key];
                }
                tableIns.reload({
                    where: param,
                    url: urlPath + "/version/findVersion?v=" + Math.random(),
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
        var field = form.val("versionForm");
        for (var key in field) {
            if (isreset) {
                param[key] = "";
            } else {
                param[key] = field[key];
            }
        }
        tableIns.reload({
            where: param,
            url: urlPath + "/version/findVersion?v=" + Math.random(),
            page: {
                curr: 1
            },
            headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('versionManage');
    }
    /**
     * 头部工具栏
     */
    table.on('toolbar(versionTable)', function (obj) {
        switch (obj.event) {
            case "add":
                onlineSystem.btnPageToShow(urlPath + "/page/version/version-add.html?type=add",
                    "新增版本信息");
                break;
        }
    });

    table.on('tool(versionTable)', function (obj) {
        var data = obj.data;
        var id = data.id;
        switch (obj.event) {
            case 'edit':
                onlineSystem.btnPageToShow(
                    urlPath + "/page/version/version-add.html?type=edit&id="+id,
                    "修改版本信息");
                break;
            case 'delete':
                layer.confirm('确定要删除当前版本信息吗？', {
                    icon: 3,
                    title: "提示"
                }, function (index) {
                    let param = {
                        id: data.id
                    }, url = urlPath + "/version/delVersion";
                    let res = httpAjax.POST(url,param);
                    if(res!=null){
                        let icon = res.code == 0 ? "1" : "2";
                        layer.alert(res.msg, {
                            icon: icon,
                            closeBtn: 0,
                        }, function (current) {
                            if (res.code == 0) {
                                renderTable();
                            }
                            layer.close(current);
                        });
                    }
                    layer.close(index);
                });
                break;
        }
        ;
    });
    form.render();
})