package cn.wp.hpc.module.member.dal.mysql.config;

import cn.wp.hpc.framework.mybatis.core.mapper.BaseMapperX;
import cn.wp.hpc.module.member.dal.dataobject.config.MemberConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 积分设置 Mapper
 *
 * @author QingX
 */
@Mapper
public interface MemberConfigMapper extends BaseMapperX<MemberConfigDO> {
}
