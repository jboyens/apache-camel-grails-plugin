package org.codehaus.groovy.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.AbstractInjectableGrailsClass;
import groovy.lang.Closure;

public class DefaultGrailsRoutesClass extends AbstractInjectableGrailsClass implements GrailsRoutesClass
{
	public DefaultGrailsRoutesClass(Class clazz) {
		super(clazz, "Routes");
	}

	public DefaultGrailsRoutesClass(Class clazz, String trailingName) {
		super(clazz, trailingName);
	}

	public Closure getRoutes() {
		return (Closure) getMetaClass().getProperty(this.getReference().getWrappedInstance(), "routes");
	}
}
