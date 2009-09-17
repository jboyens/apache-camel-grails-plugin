class DslEnhancementsRoute {

	def configure = { 
		
		def processIsNotFalse = predicate { in.headers.process != false }
		
		"direct:dslEnhancements" {
			filter processIsNotFalse
			process { in.body *= 2 }
			choice {
				when { in.headers.foo == "bar" } {
					to "bean:dslEnhancementsService?method=receive1"
				}
				otherwise {
					to "bean:dslEnhancementsService?method=receive2"
				}	
			}
		}
	}	

}