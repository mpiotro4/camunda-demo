import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.junit.Assert;

public class CamundaSteps {

    private ProcessEngine processEngine;

    private Deployment deployment;
    private ProcessInstance instance;

    private static final String PROCESS_KEY = "Lafayette-process";
    private String deploymentId;

    @Given("a Camunda process engine is set up")
    public void setupProcessEngine() {
        processEngine = ProcessEngines.getDefaultProcessEngine();
    }

    @When("the process is deployed")
    public void deployProcess() {
        DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("process.bpmn");

        deployment = deploymentBuilder.deploy();
        deploymentId = deployment.getId();
    }

    @When("process instance is started")
    public void instantiateProcess() {
        instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(PROCESS_KEY);
    }

    @Then("the process should be deployed")
    public void checkIfDeployed() {
        Assert.assertNotNull("Process should be deployed", deployment);
    }

    @Then("the process should be available")
    public void checkProcessAvailability() {
        ProcessDefinition processDefinition = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(PROCESS_KEY)
                .singleResult();

        Assert.assertNotNull("Process should be available", processDefinition);
    }

}
