package commands

import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.Role
import com.stehno.photopile.service.UserDetailsServiceWithId
import org.crsh.cli.*
import org.crsh.command.InvocationContext
import org.crsh.text.ui.Overflow
import org.crsh.text.ui.UIBuilder

@Usage('User management operations.')
class user {

    private static final String BEAN_FACTORY = 'spring.beanfactory'

    @Command @Usage('List all configured users.')
    def list(InvocationContext context) {
        UserDetailsServiceWithId userService = findBean(context, UserDetailsServiceWithId)

        out.print new UIBuilder().table(separator: dashed, overflow: Overflow.HIDDEN, rightCellPadding: 1) {
            header(decoration: bold, foreground: green) {
                label('Id')
                label('Username')
                label('Display Name')
                label('Enabled')
                label('Account Expired')
                label('Credentials Expired')
                label('Account Locked')
                label('Role')
            }

            userService.listUsers().each { PhotopileUserDetails user ->
                row {
                    label(user.id)
                    label(user.username)
                    label(user.displayName ?: user.username)
                    label(user.enabled ? 'Y' : 'N')
                    label(user.accountExpired ? 'Y' : 'N')
                    label(user.credentialsExpired ? 'Y' : 'N')
                    label(user.accountLocked ? 'Y' : 'N')
                    label(user.authorities[0].authority)
                }
            }
        }
    }

    @Command @Usage('Add a new user.')
    def add(InvocationContext context,
            @Argument @Required String username,
            @Argument @Required String password,
            @Option(names = ['d', 'display-name']) String displayName,
            @Option(names = ['r', 'role']) Role role
    ) {
        UserDetailsServiceWithId userService = findBean(context, UserDetailsServiceWithId)
        userService.addUser(username, displayName ?: username, password, role)
        return "User ($username) added."
    }

    // FIXME: update (user update --enabled false --display-name bob --password asdfasdfasdf
    // unit tests for userauth repo, userdetails service and command

    @Command @Usage('Delete a user')
    def delete(InvocationContext context,
               @Option(names = ['u', 'username']) @Usage('Specify that delete is by username.') Boolean byUsername,
               @Argument @Usage('Identifier of the user (ID by default).') String identifier
    ) {
        UserDetailsServiceWithId userService = findBean(context, UserDetailsServiceWithId)
        if( userService.deleteUser(byUsername ? identifier : identifier as long) ){
            return "User ($identifier) deleted."
        } else {
            return "User ($identifier) NOT deleted."
        }
    }

    private findBean(InvocationContext context, Class type) {
        context.attributes[BEAN_FACTORY].getBean(type)
    }
}