package moe.tlaster.shiba.scripting

import android.util.Log
import moe.tlaster.shiba.Shiba
import moe.tlaster.shiba.ShibaView
import moe.tlaster.shiba.scripting.conversion.*
import moe.tlaster.shiba.scripting.visitors.JSViewVisitor
import org.liquidplayer.javascript.JSContext
import org.liquidplayer.javascript.JSFunction
import org.liquidplayer.javascript.JSValue

class DefaultScriptRuntime : IScriptRuntime {
    private val runtime = JSContext()

    override fun execute(script: String): Any? {
        return runtime.evaluateScript(script)?.toNative()
    }

    override fun addTypeConversion(conversion: ITypeConversion) {
        conversions.add(conversion)
    }

    public fun addObject(name: String, value: (JSContext) -> Any) {
        runtime.property(name, value.invoke(runtime))
    }

    override fun hasFunction(name: String): Boolean {
        return hasFunction(runtime, name)
    }

    override fun hasFunction(instance: Any?, name: String): Boolean {
        if (instance !is JSValue || !instance.isObject) {
            return false
        }
        return instance.toObject().takeIf {
            it.hasProperty(name)
        }?.let {
            it.property(name)
        }?.takeIf {
            it.isObject
        }?.let {
            it.toObject()
        }?.let {
            it.isFunction
        } ?: false
    }

    override fun callFunction(name: String, vararg parameters: Any?): Any? {
        return callFunction(runtime, name, *parameters)
    }

    override fun callFunction(instance: Any?, name: String, vararg parameters: Any?): Any? {
        if (instance !is JSValue || !instance.isObject) {
            return null
        }
        val obj = instance.toObject().property(name).toObject()
        if (obj.isFunction) {
            kotlin.runCatching {
                // TODO: sometime cast will fail
                (obj as JSFunction).apply(null, parameters.map { it.toJSValue(runtime) }.toTypedArray())?.toNative()
            }.onSuccess {
                return it
            }.onFailure {
                Log.e("script", it.message)
                it.printStackTrace()
            }
        } else {
            Log.i("err", "func")
            Log.i("err", obj.toString())
        }
        return null
    }

    override fun isArray(instance: Any?): Boolean {
        return instance is JSValue && instance.isArray
    }

    override fun toArray(instance: Any?): List<Any> {
        if (isArray(instance)) {
            return (instance as JSValue).toJSArray()
        }
        return emptyList()
    }

    override fun getProperty(instance: Any?, name: String): Any? {
        if (instance is JSValue && instance.isObject) {
            val obj = instance.toObject()
            return obj.property(name).toNative()
        }
        return null
    }

    private val runShibaApp = object: JSFunction(runtime, "runShibaApp") {
        fun runShibaApp(view: JSValue?): Boolean {
            if (view == null) {
                return false
            }
            val component = JSViewVisitor.visit(view)
            if (component is ShibaView) {
                Shiba.appComponent = component
                return true
            }
            return false
        }
    }

    private val registerComponent = object : JSFunction(runtime, "registerComponent") {
        fun registerComponent(name: String?, view: JSValue?): Boolean {
            if (name.isNullOrEmpty()) {
                return false
            }
            if (view == null) {
                return false
            }

            val component = JSViewVisitor.visit(view)
            if (component is ShibaView) {
                Shiba.components[name] = component
                return true
            }

            return false
        }
    }

    init {
//        addObject("http") {
//            Http(it)
//        }
        addTypeConversion(JsonConversion())
        addTypeConversion(PromiseConversion())
        runtime.property("registerComponent", registerComponent)
        runtime.property("runShibaApp", runShibaApp)
    }

}
