package org.codehaus.groovy.grails.plugins.camel;

import org.springframework.util.ReflectionUtils;
import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;

public class RoutesArtefactHandler extends ArtefactHandlerAdapter {

	public static final String TYPE = "Routes";

	public RoutesArtefactHandler() {
		super(TYPE, GrailsRoutesClass.class, DefaultGrailsRoutesClass.class, TYPE);
	}

	public boolean isArtefactClass(Class clazz) {
		if (clazz == null || !clazz.getName().endsWith(TYPE)) return false;

		if (ReflectionUtils.findField(clazz, "routes") != null) {
			return true;
		} else {
			log.warn("routes class " + clazz + " does NOT define a 'routes' property");
			return false;
		}
	}
}
