package in.games.gameskash.Model;

public class PassbookHistoryModel {
    public String getAmount_type() {
        return amount_type;
    }

    public PassbookHistoryModel(String amount_type) {
        this.amount_type = amount_type;
    }

    public void setAmount_type(String amount_type) {
        this.amount_type = amount_type;
    }

    public String getParticuler_text() {
        return particuler_text;
    }

    public void setParticuler_text(String particuler_text) {
        this.particuler_text = particuler_text;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getTrans_damt() {
        return trans_damt;
    }

    public void setTrans_damt(String trans_damt) {
        this.trans_damt = trans_damt;
    }

    public String getTrans_id() {
        return trans_id;
    }

    public void setTrans_id(String trans_id) {
        this.trans_id = trans_id;
    }

    public String getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(String date_and_time) {
        this.date_and_time = date_and_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPrev_amount() {
        return prev_amount;
    }

    public void setPrev_amount(String prev_amount) {
        this.prev_amount = prev_amount;
    }

    public String getTrnasaction_amt() {
        return trnasaction_amt;
    }

    public void setTrnasaction_amt(String trnasaction_amt) {
        this.trnasaction_amt = trnasaction_amt;
    }

    public String getCurrent_amt() {
        return current_amt;
    }

    public void setCurrent_amt(String current_amt) {
        this.current_amt = current_amt;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAdded_by() {
        return added_by;
    }

    public void setAdded_by(String added_by) {
        this.added_by = added_by;
    }

    String date;
    String particuler_text;
    String mode;
    String trans_damt;
    String trans_id;
    String date_and_time;
    String status;
    String prev_amount;
    String trnasaction_amt;
    String current_amt;
    String request_id;
    String amount_type;
    String added_by;
    String bet_type;
    String matka_name;
    String matka_id;
    String game_name,bid_played,transaction_id;

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public String getBet_type() {
        return bet_type;
    }

    public void setBet_type(String bet_type) {
        this.bet_type = bet_type;
    }


    public String getMatka_name() {
        return matka_name;
    }

    public void setMatka_name(String matka_name) {
        this.matka_name = matka_name;
    }

    public String getMatka_id() {
        return matka_id;
    }

    public void setMatka_id(String matka_id) {
        this.matka_id = matka_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getBid_played() {
        return bid_played;
    }

    public void setBid_played(String bid_played) {
        this.bid_played = bid_played;
    }


    public PassbookHistoryModel() {
    }

  }
