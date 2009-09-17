package org.ix.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.ArtefactHandlerAdapter;
import org.springframework.util.ReflectionUtils;
import groovy.lang.Closure;

import java.lang.reflect.Modifier;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class RouteArtefactHandler extends ArtefactHandlerAdapter {

	public static final String TYPE = "Route";

	public RouteArtefactHandler()
	{
		super(TYPE, GrailsRouteClass.class, DefaultGrailsRouteClass.class, TYPE);
	}

	@Override
	public boolean isArtefactClass(Class clazz) {
		if(clazz == null || !clazz.getName().endsWith(TYPE)) return false;

		Field field = ReflectionUtils.findField(clazz, GrailsRouteClassProperty.CONFIGURE);
		return field != null;
	}
}
