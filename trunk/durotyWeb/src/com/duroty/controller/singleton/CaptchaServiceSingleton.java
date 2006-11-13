package com.duroty.controller.singleton;

import com.octo.captcha.service.image.DefaultManageableImageCaptchaService;
import com.octo.captcha.service.image.ImageCaptchaService;


/**
 * DOCUMENT ME!
 *
 * @author $author$
 * @version $Revision$
 */
public class CaptchaServiceSingleton {
    /**
     * DOCUMENT ME!
     */
    private static ImageCaptchaService instance = new DefaultManageableImageCaptchaService();

    /**
     * DOCUMENT ME!
     *
     * @return DOCUMENT ME!
     */
    public static ImageCaptchaService getInstance() {
        return instance;
    }
}
