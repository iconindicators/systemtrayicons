/** Possible exceptions which a Stardate may throw. */
public class StardateException extends Exception 
{
	private static final long serialVersionUID = 1L;


	/** Exception types. */
	public enum Type
	{
		INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_9999_INCLUSIVE,
		INTEGER_PART_MUST_BE_BETWEEN_ZERO_AND_99999_INCLUSIVE,
		INTEGER_PART_MUST_BE_LESS_THAN_5006,
		FRACTIONAL_PART_MUST_BE_GREATER_THAN_OR_EQUAL_TO_ZERO
	}
	

	/** * The type for this exception. */
	private Type m_type = null;
	
	
	/** * Constructs a new exception with null as its detail message. */
	public StardateException() { super(); }

	
	/**
	 * Constructs a new exception with the specified detail message.
	 * 
	 * @param message The detail message.
	 */
	public StardateException( String message ) { super( message ); }

	
	/**
	 * Constructs a new exception with the specified cause and a 
	 * detail message of (cause==null ? null : cause.toString()) 
	 * (which typically contains the class and detail message of cause).
	 * 
	 * @param cause The cause of this exception.
	 */
	public StardateException( Exception cause ) { super( cause ); }

	
	/**
	 * Constructs a new exception with the specified detail message and cause.
	 * 
	 * @param message The detail message.
	 * @param cause The cause of this exception.
	 */
	public StardateException( String message, Exception cause ) { super( message, cause ); }
	
	
	/**
	 * Constructs a new exception with the specified type.
	 * 
	 * @param type The type of this exception.
	 */
	public StardateException( Type type )
	{
		super();
		m_type = type;
	}
	
	
	/**
	 * Gets the type of this exception.
	 * 
	 * @return The type of this exception.
	 */
	public Type getType() { return m_type; }
}
