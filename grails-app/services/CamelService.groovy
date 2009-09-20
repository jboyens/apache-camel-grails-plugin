class CamelService {

	def producerTemplate

	def send(endpoint, body = null) {
		producerTemplate.sendBody(endpoint, body)
	}
	
	def send(Map headers, endpoint, body = null) {
		producerTemplate.sendBodyAndHeaders(endpoint, body, headers)
	}
	
	def request(endpoint, body = null) {
		producerTemplate.requestBody(endpoint, body)
	}
	
	def request(Map headers, endpoint, body = null) {
		producerTemplate.requestBodyAndHeaders(endpoint, body, headers)
	}
	
}