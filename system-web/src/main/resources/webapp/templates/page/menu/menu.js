let urlPath = onlineSystem.contextPath(),
    jwtToken = onlineSystem.getToken();
layui.config({
    base: urlPath + "/layui/lay/lay-module/",
    version: new Date().getTime()
}).use(['table', 'treetable','layer'], function () {
    let $ = layui.jquery,table = layui.table,
    layer = layui.layer, treetable = layui.treetable;
    // 渲染表格
    $.ajax({
        url: urlPath+'/menu/queryAllMenu',
        headers: {
            token:jwtToken
        },
        type: 'post',
        dataType: 'json',
        success: function(res) {
            renderTable(res.data);
        }
    })
    let renderTable = function (data){
        layer.load(2);
        treetable.render({
            treeColIndex: 1,
            treeSpid: 0,
            treeIdName: 'id',
            treePidName: 'parentId',
            elem: '#menu-table',
            page: false,
            data:data,
            cols: [[
                {type: 'numbers'},
                {field: 'title', minWidth: 200, title: '权限名称'},
                {field: 'permission', title: '权限标识'},
                {field: 'href', title: '菜单url'},
                {field: 'sort', width: 80, align: 'center', title: '排序号'},
                {
                    field: 'menuType', width: 80, align: 'center', templet: function (d) {
                        let menuType = d.menuType,
                            parentId = d.parentId;
                        if (menuType === 'button') {
                            return '<span class="layui-badge layui-bg-gray">按钮</span>';
                        }else if (parentId ===0) {
                            return '<span class="layui-badge layui-bg-blue">目录</span>';
                        } else {
                            return '<span class="layui-badge-rim">菜单</span>';
                        }
                    }, title: '类型'
                },
                {templet: '#auth-state', width: 120, align: 'center', title: '操作'}
            ]],
            done: function () {
                layer.closeAll('loading');
            }
        });
    }

    $('#btn-expand').click(function () {
        treetable.expandAll('#menu-table');
    });

    $('#btn-fold').click(function () {
        treetable.foldAll('#menu-table');
    });
    //监听工具条
    table.on('tool(menu-table)', function (obj) {
        let data = obj.data,
            id = data.id,
        layEvent = obj.event;
        if (layEvent === 'del') {
            layer.confirm('确定要删除当前菜单吗？', {
                icon: 3,
                title: "提示"
            }, function (index) {
                //删除菜单
                $.ajax({
                    url: urlPath+'/menu/delMenu',
                    headers: {
                        token:jwtToken
                    },
                    type: 'post',
                    dataType: 'json',
                    success: function(res) {
                        renderTable(res.data);
                    }
                })
                layer.close(index);
            });
            layer.msg('删除' + data.id);
        } else if (layEvent === 'edit') {
            layer.msg('修改' + data.id);
        }
    });
});