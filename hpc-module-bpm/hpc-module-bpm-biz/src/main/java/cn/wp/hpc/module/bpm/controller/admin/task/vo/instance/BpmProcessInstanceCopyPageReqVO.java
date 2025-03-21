package cn.wp.hpc.module.bpm.controller.admin.task.vo.instance;

import cn.wp.hpc.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.wp.hpc.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 流程实例抄送的分页 Request VO")
@Data
public class BpmProcessInstanceCopyPageReqVO extends PageParam {

    @Schema(description = "流程名称", example = "示例")
    private String processInstanceName;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
