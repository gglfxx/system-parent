package com.online.system.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Notice;

/**
 * 通知公告
 */
public interface NoticeDao extends BaseMapper<Notice> {

    IPage<Notice> queryNotice(Page<Notice> page, String noticeType);
}
