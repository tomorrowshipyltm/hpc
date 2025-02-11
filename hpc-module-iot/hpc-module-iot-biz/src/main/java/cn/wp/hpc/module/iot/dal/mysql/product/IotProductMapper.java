package cn.wp.hpc.module.iot.dal.mysql.product;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.framework.mybatis.core.mapper.BaseMapperX;
import cn.wp.hpc.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.wp.hpc.module.iot.controller.admin.product.vo.IotProductPageReqVO;
import cn.wp.hpc.module.iot.dal.dataobject.product.IotProductDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * IoT 产品 Mapper
 *
 * @author ahh
 */
@Mapper
public interface IotProductMapper extends BaseMapperX<IotProductDO> {

    default PageResult<IotProductDO> selectPage(IotProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<IotProductDO>()
                .likeIfPresent(IotProductDO::getName, reqVO.getName())
                .likeIfPresent(IotProductDO::getProductKey, reqVO.getProductKey())
                .orderByDesc(IotProductDO::getId));
    }

    default IotProductDO selectByProductKey(String productKey) {
        return selectOne(new LambdaQueryWrapperX<IotProductDO>().eq(IotProductDO::getProductKey, productKey));
    }

}