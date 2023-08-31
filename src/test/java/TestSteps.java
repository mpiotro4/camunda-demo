import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineTests.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;


public class TestSteps {
    @Given("I have a calculator")
    public void initializeCalculator() {
        // Initialization logic
    }

    @When("I add {int} and {int}")
    public void addNumbers(int num1, int num2) {
        // Logic to add numbers
    }

    @Then("the result should be {int}")
    public void verifyResult(int expected) {
        Assert.assertEquals(5, expected);
    }
}
