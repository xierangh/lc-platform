package com.lc.platform.dao.jpa;

import java.io.Serializable;
import java.lang.reflect.Method;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.data.repository.util.ClassUtils;
import org.springframework.util.ReflectionUtils;


public class GenericRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable> extends
		JpaRepositoryFactoryBean<T, S, ID> {
	protected ClassLoader classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();
	
	/**
	 * Returns a {@link RepositoryFactorySupport}.
	 * 
	 * @param entityManager
	 * @return
	 */
	@Override
	protected RepositoryFactorySupport createRepositoryFactory(EntityManager entityManager) {
		/*ProxyFactory result = new ProxyFactory();
		result.setTarget(entityManager);
		result.setInterfaces(new Class[] { EntityManager.class});
		result.addAdvice(new GenericMethodInterceptor(entityManager));
		EntityManager emproxy = (EntityManager) result.getProxy(classLoader);*/
		return new GenericRepositoryFactory(entityManager);
	}
	
	public class GenericMethodInterceptor implements MethodInterceptor{
		private final Object target;
		
		public GenericMethodInterceptor(Object target){
			this.target = target;
		}
		
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			try {
				Method method = invocation.getMethod();
				Object[] arguments = invocation.getArguments();
				Object result = method.invoke(target, arguments);
				if(result instanceof Query){
					try {
						Class<?> queryClass = Class.forName("org.hibernate.Query");
						Class<?> transformerClass = Class.forName("org.hibernate.transform.ResultTransformer");
						Object queryObject = ((Query)result).unwrap(queryClass);
						Method setResultTransformerMethod = ReflectionUtils.findMethod(queryClass, "setResultTransformer",transformerClass);
						setResultTransformerMethod.invoke(queryObject, AliasToEntityMapResultTransformer.INSTANCE);
					} catch (Exception e) {
						System.out.println("not find org.hibernate.Query");
					}
				}
				return result;
			} catch (Exception e) {
				ClassUtils.unwrapReflectionException(e);
			}
			throw new IllegalStateException("Should not occur!");
		}
	}
}
