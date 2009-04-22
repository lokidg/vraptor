package br.com.caelum.vraptor.validator;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Validations {

    private final List<String> errors = new ArrayList<String>();

    public <T> void that(T id, Matcher<T> matcher) {
        assertThat(null, id, matcher);
    }

    public <T> void assertThat(String reason, T actual, Matcher<? super T> matcher) {
        if (!matcher.matches(actual)) {
            if (reason != null) {
                errors.add(reason);
            } else {
                Description description = new StringDescription();
                description.appendDescriptionOf(matcher);
                errors.add(description.toString());
            }
        }
    }

    public void assertThat(String reason, boolean assertion) {
        if (!assertion) {
            errors.add(reason);
        }
    }
    
    public List<String> getErrors() {
        return errors;
    }
    
    public void and(List<String> errors) {
        this.errors.addAll(errors);
    }

}