package com.batman.listeners;

import com.batman.constants.CrudAction;
import com.batman.elastic.RetroEsRepository;
import com.batman.events.CrudEvent;
import com.batman.model.BaseModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CrudEventListener<T extends BaseModel> {

    private final RetroEsRepository<T> retroEsRepository;

    @EventListener
    public void onCrudEvent(CrudEvent<T> event) {
        log.info("Entering CrudEventListener onCrudEvent ...");
        T model = event.getBaseModel();
        CrudAction crudAction = event.getCrudAction();
        log.info("Received Event for {} for Model {}", crudAction, model.getClass());
        if (crudAction == CrudAction.CREATE) {
            retroEsRepository.save(model);
        }
        log.info("Exiting CrudEventListener onCrudEvent ...");
    }
}
