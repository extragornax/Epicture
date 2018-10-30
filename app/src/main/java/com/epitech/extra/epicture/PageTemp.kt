package com.epitech.extra.epicture

open class PageTemp {

    var className: String

    constructor(name: String) {
        className = name
    }

    fun retClassName(): String {
        //return this.localClassName
        return className
    }

}