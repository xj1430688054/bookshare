package com.xj.base.controller.admin;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.xj.base.common.JsonResult;
import com.xj.base.controller.BaseController;
import com.xj.base.dao.IUserDao;
import com.xj.base.entity.Dept;
import com.xj.base.entity.Role;
import com.xj.base.entity.User;
import com.xj.base.service.IRoleService;
import com.xj.base.service.impl.RoleServiceImpl;
import com.xj.base.service.specification.SimpleSpecificationBuilder;
import com.xj.base.service.specification.SpecificationOperator.Operator;

@Controller
public class LoginController extends BaseController {


	@Autowired
	private IUserDao userDao;
	@Autowired 
	private IRoleService roleService;
	
	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.GET)
	public String login() {
		return "admin/login";
	}
	


	@RequestMapping(value = { "/admin/login" }, method = RequestMethod.POST)
	public String login(@RequestParam("username") String username,
			@RequestParam("password") String password,ModelMap model
			) {
		try {
			 Subject subject = SecurityUtils.getSubject();
			 UsernamePasswordToken token = new UsernamePasswordToken(username, password);
			subject.login(token);
//			User users = userDao.findOne(Integer.valueOf(username));
			User users = userDao.findByUserName(username);
			request.getSession().setAttribute("users", users);
			AdminIndexController.id = users.getId();
			return redirect("/admin/index");
		} catch (AuthenticationException e) {
			model.put("message", e.getMessage());
		}
		return "admin/login";
	}
	
	@RequestMapping(value = { "/admin/logout" }, method = RequestMethod.GET)
	public String logout() {
		Subject subject = SecurityUtils.getSubject();
		subject.logout();
		return redirect("admin/login");
	}
	
	@RequestMapping(value = { "/regedit" }, method = RequestMethod.GET)
	public String regedit() {
		return "admin/regedit";
	}
	@RequestMapping(value = { "/regedit" }, method = RequestMethod.POST)
	public String regedit1(User user,ModelMap map) {
		try {
			user.setLocked(0);
			user.setBirthday(new Date());
			Role find = roleService.find(3);
			Set<Role> role = new HashSet<>();
			role.add(find);
			user.setRoles(role);
			userService.saveOrUpdate(user);
		} catch (Exception e) {
			e.printStackTrace();
			throw new UnknownAccountException("注册失败");
		}
		return redirect("admin/index");
	}
	

}
