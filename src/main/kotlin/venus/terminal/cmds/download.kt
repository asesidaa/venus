package venus.terminal.cmds

import venus.terminal.Command
import venus.terminal.Terminal
import venus.vfs.VFSDummy
import venus.vfs.VFSFile
import venus.vfs.VFSType

var download = Command(
        name = "download",
        execute = fun(args: MutableList<String>, t: Terminal, sudo: Boolean): String {
            val s: StringBuilder = StringBuilder()
            for (fname: String in args) {
                val f = t.vfs.getObjectFromPath(fname) ?: VFSDummy()
                if (f.type == VFSType.File) {
                    downloadFile(fname, (f as VFSFile).readText())
                } else {
                    s.append("'$fname' is not a path to a file!")
                }
            }
            return s.toString()
        },
        tab = fun(args: MutableList<String>, t: Terminal, sudo: Boolean): ArrayList<Any> {
            if (args.size == 1) {
                val prefix = args[args.size - 1]
                return arrayListOf(prefix, t.vfs.filesFromPrefix(prefix))
            }
            return arrayListOf("", ArrayList<String>())
        }
)

fun downloadFile(filename: String, text: String) {
    js("""
        var element = document.createElement('a');
        element.setAttribute('href', 'data:text/plain;charset=utf-8,' + encodeURIComponent(text));
        element.setAttribute('download', filename);
        
        element.style.display = 'none';
        document.body.appendChild(element);
        
        element.click();
        
        document.body.removeChild(element);
    """)
}