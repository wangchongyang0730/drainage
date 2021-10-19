package com.sx.drainage.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sx.drainage.entity.OmTagRelPostAccountEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * ${comments}
 * 
 * @author qianmo
 * @email qianmos@163.com
 * @date 2020-08-26 17:30:40
 */
@Mapper
public interface OmTagRelPostAccountDao extends BaseMapper<OmTagRelPostAccountEntity> {
    //获取fkid
    String getFkId(@Param("accountId") String accountId,@Param("postId") String postId,@Param("projectId") String projectId);
}
