package org.jamesgames.sitesmith.textfunctions;

import clojure.lang.RT;

import java.io.IOException;
import java.util.HashSet;
import java.util.function.Function;

/**
 * @author James Murphy
 */
public class TextScriptInterface {
    static {
        try {
            RT.loadResourceScript("clojure/Util.clj");
            RT.loadResourceScript("clojure/TextScript.clj");
        } catch (IOException e) {
            throw new IllegalStateException("Runtime loading of Clojure TextScript failed.");
        }
    }

    public static boolean isScriptInValidFormat(String scriptText) {
        return (boolean) RT.var("org.jamesgames.sitesmith.text.TextScript",
                "is-script-in-valid-format?").invoke(scriptText);
    }

    public static String executeScript(
            String uniqueNameOfPageExecutingScript,
            Function<String, String> resourceNameToRelativePath,
            String scriptText,
            HashSet<String> allResourceNames) {
        return RT.var("org.jamesgames.sitesmith.text.TextScript",
                "execute-script").invoke(uniqueNameOfPageExecutingScript,
                resourceNameToRelativePath, scriptText, allResourceNames).toString();
    }
}
