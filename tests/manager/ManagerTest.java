package manager;

import org.junit.Test;

import fr.uv1.bettingServices.Manager;
import fr.uv1.bettingServices.exceptions.AuthenticationException;

/**
 *
 */
public class ManagerTest {

    private static final String MANAGER_PWD = "";
    private static final String WRONG_PWD = "wrong_pwd";
    private Manager m=new Manager();



    @Test
    public void testAuthenticateMngr() throws AuthenticationException {

        m.authenticateMngr(MANAGER_PWD);
    }

    @Test(expected = AuthenticationException.class)
    public void testWrongPwd() throws Exception {
        m.authenticateMngr(WRONG_PWD);
    }
}
