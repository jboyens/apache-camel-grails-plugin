class DslEnchancementRoutesTests extends GroovyTestCase {

	def camelService
	def testDslEnhancementsService
	
	void testSend() {
		
		assertNull(testDslEnhancementsService.received1)
		assertNull(testDslEnhancementsService.received2)

		camelService.send "direct:dslEnhancements", 1, foo: "bar", process: false
		assertNull(testDslEnhancementsService.received1)
				
		camelService.send "direct:dslEnhancements", 1, foo: "bar"
		assertEquals(2, testDslEnhancementsService.received1)
				
		camelService.send "direct:dslEnhancements", 2, foo: "notbar"
		assertEquals(4, testDslEnhancementsService.received2)
	}
}