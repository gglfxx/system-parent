package com.online.system.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Notice;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Version;

import java.util.List;

/**
 * 通知公告
 */
public interface NoticeService {

    /**
     * 查询公告
     * @param page
     * @param noticeType
     * @return
     */
    Result<List<Notice>> queryNotice(Page<Notice> page, String noticeType);

    /**
     * 新增或修改公告
     * @param notice
     * @return
     */
    Result<String> addOrUpdateNotice(Notice notice);

    /**
     * 根据主键查询公告
     * @param id
     * @return
     */
    Result<Notice> findNoticeById(String id);

    /**
     * 根据主键删除公告
     * @param id
     * @return
     */
    Result<String> delNotice(String id);
}
