class TestCustomDSLRoute {

	def configure = { 
		'direct:testCustomDSL' {
			to 'bean:testDirectReceiverService?method=receive'
		}
	}

}