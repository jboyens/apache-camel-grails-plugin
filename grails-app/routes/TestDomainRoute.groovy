class TestDomainRoute {

	def configure = {
		from('direct:testDomain').to('bean:testDomainReceiverService?method=receive')
	}

}