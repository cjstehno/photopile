import com.stehno.photopile.importer.ActiveImportDao
import com.stehno.photopile.importer.domain.ActiveImport
import org.crsh.cli.Command
import org.crsh.cli.Usage
import org.crsh.command.CRaSHCommand

/**
 * Shell commands for active photo imports.
 */
@Usage('Manage active imports')
class imports extends CRaSHCommand {

    @Usage('List the active imports') @Command
    void list(){
        ActiveImportDao activeImportDao = activeImportDao()

        out << '--- Active Imports ---\n'

        def imports = activeImportDao.listImports()
        if( imports ){
            imports.each { ActiveImport imp->
                out << "$imp\n"
            }
        } else {
            out << 'None\n'
        }
    }

    /*
        scan()
        enqueue()
        stop()
        get()
        delete()
     */

    private ActiveImportDao activeImportDao(){
        context.attributes.beans['inMemoryActiveImportDao'] as ActiveImportDao
    }
}