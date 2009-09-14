class DirectRouteTests extends GroovyTestCase {
	
	def testDirectReceiverService
	def producerTemplate
	
	void testSimpleSendMessage() {
		producerTemplate.sendBody("direct:testDirect", "payload1")
		assertEquals("payload1", testDirectReceiverService.received)
	}

}