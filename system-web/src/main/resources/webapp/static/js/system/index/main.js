//系统时间
getLangDate();
//值小于10时，在前面补0
function dateFilter(date){
	if(date < 10){return "0"+date;}
	return date;
}
function getLangDate(){
	let dateObj = new Date(); //表示当前系统时间的Date对象
	let year = dateObj.getFullYear(); //当前系统时间的完整年份值
	let month = dateObj.getMonth()+1; //当前系统时间的月份值
	let date = dateObj.getDate(); //当前系统时间的月份中的日
	let day = dateObj.getDay(); //当前系统时间中的星期值
	let weeks = ["星期日","星期一","星期二","星期三","星期四","星期五","星期六"];
	let week = weeks[day]; //根据星期值，从数组中获取对应的星期字符串
	let hour = dateObj.getHours(); //当前系统时间的小时值
	let minute = dateObj.getMinutes(); //当前系统时间的分钟值
	let second = dateObj.getSeconds(); //当前系统时间的秒钟值
	let timeValue = "" +((hour >= 12) ? (hour >= 18) ? "晚上" : "下午" : "上午" ); //当前时间属于上午、晚上还是下午
	let newDate = dateFilter(year)+"年"+dateFilter(month)+"月"+dateFilter(date)+"日 "+" "+dateFilter(hour)+":"+dateFilter(minute)+":"+dateFilter(second);
	document.getElementById("nowTime").innerHTML = "亲爱的管理员，"+timeValue+"好！ 欢迎使用在线视频管理。当前时间为： "+newDate+"　"+week;
	setTimeout("getLangDate()",1000);
}
layui.config({
	base : "layui/"
}).use(['form','element','layer','jquery'],function(){
	let form = layui.form,
		element = layui.element,
		$ = layui.jquery, urlPath = onlineSystem.contextPath(),
		jwtToken = onlineSystem.getToken();
	$(".panel a").on("click",function(){
		window.parent.addTab($(this));
	})

	//动态获取文章总数和待审核文章数量,最新文章
	$.get(urlPath+"/json/newsList.json",
		function(data){
			var waitNews = [];
			$(".allNews span").text(data.length);  //文章总数
			for(var i=0;i<data.length;i++){
				var newsStr = data[i];
				if(newsStr["newsStatus"] == "待审核"){
					waitNews.push(newsStr);
				}
			}
			$(".waitNews span").text(waitNews.length);  //待审核文章
			//加载最新文章
			var hotNewsHtml = '';
			for(var i=0;i<5;i++){
				hotNewsHtml += '<tr>'
		    	+'<td align="left">'+data[i].newsName+'</td>'
		    	+'<td>'+data[i].newsTime+'</td>'
		    	+'</tr>';
			}
			$(".hot_news").html(hotNewsHtml);
		}
	)

	//更新版本日志
	var pageNo = 1;
	var pageSize = 5;
	$.ajax({
		url:urlPath+"/version/findVersion?v=" + Math.random(),
		type:"GET",
		data:{
			page:pageNo,
			limit:pageSize,
		},
		dataType:"json",
		headers:{
			token:jwtToken
		},
		success:function(res){
			let code = res.code;
			let data = res.data;
			if(code ==0){
				let html = "<ul class='layui-timeline'>";
				if(data!=null&&data.length>0){
					let icon = '&#xe756;',length = data.length;
					for(let i=0;i<length;i++){
						if(i==length-1){
							icon = '&#xe63f;';
						}
						let li = "<li class='layui-timeline-item'>";
						li += "<i class='layui-icon layui-timeline-axis'>"+icon+"</i>";
						li += "<div class='layui-timeline-content layui-text'>";
						li +="<h3 class='layui-timeline-title'>"+data[i].systemVersion+'('+data[i].versionType+') -'+data[i].createTime+"</h3>";
						li +="<p>"+data[i].description+"</p>";
						li +="</div>";
						li +="</li>";
						html+=li;
					}
					html+="</ul>";
				}
				$(".sysVersion").html(html);
			}
		}
	})
	//图片总数
	$.get(urlPath+"/json/images.json",
		function(data){
			$(".imgAll span").text(data.length);
		}
	)

	//用户数
	$.get(urlPath+"/json/usersList.json",
		function(data){
			$(".userAll span").text(data.length);
		}
	)

	//新消息
	$.get(urlPath+"/json/message.json",
		function(data){
			$(".newMessage span").text(data.length);
		}
	)


	//数字格式化
	$(".panel span").each(function(){
		$(this).html($(this).text()>9999 ? ($(this).text()/10000).toFixed(2) + "<em>万</em>" : $(this).text());	
	})

	//系统基本参数
	if(window.sessionStorage.getItem("systemParameter")){
		var systemParameter = JSON.parse(window.sessionStorage.getItem("systemParameter"));
		fillParameter(systemParameter);
	}else{
		$.ajax({
			url : urlPath+"/json/systemParameter.json",
			type : "get",
			dataType : "json",
			success : function(data){
				fillParameter(data);
			}
		})
	}

	//填充数据方法
 	function fillParameter(data){
 		//判断字段数据是否存在
 		function nullData(data){
 			if(data == '' || data == "undefined"){
 				return "未定义";
 			}else{
 				return data;
 			}
 		}
 		$(".version").text(nullData(data.version));      //当前版本
		$(".author").text(nullData(data.author));        //开发作者
		$(".homePage").text(nullData(data.homePage));    //网站首页
		$(".server").text(nullData(data.server));        //服务器环境
		$(".dataBase").text(nullData(data.dataBase));    //数据库版本
		$(".maxUpload").text(nullData(data.maxUpload));    //最大上传限制
		$(".userRights").text(nullData(data.userRights));//当前用户权限
 	}

})
