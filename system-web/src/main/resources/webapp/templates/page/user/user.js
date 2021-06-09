let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath+"/layui/",
    version: new Date().getTime()
}).extend({
    "httpAjax" : "httpAjax"
})
layui.use(['form', 'layer', 'laydate', 'table','httpAjax'], function () {
    let form = layui.form,
        layer = layui.layer,
        laydate = layui.laydate,
        httpAjax = layui.httpAjax,
        table = layui.table,
        $ = layui.jquery;

    let tableIns = table.render({
        elem: "#userTable",
        cols: [[
            {type: 'checkbox', width: 40},
            {type: 'numbers', title: '序号', width: "5%"},
            {field: 'id', title: '用户id', hide: true},
            {field: 'username', title: '用户名', width: "10%"},
            {field: 'name', title: '姓名', width: "10%"},
            {
                field: 'sex', title: '性别', width: "5%",align:'center', templet: function (d) {
                    let sex = d.sex;
                    let sexName = sex == 1 ? "男" : "女";
                    return sexName;
                }
            },
            {field: 'phone', title: '手机号', width: "8%"},
            {field: 'email', title: '用户邮箱',  width: "13%",templet:function(d){
                    return '<a style="color:#01AAED;" href="mailto:'+d.email+'">'+d.email+'</a>';
             }},
            {field: 'role_id', title: '所属角色id', hide: true},
            {field: 'description', title: '所属角色', width: "10%"},
            {field: 'useFlag', title: '状态', width: "8%",align:'center',templet: function (d) {
                    var id = d.id;
                    var isChecked = d.useFlag == 0 ? "checked" : "";
                    return '<input type="checkbox" lay-skin="switch" value=' + id + ' lay-filter="status" lay-text="启用|禁用" ' + isChecked + '> ';
                }
            },
            {field: 'createUser', title: '创建人', width: "10%"},
            {field: 'createTime', title: '创建时间', width: "10%"},
            {field: '', title: '操作', toolbar: "#tableBar", align: 'left'},
        ]],
        id: "userManage",
        toolbar: '#userBar',
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
            url: urlPath + "/user/findUser?v=" + Math.random(),
            page: {
                curr: 1
            },
            headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('userManage');
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
        },url = urlPath + '/user/enableUser';
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
    table.on('toolbar(userTable)', function (obj) {
        switch (obj.event) {
            case "add":
                onlineSystem.btnPageToShow(urlPath + "/page/user/user-add.html?type=add",
                    "新增用户");
                break;
        }
    });

    table.on('tool(userTable)', function (obj) {
        let data = obj.data;
        let id = data.id;
        switch (obj.event) {
            case 'modifypassword':
                onlineSystem.btnPageToShow(
                    getPath() + "/system/userManage/static/changePlatPassword.html?id="
                    + id + "&username=" + username, "修改用户密码", "");
                break;
            case 'edit':
                onlineSystem.btnPageToShow(
                    urlPath + "/page/user/user-add.html?type=edit&id="+id,
                    "修改用户");
                break;
            case 'delete':
                layer.confirm('确定要删除当前用户吗？', {
                    icon: 3,
                    title: "提示"
                }, function (index) {
                    let param = {
                        "id": id
                    },url = urlPath + "/user/delUser";
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