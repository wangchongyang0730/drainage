package com.sx.drainage.common;

import com.sx.drainage.entity.ProjectMonitordailyEntity;
import com.sx.drainage.entity.ProjectMonitordataEntity;
import com.sx.drainage.entity.ProjectMonitorinitEntity;
import com.sx.drainage.params.dsParams;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @Auth: 90kg
 * @Date: 2021-10-13
 * @Time: 11:48
 * 排序接口
 */
public class Sort {

    //排序方法
    public static void initSort(List<ProjectMonitorinitEntity> list) {
        String r = "[^0-9]";
        Pattern p = Pattern.compile(r);
        list.forEach(li -> {
            if (!StringUtils.isEmpty(li.getPointname())){
                li.setSort(Integer.parseInt(p.matcher(li.getPointname()).replaceAll("").trim()));
            }
        });
        list.sort(Comparator.comparing(ProjectMonitorinitEntity::getSort));
    }

    //排序方法
    public static void dataSort(List<ProjectMonitordataEntity> list) {
        String r = "[^0-9]";
        Pattern p = Pattern.compile(r);
        list.forEach(li -> {
            if (!StringUtils.isEmpty(li.getPointname())){
                li.setSort(Integer.parseInt(p.matcher(li.getPointname()).replaceAll("").trim()));
            }
        });
        list.sort(Comparator.comparing(ProjectMonitordataEntity::getSort));
    }
//    排序方法
    public static void dsSort(List<dsParams> list) {
        String r = "[^0-9]";
        Pattern p = Pattern.compile(r);
        list.forEach(li -> {
            if (!StringUtils.isEmpty(li.getPointName())){
                li.setSort(Integer.parseInt(p.matcher(li.getPointName()).replaceAll("").trim()));
            }
        });
        list.sort(Comparator.comparing(dsParams::getSort));
    }
}
