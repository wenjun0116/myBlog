package com.qiufeng.common.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * 实体父类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseModel implements Serializable {
    private static final long serialVersionUUID = 1L;

    /** 搜索参数 */
    private String searchValue;

    /** 创建者 */
    private String createBy;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 创建者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    private String remark;

    /** 开始时间 */
    @JsonIgnore
    private String startTime;

    /** 结束时间 */
    @JsonIgnore
    private String endTime;

    /** 请求参数集合 */
    private Map<String,Object> params;
}
