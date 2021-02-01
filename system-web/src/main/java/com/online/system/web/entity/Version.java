package com.online.system.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 版本信息
 */
@Data
@TableName("t_online_version")
@Accessors(chain = true)
public class Version extends BaseEntity{

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    @TableField(value = "version_type")
    private String versionType;

    @TableField(value = "system_version")
    private String systemVersion;

    @TableField(value = "description")
    private String description;
}
