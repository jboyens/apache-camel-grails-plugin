package org.ix.grails.plugins.camel

import org.slf4j.LoggerFactory
import org.apache.camel.builder.RouteBuilder

public class GrailsRouteBuilder extends RouteBuilder {

	static log = LoggerFactory.getLogger(this)
	
	private route
	private configure
	
	public GrailsRouteBuilder(route) {
		try {
			configure = route.configure
		} catch (MissingPropertyException e) {
			if (e.property == "configure" && e.type == route.class) {
				throw new IllegalArgumentException("${route} does not have a configure property")
			} else {
				throw e
			}
		}
		
		if (!(configure instanceof Closure)) {
			throw new IllegalArgumentException("${route} has a configure property that is not a closure")
		}
		
		configure.delegate = this
		this.route = route
	}

	void configure() {
		configure.call()
	}
	
	def methodMissing(String name, args) {
		log.debug("resolving missing method $name on builder ${configure.owner.class}")
		
		if (args.size() == 1 && args[0] instanceof Closure) {
			log.debug("creating builder invocation proxy for $name on builder ${configure.owner.class}")
			GrailsRouteBuilderInvocationProxy.build(this, this.from(name), args[0])
		} else {
			throw new MissingMethodException(name, configure.owner.class, args)
		}
	}
	
}