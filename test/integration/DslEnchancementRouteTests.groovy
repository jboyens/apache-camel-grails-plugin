class DslEnchancementRouteTests extends GroovyTestCase {

	def producerTemplate
	def dslEnhancementsService
	
	void testSend() {
		
		assertNull(dslEnhancementsService.received1)
		assertNull(dslEnhancementsService.received2)

		producerTemplate.sendBodyAndHeaders("direct:dslEnhancements", 1, [foo: "bar"])
		assertEquals(2, dslEnhancementsService.received1)
				
		producerTemplate.sendBodyAndHeaders("direct:dslEnhancements", 2, [foo: "notbar"])
		assertEquals(4, dslEnhancementsService.received2)
	}
}