package org.codehaus.groovy.grails.plugins.camel

import org.springframework.beans.factory.FactoryBean
import org.apache.camel.builder.RouteBuilder
import grails.plugins.camel.DefaultGrailsClosureRouteBuilder

class RouteBuilderFactoryBean implements FactoryBean {

	final Class objectType = RouteBuilder
	final boolean singleton = false

	private GrailsRouteClass routeClass
	
	RouteBuilderFactoryBean(GrailsRouteClass routeClass) {
		this.routeClass = routeClass
	}
	
	public getObject() throws Exception {
		new DefaultGrailsClosureRouteBuilder(routeClass.configure)
	}

}