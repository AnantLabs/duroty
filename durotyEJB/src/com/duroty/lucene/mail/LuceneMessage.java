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


/**
 *
 */
package com.duroty.lucene.mail;

import com.duroty.utils.NumberUtils;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import java.util.Date;


/**
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
 * along with GNU gv; see the file COPYING. If not, write to
 * the Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 * Author: Jordi Marquès Ferré
 * Mallorca 295 Principal B 08037 Barcelona, Spain
 * Phone: (34) 625397324
 *
 * @author Jordi Marquès
 * @version 1.0
*/
public class LuceneMessage implements LuceneMessageConstants {
    /**
     * DOCUMENT ME!
     */
    private Document docPrincipal;

    /**
    * Creates a new LuceneMessage object.
    */
    public LuceneMessage() {
        this(null, null);
    }

    /**
     * Creates a new LuceneMessage object.
     */
    public LuceneMessage(String idint) {
        this(idint, null);
    }

    /**
     * Creates a new LuceneMessage object.
     *
     * @param document DOCUMENT ME!
     */
    public LuceneMessage(Document docPrincipal) {
        this(null, docPrincipal);
    }

    /**
     * Creates a new LuceneMessage object.
     *
     * @param document DOCUMENT ME!
     */
    public LuceneMessage(String idint, Document docPrincipal) {
        super();

        if (docPrincipal == null) {
            this.docPrincipal = new Document();
        } else {
            this.docPrincipal = docPrincipal;
        }

        setIdint(idint);
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getIdint() {
        return docPrincipal.get(Field_idint);
    }

    /**
     * DOCUMENT ME!
     *
     * @param idint DOCUMENT ME!
     */
    public void setIdint(String idint) {
        if (idint != null) {
            docPrincipal.removeField(Field_idint);
            docPrincipal.add(new Field(Field_idint, idint, Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * @return Returns the attachments.
     */
    public String getAttachments() {
        return docPrincipal.get(Field_attachments);
    }

    /**
     * @param attachments The attachments to set.
     */
    public void setAttachments(String attachments) {
        docPrincipal.removeField(Field_attachments);

        if (attachments != null) {
            docPrincipal.add(new Field(Field_attachments, attachments,
                    Field.Store.NO, Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_attachments, "", Field.Store.NO,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the body.
     */
    public String getBody() {
        return docPrincipal.get(Field_body);
    }

    /**
     * @param body The body to set.
     */
    public void setBody(String body) {
        docPrincipal.removeField(Field_body);

        if (body != null) {
            docPrincipal.add(new Field(Field_body, body, Field.Store.NO,
                    Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_body, "", Field.Store.NO,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the cc.
     */
    public String getCc() {
        return docPrincipal.get(Field_cc);
    }

    /**
     * @param cc The cc to set.
     */
    public void setCc(String cc) {
        docPrincipal.removeField(Field_cc);

        if (cc != null) {
            docPrincipal.add(new Field(Field_cc, cc, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_cc, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the from.
     */
    public String getFrom() {
        return docPrincipal.get(Field_from);
    }

    /**
     * @param from The from to set.
     */
    public void setFrom(String from) {
        docPrincipal.removeField(Field_from);

        if (from != null) {
            docPrincipal.add(new Field(Field_from, from, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_from, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the sentDate.
     */
    public Date getLastDate() {
        if ((docPrincipal.get(Field_lastDate) != null) &&
                !docPrincipal.get(Field_lastDate).trim().equals("")) {
            return new Date(Long.parseLong(docPrincipal.get(Field_lastDate)));
        } else {
            return null;
        }
    }

    /**
     * @param sentDate The lastDate to set.
     */
    public void setLastDate(Date lastDate) {
        docPrincipal.removeField(Field_lastDate);

        if (lastDate != null) {
            docPrincipal.add(new Field(Field_lastDate,
                    NumberUtils.pad(lastDate.getTime()), Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_lastDate, "", Field.Store.YES,
                    Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * @return Returns the size.
     */
    public long getSize() {
        return Long.parseLong(docPrincipal.get(Field_size));
    }

    /**
     * @param size The size to set.
     */
    public void setSize(long size) {
        docPrincipal.removeField(Field_size);
        docPrincipal.add(new Field(Field_size, NumberUtils.pad(size),
                Field.Store.YES, Field.Index.UN_TOKENIZED));
    }

    /**
     * @return Returns the subject.
     */
    public String getSubject() {
        return docPrincipal.get(Field_subject);
    }

    /**
     * @param subject The subject to set.
     */
    public void setSubject(String subject) {
        docPrincipal.removeField(Field_subject);

        if (subject != null) {
            docPrincipal.add(new Field(Field_subject, subject, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_subject, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the to.
     */
    public String getTo() {
        return docPrincipal.get(Field_to);
    }

    /**
     * @param to The to to set.
     */
    public void setTo(String to) {
        docPrincipal.removeField(Field_to);

        if (to != null) {
            docPrincipal.add(new Field(Field_to, to, Field.Store.YES,
                    Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_to, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
     * @return Returns the to.
     */
    public String getFiletype() {
        return docPrincipal.get(Field_filetype);
    }

    /**
     * @param to The to to set.
     */
    public void setFiletype(String filetype) {
        if (filetype != null) {
            docPrincipal.add(new Field(Field_filetype, filetype,
                    Field.Store.YES, Field.Index.TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_filetype, "", Field.Store.YES,
                    Field.Index.TOKENIZED));
        }
    }

    /**
         * @return Returns the hasAttachment.
         */
    public boolean isHasAttachment() {
        return new Boolean(docPrincipal.get(Field_has_attachments)).booleanValue();
    }

    /**
     * @param hasAttachment The hasAttachment to set.
     */
    public void setHasAttachment(boolean hasAttachment) {
        docPrincipal.removeField(Field_has_attachments);

        if (hasAttachment) {
            docPrincipal.add(new Field(Field_has_attachments, "true",
                    Field.Store.YES, Field.Index.UN_TOKENIZED));
        } else {
            docPrincipal.add(new Field(Field_has_attachments, "false",
                    Field.Store.YES, Field.Index.UN_TOKENIZED));
        }
    }

    /**
     * @return Returns the docPrincipal.
     */
    public Document getDocPrincipal() {
        return docPrincipal;
    }

    /**
     * @param document The document to set.
     */
    public void setDocPrincipal(Document docPrincipal) {
        this.docPrincipal = docPrincipal;
    }
}
