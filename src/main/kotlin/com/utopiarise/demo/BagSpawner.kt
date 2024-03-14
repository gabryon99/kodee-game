package com.utopiarise.demo

import godot.Node2D
import godot.PackedScene
import godot.Timer
import godot.annotation.*
import godot.core.Vector2
import godot.extensions.getNodeAs
import godot.extensions.instanceAs
import godot.global.GD

@RegisterClass
class BagSpawner : Node2D() {

    @RegisterProperty
    lateinit var timer: Timer

    @RegisterProperty
    lateinit var bagScene: PackedScene

    @RegisterFunction
    override fun _ready() {
        super._ready()
        timer = getNodeAs("Timer")!!
        bagScene = GD.load("res://scenes/bag.tscn")!!
        timer.timeout.connect(this, BagSpawner::onTimeout, 0)
    }

    @RegisterFunction
    fun onTimeout() {

        val newBag = bagScene.instanceAs<TrashBag>()!!
        addChild(newBag)

        val viewportSize = getViewportRect().size
        val minX = 0.0
        val maxX = viewportSize.x
        val posX = GD.randdRange(minX, maxX)

        newBag.position = Vector2(posX, 0)
        newBag.gravityScale = GD.randfRange(10.0f, 20.0f)
        newBag.blockSize = GD.randfRange(1.0f, 5.0f)
    }
}