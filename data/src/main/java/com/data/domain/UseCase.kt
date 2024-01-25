package com.data.domain

abstract class UseCase<in Param, out T> where T : Any {

    abstract fun execute(param: Param? = null): T

    protected open fun onCleared() {}
}
