package uk.co.codera.lang.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import uk.co.codera.lang.Announcer;
import uk.co.codera.lang.Announcer.ExceptionHandler;

@RunWith(value = MockitoJUnitRunner.class)
public class AnnouncerTest {

    public static class CheckedException extends Exception {
        private static final long serialVersionUID = 1L;
    }

    public interface Listener {
        void eventA();

        void eventB();

        void eventWithArguments(int a, int b);

        void badEvent() throws Exception;
    }

    private Announcer<Listener> announcer;

    @Mock
    private Listener listener1;

    @Mock
    private Listener listener2;

    @Mock
    private ExceptionHandler exceptionHandler;

    @Before
    public void setUp() {
        this.announcer = announcerWithDefaultExceptionPolicyAndListenersRegistered().useExceptionHandler(
                this.exceptionHandler);
    }

    @Test
    public void shouldBeAbleToConstructAnnouncerWithMultipleListenersUpFront() {
        assertThat(Announcer.to(Listener.class, Arrays.asList(this.listener1, this.listener2)).numberListeners(), is(2));
    }

    @Test
    public void shouldReportNumberOfListeners() {
        assertThat(this.announcer.numberListeners(), is(2));
    }

    @Test
    public void testAnnouncesToRegisteredListenersInOrderOfAddition() {
        InOrder inOrder = inOrder(this.listener1, this.listener2);

        this.announcer.announce().eventA();
        this.announcer.announce().eventB();

        inOrder.verify(this.listener1).eventA();
        inOrder.verify(this.listener2).eventA();
        inOrder.verify(this.listener1).eventB();
        inOrder.verify(this.listener2).eventB();
    }

    @Test
    public void testCanAddMultipleListenersInOneGo() {
        Listener newListener1 = mock(Listener.class);
        Listener newListener2 = mock(Listener.class);
        this.announcer.addListeners(Arrays.asList(newListener1, newListener2));

        this.announcer.announce().eventA();

        verify(newListener1).eventA();
        verify(newListener2).eventA();
    }

    @Test
    public void testPassesEventArgumentsToListeners() {
        this.announcer.announce().eventWithArguments(1, 2);
        this.announcer.announce().eventWithArguments(3, 4);

        verify(this.listener1).eventWithArguments(1, 2);
        verify(this.listener2).eventWithArguments(1, 2);
        verify(this.listener1).eventWithArguments(3, 4);
        verify(this.listener2).eventWithArguments(3, 4);
    }

    @Test
    public void testCanRemoveListeners() {
        this.announcer.removeListener(this.listener1);
        this.announcer.announce().eventA();

        verify(this.listener1, never()).eventA();
        verify(this.listener2).eventA();
    }

    @Test
    public void testRuntimeExceptionThrownByListenerIsPassedToExceptionHandler() {
        RuntimeException exceptionToThrow = new IllegalStateException();
        doThrow(exceptionToThrow).when(this.listener1).eventA();

        this.announcer.announce().eventA();

        verify(this.exceptionHandler).onException(exceptionToThrow);
    }

    @Test
    public void testCheckedExceptionThrownByListenerIsPassedToExceptionHandler() throws Exception {
        CheckedException exceptionToThrow = new CheckedException();
        doThrow(exceptionToThrow).when(this.listener1).badEvent();

        this.announcer.announce().badEvent();

        verify(this.exceptionHandler).onException(exceptionToThrow);
    }

    @Test
    public void testExceptionThrownByListenerDoesNotStopOtherListenersBeingInvoked() {
        doThrow(new IllegalArgumentException()).when(this.listener1).eventA();

        announcerWithDefaultExceptionPolicyAndListenersRegistered().announce().eventA();

        verify(this.listener2).eventA();
    }

    @Test
    public void testErrorIsThrownAndStopsOtherListenersBeingInvoked() {
        doThrow(new Error()).when(this.listener1).eventA();

        try {
            announcerWithDefaultExceptionPolicyAndListenersRegistered().announce().eventA();
            fail("expected error to be thrown");
        } catch (Error e) {
        }

        verify(this.listener2, never()).eventA();
    }

    private Announcer<Listener> announcerWithDefaultExceptionPolicyAndListenersRegistered() {
        return Announcer.to(Listener.class).addListener(listener1).addListener(listener2);
    }
}