package com.fine.friendlycc.bean;

import java.util.List;

public class GooglePoiBean {
    /**
     * next_page_token : CrQCIgEAAKEonsp0BEpasYErZC_pheSiBZ2c49OhaRSQJXEGlT_GPCb4J_NPIPl9H_M_T-r0aVswBmhlhud6n92CURefwSRd4f6Bv7_pTA2wBN3Pfx1EAcZtPdtUZcmhVUjz72y3ObFDYxfvPcsO-y2A0MIsbhq1xi8BD1YrFfaXliVhMsvUDUf5wOtH9gHgZPA4rJSBzYZ-jbjsIvKE-FBPX02Hock_kRCJ1evhJDVAkpYa7UcIWsSVcIPH-YwIbeEKUr3e6h7TBBzEZmD75odHAr_rrsC5OxSOARr3Mj02fcroY_UW5KWRK7-gEpq3MeRUpPygECYJrZXqhUmTDmZhaTCfDzNuVcUW9YF_iqsOEmhX_BJ-Lt0y4NXjv8TD3YT8yrJtTpY6W2pmP03cf3cZiDL6CCoSEIMNq4lLF1h7izq9_4SnQqUaFO7ItVHf24MbCVwqws5kb5sFy7xy
     * results : [{"formatted_address":"广东省广州市番禺区市桥繁华路38号","geometry":{"location":{"lat":22.942026,"lng":113.363488}},"icon":"https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png","id":"554e337a5064aca2af47e37efdb189cb7c8abd98","name":"力美健健身俱乐部","place_id":"ChIJtc2TbtxSAjQRaHLHD-O9i2c","plus_code":{"compound_code":"W9R7+R9 番禺区 广东省广州市","global_code":"7PJMW9R7+R9"},"reference":"ChIJtc2TbtxSAjQRaHLHD-O9i2c","types":["gym","health","point_of_interest","establishment"]}]
     * status : OK
     */

    private String next_page_token;
    private String status;
    private List<ResultsBean> results;

    public String getNext_page_token() {
        return next_page_token;
    }

    public void setNext_page_token(String next_page_token) {
        this.next_page_token = next_page_token;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }

    public static class ResultsBean {
        /**
         * formatted_address : 广东省广州市番禺区市桥繁华路38号
         * geometry : {"location":{"lat":22.942026,"lng":113.363488}}
         * icon : https://maps.gstatic.com/mapfiles/place_api/icons/generic_business-71.png
         * id : 554e337a5064aca2af47e37efdb189cb7c8abd98
         * name : 力美健健身俱乐部
         * place_id : ChIJtc2TbtxSAjQRaHLHD-O9i2c
         * plus_code : {"compound_code":"W9R7+R9 番禺区 广东省广州市","global_code":"7PJMW9R7+R9"}
         * reference : ChIJtc2TbtxSAjQRaHLHD-O9i2c
         * types : ["gym","health","point_of_interest","establishment"]
         */

        private String formatted_address;
        private GeometryBean geometry;
        private String icon;
        private String id;
        private String name;
        private String place_id;
        private PlusCodeBean plus_code;
        private String reference;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryBean getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryBean geometry) {
            this.geometry = geometry;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public PlusCodeBean getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(PlusCodeBean plus_code) {
            this.plus_code = plus_code;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryBean {
            /**
             * location : {"lat":22.942026,"lng":113.363488}
             */

            private LocationBean location;

            public LocationBean getLocation() {
                return location;
            }

            public void setLocation(LocationBean location) {
                this.location = location;
            }

            public static class LocationBean {
                /**
                 * lat : 22.942026
                 * lng : 113.363488
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }

        public static class PlusCodeBean {
            /**
             * compound_code : W9R7+R9 番禺区 广东省广州市
             * global_code : 7PJMW9R7+R9
             */

            private String compound_code;
            private String global_code;

            public String getCompound_code() {
                return compound_code;
            }

            public void setCompound_code(String compound_code) {
                this.compound_code = compound_code;
            }

            public String getGlobal_code() {
                return global_code;
            }

            public void setGlobal_code(String global_code) {
                this.global_code = global_code;
            }
        }
    }
}
