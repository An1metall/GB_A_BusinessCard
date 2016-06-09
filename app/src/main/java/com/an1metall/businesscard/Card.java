package com.an1metall.businesscard;

    public class Card {
        private String name;
        private String data;
        private int picID;
        private int btnPicID;

        public Card(String name, String data, int picID, int btnPicID) {
            this.name = name;
            this.data = data;
            this.picID = picID;
            this.btnPicID = btnPicID;
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

        public int getBtnPicID() {
            return btnPicID;
        }
    }