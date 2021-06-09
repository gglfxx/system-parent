package com.online.system.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.annotation.OperLog;
import com.online.system.web.dao.DictContentDao;
import com.online.system.web.dao.DictDao;
import com.online.system.web.entity.Dictionary;
import com.online.system.web.entity.DictionaryContent;
import com.online.system.web.entity.Result;
import com.online.system.web.service.DictService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 字典实现类
 */
@Service
@Slf4j
public class DictServiceImpl implements DictService {

    @Resource
    private DictDao dictDao;

    @Resource
    private DictContentDao dictContentDao;

    @Override
    @OperLog
    public Result<List<Dictionary>> findDict(Page<Dictionary> page,String dictCode, String dictName) {
        int code = 0;
        String msg = "查询成功";
        Result<List<Dictionary>>  result = new Result<>();
        QueryWrapper<Dictionary> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("dict_code", dictCode);
        queryWrapper.like("dict_name",dictName);
        IPage<Dictionary> dictionaryIPage = dictDao.selectPage(page,queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setCount(dictionaryIPage.getTotal());
        result.setData(dictionaryIPage.getRecords());
        return result;
    }

    @Override
    @OperLog
    public Result<List<DictionaryContent>> findDictContent(Page<DictionaryContent> page, String dictId) {
        int code = 0;
        String msg = "查询成功";
        Result<List<DictionaryContent>>  result = new Result<>();
        QueryWrapper<DictionaryContent> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("dict_id", dictId);
        queryWrapper.orderByAsc("sort");
        IPage<DictionaryContent> dictContentPage = dictContentDao.selectPage(page,queryWrapper);
        result.setCode(code);
        result.setMsg(msg);
        result.setData(dictContentPage.getRecords());
        return result;
    }

    /**
     * 新增或更新字典
     * @param dictionary
     * @return
     */
    @Override
    @OperLog
    public Result<String> addOrUpdateDict(Dictionary dictionary) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        StringBuffer msg = new StringBuffer();
        boolean success = false;
        if(dictionary!=null){
            try {
                if(StringUtils.isNotBlank(dictionary.getId())){
                    count = dictDao.updateById(dictionary);
                    msg.append("修改");
                }else{
                    count=  dictDao.insert(dictionary);
                    msg.append("新增");
                }
                msg.append("字典");
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
    @OperLog
    public Result<String> enableDict(String id, String status) {
        Result<String> result = new Result<>();
        int code = 1;
        String msg = "更新字典状态失败！";
        boolean success = false;
        Dictionary dictionary = new Dictionary();
        dictionary.setId(id);
        dictionary.setUseFlag(Integer.valueOf(status));
        int count = 0;
        try {
            count = dictDao.updateById(dictionary);
        } catch (Exception e) {
            log.warn("更新字典状态出错！"+e.getMessage());
        }
        if(count==1){
            code =0 ;
            success = true;
            msg = "更新字典状态成功";

        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }

    @Override
    @OperLog
    public Result<String> delDict(String id) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        boolean success = false;
        String msg = "删除字典失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(success);
            result.setMsg("缺少主键无法删除字典！");
            return result;
        }
        try {
            count = dictDao.deleteById(id);
        } catch (Exception e) {
            log.warn("删除字典出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除字典成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }

    @Override
    public Result<Dictionary> findDictById(String id) {
        Result<Dictionary> result = new Result<>();
        int code = 1;
        Dictionary dictionary = null;
        boolean success = false;
        String msg = "字典查询失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(false);
            result.setData(null);
            result.setMsg("缺少主键无法查询字典！");
            return result;
        }
        try {
            dictionary = dictDao.selectById(id);
        } catch (Exception e) {
            log.warn("字典查询出错！"+e.getMessage());
        }
        if(dictionary!=null){
            code = 0;
            msg = "字典查询成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setData(dictionary);
        return result;
    }

    @Override
    @OperLog
    public Result<String> addOrUpdateDictContent(DictionaryContent dictionaryContent) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        StringBuffer msg = new StringBuffer();
        boolean success = false;
        if(dictionaryContent!=null){
            try {
                if(StringUtils.isNotBlank(dictionaryContent.getId())){
                    count = dictContentDao.updateById(dictionaryContent);
                    msg.append("修改");
                }else{
                    count=  dictContentDao.insert(dictionaryContent);
                    msg.append("新增");
                }
                msg.append("字典内容");
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
    public Result<DictionaryContent> findDictContentDetail(String id) {
        Result<DictionaryContent> result = new Result<>();
        int code = 1;
        DictionaryContent dictionaryContent = null;
        boolean success = false;
        String msg = "字典内容查询失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(false);
            result.setData(null);
            result.setMsg("缺少主键无法查询字典内容！");
            return result;
        }
        try {
            dictionaryContent = dictContentDao.selectById(id);
        } catch (Exception e) {
            log.warn("字典内容查询出错！"+e.getMessage());
        }
        if(dictionaryContent!=null){
            code = 0;
            msg = "字典内容查询成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        result.setData(dictionaryContent);
        return result;
    }

    @Override
    public Result<String> delDictContent(String id) {
        Result<String> result = new Result<>();
        int count = 0;
        int code = 1;
        boolean success = false;
        String msg = "删除字典内容失败！";
        if(StringUtils.isBlank(id)){
            result.setCode(code);
            result.setSuccess(success);
            result.setMsg("缺少主键无法删除字典内容！");
            return result;
        }
        try {
            count = dictContentDao.deleteById(id);
        } catch (Exception e) {
            log.warn("删除字典内容出错！"+e.getMessage());
        }
        if(count==1){
            code = 0;
            msg = "删除字典内容成功";
            success = true;
        }
        result.setCode(code);
        result.setMsg(msg);
        result.setSuccess(success);
        return result;
    }
}
