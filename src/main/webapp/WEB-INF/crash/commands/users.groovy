/*
 * Copyright (c) 2013 Christopher J. Stehno
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.stehno.photopile.security.Role
import org.crsh.cli.Command
import org.crsh.cli.Option
import org.crsh.cli.Usage
import org.crsh.command.CRaSHCommand
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager

/**
 * Shell command for managing user configurations.
 */
@Usage('Manage users')
class users extends CRaSHCommand {

    @Usage('Get user') @Command
    void get( @Usage('Username') @Option(names=['u','username']) String username ){
        try {
            UserDetails userDetails = userDetailsManager().loadUserByUsername(username)

            out << " . Username   : ${userDetails.username}\n"
            out << " . Non-expired: ${userDetails.accountNonExpired}\n"
            out << " . Non-locked : ${userDetails.accountNonLocked}\n"
            out << " . Enabled    : ${userDetails.enabled}\n"
            out << " . Authorities: ${userDetails.authorities}\n"

        } catch( UsernameNotFoundException unfe ){
            out << "User ($username) does not exist.\n"
        }
    }

    // FIXME: should allow role configuration too

    @Usage('Add user') @Command
    void add(
        @Usage('Username') @Option(names=['u','username']) String username,
        @Usage('Password') @Option(names=['p','password']) String password
    ){
        userDetailsManager().createUser(new User(
            username,
            passwordEncoder().encode( password ),
            [ new SimpleGrantedAuthority(Role.USER.fullName()) ]
        ))

        out << "Added user ($username) with USER role.\n"
    }

    @Usage('Delete user') @Command
    void delete(
        @Usage('Username') @Option(names=['u','username']) String username
    ){
        userDetailsManager().deleteUser(username)

        out << "Deleted user ($username).\n"
    }

    private UserDetailsManager userDetailsManager(){
        context.attributes.beans['userDetailsManager'] as UserDetailsManager
    }

    private PasswordEncoder passwordEncoder(){
        context.attributes.beans['passwordEncoder'] as PasswordEncoder
    }
}