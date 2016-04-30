package uk.co.codera.lang.io;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ClasspathResourceTest {

    private static final String FILENAME_THAT_EXISTS = "/readme.txt";
    private static final String FILENAME_THAT_DOES_NOT_EXIST = "not-existing.xml";

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void shouldBeAbleToReadContentsOfFileThatExists() {
        assertThat(new ClasspathResource(FILENAME_THAT_EXISTS).getAsString(), is("This is a readme file"));
    }

    @Test
    public void shouldBeAbleToGetResourceThatExistsAsStream() {
        assertThat(new ClasspathResource(FILENAME_THAT_EXISTS).getAsStream(), is(notNullValue()));
    }

    @Test
    public void shouldGetNullStreamForResourceThatDoesNotExist() {
        assertThat(new ClasspathResource(FILENAME_THAT_DOES_NOT_EXIST).getAsStream(), is(nullValue()));
    }

    @Test
    public void shouldWrapRuntimeExceptionInIllegalStateException() {
        this.expectedException.expect(IllegalStateException.class);
        new ClasspathResource(FILENAME_THAT_DOES_NOT_EXIST).getAsString();
    }
}