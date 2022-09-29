package com.dl.playfun.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.dl.playfun.BR;

public class CoinExchangePriceInfo extends BaseObservable{
//    {
//        "id": 9,
//         "coins": 39900,
//         "gameCoins": 88888
//    }

    private Integer id;
    private Integer coins;
    private Integer gameCoins;

    @Bindable
    public Integer getId() {
        return id;
    }


    public void setId(Integer id) {
        this.id = id;
        notifyPropertyChanged(BR.id);
    }

    @Bindable
    public Integer getCoins() {
        return coins;
    }

    public void setCoins(Integer coins) {
        this.coins = coins;
        notifyPropertyChanged(BR.coins);
    }

    @Bindable
    public Integer getGameCoins() {
        return gameCoins;
    }

    public void setGameCoins(Integer gameCoins) {
        this.gameCoins = gameCoins;
        notifyPropertyChanged(BR.gameCoins);
    }
}
