package com.github.christophpickl.awesomekotlin

import com.github.salomonbrys.kodein.*

// KOtlin DEpendency INjection
// kotlin like, small DI container: https://github.com/SalomonBrys/Kodein
// https://salomonbrys.github.io/Kodein/

/*
more:
- Aware interfaces remove syntax to reference kodein
- Configurable Kodein via maven dep "kodein-conf" to have something like "mutable kodein config object"
- god complex: kodein.global instance ;)
- type erasure problem bypassed by genericToken(), but performance problem! use erased* methods instead
    - use "kodein-erased" module instead to default to erased version (faster but unsafe)
- scopes are supported

compile 'com.github.salomonbrys.kodein:kodein:3.2.0'
compile 'com.github.salomonbrys.kodein:kodein-android:3.2.0'
 */
fun main(args: Array<String>) {
    val kodein = configureKodein()
    println(kodein.container.bindings.description)

//    Resource1(kodein).printGreet()
    Resource1(kodein).printPersonalGreet("Christoph")
//    Resource2(kodein).printGreet()

    println("Building subKodein ...")
    val subKodein = Kodein {
        extend(kodein)
        // useful for tests ;) need to allow modules explicitly to be able to override bindings
        bind<Repository>(overrides = true) with singleton { ProxyRepository(overriddenInstance()) }

        onReady {
            println("subKodein onReady")
            val repo = instance<Repository>()
            repo.initState()
        }
    }
    println(subKodein.container.bindings.description)
    Resource1(subKodein).printGreet()
}

fun configureKodein() = Kodein {
    println("Configure Kodein ...")

    constant("lang") with Lang.DE

    import(RepoModule)

    bind<ProvidedService>() with provider { ProvidedServiceImpl() }
    bind<SingletonService>() with singleton { SingletonServiceWithRepo(kodein) }

    bind<LazyService>() with factory { name: String -> ServiceProvidedRuntimeWithAssistedInject(name, kodein) }

    bind<Logger>() with factory { cls: Class<*> -> SysoutLogger(cls) }

    // bind<Dice>() with factory { sides: Int -> RandomDice(sides) }
    // bind<DataSource>() with eagerSingleton { SqliteDS.open("path/to/file") }
    // bind<Cache>() with threadSingleton { LRUCache(16 * 1024) }
    // bind<DataSource>() with instance(SqliteDataSource.open("path/to/file"))
    // bind<Dice>("Tag1") with provider { RandomDice(10) }
    // bind<Dice>("Tag2") with provider { RandomDice(20) }

    // bind() from singleton { RandomDice(6) }

    println("Configure Kodein ... DONE")
}

interface LazyService {
    fun greeting(): String
}
class ServiceProvidedRuntimeWithAssistedInject(
        private val runtimeName: String,
        kodein: Kodein,
        private val repository: Repository = kodein.instance()
) : LazyService {
    override fun greeting() = "${javaClass.simpleName} says: ${repository.loadSalutation()} $runtimeName!"
}

class Resource1(
        kodein: Kodein,
        private val singletonService: SingletonService = kodein.instance(),
        private val providedService: ProvidedService = kodein.instance(),
        private val lazyServiceFactory: (String) -> LazyService = kodein.factory()
) {
    private val log: Logger = kodein.withClassOf(this).instance()

    fun printGreet() {
        log.debug("printGreet()")
        println(singletonService.greeting())
//        println(providedService.greeting())
    }

    fun printPersonalGreet(name: String) {
        log.debug("printPersonalGreet(name)")
        println(lazyServiceFactory(name).greeting())
    }
}

class Resource2(
        kodein: Kodein,
        private val singletonService: SingletonService = kodein.instance(),
        private val providedService: ProvidedService = kodein.instance()
) {
    fun printGreet() {
        println(singletonService.greeting())
        println(providedService.greeting())
    }
}

interface SingletonService {
    fun greeting(): String
}

class SingletonServiceWithRepo(
        kodein: Kodein,
        private val repository: Repository = kodein.instance()
) : SingletonService {
    init {
        println("new ${javaClass.simpleName}()")
    }

    override fun greeting() = "${repository.loadSalutation()} from ${javaClass.simpleName}!"
}

interface ProvidedService {
    fun greeting(): String
}

class ProvidedServiceImpl : ProvidedService {
    init {
        println("new ${javaClass.simpleName}()")
    }

    override fun greeting() = "Hi from ${javaClass.simpleName}!"

}

val RepoModule = Kodein.Module {
    println("Preparing RepoModule ...")
    bind<Repository>() with singleton { RepositoryImpl(instance("lang")) }
}

interface Repository {
    fun loadSalutation(): String
    fun initState() {
        println("${javaClass.simpleName} initState()")
    }
}

class RepositoryImpl(private val lang: Lang) : Repository {
    init {
        println("new ${javaClass.simpleName}(lang=$lang)")
    }
    override fun loadSalutation() = if (lang == Lang.DE) "Hallo" else "Hello"

}
enum class Lang {
    DE, EN;
}

class ProxyRepository(private val originalRepo: Repository) : Repository {
    override fun loadSalutation(): String {
        println("ProxyRepository => Do something more in salutation")
        return originalRepo.loadSalutation()
    }

}

interface Logger {
    fun debug(message: String)
}
class SysoutLogger(cls: Class<*>) : Logger {

    private val className = cls.simpleName

    override fun debug(message: String) {
        println("$className [DEBUG] $message")
    }

}
