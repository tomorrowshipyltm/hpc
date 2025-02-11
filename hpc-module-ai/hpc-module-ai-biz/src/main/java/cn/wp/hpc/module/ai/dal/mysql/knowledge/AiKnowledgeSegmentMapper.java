package cn.wp.hpc.module.ai.dal.mysql.knowledge;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.framework.mybatis.core.mapper.BaseMapperX;
import cn.wp.hpc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.wp.hpc.module.ai.controller.admin.knowledge.vo.segment.AiKnowledgeSegmentPageReqVO;
import cn.wp.hpc.module.ai.dal.dataobject.knowledge.AiKnowledgeSegmentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * AI 知识库-分片 Mapper
 *
 * @author xiaoxin
 */
@Mapper
public interface AiKnowledgeSegmentMapper extends BaseMapperX<AiKnowledgeSegmentDO> {

    default PageResult<AiKnowledgeSegmentDO> selectPage(AiKnowledgeSegmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AiKnowledgeSegmentDO>()
                .eq(AiKnowledgeSegmentDO::getDocumentId, reqVO.getDocumentId())
                .eqIfPresent(AiKnowledgeSegmentDO::getStatus, reqVO.getStatus())
                .likeIfPresent(AiKnowledgeSegmentDO::getContent, reqVO.getKeyword())
                .orderByDesc(AiKnowledgeSegmentDO::getId));
    }

    default List<AiKnowledgeSegmentDO> selectListByVectorIds(List<String> vectorIdList) {
        return selectList(new LambdaQueryWrapperX<AiKnowledgeSegmentDO>()
                .in(AiKnowledgeSegmentDO::getVectorId, vectorIdList)
                .orderByDesc(AiKnowledgeSegmentDO::getId));
    }

}
