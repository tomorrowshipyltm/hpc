package cn.wp.hpc.module.report.service.goview;

import cn.wp.hpc.module.report.controller.admin.goview.vo.data.GoViewDataRespVO;

/**
 * GoView 数据 Service 接口
 *
 *
 */
public interface GoViewDataService {

    /**
     * 使用 SQL 查询数据
     *
     * @param sql SQL 语句
     * @return 数据
     */
    GoViewDataRespVO getDataBySQL(String sql);

}
