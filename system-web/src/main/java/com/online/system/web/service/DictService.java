package com.online.system.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Dictionary;
import com.online.system.web.entity.DictionaryContent;
import com.online.system.web.entity.Result;
import java.util.List;

/**
 * 字典实现类
 */
public interface DictService {

    Result<List<Dictionary>> findDict(Page<Dictionary> page, String dictCode, String dictName);

    Result<List<DictionaryContent>> findDictContent(Page<DictionaryContent> page, String dictId);

    //新增或更新字典
    Result<String> addOrUpdateDict(Dictionary dictionary);

    //启用禁用字典
    Result<String> enableDict(String id, String status);

    //删除字典
    Result<String> delDict(String id);

    //查询字典
    Result<Dictionary> findDictById(String id);
    //新增或字典内容
    Result<String> addOrUpdateDictContent(DictionaryContent dictionaryContent);

    //查询字典内容详情
    Result<DictionaryContent> findDictContentDetail(String id);
    //删除字典内容
    Result<String> delDictContent(String id);
}
