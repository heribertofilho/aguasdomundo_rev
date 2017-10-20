package br.heriberto.aguasdomundo.Utils

/**
 * Created by herib on 20/10/2017.
 */
class CountryCodeParser {
    private val countrys: Map<String, String> = mapOf(
            Pair("AH", "AF"), Pair("AB", "AL"), Pair("AL", "DZ"), Pair("AS", "AS"), Pair("AD", "AD"),
            Pair("AN", "AO"), Pair("A1", "AI"), Pair("AA", "AQ"), Pair("AT", "AG"), Pair("AG", "AR"),
            Pair("AM", "AM"), Pair("AW", "AW"), Pair("AU", "AU"), Pair("AU", "AU"), Pair("OS", "AT"),
            Pair("A2", "AZ"), Pair("BS", "BS"), Pair("BN", "BH"), Pair("BW", "BD"), Pair("BR", "BB"),
            Pair("BY", "BY"), Pair("BX", "BE"), Pair("BH", "BZ"), Pair("BJ", "BJ"), Pair("BE", "BM"),
            Pair("B2", "BT"), Pair("BO", "BO"), Pair("BA", "BA"), Pair("BC", "BW"), Pair("BV", "BV"),
            Pair("BZ", "BR"), Pair("BT", "IO"), Pair("BF", "BN"), Pair("BU", "BG"), Pair("HV", "BF"),
            Pair("BI", "BI"), Pair("KH", "KH"), Pair("CM", "CM"), Pair("CA", "CA"), Pair("CV", "CV"),
            Pair("GC", "KY"), Pair("CE", "CF"), Pair("CD", "TD"), Pair("CH", "CL"), Pair("CI", "CN"),
            Pair("CX", "CX"), Pair("CC", "CC"), Pair("CO", "CO"), Pair("IC", "KM"), Pair("CG", "CG"),
            Pair("CD", "CD"), Pair("KU", "CK"), Pair("CS", "CR"), Pair("IV", "CI"), Pair("RH", "HR"),
            Pair("CU", "CU"), Pair("CY", "CY"), Pair("CZ", "CZ"), Pair("DN", "DK"), Pair("DJ", "DJ"),
            Pair("DO", "DM"), Pair("DR", "DO"), Pair("EQ", "EC"), Pair("EG", "EG"), Pair("ES", "SV"),
            Pair("GQ", "GQ"), Pair("E1", "ER"), Pair("EE", "EE"), Pair("ET", "ET"), Pair("FK", "FK"),
            Pair("FA", "FO"), Pair("FJ", "FJ"), Pair("FI", "FI"), Pair("FR", "FR"), Pair("FG", "GF"),
            Pair("PF", "PF"), Pair("TF", "TF"), Pair("GO", "GA"), Pair("GB", "GM"), Pair("GE", "GE"),
            Pair("DL", "DE"), Pair("GH", "GH"), Pair("GI", "GI"), Pair("GR", "GR"), Pair("GL", "GL"),
            Pair("GD", "GD"), Pair("GP", "GP"), Pair("GU", "GU"), Pair("GU", "GT"),
            Pair("GN", "GN"), Pair("GW", "GW"), Pair("GY", "GY"), Pair("HA", "HT"), Pair("HM", "HM"),
            Pair("VA", "VA"), Pair("HO", "HN"), Pair("HK", "HK"), Pair("HU", "HU"), Pair("IL", "IS"),
            Pair("IN", "IN"), Pair("ID", "ID"), Pair("IR", "IR"), Pair("IQ", "IQ"), Pair("IE", "IE"),
            Pair("IS", "IL"), Pair("IY", "IT"), Pair("JM", "JM"), Pair("JP", "JP"), Pair("JD", "JO"),
            Pair("KZ", "KZ"), Pair("KN", "KE"), Pair("KB", "KI"), Pair("KR", "KP"), Pair("KO", "KR"),
            Pair("KW", "KW"), Pair("KG", "KG"), Pair("LA", "LA"), Pair("LV", "LV"), Pair("LB", "LB"),
            Pair("LS", "LS"), Pair("LI", "LR"), Pair("LY", "LY"), Pair("LT", "LI"), Pair("L1", "LT"),
            Pair("LU", "LU"), Pair("MU", "MO"), Pair("MK", "MK"), Pair("MG", "MG"), Pair("MW", "MW"),
            Pair("MS", "MY"), Pair("MV", "MV"), Pair("MI", "ML"), Pair("ML", "MT"), Pair("MH", "MH"),
            Pair("MP", "MP"), Pair("MR", "MQ"), Pair("MT", "MR"), Pair("MA", "MU"), Pair("YT", "YT"),
            Pair("MX", "MX"), Pair("US_FM", "FM"), Pair("M1", "MD"), Pair("M3", "MC"), Pair("MO", "MN"),
            Pair("M2", "MS"), Pair("MC", "MA"), Pair("MZ", "MZ"), Pair("BM", "MM"), Pair("NM", "NA"),
            Pair("NW", "NR"), Pair("NP", "NP"), Pair("NL", "NL"), Pair("AN", "AN"), Pair("NZ", "NZ"),
            Pair("NC", "NC"), Pair("NK", "NI"), Pair("NR", "NE"), Pair("NI", "NG"), Pair("N1", "NU"),
            Pair("XX_NF", "NF"), Pair("US_MP", "MP"), Pair("NO", "NO"), Pair("OM", "OM"), Pair("PK", "PK"),
            Pair("PW", "PW"), Pair("PS", "PS"), Pair("PM", "PA"), Pair("NG", "PG"), Pair("PY", "PY"),
            Pair("PR", "PE"), Pair("PH", "PH"), Pair("P2", "PN"), Pair("PL", "PL"), Pair("PO", "PT"),
            Pair("PR", "PR"), Pair("QT", "QA"), Pair("RE", "RE"), Pair("RO", "RO"), Pair("RS", "RU"),
            Pair("RW", "RW"), Pair("HE", "SH"), Pair("K1", "KN"), Pair("LC", "LC"), Pair("P1", "PM"),
            Pair("VC", "VC"), Pair("ZM", "WS"), Pair("SM", "SM"), Pair("TP", "ST"), Pair("SD", "SA"),
            Pair("SG", "SN"), Pair("SC", "SC"), Pair("SL", "SL"), Pair("SR", "SG"), Pair("S1", "SK"),
            Pair("LJ", "SI"), Pair("SO", "SB"), Pair("SI", "SO"), Pair("ZA", "ZA"), Pair("GS", "GS"),
            Pair("SP", "ES"), Pair("SB", "LK"), Pair("SU", "SD"), Pair("SM", "SR"), Pair("SJ", "SJ"),
            Pair("SV", "SZ"), Pair("SN", "SE"), Pair("SW", "CH"), Pair("SY", "SY"), Pair("TW", "TW"),
            Pair("TJ", "TJ"), Pair("TN", "TZ"), Pair("TH", "TH"), Pair("EA", "TL"), Pair("TG", "TG"),
            Pair("TK", "TK"), Pair("TO", "TO"), Pair("TD", "TT"), Pair("TS", "TN"), Pair("TU", "TR"),
            Pair("TM", "TM"), Pair("TI", "TC"), Pair("TV", "TV"), Pair("TB", "TB"), Pair("UG", "UG"),
            Pair("UR", "UA"), Pair("ER", "AE"), Pair("UK", "GB"), Pair("US", "US"), Pair("US_UM", "UM"),
            Pair("UY", "UY"), Pair("UZ", "UZ"), Pair("NH", "VU"), Pair("VN", "VE"), Pair("VS", "VN"),
            Pair("VG", "VG"), Pair("VI", "VI"), Pair("FW", "WF"), Pair("EH", "EH"), Pair("YE", "YE"),
            Pair("RB", "RS"), Pair("KV", "KV"), Pair("M4", "ME"), Pair("ZB", "ZM"), Pair("ZW", "ZW")
    )

    fun parseCountryCode(country: String): String {
        var parsedCountry = ""
        for (pair in countrys) {
            if (country == pair.key) {
                parsedCountry = pair.value
                break
            }
        }
        return parsedCountry
    }
}