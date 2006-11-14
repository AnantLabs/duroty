package com.duroty.controller.requestprocessor;

import org.apache.struts.action.ActionMapping;


/**
 * DOCUMENT ME!
 *
 * @author DUROT
 * @version 1.0
 */
public class DActionMapping extends ActionMapping {
    /**
         *
         */
    private static final long serialVersionUID = 86412867403550123L;

    /**
    * DOCUMENT ME!
    */
    private String ajaxForward;

    /**
    * Creates a new DActionMapping object.
    */
    public DActionMapping() {
        super();
    }

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public String getAjaxForward() {
        return ajaxForward;
    }

    /**
     * DOCUMENT ME!
     *
     * @param ajaxForward DOCUMENT ME!
     */
    public void setAjaxForward(String ajaxForward) {
        this.ajaxForward = ajaxForward;
    }
}
