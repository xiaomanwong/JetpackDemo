package com.example.lib.factory

/**
 * @author admin
 * @date 2021/12/7
 * @Desc
 */
class SimpleFactory {

    fun load(filePath: String): IRuleParserConfig {
        val parserConfig: IRuleParserConfig = RuleConfigParserFactory.createParser(filePath)
        parserConfig.parser(filePath)
        return parserConfig;
    }


}

val suffixArray: Array<String> = arrayOf("json", "yaml", "xml", "properties")
fun getFileExtension(filePath: String): String {
    return suffixArray[1]
}

class RuleConfigParserFactory {
    companion object {
        fun createParser(configFormat: String): IRuleParserConfig {
            return when (getFileExtension(configFormat)) {
                "json" -> {
                    JsonRuleParser()
                }
                "yaml" -> {
                    YamlRuleParser()
                }
                "xml" -> {
                    XmlRuleParser()
                }
                "properties" -> {
                    PropertiesRuleParser()
                }
                else -> {
                    throw IllegalArgumentException()
                }
            }
        }
    }
}

interface IRuleParserConfig {
    fun parser(filePath: String)
}

class JsonRuleParser : IRuleParserConfig {
    override fun parser(filePath: String) {
        println("Json parser Config")
    }
}

class XmlRuleParser : IRuleParserConfig {
    override fun parser(filePath: String) {
        println("Xml parser Config")
    }
}

class YamlRuleParser : IRuleParserConfig {
    override fun parser(filePath: String) {
        println("Yaml parser Config")
    }
}


class PropertiesRuleParser : IRuleParserConfig {
    override fun parser(filePath: String) {
        println("Properties parser Config")
    }
}

fun main() {
    SimpleFactory().load("")
}