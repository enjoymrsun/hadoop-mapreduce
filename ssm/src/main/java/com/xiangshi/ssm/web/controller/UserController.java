package com.xiangshi.ssm.web.controller;

import com.xiangshi.ssm.domain.User;
import com.xiangshi.ssm.service.UserService;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

import javax.annotation.Resource;

/**
 */
@Controller
public class UserController {

  @Resource(name = "userService")
  private UserService us;

  /**
   * 查看全部user
   */
  public String findAll(Model m) {
    List<User> list = us.selectAll();
    m.addAttribute("allUsers", list);
    return "user/userList";
  }

  @RequestMapping(value = {"/user/findPage", "/user/findall"})
  public String findPage(Model m, @RequestParam("pn") int pn) {
    //查询总记录数
    int counts = us.selectCount();

    //每页记录数
    int recordPerPage = 5;

    //结算页数
    int pages = 0;
    if (counts % recordPerPage == 0) {
      pages = counts / recordPerPage;
    } else {
      pages = counts / recordPerPage + 1;
    }

    //偏移量
    int offset = (pn - 1) * recordPerPage;
    List<User> list = us.selectPage(offset, recordPerPage);
    m.addAttribute("allUsers", list);
    m.addAttribute("pages", pages);
    return "user/userList";
  }

  /**
   * 删除用户
   */
  @RequestMapping("/user/deleteUser")
  public String deleteUser(@RequestParam("uid") Integer uid) {
    us.delete(uid);
    return "redirect:/user/findall";
  }

  @RequestMapping("/user/hello")
  public String accessHtml() {
    System.out.println("kkkk");
    return "forward:/html/hello.html";
  }

  @RequestMapping(value = "/user/userSave", method = RequestMethod.POST)
  public String saveUser(User u) {
    us.save(u);
    return "redirect:/user/findall";
  }

  @RequestMapping("/user/toAddUserPage")
  public String toAddUserPage() {
    return "user/userEdit";
  }
}
