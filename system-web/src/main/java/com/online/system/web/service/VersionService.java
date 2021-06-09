package com.online.system.web.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Result;
import com.online.system.web.entity.Version;

import java.util.List;

/**
 * 版本Service
 */
public interface VersionService {

    /**
     * 查询
     * @param page
     * @param versionType
     * @return
     */
    Result<List<Version>> queryVersion(Page<Version> page, String versionType);

    /**
     * 新增或更新版本
     * @param version
     * @return
     */
    Result<String> addOrUpdateVersion(Version version);

    /**
     * 根据id查询版本信息
     * @param id
     * @return
     */
    Result<Version> findVersionById(String id);

    /**
     * 根据主键删除版本信息
     * @param id
     * @return
     */
    Result<String> delVersion(String id);
}
