package cn.wp.hpc.module.bpm.service.oa.listener;

import cn.wp.hpc.module.bpm.event.BpmProcessInstanceStatusEvent;
import cn.wp.hpc.module.bpm.event.BpmProcessInstanceStatusEventListener;
import cn.wp.hpc.module.bpm.service.oa.BpmOALeaveService;
import cn.wp.hpc.module.bpm.service.oa.BpmOALeaveServiceImpl;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * OA 请假单的结果的监听器实现类
 *
 *
 */
@Component
public class BpmOALeaveStatusListener extends BpmProcessInstanceStatusEventListener {

    @Resource
    private BpmOALeaveService leaveService;

    @Override
    protected String getProcessDefinitionKey() {
        return BpmOALeaveServiceImpl.PROCESS_KEY;
    }

    @Override
    protected void onEvent(BpmProcessInstanceStatusEvent event) {
        leaveService.updateLeaveStatus(Long.parseLong(event.getBusinessKey()), event.getStatus());
    }

}
