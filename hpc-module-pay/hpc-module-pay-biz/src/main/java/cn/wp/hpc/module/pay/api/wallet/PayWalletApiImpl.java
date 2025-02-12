package cn.wp.hpc.module.pay.api.wallet;

import cn.hutool.core.lang.Assert;
import cn.wp.hpc.module.pay.api.wallet.dto.PayWalletAddBalanceReqDTO;
import cn.wp.hpc.module.pay.dal.dataobject.wallet.PayWalletDO;
import cn.wp.hpc.module.pay.enums.wallet.PayWalletBizTypeEnum;
import cn.wp.hpc.module.pay.service.wallet.PayWalletService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 钱包 API 实现类
 *
 * 
 */
@Service
public class PayWalletApiImpl implements PayWalletApi {

    @Resource
    private PayWalletService payWalletService;

    @Override
    public void addWalletBalance(PayWalletAddBalanceReqDTO reqDTO) {
        // 创建或获取钱包
        PayWalletDO wallet = payWalletService.getOrCreateWallet(reqDTO.getUserId(), reqDTO.getUserType());
        Assert.notNull(wallet, "钱包({}/{})不存在", reqDTO.getUserId(), reqDTO.getUserType());

        // 增加余额
        PayWalletBizTypeEnum bizType = PayWalletBizTypeEnum.valueOf(reqDTO.getBizType());
        payWalletService.addWalletBalance(wallet.getId(), reqDTO.getBizId(), bizType, reqDTO.getPrice());
    }

}
