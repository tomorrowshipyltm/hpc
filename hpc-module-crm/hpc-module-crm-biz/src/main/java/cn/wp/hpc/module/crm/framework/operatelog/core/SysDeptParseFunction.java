package cn.wp.hpc.module.crm.framework.operatelog.core;

import cn.hutool.core.util.StrUtil;
import cn.wp.hpc.module.system.api.dept.DeptApi;
import cn.wp.hpc.module.system.api.dept.dto.DeptRespDTO;
import com.mzt.logapi.service.IParseFunction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * 管理员名字的 {@link IParseFunction} 实现类
 *
 * @author HUIHUI
 */
@Slf4j
@Component
public class SysDeptParseFunction implements IParseFunction {

    public static final String NAME = "getDeptById";

    @Resource
    private DeptApi deptApi;

    @Override
    public String functionName() {
        return NAME;
    }

    @Override
    public String apply(Object value) {
        if (StrUtil.isEmptyIfStr(value)) {
            return "";
        }

        // 获取部门信息
        DeptRespDTO dept = deptApi.getDept(Long.parseLong(value.toString()));
        if (dept == null) {
            log.warn("[apply][获取部门{{}}为空", value);
            return "";
        }
        return dept.getName();
    }

}
