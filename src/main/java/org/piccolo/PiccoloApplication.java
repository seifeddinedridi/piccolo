package org.piccolo;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.piccolo.parsing.impl.LanguageParser;
import org.piccolo.node.TokenNode;
import org.piccolo.parsing.context.CompilationErrorListener;
import org.piccolo.parsing.context.ParsingContext;
import org.piccolo.parsing.exception.ParsingException;

public class PiccoloApplication {

    public static void main(String[] args) {
        LanguageParser parser = new LanguageParser();
        ParsingContext context = new ParsingContext(new CompilationErrorListener());
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
