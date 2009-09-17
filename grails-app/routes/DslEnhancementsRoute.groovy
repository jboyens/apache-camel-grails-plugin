class DslEnhancementsRoute {

	def configure = { 
		"direct:dslEnhancements" {
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