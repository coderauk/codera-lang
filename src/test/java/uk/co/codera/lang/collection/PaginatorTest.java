package uk.co.codera.lang.collection;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.empty;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;

public class PaginatorTest {

    private static final List<Character> ORIGINAL_LIST = Arrays.asList('a', 'b', 'c', 'd', 'e');

    @Test
    public void shouldReturnEntireListIfPageSizeIsBiggerThanOriginalList() {
        assertThat(Paginator.paginate(ORIGINAL_LIST, 10, 0), is(equalTo(ORIGINAL_LIST)));
    }

    @Test
    public void shouldReturnCorrectNumberOfElementsIfPageSizeIsLessThanOriginalList() {
        assertThat(Paginator.paginate(ORIGINAL_LIST, 2, 0).size(), is(2));
    }

    @Test
    public void shouldReturnCorrectPage() {
        assertThat(Paginator.paginate(ORIGINAL_LIST, 2, 1), is(equalTo(Arrays.asList('c', 'd'))));
    }

    @Test
    public void shouldReturnCollectionLessThanPageSizeIfLastPageAndIsNotEnoughElements() {
        assertThat(Paginator.paginate(ORIGINAL_LIST, 2, 2), is(equalTo(Arrays.asList('e'))));
    }

    @Test
    public void shouldReturnEmptyCollectionIfPageRequestedIsNotValid() {
        assertThat(Paginator.paginate(ORIGINAL_LIST, 2, 3), is(empty()));
    }
}