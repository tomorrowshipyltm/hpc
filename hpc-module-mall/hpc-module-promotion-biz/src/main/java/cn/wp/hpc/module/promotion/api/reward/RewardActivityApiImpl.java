package cn.wp.hpc.module.promotion.api.reward;

import cn.wp.hpc.module.promotion.api.reward.dto.RewardActivityMatchRespDTO;
import cn.wp.hpc.module.promotion.service.reward.RewardActivityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

/**
 * 满减送活动 API 实现类
 *
 *
 */
@Service
@Validated
public class RewardActivityApiImpl implements RewardActivityApi {

    @Resource
    private RewardActivityService rewardActivityService;

    @Override
    public List<RewardActivityMatchRespDTO> getMatchRewardActivityListBySpuIds(Collection<Long> spuIds) {
        return rewardActivityService.getMatchRewardActivityListBySpuIds(spuIds);
    }

}
