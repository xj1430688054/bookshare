package com.xj.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.xj.base.dao.IShareDao;
import com.xj.base.dao.support.IBaseDao;
import com.xj.base.entity.Share;
import com.xj.base.entity.Role;
import com.xj.base.entity.User;
import com.xj.base.service.IShareService;
import com.xj.base.service.IRoleService;
import com.xj.base.service.support.impl.BaseServiceImpl;
import com.xj.base.vo.ZtreeView;

/**
 * <p>
 * 资源表 服务实现类
 * </p>
 *
 * @author xujian
 * @since 2020-02-28
 */
@Service
public class ShareServiceImpl extends BaseServiceImpl<Share, Integer>
		implements IShareService {

	@Autowired
	private IShareDao ShareDao;

	@Autowired
	private IRoleService roleService;
	
	

	@Override
	public IBaseDao<Share, Integer> getBaseDao() {
		return this.ShareDao;
	}



	@Override
	public void saveOrUpdate(Share Share, HttpServletRequest request) {
		User user = (User)request.getSession().getAttribute("users");
		if(Share.getId() != null){
			Share dbShare = find(Share.getId());
			dbShare.setPid(String.valueOf(user.getId()));
			dbShare.setUpdateTime(new Date());
			dbShare.setStatus(Share.getStatus());

			update(dbShare);
		}else{
			Share.setStatus("0");
			Share.setUid(String.valueOf(user.getId()));
			Share.setCreateTime(new Date());
			save(Share);
		}
	}


	
}
