class DslEnhancementsRoute {

	def configure = { 
		
		def processIsNotFalse = predicate { in.headers.process != false }
		
		"direct:dslEnhancements" {
			filter processIsNotFalse
			process { it.in.body *= 2 }
			choice {
				when { header("foo").isEqualTo("bar") } {
					to "bean:dslEnhancementsService?method=receive1"
				}
				otherwise {
					to "bean:dslEnhancementsService?method=receive2"
				}	
			}
		}
	}	

}