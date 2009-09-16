package org.ix.grails.plugins.camel

import org.apache.camel.builder.RouteBuilder
import org.slf4j.LoggerFactory

class GrailsRouteBuilderInvocationProxy {

	static log = LoggerFactory.getLogger(this)
	
	private builder
	private definition
	private node
	
	static build(RouteBuilder builder, Closure definition) {
		build(builder, null, definition)
	}
	
	static build(RouteBuilder builder, Object startingNode, Closure definition) {
		log.debug("building {builder: '$builder', startingNode: '$startingNode', definition: '${definition.toString()}'}")
		new GrailsRouteBuilderInvocationProxy(builder, startingNode, definition).node
	}
	
	private GrailsRouteBuilderInvocationProxy(RouteBuilder builder, Object startingNode, Closure definition) {
		this.builder = builder
		this.node = startingNode
		this.definition = definition
		
		this.definition.delegate = this
		this.definition.resolveStrategy = Closure.DELEGATE_FIRST
		this.definition()
	}
		
	def methodMissing(String name, args) {
		log.debug("invoking $name($args) {node = $node}")
		
		if ((args.size() == 1) && (args[0] instanceof Closure)) {
			log.debug("$name was called with closure as node arg")
			
			this."$name"(build(builder, args[0]))
		} else {
			def argClasses = args.collect { it.class } as Object[]

			def resolved = [node: node, builder: builder].find { targetName, target ->
				if (target == null) return false
				
				log.debug("resolving against '$targetName' (${target.class})")
				if (target.metaClass.respondsTo(target, name, argClasses)) {
					log.debug("$targetName DOES respond")
					node = target.metaClass.invokeMethod(target, name, args)
					true
				} else {
					log.debug("$targetName DOES NOT respond")
					false
				}
			}

			if (!resolved) {
				log.error("could not resolve $name($args)")
				throw new MissingMethodException(name, definition.owner.class, args)
			}
		
			node
		}
	}

}