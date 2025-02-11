package cn.wp.hpc.module.promotion.api.coupon;


import cn.wp.hpc.framework.common.util.object.BeanUtils;
import cn.wp.hpc.module.promotion.api.coupon.dto.CouponRespDTO;
import cn.wp.hpc.module.promotion.api.coupon.dto.CouponUseReqDTO;
import cn.wp.hpc.module.promotion.service.coupon.CouponService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 优惠劵 API 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CouponApiImpl implements CouponApi {

    @Resource
    private CouponService couponService;

    @Override
    public List<CouponRespDTO> getCouponListByUserId(Long userId, Integer status) {
        return BeanUtils.toBean(couponService.getCouponList(userId, status), CouponRespDTO.class);
    }

    @Override
    public void useCoupon(CouponUseReqDTO useReqDTO) {
        couponService.useCoupon(useReqDTO.getId(), useReqDTO.getUserId(),
                useReqDTO.getOrderId());
    }

    @Override
    public void returnUsedCoupon(Long id) {
        couponService.returnUsedCoupon(id);
    }

    @Override
    public List<Long> takeCouponsByAdmin(Map<Long, Integer> giveCoupons, Long userId) {
        return couponService.takeCouponsByAdmin(giveCoupons, userId);
    }

    @Override
    public void invalidateCouponsByAdmin(List<Long> giveCouponIds, Long userId) {
        couponService.invalidateCouponsByAdmin(giveCouponIds, userId);
    }

}
