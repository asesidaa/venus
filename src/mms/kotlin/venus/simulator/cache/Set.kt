package venus.simulator.cache

import kotlin.js.Math

/**
 * Created by thaum on 7/29/2018.
 */
class Set(internal var associativity: Int, internal var blocksize: Int) {
    internal var useCounter: Int = 0
    internal var blocks: Array<Block>

    init {
        useCounter = 0
        val bs = arrayOfNulls<Block>(associativity)
        for (i in bs.indices) {
            bs[i] = Block(blocksize)
        }
        /*This is a crapy workaround to the no null type issue.*/
        blocks = bs.filterNotNull().toTypedArray()
    }

    fun copy(): Set {
        var s = Set(associativity, blocksize)
        s.useCounter = this.useCounter
        for (i in blocks.indices) {
            s.blocks[i] = this.blocks[i].copy()
        }
        return s
    }

    // returns the relevant block, or null if not found
    fun read(tag: Int, offset: Int, bs: BlockState) {
        val theBlock = findBlock(tag)
        theBlock?.blockState = bs
        theBlock?.read(offset, ++useCounter)
    }

    fun write(tag: Int, offset: Int, /*data: Int, */bs: BlockState) {
        val theBlock = findBlock(tag)
        if (theBlock != null) {
            theBlock.write(offset, /*data,*/ ++useCounter)
            theBlock.blockState = bs
        } else {
            // this shouldn't happen ...
            // throw CacheError("Could not find block with tag '" + tag.toString() + "'! This error should not have occurred since it should have been handled earlier in the code.")
        }
    }

    fun getLRU(): Block {
        var lru = blocks[0]
        var least = blocks[0].recentUse
        var temp: Int
        for (i in 1 until associativity) {
            temp = blocks[i].recentUse
            if (temp < least) {
                lru = blocks[i]
                least = temp
            }
        }
        return lru
    }

    fun getRandom(): Block {
        val index = getRandomInt(0, associativity - 1)
        return blocks[index]
    }

    fun getRandomInt(min: Int, max: Int): Int {
        val min = Math.ceil(min)
        val max = Math.floor(max) + 1
        return Math.min(Math.floor(Math.random() * (max - min)) + min)
    }

    // returns the Block if found, null otherwise
    fun findBlock(tag: Int): Block? {
        val result: Block? = null
        for (i in blocks.indices) {
            if (blocks[i].tag == tag && blocks[i].isValid)
                return blocks[i]
        }
        return result
    }

    fun blockStates(a: ArrayList<String>) {
        for (b in blocks) {
            a.add(b.blockState.toString())
        }
    }
}