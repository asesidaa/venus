package venusbackend.simulator

import kotlin.test.Test
import venusbackend.assembler.Assembler
import venus.vfs.VirtualFileSystem
import venusbackend.linker.Linker
import venusbackend.linker.ProgramAndLibraries
import kotlin.test.assertEquals

class TracerTest {

    fun trace(tr: Tracer): String {
        tr.traceStart()
        while (!tr.sim.isDone()) {
            tr.traceStep()
        }
        tr.traceEnd()
        tr.traceStringStart()
        while (tr.traceStringStep()) {}
        tr.traceStringEnd()
        return tr.getString()
    }

    fun makeSim(text: String, simSettings: SimulatorSettings = SimulatorSettings()): Simulator {
        val (prog, _) = Assembler.assemble(text)
        val PandL = ProgramAndLibraries(listOf(prog), VirtualFileSystem("dummy"))
        val linked = Linker.link(PandL)
        return Simulator(linked, settings = simSettings)
    }

    @Test
    fun addiJalrInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
        main:
            addi t0 x0 1
            addi t1 x0 16
            jalr t0 t1 0
        func1:
            addi s0 x0 0x01
        func2:
            addi s1 x0 0x02
        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        val traceString = trace(t)
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0001 0000 0000 0010 1001 0011	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0001 0000 0000 0000 0011 0001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0000 0000 0011 0000 0010 1110 0111	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0000 0000 0000 0000 0000 0001 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0000 0001 0000 0000 0100 0001 0011	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0000 0001 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0010 0000 0000 0100 1001 0011	0000 0000 0000 0100
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0000 0001 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0101
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0000 0001 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0110
""", traceString)
    }

    @Test
    fun addluisllInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
        addi s0 s0 1
        addi s1 x0 14
        sll s0 s0 s1
        add s1 s0 x0
        add t0 s1 s0
        lui ra 0xfffff
        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        val traceString = trace(t)
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0001 0100 0000 0100 0001 0011	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0000 1110 0000 0000 0100 1001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0000 1001 0100 0001 0100 0011 0011	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0000 0000 0000 0000 0000 0000 1110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0000 0000 0100 0000 0100 1011 0011	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 1000 0100 1000 0010 1011 0011	0000 0000 0000 0100
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	1111 1111 1111 1111 1111 0000 1011 0111	0000 0000 0000 0101
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 1000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0110
1111 1111 1111 1111 1111 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 1000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0100 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0111	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0111
""", traceString)
    }

    @Test
    fun addiInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
        addi t0, x0, 5
        addi t1, t0, 7
        addi s0, t0, 9
        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        t.totCommands = 6
        val traceString = trace(t)
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0101 0000 0000 0010 1001 0011	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0000 0000 0111 0010 1000 0011 0001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0000 1001 0010 1000 0100 0001 0011	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0100
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0101
""", traceString)
    }

    @Test
    fun brjalrInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
        add s0 x0 x0
        addi a0 x0 -1
        bne s0 s0 never_reach
        addi s0 s0 -1
        lui s1 0 #end
        addi s1 s1 36 #end
        jr s1
        never_reach:
          addi s0, s0, 1
          j end
        end:
          addi a0 a0 1

        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        val traceString = trace(t)
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0100 0011 0011	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	1111 1111 1111 0000 0000 0101 0001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0000 1000 0100 0001 1010 0110 0011	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0011	1111 1111 1111 0100 0000 0100 0001 0011	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0100 1011 0111	0000 0000 0000 0100
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0101	0000 0010 0100 0100 1000 0100 1001 0011	0000 0000 0000 0101
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0110	0000 0000 0000 0100 1000 0000 0110 0111	0000 0000 0000 0110
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0010 0100	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0111	0000 0000 0001 0100 0000 0100 0001 0011	0000 0000 0000 0111
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0010 0100	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 1001	0000 0000 0001 0101 0000 0101 0001 0011	0000 0000 0000 1000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0010 0100	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 1001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0010 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1011	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 1010
""", traceString)
    }

    @Test
    fun branchesInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
            beq s0 s0 start #0
        bad-loop:
            addi sp sp -1 #1
            beq x0 x0 bad-loop #2

        start:
            addi s1 s1 10#3
            blt s0 s1 label1#4
            beq x0 x0 bad-loop#5

        label2:
            addi s1 s1 -20#6
            bltu s0 s1 end#7
            beq x0 x0 bad-loop#8

        label1:
            addi s0 s0 20#9
            blt s1 s0 label2#10
            beq x0 x0 bad-loop#11

        end:
            add a0 x0 x0#12

        #0,3,4,9,10,6,7,12
        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        val traceString = trace(t)
        return
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 1000 0100 0000 0110 0110 0011	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	1111 1111 1111 0001 0000 0001 0001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0000 1010 0100 1000 0100 1001 0011	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 1001 0100 0100 1010 0110 0011	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	1111 1110 0000 0000 0000 1000 1110 0011	0000 0000 0000 0100
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1001	0000 0001 0100 0100 0000 0100 0001 0011	0000 0000 0000 0101
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1010	1111 1110 1000 0100 1100 1000 1110 0011	0000 0000 0000 0110
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1011	1111 1100 0000 0000 0000 1100 1110 0011	0000 0000 0000 0111
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0110	1111 1110 1100 0100 1000 0100 1001 0011	0000 0000 0000 1000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	0000 0000 0000 0000 0000 0000 0000 1010	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0111	0000 0000 1001 0100 0110 1010 0110 0011	0000 0000 0000 1001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	1111 1111 1111 1111 1111 1111 1111 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1000	1111 1110 0000 0000 0000 0010 1110 0011	0000 0000 0000 1010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	1111 1111 1111 1111 1111 1111 1111 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1100	0000 0000 0000 0000 0000 0101 0011 0011	0000 0000 0000 1011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	1111 1111 1111 1111 1111 1111 1111 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1101	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 1100
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0001 0100	1111 1111 1111 1111 1111 1111 1111 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 1110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 1101
""", traceString)
    }

    @Test
    fun jumpInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
        jal ra label
        addi s0 x0 -1
        jal x0 end
        label: jalr x0 ra 0
        end: addi a0 x0 -1

        #0,3,1,2,4
        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        val traceString = trace(t)
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 1100 0000 0000 0000 1110 1111	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	1111 1111 1111 0000 0000 0100 0001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0000 0000 0000 1000 0000 0110 0111	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	1111 1111 1111 0000 0000 0101 0001 0011	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	1111 1111 1111 0000 0000 0100 0001 0011	0000 0000 0000 0100
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0000 1000 0000 0000 0000 0110 1111	0000 0000 0000 0101
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0000 0000 0000 1000 0000 0110 0111	0000 0000 0000 0110
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	1111 1111 1111 0000 0000 0101 0001 0011	0000 0000 0000 0111
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 1000
0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0000	1111 1111 1111 1111 1111 1111 1111 1111	0000 0000 0000 0000 0000 0000 0000 0110	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 1001
""", traceString)
    }

    @Test
    fun memInstFirstTwoStageTest() {
        val ss = SimulatorSettings()
        ss.setRegesOnInit = false
        val sim = makeSim("""
        lui s0 74565
        addi s0 s0 1656
        sw s0 40(x0)
        lw ra 40(x0)
        """, ss)
        val t = Tracer(sim)
        t.format = "%1%\\t%2%\\t%5%\\t%6%\\t%7%\\t%8%\\t%9%\\t%10%\\t%pc%\\t%inst%\\t%line%\\n"
        t.instFirst = true
        wordAddressed = true
        t.twoStage = true
        val traceString = trace(t)
        assertEquals("""0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0001 0010 0011 0100 0101 0100 0011 0111	0000 0000 0000 0000
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0001	0110 0111 1000 0100 0000 0100 0001 0011	0000 0000 0000 0001
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0001 0010 0011 0100 0101 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0010	0000 0010 1000 0000 0010 0100 0010 0011	0000 0000 0000 0010
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0001 0010 0011 0100 0101 0110 0111 1000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0011	0000 0010 1000 0000 0010 0000 1000 0011	0000 0000 0000 0011
0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0001 0010 0011 0100 0101 0110 0111 1000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0100	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0100
0001 0010 0011 0100 0101 0110 0111 1000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0001 0010 0011 0100 0101 0110 0111 1000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0000 0000 0000 0000 0101	0000 0000 0000 0000 0000 0000 0000 0000	0000 0000 0000 0101
""", traceString)
    }
}