class DomainRoutesTests extends GroovyTestCase {
	
	def camelService
	
	void testSend() {
		camelService.send "direct:testDomain", "payload1"
		assertNotNull(Message.findByBody("payload1"))
	}

}