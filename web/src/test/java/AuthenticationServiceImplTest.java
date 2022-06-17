import com.epam.esm.dao.entity.User;
import com.epam.esm.service.UserService;
import com.epam.esm.service.exception.ServiceException;
import com.epam.esm.controller.config.security.jwt.AuthenticationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    private final String LOGIN = "TestUser";
    private final String PASSWORD = "12345";
    @Mock
    private UserService mockUserService;
    @Mock
    private AuthenticationManager mockAuthenticationManager;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Test
    void testLogin() {
        User user = new User(LOGIN,LOGIN, PASSWORD);
        String expected = "TestUser";

        when(mockAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD))).thenReturn(null);
        when(mockUserService.findUserByLogin(LOGIN)).thenReturn(user);

        User actual = authenticationService.login(LOGIN, PASSWORD);

        assertEquals(expected, actual.getLogin());
    }

    @Test
    void testLogin_AuthenticationDataException() {
        when(mockAuthenticationManager.authenticate(new UsernamePasswordAuthenticationToken(LOGIN, PASSWORD))).thenThrow(new ServiceException());

        assertThrows(ServiceException.class, () -> authenticationService.login(LOGIN, PASSWORD));
    }
}