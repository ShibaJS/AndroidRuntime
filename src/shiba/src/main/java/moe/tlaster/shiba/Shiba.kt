package moe.tlaster.shiba

import android.app.Application
import android.content.Context
import android.util.ArrayMap
import moe.tlaster.shiba.commonProperty.GridProperty
import moe.tlaster.shiba.commonProperty.ICommonProperty
import moe.tlaster.shiba.extensionExecutor.BindingExecutor
import moe.tlaster.shiba.extensionExecutor.IExtensionExecutor
import moe.tlaster.shiba.extensionExecutor.JsonExecutor
import moe.tlaster.shiba.mapper.*
import moe.tlaster.shiba.scripting.*
import moe.tlaster.shiba.scripting.conversion.JsonConversion
import moe.tlaster.shiba.scripting.runtime.Http
import moe.tlaster.shiba.scripting.runtime.Storage
import moe.tlaster.shiba.type.View

internal typealias NativeView = android.view.View
internal typealias ShibaView = View

class ShibaConfiguration {
    var scriptRuntime: IScriptRuntime = DefaultScriptRuntime()
    var platformType = "Android"
    val commonProperties = ArrayList<ICommonProperty>()
    val extensionExecutors = ArrayList<IExtensionExecutor>()
    val nativeConverter = androidx.collection.ArrayMap<String, ((List<Any?>) -> Any?)>()
}

object Shiba {
    internal var appComponent: ShibaView? = null
    val viewMapping = ArrayMap<String, IViewMapper<*>>()
    val configuration = ShibaConfiguration()
    internal val components = ArrayMap<String, ShibaView>()

    public fun init(application: Context) {
        addRenderer("stack", StackMapper())
        addRenderer("text", TextMapper())
        addRenderer("input", InputMapper())
        addRenderer("list", ListMapper())
        addRenderer("grid", GridMapper())
        addExtensionExecutor(BindingExecutor())
        addExtensionExecutor(JsonExecutor())
        configuration.commonProperties.add(GridProperty())
        if (configuration.scriptRuntime is DefaultScriptRuntime) {
            (configuration.scriptRuntime as DefaultScriptRuntime).addObject("shibaStorage") {
                Storage(application, it)
            }
            (configuration.scriptRuntime as DefaultScriptRuntime).addObject("http") {
                Http(it)
            }
        }
    }

    public fun addConverter(name: String, converter: ((List<Any?>) -> Any?)) {
        configuration.nativeConverter[name] = converter
    }

    public fun addRenderer(name: String, mapper: IViewMapper<*>) {
        viewMapping[name] = mapper
    }

    public fun addExtensionExecutor(executor: IExtensionExecutor) {
        configuration.extensionExecutors.add(executor)
    }
}