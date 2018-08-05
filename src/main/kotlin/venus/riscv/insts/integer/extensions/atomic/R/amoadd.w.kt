package venus.riscv.insts.integer.extensions.atomic.r

import venus.riscv.insts.dsl.AMORTypeInstruction

val amoaddw = AMORTypeInstruction(
        name = "amoadd.w",
        opcode = 0b0101111,
        funct3 = 0b010,
        funct5 = 0b00000,
        //eval16 = { a, b -> (a + b).toShort() },
        eval32 = { data, vrs2 -> data + vrs2 }
        //eval64 = { a, b -> a + b },
        //eval128 = { a, b -> a + b }
)