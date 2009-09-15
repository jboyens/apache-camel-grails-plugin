class DslEnchancementRouteTests extends GroovyTestCase {

	def producerTemplate
	def dslEnhancementsService
	
	void testSend() {
		
		assertNull(dslEnhancementsService.received1)
		assertNull(dslEnhancementsService.received2)

		producerTemplate.sendBodyAndHeaders("direct:dslEnhancements", "abc", [foo: "bar"])
		assertEquals("abc", dslEnhancementsService.received1)
				
		producerTemplate.sendBodyAndHeaders("direct:dslEnhancements", "abc", [foo: "notbar"])
		assertEquals("abc", dslEnhancementsService.received2)
	}
}