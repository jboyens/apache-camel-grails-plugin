class DslEnhancementsRoute {

	def configure = { 
		"direct:dslEnhancements" {
			to "bean:dslEnhancementsService?method=decorate"
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