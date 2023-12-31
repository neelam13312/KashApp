package in.games.gameskash.Model;

public class MatkaModel{
    String id;
    String name,start_time,end_time,starting_num,number;
    String end_num,bid_start_time,bid_end_time,created_at;
    String updated_at,status,sat_end_time,sat_start_time;
    String loader,text,text_status;
    String open_bid_last_time,close_bid_last_time,open_result_time,close_result_time;
    String color,is_market_open,is_market_open_nextday,is_market_open_nextday2;




    public MatkaModel() {
    }
    public MatkaModel(String id, String name, String start_time, String end_time,
                      String starting_num, String number, String end_num, String bid_start_time,
                      String bid_end_time, String created_at, String updated_at, String status,
                      String sat_end_time, String sat_start_time, String loader, String text, String text_status,String is_market_open) {
        this.id = id;
        this.name = name;
        this.start_time = start_time;
        this.end_time = end_time;
        this.starting_num = starting_num;
        this.number = number;
        this.end_num = end_num;
        this.bid_start_time = bid_start_time;
        this.bid_end_time = bid_end_time;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.status = status;
        this.sat_end_time = sat_end_time;
        this.sat_start_time = sat_start_time;
        this.loader = loader;
        this.text = text;
        this.text_status = text_status;
        this.is_market_open = is_market_open;
    }

    public String getIs_market_open_nextday() {
        return is_market_open_nextday;
    }

    public void setIs_market_open_nextday(String is_market_open_nextday) {
        this.is_market_open_nextday = is_market_open_nextday;
    }

    public String getIs_market_open_nextday2() {
        return is_market_open_nextday2;
    }

    public void setIs_market_open_nextday2(String is_market_open_nextday2) {
        this.is_market_open_nextday2 = is_market_open_nextday2;
    }

    public String getIs_market_open() {
        return is_market_open;
    }

    public void setIs_market_open(String is_market_open) {
        this.is_market_open = is_market_open;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
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

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStarting_num() {
        return starting_num;
    }

    public void setStarting_num(String starting_num) {
        this.starting_num = starting_num;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEnd_num() {
        return end_num;
    }

    public void setEnd_num(String end_num) {
        this.end_num = end_num;
    }

    public String getBid_start_time() {
        return bid_start_time;
    }

    public void setBid_start_time(String bid_start_time) {
        this.bid_start_time = bid_start_time;
    }

    public String getBid_end_time() {
        return bid_end_time;
    }

    public void setBid_end_time(String bid_end_time) {
        this.bid_end_time = bid_end_time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSat_end_time() {
        return sat_end_time;
    }

    public void setSat_end_time(String sat_end_time) {
        this.sat_end_time = sat_end_time;
    }

    public String getSat_start_time() {
        return sat_start_time;
    }

    public void setSat_start_time(String sat_start_time) {
        this.sat_start_time = sat_start_time;
    }

    public String getLoader() {
        return loader;
    }

    public void setLoader(String loader) {
        this.loader = loader;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText_status() {
        return text_status;
    }

    public void setText_status(String text_status) {
        this.text_status = text_status;
    }

    public String getOpen_bid_last_time() {
        return open_bid_last_time;
    }

    public void setOpen_bid_last_time(String open_bid_last_time) {
        this.open_bid_last_time = open_bid_last_time;
    }

    public String getClose_bid_last_time() {
        return close_bid_last_time;
    }

    public void setClose_bid_last_time(String close_bid_last_time) {
        this.close_bid_last_time = close_bid_last_time;
    }

    public String getOpen_result_time() {
        return open_result_time;
    }

    public void setOpen_result_time(String open_result_time) {
        this.open_result_time = open_result_time;
    }

    public String getClose_result_time() {
        return close_result_time;
    }

    public void setClose_result_time(String close_result_time) {
        this.close_result_time = close_result_time;
    }
}
