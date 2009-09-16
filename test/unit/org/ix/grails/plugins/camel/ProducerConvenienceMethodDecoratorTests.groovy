package org.ix.grails.plugins.camel

class ProducerConvenienceMethodDecoratorTests extends GroovyTestCase {

	def recorder
	def decoratee
	
	void setUp() {
		recorder = new ProducerConvenienceMethodDecoratorTestsInvocationRecorder()
		decoratee = new Object()
	}
	
	void testDecorations() {
		
		shouldFail(MissingMethodException) {
			decoratee.sendMessage "1", "1"
		}
		
		ProducerConvenienceMethodDecorator.decorate(recorder, decoratee)

		decoratee.sendMessage "1", "1"
		assertInvoked('sendBody', "1", "1")
				
		decoratee.requestMessage "1", "1"
		assertInvoked('requestBody', "1", "1")
	}
	
	void testDecorateMultiple() {
		def decoratee2 = new Object()
		ProducerConvenienceMethodDecorator.decorate(recorder, decoratee, decoratee2) 
		decoratee2.sendMessage "1", "1"
		assertInvoked('sendBody', "1", "1")
	}
	
	void assertInvoked(name, Object[] args) {
		assertEquals(name, recorder.name)
		args.eachWithIndex { it, i ->
			assertEquals("arg $i", it, recorder.args[i]) 
		}
	}
}

class ProducerConvenienceMethodDecoratorTestsInvocationRecorder {
	def name
	def args
	
	def methodMissing(String name, args) {
		this.name = name
		this.args = args
	}
}