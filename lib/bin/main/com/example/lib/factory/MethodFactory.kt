package com.example.lib.factory

/**
 * @author admin
 * @date 2021/12/7
 * @Desc
 */
class MethodFactory {

    fun load(filePath: String): IRuleParserConfig {
        val factory = IRuleConfigParserFactoryMap.getFactory(getFileExtension(filePath))
        val parser = factory?.createParser()
        parser?.parser(filePath)
        return parser!!
    }
}

class IRuleConfigParserFactoryMap {
    companion object {
        private val cachedFactories: HashMap<String, IRuleConfigParserFactory> = HashMap()

        init {
            cachedFactories["json"] = JsonParseConfigFactory()
            cachedFactories["yaml"] = YamlParseConfigFactory()
            cachedFactories["xml"] = XmlParseConfigFactory()
            cachedFactories["properties"] = PropertiesParseConfigFactory()
        }

        fun getFactory(type: String): IRuleConfigParserFactory? {
            return cachedFactories[type]
        }
    }
}

interface IRuleConfigParserFactory {
    fun createParser(): IRuleParserConfig
}

class JsonParseConfigFactory : IRuleConfigParserFactory {
    override fun createParser(): IRuleParserConfig {
        return JsonRuleParser()
    }
}

class XmlParseConfigFactory : IRuleConfigParserFactory {
    override fun createParser(): IRuleParserConfig {
        return XmlRuleParser()
    }
}

class YamlParseConfigFactory : IRuleConfigParserFactory {
    override fun createParser(): IRuleParserConfig {
        return YamlRuleParser()
    }
}

class PropertiesParseConfigFactory : IRuleConfigParserFactory {
    override fun createParser(): IRuleParserConfig {
        return PropertiesRuleParser()
    }
}

fun main() {
    MethodFactory().load("")
}