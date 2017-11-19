package com.wy.insurance.box.backend.service.order;

import com.wy.insurance.box.backend.dao.order.ClientOrderDao;
import com.wy.insurance.box.backend.model.po.ClientOrder;
import com.wy.insurance.box.backend.service.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
@Service
public class ClientOrderService extends AbstractService<ClientOrder, String, ClientOrderDao> {

    @Autowired
    private ClientOrderDao clientOrderDao;


    @Override
    protected ClientOrderDao getDao() {
        return clientOrderDao;
    }


    public ClientOrder getOrderByUUid(String uuid) {
        return getDao().get(uuid);
    }
}
