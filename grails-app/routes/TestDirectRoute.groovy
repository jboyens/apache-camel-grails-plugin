class TestDirectRoute {

	def configure = {
		from('direct:testDirect').to('bean:testDirectReceiverService?method=receive')
	}

}