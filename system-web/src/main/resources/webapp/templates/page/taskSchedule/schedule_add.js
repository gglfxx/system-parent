//项目地址
let urlPath = onlineSystem.contextPath(),
	jwtToken = onlineSystem.getToken(),
//主键ID
	id = onlineSystem.getUrlParam("id"),
//类型
	type = onlineSystem.getUrlParam("type");
layui.config({
	base: urlPath+"/layui/",
	version: new Date().getTime()
}).extend({
	"httpAjax" : "httpAjax"
})
layui.use(['form', 'layer', 'table','httpAjax'], function () {
	let form = layui.form,
		layer = layui.layer,
		httpAjax = layui.httpAjax,
		$ = layui.jquery;

	if(type!=='add'){
		let param = {
			id: id,
		},url = urlPath + "/schedule/queryTaskDetail";
		let res = httpAjax.POST(url,param);
		if(res!=null){
			let icon = res.code == 0 ? "1" : "2";
			let data = res.data;
			if (res.code == 0) {
				form.val("scheduleAddForm",data);
			}else{
				layer.alert(res.msg, {
					icon: icon,
					closeBtn: 0
				});
			}
		}
	}
	//提交
	form.on('submit(saveBtn)', function (data) {
		data.field.id = id;
		let param =JSON.stringify(data.field) ,
			url = urlPath + "/schedule/addOrUpdateTask";
		let res = httpAjax.AXIOS_POST(url,param);
		if(res!=null){
			if(res.code === 0) {
				layer.msg(res.msg, function () {
					var index = parent.layer.getFrameIndex(window.name);
					parent.location.reload();
					parent.layer.close(index);
				});
			} else {
				layer.alert(res.msg, {
					icon: 2,
					closeBtn: 0,
				});
			}
		}
		return false;
	});
	form.render();
})
//关闭窗口
function iframeClose() {
	onlineSystem.closeIframe();
}

