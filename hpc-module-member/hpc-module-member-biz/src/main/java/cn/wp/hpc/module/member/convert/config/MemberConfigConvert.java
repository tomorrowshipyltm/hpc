package cn.wp.hpc.module.member.convert.config;

import cn.wp.hpc.module.member.api.config.dto.MemberConfigRespDTO;
import cn.wp.hpc.module.member.controller.admin.config.vo.MemberConfigRespVO;
import cn.wp.hpc.module.member.controller.admin.config.vo.MemberConfigSaveReqVO;
import cn.wp.hpc.module.member.dal.dataobject.config.MemberConfigDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * 会员配置 Convert
 *
 * @author QingX
 */
@Mapper
public interface MemberConfigConvert {

    MemberConfigConvert INSTANCE = Mappers.getMapper(MemberConfigConvert.class);

    MemberConfigRespVO convert(MemberConfigDO bean);

    MemberConfigDO convert(MemberConfigSaveReqVO bean);

    MemberConfigRespDTO convert01(MemberConfigDO config);
}
