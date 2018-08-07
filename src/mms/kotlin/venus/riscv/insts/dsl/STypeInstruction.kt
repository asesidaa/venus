package venus.riscv.insts.dsl

import venus.riscv.insts.dsl.disasms.base.STypeDisassembler
import venus.riscv.insts.dsl.formats.base.STypeFormat
import venus.riscv.insts.dsl.impls.NoImplementation
import venus.riscv.insts.dsl.impls.base.STypeImplementation32
import venus.riscv.insts.dsl.parsers.base.STypeParser
import venus.simulator.Simulator

class STypeInstruction(
    name: String,
    opcode: Int,
    funct3: Int,
    store16: (Simulator, Short, Short) -> Unit = { _, _, _ -> throw NotImplementedError("no rv16") },
    store32: (Simulator, Int, Int) -> Unit,
    store64: (Simulator, Long, Long) -> Unit = { _, _, _ -> throw NotImplementedError("no rv64") },
    store128: (Simulator, Long, Long) -> Unit = { _, _, _ -> throw NotImplementedError("no rv128") }
) : Instruction(
        name = name,
        format = STypeFormat(opcode, funct3),
        parser = STypeParser,
        impl16 = NoImplementation,
        impl32 = STypeImplementation32(store32),
        impl64 = NoImplementation,
        impl128 = NoImplementation,
        disasm = STypeDisassembler
)