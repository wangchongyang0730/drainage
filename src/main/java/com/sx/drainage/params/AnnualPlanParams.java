package com.sx.drainage.params;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/11/16
 * Time: 18:07
 */
@ApiModel(value = "年度总计划投资额请求对象")
@Data
public class AnnualPlanParams implements Serializable {
    private static final long serialVersionUID = 1L;

    private String date;
    private Float complete;
    private Float plan;
}
