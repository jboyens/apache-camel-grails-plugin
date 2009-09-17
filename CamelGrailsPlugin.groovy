import org.ix.grails.plugins.camel.*
import org.apache.camel.spring.CamelContextFactoryBean
import org.apache.camel.spring.CamelProducerTemplateFactoryBean
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class CamelGrailsPlugin {

    def description = '''\\
This plugin provides an integration with Apache Camel (http://camel.apache.org), giving Controllers and Services 'camelSend()' and 'camelRequest()' methods that will send
a message to a given endpoint.

It also adds a 'Route' artifact that allows configuration of Camel routing using the Java DSL. New Routes can be
added with the 'grails create-route MyRouteName' command.
'''
	def title = "Integration with Apache Camel"
	def author = "Chris Navta"
    def authorEmail = "chris@ix-n.com"
	def documentation = "http://grails.org/Camel+Plugin"
    def version = "0.2"
    def grailsVersion = "1.1 > *"
    def dependsOn = [:]
    def loadAfter = ['controllers','services','domainClass']
	def artefacts = [new RouteArtefactHandler()]
    
	def watchedResources = [
		"file:./grails-app/controllers/**/*Controller.groovy",
		"file:./grails-app/services/**/*Service.groovy"
	]

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

    def doWithDynamicMethods = { ctx ->
		def producerTemplate = ctx.getBean('producerTemplate')
		ProducerConvenienceMethodDecorator.decorate(producerTemplate, application.controllerClasses)
		ProducerConvenienceMethodDecorator.decorate(producerTemplate, application.serviceClasses)
    }

    def onChange = { event ->
    	def artifactName = "${event.source.name}"
    	log.debug "Detected a change in ${artifactName}"

		def artefactClass
		def isSendMessageTarget
		
        if (application.isControllerClass(event.source)) {
			isSendMessageTarget = true
			artefactClass = application.getControllerClass(artifactName)
		} else if (application.isServiceClass(event.source)) {
			isSendMessageTarget = true
			artefactClass = application.getServiceClass(artifactName)
		}

		if (isSendMessageTarget) {
			def producerTemplate = event.ctx.getBean('producerTemplate')
			ProducerConvenienceMethodDecorator.decorate(producerTemplate, artefactClass)
		}
    }
}
