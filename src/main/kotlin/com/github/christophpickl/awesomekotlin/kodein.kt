package com.github.christophpickl.awesomekotlin

import com.github.salomonbrys.kodein.*

fun main(args: Array<String>) {
    val kodein = configureKodein()

    Resource1(kodein).printGreet()
    Resource2(kodein).printGreet()
}

fun configureKodein() = Kodein {
    // https://salomonbrys.github.io/Kodein/
    println("Configure Kodein ...")
    constant("lang") with Lang.DE
    bind<Repository>() with singleton { RepositoryImpl(instance("lang")) }
    bind<ProvidedService>() with provider { ProvidedServiceImpl() }
    bind<SingletonService>() with singleton { SingletonServiceWithRepo(kodein) }

    // bind<Dice>() with factory { sides: Int -> RandomDice(sides) }
    // bind<DataSource>() with eagerSingleton { SqliteDS.open("path/to/file") }
    // bind<Cache>() with threadSingleton { LRUCache(16 * 1024) }
    // bind<DataSource>() with instance(SqliteDataSource.open("path/to/file"))
    // bind<Dice>("Tag1") with provider { RandomDice(10) }
    // bind<Dice>("Tag2") with provider { RandomDice(20) }

    // bind() from singleton { RandomDice(6) }

    println("Configure Kodein ... DONE")
}

class Resource1(
        kodein: Kodein,
        private val singletonService: SingletonService = kodein.instance(),
        private val providedService: ProvidedService = kodein.instance()
) {
    fun printGreet() {
        println(singletonService.greeting())
        println(providedService.greeting())
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

interface Repository {
    fun loadSalutation(): String
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
