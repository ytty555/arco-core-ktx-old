package ru.yurtitov.arco.informer

interface Event<out V> {
    val payload: V
}