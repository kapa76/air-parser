package ru.air.common;

public enum ArrivalStatus {
    UNKNOWN(1, "статус не известен"),
    SCHEDULED(2, "рейс идет по расписанию"),
    EXPECTED(3, "ожидается скорая посадка"),
    DELAYED(4, "рейс задерживается"),
    LANDED(5, "приземлился"),
    CANCELLED(6, "рейс отменен");

    private int code;
    private String msg;

    ArrivalStatus(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return String.valueOf(code);
    }

    public String getMsg() {
        return msg;
    }
}
