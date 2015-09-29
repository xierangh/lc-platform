package com.lc.platform.system.dao;

import java.util.List;

import com.lc.platform.dao.jpa.GenericRepository;
import com.lc.platform.system.domain.Dict;

/**
 * 数据词典数据接口
 * @author chenjun
 *
 */
public interface DictDao extends GenericRepository<Dict, String>{

	List<Dict> findDictByParentId(String parentId);

}
