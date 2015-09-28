package com.lc.platform.dao.jpa;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.QueryHints;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.lc.platform.dao.Condition;
import com.lc.platform.dao.Operation;
import com.lc.platform.dao.Order;
import com.lc.platform.dao.OrderType;
import com.lc.platform.dao.PageBean;
import com.lc.platform.dao.RelateType;
public class  GenericJpaRepository<T, ID extends Serializable> 
extends SimpleJpaRepository<T, ID>  implements GenericRepository<T, ID> , Serializable {
	private static Logger log = LoggerFactory.getLogger(GenericJpaRepository.class
			.getName());
	private static final long serialVersionUID = -2357827398754143421L;
	private EntityManager em;
	
	
	
	public GenericJpaRepository(Class<T> domainClass, EntityManager em) {
		super(domainClass, em);
		this.em = em;
	}
	
	
	/**
	 * Creates a new {@link SimpleJpaRepository} to manage objects of the given {@link JpaEntityInformation}.
	 * 
	 * @param entityInformation must not be {@literal null}.
	 * @param entityManager must not be {@literal null}.
	 */
	public GenericJpaRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.em = entityManager;
	}
	
	@Override
	public void doPager(PageBean pageBean, String qlString){
		doPager(pageBean, qlString, new ArrayList<Object>());
	}
	
	
	protected EntityManager getEntityManager() {
		return em;
	}
	
	public void doPager(PageBean pageBean, String qlString, List<Object> values){
		doPager(pageBean, qlString, values,false);
	}
	
	public void doPager(PageBean pageBean, String qlString, List<Object> values,boolean cacheable){
		if (values == null) {
			values = new ArrayList<Object>();
		}
		qlString = convertQL(qlString);// 1.转换ql为指定格式的语句
		List<Object> conValues = new ArrayList<Object>();// 条件参数值集合,来自pageBean.conditions的values
		List<Object> list = preConditionJPQL(qlString,
				pageBean.getConditions(), conValues);// 解析条件语句,获取条件参数集合
		int conBeginIndex = (Integer) list.get(0);// 返回条件集合在hql起始位置
		String condition_jpql = (String) list.get(1);// 获取条件ql语句
		String order_jpql = preOrderJPQL(pageBean.getOrders());// 获取排序ql语句
		String list_ql = preQL(qlString, condition_jpql, order_jpql);// 获取完整的list ql语句
		String count_ql = preCountJPQL(qlString, condition_jpql);// 获取完整的count ql语句
		log.debug("count_ql = {}", count_ql);
		for (int i = conValues.size() - 1; i >= 0; i--) {
			values.add(conBeginIndex, conValues.get(i));
		}
		executeCount(pageBean, count_ql, values, conBeginIndex,cacheable);// 执行count语句，将填充pageBean中的totalRows
		executeList(pageBean, list_ql, values, conBeginIndex,cacheable);// 执行list语句，将填充pageBean中的items
	}
	
	
	/**
	 * 解析条件语句
	 * 
	 * @param qlString
	 * @param conditions
	 * @param conValues
	 * @throws Exception
	 */
	protected List<Object> preConditionJPQL(String qlString,
			List<Condition> conditions, List<Object> conValues)
			{
		List<Object> list = new ArrayList<Object>();
		int conBeginIndex = getConBeginIndex(qlString);
		list.add(conBeginIndex);
		list.add(preConditionJPQL(conditions, conValues).toString());
		return list;
	}

	protected String preConditionJPQL(List<Condition> conditions,
			List<Object> values){
 		StringBuffer c = new StringBuffer();
		if (conditions != null && conditions.size() > 0) {
			c.append(RelateType.AND.toString() + " ( ");
			for (int i = 0; i < conditions.size(); i++) {
				Condition condition = conditions.get(i);
				String groupPrefixBrackets = condition.getGroupPrefixBrackets();
				String propertyName = condition.getPropertyName();
				Object value = condition.getPropertyValue();
				boolean isPrefixBrackets = condition.isPrefixBrackets();
				boolean isSuffixBrackets = condition.isSuffixBrackets();
				Operation operation = condition.getOperation();
				RelateType relateType = condition.getRelateType();
				String related = "";
				if(i!=0){
					if(relateType==null){
						relateType = RelateType.AND;
					}
					related = relateType  + (isPrefixBrackets?" ( ": " ");
				}else{
					related = "" + (isPrefixBrackets?" ( ": " ");
				}
				c.append(groupPrefixBrackets);
				switch (operation) {
				case NC:
				case CN:
					String[] list = value.toString().split("[, ]");
					if(list.length>1){
						c.append(related + " ( " + propertyName + operation + "?");
						values.add("%" + list[0] + "%");
						for (int j = 1; j < list.length; j++) {
							c.append(RelateType.OR + propertyName + operation + "?");
							values.add("%" + list[j] + "%");
						}
						c.append(" ) "); 
					}else{
						c.append(related + propertyName + operation + "?");
						values.add("%" + value + "%");
					}
					break;
				case BN:
				case BW:
					c.append(related + propertyName + operation + "?");
					values.add(value + "%");
					break;
				case EN:
				case EW:
					c.append(related + propertyName + operation + "?");
					values.add("%" + value);
					break;
				case BETWEEN:
					c.append(related + propertyName + operation + "?" + " AND "
							+ "?");
					Object[] params = new Object[2];
					if (value instanceof String) {
						String[] array = value.toString().split("#|,");
						params[0] = array[0];
						params[1] = array[1];
					} else {
						params = (Object[]) value;
					}
					values.add(params[0]);
					values.add(params[1]);
					break;
				case NI:
				case IN:
					c.append(related + propertyName + operation + "(");
					if(value!=null){
						Class<?> clazz = value.getClass();
						if (clazz.isArray()) {
							Object[] array = (Object[])value;
							for (Object object : array) {
								c.append("?,");
								values.add(object);
							}
							if(array.length>0){
								c.replace(c.length() - 1, c.length(), "");
							}
						} else if (value instanceof Collection<?>) {
							Collection<?> coll = (Collection<?>) value;
							for (Object object : coll) {
								c.append("?,");
								values.add(object);
							}
							if(coll.size()>0){
								c.replace(c.length() - 1, c.length(), "");
							}
						}else if(value instanceof String){
							if(StringUtils.isEmpty((String)value)){
								c.append("''");
							}else{
								String[]array = ((String) value).split(",");
								for (String val : array) {
									c.append("?,");
									values.add(val);
								}
								if(array.length>0){
									c.replace(c.length() - 1, c.length(), "");
								}
							}
						}
					}else{
						throw new RuntimeException("in条件没有包含数据,不合法");
					}
					c.append(")");
					break;
				case EQ:
				case GE:
				case GT:
				case LE:
				case LT:
				case NE:
					c.append(related + propertyName + operation + "?");
					values.add(value);
					break;
				case NN:
				case NU:
					c.append(related + propertyName + operation);
					break;
				default:
					break;
				}
				c.append(isSuffixBrackets?" ) ": " ");
			}
			c.append(" ) ");
		}
		log.debug("condition = {}", c.toString());
		return c.toString();
	}
	
	/**
	 * 执行统计语句
	 */
	protected void executeCount(PageBean pageBean, String count_ql,
			List<Object> values, int conBeginIndex,boolean cacheable){
		Query query = getEntityManager().createQuery(count_ql);
		query.setHint(QueryHints.CACHEABLE, cacheable);
		
		for (int i = 0; i + conBeginIndex < values.size(); i++) {
			setParameter(query, i + 1, values.get(i + conBeginIndex));
		}
		List<?> list = null;
		list = query.getResultList();
		if (list.size() == 1) {
			int totalRows = Integer.parseInt(list.get(0).toString());
			log.debug("executeCount totalRows = {}", totalRows);
			pageBean.setTotalRows(totalRows);
		} else {
			pageBean.setTotalRows(list.size());
		}
	}

	protected void setParameter(Query query, int position, Object value){
		try {
			query.setParameter(position, value);
		} catch (IllegalArgumentException e) {
			log.debug("WARN : " + e.getMessage());
			Pattern p = Pattern.compile("(\\w+\\.\\w+\\.\\w+)");
			Matcher matcher = p.matcher(e.getMessage());
			while(matcher.find()){
				String clazz = matcher.group(1);
				if (Integer.class.getName().equals(clazz)) {
					value = Integer.parseInt(value.toString());
				}else if(Long.class.getName().equals(clazz)){
					value = Long.parseLong(value.toString());
				}else if(Double.class.getName().equals(clazz)){
					value = Double.parseDouble(value.toString());
				}else if(Boolean.class.getName().equals(clazz)){
					value = Boolean.parseBoolean(value.toString());
				}else if(Date.class.getName().equals(clazz)){
					String temp = value.toString();
					String pattern = null;
					if(temp.matches("^\\d{4}-\\d{2}-\\d{2}$")){
						pattern = "yyyy-MM-dd";
					}else if(temp.matches("^\\d{8}$")){
						pattern = "yyyyMMdd";
					}else if(temp.matches("^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}$")){
						pattern = "yyyy-MM-dd HH:mm:ss";
					}
					if(pattern!=null){
						SimpleDateFormat sdf = new SimpleDateFormat(pattern);
						try {
							value = sdf.parse(temp);
						} catch (ParseException e1) {
							throw new RuntimeException(e1);
						}
					}else{
						throw new RuntimeException("传递的日期值"+temp+"不能够被识别");
					}
				}
				query.setParameter(position, value);
			}
		}
	}

	/**
	 * 执行集合语句
	 */
	protected void executeList(PageBean pageBean, String list_ql,
			List<Object> values, int conBeginIndex,boolean cacheable){
		Query query = getEntityManager().createQuery(list_ql);
		query.setHint(QueryHints.CACHEABLE, cacheable);
		for (int i = 0; i < values.size(); i++) {
			setParameter(query, i + 1, values.get(i));
		}
		int firstResult = (pageBean.getCurrentPage() - 1)
				* pageBean.getRowsPerPage();
		int maxResults = pageBean.getRowsPerPage();
		query.setFirstResult(firstResult)
		.setMaxResults(maxResults);
		List<?> list = query.getResultList();
		if(list.size()>0){
			Object item = list.get(0);
			if(item.getClass().isArray()){
				String[] fieldArray = preFieldInfo(list_ql);
				Map<String, Class<?>> propertyMap = preProp(fieldArray,(Object[]) list.get(0));
				List<Object> items = new ArrayList<Object>();
				for (Object object : list) {
					Object[] entity = (Object[]) object;
					CglibBean bean = new CglibBean(propertyMap);
					for (int i = 0; i < fieldArray.length; i++) {
						bean.setValue(fieldArray[i], entity[i]);
					}
					items.add(bean.getObject());
				}
				list = items;
			}
		}
		pageBean.setItems(list);
		getEntityManager().clear();
	}

	
	public Map<String, Class<?>> preProp(String[] fieldArray, Object[] fieldValArray) {
		Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
		Class<?> clazz = null;
		for (int i = 0; i < fieldArray.length; i++) {
			clazz = fieldValArray[i]!=null?fieldValArray[i].getClass():Object.class;
			propertyMap.put(fieldArray[i],clazz);
		}
		return propertyMap;
	}

	public String[] preFieldInfo(String list_ql) {
		String[] fieldArray;
		int firstFormIndex = list_ql.indexOf("FROM");
		log.debug("firstFromIndex={}",firstFormIndex);
		String prefixFrom = list_ql.substring(0, firstFormIndex);
		log.debug("prefixFrom={}",prefixFrom);
		fieldArray = prefixFrom.replace("SELECT", "").trim().split(",");
		for (int i = 0; i < fieldArray.length; i++) {
			String field = fieldArray[i];
			String[] s = field.split(" as | AS | ");
			if(s.length==2){
				fieldArray[i] = s[1];
			}
			String[]tempArray = fieldArray[i].split("\\.");
			fieldArray[i] = tempArray[tempArray.length-1];
		}
		return fieldArray;
	}

	/**
	 * 解析ql语句
	 */
	protected String preQL(String qlString, String condition_jpql,
			String order_jpql) {
		if (qlString.endsWith("ASC") || qlString.endsWith("DESC")) {
			order_jpql = order_jpql.replace("ORDER BY", ",");
		}
		qlString = qlString.replaceAll("WHERE 1=1", " WHERE 1=1 "
				+ condition_jpql)
				+ order_jpql;// 排序位置有待修改
		log.debug("list_ql = {}", qlString);
		return qlString;
	}

	/**
	 * 解析统计ql语句
	 * 
	 * @param qlString
	 * @return
	 * @throws Exception
	 */
	protected String preCountJPQL(String qlString, String condition_jpql){
		String countField = "*";
		String distinctField = findDistinctField(qlString);
		if(distinctField!=null){
			countField = "DISTINCT " + distinctField;
		}
		if (qlString.matches("^FROM.+")) {
			qlString = "SELECT count("+countField+") " + qlString;
		} else {
			int beginIndex = qlString.indexOf("FROM");
			qlString = "SELECT count("+countField+") " + qlString.substring(beginIndex);
		}
		qlString = qlString.replaceAll("WHERE 1=1", " WHERE 1=1 "
				+ condition_jpql);
		return qlString.replaceAll("FETCH", "");
	}

	
	protected int getConBeginIndex(String qlString) {
		int conIndex = qlString.indexOf("WHERE 1=1");
		if (conIndex == -1) {
			throw new RuntimeException("ql中没有WHERE 1=1");
		}
		log.debug("conIndex = {}",conIndex);
		String conBefore = qlString.substring(0, conIndex);
		String conAfter = qlString.substring(conIndex);
		int conBeforeCount = counter(conBefore, '?');
		int conAfterCount = counter(conAfter, '?');
		log.debug("条件前的?个数：{}", conBeforeCount);
		log.debug("条件后的?个数：{}", conAfterCount);
		log.debug("条件的起始位置：{}", conBeforeCount);
		return conBeforeCount;
	}
	
	/**
	 * 统计语句中问号的个数
	 * @param s
	 * @param c
	 * @return
	 */
	protected int counter(String s, char c) {
		int count = 0;
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == c) {
				count++;
			}
		}
		return count;
	}
	
	private String findDistinctField(String qlString) {
		int index = qlString.indexOf("DISTINCT");
		if(index!=-1){
			String subql = qlString.substring(index+9);
			int end = -1;
			for (int i = 0; i < subql.length(); i++) {
				if(subql.substring(i,i+1).matches(" |,")){
					end = i;
					break;
				}
			}
			return subql.substring(0, end);
		}
		return null;
	}

	/**
	 * 解析排序语句
	 * @param orders
	 * @return
	 */
	protected String preOrderJPQL(List<Order> orders) {
		if (orders.size() == 0) {
			return "";
		}
		StringBuffer c = new StringBuffer(" ORDER BY ");
		for (Order order : orders) {
			String propertyName = order.getPropertyName();
			OrderType orderType = order.getOrderType();
			c.append(propertyName + " " + orderType + ",");
		}
		if (orders.size() > 0) {
			c.replace(c.length() - 1, c.length(), "");
		}
		log.debug("order = {}", c.toString());
		return c.toString();
	}

	/**
	 * 格式化ql
	 * @param qlString
	 * @return
	 */
	protected String convertQL(String qlString) {
		String result = qlString.replaceAll("from", "FROM")
				.replaceAll("distinct", "DISTINCT")
				.replaceAll("left join", "LEFT JOIN")
				.replaceAll("fetch", "FETCH")
				.replaceAll("select", "SELECT").replaceAll("where", "WHERE")
				.replaceAll("order by", "ORDER BY").replaceAll("asc", "ASC")
				.replaceAll("desc", "DESC").trim();
		log.debug("qlString = {}", result);
		return result;
	}

	/**
	 * 解析ql语句和参数
	 * 
	 * @param qlString
	 * @param params
	 * @param values
	 * @return
	 */
	protected String preQLAndParam(String qlString, Map<String, Object> params,
			List<Object> values) {
		log.debug("开始解析qlString：{}", qlString);
		Map<Integer, Object> map_values = new HashMap<Integer, Object>();
		Map<Integer, String> map_names = new HashMap<Integer, String>();
		List<Integer> list = new ArrayList<Integer>();
		String preQL = qlString;
		if (null != params) {
			for (String key : params.keySet()) {
				int index = qlString.indexOf(":" + key);
				Object value = params.get(key);
				preQL = preQL.replaceAll(":" + key + " ", "? ");
				list.add(index);
				map_values.put(index, value);
				map_names.put(index, key);
			}
			log.debug("解析完成qlString:{}", preQL);
			Collections.sort(list);
			log.debug("最终参数值顺序(参数名->参数位置->参数值)：");
			for (Integer position : list) {
				if (log.isDebugEnabled()) {
					System.out.println(map_names.get(position) + "->"
							+ position + "->" + map_values.get(position));
				}
				values.add(map_values.get(position));
			}
		}
		return preQL;
	}
}
