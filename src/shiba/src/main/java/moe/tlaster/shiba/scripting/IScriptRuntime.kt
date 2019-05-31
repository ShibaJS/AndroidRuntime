package moe.tlaster.shiba.scripting

import moe.tlaster.shiba.scripting.conversion.ITypeConversion

interface IScriptRuntime {
    fun callFunction(name: String, vararg parameters: Any?): Any?
    fun execute(script: String): Any?
    fun addTypeConversion(conversion: ITypeConversion)
}