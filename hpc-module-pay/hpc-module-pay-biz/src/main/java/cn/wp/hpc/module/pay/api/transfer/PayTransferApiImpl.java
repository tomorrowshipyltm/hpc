package cn.wp.hpc.module.pay.api.transfer;

import cn.wp.hpc.framework.common.util.object.BeanUtils;
import cn.wp.hpc.module.pay.api.transfer.dto.PayTransferCreateReqDTO;
import cn.wp.hpc.module.pay.api.transfer.dto.PayTransferRespDTO;
import cn.wp.hpc.module.pay.dal.dataobject.transfer.PayTransferDO;
import cn.wp.hpc.module.pay.service.transfer.PayTransferService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 转账单 API 实现类
 *
 * @author jason
 */
@Service
@Validated
public class PayTransferApiImpl implements PayTransferApi {

    @Resource
    private PayTransferService payTransferService;

    @Override
    public Long createTransfer(PayTransferCreateReqDTO reqDTO) {
        return payTransferService.createTransfer(reqDTO);
    }

    @Override
    public PayTransferRespDTO getTransfer(Long id) {
        PayTransferDO transfer = payTransferService.getTransfer(id);
        return BeanUtils.toBean(transfer, PayTransferRespDTO.class);
    }

}
