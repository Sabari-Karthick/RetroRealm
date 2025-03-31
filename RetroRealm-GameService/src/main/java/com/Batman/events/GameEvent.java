package com.Batman.events;

import com.Batman.entity.Game;
import com.batman.constants.CrudAction;
import com.batman.events.CrudEvent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GameEvent extends CrudEvent<Game> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public GameEvent(Object source, Game game, CrudAction crudAction) {
        super(source, game, crudAction);
    }
}
