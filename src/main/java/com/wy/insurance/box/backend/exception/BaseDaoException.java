package com.wy.insurance.box.backend.exception;

/**
 * @author daobin<wdb@winbaoxian.com>
 * @date 2017/11/17.
 */
public class BaseDaoException extends Exception {

    public BaseDaoException() {
    }

    public BaseDaoException(String message) {
        super(message);
    }

    public BaseDaoException(Exception e) {
        super(e);
    }
}

