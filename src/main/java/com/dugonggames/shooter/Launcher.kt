package com.dugonggames.shooter

import com.dugonggames.shooter.graphics.GfxWrapper
import com.dugonggames.shooter.shooter.ShooterSim
import com.dugonggames.shooter.shooter.ShooterState
import javafx.animation.AnimationTimer
import javafx.application.Application
import javafx.scene.Group
import javafx.scene.Scene
import javafx.scene.canvas.Canvas
import javafx.scene.canvas.GraphicsContext
import javafx.scene.input.KeyEvent
import javafx.scene.input.MouseEvent
import javafx.stage.Stage

import java.util.ArrayList

class Launcher : Application() {
    internal var lastNanoTime: Long = 0
    internal var keyPressed = ArrayList<KeyEvent>()
    internal var mouseClicked = ArrayList<MouseEvent>()
    private val CANVAS_WIDTH = 1500
    private val CANVAS_HEIGHT = 1000

    override fun start(theStage: Stage) {
        theStage.title = "shooted"

        val root = Group()
        val scene = Scene(root)
        theStage.scene = scene

        val canvas = Canvas(CANVAS_WIDTH.toDouble(), CANVAS_HEIGHT.toDouble())
        root.children.add(canvas)

        val gc = canvas.graphicsContext2D
        val gfx = GfxWrapper(gc, CANVAS_WIDTH.toDouble(), CANVAS_HEIGHT.toDouble())

        val sim = ShooterSim()

        lastNanoTime = System.nanoTime()
        var state = sim.init(CANVAS_WIDTH, CANVAS_HEIGHT)

        scene.setOnKeyPressed { e -> keyPressed.add(e) }
        scene.setOnKeyReleased { e -> keyPressed.add(e) }
        scene.setOnMousePressed { e -> mouseClicked.add(e) }
        scene.setOnMouseReleased { e -> mouseClicked.add(e) }

        object : AnimationTimer() {
            override fun handle(currentNanoTime: Long) {
                val actualDT = currentNanoTime - lastNanoTime
                lastNanoTime = currentNanoTime

                sim.stepForward(state, actualDT / 1000000000.0, keyPressed, mouseClicked, CANVAS_WIDTH, CANVAS_HEIGHT)
                keyPressed = ArrayList()
                mouseClicked = ArrayList()

                // draw
                sim.draw(state, gfx, CANVAS_WIDTH.toDouble(), CANVAS_HEIGHT.toDouble())
            }
        }.start()

        theStage.show()
    }

    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            launch(Launcher::class.java, *args)
        }
    }
}