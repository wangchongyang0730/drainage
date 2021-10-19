package com.sx.drainage.params;

import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/15
 * Time: 11:05
 */
@Data
public class QueryParams implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String startdate;
    private String enddate;
    private Integer pageSize;
    private Integer pageIndex;
    private String sort;
    private String isasc;
    private String projectId;
    private Integer type;
}
