package ru.inwords.inwords.domain.interactor.translation;

import javax.inject.Inject;

import io.reactivex.Completable;
import ru.inwords.inwords.core.util.SchedulersFacade;
import ru.inwords.inwords.data.sync.TranslationSyncController;

public class TranslationSyncInteractorImpl implements TranslationSyncInteractor {
    private final TranslationSyncController syncController;

    @Inject
    TranslationSyncInteractorImpl(TranslationSyncController syncController) {
        this.syncController = syncController;
    }

    @Override
    public Completable presyncOnStart() {
        return syncController.presyncOnStart()
                .subscribeOn(SchedulersFacade.io())
                .doOnError(Throwable::printStackTrace)
                .ignoreElement();
    }

    @Override
    public Completable trySyncAllReposWithCache() {
        return syncController.trySyncAllReposWithCache()
                .subscribeOn(SchedulersFacade.io());
    }

    @Override
    public void notifyDataChanged() {
        syncController.notifyDataChanged();
    }
}
