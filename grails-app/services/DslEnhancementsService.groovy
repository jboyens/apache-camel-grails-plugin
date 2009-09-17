class DslEnhancementsService {

	def received1
	def received2
	
	def receive1(packet) {
		received1 = packet
	}

	def receive2(packet) {
		received2 = packet
	}
	
	def decorate(packet) {
		packet * 2
	}
	
}