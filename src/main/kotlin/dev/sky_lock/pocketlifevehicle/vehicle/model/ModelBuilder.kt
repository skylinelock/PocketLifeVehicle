package dev.sky_lock.pocketlifevehicle.vehicle.model

/**
 * @author sky_lock
 */
class ModelBuilder(private val id: String) {
    private var name: String? = null
    private var lore: List<String>? = null
    private var spec: Spec? = null
    private var itemOption: ItemOption? = null
    private var isBig = false
    private var collideBox: CollideBox? = null
    private var height = 0f
    private var sound: Sound? = null
    fun name(name: String): ModelBuilder {
        this.name = name
        return this
    }

    fun lore(lore: List<String>): ModelBuilder {
        this.lore = lore
        return this
    }

    fun spec(spec: Spec): ModelBuilder {
        this.spec = spec
        return this
    }

    fun item(itemOption: ItemOption): ModelBuilder {
        this.itemOption = itemOption
        return this
    }

    fun big(isBig: Boolean): ModelBuilder {
        this.isBig = isBig
        return this
    }

    fun collideBox(baseSide: Float, height: Float): ModelBuilder {
        collideBox = CollideBox(baseSide, height)
        return this
    }

    fun height(height: Float): ModelBuilder {
        this.height = height
        return this
    }

    fun sound(sound: Sound): ModelBuilder {
        this.sound = sound
        return this
    }

    fun build(): Model {
        if (name == null || spec == null || itemOption == null || collideBox == null || sound == null) {
            throw IllegalStateException()
        }
        return Model(id, name!!, lore!!, spec!!, itemOption!!, collideBox!!, isBig, height, sound!!)
    }

    companion object {
        fun of(id: String): ModelBuilder {
            return ModelBuilder(id)
        }
    }

}