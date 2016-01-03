package commands

import com.stehno.photopile.entity.PhotopileUserDetails
import com.stehno.photopile.entity.Role
import com.stehno.photopile.entity.UserAuthority
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

    @Command @Usage('Add a new user by id or username.')
    def add(InvocationContext context,
            @Argument @Required String username,
            @Argument @Required String password,
            @Option(names = ['d', 'display-name']) String displayName,
            @Option(names = ['r', 'role']) Role role
    ) {
        UserDetailsServiceWithId userService = findBean(context, UserDetailsServiceWithId)
        userService.addUser(username, displayName ?: username, password, role)

        out.println "User ($username) added.", yellow

        return list(context)
    }

    @Command @Usage('Edit information for a user.')
    def update(InvocationContext context,
               @Argument @Required String userid,
               @Option(names = ['u', 'username']) String username,
               @Option(names = ['p', 'password']) String password,
               @Option(names = ['d', 'display-name']) String displayName,
               @Option(names = ['enabled']) Boolean enabled,
               @Option(names = ['disabled']) Boolean disabled,
               @Option(names = ['r', 'role']) Role role
    ) {
        UserDetailsServiceWithId userService = findBean(context, UserDetailsServiceWithId)

        PhotopileUserDetails user = userService.loadUserById(userid as long)
        boolean encode = false

        if (username) {
            user.username = username
        }

        if (password) {
            user.password = password
            encode = true
        }

        if (displayName) {
            user.displayName = displayName
        }

        if (enabled != null) {
            user.enabled = true
        }

        if( disabled != null ){
            user.enabled = false
        }

        if (role) {
            user.authorities = [new UserAuthority(authority: role.name())]
        }

        PhotopileUserDetails updated = userService.updateUser(user, encode)

        out.println "Updated user (${updated.username}:${updated.id}).", yellow

        return list(context)
    }

    @Command @Usage('Delete a user')
    def delete(InvocationContext context,
               @Option(names = ['u', 'username']) @Usage('Specify that delete is by username.') Boolean byUsername,
               @Argument @Usage('Identifier of the user (ID by default).') String identifier
    ) {
        UserDetailsServiceWithId userService = findBean(context, UserDetailsServiceWithId)
        if (userService.deleteUser(byUsername ? identifier : identifier as long)) {
            out.println "User ($identifier) deleted.", yellow
        } else {
            out.println "User ($identifier) NOT deleted.", yellow
        }

        return list(context)
    }

    private findBean(InvocationContext context, Class type) {
        context.attributes[BEAN_FACTORY].getBean(type)
    }
}