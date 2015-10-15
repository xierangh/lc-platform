package com.lc.platform.system.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.Dict;

/**
 * 数据词典数据接口
 * @author chenjun
 *
 */
public interface DictDao extends GenericRepository<Dict, String>{

	@Query("select d from Dict d where d.parentId=?1 order by d.dictOrder ")
	List<Dict> findDictByParentId(String parentId);

	Page<Dict> findByParentIdOrderByCreateDateDesc(String parentId,
			Pageable pageable);

	@Modifying
	@Query("delete Dict where id like ?1")
	void deleteChildDict(String dictId);

	@Query("select d from Dict d where id like ?1 order by dictOrder")
	List<Dict> findAllDictByParentId(String dictId);
}
