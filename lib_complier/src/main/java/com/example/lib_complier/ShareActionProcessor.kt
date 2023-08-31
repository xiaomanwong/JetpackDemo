//package com.example.lib_complier
//
//import com.example.lib_annotation.ShareItemAction
//import com.google.auto.service.AutoService
//import com.squareup.kotlinpoet.ClassName
//import com.squareup.kotlinpoet.FileSpec
//import com.squareup.kotlinpoet.FunSpec
//import com.squareup.kotlinpoet.KModifier
//import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
//import com.squareup.kotlinpoet.PropertySpec
//import com.squareup.kotlinpoet.TypeSpec
//import com.squareup.kotlinpoet.WildcardTypeName
//import com.squareup.kotlinpoet.asClassName
//import com.squareup.kotlinpoet.asTypeName
//import javax.annotation.processing.AbstractProcessor
//import javax.annotation.processing.Filer
//import javax.annotation.processing.Messager
//import javax.annotation.processing.ProcessingEnvironment
//import javax.annotation.processing.Processor
//import javax.annotation.processing.RoundEnvironment
//import javax.annotation.processing.SupportedAnnotationTypes
//import javax.annotation.processing.SupportedSourceVersion
//import javax.lang.model.SourceVersion
//import javax.lang.model.element.Element
//import javax.lang.model.element.TypeElement
//import javax.lang.model.util.Elements
//import javax.tools.Diagnostic
//import kotlin.reflect.KClass
//
//@AutoService(Processor::class)
//@SupportedSourceVersion(SourceVersion.RELEASE_8)
//@SupportedAnnotationTypes(
//    "com.example.lib_annotation.ShareItemAction",
//)
//class ShareActionProcessor() {
//    companion object {
//        const val SHARE_PACKAGE = "com.cyberrabbit.share"
//        const val CLASS_NAME = "ShareCollectorImpl"
//        const val PLATFORM_COLLECTOR_NAME = "sharePlatformCollector"
//        const val REGION_COLLECTOR_NAME = "shareRegionCollector"
//        const val ITEM_COLLECTOR_NAME = "shareItemCollector"
//        const val GET_EXTENDED_SHARE_PLATFORM = "getExtendedSharePlatform"
//
//    }
//
//    @Synchronized
//    override fun init(porcessEnv: ProcessingEnvironment?) {
//        super.init(porcessEnv)
//        // 获取控制台日志
//        messager = porcessEnv?.messager
//        // 获取文件对象，最终可以用来生成文件
//        filer = processingEnv.filer
//        elementUtils = processingEnv?.elementUtils
//    }
//
//    override fun process(
//        annotations: MutableSet<out TypeElement>?,
//        roundEnv: RoundEnvironment?
//    ): Boolean {
//        println("开始编译分享")
//        val elements4 =
//            roundEnv?.getElementsAnnotatedWith(ShareItemAction::class.java) ?: return false
//        handleShareItemAction(elements4, ShareItemAction::class.java).build()
//        return false
//
//    }
//
//    private var filer: Filer? = null
//    private var messager: Messager? = null
//    private var elementUtils: Elements? = null
//
//    private val fileBuilder =
//        FileSpec.builder(SHARE_PACKAGE, CLASS_NAME)
//    private val classBuilder = TypeSpec.classBuilder(CLASS_NAME)
//    private var funcList = mutableListOf<FunSpec>()
//    private val propertyList = mutableListOf<PropertySpec>()
//
//
//    fun handleShareItemAction(
//        elements: MutableSet<out Element>,
//        annotationClazz: Class<out Annotation>
//    ): ShareActionProcessor {
//
//        val propertyName = ITEM_COLLECTOR_NAME
//        val keyType = String::class.asTypeName()
//        val valueType = Pair::class.asClassName().parameterizedBy(
//            List::class.asTypeName().parameterizedBy(
//                KClass::class.asClassName().parameterizedBy(
//                    WildcardTypeName.producerOf(Any::class.asClassName())
//                )
//            ),
//            List::class.asTypeName().parameterizedBy(
//                KClass::class.asClassName().parameterizedBy(
//                    WildcardTypeName.producerOf(Any::class.asClassName())
//                )
//            )
//        )
//        messager?.printMessage(
//            Diagnostic.Kind.NOTE,
//            "开始编译分享 xxxxxxxxxxxxx"
//        )
//        val propertyType = HashMap::class.asClassName().parameterizedBy(keyType, valueType)
//        val initialMap = hashMapOf<String, Pair<List<KClass<out Any>>, List<KClass<out Any>>>>()
//
//
//
//
//        elements.forEach {
//            val annotation = it.getAnnotation(annotationClazz)
//            if (annotation is ShareItemAction) {
//                if (initialMap[annotation.item] != null) {
//                    messager?.printMessage(
//                        Diagnostic.Kind.ERROR,
//                        "Compiler error: Region of ${annotation.item} multiple declarations where ${it.simpleName} and ${initialMap[annotation.item]}! please replace with other!"
//                    )
//                } else {
//                    messager?.printMessage(
//                        Diagnostic.Kind.NOTE,
//                        "share item add => ${annotation.item}"
//                    )
//                    val packageElement = elementUtils?.getPackageOf(it)
//                    val packageNameElement = packageElement?.qualifiedName.toString()
//                    var first = listOf<KClass<out Any>>()
//                    var second = listOf<KClass<out Any>>()
//
//                    it.annotationMirrors.getOrNull(1)?.apply {
//                        this.elementValues.entries.forEach { (key, value) ->
//                            if (key.simpleName.toString() == "first") {
//                                first = value.value as List<KClass<out Any>>
//                            } else if (key.simpleName.toString() == "second") {
//                                second = value.value as List<KClass<out Any>>
//                            }
//                        }
//
//                        initialMap[annotation.item] = Pair(first, second)
//                    }
//                }
//
//            }
//        }
//        funcList.add(
//            FunSpec.builder(GET_EXTENDED_SHARE_PLATFORM)
//                .addParameter("itemName", String::class.asTypeName())
//                .addStatement(
//                    """
//                       val firstLine = mutableListOf<com.cyberrabbit.local.share.destination.BaseShare>()
//                       val secondLine = mutableListOf<com.cyberrabbit.local.share.destination.BaseShare>()
//                       shareItemCollector[itemName]?.apply {
//                            first.forEach{
//                               firstLine.add(it.java.newInstance() as com.cyberrabbit.local.share.destination.BaseShare)
//                            }
//                            second.forEach{
//                               secondLine.add(it.java.newInstance() as com.cyberrabbit.local.share.destination.BaseShare)
//                            }
//                       }
//                       return Pair(firstLine, secondLine)
//
//                    """.trimIndent()
//                )
//                .returns(
//                    Pair::class.asClassName().parameterizedBy(
//                        List::class.asTypeName().parameterizedBy(
//                            Any::class.asClassName()
//                        ),
//                        List::class.asTypeName().parameterizedBy(
//                            Any::class.asClassName()
//                        )
//                    ),
//                )
//                .build()
//        )
//
//        propertyList.add(
//            PropertySpec.builder(propertyName, propertyType)
//                .initializer(
//                    "hashMapOf<%T, %T>(%L)",
//                    keyType,
//                    valueType,
////                    initialMap
//                    buildMapInitializer2(initialMap)
//                )
//                .addModifiers(KModifier.PRIVATE)
//                .build()
//        )
//        return this
//    }
//
//    fun build() {
//
//        filer?.let {
//            fileBuilder.addType(
//                classBuilder.apply {
//                    propertyList.forEach { property ->
//                        println("===> $property")
//                        this.addProperty(property)
//                    }
//                    funcList.forEach { func ->
//                        println("====>${func.name}")
//                        addFunction(func)
//                    }
//                }.build()
//            )
//                .build()
//                .writeTo(it)
//        }
//
//    }
//
//    private fun buildMapInitializer2(map: Map<String, Pair<List<KClass<out Any>>, List<KClass<out Any>>>>): String {
//
//        println("xxxxx=> 1     $map")
//        val entries = map.entries.joinToString(", ") { (key, value) ->
//
//            println("xxxxx=> 2     $key   $value")
//            """
//            "%s" to Pair(first = mutableListOf(%s),second = mutableListOf(%s))
//            """.trimIndent().format(
//                key,
//                value.first,
////                    .joinToString(", ") {
////                    "\n%s".format(it)
////                },
//                value.second//.joinToString(", ") {
////                    "\n%s".format(it)
////                }
//            ).also {
//                println("xxxxx=> 3     $it")
//            }.replace(".class", "::class")
//
//        }
//        println("xxxxx=> 4     $entries")
//        return entries
//    }
//
//    private fun buildMapInitializer(map: Map<String, ClassName>): String {
//        val entries = map.entries.joinToString(", ") { (key, value) ->
//            val result = "\n\"%s\" to %s::class.java".format(key, value)
//            result
//        }
//        return entries
//    }
//
//
//}