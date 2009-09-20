package org.codehaus.groovy.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import groovy.lang.Closure;

public class DefaultGrailsRouteClass  extends AbstractInjectableGrailsClass implements GrailsRouteClass, GrailsRouteClassProperty
{
	public DefaultGrailsRouteClass(Class clazz)
	{
		super(clazz, ROUTE);
	}

	public DefaultGrailsRouteClass(Class clazz, String trailingName) {
		super(clazz, trailingName);
	}

	@Override
	public Closure getConfigure() {
		return (Closure) getMetaClass().getProperty(this.getReference().getWrappedInstance(),CONFIGURE);
	}
}
