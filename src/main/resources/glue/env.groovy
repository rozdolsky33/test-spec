import cucumber.runtime.ScenarioImpl
import cucumber.runtime.model.CucumberFeature
import cucumber.runtime.model.CucumberTagStatement

import gherkin.formatter.PrettyFormatter
import gherkin.formatter.model.Background
import gherkin.formatter.model.Result
import gherkin.formatter.model.TagStatement
import org.edushak.testspec.Scenario
import org.edushak.testspec.util.ElasticClient

import static cucumber.api.groovy.Hooks.Before
import static cucumber.api.groovy.Hooks.After

long scenarioEndTime, scenarioStartTime

Before() { ScenarioImpl scenario ->
    scenarioStartTime = System.currentTimeMillis()

/*
    // if (user asks for it, load browser)
    String configFilePath = 'GebConfig.groovy' // rename
    configuration = new ConfigurationLoader().getConf(configFilePath)
    theBrowser = new Browser(configuration)

    try {
        binding.variables.putAll(configuration.rawConfig)
        bindingUpdater = new BindingUpdater(binding, theBrowser)
        bindingUpdater.initialize()
    } catch (DriverCreationException dce) {
        // incompatibility between driver and browser
    } catch (IllegalStateException ise) {
        // driver cannot be found ?
    }

    if (loadingFirstTime) {
        // grab browser name & version and set to ElasticClient
        // etc
    }
*/
}

After() { ScenarioImpl scenario ->
    scenarioEndTime = System.currentTimeMillis()
    long scenarioDuration = scenarioEndTime - scenarioStartTime
    bindingUpdater?.remove()

    List tags = scenario.sourceTagNames.collect { it.toLowerCase() }

    Result failedResult = scenario.stepResults.find { it.status == 'failed' }
    if (failedResult) {
        // String featureLine = failedResult.error.stackTrace.last()
        // todo: capture screenshot if failed on web step
    }

    publishToElastic(scenario, scenarioDuration)
}

def publishToElastic(ScenarioImpl scenario, long scenarioDuration) {
    if (ElasticClient.instance.isActive()) {
        // save currently executed feature in Main
        CucumberFeature currentFeature = null
        CucumberTagStatement currentScenario = null

        String scenarioSource = getScenarioSource(currentScenario)
        String errorMessage = getErrorMessage(scenario)

        List tags = fetchTags(scenario)

        ElasticClient.instance << new Scenario(
            featureFile: (currentFeature.path as File)?.name ?: '',
            featureName: currentFeature.gherkinFeature.name ?: '',
            scenarioSource: scenarioSource,
            scenarioName: scenario.name,
            status: scenario.status,
            errorMessage: errorMessage,
            passed: scenario.status == 'passed' ? 1 : 0,
            failed: scenario.status == 'failed' ? 1 : 0,
            execTimeMs: scenarioDuration,
            tags: tags
        )
    }
}

String getErrorMessage(ScenarioImpl scenario) {
    String errorMessage = ''
    if (scenario.failed) {
        for (Result stepResult : scenario.@stepResults) {
            if (stepResult.status == 'failed') {
                errorMessage = stepResult.errorMessage
                break
            }
        }
    }
    return errorMessage
}

String getScenarioSource(CucumberTagStatement scenario) {
    def result = new StringBuilder()
    def formatter = new PrettyFormatter(result, true, false)
    TagStatement model = scenario?.gherkinModel
    if (model instanceof Background) {
        formatter.background(model)
    }
    // ...
    scenario.steps.each {
        formatter.step(it)
    }
    formatter.eof()
    return result.toString()
}

List fetchTags(ScenarioImpl scenario) {
    scenario.sourceTagNames
}