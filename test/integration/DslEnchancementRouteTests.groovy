class DslEnchancementRouteTests extends GroovyTestCase {

	def camelService
	def dslEnhancementsService
	
	void testSend() {
		
		assertNull(dslEnhancementsService.received1)
		assertNull(dslEnhancementsService.received2)

		camelService.send "direct:dslEnhancements", 1, foo: "bar", process: false
		assertNull(dslEnhancementsService.received1)
				
		camelService.send "direct:dslEnhancements", 1, foo: "bar"
		assertEquals(2, dslEnhancementsService.received1)
				
		camelService.send "direct:dslEnhancements", 2, foo: "notbar"
		assertEquals(4, dslEnhancementsService.received2)
	}
}