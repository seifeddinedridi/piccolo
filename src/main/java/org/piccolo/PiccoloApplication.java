package org.piccolo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.piccolo.parser.impl.LanguageParser;
import org.piccolo.node.TokenNode;
import org.piccolo.context.ErrorListener;
import org.piccolo.context.ParsingContext;
import org.piccolo.exception.ParsingException;

public class PiccoloApplication {

    public static void main(String[] args) {
        LanguageParser parser = new LanguageParser();
        ParsingContext context = new ParsingContext(new ErrorListener());
        TokenNode moduleNode;
        try {
            String code = Files.readString(Paths.get(PiccoloApplication.class.getResource("/sample_code.txt").toURI()));
            moduleNode = parser.parse(context, code);
            System.out.println("Parsed " + moduleNode.getChildren().size() + " nodes");
        } catch (ParsingException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
        if (context.hasErrors()) {
            System.out.println(context.toString());
        }
    }
}
