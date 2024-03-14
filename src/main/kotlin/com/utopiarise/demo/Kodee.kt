package com.utopiarise.demo

import godot.Area2D
import godot.Input
import godot.Node2D
import godot.annotation.*
import godot.core.Vector2
import godot.core.asStringName

@RegisterClass
class Kodee : Area2D() {

    @Export
    @RegisterProperty
    @DoubleRange(min = 0.0, max = 500.0)
    var speed: Double = 200.0

    private lateinit var screenSize: Vector2

    @RegisterFunction
    override fun _ready() {
        screenSize = getViewportRect().size
        bodyEntered.connect(this, Kodee::onBodyEntered, 0)
    }

    @RegisterFunction
    override fun _process(delta: Double) {
        super._process(delta)

        var velocity: Vector2 = when {
            Input.isActionPressed("left".asStringName()) -> {
                Vector2.LEFT
            }

            Input.isActionPressed("right".asStringName()) -> {
                Vector2.RIGHT
            }

            else -> {
                Vector2.ZERO
            }
        }


        when (velocity) {
            Vector2.LEFT, Vector2.RIGHT -> {
                val newTransform = transform
                newTransform.x = -velocity
                transform = newTransform
            }
        }

        if (velocity.length() > 0) {
            velocity = velocity.normalized() * speed
        }

        position += velocity * delta

        when {
            position < Vector2.LEFT -> {
                position = Vector2(screenSize.x, position.y)
            }
            position > screenSize -> {
                position = Vector2(1.0, position.y)
            }
        }
    }

    @RegisterFunction
    fun onBodyEntered(node: Node2D) = when (node) {
        is TrashBag -> {
            node.queueFree()
        }
        else -> Unit
    }
}
