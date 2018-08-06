package venus.riscv.insts.dsl

import venus.riscv.insts.dsl.disasms.extensions.AMORTypeDisassembler
import venus.riscv.insts.dsl.formats.extensions.AMORTypeFormat
import venus.riscv.insts.dsl.impls.extensions.AMORTypeImplementation32
import venus.riscv.insts.dsl.impls.NoImplementation
import venus.riscv.insts.dsl.parsers.extensions.AMORTypeParser

class AMORTypeInstruction(
        name: String,
        opcode: Int,
        funct3: Int,
        aq: Int,
        rl: Int,
        funct5: Int,
        eval16: (Short, Short) -> Short = { _, _ -> throw NotImplementedError("no rv16") },
        eval32: (Int, Int) -> Int,
        eval64: (Long, Long) -> Long = { _, _ -> throw NotImplementedError("no rv64") },
        eval128: (Long, Long) -> Long = { _, _ -> throw NotImplementedError("no rv128") }
) : Instruction(
        name = name,
        format = AMORTypeFormat(opcode, funct3, funct5, aq, rl),
        parser = AMORTypeParser,
        impl16 = NoImplementation,
        impl32 = AMORTypeImplementation32(eval32),
        impl64 = NoImplementation,
        impl128 = NoImplementation,
        disasm = AMORTypeDisassembler
)
