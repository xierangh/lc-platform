package com.lc.platform.jqgrid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.platform.dao.Condition;
import com.lc.platform.dao.Operation;
import com.lc.platform.dao.Order;
import com.lc.platform.dao.OrderType;
import com.lc.platform.dao.PageBean;
import com.lc.platform.dao.PageInfo;
import com.lc.platform.dao.RelateType;

public class JqGridUtil {
	public static ObjectMapper objectMapper = new ObjectMapper();
	public static Map<String, Operation> map = new HashMap<String, Operation>();
	static {
		map.put("bw", Operation.BW);
		map.put("eq", Operation.EQ);
		map.put("ne", Operation.NE);
		map.put("bn", Operation.BN);
		map.put("ew", Operation.EW);
		map.put("en", Operation.EN);
		map.put("cn", Operation.CN);
		map.put("nc", Operation.NC);
		map.put("nu", Operation.NU);
		map.put("nn", Operation.NN);
		map.put("in", Operation.IN);
		map.put("ni", Operation.NI);
		map.put("le", Operation.LE);
		map.put("lt", Operation.LT);
		map.put("ge", Operation.GE);
		map.put("gt", Operation.GT);
		map.put("bt", Operation.BETWEEN);
	}

	public static <T> PageInfo<T> getPageInfo(QueryParams queryParams){
		PageInfo<T> pageInfo = new PageInfo<T>();
		int page = queryParams.getPage()-1;
		int size = queryParams.getRows();
		PageRequest pageRequest;
		String sidx = queryParams.getSidx();
		String sord = queryParams.getSord();
		if (StringUtils.isNotEmpty(sidx)) {
			Sort sort = new Sort(Direction.fromString(sord), sidx);
			pageRequest = new PageRequest(page,size,sort);
		}else{
			pageRequest = new PageRequest(page,size);
		}
		pageInfo.setPageable(pageRequest);
		final String searchField = queryParams.getSearchField();
		final String searchString = queryParams.getSearchString();
		final Operation operation = map.get(queryParams.getSearchOper());
		final String content = queryParams.getFilters();
		Specification<T> specs =  new JqgridSpecification<T>(searchField, searchString, operation, content);
		pageInfo.setSpecs(specs);
		return pageInfo;
	}
	
	public static PageBean getPageBean(QueryParams queryParams)
			throws Exception {
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(queryParams.getPage()).setRowsPerPage(queryParams.getRows());
		String searchField = queryParams.getSearchField();
		String searchString = queryParams.getSearchString();
		Operation operation = map.get(queryParams.getSearchOper());
		String sidx = queryParams.getSidx();
		String sord = queryParams.getSord();
		if (StringUtils.isNotEmpty(searchField)) {
			pageBean.addCondition(new Condition(searchField, searchString,
					operation));
		}
		if (StringUtils.isNotEmpty(sidx)) {
			pageBean.addOrder(new Order(sidx, OrderType.valueOf(sord
					.toUpperCase())));
		}
		String content = queryParams.getFilters();
		if (StringUtils.isNotEmpty(content)) {
			Filters filters = objectMapper.readValue(content, Filters.class);
			String groupOp = filters.getGroupOp();
			for (RuleItem ruleItem : filters.getRules()) {
				pageBean.addCondition(new Condition(RelateType.valueOf(groupOp
						.toUpperCase()), ruleItem.getField(), ruleItem
						.getData(), map.get(ruleItem.getOp())));
			}
		}
		return pageBean;
	}

	@SuppressWarnings("unchecked")
	public static <T> JsonReader getJsonReader(Page<T> page){
		JsonReader jsonReader = new JsonReader();
		jsonReader.setPage(page.getNumber()+1)
		.setTotal(page.getTotalPages())
		.setRecords(page.getTotalElements());
		List<?> list = page.getContent();
		if (list.size() != 0) {
			Object obj = list.get(0);
			if (obj.getClass().isArray()) {
				for (int i = 0; i < list.size(); i++) {
					jsonReader.addRows(new RowItem(i + 1, (Object[]) list
							.get(i)));
				}
			} else {
				jsonReader.setRows((List<Object>) list);
			}
		}
		return jsonReader;
	}
	
	public static JsonReader getJsonReader(PageBean pageBean) {
		JsonReader jsonReader = new JsonReader();
		jsonReader.setPage(pageBean.getCurrentPage())
				.setTotal(pageBean.getTotalPages())
				.setRecords(pageBean.getTotalRows());
		List<?> list = pageBean.getItems();
		if (list.size() != 0) {
			Object obj = list.get(0);
			if (obj.getClass().isArray()) {
				for (int i = 0; i < list.size(); i++) {
					jsonReader.addRows(new RowItem(i + 1, (Object[]) list
							.get(i)));
				}
			} else {
				jsonReader.setRows(pageBean.getItems());
			}
		}
		return jsonReader;
	}
}
