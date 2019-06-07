package moe.tlaster.shiba.scripting

import moe.tlaster.shiba.scripting.conversion.ITypeConversion

interface IScriptRuntime {
    fun callFunction(name: String, vararg parameters: Any?): Any?
    fun callFunction(instance: Any?, name: String, vararg parameters: Any?): Any?
    fun execute(script: String): Any?
    fun addTypeConversion(conversion: ITypeConversion)
    fun getProperty(instance: Any?, name: String): Any?
    fun isArray(instance: Any?): Boolean
    fun toArray(instance: Any?): List<Any>
}