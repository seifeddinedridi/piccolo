package org.piccolo.util;

import com.google.gson.JsonElement;
import java.util.Map;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.piccolo.node.TokenType;

public class TokenJsonNodeMatcher extends BaseMatcher<JsonElement> {

    private final TokenType expectedTokenType;
    private final String expectedName;

    public TokenJsonNodeMatcher(TokenType type, String name) {
        this.expectedTokenType = type;
        this.expectedName = name;
    }

    @Override
    public boolean matches(Object obj) {
        if (!(obj instanceof Map)) {
            return false;
        }
        Map<Object, Object> item = (Map<Object, Object>) obj;
        String actualName = (String) item.get("name");
        String actualType = (String) item.get("type");
        return expectedName.equals(actualName) && TokenType.valueOf(actualType).equals(expectedTokenType);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("Matches TokenNode properties: name=" + expectedName + ", tokenType=" + expectedTokenType);
    }
}
