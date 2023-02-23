package com.bright.commoncode.domain;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bright.commoncode.utils.SqlUtil;
import com.bright.commoncode.utils.StringUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * PageQuery
 *
 * @author yjj
 * @since 2022/8/1
 */
@Data
@ApiModel(value = "分页查询条件")
public class PageQuery implements Serializable {

    /**
     * 当前记录起始索引 默认值
     */
    public static final long DEFAULT_PAGE_NUM = 1;
    /**
     * 每页显示记录数 默认值 默认查全部
     */
    public static final long DEFAULT_PAGE_SIZE = 10;
    /**
     * 顺序
     */
    public static final String ASC = "asc";
    /**
     * 倒序
     */
    public static final String DESC = "desc";
    private static final long serialVersionUID = 1L;
    /**
     * 分页大小
     */
    @ApiModelProperty(value = "分页大小")
    private Long pageSize;
    /**
     * 当前页数
     */
    @ApiModelProperty(value = "当前页数")
    private Long pageNum;
    /**
     * 排序列
     */
    @ApiModelProperty(value = "排序列")
    private String orderByColumn;
    /**
     * 排序的方向desc或者asc
     */
    @ApiModelProperty(value = "排序的方向", example = "asc,desc")
    private String isAsc;

    public <T> Page<T> build() {
        Long current = ObjectUtil.defaultIfNull(getPageNum(), DEFAULT_PAGE_NUM);
        Long size = ObjectUtil.defaultIfNull(getPageSize(), DEFAULT_PAGE_SIZE);
        if (current <= 0) {
            current = DEFAULT_PAGE_NUM;
        }
        Page<T> page = new Page<>(current, size);
        OrderItem orderItem = buildOrderItem();
        if (ObjectUtil.isNotNull(orderItem)) {
            page.addOrder(orderItem);
        }
        return page;
    }

    private OrderItem buildOrderItem() {
        // 兼容前端排序类型
        if ("ascending".equals(isAsc)) {
            isAsc = ASC;
        } else if ("descending".equals(isAsc)) {
            isAsc = DESC;
        }
        if (StringUtils.isNotBlank(orderByColumn)) {
            String orderBy = SqlUtil.escapeOrderBySql(orderByColumn);
            orderBy = StringUtil.toUnderScoreCase(orderBy);
            if (ASC.equals(isAsc)) {
                return OrderItem.asc(orderBy);
            } else if (DESC.equals(isAsc)) {
                return OrderItem.desc(orderBy);
            }
        }
        return null;
    }
}
