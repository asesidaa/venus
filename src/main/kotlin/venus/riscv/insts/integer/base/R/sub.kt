package venus.riscv.insts.integer.base.r

import venus.riscv.insts.dsl.RTypeInstruction

val sub = RTypeInstruction(
        name = "sub",
        opcode = 0b0110011,
        funct3 = 0b000,
        funct7 = 0b0100000,
        eval16 = { a, b -> (a - b).toShort() },
        eval32 = { a, b -> a - b },
        eval64 = { a, b -> a - b },
        eval128 = { a, b -> a - b }
)
