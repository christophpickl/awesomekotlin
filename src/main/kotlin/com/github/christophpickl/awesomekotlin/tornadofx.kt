package com.github.christophpickl.awesomekotlin

import javafx.collections.FXCollections
import javafx.scene.layout.VBox
import tornadofx.App
import tornadofx.Controller
import tornadofx.View
import tornadofx.add
import tornadofx.borderpane
import tornadofx.button
import tornadofx.center
import tornadofx.label
import tornadofx.listview
import tornadofx.plusAssign
import tornadofx.vbox

// https://github.com/edvin/tornadofx
// https://edvin.gitbooks.io/tornadofx-guide/content/3.%20Components.html

// https://github.com/TomasMikula/ReactFX
// https://github.com/ReactiveX/RxJavaFX
// http://kovenant.komponents.nl/

class MyApp : App(MyView::class)

class MyController : Controller() {
    val values = FXCollections.observableArrayList("Alpha", "Beta", "Gamma", "Delta")
}

class MyView : View() {

    private val controller: MyController by inject()

    override val root = borderpane {
        top(TopView::class)
        center {
            add(listview(controller.values))
        }
        bottom(BottomView::class)
    }

//    private val topView: TopView by inject()
//    private val bottomView: BottomView by inject()
//    init {
//        with(root) {
//            top = topView.root
//            bottom = bottomView.root
//        }
//    }

}

class TopView : View() {

    // var inputField: TextField by singleAssign()
    override val root = vbox {
        label("TopView")
        // inputField = textfield()
        button("press me") {
            setOnAction {
                println("pressed")
                /*
                runAsync {
                    myController.loadText()
                } ui { loadedText ->
                    textfield.text = loadedText
                }
                 */
            }
        }
    }
}

class BottomView : View() {
    override val root = VBox()
    private val label = label() // change text via controller

    init {
        root += label("BottomView")
        root += label
    }
}


// https://edvin.gitbooks.io/tornadofx-guide/content/19.%20JSON%20and%20REST.html
