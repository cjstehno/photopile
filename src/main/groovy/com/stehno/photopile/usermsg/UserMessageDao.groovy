package com.stehno.photopile.usermsg
import com.stehno.photopile.usermsg.domain.UserMessage

/**
 * DAO for managing UserMessage persistence.
 */
interface UserMessageDao {
    // FIXME: once I get a real user database use id rather then username

    /**
     * Persists the given information message.
     *
     * @param message the message to be saved
     */
    void save( UserMessage message )

    /**
     * Counts the number of messages for the given user.
     *
     * @param username user
     * @return a count of the messages for the user
     */
    long count( String username )

    /**
     * Counts the number of read or unread messages only, for the specified user.
     *
     * @param username the user
     * @param read whether to count read or unread messages
     * @return the number of read or unread messages
     */
    long count( String username, boolean read )

    /**
     * Marks the message with the given id as having been "read". Sets the read flag to true.
     *
     * If the message with the given id does not exist or does not belong to the specified user, no action
     * will be performed.
     *
     * @param username the username for the user who owns this message
     * @param id the id of the message to be marked as read.
     */
    void markRead( String username, long id )

    /**
     * Deletes the message with the given id for the specified user.
     *
     * If no message exists with the given id or it does not belong to the given user, no action will be performed.
     *
     * @param username the username for the user who owns this message.
     * @param id the message id
     */
    void delete( String username, long id )

    /**
     * Retrieves a list of all messages associated with the given user.
     *
     * @param username the user
     * @return the list of messages for the user
     */
    List<UserMessage> list( String username )

    /**
     * Retrieves a single message.
     *
     * If no message exists for the given id or it does not belong to the specified user, a DataAccessException will be
     * thrown.
     *
     * @param username the user
     * @param id the id of the message
     * @return the message, or null
     */
    UserMessage fetch( String username, long id )
}