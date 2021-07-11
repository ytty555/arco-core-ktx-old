package ru.yurtitov.arco.informer

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

abstract class EventInformer<T> {
    private val _observable: Subject<Event<T>> = PublishSubject.create()

    /**
     *  Observable information about event happened
     */
    val observable: Observable<Event<T>> = _observable.hide()

    /**
     * Function send information about event happened to observers
     * @param: Event with some payload
     */
    fun informAbout(event: Event<T>) {
        _observable.onNext(event)
    }
}
