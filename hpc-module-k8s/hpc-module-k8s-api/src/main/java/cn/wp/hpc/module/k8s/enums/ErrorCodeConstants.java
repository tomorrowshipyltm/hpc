package cn.wp.hpc.module.k8s.enums;

import cn.wp.hpc.framework.common.exception.ErrorCode;

/**
 * k8s 错误码枚举类
 *
 * k8s 模块，使用 1-060-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== 参数配置 1-061-000-000 ==========
    ErrorCode CONFIG_NOT_EXISTS = new ErrorCode(1_061_000_001, "参数配置不存在");
    ErrorCode CONFIG_KEY_DUPLICATE = new ErrorCode(1_061_000_002, "参数配置 key 重复");
    ErrorCode CONFIG_CAN_NOT_DELETE_SYSTEM_TYPE = new ErrorCode(1_061_000_003, "不能删除类型为系统内置的参数配置");
    ErrorCode CONFIG_GET_VALUE_ERROR_IF_VISIBLE = new ErrorCode(1_061_000_004, "获取参数配置失败，原因：不允许获取不可见配置");

}
