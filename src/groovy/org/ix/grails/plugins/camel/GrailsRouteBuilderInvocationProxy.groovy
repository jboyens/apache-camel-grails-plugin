package org.ix.grails.plugins.camel

import org.slf4j.LoggerFactory

class GrailsRouteBuilderInvocationProxy {

	static log = LoggerFactory.getLogger(this)
	
	def builder
	def definition
	def last
	
	void configure() {
		log.debug("configuring ${definition.class.name}")
		definition.delegate = this
		definition.resolveStrategy = Closure.DELEGATE_FIRST
		definition()
	}
	
	private collectNestedDefinitionAsFirstArg(target, nestedDefinition) {
		def nestedInvocationProxy = this.class.newInstance(last: null, builder: builder, definition: nestedDefinition)
		nestedInvocationProxy.configure()
		this."$target"(nestedInvocationProxy.last)
	}
	
	def methodMissing(String name, args) {
		if ((args.size() == 1) && (args[0] instanceof Closure)) {
			log.debug("$name was called with closure as last arg")
			collectNestedDefinitionAsFirstArg(name, args[0])
		} else {
			log.debug("resolving $name($args)")
			def argClasses = args.collect { it.class } as Object[]
			log.debug("classes: $argClasses")

			def value = [last, builder].find {
				if (it == null) return false
				
				log.debug("resolving against '$it' (${it.class})")
				if (it.metaClass.respondsTo(it, name, argClasses)) {
					log.debug("$it DOES respond")
					last = it.metaClass.invokeMethod(it, name, args)
					true
				} else {
					log.debug("$it DOES NOT respond")
					false
				}
			}

			if (!value) {
				log.error("could not resolve $name($args)")
				throw new MissingMethodException(name, definition.owner.class, args)
			}
		
			last
		}
	}

}