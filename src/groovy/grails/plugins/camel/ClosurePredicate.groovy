package grails.plugins.camel

import org.apache.camel.Predicate;
import org.apache.camel.Exchange;
import groovy.lang.Closure;

class ClosurePredicate implements Predicate {

	private predicate

	ClosurePredicate(Closure predicate) {
		this.predicate = predicate
	}

	void assertMatches(String text, Exchange exchange) {
		if (!matches(exchange)) throw new AssertionError(text)
	}
	
	boolean matches(Exchange exchange) throws Exception {
		def predicateClone = predicate.clone()
		predicateClone.delegate = exchange
		predicateClone.resolveStrategy = Closure.DELEGATE_FIRST
		predicateClone() == true
	}

	boolean matches(Object object) throws Exception {
		matches((Exchange) exchange)
	}
}
