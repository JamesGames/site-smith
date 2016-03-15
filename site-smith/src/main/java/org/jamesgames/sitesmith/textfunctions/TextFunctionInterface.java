package org.jamesgames.sitesmith.textfunctions;

import clojure.lang.RT;

import java.io.IOException;

/**
 * @author James Murphy
 */
public class TextFunctionInterface {

    static {
        try {
            RT.loadResourceScript("clojure/TextFunction.clj");
        } catch (IOException e) {
            throw new IllegalStateException("Runtime loading of Clojure TextFunction failed.");
        }
    }

    public static boolean isFunctionTextInValidFormat(String functionText) {
        return (boolean) RT.var("org.jamesgames.sitesmith.text.TextFunction",
                "is-function-text-in-valid-format?").invoke(functionText);
    }

    public static void defineFunction(String functionName, String functionText) {
        RT.var("org.jamesgames.sitesmith.text.TextFunction",
                "define-text-function").invoke(functionName, functionText);
    }
}
