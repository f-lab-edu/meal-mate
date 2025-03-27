package com.flab.mealmate.global.error.exception;
public class DataBaseException extends RuntimeException {
    private static final long serialVersionUID = 3L;
    private String[] stringArgList;

    public DataBaseException(Exception e){
        super(e);
        this.stringArgList = new String[]{};
    }

}
