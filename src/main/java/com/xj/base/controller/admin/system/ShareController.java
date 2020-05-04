package com.xj.base.controller.admin.system;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.xj.base.common.JsonResult;
import com.xj.base.config.WebMvcConfig;
import com.xj.base.controller.BaseController;
import com.xj.base.entity.Share;
import com.xj.base.service.IShareService;
import com.xj.base.service.specification.SimpleSpecificationBuilder;
import com.xj.base.service.specification.SpecificationOperator.Operator;
import com.xj.base.vo.ZtreeView;

@Controller
@RequestMapping("/admin/share")
public class ShareController extends BaseController {
	@Autowired
	private IShareService shareService;

	@RequestMapping("/index")
	public String index() {
		return "admin/share/index";
	}

	@RequestMapping("/list")
	@ResponseBody
	public Page<Share> list() {
		SimpleSpecificationBuilder<Share> builder = new SimpleSpecificationBuilder<Share>();
		String searchText = request.getParameter("searchText");
		if (StringUtils.isNotBlank(searchText)) {
			builder.add("name", Operator.likeAll.name(), searchText);
		}
		Page<Share> page = shareService.findAll(builder.generateSpecification(), getPageRequest());
		for (Share Share : page) {
			// 给批改人赋值
			if (null != Share.getPid()) {
				Share.setPname(userService.findNameById(Share.getPid()));
			}
			// 给申请人赋值
			if (null != Share.getUid()) {
				Share.setUname(userService.findNameById(Share.getUid()));
			}
		}
		return page;
	}

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(ModelMap map) {
		List<Share> list = shareService.findAll();
		map.put("list", list);
		return "admin/share/form";
	}

	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public String edit(@PathVariable Integer id, ModelMap map) {
		Share share = shareService.find(id);
		map.put("share", share);

		List<Share> list = shareService.findAll();
		map.put("list", list);
		return "admin/share/form";
	}

	@RequestMapping(value = { "/edit" }, method = RequestMethod.POST)
	@ResponseBody
	public JsonResult edit(Share Share, ModelMap map) {

		try {
			shareService.saveOrUpdate(Share, request);
		} catch (Exception e) {
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult delete(@PathVariable Integer id, ModelMap map) {
		try {
			shareService.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
			return JsonResult.failure(e.getMessage());
		}
		return JsonResult.success();
	}

	@PostMapping("/upload")
	public String  upload(@RequestParam("file") MultipartFile file, Model model, Share Share) {
		if (file.isEmpty()) {
		}
		String fileName = file.getOriginalFilename();
		String fileF = fileName.substring(fileName.lastIndexOf("."), fileName.length());// 文件后缀
		fileName = new Date().getTime() + "_" + new Random().nextInt(1000) + fileF;// 新的文件名

		File dest = null;
		String os = System.getProperty("os.name");
		System.out.println(os);
		String path = (WebMvcConfig.URL).replaceAll("file:", "");
		System.out.println(path);
		dest = new File(path + fileName);

		model.addAttribute("src", "img/" + fileName);
		try {
			file.transferTo(dest);
			if (null != Share.getId()) {
				Share = shareService.find(Share.getId());
			}
			Share.setUrl("img/" + fileName);
			model.addAttribute("Share", Share);
			return "admin/share/form";
		} catch (IOException e) {
			e.printStackTrace();
			return null;

		}
		
	}
}
