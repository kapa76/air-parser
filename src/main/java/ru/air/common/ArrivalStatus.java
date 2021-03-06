package ru.air.common;

public enum ArrivalStatus {
    UNKNOWN(1, "статус не известен"),
    SCHEDULED(2, "рейс идет по расписанию"),
    EXPECTED(3, "ожидается скорая посадка"),
    DELAYED(4, "рейс задерживается"),
    LANDED(5, "приземлился"),
    TRANSFERED(6, "рейс перенесен"),
    CANCELLED(7, "рейс отменен"),
    FLIGHTSENT(8, "рейс отправлен"),
    DEPARTED(9, "рейс вылетел");

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
