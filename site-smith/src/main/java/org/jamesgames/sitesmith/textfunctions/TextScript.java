package org.jamesgames.sitesmith.textfunctions;

import clojure.lang.RT;

import java.io.IOException;
import java.util.function.Function;

/**
 * @author James Murphy
 */
public class TextScript {
    static {
        try {
            RT.loadResourceScript("clojure/TextScript.clj");
        } catch (IOException e) {
            throw new IllegalStateException("Runtime loading of Clojure TextScript failed.");
        }
    }

    public static boolean isScriptInValidFormat(String scriptText) {
        return (boolean) RT.var("org.jamesgames.sitesmith.text.TextScript",
                "is-script-in-valid-format?").invoke(scriptText);
    }

    public static String executeScript(Function<String, String> resourceNameToRelativePath,
            String scriptText) {
        return RT.var("org.jamesgames.sitesmith.text.TextScript",
                "execute-script").invoke(resourceNameToRelativePath, scriptText).toString();
    }
}
