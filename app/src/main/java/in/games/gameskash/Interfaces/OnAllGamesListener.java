package in.games.gameskash.Interfaces;

import java.util.ArrayList;

import in.games.gameskash.Model.GameStatusModel;


public interface OnAllGamesListener {
    void onMatkaGames(ArrayList<GameStatusModel> matkaGameList);
    void onStarlineGames(ArrayList<GameStatusModel> starlineGAmeList);

}
