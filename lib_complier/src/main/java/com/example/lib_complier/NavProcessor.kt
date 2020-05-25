import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.example.lib_annotation.ActivityDestination
import com.example.lib_annotation.FragmentDestination
import com.google.auto.service.AutoService
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation

/**
 * @author wangxu
 * @date 20-5-11
 * @Description
 *
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes(
    "com.example.lib_annotation.FragmentDestination",
    "com.example.lib_annotation.ActivityDestination"
)
class NavProcessor : AbstractProcessor() {
    private var messager: Messager? = null
    private var filer: Filer? = null

    companion object {
        private const val OUTPUT_FILE_NAME = "destination.json"
    }

    @Synchronized
    override fun init(porcessEnv: ProcessingEnvironment?) {
        super.init(porcessEnv)
        // 获取控制台日志
        messager = porcessEnv?.messager
        // 获取文件对象，最终可以用来生成文件
        filer = processingEnv.filer
    }

    override fun process(
        annotation: MutableSet<out TypeElement>?,
        roundEnvironment: RoundEnvironment?
    ): Boolean {
        val fragmentElements = roundEnvironment?.getElementsAnnotatedWith(
            FragmentDestination::class.java
        )

        val activityElements = roundEnvironment?.getElementsAnnotatedWith(
            ActivityDestination::class.java
        )

        println("开始编译")
        if (!fragmentElements?.isEmpty()!! || !activityElements?.isEmpty()!!) {
            val destMap = HashMap<String, JSONObject>()
            handleDestination(fragmentElements, FragmentDestination::class.java, destMap)
            handleDestination(
                activityElements as Set<Element>,
                ActivityDestination::class.java,
                destMap
            )

//            // 生成文件 app/src/main/assets
//               filer.createResource()意思是创建源文件
//               我们可以指定为class文件输出的地方，
//               StandardLocation.CLASS_OUTPUT：java文件生成class文件的位置，/app/build/intermediates/javac/debug/classes/目录下
//               StandardLocation.SOURCE_OUTPUT：java文件的位置，一般在/ppjoke/app/build/generated/source/apt/目录下
//               StandardLocation.CLASS_PATH 和 StandardLocation.SOURCE_PATH用的不多，指的了这个参数，就要指定生成文件的pkg包名了

            var fileOutputStream: FileOutputStream? = null
            var outputStreamWriter: OutputStreamWriter? = null
            try {

                val fileObject =
                    filer?.createResource(StandardLocation.CLASS_OUTPUT, "", OUTPUT_FILE_NAME)
                val resourcePath = fileObject?.toUri()?.path
                messager!!.printMessage(Diagnostic.Kind.NOTE, "resourcePath:$resourcePath")

                val appPath = resourcePath?.substring(0, resourcePath.indexOf("myapplication") + 14)
                val assetsPath = appPath + "src/main/assets/"
                println("资源路径地址： $assetsPath")

                val file = File(assetsPath)
                if (!file.exists()) {
                    file.mkdirs()
                }
                val outputFile = File(file, OUTPUT_FILE_NAME)
                if (outputFile.exists()) {
                    outputFile.delete()
                }
                outputFile.createNewFile()
                val content = JSON.toJSONString(destMap)
                fileOutputStream = FileOutputStream(outputFile)
                outputStreamWriter = OutputStreamWriter(fileOutputStream, "utf-8")
                outputStreamWriter.write(content)
                outputStreamWriter.flush()

            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                outputStreamWriter?.close()
                fileOutputStream?.close()
            }
        }
        return true
    }

    private fun handleDestination(
        elements: Set<Element>,
        annotationClazz: Class<out Annotation?>,
        destMap: java.util.HashMap<String, JSONObject>
    ) {
        println("elements size :" + elements.size)
        for (element in elements) {
            val typeElement = element as TypeElement
            var pageUrl: String = ""
            val clazzName = typeElement.qualifiedName.toString()
            val id = Math.abs(clazzName.hashCode())
            var needLogin = false
            var asStart = false
            val annotation = typeElement.getAnnotation(annotationClazz)
            var isFragment: Boolean = false
            if (annotation is FragmentDestination) {
                pageUrl = annotation.pageUrl
                needLogin = annotation.needLogin
                asStart = annotation.asStart
                isFragment = true
            } else if (annotation is ActivityDestination) {
                pageUrl = annotation.pageUrl
                needLogin = annotation.needLogin
                asStart = annotation.asStart
                isFragment = false
            }
            println(pageUrl + destMap.size)

            if (destMap.containsKey(pageUrl)) {
                messager?.printMessage(Diagnostic.Kind.ERROR, "不同的页面不允许使用相同的 pageURL: $clazzName")
            } else {
                val jsonObject = JSONObject()
                jsonObject["id"] = id
                jsonObject["needLogin"] = needLogin
                jsonObject["asStart"] = asStart
                jsonObject["pageUrl"] = pageUrl
                jsonObject["className"] = clazzName
                jsonObject["isFragment"] = isFragment
                destMap[pageUrl] = jsonObject
            }

        }
    }

}