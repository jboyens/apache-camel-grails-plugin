package org.ix.grails.plugins.camel

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.spring.spi.SpringTransactionPolicy
import org.slf4j.LoggerFactory

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 13, 2009
 * Time: 12:59:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class GrailsRouteBuilder extends RouteBuilder {

	static log = LoggerFactory.getLogger(this)
	
	def confClosure
	
	public GrailsRouteBuilder(Closure confClosure) {
		this.confClosure = confClosure
	}

	public void configure() {
		this.confClosure.delegate = this
		log.info("invoking config of builder ${confClosure.owner.class}")
		this.confClosure()
	}
	
	def methodMissing(String name, args) {
		log.debug("resolving missing method $name on builder ${confClosure.owner.class}")
		
		if (args.size() == 1 && args[0] instanceof Closure) {
			log.debug("creating builder invocation proxy for $name on builder ${confClosure.owner.class}")
			GrailsRouteBuilderInvocationProxy.build(this, this.from(name), args[0])
		} else {
			throw new MissingMethodException(name, confClosure.owner.class, args)
		}
	}
}