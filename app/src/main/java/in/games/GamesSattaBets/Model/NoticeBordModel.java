package in.games.GamesSattaBets.Model;

public class NoticeBordModel {
    String title;
    String discription ;

    public NoticeBordModel() {
    }


    public NoticeBordModel(String title, String discription) {
        this.title = title;
        this.discription = discription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }
}
