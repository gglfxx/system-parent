package com.online.system.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.online.system.web.entity.Version;
import org.apache.ibatis.annotations.Param;

public interface VersionDao extends BaseMapper<Version> {
    public IPage<Version> queryVersion(Page<Version> page,@Param("versionType") String versionType);
}
