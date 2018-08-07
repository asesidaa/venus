package venus.riscv.insts.dsl

import venus.riscv.insts.dsl.disasms.base.BTypeDisassembler
import venus.riscv.insts.dsl.formats.base.BTypeFormat
import venus.riscv.insts.dsl.impls.base.BTypeImplementation32
import venus.riscv.insts.dsl.impls.NoImplementation
import venus.riscv.insts.dsl.parsers.base.BTypeParser

class BTypeInstruction(
    name: String,
    opcode: Int,
    funct3: Int,
    cond16: (Short, Short) -> Boolean = { _, _ -> throw NotImplementedError("no rv64") },
    cond32: (Int, Int) -> Boolean,
    cond64: (Long, Long) -> Boolean = { _, _ -> throw NotImplementedError("no rv64") },
    cond128: (Long, Long) -> Boolean = { _, _ -> throw NotImplementedError("no rv128") }
) : Instruction(
        name = name,
        format = BTypeFormat(opcode, funct3),
        parser = BTypeParser,
        impl16 = NoImplementation,
        impl32 = BTypeImplementation32(cond32),
        impl64 = NoImplementation,
        impl128 = NoImplementation,
        disasm = BTypeDisassembler
)