package com.isoftzone.yoappstore.network;

import android.graphics.Color;


public class Enums {

    public enum UrlType {
       // API, REFRESH_TOKEN, SERVER, OTHER;
        LOGIN,SIGNUP,ME,REFRESH_TOKEN,OTHER,API,BACKTRAX,UPLOADIMG,STATGING;
    }

    public enum TokenType {
        ACCESS, REFRESH, DEVICE_TOKEN;
    }

    public enum Keys {
        PROD_GOOGLE_API_KEY("prodGoogleAPIKey"),
        TEST_GOOGLE_API_KEY("testGoogleAPIKey"),
        AUTOGRAPH_API_KEY("autographAPIKey");

        Keys (String i)
        {
            this.key = i;
        }

        private String key;

        public String getKey()
        {
            return key;
        }
    }

    public enum TabBarButtons {
        SETTINGS(0),
        CHAT(1),
        MY_STOPS(2),
        ADD_ROUTE(3),
        ADD_INVOICE(4),
        BREAK(5),
        NONE(6);

        TabBarButtons (int i)
        {
            this.type = i;
        }

        private int type;

        public int geTabIndex()
        {
            return type;
        }
    }

    public enum Views {
        MAIN_LOGIN(0),
        USER_LOGIN(1),
        QR_LOGIN(2),
        HOME_VIEW(3),
        SELECT_ROUTE_VIEW(4),
        BUILD_ROUTE_VIEW(5);

        Views (int i)
        {
            this.type = i;
        }

        private int type;

        public int geTypeValue()
        {
            return type;
        }
    }

    public enum HexCodeManager {
        DARK_BLUE("#07478c"),
        MEDIUM_BLUE("#0960bd"),
        GREEN("#00ca72"),
        GREY("#ececec"),
        LIGHT_GREY("#C6C6C6"),
        LIGHT_BLUE("#76b4f7"),
        WHITE("#FFFFFF"),
        BLACK("#000000"),
        LIGHT_SMOKE("#F8F7F9"),
        OFF_WHITE("#FFFEF7"),
        ORANGE("#f36713");

        HexCodeManager (String i)
        {
            this.value = i;
        }

        private String value;

        public String getHexCode()
        {
            return value;
        }
    }

    public enum RouteStatus {
        NEW_ROUTE(0),
        LOCKED_ROUTE(1),
        SHIPPED_ROUTE(2),
        FINISHED_ROUTE(3);

        RouteStatus (int i)
        {
            this.type = i;
        }

        private int type;

        public int getRouteStatus()
        {
            return type;
        }
    }

    public enum MarkerType {
        DEALER(0),
        TRUCK(1),
        ROUTE_STOP(2);

        MarkerType (int i)
        {
            this.type = i;
        }

        private int type;

        public int getMarkerType()
        {
            return type;
        }
    }

    public enum MissingReasonsResult {
        CLEAR(-1),
        DISMISS(-2);

        private int type;

        MissingReasonsResult (int i) { this.type = i; }

        public int getMissingReasonResult() { return type; }
    }

    public enum ToastType {
        INFO(0),
        WARNING(1),
        SUCCESS(2),
        ERROR(3);

        ToastType (int i)
        {
            this.type = i;
        }

        private int type;
        private static String[] colors = {"#1F6EC3", "#FFB82E", "#00A354", "#F75336"};

        public int getToastType()
        {
            return type;
        }

        public static int getColor(int type) {
            return Color.parseColor(colors[type]);
        }
    }

    public enum CharacterCount {
        NAME(25),
        NOTES(100),
        BREAK_REASON(50);

        private int type;

        CharacterCount (int i) { this.type = i; }

        public int getCharacterCount() { return type; }
    }
}