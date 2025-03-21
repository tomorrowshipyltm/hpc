package cn.wp.hpc.module.product.convert.brand;

import cn.wp.hpc.framework.common.pojo.PageResult;
import cn.wp.hpc.module.product.controller.admin.brand.vo.ProductBrandCreateReqVO;
import cn.wp.hpc.module.product.controller.admin.brand.vo.ProductBrandRespVO;
import cn.wp.hpc.module.product.controller.admin.brand.vo.ProductBrandSimpleRespVO;
import cn.wp.hpc.module.product.controller.admin.brand.vo.ProductBrandUpdateReqVO;
import cn.wp.hpc.module.product.dal.dataobject.brand.ProductBrandDO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * 品牌 Convert
 *
 *
 */
@Mapper
public interface ProductBrandConvert {

    ProductBrandConvert INSTANCE = Mappers.getMapper(ProductBrandConvert.class);

    ProductBrandDO convert(ProductBrandCreateReqVO bean);

    ProductBrandDO convert(ProductBrandUpdateReqVO bean);

    ProductBrandRespVO convert(ProductBrandDO bean);

    List<ProductBrandSimpleRespVO> convertList1(List<ProductBrandDO> list);

    List<ProductBrandRespVO> convertList(List<ProductBrandDO> list);

    PageResult<ProductBrandRespVO> convertPage(PageResult<ProductBrandDO> page);

}
