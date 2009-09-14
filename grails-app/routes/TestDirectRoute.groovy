class TestDirectRoute {

	def configure = {
		from('direct:test').to('bean:testDirectReceiverService?method=receive')
	}

}