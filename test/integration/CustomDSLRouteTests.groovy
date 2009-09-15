class CustomDSLRouteTests extends GroovyTestCase {
	
	def testDirectReceiverService
	def producerTemplate
	
	void testSimpleSendMessage() {
		producerTemplate.sendBody("direct:testCustomDSL", "payload2")
		assertEquals("payload2", testDirectReceiverService.received)
	}

}