import com.example.workflow.Application;
import com.example.workflow.ReserveSeatOnBoat;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.repository.DeploymentBuilder;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.repository.Deployment;
import org.camunda.bpm.engine.test.mock.Mocks;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.*;
//@SpringBootTest(classes = Application.class)
//@RunWith(SpringRunner.class)
public class testCaseSample extends AbstractProcessEngineRuleTest{
    @Before
    public void setUp() {
        Mocks.register("reserveSeatOnBoat", new ReserveSeatOnBoat());
    }
    private ProcessEngine processEngine;

    private Deployment deployment;
    private ProcessInstance instance;

    private static final String PROCESS_KEY = "Lafayette-process";
    private String deploymentId;


//    @Given("a Camunda process engine is set up")
    public void setupProcessEngine() {
        processEngine = ProcessEngines.getDefaultProcessEngine();
    }

//    @When("the process is deployed")
    public void deployProcess() {
        DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("process.bpmn");

        deployment = deploymentBuilder.deploy();
        deploymentId = deployment.getId();
    }

//    @When("process instance is started")
    public void instantiateProcess() {
        instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(PROCESS_KEY);
    }

//    @Then("the process should be deployed")
    public void checkIfDeployed() {
        Assert.assertNotNull("Process should be deployed", deployment);
    }

//    @Then("the process should be available")
    public void checkProcessAvailability() {
        ProcessDefinition processDefinition = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(PROCESS_KEY)
                .singleResult();

        Assert.assertNotNull("Process should be available", processDefinition);
    }

    @Test
    public void test(){
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
        complete(task(), withVariables(
                "country", "America",
                "money", "1000.0"
        ));

        assertThat(instance)
                .isActive()
                .hasPassed("Gateway_1ubbpc3")
                .isWaitingAtExactly("overthrowTheMonarchy")
                .task().isNotAssigned();

        assertThat(instance)
                .isActive()
                .hasPassed("Gateway_1ubbpc3")
                .isWaitingAtExactly("overthrowTheMonarchy");

        complete(task());
        assertThat(instance)
                .hasPassed("overthrowTheMonarchy")
                .isEnded();
    }
    @Test
//    @Deployment(resources = {"7.12-bpmn-dmn-files/testCaseSample.bpmn"})
    public void testSampleCase_happyPath() {

        processEngine = ProcessEngines.getDefaultProcessEngine();

        DeploymentBuilder deploymentBuilder = processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("process.bpmn");
        deployment = deploymentBuilder.deploy();
        deploymentId = deployment.getId();

        Assert.assertNotNull("Process should be deployed", deployment);
        instance = processEngine.getRuntimeService()
                .startProcessInstanceByKey(PROCESS_KEY);

        ProcessDefinition processDefinition = processEngine.getRepositoryService()
                .createProcessDefinitionQuery()
                .processDefinitionKey(PROCESS_KEY)
                .singleResult();

        Assert.assertNotNull("Process should be available", processDefinition);


        assertThat(instance)
                .isActive()
                .hasPassed("StartEvent_1")
                .isWaitingAtExactly("say-hello")
                .task().isNotAssigned();

        complete(task(), withVariables(
                "country", "America",
                "money", "100.0"
        ));
    }
}
