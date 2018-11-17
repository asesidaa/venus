package venus.riscv.insts.integer.base.r

import venus.riscv.insts.InstructionNotSupportedError
import venus.riscv.insts.dsl.RTypeInstruction

val sraw = RTypeInstruction(
        name = "sraw",
        opcode = 0b0111011,
        funct3 = 0b101,
        funct7 = 0b0100000,
        eval32 = { a, b ->
            throw InstructionNotSupportedError("addiw is not supported on 32 bit systems!")
        }
)