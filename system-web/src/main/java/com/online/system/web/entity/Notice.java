package com.online.system.web.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 通知公告实体类
 */
@Data
@TableName("t_online_notice")
@Accessors(chain = true)
public class Notice extends BaseEntity{

    @TableId(value = "id",type = IdType.UUID)
    private String id;

    @TableField(value = "title")
    private String title;

    @TableField(value = "notice_type")
    private String noticeType;

    @TableField(exist = false)
    private String noticeName;

    @TableField(value = "notice_content")
    private String noticeContent;

    @TableField(value = "is_read")
    private int isRead;
}
