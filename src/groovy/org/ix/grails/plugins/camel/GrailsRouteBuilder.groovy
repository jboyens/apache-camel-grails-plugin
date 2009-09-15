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
	def PROPAGATION_REQUIRED
	def PROPAGATION_REQUIRES_NEW
	
	public GrailsRouteBuilder(Closure confClosure, SpringTransactionPolicy propagationRequired, SpringTransactionPolicy propagationRequiresNew) {
		this.confClosure = confClosure
		this.PROPAGATION_REQUIRED = propagationRequired
		this.PROPAGATION_REQUIRES_NEW = propagationRequiresNew
	}

	public void configure() {
		this.confClosure.delegate = this
		log.info("invoking config of builder ${confClosure.owner.class}")
		this.confClosure()
	}
	
	def methodMissing(String name, args) {
		if (args.size() == 1 && args[0] instanceof Closure) {
			log.info("creating builder invocation proxy for route $name on builder ${confClosure.owner.class}")
			def invocationProxy = new GrailsRouteBuilderInvocationProxy(builder: this, last: this.from(name), definition: args[0])
			invocationProxy.configure()
		} else {
			throw new MissingMethodException(name, confClosure.owner.class, args)
		}
	}
}