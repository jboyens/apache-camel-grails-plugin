class DirectRouteTests extends GroovyTestCase {
	
	def testDirectReceiverService
	def camelService
	
	void testSimpleSendMessage() {
		camelService.send "direct:testDirect", "payload1"
		assertEquals("payload1", testDirectReceiverService.received)
	}

}