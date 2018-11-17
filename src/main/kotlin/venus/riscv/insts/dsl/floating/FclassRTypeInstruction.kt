package venus.riscv.insts.dsl.floating

import venus.riscv.insts.dsl.Instruction
import venus.riscv.insts.dsl.disasms.extensions.FRRTypeDisassembler
import venus.riscv.insts.dsl.formats.base.RTypeFormat
import venus.riscv.insts.dsl.impls.NoImplementation
import venus.riscv.insts.dsl.impls.extensions.FFRRTypeImplementation32
import venus.riscv.insts.dsl.parsers.extensions.FRRTypeParser
import venus.riscv.insts.floating.Decimal

class FclassRTypeInstruction(
    name: String,
    opcode: Int,
    funct3: Int,
    funct7: Int,
        // eval16: (Short, Short) -> Short = { _, _ -> throw NotImplementedError("no rv16") },
    eval32: (Decimal, Decimal) -> Int // ,
        // eval64: (Long, Long) -> Long = { _, _ -> throw NotImplementedError("no rv64") },
        // eval128: (Long, Long) -> Long = { _, _ -> throw NotImplementedError("no rv128") }
) : Instruction(
        name = name,
        format = RTypeFormat(opcode, funct3, funct7),
        parser = FRRTypeParser,
        impl16 = NoImplementation,
        impl32 = FFRRTypeImplementation32(eval32),
        impl64 = NoImplementation,
        impl128 = NoImplementation,
        disasm = FRRTypeDisassembler
)