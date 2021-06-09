package com.online.system.web.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Notice;
import com.online.system.web.entity.Result;
import com.online.system.web.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 通知公告
 */
@RestController
@RequestMapping("/notice")
public class NoticeHandler {

    private NoticeService noticeService;

    @Autowired
    public void setNoticeService (NoticeService noticeService) {
        this.noticeService = noticeService;
    }

    @RequestMapping("/findNotice")
    public Result<List<Notice>> findNotice(HttpServletRequest request){
        Result<List<Notice>> result;
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        Page<Notice> page = new Page<>(pageNo,pageSize);
        String noticeType = request.getParameter("noticeType");
        result = noticeService.queryNotice(page,noticeType);
        return result;
    }

    /**
     * 新增或更改公告
     * @param notice 公告
     * @return 返回结果
     */
    @PostMapping("/addOrUpdateNotice")
    public Result<String> addOrUpdateNotice(@RequestBody Notice notice){
        Result<String> result = noticeService.addOrUpdateNotice(notice);
        return result;
    }

    /**
     * 根据主键查询版本信息
     * @param request 参数
     * @return 返回值
     */
    @PostMapping("/queryNoticeDetail")
    public Result<Notice> queryNoticeDetatil(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<Notice> result = noticeService.findNoticeById(id);
        return result;
    }

    /**
     * 根据主键删除公告
     * @param request 参数
     * @return 返回值
     */
    @PostMapping("/delNotice")
    public Result<String> delNotice(HttpServletRequest request){
        String id = request.getParameter("id");
        Result<String> result = noticeService.delNotice(id);
        return result;
    }
}
