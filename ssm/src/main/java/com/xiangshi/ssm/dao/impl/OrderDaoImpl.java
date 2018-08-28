package com.xiangshi.ssm.dao.impl;

import com.xiangshi.ssm.dao.BaseDao;
import com.xiangshi.ssm.domain.Order;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * OrderDao
 */
@Repository
public class OrderDaoImpl extends SqlSessionDaoSupport implements BaseDao<Order> {

  public void insert(Order order) {

  }

  public void update(Order order) {

  }

  public void delete(Integer id) {

  }

  public Order selectOne(Integer id) {
    return null;
  }

  public List<Order> selectAll() {
    return null;
  }

  public List<Order> selectPage(int offset, int len) {
    return null;
  }

  public int selectCount() {
    return 0;
  }
}