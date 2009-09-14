class DomainRouteTests extends GroovyTestCase {
	
	def producerTemplate
	
	void testSend() {
		producerTemplate.sendBody("direct:testDomain", "payload1")
		assertNotNull(Message.findByBody("payload1"))
	}

}