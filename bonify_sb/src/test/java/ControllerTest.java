import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Controller.class)

public class ControllerTest {

    @LocalServerPort
    private int port;
    @Autowired
    private TestRestTemplate restTemplate;


    @Test
    public void processingType() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/process",
                String.class)).isEqualTo("Processed Nothing");
    }

    @Test
    public void usersInfo() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/users/Marcelo/info/location",
                String.class)).contains("Berlin");
    }
}