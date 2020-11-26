package fr.nextdigital.lab.account.worker;

import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import fr.nextdigital.lab.account.worker.domain.AccountStatus;
import fr.nextdigital.lab.account.worker.event.AccountEvent;
import fr.nextdigital.lab.account.worker.event.AccountEventType;
import fr.nextdigital.lab.account.worker.state.StateMachineService;

@RunWith(SpringRunner.class)
@ActiveProfiles("docker")
@SpringBootTest
public class MachineStatusTests {
	@Autowired
	StateMachineService smf;
	@Test
	public void testMachineState(){
		StateMachine<AccountStatus, AccountEventType> sm=smf.getStateMachine();//.getgetStateMachine(UUID.randomUUID());
		//sm.start();
		sm.sendEvent(AccountEventType.ACCOUNT_CREATED);
		System.out.println("Account created: "+sm.getStates().toString());
		sm.sendEvent(AccountEventType.ACCOUNT_CONFIRMED);
		System.out.println("Account created: "+sm.getStates().toString());
	
		//System.out.println(sm.getTransitions().);
		sm.stop();
		
	}

}
