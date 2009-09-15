import org.apache.camel.spring.CamelContextFactoryBean
import org.ix.grails.plugins.camel.*
import org.ix.test.*
import grails.util.GrailsNameUtils
import org.apache.camel.model.ProcessorDefinition
import org.apache.camel.model.ChoiceDefinition
import org.apache.camel.spring.spi.SpringTransactionPolicy
import org.apache.camel.spring.CamelContextFactoryBean
import org.apache.camel.spring.CamelProducerTemplateFactoryBean
import org.apache.camel.language.groovy.CamelGroovyMethods
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
This plugin provides an integration with Apache Camel (http://camel.apache.org), giving Controllers and Services a 'sendMessage' method that will send
a message to a given endpoint.

It also adds a 'Route' artifact that allows configuration of Camel routing using the Java DSL. New Routes can be
added with the 'grails create-route MyRouteName' command.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/Camel+Plugin"

    def doWithSpring = {
		init()
		
		propagationRequiredTransactionPolicy(SpringTransactionPolicy) {
			transactionManager = ref("transactionManager")
		}
		
		propagationRequiresNewTransactionPolicy(SpringTransactionPolicy) {
			transactionManager = ref("transactionManager")
			propagationBehaviorName = "PROPAGATION_REQUIRES_NEW"
		}
		
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

				"$routeBuilderBeanName"(GrailsRouteBuilderFactoryBean) {
					routeClass = ref(routeClassBeanName)
					propagationRequiredTransactionPolicy = propagationRequiredTransactionPolicy
					propagationRequiresNewTransactionPolicy = propagationRequiresNewTransactionPolicy
				}
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

    def doWithWebDescriptor = { xml ->
    }

    def doWithDynamicMethods = { ctx ->
    	this.addMethods(application.controllerClasses,ctx);
    	this.addMethods(application.serviceClasses,ctx);
    }

    def onChange = { event ->
    	def artifactName = "${event.source.name}"
    	log.debug "Detected a change in ${artifactName}"
        if (artifactName.endsWith('Controller') || artifactName.endsWith('Service'))
        {
			def artifactType = (artifactName.endsWith('Controller')) ? 'controller' : 'service'
			log.debug "It's a ${artifactType}"
			def grailsClass = application."${artifactType}Classes".find { it.fullName == artifactName }
			this.addMethods([grailsClass],event.ctx)
		}
    }

    def onConfigChange = { event ->

    }

    private init() {
    	log.debug "Adding Groovy-ish methods to RouteBuilder Helpers"
    	ProcessorDefinition.metaClass.filter = {filter ->
			if (filter instanceof Closure) {
				filter = CamelGroovyMethods.toExpression(filter)
			}
			delegate.filter(filter);
		}

		ChoiceDefinition.metaClass.when = {filter ->
			if (filter instanceof Closure) {
				filter = CamelGroovyMethods.toExpression(filter)
			}
			delegate.when(filter);
		}

		ProcessorDefinition.metaClass.process = {filter ->
			if (filter instanceof Closure) {
				filter = new ClosureProcessor(filter)
			}
			delegate.process(filter);
		}
	}

	private addMethods(artifacts,ctx) {
		log.debug "Adding dynamic methods to ${artifacts}"
		def template = ctx.getBean('producerTemplate')

        artifacts.each { artifact ->
        	artifact.metaClass.sendMessage = { endpoint,message ->
        		template.sendBody(endpoint,message)
			}
			artifact.metaClass.requestMessage = { endpoint,message ->
        		template.requestBody(endpoint,message)
			}
		}
	}
}
