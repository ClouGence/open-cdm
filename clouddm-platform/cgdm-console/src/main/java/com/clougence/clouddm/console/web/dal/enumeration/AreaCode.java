/*
 * Copyright 2026 杭州开云集致科技有限公司
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.clougence.clouddm.console.web.dal.enumeration;

import com.clougence.clouddm.console.web.util.DmI18nUtils;
import com.fasterxml.jackson.annotation.JsonFormat;

@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum AreaCode {

    CHINA(86),
    CHINA_HONG_KONG(852),
    CHINA_MACAO(853),
    CHINA_TAIWAN(886),
    JAPAN(81),
    MALAYSIA(60),
    AUSTRALIA(61),
    CANADA(1),
    BRITAIN(44),
    SINGAPORE(65),
    GERMANY(49),
    RUSSIA(7),
    USA(1),
    MEXICO(52),
    BRAZIL(55),
    ARGENTINA(54),
    CHILE(56),
    PERU(51),
    COLOMBIA(57),
    VENEZUELA(58),
    CUBA(53),
    AFGHANISTAN(93),
    PAKISTAN(92),
    BANGLADESH(880),
    INDIA(91),
    SRI_LANKA(94),
    MYANMAR(95),
    SOUTH_KOREA(82),
    PHILIPPINES(63),
    INDONESIA(62),
    THAILAND(66),
    NEW_ZEALAND(64),
    GREECE(30),
    ITALY(39),
    SPAIN(34),
    PORTUGAL(351),
    FRANCE(33),
    NETHERLANDS(31),
    BELGIUM(32),
    LUXEMBOURG(352),
    SWITZERLAND(41),
    AUSTRIA(43),
    CZECH_REPUBLIC(420),
    HUNGARY(36),
    POLAND(48),
    DENMARK(45),
    SWEDEN(46),
    NORWAY(47),
    FINLAND(358),
    ICELAND(354),
    IRELAND(353),
    UNITED_KINGDOM(44),
    GABON(241),
    CAMEROON(237),
    SENEGAL(221),
    MALI(223),
    MOZAMBIQUE(258),
    SOUTH_AFRICA(27),
    KENYA(254),
    NIGERIA(234),
    EGYPT(20),
    MOROCCO(212),
    SAUDI_ARABIA(966),
    UNITED_ARAB_EMIRATES(971),
    ISRAEL(972),
    TURKEY(90),
    IRAN(98),
    KAZAKHSTAN(7),
    UKRAINE(380),
    BELARUS(375),
    MOLDOVA(373),
    ROMANIA(40),
    BULGARIA(359),
    SERBIA(381),
    CROATIA(385),
    SLOVENIA(386),
    NORTH_MACEDONIA(389),
    ALBANIA(355),
    BOSNIA_AND_HERZEGOVINA(387),
    MONTENEGRO(382),
    CYPRUS(357),
    SYRIA(963),
    LEBANON(961),
    JORDAN(962),
    IRAQ(964),
    KUWAIT(965),
    BAHRAIN(973),
    QATAR(974),
    OMAN(968),
    YEMEN(967),
    NAURU(674),
    TUVALU(688);

    private final Integer code;

    private final String  name;

    private String        title;

    AreaCode(Integer code){
        this.code = code;
        this.name = this.name();
    }

    public void fillI18nTitle() {
        this.title = DmI18nUtils.getMessage(this.name());
    }

    public Integer getCode() { return code; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public String getName() { return name; }

    public static AreaCode valueOfCode(Integer code) {
        if (code == null) {
            return null;
        }

        for (AreaCode areaCode : AreaCode.values()) {
            if (areaCode.code.equals(code)) {
                return areaCode;
            }
        }

        return null;
    }
}
