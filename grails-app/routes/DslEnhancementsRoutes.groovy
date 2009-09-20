class DslEnhancementsRoutes {

	def routes = {
		
		def processIsNotFalse = { in.headers.process != false }
		
		"direct:dslEnhancements" {
			
			onException (Exception) {
				to "bean:testDslEnhancementsService?method=exception"
				handled true
			}
			
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