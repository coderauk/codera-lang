package uk.co.codera.lang.concurrent;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.hamcrest.CoreMatchers;
import org.junit.Test;

public class TasksTest {

	@Test
	public void simpleTasksShouldImplementCommandExecutorInterface() {
		assertThat(Tasks.aTask().build(), is(instanceOf(CommandExecutor.class)));
	}
	
	@Test
	public void shouldBeAbleToRetrieveCommandFromTask() {
		Command command = aCommand();
		Task task = Tasks.aTask().with(command).build();
		assertThat(((CommandExecutor)task).getCommand(), CoreMatchers.is(command));
	}
	
	private Command aCommand() {
		return () -> System.out.println("hello");
	}
}