package com.sx.drainage.controller;

import com.sx.drainage.common.R;
import com.sx.drainage.entity.ProjectNotificationEntity;
import com.sx.drainage.service.ProjectNotificationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: QianMo
 * Date: 2020/8/28
 * Time: 17:13
 */
@Api(value = "/api/TaskMessage",description = "TaskMessage任务消息 *")
@RestController
@CrossOrigin
@RequestMapping("/api/TaskMessage")
public class TaskMessageController {

    @Autowired
    private ProjectNotificationService projectNotificationService;

    /*@GetMapping("/GetTaskList")
    @ApiOperation(value = "获取所有任务列表")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getTaskList(HttpServletRequest request){
        String  userId = (String) request.getAttribute("userId");
//        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from fn_FlowRunPageAll('" + userId + "')");
        return R.ok(1,"获取成功!","",true,null);
    }*/
    //        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from (select row_Number() over(order by isNULL(s.isRead,0) asc,m.creationDate desc) as 'Num', m.sysId,m.topic,m.sender,(select name from OM_Account where sysId=m.sender) as 'senderName',m.body,m.creationDate,s.sysId as 'DetSysId',s.isRead,(case isnull(s.isRead,0) when 0 then '未读' when 1 then '已读' else '' end) as 'isReadName' from dbo.MESSAGE m left join MESSAGE_SYSMSG s on m.sysId=s.MESSAGE_FK where s.receiverId='" + userId + "' and m.topic like '%%') a");
    @GetMapping("/GetMessageList")
    @ApiOperation(value = " 获取所有消息列表 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R getMessageList(HttpServletRequest request){
        String  userId = (String) request.getAttribute("userId");
        List<Map<String, Object>> map = projectNotificationService.getMyMessages(userId);
        return R.ok(1,"获取成功!",map,true,null);
    }
    @GetMapping("/read/{sysId}")
    @ApiOperation(value = "消息状态变更 *")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String"),
            @ApiImplicitParam(paramType = "path",name = "sysId",value = "消息主键id",required = true,dataType = "String")
    })
    public R read(@PathVariable("sysId") String sysId){
        projectNotificationService.read(sysId);
        return R.ok();
    }
    @PostMapping("/sendMessage")
    @ApiOperation(value = "报送 *")
    @ApiImplicitParam(paramType = "header",name = "Authorization",value = "用户token",required = true,dataType = "String")
    public R sendMessage(@RequestBody ProjectNotificationEntity projectNotificationEntity,HttpServletRequest request){
        String userId = (String) request.getAttribute("userId");
        projectNotificationService.sendMessage(projectNotificationEntity,userId);
        return R.ok(1,"报送成功!",null,true,null);
    }
}
