/*
* Copyright (C) 2006 Jordi Marquès Ferré
*
* This program is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 2 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this software; see the file DUROTY.txt.
*
* Author: Jordi Marquès Ferré
* c/Mallorca 295 principal B 08037 Barcelona Spain
* Phone: +34 625397324
*/


package com.duroty.lucene.bookmark.indexer;

import com.duroty.lucene.analysis.DictionaryAnalyzer;
import com.duroty.lucene.didyoumean.DidYouMeanIndexer;
import com.duroty.lucene.utils.FileUtilities;

import org.apache.commons.io.FileUtils;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class BookmarkIndexer implements BookmarkIndexerConstants {
    /**
        * DOCUMENT ME!
        */
    private static long sleepTime = 1000;

    /**
     * DOCUMENT ME!
     */
    private int countInsert = 0;

    /**
     * DOCUMENT ME!
     */
    private int countDelete = 0;

    /**
     * Creates a new MailIndexer object.
     */
    public BookmarkIndexer() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static Searcher getSearcher(String path) throws Exception {
        String optimizedPath = null;

        if (!path.endsWith(File.separator)) {
            optimizedPath = path + File.separator + OPTIMIZED_PATH_NAME +
                File.separator;
        } else {
            optimizedPath = path + OPTIMIZED_PATH_NAME + File.separator;
        }

        File file = new File(optimizedPath);

        if (!IndexReader.indexExists(file)) {
            file.mkdirs();

            IndexWriter writer = new IndexWriter(file, null, true);
            writer.close();
        }

        return new IndexSearcher(optimizedPath);
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param idint DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public Document selectMessage(String path, String field, String idint)
        throws Exception {
        Searcher searcher = null;
        Document doc = null;

        try {
            searcher = getSearcher(path);

            TermQuery query = new TermQuery(new Term(field, idint));
            Hits hits = searcher.search(query);

            if (hits.length() == 1) {
                doc = hits.doc(0);
            } else {
                doc = null;
            }

            return doc;
        } finally {
            if (searcher != null) {
                try {
                    searcher.close();
                } catch (Exception ex) {
                }
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param id DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void insertDocument(String path, String id, Document doc,
        Analyzer analyzer) throws Exception {
        if (!path.endsWith(File.separator)) {
            path = path + File.separator + SIMPLE_PATH_NAME + File.separator;
        } else {
            path = path + SIMPLE_PATH_NAME + File.separator;
        }

        IndexWriter writer = null;
        File file = null;
        boolean create = false;

        try {
            file = new File(path + id);

            if (!IndexReader.indexExists(file)) {
                file.mkdirs();

                create = true;
            }

            if (IndexReader.isLocked(path)) {
                Thread.sleep(sleepTime);

                if (countInsert > 5) {
                    throw new Exception(
                        "The index lucene MainIndexer is locked insert document");
                }

                countInsert++;

                insertDocument(path, id, doc, analyzer);

                return;
            }

            Directory dir = FSDirectory.getDirectory(file, create);

            writer = new IndexWriter(dir, analyzer, create);
            writer.setMaxFieldLength(Integer.MAX_VALUE);
            writer.addDocument(doc);

            writer.optimize();

            writer.close();
            writer = null;
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }

            //Aquest fitxer dins el directori de l'index individual ens permet saber si està indexant
            File unlock = new File(file, FileUtilities.FILE_IS_UNLOCK);

            try {
                unlock.createNewFile();
            } catch (Exception e) {
            }
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param id DOCUMENT ME!
     * @param doc DOCUMENT ME!
     * @param analyzer DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void updateDocument(String path, String field, String id,
        Document doc, Analyzer analyzer) throws Exception {
        try {
            //1-Borro document d'algun lloc
            deleteDocument(path, field, id);

            //2-Inserto document
            insertDocument(path, id, doc, analyzer);
        } finally {
        }
    }

    /**
     * DOCUMENT ME!
     *
     * @param path DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param id DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public void deleteDocument(String path, String field, String id)
        throws Exception {
        String simplePath = null;
        String optimizedPath = null;

        if (!path.endsWith(File.separator)) {
            simplePath = path + File.separator + SIMPLE_PATH_NAME +
                File.separator;
            optimizedPath = path + File.separator + OPTIMIZED_PATH_NAME +
                File.separator;
        } else {
            simplePath = path + SIMPLE_PATH_NAME + File.separator;
            optimizedPath = path + OPTIMIZED_PATH_NAME + File.separator;
        }

        IndexReader reader = null;

        try {
            //La primera cosa que faig al borrar es controlar els simple paths
            File simple = new File(simplePath + id);

            //Miro si està lock
            boolean lock;

            try {
                lock = FileUtilities.isLockDir(simple);

                if (!lock) {
                    FileUtils.deleteDirectory(simple);

                    return;
                } else {
                    Thread.sleep(sleepTime);

                    if (countDelete > 5) {
                        throw new Exception(
                            "The index lucene MainIndexer is locked in delete document");
                    }

                    countDelete++;

                    deleteDocument(path, field, id);
                }
            } catch (IOException ioe) {
                //vol dir que no existeix i per tant he de mirar al optimitzat
            }

            if (IndexReader.isLocked(optimizedPath)) {
                Thread.sleep(sleepTime);

                if (countDelete > 5) {
                    throw new Exception(
                        "The index lucene MainIndexer is locked in delete document");
                }

                countDelete++;

                deleteDocument(path, field, id);

                return;
            }

            //En primer lloc borro del index optimitzat
            reader = IndexReader.open(optimizedPath);
            reader.deleteDocuments(new Term(field, id));
            reader.close();
            reader = null;
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
     * @param path DOCUMENT ME!
     * @param field DOCUMENT ME!
     * @param doc DOCUMENT ME!
     */
    public static void createSpell(String path, String field, Document doc)
        throws Exception {
        RAMDirectory ramDir = null;
        IndexWriter writer = null;

        try {
            ramDir = new RAMDirectory();
            writer = new IndexWriter(ramDir, new DictionaryAnalyzer(), true);
            writer.addDocument(doc);
            writer.optimize();
            writer.close();

            DidYouMeanIndexer.createSpell(field, ramDir, path);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        }
    }
}
