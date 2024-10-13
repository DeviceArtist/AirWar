package org.gongclouds.airwar

import android.media.Image
import androidx.compose.ui.graphics.ImageBitmap

open class BaseSprite {
    var screenWidth = 0
    var screenHeight = 0
    private var attack = 10
    private var blood = 100
    private var maxBlood = 100
    private var scal = 1
    var tox = 0
    var toy = 0
    var x = 0
    var y = 0
    var w = 0
    var h = 0
    var speed: Int = 2
    private var die: Boolean = false
    private lateinit var img: ImageBitmap
    open var canOutOfScreen: Boolean = true

    open fun reset() {
        this.blood = 100
    }

    open fun stop() {
        this.tox = 0
        this.toy = 0
    }

    open fun loadImg(img: ImageBitmap) {
        this.img = img
        this.w = (img.width.toInt() * this.scal).toInt()
        this.h = (img.height.toInt() * this.scal).toInt()
    }

    open fun getImg(): ImageBitmap? {
        return this.img
    }

    open fun setScreenSize(screenWidth: Int, screenHeight: Int) {
        this.screenWidth = screenWidth
        this.screenHeight = screenHeight
    }

    open fun inActive(): Boolean {
        return !this.die
    }

    open fun setActive(flag: Boolean) {
        this.die = !flag
    }

    open fun setAttack(attack: Int) {
        this.attack = attack
    }

    open fun getAttack(): Int {
        return this.attack
    }

    open fun setBlood(blood: Int) {
        this.blood = blood
    }

    open fun getBlood(): Int {
        return this.blood
    }

    open fun setMaxBlood(blood: Int) {
        this.maxBlood = blood
    }

    open fun getMaxBlood(): Int {
        return this.maxBlood
    }

    open fun onUnderAttack(attack: Int) {
        this.blood -= attack
        if (this.blood <= 0) {
            this.blood = 0
            this.setActive(false)
        }
    }

    fun preUpdate() {
        if (this.tox != 0) {
            this.x += this.tox * this.speed
        }
        if (this.toy != 0) {
            this.y += this.toy * this.speed
        }


        if (!this.canOutOfScreen) {
            if (this.x < 0) {
                this.x = 0
            }
            if (this.x > this.screenWidth - this.w) {
                this.x = this.screenWidth - this.w
                this.tox = 0
            }
            if (this.y < 0) {
                this.y = 0
            }
            if (this.y > this.screenHeight - this.h) {
                this.y = this.screenHeight - this.h
                this.toy = 0
            }
        }
    }

    open fun update() {


    }
}

class PlayerSprite : BaseSprite() {
    init {
        this.speed = 8
        this.canOutOfScreen = false
        this.setBlood(100)
        this.setMaxBlood(100)
        this.setAttack(2)
    }

    override fun reset() {
        this.setBlood(100)
        this.setMaxBlood(100)
        this.setActive(true)
    }

    override fun update() {
        this.preUpdate()
    }
}

class EnemySprite : BaseSprite() {
    init {
        this.speed = 2
        this.toy = 1
        this.setAttack(10)
        this.setBlood(1)
        this.setMaxBlood(1)
    }

    override fun update() {
        this.preUpdate()
        if (this.y > this.screenHeight) {
            this.setActive(false)
        }
    }
}

class BulletSprite : BaseSprite() {
    init {
        this.toy = -1
        this.speed = 20
        this.setAttack(1)
        this.setBlood(1)
        this.setMaxBlood(1)
    }

    override fun update() {
        this.preUpdate()
        if (this.y < this.h) {
            this.setActive(false)
        }
    }
}

