package com.sx.drainage.common;

import com.sx.drainage.entity.OmModuleEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/9/3
 * Time: 9:52
 */
@Data
public class Module implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;
    private String parentId;
    private String name;
    private String type;
    private List<Module> children;

    public Module(OmModuleEntity omModuleEntity,List<String> id){
        this.id=omModuleEntity.getSysid();
        this.parentId=omModuleEntity.getParentId();
        this.name=omModuleEntity.getName();
        if(id.contains(omModuleEntity.getSysid())){
            this.type="Y";
        }else{
            this.type="N";
        }

    }
}
