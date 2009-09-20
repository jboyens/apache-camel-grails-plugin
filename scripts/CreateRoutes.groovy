includeTargets << grailsScript("Init")

target(main: "Creates a new Camel Routes definition.") {
	typeName = "Routes"
	artifactName = "Routes"
	artifactPath = "grails-app/routes"

	createArtifact()
}

setDefaultTarget(main)
