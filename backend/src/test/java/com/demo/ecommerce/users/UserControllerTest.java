package com.demo.ecommerce.users;

import com.demo.ecommerce.users.dto.UserPublicData;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.Mockito.when;

@Tag("Unit")
@Import(UserMapperImpl.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    /*@MockitoBean
    private UserMapper userMapper;*/

    @Test
    void testController() {
        assert(true);
    }

    @Test
    void get_returnUserPublic() {
        Long userId = 10L;
        String name = "Test";
        String email = "test@outlook.com";

        User user = new User(userId, name, email, "test", EnumSet.of(Role.CUSTOMER));
        UserPublicData userDto = new UserPublicData(userId, name, email);

        when(userService.find(10L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/{id}", userId))
    }
}
