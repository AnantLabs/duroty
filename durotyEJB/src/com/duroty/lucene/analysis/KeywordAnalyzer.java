/*
 * Copyright 2004 The Apache Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.duroty.lucene.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.TokenStream;

import java.io.IOException;
import java.io.Reader;


/**
 * "Tokenizes" the entire stream as a single token.
 */
public class KeywordAnalyzer extends Analyzer {
    /**
     * DOCUMENT ME!
     *
     * @param fieldName DOCUMENT ME!
     * @param reader DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public TokenStream tokenStream(String fieldName, final Reader reader) {
        return new TokenStream() {
                private boolean done;
                private final char[] buffer = new char[1024];

                public Token next() throws IOException {
                    if (!done) {
                        done = true;

                        StringBuffer buffer = new StringBuffer();
                        int length = 0;

                        while (true) {
                            length = reader.read(this.buffer);

                            if (length == -1) {
                                break;
                            }

                            buffer.append(this.buffer, 0, length);
                        }

                        String text = buffer.toString();

                        return new Token(text, 0, text.length());
                    }

                    return null;
                }
            };
    }
}
