package com.sx.drainage.params;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * @Auth: 90kg
 * @Date: 2021-10-13
 * @Time: 17:19
 */
@Data
public class dsParams {
    private BigDecimal pointZ;
    private BigDecimal pointTotalZ;
    private BigDecimal currentValueZ;
    private BigDecimal pointD;
    private BigDecimal pointTotalD;
    private BigDecimal currentValueD;
    private String remark;
    private String pointName;
    private Timestamp monitorDate;

    private int sort;
}
