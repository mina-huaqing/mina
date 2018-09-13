package com.chinadovey.parking.core.supports.freemarker;

import java.io.IOException;
import java.net.URL;

import org.apache.log4j.Logger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import freemarker.cache.ClassTemplateLoader;
/**
 * 自定义的FreeMarker模版加载器<br>
 * 加载jar包中的ftl资源
 * @author Bean
 *
 */
public class TemplateLoader extends ClassTemplateLoader {
	
	private static Logger logger = Logger.getLogger(TemplateLoader.class);
	
	private String path = "/";
	
	public TemplateLoader() {
		super(TemplateLoader.class, "/");
	}

	protected URL getURL(String name) {
		String fullPath = path + name;
		
		ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
		Resource resource = resolver.getResource("classpath:"+fullPath.replaceAll("/{2,}", "/"));
				
		if(resource!=null)
			try {
				return resource.getURL();
			} catch (IOException e) {
				if(logger.isDebugEnabled())
					logger.debug(e.getMessage());
			}
		return null;
	}
}
