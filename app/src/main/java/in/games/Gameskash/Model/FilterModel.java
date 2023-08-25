package in.games.Gameskash.Model;

public class FilterModel {
    String game;
    boolean isSelected=false;

    public FilterModel() {
    }

    public FilterModel(String game) {
        this.game = game;
    }

    public String getGame() {
        return game;
    }

    public void setGame(String game) {
        this.game = game;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
