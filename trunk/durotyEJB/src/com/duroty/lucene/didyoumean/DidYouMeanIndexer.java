package com.duroty.lucene.didyoumean;

import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.Dictionary;
import org.apache.lucene.search.spell.LuceneDictionary;
import org.apache.lucene.search.spell.SpellChecker;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;


public class DidYouMeanIndexer {
    /**
     * Creates a new DidYouMeanIndexer object.
     */
    public DidYouMeanIndexer() {
        super();

        // TODO Auto-generated constructor stub
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param originalPath DOCUMENT ME!
     * @param spellPath DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void createSpell(String field, String originalPath,
        String spellPath) throws IOException {
        if (IndexReader.isLocked(originalPath)) {
            return;
        }

        boolean create = false;

        if (!IndexReader.indexExists(spellPath)) {
            File file = new File(spellPath);
            file.mkdirs();

            create = true;
        }

        IndexReader reader = null;

        try {
            reader = IndexReader.open(originalPath);

            Dictionary dictionary = new LuceneDictionary(reader, field);

            Directory dir = FSDirectory.getDirectory(spellPath, create);
            SpellChecker spellChecker = new SpellChecker(dir);
            spellChecker.indexDictionnary(dictionary);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param field DOCUMENT ME!
     * @param originalDir DOCUMENT ME!
     * @param spellPath DOCUMENT ME!
     *
     * @throws IOException DOCUMENT ME!
     */
    public static void createSpell(String field, Directory originalDir,
        String spellPath) throws IOException {
        if (IndexReader.isLocked(originalDir)) {
            return;
        }

        boolean create = false;

        if (!IndexReader.indexExists(spellPath)) {
            File file = new File(spellPath);
            file.mkdirs();

            create = true;
        }

        IndexReader reader = null;
        Directory dir = null;

        try {
            reader = IndexReader.open(originalDir);

            Dictionary dictionary = new LuceneDictionary(reader, field);

            dir = FSDirectory.getDirectory(spellPath, create);

            if (IndexReader.isLocked(dir)) {
                return;
            }

            SpellChecker spellChecker = new SpellChecker(dir);
            spellChecker.indexDictionnary(dictionary);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                }
            }

            if (dir != null) {
                try {
                    dir.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
