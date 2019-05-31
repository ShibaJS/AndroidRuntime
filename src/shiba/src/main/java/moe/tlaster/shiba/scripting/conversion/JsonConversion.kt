package moe.tlaster.shiba.scripting.conversion

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import moe.tlaster.shiba.common.Singleton
import org.liquidplayer.javascript.JSContext
import org.liquidplayer.javascript.JSObject
import org.liquidplayer.javascript.JSValue

class JsonConversion(override val objectType: Class<*> = JsonNode::class.java) : ITypeConversion {
    override fun toJSValue(context: JSContext, value: Any): JSValue {

        if (value !is JsonNode) {
            return JSValue(context,null)
        }
        if (value.isObject) {
            // TODO:
            //W/System.err: java.lang.ArrayIndexOutOfBoundsException: length=320; index=320
            //        at android.util.LongSparseArray.gc(Unknown Source:20)
            //        at android.util.LongSparseArray.put(Unknown Source:48)
            //        at org.liquidplayer.javascript.JSContext.persistObject(JSContext.java:297)
            //        at org.liquidplayer.javascript.JSArray.<init>(JSArray.java:106)
            //        at org.liquidplayer.javascript.JSArray.<init>(JSArray.java:132)
            //        at org.liquidplayer.javascript.JSValue.<init>(JSValue.java:58)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:278)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:298)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.put(JSObjectPropertiesMap.java:139)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.putAll(JSObjectPropertiesMap.java:163)
            //        at org.liquidplayer.javascript.JSObject.<init>(JSObject.java:236)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.<init>(JSObjectPropertiesMap.java:57)
            //        at org.liquidplayer.javascript.JSValue.<init>(JSValue.java:56)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:278)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:298)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.put(JSObjectPropertiesMap.java:139)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.putAll(JSObjectPropertiesMap.java:163)
            //        at org.liquidplayer.javascript.JSObject.<init>(JSObject.java:236)
            // TODO:
            //E/script: org.liquidplayer.javascript.JNIJSValue cannot be cast to org.liquidplayer.javascript.JNIJSObject
            //W/System.err: java.lang.ClassCastException: org.liquidplayer.javascript.JNIJSValue cannot be cast to org.liquidplayer.javascript.JNIJSObject
            //W/System.err:     at org.liquidplayer.javascript.JNIJSObject.fromRef(JNIJSObject.java:80)
            //        at org.liquidplayer.javascript.JNIJSContext.make(JNIJSContext.java:73)
            //        at org.liquidplayer.javascript.JSObject.<init>(JSObject.java:134)
            //        at org.liquidplayer.javascript.JSObject.<init>(JSObject.java:235)
            //W/System.err:     at org.liquidplayer.javascript.JSObjectPropertiesMap.<init>(JSObjectPropertiesMap.java:57)
            //        at org.liquidplayer.javascript.JSValue.<init>(JSValue.java:56)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:278)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:298)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.put(JSObjectPropertiesMap.java:139)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.putAll(JSObjectPropertiesMap.java:163)
            //        at org.liquidplayer.javascript.JSObject.<init>(JSObject.java:236)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.<init>(JSObjectPropertiesMap.java:57)
            //W/System.err:     at org.liquidplayer.javascript.JSValue.<init>(JSValue.java:56)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:278)
            //        at org.liquidplayer.javascript.JSObject.property(JSObject.java:298)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.put(JSObjectPropertiesMap.java:139)
            //        at org.liquidplayer.javascript.JSObjectPropertiesMap.putAll(JSObjectPropertiesMap.java:163)
            //        at org.liquidplayer.javascript.JSObject.<init>(JSObject.java:236)

            return JSObject(context, Singleton.get<ObjectMapper>().convertValue(value, Map::class.java))
        }
        return JSValue(context, when {
            value.isArray -> Singleton.get<ObjectMapper>().convertValue(value, List::class.java)
            else -> null
        })
    }

    override fun fromJSValue(value: JSValue): Any? {
        throw NotImplementedError()
    }

    override fun canConvert(value: JSValue): Boolean {
        return false
    }
}