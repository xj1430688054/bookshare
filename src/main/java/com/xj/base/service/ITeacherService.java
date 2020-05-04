package com.xj.base.service;

import com.xj.base.entity.Share;
import com.xj.base.service.support.IBaseService;

public interface ITeacherService extends IBaseService<Share, Integer>{

	String saveOrUpdate(Share Share);

	void grant(Integer id, String[] roleIds);

}
