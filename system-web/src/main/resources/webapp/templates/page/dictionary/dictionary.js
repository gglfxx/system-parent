let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
}).use(['form', 'layer', 'table','httpAjax'], function () {
    let form = layui.form,
        layer = layui.layer,
        table = layui.table,
        httpAjax = layui.httpAjax,
        $ = layui.jquery;

    var tableIns = table.render({
        elem: "#dictTable",
        cols: [[
            {type: 'checkbox'},
            {type: 'numbers', title: '序号', width: "5%"},
            {field: 'id', title: '主键ID', hide: true},
            {field: 'dictCode', title: '字典编码', align: 'center'},
            {field: 'dictName', title: '字典名称', align: 'center'},
            {field: 'description', title: '描述', align: 'center'},
            {
                field: 'useFlag', title: '状态', align: 'center', templet: function (d) {
                    var id = d.id;
                    var isChecked = d.useFlag == 0 ? "checked" : "";
                    return '<input type="checkbox" lay-skin="switch" value=' + id + ' lay-filter="status" lay-text="启用|禁用" ' + isChecked + '> ';
                }
            },
            {field: 'createUser', title: '创建用户', align: 'center'},
            {field: 'createTime', title: '创建时间', align: 'center'},
            {fixed: 'right', title: '操作', toolbar: '#operate',align: 'center'}
        ]],
        id: "dictManage",
        toolbar: '#dictBar',
        defaultToolbar: [''],
        page: true,
        limit: 10,
        limits: [10, 20, 30, 40, 50],
        jump: function (obj, first) {
            if (!first) {
                var param = {};
                var field = form.val("dictForm");
                for (var key in field) {
                    param[key] = field[key];
                }
                tableIns.reload({
                    where: param,
                    url: urlPath + "/dict/findDict?v=" + Math.random(),
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
        var field = form.val("dictForm");
        for (var key in field) {
            if (isreset) {
                param[key] = "";
            } else {
                param[key] = field[key];
            }
        }
        tableIns.reload({
            where: param,
            url: urlPath + "/dict/findDict?v=" + Math.random(),
            page: {
                curr: 1
            },
            headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('dictManage');
    }

    //状态开关
    form.on('switch(status)', function (data) {
        var checked = data.elem.checked;//判断开关状态
        var status = null;
        if (checked) {
            status = 0;
        } else {
            status = 1;
        }
        let param = {
            id: data.value,
            status: status
        },url = urlPath + '/dict/enableDict';
        let res = httpAjax.POST(url,param);
        if(res!=null){
            if (res.code == 0) {
                layer.msg(res.msg);
            } else {
                layer.alert(res.msg, {
                    icon: 2,
                    closeBtn: 0,
                }, function (index) {
                    renderTable();
                    layer.close(index);
                });
            }
        }
    });
    /**
     * 头部工具栏
     */
    table.on('toolbar(dictTable)', function (obj) {
        switch (obj.event) {
            case "add":
                onlineSystem.btnDivToShow(urlPath + "/page/dictionary/dict-add.html?type=add",
                    "新增字典");
                break;
        }
    });

    table.on('tool(dictTable)', function (obj) {
        var data = obj.data;
        var id = data.id;
        switch (obj.event) {
            case 'edit':
                onlineSystem.btnDivToShow(
                    urlPath + "/page/dictionary/dict-add.html?type=edit&id=" + id,
                    "修改字典");
                break;
            case 'delete':
                layer.confirm('确定要删除当前字典吗？', {
                    icon: 3,
                    title: "提示"
                }, function (index) {
                    let param = {
                        "id": id
                    },url = urlPath + "/dict/delDict";
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
    //监听每行双击事件
    table.on('rowDouble(dictTable)', function (res) {
        var data = res.data;
        var id = data.id;
        var dictName = data.dictName;
        onlineSystem.btnPageToShow(
            urlPath + "/page/dictionary/dictContent.html?id=" + id,
            dictName + ">添加字典内容");
    });
    form.render();
})