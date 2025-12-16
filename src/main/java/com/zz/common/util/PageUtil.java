package com.zz.common.util;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

/**
 * 分页工具类
 *
 * @author 韦笑迎
 * ===============================
 * Date：2024/12/30
 * Time：14:43
 * ================================
 */
public class PageUtil {

    /**
     * 获取分页对象
     *
     * @param query 查询条件
     * @return 分页对象
     */
    public static <T> IPage<T> getPage(Query query) {
        return new Page<>(query.getCurrent(), query.getSize());
    }
}
