package ru.air.common;

/**
 * Created by Admin on 20.10.2016.
 */
public enum AirportEnum {
    EKATERINBURG("SVX", "Екатеринбург (Кольцово)", "http://www.koltsovo.ru/ru/onlayn_tablo"),
    KRASNODAR("KRR", "Краснодар (Пашковский)", "http://basel.aero/krasnodar/passengers/online-schedule/"),
    SOCHI("AER", "Сочи", "http://basel.aero/sochi/passengers/online-schedule/"),
    SAMARA("KUF", "Самара (Курумоч)", "http://airport.samara.ru/ru/onlayn_tablo"),
    HABAROVSK("KHV", "Хабаровск (Новый)", "http://airkhv.ru/index.php?option=com_tablo&lang=ru"),
    EREWAN("EVN", "Ереван (Звартноц)", "http://zvartnots.am/new/"),
    WLADIWOSTOK("VVO", "Владивосток (Кневичи)", "http://vvo.aero"),
    KAZAN("KZN", "Казань", "http://www.kazan.aero"),
    IRKUTSK("IKT", "Иркутск", "http://iktport.ru/component/option,com_tarchive/Itemid,122/task,prilet/arc,1/"),
    DUSHANBE("DYU", "Душанбе", "http://airport.tj/index.php/ru/tablo");

    private String airportId;
    private String airportName;
    private String url;

    AirportEnum(String airportId, String airportName, String url) {
        this.airportId = airportId;
        this.airportName = airportName;
        this.url = url;
    }

    public String getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getUrl() {
        return url;
    }
}
