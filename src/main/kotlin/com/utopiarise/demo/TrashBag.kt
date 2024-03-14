package com.utopiarise.demo

import godot.*
import godot.annotation.Export
import godot.annotation.RegisterClass
import godot.annotation.RegisterFunction
import godot.annotation.RegisterProperty
import godot.core.Vector2
import godot.extensions.getNodeAs

@RegisterClass
class TrashBag : RigidBody2D() {

    val size: Vector2 by lazy {
        sprite2D.texture!!.getSize()
    }

    @Export
    @RegisterProperty
    var blockSize: Float = 1.0f
        set(value) {
            field = value
            label.text = "%.2f KB".format(value)
        }

    @RegisterProperty
    lateinit var sprite2D: Sprite2D

    @RegisterProperty
    lateinit var screenNotifier2D: VisibleOnScreenNotifier2D

    @Export
    @RegisterProperty
    lateinit var garbageCollector: GarbageCollector

    @Export
    @RegisterProperty
    lateinit var label: Label

    @RegisterFunction
    override fun _ready() {
        super._ready()
        garbageCollector = getNodeAs("../../GarbageCollector")!!
        sprite2D = getNodeAs("Sprite2D")!!
        screenNotifier2D = getNodeAs("ScreenNotifier2D")!!
        label = getNodeAs("Label")!!
        screenNotifier2D.screenExited.connect(this, TrashBag::onScreenExit, 0)
    }

    @RegisterFunction
    override fun _physicsProcess(delta: Double) {
        super._physicsProcess(delta)
        linearVelocity = Vector2.DOWN * delta
    }

    @RegisterFunction
    fun onScreenExit() {
        garbageCollector.leakMemory(this)
        queueFree()
    }

}