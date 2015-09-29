package com.lc.platform.system.dao;

import java.util.List;

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

}
