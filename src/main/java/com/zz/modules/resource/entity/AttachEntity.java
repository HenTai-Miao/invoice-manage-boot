package com.zz.modules.resource.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * @author 韦笑迎
 * ===============================
 * Date：2025/12/12
 * Time：11:44
 * ================================
 */
@Data
//@TableName("zz_attach")
public class AttachEntity {
    /**
     * 主键
     */
    @TableId(
            value = "id",
            type = IdType.ASSIGN_ID
    )
    private Long id;

    /**
     * 下载地址
     */
    private String link;

    /**
     * 文件存储路径
     */
    private String name;

    /**
     * 原始文件名
     */
    private String originalName;

    /**
     * 拓展名
     */
    private String extension;

    /**
     * 文件大小
     */
    private String attachSize;

    /**
     * 创建人
     */
    private String createUser;

    /**
     * 创建部门
     */
    private String createDept;

    /**
     * 创建时间
     */
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(
            fill = FieldFill.INSERT
    )
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updateUser;

    /**
     * 更新时间
     */
    @DateTimeFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss"
    )
    @TableField(
            fill = FieldFill.INSERT_UPDATE
    )
    private LocalDateTime updateTime;

    /**
     * 状态
     */
    private String status;

    /**
     * 逻辑删除标识(0:未删除 1:已删除)
     */
    @TableLogic
    @TableField(
            fill = FieldFill.INSERT
    )
    private Integer isDeleted;

}
