package org.ix.grails.plugins.camel

class GrailsRouteBuilderInvocationProxy {

	def builder
	def definition
	def last
	
	void configure() {
		definition.delegate = this
		definition()
	}
	
	def methodMissing(String name, args) {
		def argClasses = args.collect { it.class }
		def value = [last, builder].find {
			if (it.metaClass.respondsTo(it, name, argClasses)) {
				last = it.metaClass.invokeMethod(it, name, args)
				true
			} else {
				false
			}
		}
		
		value ?: { throw new MissingMethodException(name, definition.owner.class, args) }()
	}

}