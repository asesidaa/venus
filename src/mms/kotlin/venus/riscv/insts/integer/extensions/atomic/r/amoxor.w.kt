package venus.riscv.insts.integer.extensions.atomic.r

import venus.riscv.insts.dsl.AMORTypeInstruction

val amoxorw = AMORTypeInstruction(
        name = "amoxor.w",
        opcode = 0b0101111,
        funct3 = 0b010,
        funct5 = 0b00100,
        rl = 0b0,
        aq = 0b0,
        // eval16 = { a, b -> (a + b).toShort() },
        eval32 = { data, vrs2 -> data xor vrs2 }
        // eval64 = { a, b -> a + b },
        // eval128 = { a, b -> a + b }
)

val amoxorwaq = AMORTypeInstruction(
        name = "amoxor.w.aq",
        opcode = 0b0101111,
        funct3 = 0b010,
        funct5 = 0b00100,
        rl = 0b0,
        aq = 0b1,
        // eval16 = { a, b -> (a + b).toShort() },
        eval32 = { data, vrs2 -> data xor vrs2 }
        // eval64 = { a, b -> a + b },
        // eval128 = { a, b -> a + b }
)

val amoxorwrl = AMORTypeInstruction(
        name = "amoxor.w.rl",
        opcode = 0b0101111,
        funct3 = 0b010,
        funct5 = 0b00100,
        rl = 0b1,
        aq = 0b0,
        // eval16 = { a, b -> (a + b).toShort() },
        eval32 = { data, vrs2 -> data xor vrs2 }
        // eval64 = { a, b -> a + b },
        // eval128 = { a, b -> a + b }
)

val amoxorwaqrl = AMORTypeInstruction(
        name = "amoxor.w.aq.rl",
        opcode = 0b0101111,
        funct3 = 0b010,
        funct5 = 0b00100,
        rl = 0b1,
        aq = 0b1,
        // eval16 = { a, b -> (a + b).toShort() },
        eval32 = { data, vrs2 -> data xor vrs2 }
        // eval64 = { a, b -> a + b },
        // eval128 = { a, b -> a + b }
)

val amoxorwrlaq = AMORTypeInstruction(
        name = "amoxor.w.rl.aq",
        opcode = 0b0101111,
        funct3 = 0b010,
        funct5 = 0b00100,
        rl = 0b1,
        aq = 0b1,
        // eval16 = { a, b -> (a + b).toShort() },
        eval32 = { data, vrs2 -> data xor vrs2 }
        // eval64 = { a, b -> a + b },
        // eval128 = { a, b -> a + b }
)