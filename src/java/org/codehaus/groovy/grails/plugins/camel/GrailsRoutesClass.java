package org.codehaus.groovy.grails.plugins.camel;

import org.codehaus.groovy.grails.commons.InjectableGrailsClass;
import groovy.lang.Closure;

public interface GrailsRoutesClass extends InjectableGrailsClass {
	Closure getRoutes();
}
