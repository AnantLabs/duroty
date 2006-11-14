/*
 * StripAccent.java
 *
 * Created on 28 de abril de 2004, 11:40
 */
package com.duroty.utils.io;

/**
 * DOCUMENT ME!
 *
 * @author jordi marques
 */
public class ParseAccent {
    /**
     * DOCUMENT ME!
     *
     * @param text DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static String start(String text) {
        text = text.replaceAll("à", "a");
        text = text.replaceAll("á", "a");
        text = text.replaceAll("â", "a");
        text = text.replaceAll("ä", "a");

        text = text.replaceAll("À", "A");
        text = text.replaceAll("Á", "A");
        text = text.replaceAll("Â", "A");
        text = text.replaceAll("Ä", "A");

        text = text.replaceAll("è", "e");
        text = text.replaceAll("é", "e");
        text = text.replaceAll("ê", "e");
        text = text.replaceAll("ë", "e");

        text = text.replaceAll("È", "E");
        text = text.replaceAll("É", "E");
        text = text.replaceAll("Ê", "E");
        text = text.replaceAll("Ë", "E");

        text = text.replaceAll("ì", "i");
        text = text.replaceAll("í", "i");
        text = text.replaceAll("î", "i");
        text = text.replaceAll("ï", "i");

        text = text.replaceAll("Ì", "I");
        text = text.replaceAll("Í", "I");
        text = text.replaceAll("Î", "I");
        text = text.replaceAll("Ï", "I");

        text = text.replaceAll("ò", "o");
        text = text.replaceAll("ó", "o");
        text = text.replaceAll("ô", "o");
        text = text.replaceAll("ö", "o");

        text = text.replaceAll("Ò", "O");
        text = text.replaceAll("Ó", "O");
        text = text.replaceAll("Ô", "O");
        text = text.replaceAll("Ö", "O");

        text = text.replaceAll("ù", "u");
        text = text.replaceAll("ú", "u");
        text = text.replaceAll("û", "u");
        text = text.replaceAll("ü", "u");

        text = text.replaceAll("Ù", "U");
        text = text.replaceAll("Ú", "U");
        text = text.replaceAll("Û", "U");
        text = text.replaceAll("Ü", "U");

        return text;
    }
}
