let urlPath = onlineSystem.contextPath(), jwtToken = onlineSystem.getToken();
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
		elem: "#propTable",
		cols: [[
			{type: 'checkbox', width: 40},
			{type: 'numbers', title: '序号'},
			{field: 'id', title: 'id',hide: true},
			{field: 'sysCode', title: '配置代码',width:'10%'},
			{field: 'sysName', title: '配置名称',width:'20%'},
			{field: 'sysVal', title: '参数值',width:'20%'},
			{field: 'createTime', title: '创建时间',width:"15%"},
			{field: '', title: '操作',toolbar: "#operate",align:'left'}
		]],
		id: "propManage",
		toolbar: '#propBar',
		defaultToolbar: [''],
		page: true,
		limit: 10,
		limits: [10, 20, 30, 40, 50],
		jump: function (obj, first) {
			if (!first) {
				var param = {};
				var field = form.val("taskForm");
				for (var key in field) {
					param[key] = field[key];
				}
				tableIns.reload({
					where: param,
					url: urlPath + "/config/querySysProp?v=" + Math.random(),
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
		var field = form.val("propForm");
		for (var key in field) {
			if (isreset) {
				param[key] = "";
			} else {
				param[key] = field[key];
			}
		}
		tableIns.reload({
			where: param,
			url: urlPath + "/config/querySysProp?v=" + Math.random(),
			page: {
				curr: 1
			},
			headers: { //通过 request 头传递
				token: jwtToken
			}
		});
		table.resize('propManage');
	}
	/**
	 * 头部工具栏
	 */
	table.on('toolbar(propTable)', function (obj) {
		switch (obj.event) {
			case "add":
				onlineSystem.btnPageToShow(urlPath + "/page/sysProperties/properties_add.html?type=add",
					"新增定时任务");
				break;
		}
	});

	table.on('tool(propTable)', function (obj) {
		let data = obj.data, id = data.id;
		switch (obj.event) {
			case 'edit':
				onlineSystem.btnPageToShow(
					urlPath + "/page/sysProperties/properties_add.html?type=edit&id="+id,
					"修改定时任务");
				break;
			case 'delete':
				layer.confirm('确定要删除当前定时任务吗？', {
					icon: 3,
					title: "提示"
				}, function (index) {
					let param = {
						id: data.id
					},url = urlPath + '/config/delProp';
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