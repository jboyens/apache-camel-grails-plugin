class CamelService {

	def camelProducerTemplate

	def getProducer() {
		camelProducerTemplate
	}
	
	def send(endpoint, body = null) {
		camelProducerTemplate.sendBody(endpoint, body)
	}
	
	def send(Map headers, endpoint, body = null) {
		camelProducerTemplate.sendBodyAndHeaders(endpoint, body, headers)
	}
	
	def request(endpoint, body = null) {
		camelProducerTemplate.requestBody(endpoint, body)
	}
	
	def request(Map headers, endpoint, body = null) {
		camelProducerTemplate.requestBodyAndHeaders(endpoint, body, headers)
	}
	
}