package cn.wp.hpc.module.mp.dal.mysql.account;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.framework.mybatis.core.mapper.BaseMapperX;
import cn.wp.hpc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.wp.hpc.module.mp.controller.admin.account.vo.MpAccountPageReqVO;
import cn.wp.hpc.module.mp.dal.dataobject.account.MpAccountDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;

@Mapper
public interface MpAccountMapper extends BaseMapperX<MpAccountDO> {

    default PageResult<MpAccountDO> selectPage(MpAccountPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MpAccountDO>()
                .likeIfPresent(MpAccountDO::getName, reqVO.getName())
                .likeIfPresent(MpAccountDO::getAccount, reqVO.getAccount())
                .likeIfPresent(MpAccountDO::getAppId, reqVO.getAppId())
                .orderByDesc(MpAccountDO::getId));
    }

    default MpAccountDO selectByAppId(String appId) {
        return selectOne(MpAccountDO::getAppId, appId);
    }

    @Select("SELECT COUNT(*) FROM mp_account WHERE update_time > #{maxUpdateTime}")
    Long selectCountByUpdateTimeGt(LocalDateTime maxUpdateTime);

}
