class ProducerConvenienceMethodsTests extends GroovyTestCase {

	def testDirectReceiverService
	
	void setUp() {
		testDirectReceiverService.received = null
	}
	
	void testSend() {
		testDirectReceiverService.camelSend "direct:testDirect", "123"
		assertEquals("123", testDirectReceiverService.received)
	}
}