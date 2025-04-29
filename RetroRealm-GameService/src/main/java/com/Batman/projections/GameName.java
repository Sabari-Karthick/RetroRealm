package com.Batman.projections;

/**
 * Projection interface for retrieving the name of a game.
 * This interface is used to retrieve only the game ID and name fields
 * from the {@link com.Batman.entity.Game} entity.
 *
 * <p>
 * Projections are used to retrieve only the necessary data from the database,
 * reducing the amount of data transferred and improving performance.
 * </p>
 *
 * @author SK
 */

public interface GameName extends View {
	
     String getGameId();
     String getGameName();
    
}
