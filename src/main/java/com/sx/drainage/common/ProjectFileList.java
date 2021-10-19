package com.sx.drainage.common;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/12/22
 * Time: 11:26
 */
@Data
@AllArgsConstructor
public class ProjectFileList implements Serializable {
    private static final long serialVersionUID = 1L;

    private String projectId;
    private String fileId;

}
