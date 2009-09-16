package org.ix.grails.plugins.camel

import org.slf4j.LoggerFactory

class ProducerConvenienceMethodDecorator {

	static log = LoggerFactory.getLogger(this)
	
	static decorations = [
		camelSend: [
			{ producerTemplate, endpoint, body -> producerTemplate.sendBody(endpoint, body) },
		],
		camelRequest: [
			{ producerTemplate, endpoint, body -> producerTemplate.requestBody(endpoint, body) },
		]
	]
	
	static decorate(producerTemplate, Object[] decoratees) {
		decoratees.each { decoratee ->
			log.info("decorating $decoratee")
			decorations.each { name, impls ->
				impls.each { impl ->
					decoratee.metaClass."$name" = impl.curry(producerTemplate)
				}
			}
		}
	}

}