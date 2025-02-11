package cn.wp.hpc.module.trade.convert.order;

import cn.wp.hpc.module.trade.dal.dataobject.order.TradeOrderLogDO;
import cn.wp.hpc.module.trade.service.order.bo.TradeOrderLogCreateReqBO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TradeOrderLogConvert {

    TradeOrderLogConvert INSTANCE = Mappers.getMapper(TradeOrderLogConvert.class);

    TradeOrderLogDO convert(TradeOrderLogCreateReqBO bean);

}
