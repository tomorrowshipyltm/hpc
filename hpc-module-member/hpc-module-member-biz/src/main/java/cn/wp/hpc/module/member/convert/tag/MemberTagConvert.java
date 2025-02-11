package cn.wp.hpc.module.member.convert.tag;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.module.member.controller.admin.tag.vo.MemberTagCreateReqVO;
import cn.wp.hpc.module.member.controller.admin.tag.vo.MemberTagRespVO;
import cn.wp.hpc.module.member.controller.admin.tag.vo.MemberTagUpdateReqVO;
import cn.wp.hpc.module.member.dal.dataobject.tag.MemberTagDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 会员标签 Convert
 *
 * @author 芋道源码
 */
@Mapper
public interface MemberTagConvert {

    MemberTagConvert INSTANCE = Mappers.getMapper(MemberTagConvert.class);

    MemberTagDO convert(MemberTagCreateReqVO bean);

    MemberTagDO convert(MemberTagUpdateReqVO bean);

    MemberTagRespVO convert(MemberTagDO bean);

    List<MemberTagRespVO> convertList(List<MemberTagDO> list);

    PageResult<MemberTagRespVO> convertPage(PageResult<MemberTagDO> page);

}
