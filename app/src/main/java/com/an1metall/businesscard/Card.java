package com.an1metall.businesscard;

    public class Card {
        private String name;
        private String data;
        private int picID;

        public Card(String name, String data, int picID) {
            this.name = name;
            this.data = data;
            this.picID = picID;
        }

        public String getName() {
            return name;
        }

        public String getData() {
            return data;
        }

        public int getPicID() {
            return picID;
        }
    }