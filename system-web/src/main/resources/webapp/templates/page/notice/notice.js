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
        elem: "#noticeTable",
        cols: [[
            {type: 'checkbox', width: 40},
            {type: 'numbers', title: '序号', width: "5%"},
            {field: 'id', title: 'id', hide: true},
            {field: 'title', title: '标题', width: "10%"},
            {field: 'noticeName', title: '公告类型', width: "10%"},
            {field: 'noticeContent', title: '公告内容', width: "25%"},
            {field: 'isRead', title: '是否已读', width: "10%",templet:function (d){
                    let isRead = d.isRead==0?'已读':'未读';
                    let color = d.isRead==0?'green':'red';
                    return "<font color='"+color+"'>"+isRead+"</font>";
            }},
            {field: 'createUser', title: '创建人', width: "10%"},
            {field: 'createTime', title: '创建时间', width: "15%"},
            {field: '', title: '操作', toolbar: "#tableBar", align: 'left'},
        ]],
        id: "noticeManage",
        toolbar: '#noticeBar',
        defaultToolbar: [''],
        page: true,
        limit: 10,
        limits: [10, 20, 30, 40, 50],
        jump: function (obj, first) {
            if (!first) {
                var param = {};
                var field = form.val("noticeForm");
                for (var key in field) {
                    param[key] = field[key];
                }
                tableIns.reload({
                    where: param,
                    url: urlPath + "/notice/findNotice?v=" + Math.random(),
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
        var field = form.val("noticeForm");
        for (var key in field) {
            if (isreset) {
                param[key] = "";
            } else {
                param[key] = field[key];
            }
        }
        tableIns.reload({
            where: param,
            url: urlPath + "/notice/findNotice?v=" + Math.random(),
            page: {
                curr: 1
            },
            headers: { //通过 request 头传递
                token: jwtToken
            }
        });
        table.resize('noticeManage');
    }
    /**
     * 头部工具栏
     */
    table.on('toolbar(noticeTable)', function (obj) {
        switch (obj.event) {
            case "add":
                onlineSystem.btnPageToShow(urlPath + "/page/notice/notice-add.html?type=add",
                    "新增通知公告");
                break;
        }
    });

    table.on('tool(noticeTable)', function (obj) {
        var data = obj.data;
        var id = data.id;
        switch (obj.event) {
            case 'edit':
                onlineSystem.btnPageToShow(
                    urlPath + "/page/notice/notice-add.html?type=edit&id="+id,
                    "修改通知公告");
                break;
            case 'delete':
                layer.confirm('确定要删除当前通知公告吗？', {
                    icon: 3,
                    title: "提示"
                }, function (index) {
                    let param = {
                        id: data.id
                    },url = urlPath + '/notice/delNotice';
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