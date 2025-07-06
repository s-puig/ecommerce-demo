package com.demo.ecommerce.users;

import com.demo.ecommerce.common.annotations.ImportTestContext;
import com.demo.ecommerce.users.dto.UserPublicData;
import com.fasterxml.jackson.databind.ObjectMapper;
/*import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;*/
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.EnumSet;
import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Tag("Unit")
@ImportTestContext(UserMapperImpl.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @Test
    void testController() {
        assert(true);
    }

    @Test
    void get_returnUserPublic() throws Exception {
        Long userId = 10L;
        String name = "Test";
        String email = "test@outlook.com";

        User user = new User(userId, name, email, "test", EnumSet.of(Role.CUSTOMER));
        UserPublicData userDto = new UserPublicData(userId, name, email);

        when(userService.find(10L)).thenReturn(Optional.of(user));

        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userDto)));
    }
}
