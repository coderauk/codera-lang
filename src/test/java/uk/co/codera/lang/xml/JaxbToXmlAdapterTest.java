package uk.co.codera.lang.xml;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class JaxbToXmlAdapterTest {

    private JaxbToXmlAdapter<JaxbAnnotatedObject> adapter;

    @Before
    public void before() {
        this.adapter = new JaxbToXmlAdapter<>(JaxbAnnotatedObject.class);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBeAbleToMarshalNonAnnotatedObject() {
        new JaxbToXmlAdapter<>(Object.class).adapt(new Object());
    }

    @Test
    public void shouldTransformToNonNullXmlString() {
        assertThat(adapt(aValidJaxbObject()), is(notNullValue()));
    }

    @Test
    public void shouldTransformToXmlWithDeclaration() {
        assertThat(adapt(aValidJaxbObject()),
                containsString("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>"));
    }

    @Test
    public void shouldHaveElementForProperty() {
        assertThat(adapt(aValidJaxbObject()), containsString("<stringAttribute>"));
    }

    private JaxbAnnotatedObject aValidJaxbObject() {
        JaxbAnnotatedObject object = new JaxbAnnotatedObject();
        object.setStringAttribute("hello");
        return object;
    }

    private String adapt(JaxbAnnotatedObject object) {
        return this.adapter.adapt(object);
    }
}