package venus.terminal.cmds

import venus.Driver
import venus.terminal.Command
import venus.terminal.Terminal
import venus.vfs.VFSFile
import venus.vfs.VFSType

var edit = Command(
        name = "edit",
        execute = fun(args: MutableList<String>, t: Terminal, sudo: Boolean): String {
            if (args.size != 1) {
                return "edit: Takes in one argument [filename]"
            }
            val obj = t.vfs.getObjectFromPath(args[0])
            if (obj === null) {
                return "edit: '${args[0]}' could not be found!"
            }
            if (obj.type !== VFSType.File) {
                return "edit: Only files can be loaded into the editor."
            }
            try {
                val txt: String = (obj as VFSFile).readText()
                js("codeMirror.setValue(txt);")
                Driver.openEditor()
                js("codeMirror.refresh();")
                Driver.activeFileinEditor = obj.getPath()
            } catch (e: Throwable) {
                return "edit: Could not load file to the editor!"
            }
            return ""
        },
        tab = fun(args: MutableList<String>, t: Terminal, sudo: Boolean): ArrayList<Any> {
            if (args.size == 1) {
                val prefix = args[args.size - 1]
                return arrayListOf(prefix, t.vfs.filesFromPrefix(prefix))
            }
            return arrayListOf("", ArrayList<String>())
        },
        help = "edit: Takes in one argument [filename] and will copy the contents to the editor tab and then go to the editor tab." +
                "\nUsage: edit [filename]"
)