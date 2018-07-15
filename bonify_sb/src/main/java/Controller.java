
import dto.User;
import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@EnableAutoConfiguration
public class Controller {

    Map<String, User> userMap = new HashMap<String, User>();
    @Value("${type}")
    private String type;

    @RequestMapping("/process")
    public String processingType() {

        return "Processed " + type;
    }

    @RequestMapping("/users/{user_name}/info/{info_query}")
    public String usersInfo(@PathVariable("user_name") String user_name, @PathVariable("info_query") String info_query) {
        System.out.println("user_name : " + user_name);
        System.out.println("info_query : " + info_query);
        StringBuilder response = new StringBuilder();
        createUsersDataMap();
        User user = userMap.get(user_name);
        if (user != null) {
            try {
                String info = (String) PropertyUtils.getProperty(user, info_query);
                response.append("name: ").append(user_name).append("<br />\n").append(info_query).append(": ").append(info);
            } catch (Exception e) {
                response.append("Exception occurred: Incorrect information query");
                System.out.println(e.toString());
            }
        } else {
            response.append("User not found");
        }
        return response.toString();
    }

    private void createUsersDataMap() {
        User marcelo = new User("Marcelo", "Berlin", "01/01/1991");
        User maria = new User("Maria", "Munich", "05/05/1993");
        userMap.put("Marcelo", marcelo);
        userMap.put("Maria", maria);
    }

    public static void main(String[] args) throws Exception {
        for (String arg : args) {
            System.out.println(arg);
        }

        SpringApplication.run(Controller.class, args);
    }
}
