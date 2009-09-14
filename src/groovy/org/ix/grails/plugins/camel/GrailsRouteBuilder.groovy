package org.ix.grails.plugins.camel

import org.apache.camel.builder.RouteBuilder
import org.apache.camel.spring.spi.SpringTransactionPolicy

/**
 * Created by IntelliJ IDEA.
 * User: navtach
 * Date: Mar 13, 2009
 * Time: 12:59:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class GrailsRouteBuilder extends RouteBuilder {

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
		this.confClosure()
	}
}