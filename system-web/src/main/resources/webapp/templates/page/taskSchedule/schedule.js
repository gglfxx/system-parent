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
		elem: "#taskTable",
		cols: [[
			{type: 'checkbox', width: 40},
			{type: 'numbers', title: '序号'},
			{field: 'id', title: 'id',hide: true},
			{field: 'taskName', title: '任务名称',width:180},
			{field: 'taskGroup', title: '任务分组',templet:function(d){
					if(d.taskGroup =='system'){
						return "系统";
					}else{
						return "默认";
					}
				},width:100},
			{field: 'taskClass', title: '调用目标字符串'},
			{field: 'taskExpress', title: '执行表达式',width:'20%'},
			{field: 'taskStatus', title: '任务状态',templet:function(d){
					let isChecked = d.taskStatus == "0" ?"checked":"";
					let id = d.id;
					return '<input type="checkbox" lay-skin="switch" value='+id+' lay-filter="taskStatus" lay-text="启用|禁用" '+ isChecked +'> ';
				},width:100},
			{field: 'createTime', title: '创建时间',width:"15%"},
			{field: '', title: '操作',toolbar: "#operate",align:'left'}
		]],
		id: "taskManage",
		toolbar: '#taskBar',
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
					url: urlPath + "/schedule/querySchedule?v=" + Math.random(),
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
		var field = form.val("taskForm");
		for (var key in field) {
			if (isreset) {
				param[key] = "";
			} else {
				param[key] = field[key];
			}
		}
		tableIns.reload({
			where: param,
			url: urlPath + "/schedule/querySchedule?v=" + Math.random(),
			page: {
				curr: 1
			},
			headers: { //通过 request 头传递
				token: jwtToken
			}
		});
		table.resize('taskManage');
	}
	/**
	 * 头部工具栏
	 */
	table.on('toolbar(taskTable)', function (obj) {
		switch (obj.event) {
			case "add":
				onlineSystem.btnPageToShow(urlPath + "/page/taskSchedule/schedule_add.html?type=add",
					"新增定时任务");
				break;
		}
	});

	//状态开关
	form.on('switch(taskStatus)', function (data) {
		//判断开关状态
		let checked = data.elem.checked,status = null;
		if(checked){
			status = 0;
		} else {
			status = 1;
		}
		let param = {
			id: data.value,
			status: status
		},url = urlPath + '/schedule/enableTask';
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

	table.on('tool(taskTable)', function (obj) {
		let data = obj.data, id = data.id;
		switch (obj.event) {
			case 'edit':
				onlineSystem.btnPageToShow(
					urlPath + "/page/taskSchedule/schedule_add.html?type=edit&id="+id,
					"修改定时任务");
				break;
			case 'delete':
				layer.confirm('确定要删除当前定时任务吗？', {
					icon: 3,
					title: "提示"
				}, function (index) {
					let param = {
						id: data.id
					},url = urlPath + '/schedule/delTaskSchedule';
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