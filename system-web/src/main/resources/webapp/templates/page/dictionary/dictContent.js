let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken(),
 dictId = onlineSystem.getUrlParam("id");
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
        elem: "#dictContentTable",
        cols: [[
            {type: 'checkbox'},
            {title:'序号', type: 'numbers', width: 60},
            {field:'id', title: '主键ID',hide: true},
            {field:'code', title: '编码', align:'center'},
            {field:'name', title: '名称', align:'center'},
            {field:'sort', title: '排序', align:'center'},
            {fixed:'right', title:'操作', toolbar: '#operate', width:200, align:'center'}
        ]],
        id: "dictContentManage",
        toolbar: '#dictContentBar',
        defaultToolbar: [''],
        page: true,
        limit: 10,
        limits: [10, 20, 30, 40, 50],
        jump: function (obj, first) {
            if (!first) {
                var param = {};
                param.dictId = dictId;
                tableIns.reload({
                    where: param,
                    url: urlPath + "/dict/findDictContent?v=" + Math.random(),
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
    function renderTable(isreset) {
        var param = {};
        param.dictId = dictId;
        tableIns.reload({
            where: param,
            url: urlPath + "/dict/findDictContent?v=" + Math.random(),
            page: {
                curr: 1
            },
            headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('dictContentManage');
    }

    /**
     * 头部工具栏
     */
    table.on('toolbar(dictContentTable)', function (obj) {
        switch (obj.event) {
            case "add":
                onlineSystem.btnDivToShow(urlPath + "/page/dictionary/dictContent-add.html?type=add&dictId="+dictId,
                    "新增字典内容");
                break;
        }
    });

    table.on('tool(dictContentTable)', function (obj) {
        let data = obj.data,id = data.id;
        switch (obj.event) {
            case 'edit':
                onlineSystem.btnDivToShow(urlPath + "/page/dictionary/dictContent-add.html?type=edit&dictId="+dictId+"&id="+id,
                    "修改字典内容");
                break;
            case 'delete':
                layer.confirm('确定要删除当前字典内容吗？', {
                    icon: 3,
                    title: "提示"
                }, function (index) {
                    let param = {
                        id: data.value,
                        status: status
                    },url = urlPath + "/dict/delDictContent";
                    let res = httpAjax.POST(url,param);
                    if(res!=null){
                        var icon = res.code == 0 ? "1" : "2";
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