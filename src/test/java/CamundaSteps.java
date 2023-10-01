import com.example.workflow.ReserveSeatOnBoat;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.junit.Assert;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.*;

public class CamundaSteps {
    @Before
    public void setUp() {
        Mocks.register("reserveSeatOnBoat", new ReserveSeatOnBoat());
    }
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
    @Given("Lafayette want to be involved in revolution")
    public void lafayetteWantToBeInvolvedInRevolution(){
        setupProcessEngine();
        deployProcess();
        checkIfDeployed();
        instantiateProcess();
        checkProcessAvailability();

        assertThat(instance)
                .isActive()
                .hasPassed("StartEvent_1")
                .isWaitingAtExactly("say-hello")
                .task().isNotAssigned();
    }

    @When("^Prepare for departure to (.*) with (.*) money$")
    public void prepareForDeparture(String country, String money){
        complete(task(), withVariables(
                "country", country,
                "money", money
        ));
    }

    @Then("Book a place on ship")
    public void bookPlaceOnShip() {
        assertThat(instance)
                .isActive()
                .hasPassed("Gateway_1ubbpc3")
                .isWaitingAtExactly("overthrowTheMonarchy")
                .task().isNotAssigned();
    }

    @When("Overthrow the monarchy")
    public void overthrowMonarchy() {
        assertThat(instance)
                .isActive()
                .hasPassed("Gateway_1ubbpc3")
                .isWaitingAtExactly("overthrowTheMonarchy");
    }

    @Then("Lafayette has helped a revolution")
    public void helpedRevolution() {
        complete(task());
        assertThat(instance)
                .hasPassed("overthrowTheMonarchy")
                .isEnded();
    }

    @When("Send a letter back to your wife")
    public void sendLetter() throws InterruptedException {
        assertThat(instance)
                .isWaitingAt("sendLetter");
    }

    public void someFunction(){
        processEngine = ProcessEngines.getDefaultProcessEngine();

        DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("7.12-bpmn-dmn-files/testCaseSample.bpmn");

        Deployment deployment = deploymentBuilder.deploy();
        deploymentId = deployment.getId();

        ProcessInstance instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(PROCESS_KEY);

        ProcessDefinition processDefinition = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(PROCESS_KEY)
                .singleResult();

//        ProcessInstance instance = rule.getRuntimeService().startProcessInstanceByKey(PROCESS_KEY);

        assertThat(instance)
                .isActive()
                .hasPassed("startEvent")
                .isWaitingAtExactly("userTask1")
                .task().isNotAssigned();

        complete(task(), withVariables(
                "assignPerson", "dpoint",
                "attribute1", "value1"
        ));

        assertThat(instance)
                .hasPassed("userTask1")
                .hasPassedInOrder("userTask1", "serviceTask1")
                .isWaitingAt("userTask2")
                .task().isAssignedTo("dpoint");

        complete(task(), withVariables("attributeService", "variableServicevalue"));
        assertThat(instance)
                .hasPassedInOrder("userTask2", "endEvent")
                .isEnded();


        Assert.assertNotNull("Process should be deployed", processDefinition);

    }
}
