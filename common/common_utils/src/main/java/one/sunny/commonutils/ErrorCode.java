package one.sunny.commonutils;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum ErrorCode {
    TOKEN_ERROR(5001, "TOKEN ERROR: "),
    FILE_ERROR(5002, "FILE ERROR: "),
    PARAM_ERROR(5003, "PARAM ERROR: "),
    INSERT_ERROR(5004, "INSERT ERROR: "),
    UPDATE_ERROR(5005, "UPDATE ERROR: "),
    NOT_FOUND_ERROR(5005, "NOT FOUNT ERROR: "),
    UNKNOWN_ERROR(5005, "UNKNOWN_ERROR: ");

    private int code;
    private String msg;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
