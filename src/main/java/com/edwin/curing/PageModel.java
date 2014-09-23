package com.edwin.curing;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页数据模型
 * 
 * @author jinming.wu
 * @date 2014-8-21
 */
public class PageModel implements Serializable {

    private static final long serialVersionUID = -2373800210768940048L;

    /** 结果总数 */
    @Setter
    @Getter
    private int               recordCount;

    /** 每页大小 */
    @Setter
    @Getter
    private int               pageSize;

    /** 当前页码 */
    @Setter
    @Getter
    private int               page             = 1;

    /** 每页数据 */
    @Setter
    @Getter
    private List<?>           records;

    public int getNextStart() {

        if (page < 1) {
            return 1;
        }

        return (page - 1) * pageSize + (records != null ? records.size() : 0) + 1;
    }

    public int getPageCount() {

        if (recordCount == 0) {
            return 0;
        }

        return (recordCount + pageSize - 1) / pageSize;
    }
}
