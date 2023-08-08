package in.games.GamesSattaBets.Interfaces;

import java.util.ArrayList;

import in.games.GamesSattaBets.Model.GameStatusModel;


public interface OnAllGamesListener {
    void onMatkaGames(ArrayList<GameStatusModel> matkaGameList);
    void onStarlineGames(ArrayList<GameStatusModel> starlineGAmeList);

}
