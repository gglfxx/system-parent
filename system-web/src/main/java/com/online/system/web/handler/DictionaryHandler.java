package com.online.system.web.handler;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.online.system.annotation.OperLog;
import com.online.system.web.entity.Dictionary;
import com.online.system.web.entity.DictionaryContent;
import com.online.system.web.entity.Result;
import com.online.system.web.service.DictService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 字典管理
 */
@RestController
@RequestMapping("/dict")
public class DictionaryHandler {

    private DictService dictService;

    @Autowired
    public void setDictService (DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping("/findDict")
    @OperLog(module = "数据字典管理",type="查询",desc="数据字典查询")
    public Result<List<Dictionary>> queryDict(HttpServletRequest request) throws JsonProcessingException {
        String dictCode = request.getParameter("dictCode");
        String dictName = request.getParameter("dictName");
        int pageNo = Integer.valueOf(request.getParameter("page"));
        int pageSize = Integer.valueOf(request.getParameter("limit"));
        Page<Dictionary> page = new Page<>(pageNo,pageSize);
        Result<List<Dictionary>> result = dictService.findDict(page,dictCode,dictName);
        return result;
    }

    @GetMapping("/findDictContent")
    @OperLog(module = "数据字典管理",type="查询",desc="数据字典内容查询")
    public Result<List<DictionaryContent>> queryDictContent(HttpServletRequest request) throws JsonProcessingException {
        String dictId = request.getParameter("dictId");
        int pageNo = Integer.parseInt(request.getParameter("page"));
        int pageSize = Integer.parseInt(request.getParameter("limit"));
        Page<DictionaryContent> page = new Page<>(pageNo,pageSize);
        Result<List<DictionaryContent>> result = dictService.findDictContent(page,dictId);
        return result;
    }

    /**
     * 数据字典新增或更新
     * @param dictionary 数字字典参数
     * @return 返回结果
     */
    @PostMapping("/addOrUpdateDict")
    @OperLog(module = "数据字典管理",type="新增",desc="新增字典")
    public Result<String> addOrUpdateDict(@RequestBody Dictionary dictionary) throws JsonProcessingException {
        Result<String> result = dictService.addOrUpdateDict(dictionary);
        return result;
    }

    @PostMapping("/enableDict")
    @OperLog(module = "数据字典管理",type="更新",desc="禁用启用字典")
    public Result<String> enableDict(HttpServletRequest request) throws JsonProcessingException {
        String id = request.getParameter("id");
        String status = request.getParameter("status");
        Result<String> result = dictService.enableDict(id,status);
        return result;
    }

    @PostMapping("/delDict")
    @OperLog(module = "数据字典管理",type="删除",desc="删除字典")
    public Result<String> delDict(HttpServletRequest request) throws JsonProcessingException {
        String id = request.getParameter("id");
        Result<String> result = dictService.delDict(id);
        return result;
    }

    @PostMapping("/queryDictDetail")
    @OperLog(module = "数据字典管理",type="查询",desc="查询字典详情")
    public Result<Dictionary> queryDictDetail(HttpServletRequest request) throws JsonProcessingException {
        String id = request.getParameter("id");
        Result<Dictionary> result = dictService.findDictById(id);
        return result;
    }

    /**
     * 数据字典内容新增或更新
     * @param dictionaryContent 字典内容实体
     * @return 返回
     */
    @PostMapping("/addOrUpdateDictContent")
    @OperLog(module = "数据字典管理",type="新增",desc="新增字典内容")
    public Result<String> addOrUpdateDictContent(@RequestBody DictionaryContent dictionaryContent) throws JsonProcessingException {
        Result<String> result = dictService.addOrUpdateDictContent(dictionaryContent);
        return result;
    }

    @PostMapping("/queryDictContentDetail")
    @OperLog(module = "数据字典管理",type="查询",desc="数据字典内容详情")
    public Result<DictionaryContent> queryDictContentDetail(HttpServletRequest request) throws JsonProcessingException {
        String id = request.getParameter("id");
        Result<DictionaryContent> result = dictService.findDictContentDetail(id);
        return result;
    }

    @PostMapping("/delDictContent")
    @OperLog(module = "数据字典管理",type="删除",desc="删除字典内容")
    public Result<String> delDictContent(HttpServletRequest request) throws JsonProcessingException {
        String id = request.getParameter("id");
        Result<String> result = dictService.delDictContent(id);
        return result;
    }
}
