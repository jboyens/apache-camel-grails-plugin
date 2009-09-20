package grails.plugins.camel

class DefaultGrailsClosureRouteBuilder extends GrailsClosureRouteBuilder {

	final routes
	
	DefaultGrailsClosureRouteBuilder(Closure routes) {
		this.routes = routes
	}
	
}

