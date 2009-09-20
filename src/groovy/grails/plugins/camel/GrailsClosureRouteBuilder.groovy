package grails.plugins.camel

abstract class GrailsClosureRouteBuilder extends GrailsRouteBuilder {

	abstract getRoutes()

	void configure() {
		def routes = this.routes
		if (routes instanceof Closure) {
			def routesClone = this.routes.clone()
			routesClone.delegate = this
			routesClone.call()
		} else {
			throw new IllegalStateException("getRoutes() of ${this} does not return a Closure")
		}
	}
	
}

