package com.batman.events;

import com.batman.constants.CrudAction;
import com.batman.model.BaseModel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
@ToString
public class CrudEvent<T extends BaseModel> extends ApplicationEvent {

    private final T baseModel;

    private final CrudAction crudAction;

    public CrudEvent(Object source, T baseModel, CrudAction crudAction) {
        super(source);
        this.baseModel = baseModel;
        this.crudAction = crudAction;
    }
}
