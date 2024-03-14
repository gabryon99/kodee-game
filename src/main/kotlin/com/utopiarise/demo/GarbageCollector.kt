package com.utopiarise.demo

import godot.Label
import godot.Node2D
import godot.PackedScene
import godot.annotation.*
import godot.core.asNodePath
import godot.extensions.getNodeAs
import godot.global.GD

@RegisterClass
class GarbageCollector : Node2D() {

    @Export
    @RegisterProperty
    var memoryInUse = 0.0f

    @Export
    @RegisterProperty
    @FloatRange(min = 1.0f, max = 128.0f)
    var maximumMemory = 128.0f

    @Export
    @RegisterProperty
    lateinit var heapUsageLabel: Label

    private var runOutOfMemory = false

    @RegisterFunction
    override fun _ready() {
        super._ready()
        heapUsageLabel = getNodeAs("CanvasLayer/Control/HUD/HeapUsageLabel")!!
    }

    @RegisterFunction
    override fun _process(delta: Double) {
        super._process(delta)
        heapUsageLabel.text = "Heap Usage: ${"%.2f".format(memoryInUse)} KB / $maximumMemory KB"
    }

    @RegisterFunction
    fun freeMemory(trashBag: TrashBag) {
        memoryInUse = GD.clamp(memoryInUse - trashBag.blockSize, 0.0f, memoryInUse)
    }

    @RegisterFunction
    fun leakMemory(trashBag: TrashBag) {
        memoryInUse += trashBag.blockSize

        if (memoryInUse > maximumMemory && !runOutOfMemory) {
            val gameOverScene: PackedScene = GD.load("res://scenes/game_over.tscn")!!
            val sceneNode = gameOverScene.instantiate()!!
            getNode("/root".asNodePath())!!.addChild(sceneNode)
            runOutOfMemory = true
        }
    }
}