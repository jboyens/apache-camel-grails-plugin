package grails.plugins.camel;

import org.apache.camel.Processor;
import org.apache.camel.Exchange;
import groovy.lang.Closure;

public class ClosureProcessor implements Processor {

	private Closure target;

	public ClosureProcessor(Closure target) {
		this.target = target;
		this.target.setResolveStrategy(Closure.DELEGATE_FIRST);
	}

	@Override
	public void process(Exchange exchange) throws Exception {
	    this.target.setDelegate(exchange);
		this.target.call(exchange);
	}
}
