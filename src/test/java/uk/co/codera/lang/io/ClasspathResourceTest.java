package uk.co.codera.lang.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class ClasspathResourceTest {

    @Test
    public void shouldBeAbleToReadContentsOfFileThatExists() {
        assertThat(new ClasspathResource("/readme.txt").getAsString(), is("This is a readme file"));
    }
}