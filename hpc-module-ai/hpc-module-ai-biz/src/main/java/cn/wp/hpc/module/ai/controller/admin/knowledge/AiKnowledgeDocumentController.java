package cn.wp.hpc.module.ai.controller.admin.knowledge;

import cn.wp.hpc.framework.common.pojo.CommonResult;
import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.framework.common.util.object.BeanUtils;
import cn.wp.hpc.module.ai.controller.admin.knowledge.vo.document.AiKnowledgeDocumentPageReqVO;
import cn.wp.hpc.module.ai.controller.admin.knowledge.vo.document.AiKnowledgeDocumentRespVO;
import cn.wp.hpc.module.ai.controller.admin.knowledge.vo.document.AiKnowledgeDocumentUpdateReqVO;
import cn.wp.hpc.module.ai.controller.admin.knowledge.vo.knowledge.AiKnowledgeDocumentCreateReqVO;
import cn.wp.hpc.module.ai.dal.dataobject.knowledge.AiKnowledgeDocumentDO;
import cn.wp.hpc.module.ai.service.knowledge.AiKnowledgeDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static cn.wp.hpc.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - AI 知识库文档")
@RestController
@RequestMapping("/ai/knowledge/document")
@Validated
public class AiKnowledgeDocumentController {

    @Resource
    private AiKnowledgeDocumentService documentService;

    @PostMapping("/create")
    @Operation(summary = "新建文档")
    public CommonResult<Long> createKnowledgeDocument(@Valid AiKnowledgeDocumentCreateReqVO reqVO) {
        Long knowledgeDocumentId = documentService.createKnowledgeDocument(reqVO);
        return success(knowledgeDocumentId);
    }

    @GetMapping("/page")
    @Operation(summary = "获取文档分页")
    public CommonResult<PageResult<AiKnowledgeDocumentRespVO>> getKnowledgeDocumentPage(@Valid AiKnowledgeDocumentPageReqVO pageReqVO) {
        PageResult<AiKnowledgeDocumentDO> pageResult = documentService.getKnowledgeDocumentPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AiKnowledgeDocumentRespVO.class));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文档")
    public CommonResult<Boolean> updateKnowledgeDocument(@Valid @RequestBody AiKnowledgeDocumentUpdateReqVO reqVO) {
        documentService.updateKnowledgeDocument(reqVO);
        return success(true);
    }

}
