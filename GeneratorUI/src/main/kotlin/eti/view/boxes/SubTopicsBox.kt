package eti.view.boxes

import eti.data.SubTopic
import eti.data.Topic
import eti.data.TopicSelectorListener
import javafx.beans.value.ObservableValue
import javafx.geometry.Insets
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.text.Font
import tornadofx.*

class SubTopicsBox : View(), TopicSelectorListener {
    val selected = mutableMapOf<Topic, MutableList<SubTopic>>()
    val display =
            vbox {
                background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
                border = Border(BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(0.0)))
            }

    override val root = vbox {
        label("Unteraufgabenbereich") {
            vboxConstraints { margin = Insets(10.0) }
        }
        gridpane {
            vboxConstraints {
                marginLeft = 20.0
                vgrow = Priority.ALWAYS
            }
            scrollpane {
                add(display)
                gridpaneConstraints {
                    hgrow = Priority.ALWAYS
                    vgrow = Priority.ALWAYS
                }
                minHeight = 0.0
                minWidth = 230.0
                isFitToWidth = true
                hbarPolicy = ScrollPane.ScrollBarPolicy.NEVER
                style = "-fx-background-color:transparent;"
            }
            background = Background(BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))
            border = Border(BorderStroke(Color(0.78431372549, 0.78431372549, 0.78431372549, 1.0), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths(2.0)))
            minWidth = 230.0
        }
    }

    fun showTopic(topic: Topic) {
        showSubTopics(topic)
    }

    fun showSubTopics(topic: Topic) {
        if (selected.keys.contains(topic)) return

        val selectedSubTopics = selected[topic]
        display.add(label(topic.name) { font = Font("Arial", 14.5); paddingBottom = 5; paddingTop = 5 })
        for (subTopic in topic.SubTopics) {
            display.add(checkbox(subTopic.nameWithoutExtension) {
                paddingAll = 5
                selectedProperty().set(selectedSubTopics != null && selectedSubTopics.contains(subTopic) || selectedSubTopics == null)
                selectedProperty().addListener(
                        ChangeListener<Boolean> { observableValue: ObservableValue<out Boolean>?, oldValue: Boolean, newValue: Boolean ->
                            if (!newValue && selected[topic] != null && selected[topic]!!.contains(subTopic)) selected[topic]?.remove(subTopic)
                            else if (newValue) {
                                selected.getOrPut(topic, defaultValue = { mutableListOf(subTopic) })
                            }
                        })
            })
            if (selectedSubTopics == null) selected.getOrPut(topic, defaultValue = { mutableListOf(subTopic) })
        }
    }

    fun removeSubTopic(topic: Topic) {
        var remove = false
        var i = 0
        while (i < display.children.size) {
            val child = display.children[i++]
            if (child is Label)
                remove = child.text == topic.name
            if (remove) {
                display.children.remove(child)
                i--
            }
        }
        selected.remove(topic)
    }

    fun getSelectedMap(): Map<Topic, List<SubTopic>> {
        return selected
    }

    override fun onTopicChanged(topic: Topic, checked: Boolean) {
        if (checked) showSubTopics(topic)
        else removeSubTopic(topic)
    }
}