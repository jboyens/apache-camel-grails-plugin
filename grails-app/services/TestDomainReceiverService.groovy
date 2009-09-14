class TestDomainReceiverService {

	def transactional = false
	
	def receive(payload) {
		def message
		Message.withTransaction {
			message = new Message(body: payload)
			if (!message.save()) {
				throw new RuntimeException("${message.errors}")
			}
		}
		message
	}

}