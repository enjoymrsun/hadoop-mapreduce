package com.xiangshi.ssm.service.impl;

import com.xiangshi.ssm.dao.BaseDao;
import com.xiangshi.ssm.domain.Item;
import com.xiangshi.ssm.domain.Order;
import com.xiangshi.ssm.domain.User;
import com.xiangshi.ssm.service.UserService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("userService")
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

  @Resource(name = "itemDao")
  private BaseDao<Item> itemDao;

  @Resource(name = "userDao")
  public void setDao(BaseDao<User> dao) {
    super.setDao(dao);
  }

  /**
   * 长事务测试
   */
  public void longTx() {
    //插入item
    Item i = new Item();
    i.setItemName("ttt");

    Order o = new Order();
    o.setId(2);
    //
    itemDao.insert(i);

    this.delete(1);
  }

  public void save(User u) {
    if (u.getId() != null) {
      this.update(u);
    } else {
      this.insert(u);
    }
  }
}