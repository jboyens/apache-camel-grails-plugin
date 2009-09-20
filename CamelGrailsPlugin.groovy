import grails.plugins.camel.GrailsRouteBuilder
import org.codehaus.groovy.grails.plugins.camel.RoutesArtefactHandler
import org.codehaus.groovy.grails.plugins.camel.RouteBuilderFactoryBean
import org.apache.camel.spring.CamelContextFactoryBean
import org.apache.camel.spring.CamelProducerTemplateFactoryBean
import org.apache.camel.spring.CamelConsumerTemplateFactoryBean
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class CamelGrailsPlugin {

	def title = "Integration with Apache Camel"
    def description = 'This plugin provides integration with Apache Camel (http://camel.apache.org). It features a service for sending messages, and a Groovy DSL for defining routes.'
	def documentation = "http://grails.org/Camel+Plugin"
	def version = "0.3-RC3"
	def author = "Chris Navta & Luke Daley"
    def authorEmail = "chris@ix-n.com"

    def grailsVersion = "1.1 > *"
	def artefacts = [new RoutesArtefactHandler()]

	def pluginExcludes = [
		"grails-app/routes/**",
		"grails-app/services/**/Test*",
		"grails-app/conf/**",
		"grails-app/domain/**"
    ]

    def doWithSpring = {
		def routeBuilderBeanNames = []
		
		application.routesClasses.each {
			def routesClassName = it.fullName
			def routesClassBeanName = "${routesClassName}RoutesClass"
			def routeBuilderBeanName = "${routesClassName}Builder"
			routeBuilderBeanNames << routeBuilderBeanName
			
			"$routesClassBeanName"(MethodInvokingFactoryBean) {
				targetObject = ref("grailsApplication",true)
				targetMethod = "getArtefact"
				arguments = [RoutesArtefactHandler.TYPE, routesClassName]
			}

			"$routeBuilderBeanName"(RouteBuilderFactoryBean, ref(routesClassBeanName))
		}

		camelContext(CamelContextFactoryBean) {
			routeBuilders = routeBuilderBeanNames.collect { ref(it) }
			shouldStartContext = false
		}
		
		camelProducerTemplate(CamelProducerTemplateFactoryBean) {
			camelContext = camelContext
		}
		
		camelConsumerTemplate(CamelConsumerTemplateFactoryBean) {
			camelContext = camelContext
		}
    }
	
    def doWithApplicationContext = { applicationContext ->
		applicationContext.getBean("camelContext").with {
			shouldStartContext = true
			start()
		}
    }

}
