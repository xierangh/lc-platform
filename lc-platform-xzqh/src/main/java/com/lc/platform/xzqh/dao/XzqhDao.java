package com.lc.platform.xzqh.dao;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.lc.platform.xzqh.domain.Xzqh;

public interface XzqhDao extends JpaRepository<Xzqh, String> {
	/**
	 * 根据父节点获取子节点的行政区划数据(经过排序)
	 * @param pid 父节点ID
	 * @return
	 */
	@Query("select u from Xzqh u where u.parentId = :pid order by px")
	public List<Xzqh> findXzqhByParent(@Param("pid") String pid);

	/**
	 * 根据提供的名称模糊查询数据
	 * @param codeName 名称信息
	 * @return
	 */
	public List<Xzqh> findByCodeNameContaining(@Param("codeName") String codeName);
	
	/**
	 * 根据提供的主键集合查询具体的数据信息
	 * @param ids
	 * @return
	 */
	public List<Xzqh> findByIdIn(Collection<String> ids);
	
	/**
	 * 根据提供的编码信息查询具体的数据信息
	 * @param numberCodes
	 * @return
	 */
	public List<Xzqh> findByNumberCodeIn(Collection<String> numberCodes);
	/**
	 * 根据提供的简拼模糊查询数据
	 * @param jianpin 简拼信息
	 * @return
	 */
	public List<Xzqh> findByJianpinStartingWith(String jianpin);
	/**
	 * 根据提供的简拼模糊查询数据
	 * @param jianpin 简拼信息
	 * @return
	 */
	public List<Xzqh> findByJianpinContaining(String jianpin);
	/**
	 * 根据提供的拼音模糊查询数据
	 * @param pinyin 拼音信息
	 * @return
	 */
	public List<Xzqh> findByPinyinContaining(String pinyin);
}
