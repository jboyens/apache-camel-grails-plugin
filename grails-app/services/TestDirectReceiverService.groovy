class TestDirectReceiverService {

	def received = null
	
	def receive(payload) {
		received = payload
	}

}