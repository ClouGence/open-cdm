package com.clougence.clouddm.comm.model;

/**
 * @author wanshao create time is 2021/1/11
 **/
public enum RSocketRespCode {

    /**
     * Success result enum.
     */
    SUCCESS("1", "Request success"),
    /**
     * Error result enum.
     */
    ERROR("0", "Request failure");

    private String code;
    private String msg;

    RSocketRespCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() { return code; }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) { this.code = code; }

    /**
     * Gets msg.
     *
     * @return the msg
     */
    public String getMsg() { return msg; }

    /**
     * Sets msg.
     *
     * @param msg the msg
     */
    public void setMsg(String msg) { this.msg = msg; }
}
