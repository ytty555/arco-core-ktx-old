package ru.yurtitov.arco.core.informer

interface Event<out V> {
    val payload: V
}