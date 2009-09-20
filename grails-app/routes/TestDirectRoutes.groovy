class TestDirectRoutes {

	def routes = {
		from('direct:testDirect').to('bean:testDirectReceiverService?method=receive')
	}

}