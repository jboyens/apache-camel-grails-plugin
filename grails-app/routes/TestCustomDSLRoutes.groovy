class TestCustomDSLRoutes {

	def routes = { 
		'direct:testCustomDSL' {
			to 'bean:testDirectReceiverService?method=receive'
		}
	}

}