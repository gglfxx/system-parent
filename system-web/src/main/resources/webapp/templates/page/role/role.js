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

    let tableIns = table.render({
        elem: "#roleTable",
        cols: [[
            {type: 'checkbox', width: 40},
            {type: 'numbers', title: '序号', width: "5%"},
            {field: 'id', title: 'id', hide: true},
            {field: 'roleCode', title: '角色编码', width: "10%"},
            {field: 'roleName', title: '角色名称', width: "10%"},
            {field: 'roleDesc', title: '角色描述', width: "15%"},
            {field: 'useFlag', title: '状态', width: "8%",align:'center',templet: function (d) {
                    var id = d.id;
                    var isChecked = d.useFlag == 0 ? "checked" : "";
                    return '<input type="checkbox" lay-skin="switch" value=' + id + ' lay-filter="status" lay-text="启用|禁用" ' + isChecked + '> ';
                }
            },
            {field: 'createUser', title: '创建人', width: "10%"},
            {field: 'createTime', title: '创建时间', width: "15%"},
            {field: '', title: '操作', toolbar: "#tableBar", align: 'left'},
        ]],
        id: "roleManage",
        toolbar: '#roleBar',
        defaultToolbar: [''],
        page: true,
        limit: 10,
        limits: [10, 20, 30, 40, 50],
        jump: function (obj, first) {
            if (!first) {
                var param = {};
                var field = form.val("userForm");
                for (var key in field) {
                    param[key] = field[key];
                }
                tableIns.reload({
                    where: param,
                    url: urlPath + "/user/findUser?v=" + Math.random(),
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
        var field = form.val("userForm");
        for (var key in field) {
            if (isreset) {
                param[key] = "";
            } else {
                param[key] = field[key];
            }
        }
        tableIns.reload({
            where: param,
            url: urlPath + "/role/findRole?v=" + Math.random(),
            page: {
                curr: 1
            },headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('roleManage');
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
        },url = urlPath + '/role/enableRole';
        let res = httpAjax.POST(url,param);
        if(res!=null){
            if (res.code == 0) {
                layer.msg(res.msg, function (index) {
                    renderTable();
                });
            } else {
                layer.alert(res.msg, {
                    icon: 2,
                    closeBtn: 0,
                }, function (index) {
                    layer.close(index);
                });
            }
        }
    });
    /**
     * 头部工具栏
     */
    table.on('toolbar(roleTable)', function (obj) {
        switch (obj.event) {
            case "add":
                onlineSystem.btnPageToShow(urlPath + "/page/role/role-add.html?type=add",
                    "新增角色");
                break;
        }
    });

    table.on('tool(roleTable)', function (obj) {
        var data = obj.data;
        var id = data.id;
        switch (obj.event) {
            case 'edit':
                onlineSystem.btnPageToShow(
                    urlPath + "/page/role/role-add.html?type=edit&id="+id,
                    "修改角色");
                break;
            case 'delete':
                layer.confirm('确定要删除当前角色吗？', {
                    icon: 3,
                    title: "提示"
                }, function (index) {
                    let param = {
                        "id": id
                    },url = urlPath + "/role/delRole";
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
        };
    });
    form.render();
})