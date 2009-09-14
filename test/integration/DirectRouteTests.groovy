class DirectRouteTests extends GroovyTestCase {
	
	def testDirectReceiverService
	def producerTemplate
	
	void testSimpleSendMessage() {
		producerTemplate.sendBody("direct:test", "payload1")
		assertEquals("payload1", testDirectReceiverService.received)
	}

}