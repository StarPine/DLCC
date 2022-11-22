package com.fine.friendlycc.bean;

import java.util.List;

public class GoogleNearPoiBean {

    /**
     * next_page_token : CqQCEwEAAE-1eg98-K0hlJyRTAJdzEGbUzbIOJwLao7ERfB4LgIw87nd7Xa_cEy9fa8Gk_2B1OAtMIgytEp_td2Kue6hhXABVEWnl2P79CAvbFucSxDGz01D4Ch5ypYQRSpSibpG9UJjFGjwjwYPP1j15wUmYK5A-9mv-4xmmulTjg9ev4BErjlRaakjHvxAkdgTMwqiMlTkF4rNyZvFHL5VJ9sYoTr6TLngdJ9LH7n44j7APnRE7XHn8K0RsEhBc_mEYKXRE3K2BiKz7sUaI-SkHFRidrGAZjvZkghM1NJuawoau97LXISgkvd0WSagQPhgudsW2UeONLabhmfn8N1hMTclVFeoyh8Kd8pDGqRQ9JBXz6_CbtrvRU8X2-LjVJaZ5Wn7zhIQvMEQSdvSB66b0cxE7FWsSxoU32nIvKYeANalMorD_2Za6257_ts
     * results : [{"geometry":{"location":{"lat":23.12911,"lng":113.264385}},"icon":"https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png","id":"1eab06e98e9d9a81941d427c1ead4ece2f75ed67","name":"广州市","place_id":"ChIJxytco5X4AjQRFeTqrXXgWQ4","reference":"ChIJxytco5X4AjQRFeTqrXXgWQ4","scope":"GOOGLE","vicinity":"广州市"}]
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
         * geometry : {"location":{"lat":23.12911,"lng":113.264385}}
         * icon : https://maps.gstatic.com/mapfiles/place_api/icons/geocode-71.png
         * id : 1eab06e98e9d9a81941d427c1ead4ece2f75ed67
         * name : 广州市
         * place_id : ChIJxytco5X4AjQRFeTqrXXgWQ4
         * reference : ChIJxytco5X4AjQRFeTqrXXgWQ4
         * scope : GOOGLE
         * vicinity : 广州市
         */

        private GeometryBean geometry;
        private String icon;
        private String id;
        private String name;
        private String place_id;
        private String reference;
        private String scope;
        private String vicinity;

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

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getScope() {
            return scope;
        }

        public void setScope(String scope) {
            this.scope = scope;
        }

        public String getVicinity() {
            return vicinity;
        }

        public void setVicinity(String vicinity) {
            this.vicinity = vicinity;
        }

        public static class GeometryBean {
            /**
             * location : {"lat":23.12911,"lng":113.264385}
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
                 * lat : 23.12911
                 * lng : 113.264385
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
    }
}
