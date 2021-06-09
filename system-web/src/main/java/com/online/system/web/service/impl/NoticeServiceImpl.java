package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.dao.NoticeDao;
import com.online.system.web.entity.Notice;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Version;
import com.online.system.web.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 通知公告实现类
 */
@Service
@Slf4j
public class NoticeServiceImpl implements NoticeService {

    @Resource
    private NoticeDao noticeDao;

    @Override
    public Result<List<Notice>> queryNotice(Page<Notice> page, String noticeType) {
        int code = 0;
        String msg = "查询成功";
        Result<List<Notice>>  result = new Result<>();
        IPage<Notice> noticeIPage = noticeDao.queryNotice(page,noticeType);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(noticeIPage.getTotal());
        result.setData(noticeIPage.getRecords());
        return result;
    }

    @Override
    public Result<String> addOrUpdateNotice(Notice notice) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        StringBuffer msg = new StringBuffer();
        boolean success = false;
        if(notice!=null){
            try {
                if(StringUtils.isNotBlank(notice.getId())){
                    count = noticeDao.updateById(notice);
                    msg.append("修改");
                }else{
                    count=  noticeDao.insert(notice);
                    msg.append("新增");
                }
                msg.append("公告");
            } catch (Exception e) {
                log.warn(msg.toString()+"出错！"+e.getMessage());
            }
        }
        if(count==1){
            code = 0;
            msg.append("成功");
            success = true;
        }else{
            msg.append("失败！");
        }
        result.setCode(code);
        result.setMsg(msg.toString());
        result.setSuccess(success);
        return result;
    }

    @Override
    public Result<Notice> findNoticeById(String id) {
        Result<Notice> result = new Result<>();
        int code = 1;
        Notice notice = null;
        boolean success = false;
        String msg = "公告查询失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(false);
            result.setData(null);
            result.setMsg("无法查询公告！");
            return result;
        }
        try {
            notice = noticeDao.selectById(id);
        } catch (Exception e) {
            log.warn("版本信息查询出错！"+e.getMessage());
        }
        if(notice!=null){
            code = 0;
            msg = "公告查询成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setData(notice);
        return result;
    }

    @Override
    public Result<String> delNotice(String id) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        boolean success = false;
        String msg = "删除公告信息失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(success);
            result.setMsg("无法删除公告！");
            return result;
        }
        try {
            count = noticeDao.deleteById(id);
        } catch (Exception e) {
            log.warn("删除公告出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除公告成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }
}
