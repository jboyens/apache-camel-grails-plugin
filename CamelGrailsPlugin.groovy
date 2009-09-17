import org.ix.grails.plugins.camel.*
import org.apache.camel.spring.CamelContextFactoryBean
import org.apache.camel.spring.CamelProducerTemplateFactoryBean
import org.springframework.beans.factory.config.MethodInvokingFactoryBean
import org.apache.log4j.Logger

class CamelGrailsPlugin {

	private static final Logger log = Logger.getLogger('org.ix.grails.plugins.camel.CamelGrailsPlugin')

    // the plugin version
    def version = "0.2"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.1 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def loadAfter = ['controllers','services','domainClass']
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]
    def watchedResources = [
            "file:./grails-app/routes/**/*Route.groovy",
            "file:./plugins/*/grails-app/routes/**/*Route.groovy",
            "file:./grails-app/controllers/**/*Controller.groovy",
            "file:./grails-app/services/**/*Service.groovy"
    ]


    def artefacts = [new RouteArtefactHandler()]

    def author = "Chris Navta"
    def authorEmail = "chris@ix-n.com"
    def title = "Integration with Apache Camel"
    def description = '''\\
This plugin provides an integration with Apache Camel (http://camel.apache.org), giving Controllers and Services 'camelSend()' and 'camelRequest()' methods that will send
a message to a given endpoint.

It also adds a 'Route' artifact that allows configuration of Camel routing using the Java DSL. New Routes can be
added with the 'grails create-route MyRouteName' command.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Camel+Plugin"

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
