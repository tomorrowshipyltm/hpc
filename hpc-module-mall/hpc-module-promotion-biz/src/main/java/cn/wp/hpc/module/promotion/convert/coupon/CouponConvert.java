package cn.wp.hpc.module.promotion.convert.coupon;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.module.promotion.api.coupon.dto.CouponRespDTO;
import cn.wp.hpc.module.promotion.controller.admin.coupon.vo.coupon.CouponPageItemRespVO;
import cn.wp.hpc.module.promotion.controller.admin.coupon.vo.coupon.CouponPageReqVO;
import cn.wp.hpc.module.promotion.controller.app.coupon.vo.coupon.AppCouponPageReqVO;
import cn.wp.hpc.module.promotion.dal.dataobject.coupon.CouponDO;
import cn.wp.hpc.module.promotion.dal.dataobject.coupon.CouponTemplateDO;
import cn.wp.hpc.module.promotion.enums.coupon.CouponStatusEnum;
import cn.wp.hpc.module.promotion.enums.coupon.CouponTemplateValidityTypeEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.Collection;

/**
 * 优惠劵 Convert
 *
 *
 */
@Mapper
public interface CouponConvert {

    CouponConvert INSTANCE = Mappers.getMapper(CouponConvert.class);

    PageResult<CouponPageItemRespVO> convertPage(PageResult<CouponDO> page);

    CouponRespDTO convert(CouponDO bean);

    default CouponDO convert(CouponTemplateDO template, Long userId) {
        CouponDO couponDO = new CouponDO()
                .setTemplateId(template.getId())
                .setName(template.getName())
                .setTakeType(template.getTakeType())
                .setUsePrice(template.getUsePrice())
                .setProductScope(template.getProductScope())
                .setProductScopeValues(template.getProductScopeValues())
                .setDiscountType(template.getDiscountType())
                .setDiscountPercent(template.getDiscountPercent())
                .setDiscountPrice(template.getDiscountPrice())
                .setDiscountLimitPrice(template.getDiscountLimitPrice())
                .setStatus(CouponStatusEnum.UNUSED.getStatus())
                .setUserId(userId);
        if (CouponTemplateValidityTypeEnum.DATE.getType().equals(template.getValidityType())) {
            couponDO.setValidStartTime(template.getValidStartTime());
            couponDO.setValidEndTime(template.getValidEndTime());
        } else if (CouponTemplateValidityTypeEnum.TERM.getType().equals(template.getValidityType())) {
            couponDO.setValidStartTime(LocalDateTime.now().plusDays(template.getFixedStartTerm()));
            couponDO.setValidEndTime(LocalDateTime.now().plusDays(template.getFixedEndTerm()));
        }
        return couponDO;
    }

    CouponPageReqVO convert(AppCouponPageReqVO pageReqVO, Collection<Long> userIds);

}
