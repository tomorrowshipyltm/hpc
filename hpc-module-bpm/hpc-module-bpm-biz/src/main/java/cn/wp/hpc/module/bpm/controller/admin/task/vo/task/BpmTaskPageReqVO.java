package cn.wp.hpc.module.bpm.controller.admin.task.vo.task;

import cn.wp.hpc.framework.common.pojo.PageParam;
import cn.wp.hpc.framework.common.util.date.DateUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 流程任务的的分页 Request VO") // 待办、已办，都使用该分页
@Data
public class BpmTaskPageReqVO extends PageParam {

    @Schema(description = "流程任务名", example = "示例")
    private String name;

    @Schema(description = "流程分类", example = "1")
    private String category;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
