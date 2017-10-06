package be.deschutter.scimitar.user;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private ScimitarUserEao scimitarUserEao;
    @Test
    public void findBy() throws Exception {
        final ScimitarUser user = new ScimitarUser();
        when(scimitarUserEao.findByUsernameIgnoreCase("username")).thenReturn(
            user);
        assertThat(userService.findBy("username")).isSameAs(user);
    }

    @Test(expected = UserNotFoundException.class)
    public void findBy_NotFound() throws Exception {
       try {
           userService.findBy("username");
       } catch (UserNotFoundException e) {
           assertThat(e.getUsername()).isEqualTo("username");
           throw e;
       }
       fail("Should have thrown an exception");
    }

}