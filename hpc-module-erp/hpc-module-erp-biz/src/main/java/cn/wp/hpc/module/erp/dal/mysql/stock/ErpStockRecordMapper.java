package cn.wp.hpc.module.erp.dal.mysql.stock;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.framework.mybatis.core.mapper.BaseMapperX;
import cn.wp.hpc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.wp.hpc.module.erp.controller.admin.stock.vo.record.ErpStockRecordPageReqVO;
import cn.wp.hpc.module.erp.dal.dataobject.stock.ErpStockRecordDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * ERP 产品库存明细 Mapper
 *
 *
 */
@Mapper
public interface ErpStockRecordMapper extends BaseMapperX<ErpStockRecordDO> {

    default PageResult<ErpStockRecordDO> selectPage(ErpStockRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ErpStockRecordDO>()
                .eqIfPresent(ErpStockRecordDO::getProductId, reqVO.getProductId())
                .eqIfPresent(ErpStockRecordDO::getWarehouseId, reqVO.getWarehouseId())
                .eqIfPresent(ErpStockRecordDO::getBizType, reqVO.getBizType())
                .likeIfPresent(ErpStockRecordDO::getBizNo, reqVO.getBizNo())
                .betweenIfPresent(ErpStockRecordDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ErpStockRecordDO::getId));
    }

}