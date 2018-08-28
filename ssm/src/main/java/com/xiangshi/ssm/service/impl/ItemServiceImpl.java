package com.xiangshi.ssm.service.impl;

import com.xiangshi.ssm.dao.BaseDao;
import com.xiangshi.ssm.domain.Item;
import com.xiangshi.ssm.service.ItemService;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 *
 */
@Service("itemService")
public class ItemServiceImpl extends BaseServiceImpl<Item> implements ItemService {

  @Resource(name = "itemDao")
  public void setDao(BaseDao<Item> dao) {
    super.setDao(dao);
  }
}