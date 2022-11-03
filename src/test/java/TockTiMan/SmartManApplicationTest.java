package TockTiMan;

import TockTiMan.controller.MemberController;
import TockTiMan.domain.Member;
import TockTiMan.domain.MemberRepository;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SmartManApplicationTest {

    @Test
    void contextLoads() {
    }


}


