class TestDomainRoutes {

	def routes = {
		from('direct:testDomain').to('bean:testDomainReceiverService?method=receive')
	}

}