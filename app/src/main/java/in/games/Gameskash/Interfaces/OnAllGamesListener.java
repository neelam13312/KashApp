package in.games.Gameskash.Interfaces;

import java.util.ArrayList;

import in.games.Gameskash.Model.GameStatusModel;


public interface OnAllGamesListener {
    void onMatkaGames(ArrayList<GameStatusModel> matkaGameList);
    void onStarlineGames(ArrayList<GameStatusModel> starlineGAmeList);

}
