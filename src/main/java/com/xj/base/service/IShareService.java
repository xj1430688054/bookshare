package com.xj.base.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.xj.base.entity.Share;
import com.xj.base.entity.Resource;
import com.xj.base.service.support.IBaseService;
import com.xj.base.vo.ZtreeView;

/**
 * <p>
 * 资源服务类
 * </p>
 *
 * @author xujian
 * @since 2020-02-28
 */
public interface IShareService extends IBaseService<Share, Integer> {

	void saveOrUpdate(Share Share, HttpServletRequest request);


}
