package com.zz.common.util;

import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 分页条件
 *
 * @author 韦笑迎
 * ===============================
 * Date：2024/12/30
 * Time：14:39
 * ================================
 */
@Data
public class Query {

    /**
     * 当前页
     */
    @NotNull(message = "当前页不能为空")
    private Integer current;

    /**
     * 每页条数
     */
    @NotNull(message = "每页条数不能为空")
    private Integer size;
 }
