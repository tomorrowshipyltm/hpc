package cn.wp.hpc.module.bpm.framework.flowable.core.candidate.strategy.dept;

import cn.wp.hpc.framework.common.util.string.StrUtils;
import cn.wp.hpc.module.bpm.framework.flowable.core.candidate.BpmTaskCandidateStrategy;
import cn.wp.hpc.module.bpm.framework.flowable.core.enums.BpmTaskCandidateStrategyEnum;
import cn.wp.hpc.module.system.api.dept.DeptApi;
import cn.wp.hpc.module.system.api.dept.dto.DeptRespDTO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

import static cn.wp.hpc.framework.common.util.collection.CollectionUtils.convertSet;

/**
 * 部门的负责人 {@link BpmTaskCandidateStrategy} 实现类
 *
 * @author kyle
 */
@Component
public class BpmTaskCandidateDeptLeaderStrategy implements BpmTaskCandidateStrategy {

    @Resource
    private DeptApi deptApi;

    @Override
    public BpmTaskCandidateStrategyEnum getStrategy() {
        return BpmTaskCandidateStrategyEnum.DEPT_LEADER;
    }

    @Override
    public void validateParam(String param) {
        Set<Long> deptIds = StrUtils.splitToLongSet(param);
        deptApi.validateDeptList(deptIds);
    }

    @Override
    public Set<Long> calculateUsers(String param) {
        Set<Long> deptIds = StrUtils.splitToLongSet(param);
        List<DeptRespDTO> depts = deptApi.getDeptList(deptIds);
        return convertSet(depts, DeptRespDTO::getLeaderUserId);
    }

}