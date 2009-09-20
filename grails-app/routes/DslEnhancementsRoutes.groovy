class DslEnhancementsRoutes {

	def routes = {
		
		def processIsNotFalse = { in.headers.process != false }
		
		"direct:dslEnhancements" {
			filter processIsNotFalse
			process { in.body *= 2 }
			choice {
				when { in.headers.foo == "bar" } {
					to "bean:testDslEnhancementsService?method=receive1"
				}
				otherwise {
					to "bean:testDslEnhancementsService?method=receive2"
				}	
			}
		}
	}	

}