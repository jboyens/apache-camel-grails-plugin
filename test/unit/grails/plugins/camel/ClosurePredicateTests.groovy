package grails.plugins.camel

import org.apache.camel.Exchange
import org.apache.camel.Message

class ClosurePredicateTests extends GroovyTestCase {

	def make(predicate) {
		new ClosurePredicate(predicate)
	}
	
	def mockExchangeWithInHeaders(Map headers) {
		[getIn: { -> [getHeaders: { -> headers }] as Message }] as Exchange
	}
	
	void testSimple() {
		def cp = make { in.headers.foo == "bar" }
		
		assertTrue(cp.matches(mockExchangeWithInHeaders(foo: "bar")))
		assertFalse(cp.matches(mockExchangeWithInHeaders(foo: "notbar")))
	}
}