package com.lc.platform.jqgrid;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.data.jpa.domain.Specification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lc.platform.dao.Operation;
import com.lc.platform.dao.RelateType;

public class JqgridSpecification<T> implements Specification<T>{
	public static ObjectMapper objectMapper = new ObjectMapper();
	CriteriaBuilder cb;
	Root<T> root;
	String searchField;
	String searchString;
	Operation operation;
	String content;
	Predicate predicate;
	private Filters filters = null;
	Pattern betweenPattern = Pattern.compile("^.+,.+$");
	String[] parsePatterns = new String[]{"yyyyMMdd","yyyy-MM-dd","yyyy/MM/dd"};
	static Map<String,Class<?>> priClassMap = new HashMap<String,Class<?>>();
	
	static{
		priClassMap.put("double", Double.class);
		priClassMap.put("int", Integer.class);
		priClassMap.put("float", Float.class);
	}
	
	public JqgridSpecification(String searchField, String searchString,
			Operation operation, String content) {
		this.searchField = searchField;
		this.searchString = searchString;
		this.operation = operation;
		this.content = content;
		if (StringUtils.isNotEmpty(content)) {
			try {
				filters = objectMapper.readValue(content, Filters.class);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			filters = new Filters();
			filters.setGroupOp("AND");
			filters.setRules(new ArrayList<RuleItem>());
		}
	}
	
	public <V> Path<V> buildPath(String propertyName,Class<V> clazz){
		if(propertyName.contains(".")){//ca.criminalInfo.xm,//criminalInfo.xm
			String[] attrs = propertyName.split("\\.");
			Path<V> join  = root.join(attrs[0]);
			for (int i = 1; i < attrs.length; i++) {
				join = join.get(attrs[i]);
			}
			return join;
		}else{
			return root.get(propertyName);
		}
	}
	
	protected Predicate buildPredicate(String propName,String propValue,Operation operation) throws Exception{
		if(!checkRuleItem(propName, propValue, operation)){
			return null;
		}
		Path<?> attrPath = buildPath(propName, Object.class);
		Class<?> fieldType = attrPath.getJavaType();
		Predicate predicate = null;
		switch (operation) {
		case EQ:
			 if(fieldType==Date.class){
				Date value = DateUtils.parseDate(propValue,parsePatterns);
				predicate = cb.equal(attrPath, value);
			}else{
				predicate = cb.equal(attrPath, propValue);
			}
			break;
		case NC:
		case CN:
		case BN:
		case BW:
		case EN:
		case EW:
			predicate = processLike(propName,propValue,operation);
			break;
		case GE:
			predicate = processGLE(propName, propValue, "greaterThanOrEqualTo");
			break;
		case GT:
			predicate = processGLE(propName, propValue, "greaterThan");
			break;
		case LE:
			predicate = processGLE(propName, propValue, "lessThanOrEqualTo");
			break;
		case LT:
			predicate = processGLE(propName, propValue, "lessThan");
			break;
		case NE:
			predicate = cb.notEqual(attrPath, propValue);
			break;
		case NN:
			predicate = cb.isNotNull(attrPath);
			break;
		case NU:
			predicate = cb.isNull(attrPath);
			break;
		case BETWEEN:
			if(!propValue.contains(",")){
				return buildPredicate(propName, propValue, Operation.EQ);
			}else if(propValue.startsWith(",")){
				return buildPredicate(propName, propValue.substring(1), Operation.LE);
			}else if(propValue.endsWith(",")){
				return buildPredicate(propName, propValue.substring(0,propValue.length()-1), Operation.GE);
			}
			Matcher matcher = betweenPattern.matcher(propValue);
			String[] array = propValue.split(",");
			Object x = null,y = null;
			if(matcher.find()){
				if(isPrimitive(fieldType)){
					Constructor<?> cons = fieldType.getConstructor(String.class);
					x = cons.newInstance(array[0]);
					y = cons.newInstance(array[1]);
					
				}else if(fieldType==Date.class){
					x = DateUtils.parseDate(array[0],parsePatterns);
					y = DateUtils.parseDate(array[1],parsePatterns);
				}
			}
			Method method = cb.getClass().getDeclaredMethod("between", Expression.class,Comparable.class,Comparable.class);
			predicate = (Predicate) method.invoke(cb, attrPath,x,y);
			break;
		case IN:
			In<Object> in = cb.in(attrPath);
			if(StringUtils.isNotBlank(propValue)){
				String[]values = propValue.split(",");
				for (String value : values) {
					in.value(value);
				}
			}
			predicate = in;
			break;
		case NI:
			In<Object> ni = cb.in(attrPath);
			if(StringUtils.isNotBlank(propValue)){
				String[]values = propValue.split(",");
				for (String value : values) {
					ni.value(value);
				}
			}
			predicate = cb.not(ni);
			break;
		}
		return predicate;
	}
	
	/**
	 * 处理数字和日期类型的大于，大于等于，小于，小于等于的情况
	 * @param propName
	 * @param propValue
	 * @param oper
	 * @return Predicate
	 */
	protected Predicate processGLE(String propName,String propValue,String oper) throws Exception{
		Path<?> fieldPath = buildPath(propName, Object.class);
		Class<?> fieldType = fieldPath.getJavaType();
		Object val;
		if(isPrimitive(fieldType)){
			System.out.println(fieldType.getName());
			Class<?> clazz = priClassMap.get(fieldType.getName());
			if(clazz!=null){
				fieldType = clazz;
			}
			Constructor<?> cons = fieldType.getConstructor(String.class);
			val = cons.newInstance(propValue);
		}else if(fieldType==Date.class){
			val = DateUtils.parseDate(propValue,parsePatterns);
		}else{
			return null;
		}
		Method method = cb.getClass().getDeclaredMethod(oper, Expression.class,Comparable.class);
		return (Predicate) method.invoke(cb, fieldPath,val);
	}
	
	public boolean isPrimitive(Class<?> fieldType){
		try {
			fieldType.getField("TYPE");
			return true;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return fieldType.isPrimitive();
	}
	
	public Predicate processLike(String propertyName,String propertyValue,Operation operation){
		Path<String> fieldPath = buildPath(propertyName, String.class);
		String[] list = propertyValue.toString().split("[, ]");
		String operPrefix = "";
		String operSuffix = "";
		boolean islike = true;
		switch (operation) {
			case CN:
				operPrefix = "%";
				operSuffix= "%";
				islike = true;
				break;
			case BW:
				operSuffix= "%";
				islike = true;
				break;
			case EW:
				operPrefix = "%";
				islike = true;
				break;
			case NC:
				operPrefix = "%";
				operSuffix= "%";
				islike = false;
				break;
			case BN:
				operSuffix= "%";
				islike = false;
				break;
			case EN:
				operPrefix = "%";
				islike = false;
				break;
			default:
				break;
		}
		if(list.length>1){
			Predicate[] predicates = new Predicate[list.length];
			for (int i = 0; i < list.length; i++) {
				String value = list[i];
				if(islike){
					predicates[i] = cb.like(fieldPath,operPrefix + value+operSuffix);
				}else{
					predicates[i] = cb.notLike(fieldPath,operPrefix + value+operSuffix);
				}
			}
			return cb.or(predicates);
		}
		if(islike){
			return cb.like(fieldPath, operPrefix + propertyValue+operSuffix);
		}
		return cb.notLike(fieldPath, operPrefix + propertyValue+operSuffix);
	}
	
	
	public boolean checkRuleItem(String propertyName,String propertyValue,Operation operation){
		return (StringUtils.isNotBlank(propertyName) && StringUtils.isNotBlank(propertyValue) && operation!=null)
				||(operation!=null && (operation==Operation.NN || operation==Operation.NU));	
	}
	
	@Override
	public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query,
			CriteriaBuilder cb) {
		List<Predicate> restrictions = new ArrayList<Predicate>();
		this.cb = cb;
		this.root = root;
		try {
			predicate = buildPredicate(searchField, searchString, operation);
			if(predicate!=null){
				restrictions.add(predicate);
			}
			Predicate rootGrouppre = buildGroupPre(filters.getGroupOp(),filters.getRules());
			if(rootGrouppre!=null){
				restrictions.add(rootGrouppre);
			}
			List<GroupItem> groups = filters.getGroups();
			if(groups!=null){
				for (GroupItem groupItem : groups) {
					Predicate subGrouppre = buildGroupPre(groupItem.getGroupOp(),groupItem.getRules());
					if(subGrouppre!=null){
						restrictions.add(subGrouppre);
					}
				}
			}
			Predicate[] array = new Predicate[restrictions.size()];
			restrictions.toArray(array);
			predicate = cb.and(array);
		} catch (Exception e) {
			predicate = null;
			throw new RuntimeException(e);
		}
		return predicate;
	}

	
	public Predicate buildGroupPre(String groupOp,List<RuleItem> rules) throws Exception{
		Predicate grouppre = null;
		RelateType relateType = RelateType.valueOf(groupOp.toUpperCase());
		List<Predicate> groupPreDicates = new ArrayList<Predicate>();
		for (RuleItem ruleItem : rules) {
			String propertyName = ruleItem.getField();
			String propertyValue = ruleItem.getData();
			Operation operation = JqGridUtil.map.get(ruleItem.getOp());
			predicate = buildPredicate(propertyName, propertyValue, operation);
			if(predicate!=null){
				groupPreDicates.add(predicate);
			}
		}
		if(groupPreDicates.size()>0){
			Predicate[] array = new Predicate[groupPreDicates.size()];
			switch (relateType) {
			case AND:
				grouppre = cb.and(groupPreDicates.toArray(array));
				break;
			case OR:
				grouppre = cb.or(groupPreDicates.toArray(array));
				break;
			}
		}
		return grouppre;
	}
	
	public Filters getFilters() {
		return filters;
	}
}
