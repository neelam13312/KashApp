package in.games.Gameskash.Model;

public class FundModel {
    int image;
    String title;
    public FundModel() {
    }

    public FundModel(int image, String title) {
        this.image = image;
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
