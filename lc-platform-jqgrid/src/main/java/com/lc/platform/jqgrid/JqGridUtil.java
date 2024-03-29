package com.lc.platform.jqgrid;

import java.util.ArrayList;
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

	public static <T> PageInfo<T> getPageInfo(QueryParams queryParams) {
		PageInfo<T> pageInfo = new PageInfo<T>();
		int page = queryParams.getPage() - 1;
		int size = queryParams.getRows();
		PageRequest pageRequest;
		String sidx = queryParams.getSidx();
		String sord = queryParams.getSord();
		if (StringUtils.isNotEmpty(sidx)) {
			Sort sort = new Sort(Direction.fromString(sord), sidx);
			pageRequest = new PageRequest(page, size, sort);
		} else {
			pageRequest = new PageRequest(page, size);
		}
		pageInfo.setPageable(pageRequest);
		final String searchField = queryParams.getSearchField();
		final String searchString = queryParams.getSearchString();
		final Operation operation = map.get(queryParams.getSearchOper());
		final String content = queryParams.getFilters();
		Specification<T> specs = new JqgridSpecification<T>(searchField,
				searchString, operation, content);
		pageInfo.setSpecs(specs);
		return pageInfo;
	}

	public static PageBean getPageBean(QueryParams queryParams)
			throws Exception {
		PageBean pageBean = new PageBean();
		pageBean.setCurrentPage(queryParams.getPage()).setRowsPerPage(
				queryParams.getRows());
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
			if (filters == null)
				return pageBean;
			String rootGroupOp = filters.getGroupOp();
			RelateType rootRelateType = null;
			if (StringUtils.isNotEmpty(rootGroupOp)) {
				rootRelateType = RelateType.valueOf(rootGroupOp.toUpperCase());
			}

			List<GroupItem> groups = filters.getGroups();

			if (groups != null && groups.size() > 0) {
				int prefixBracketsCount = groups.size();
				String groupPrefixBrackets = "";
				for (int i = 0; i < prefixBracketsCount; i++) {
					groupPrefixBrackets += "(";
				}
				for (int i = 0; i < groups.size(); i++) {
					GroupItem groupItem = groups.get(i);
					String suGroupOp = groupItem.getGroupOp().toUpperCase();
					List<RuleItem> subRuleItems = filterRuleItem(groupItem.getRules());

					for (int j = 0; j < subRuleItems.size(); j++) {
						RuleItem subRuleItem = subRuleItems.get(j);
						Condition condition = new Condition(
								RelateType.valueOf(suGroupOp),
								subRuleItem.getField(), subRuleItem.getData(),
								map.get(subRuleItem.getOp().toLowerCase()));
						if ((groups.size() == 1 || i == groups.size() - 1)
								&& j == 0) {
							condition.setRelateType(null);
						}

						if (i == 0 && j == 0) {
							condition
									.setGroupPrefixBrackets(groupPrefixBrackets);
						}
						if (j == subRuleItems.size() - 1) {
							condition.setSuffixBrackets(true);
						}
						pageBean.addCondition(condition);
					}
				}
				List<RuleItem> rules = filterRuleItem(filters.getRules());
				for (int j = 0; j < rules.size(); j++) {
					RuleItem ruleItem = rules.get(j);
					Condition condition = new Condition(rootRelateType,
							ruleItem.getField(), ruleItem.getData(),
							map.get(ruleItem.getOp().toLowerCase()));
					if (j == rules.size() - 1) {
						condition.setSuffixBrackets(true);
					}
					if (groups.size() == 0 && j == 0) {
						condition.setRelateType(null);
					}
					pageBean.addCondition(condition);
				}
			} else {
				List<RuleItem> rules = filterRuleItem(filters.getRules());
				for (int i = 0; i < rules.size(); i++) {
					RuleItem ruleItem = rules.get(i);
					Condition condition = new Condition(rootRelateType,
							ruleItem.getField(), ruleItem.getData(),
							map.get(ruleItem.getOp().toLowerCase()));
					if (i == 0) {
						condition.setPrefixBrackets(true);
					}
					if (i == rules.size() - 1) {
						condition.setSuffixBrackets(true);
					}
					pageBean.addCondition(condition);
				}
			}

		}
		return pageBean;
	}

	/**
	 * 过滤废数据
	 * @param rules
	 * @return
	 */
	private static List<RuleItem> filterRuleItem(List<RuleItem> rules){
		List<RuleItem> filterRules = new ArrayList<RuleItem>();
		for (RuleItem ruleItem : rules) {
			if(ruleItem.getData()==null){
				continue;
			}
			filterRules.add(ruleItem);
		}
		return filterRules;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> JsonReader getJsonReader(Page<T> page) {
		JsonReader jsonReader = new JsonReader();
		jsonReader.setPage(page.getNumber() + 1).setTotal(page.getTotalPages())
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
