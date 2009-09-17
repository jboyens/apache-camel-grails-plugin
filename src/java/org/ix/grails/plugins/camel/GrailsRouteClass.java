package org.ix.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.InjectableGrailsClass;
import groovy.lang.Closure;

public interface GrailsRouteClass extends InjectableGrailsClass {
	Closure getConfigure();
}
