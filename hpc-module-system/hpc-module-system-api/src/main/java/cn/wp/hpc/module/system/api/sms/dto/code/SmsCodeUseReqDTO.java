package cn.wp.hpc.module.system.api.sms.dto.code;

import cn.wp.hpc.framework.common.validation.InEnum;
import cn.wp.hpc.framework.common.validation.Mobile;
import cn.wp.hpc.module.system.enums.sms.SmsSceneEnum;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * 短信验证码的使用 Request DTO
 *
 *
 */
@Data
public class SmsCodeUseReqDTO {

    /**
     * 手机号
     */
    @Mobile
    @NotEmpty(message = "手机号不能为空")
    private String mobile;
    /**
     * 发送场景
     */
    @NotNull(message = "发送场景不能为空")
    @InEnum(SmsSceneEnum.class)
    private Integer scene;
    /**
     * 验证码
     */
    @NotEmpty(message = "验证码")
    private String code;
    /**
     * 使用 IP
     */
    @NotEmpty(message = "使用 IP 不能为空")
    private String usedIp;

}
