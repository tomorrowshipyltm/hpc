package cn.wp.hpc.module.system.controller.app.ip;

import cn.hutool.core.lang.Assert;
import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.framework.common.util.object.BeanUtils;
import cn.wp.hpc.framework.ip.core.Area;
import cn.wp.hpc.framework.ip.core.utils.AreaUtils;
import cn.wp.hpc.module.system.controller.app.ip.vo.AppAreaNodeRespVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import java.util.List;

import static cn.wp.hpc.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 地区")
@RestController
@RequestMapping("/system/area")
@Validated
public class AppAreaController {

    @GetMapping("/tree")
    @Operation(summary = "获得地区树")
    @PermitAll
    public CommonResult<List<AppAreaNodeRespVO>> getAreaTree() {
        Area area = AreaUtils.getArea(Area.ID_CHINA);
        Assert.notNull(area, "获取不到中国");
        return success(BeanUtils.toBean(area.getChildren(), AppAreaNodeRespVO.class));
    }

}
