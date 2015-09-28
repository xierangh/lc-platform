package com.lc.platform.dao.jpa;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

public class EntityDependUtil {
	
	static ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
	/**
	 * 存放解析出来的实体类,按照依赖顺序存放
	 */
	List<String> list = new ArrayList<String>();
	/**
	 * 存放当前实体类的所有被依赖对象信息
	 */
	Map<String, Set<String>> map = new HashMap<String, Set<String>>();

	List<String> tableList = new ArrayList<String>();

	/**
	 * class类路径
	 */
	String[] packagesToScan;

	public EntityDependUtil(String...packagesToScan) {
		if(packagesToScan!=null){
			this.packagesToScan = packagesToScan;
			for (String location : packagesToScan) {
				initClass(location);
			}
			buildDomain();
		}
	}

	public String[] getPackagesToScan() {
		return packagesToScan;
	}

	public void setPackagesToScan(String[] packagesToScan) {
		this.packagesToScan = packagesToScan;
	}

	public void removeClassName(String... classNames) {
		if (classNames != null && classNames.length > 0) {
			for (String className : classNames) {
				list.remove(className);
			}
		}
	}

	protected void initClass(String location) {
		try {
			Resource[] resources = resourcePatternResolver
					.getResources(location);
			for (int i = 0; i < resources.length; i++) {
				String url = resources[i].getURL().toString();
				if (url.endsWith(".class")) {
					String className = "";
					if (url.startsWith("file")) {
						int index = url.indexOf("classes");
						className = url.substring(index + 7).substring(1);
					} else if (url.startsWith("jar")) {
						className = url.split("!")[1];
						className = className.substring(1);
					}
					className = className.replaceAll("\\.class", "")
							.replaceAll("/", ".");
					Class<?> clazz = Class.forName(className);
					Entity entity = clazz.getAnnotation(Entity.class);
					if (entity != null) {
						list.add(className);
						map.put(className, new HashSet<String>());
					}
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected void buildDomain() {
		try {
			List<String> childList = new ArrayList<String>();
			for (String className : list) {
				Class<?> clazz = Class.forName(className);
				sortClassNameByField(1, clazz);
				sortClassNameByMethod(1, clazz);
			}
			List<String> removeList = new ArrayList<String>();
			for (String className : list) {
				Class<?> clazz = Class.forName(className);
				Table table = clazz.getAnnotation(Table.class);
				if (table == null) {
					childList.add(className);
				} else {
					String tableName = table.name();
					if (!tableList.contains(tableName)) {
						tableList.add(table.name());
					} else {
						removeList.add(className);
					}
				}
			}
			for (String className : removeList) {
				list.remove(className);
			}
			for (String className : childList) {
				Class<?> clazz = Class.forName(className);
				String superName = clazz.getSuperclass().getName();
				list.remove(className);
				int index = list.indexOf(superName);
				if (index != -1) {
					list.add(index, className);
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void sortClassNameByField(int level, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			Class<?> type = field.getType();
			if (clazz.equals(type)) {
				continue;
			}
			ManyToOne manyToOne = field.getAnnotation(ManyToOne.class);
			OneToOne oneToOne = field.getAnnotation(OneToOne.class);
			ManyToMany manyToMany = field.getAnnotation(ManyToMany.class);
			OneToMany oneToMany = field.getAnnotation(OneToMany.class);
			if (manyToOne != null || oneToOne != null) {
				int i = list.indexOf(clazz.getName());
				int j = list.indexOf(type.getName());
				if (i < j) {
					Collections.swap(list, i, j);
				}
				map.get(type.getName()).add(clazz.getName());
				sortClassNameByField(level + 1, type);
			} else if (manyToMany != null) {
				throw new RuntimeException();
			} else if (oneToMany != null) {
				int i = list.indexOf(clazz.getName());
				if (type == Set.class) {
					ParameterizedType s = (ParameterizedType) field
							.getGenericType();
					Type typed = s.getActualTypeArguments()[0];
					String className = ((Class<?>) typed).getName();
					int j = list.indexOf(className);
					if (i > j) {
						Collections.swap(list, i, j);
					}
					Set<String> set = getDependInfo(className);
					int ji = list.indexOf(className);
					for (String sd : set) {
						int sdi = list.indexOf(sd);
						if (ji > sdi) {
							Collections.swap(list, ji, sdi);
						}
					}
					map.get(clazz.getName()).add(className);
					sortClassNameByField(level + 1, type);
				}
			}
		}
	}

	public void sortClassNameByMethod(int level, Class<?> clazz) {
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
			Class<?> type = method.getReturnType();
			if (clazz.equals(type)) {
				continue;
			}
			ManyToOne manyToOne = method.getAnnotation(ManyToOne.class);
			OneToOne oneToOne = method.getAnnotation(OneToOne.class);
			ManyToMany manyToMany = method.getAnnotation(ManyToMany.class);
			if (manyToOne != null || oneToOne != null) {
				int i = list.indexOf(clazz.getName());
				int j = list.indexOf(type.getName());
				if (i < j) {
					Collections.swap(list, i, j);
				}
				map.get(type.getName()).add(clazz.getName());
				sortClassNameByMethod(level + 1, type);
			} else if (manyToMany != null) {
				throw new RuntimeException();
			}
		}
	}

	public Set<String> getDependInfo(String className) {
		return map.get(className);
	}

	public List<String> getEntityList() {
		return list;
	}

}
