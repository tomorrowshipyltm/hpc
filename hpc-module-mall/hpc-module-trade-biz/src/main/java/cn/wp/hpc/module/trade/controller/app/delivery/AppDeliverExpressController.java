package cn.wp.hpc.module.trade.controller.app.delivery;

import cn.wp.hpc.framework.common.enums.CommonStatusEnum;
import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.module.trade.controller.app.delivery.vo.express.AppDeliveryExpressRespVO;
import cn.wp.hpc.module.trade.convert.delivery.DeliveryExpressConvert;
import cn.wp.hpc.module.trade.dal.dataobject.delivery.DeliveryExpressDO;
import cn.wp.hpc.module.trade.service.delivery.DeliveryExpressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.annotation.security.PermitAll;
import java.util.Comparator;
import java.util.List;

import static cn.wp.hpc.framework.common.pojo.CommonResult.success;

@Tag(name = "用户 App - 快递公司")
@RestController
@RequestMapping("/trade/delivery/express")
@Validated
public class AppDeliverExpressController {

    @Resource
    private DeliveryExpressService deliveryExpressService;

    @GetMapping("/list")
    @Operation(summary = "获得快递公司列表")
    @PermitAll
    public CommonResult<List<AppDeliveryExpressRespVO>> getDeliveryExpressList() {
        List<DeliveryExpressDO> list = deliveryExpressService.getDeliveryExpressListByStatus(CommonStatusEnum.ENABLE.getStatus());
        list.sort(Comparator.comparing(DeliveryExpressDO::getSort));
        return success(DeliveryExpressConvert.INSTANCE.convertList03(list));
    }

}
