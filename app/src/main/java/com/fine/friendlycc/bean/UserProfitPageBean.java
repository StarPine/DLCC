package com.fine.friendlycc.bean;

import java.math.BigDecimal;
import java.util.List;

/**
 * Author: 彭石林
 * Time: 2021/12/6 17:13
 * Description: This is UserProfitPageBean
 */
public class UserProfitPageBean {
    private BigDecimal totalProfits;
    private CustomProfitList   userProfitList;
    private int  enableWithdraw;
    private int  displayProfitTips;
    private String  currencyName;

    public int getDisplayProfitTips() {
        return displayProfitTips;
    }

    public void setDisplayProfitTips(int displayProfitTips) {
        this.displayProfitTips = displayProfitTips;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public BigDecimal getTotalProfits() {
        return totalProfits;
    }

    public void setTotalProfits(BigDecimal totalProfits) {
        this.totalProfits = totalProfits;
    }

    public CustomProfitList getUserProfitList() {
        return userProfitList;
    }

    public void setUserProfitList(CustomProfitList userProfitList) {
        this.userProfitList = userProfitList;
    }

    public int getEnableWithdraw() {
        return enableWithdraw;
    }

    public void setEnableWithdraw(int enableWithdraw) {
        this.enableWithdraw = enableWithdraw;
    }

    public class CustomProfitList{
        private Integer currentPage;
        private Integer perPage;
        private Integer total;
        private List<UserProfitPageInfoBean> data;

        public Integer getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(Integer currentPage) {
            this.currentPage = currentPage;
        }

        public Integer getPerPage() {
            return perPage;
        }

        public void setPerPage(Integer perPage) {
            this.perPage = perPage;
        }

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }

        public List<UserProfitPageInfoBean> getData() {
            return data;
        }

        public void setData(List<UserProfitPageInfoBean> data) {
            this.data = data;
        }
    }
}