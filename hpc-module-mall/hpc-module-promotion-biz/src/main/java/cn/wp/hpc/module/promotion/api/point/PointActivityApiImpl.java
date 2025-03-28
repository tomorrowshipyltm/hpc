package cn.wp.hpc.module.promotion.api.point;

import cn.wp.hpc.module.promotion.api.point.dto.PointValidateJoinRespDTO;
import cn.wp.hpc.module.promotion.service.point.PointActivityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 积分商城活动 Api 接口实现类
 *
 * @author HUIHUI
 */
@Service
@Validated
public class PointActivityApiImpl implements PointActivityApi {

    @Resource
    private PointActivityService pointActivityService;

    @Override
    public PointValidateJoinRespDTO validateJoinPointActivity(Long activityId, Long skuId, Integer count) {
        return pointActivityService.validateJoinPointActivity(activityId, skuId, count);
    }

    @Override
    public void updatePointStockDecr(Long id, Long skuId, Integer count) {
        pointActivityService.updatePointStockDecr(id, skuId, count);
    }

    @Override
    public void updatePointStockIncr(Long id, Long skuId, Integer count) {
        pointActivityService.updatePointStockIncr(id, skuId, count);
    }

}
