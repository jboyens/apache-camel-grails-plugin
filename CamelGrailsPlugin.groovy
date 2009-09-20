import org.ix.grails.plugins.camel.*
import org.apache.camel.spring.CamelContextFactoryBean
import org.apache.camel.spring.CamelProducerTemplateFactoryBean
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class CamelGrailsPlugin {

	def title = "Integration with Apache Camel"
    def description = 'This plugin provides integration with Apache Camel (http://camel.apache.org). It features a service for sending messages, and a Groovy DSL for defining routes.'
	def documentation = "http://grails.org/Camel+Plugin"
	def version = "0.3-RC1"
	def author = "Chris Navta & Luke Daley"
    def authorEmail = "chris@ix-n.com"

    def grailsVersion = "1.1 > *"
	def artefacts = [new RouteArtefactHandler()]

	def pluginExcludes = [
		"grails-app/routes/**",
		"grails-app/services/**",
		"grails-app/conf/**",
		"grails-app/domain/**"
    ]

    def doWithSpring = {
		registerRoutesCamelContextAndProducerTemplate(delegate, application.routeClasses)
    }

	def registerRoutesCamelContextAndProducerTemplate(beanBuilder, routeClasses) {
		def routeBuilderBeanNames = []
		
		routeClasses.each {
			def routeClassName = it.fullName
			def routeClassBeanName = "${routeClassName}RouteClass"
			def routeBuilderBeanName = "${routeClassName}Builder"
			routeBuilderBeanNames << routeBuilderBeanName
			
			beanBuilder.with {
				"$routeClassBeanName"(MethodInvokingFactoryBean) {
					targetObject = ref("grailsApplication",true)
					targetMethod = "getArtefact"
					arguments = [RouteArtefactHandler.TYPE, routeClassName]
				}

				"$routeBuilderBeanName"(GrailsRouteBuilder, ref(routeClassBeanName))
			}
		}

		beanBuilder.with {
			camelContext(CamelContextFactoryBean) {
				routeBuilders = routeBuilderBeanNames.collect { ref(it) }
				shouldStartContext = false
			}
			
			producerTemplate(CamelProducerTemplateFactoryBean) {
				camelContext = camelContext
			}
		}
	}
	
    def doWithApplicationContext = { applicationContext ->
		applicationContext.getBean("camelContext").with {
			shouldStartContext = true
			start()
		}
    }

}
