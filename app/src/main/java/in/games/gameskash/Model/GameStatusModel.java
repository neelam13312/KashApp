package in.games.gameskash.Model;

public class GameStatusModel {
    String game_id,game_name,name,points,is_close,is_disabled,is_starline_disable,is_deleted,starline_points;

    public GameStatusModel() {
    }

    public String getIs_starline_disable() {
        return is_starline_disable;
    }

    public void setIs_starline_disable(String is_starline_disable) {
        this.is_starline_disable = is_starline_disable;
    }

    public String getStarline_points() {
        return starline_points;
    }

    public void setStarline_points(String starline_points) {
        this.starline_points = starline_points;
    }

    public String getGame_id() {
        return game_id;
    }

    public void setGame_id(String game_id) {
        this.game_id = game_id;
    }

    public String getGame_name() {
        return game_name;
    }

    public void setGame_name(String game_name) {
        this.game_name = game_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getIs_close() {
        return is_close;
    }

    public void setIs_close(String is_close) {
        this.is_close = is_close;
    }

    public String getIs_disabled() {
        return is_disabled;
    }

    public void setIs_disabled(String is_disabled) {
        this.is_disabled = is_disabled;
    }

    public String getIs_deleted() {
        return is_deleted;
    }

    public void setIs_deleted(String is_deleted) {
        this.is_deleted = is_deleted;
    }
}

