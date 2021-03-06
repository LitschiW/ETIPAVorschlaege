package eti.view.boxes

import eti.app.Styles
import eti.data.Options
import eti.extensions.isChecked
import eti.view.OptionsObserver
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Pos
import javafx.scene.layout.Priority
import tornadofx.*

class OptionsBox(private val optionsObserver: OptionsObserver) : View(), ChangeListener<Any> {
    val optionsPadding = 5.0

    val box1 = checkbox("speichere LaTeX Projekt") { paddingAll = optionsPadding; selectedProperty().addListener(this@OptionsBox) }
    val box2 = checkbox("generiere Antworten") { paddingAll = optionsPadding; selectedProperty().addListener(this@OptionsBox) }
    val box3 = checkbox("zufällige Subaufgabenbereiche") { paddingAll = optionsPadding; selectedProperty().addListener(this@OptionsBox) }
    val subTopicExerciseCountBox = textfield("2") {
        maxWidth = 40.0
        minWidth = 40.0
        alignment = Pos.BASELINE_RIGHT
        hboxConstraints { marginRight = 10.0 }
        textProperty().addListener(this@OptionsBox)
    }
    val subTopicCountBox = textfield("2") {
        maxWidth = 40.0
        minWidth = 40.0
        alignment = Pos.BASELINE_RIGHT
        hboxConstraints { marginRight = 10.0 }
        textProperty().addListener(this@OptionsBox)
    }
    var subTopicCountBoxHolder = hbox {
        label("max. #Unteraufgabenbereiche") {
            paddingAll = 5.0
        }
        spacer { }
        add(subTopicCountBox)
        visibleProperty().set(false)
    }

    override val root = vbox {
        addClass(Styles.boxRoot)
        label("Optionen") {
            addClass(Styles.heading)
        }
        gridpane {
            addClass(Styles.boxHolder)
            vboxConstraints { vgrow = Priority.ALWAYS }
            vbox {
                add(box1)
                //add(box2)
                add(box3)
                hbox {
                    label("max. #Unteraufgaben") {
                        paddingAll = 5.0
                    }
                    spacer { }
                    add(subTopicExerciseCountBox)
                }
                add(subTopicCountBoxHolder)
            }
        }
    }

    @ExperimentalUnsignedTypes
    override fun changed(observable: ObservableValue<out Any>?, oldValue: Any?, newValue: Any?) {
        onOptionsChanged()
    }

    @ExperimentalUnsignedTypes
    private fun onOptionsChanged() {
        subTopicCountBoxHolder.isVisible = box3.isChecked()
        val text1 = subTopicExerciseCountBox.text
        val text2 = subTopicCountBox.text
        val subTopicExerciseCount = if (text1.toUIntOrNull() == null) Int.MAX_VALUE else text1.toInt()
        val subTopicCount = if (text2.toUIntOrNull() == null || !box3.isChecked()) -1 else text2.toInt()

        optionsObserver.onOptionschanged(Options(
                box1.isChecked(),
                box2.isChecked(),
                box3.isChecked(),
                subTopicExerciseCount,
                subTopicCount))
    }
}

