package cn.wp.hpc.module.promotion.dal.mysql.banner;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.framework.mybatis.core.mapper.BaseMapperX;
import cn.wp.hpc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.wp.hpc.module.promotion.controller.admin.banner.vo.BannerPageReqVO;
import cn.wp.hpc.module.promotion.dal.dataobject.banner.BannerDO;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * Banner Mapper
 *
 * @author xia
 */
@Mapper
public interface BannerMapper extends BaseMapperX<BannerDO> {

    default PageResult<BannerDO> selectPage(BannerPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BannerDO>()
                .likeIfPresent(BannerDO::getTitle, reqVO.getTitle())
                .eqIfPresent(BannerDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BannerDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BannerDO::getSort));
    }

    default void updateBrowseCount(Long id) {
        update(null, new LambdaUpdateWrapper<BannerDO>()
                .eq(BannerDO::getId, id)
                .setSql("browse_count = browse_count + 1"));
    }

    default List<BannerDO> selectBannerListByPosition(Integer position) {
        return selectList(new LambdaQueryWrapperX<BannerDO>().eq(BannerDO::getPosition, position));
    }

}
