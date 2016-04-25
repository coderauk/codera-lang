package uk.co.codera.lang.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ClasspathResourceTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBeAbleToReadContentsOfFileThatExists() {
        assertThat(new ClasspathResource("/readme.txt").getAsString(), is("This is a readme file"));
    }

    @Test
    public void shouldWrapRuntimeExceptionInIllegalStateException() {
        this.expectedException.expect(IllegalStateException.class);
        new ClasspathResource("not-existing.xml").getAsString();
    }
}