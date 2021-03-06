package com.xj.base.controller.admin.system;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xj.base.common.JsonResult;
import com.xj.base.controller.BaseController;
import com.xj.base.entity.Sche;
import com.xj.base.entity.User;
import com.xj.base.service.IScheService;
import com.xj.base.service.specification.SimpleSpecificationBuilder;
import com.xj.base.service.specification.SpecificationOperator.Operator;
import com.xj.base.vo.ZtreeView;

@Controller
@RequestMapping("/admin/sche")
public class ScheController extends BaseController{
	
	@Autowired
	private IScheService scheService;
	

	
	@RequestMapping("/index")
	public String index() {
		return "admin/sche/index";
	}

	@RequestMapping("/list")
	@ResponseBody
	public Page<Sche> list() {
		SimpleSpecificationBuilder<Sche> builder = new SimpleSpecificationBuilder<Sche>();
		String searchText = request.getParameter("searchText");
//		if(StringUtils.isNotBlank(searchText)){
//			builder.add("name", Operator.likeAll.name(), searchText);
//			builder.add("sche_date", Operator.likeAll.name(), searchText);
//			builder.add("uid", Operator.likeAll.name(), searchText);
//			
//		}
//		Page<Sche> page = scheService.findAll(builder.generateSpecification(),getPageRequest());
		Pageable pageable = getPageRequest();
		Page<Sche> page = scheService.findByUser(pageable);
		for (Sche sche : page) {
			sche.setUname(userService.findNameById(String.valueOf(sche.getUid())));
			sche.setReuname(userService.findNameById(String.valueOf(sche.getUid())));
		}
		return page;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<Sche> list = scheService.findAll();
		map.put("list", list);
		List<User> users = userService.findAll();
		map.put("users", users);
		return "admin/sche/form";
	}
	

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id,ModelMap map) {
		Sche sche = scheService.find(id);
		map.put("sche", sche);
		List<User> users = userService.findAll();
		map.put("users", users);
		List<Sche> list = scheService.findAll();
		map.put("list", list);
		return "admin/sche/form";
	}
	
	@RequestMapping(value= {"/edit"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Sche sche,ModelMap map){
		try {
			scheService.saveOrUpdate(sche, request);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id,ModelMap map) {
		try {
			scheService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	
	
	@RequestMapping(value = "/summary/{id}", method = RequestMethod.GET)
	public String summary(@PathVariable Integer id,ModelMap map) {
		User user = (User)request.getSession().getAttribute("users");
		map.put("user", user);
		Sche sche = scheService.find(id);
		map.put("sche", sche);
		List<Sche> list = scheService.findAll();
		map.put("list", list);
		return "admin/sche/summary";
	}
	
	@RequestMapping(value= {"/summary"}, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult summary(Sche sche,ModelMap map){
		try {
			Sche dbsche = scheService.find(sche.getId());
			dbsche.setSummary(sche.getSummary());
			dbsche.setStatus(1);
			scheService.update(dbsche);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}
}
