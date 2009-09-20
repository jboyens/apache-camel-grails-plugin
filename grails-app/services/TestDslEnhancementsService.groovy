import org.apache.camel.Exchange

class TestDslEnhancementsService {

	def received1
	def received2
	def receivedException
	
	def receive1(packet) {
		received1 = packet
		if (packet == 8) {
			throw new IllegalStateException("error")
		}
	}

	def receive2(packet) {
		received2 = packet
	}
	
	def decorate(packet) {
		packet * 2
	}
	
	def exception(Exchange exchange) {
		def e = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable)
		if (e.message == "error") {
			receivedException = true
		} else {
			throw new Error("unknown exception", e)
		}
	}
}