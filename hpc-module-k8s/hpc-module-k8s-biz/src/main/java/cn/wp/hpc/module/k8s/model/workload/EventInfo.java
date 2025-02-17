package cn.wp.hpc.module.k8s.model.workload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Event信息前端展示
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventInfo {
    private String type;
    private String reason;
    private String age;
    private String from;
    private String message;
}
