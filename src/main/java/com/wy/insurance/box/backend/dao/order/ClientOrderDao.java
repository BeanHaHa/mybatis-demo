package com.wy.insurance.box.backend.dao.order;

import com.wy.insurance.box.backend.dao.GeneralDao;
import com.wy.insurance.box.backend.model.po.ClientOrder;
import com.wy.insurance.box.backend.model.vo.PageModel;

/**
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public interface ClientOrderDao extends GeneralDao<ClientOrder, String> {
    /**
     * 投保人手机号、被保人手机号返回订单列表
     * @param mobile
     * @return
     */
    PageModel<ClientOrder> getClientOrderList(String mobile);
}
