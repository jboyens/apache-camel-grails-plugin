package grails.plugins.camel

import org.apache.camel.builder.RouteBuilder
import org.codehaus.groovy.grails.plugins.camel.GrailsRouteBuilderConfigureHelper

abstract class GrailsRouteBuilder extends RouteBuilder {

	def methodMissing(String name, args) {
		if (args.size() == 1 && args[0] instanceof Closure) {
			GrailsRouteBuilderConfigureHelper.configure(this, this.from(name), args[0])
		} else {
			throw new MissingMethodException(name, configure.owner.class, args)
		}
	}

}