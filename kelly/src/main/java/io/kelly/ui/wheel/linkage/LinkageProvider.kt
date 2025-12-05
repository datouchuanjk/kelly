package io.kelly.ui.wheel.linkage

interface LinkageProvider<T> {
    fun getLevel1(): List<T>
    fun getLevel2(l1: T): List<T> = emptyList()
    fun getLevel3(l1: T, l2: T): List<T> = emptyList()
}