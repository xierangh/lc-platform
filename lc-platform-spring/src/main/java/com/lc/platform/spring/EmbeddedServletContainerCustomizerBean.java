package com.lc.platform.spring;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.http.HttpStatus;

/**
 * 对嵌入式容器进行自定义配置</br>
 * 错误页面的定制:400,404,500</br>
 * @author chenjun
 *
 */
public class EmbeddedServletContainerCustomizerBean implements
		EmbeddedServletContainerCustomizer {

	@Override
	public void customize(ConfigurableEmbeddedServletContainer container) {
		container.addErrorPages(new ErrorPage(HttpStatus.BAD_REQUEST, "/400"));
		container.addErrorPages(new ErrorPage(HttpStatus.NOT_FOUND, "/404"));
		container.addErrorPages(new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/500"));
	} 
}
