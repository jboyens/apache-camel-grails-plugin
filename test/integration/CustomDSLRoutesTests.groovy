class CustomDSLRoutesTests extends GroovyTestCase {
	
	def testDirectReceiverService
	def camelService
	
	void testSimpleSendMessage() {
		camelService.send "direct:testCustomDSL", "payload2"
		assertEquals("payload2", testDirectReceiverService.received)
	}

}