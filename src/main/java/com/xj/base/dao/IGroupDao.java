package com.xj.base.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.xj.base.dao.support.IBaseDao;
import com.xj.base.entity.Group;

@Repository
public interface IGroupDao  extends IBaseDao<Group, Integer>{
	
	/** 查找个人项目历史 */
	@Query(nativeQuery = true,value = "select a.* from tb_group  a  where a.id in (select gid from tb_group_user where uid = ?1)")
	List<Group> findHisUser(Integer id);
	
	
	/** 查找当前项目组的员工 */
	@Query(nativeQuery = true,value ="select a.*  from tb_group a  where a.id = (select c.gid from tb_group_user c where  c.gid in (select b.id from tb_group b where status = 0) and c.uid = ?1)")
	List<Group> findGroupByUid (Integer id);

}
