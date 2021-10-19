package com.sx.drainage.params;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2021/3/16
 * Time: 11:12
 */
@Data
public class Pipejacking implements Serializable {
    private static final long serialVersionUID = 1L;

    private Date reportTime;
}
