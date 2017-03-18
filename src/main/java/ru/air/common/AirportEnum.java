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
    DUSHANBE("DYU", "Душанбе", "http://airport.tj/index.php/ru/tablo"),

    /* ------ task 2 */

    ANAPA("AAQ", "Anapa", "http://basel.aero/anapa/passengers/online-schedule/"),
    CHELYABINKS("CEK", "Chelyabinsk Balandino", "http://cekport.ru/passengers/information/timetable/"),
    TALAGI_ARHANGELSK("ARH", "Talagi Archangelsk", "http://arhaero.ru/pax/flying/online-tablo-arrivals"),
    AMSTERDAM("AMS", "Amsterdam SchipholHaarlemmermeer, North Holland", "http://schiphol.nl"),
    JACKSON_ATLANTA("ATL", "Hartsfield Jackson Atlanta International Atlanta, Georgia", "http://www.atl.com/"),
    BARSELONA("BCN", "Barcelona International", "http://www.aena.es/en/barcelona-airport/index.html#"),
    BANGKOK("BKK", "Suvarnabhumi Bangkok", "http://www.suvarnabhumiairport.com/en/4-passenger-departures"),
    CHHATRAPATI_SHIVAJI("BOM", "Chhatrapati Shivaji International Mumbai", "http://www.csia.in/flightinformation/passenger-flight.aspx"),
    GUANGZHOU("CAN", "Guangzhou Baiyun International Guangzhou", "http://www.gbiac.net/hbxx/flightQuery?isArrival=false&isAll=true&recently_20_data=true"),
    CHARLES_DE_GAULLE("CDG", "Charles de Gaulle International Paris", "http://www.parisaeroport.fr/en/homepage");

    /* --------------------*/
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
