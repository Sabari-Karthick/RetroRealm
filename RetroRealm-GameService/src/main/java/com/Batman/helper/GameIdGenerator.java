package com.Batman.helper;

import java.io.Serial;
import java.time.Year;
import java.util.Properties;

import com.batman.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.spi.JavaTypeBasicAdaptor;
import org.hibernate.type.descriptor.jdbc.NumericJdbcType;
import org.hibernate.type.internal.NamedBasicTypeImpl;


/**
 * Custom ID generator for generating game IDs.
 * This class extends {@link SequenceStyleGenerator} to provide a custom ID generation strategy.
 * The generated ID will have the format "GAME{year}{sequence}", where {year} is the current year
 * and {sequence} is a 5-digit sequence number.
 *
 * <p>
 * This Class will be Used with the Custom Annotation {@link com.Batman.annotations.GameId}
 * This Class will be mentioned in the {@link org.hibernate.annotations.IdGeneratorType} Of that Annotation.
 * </p>
 *
 * <p>
 * Example: GAME202400001, GAME202400002, etc.
 * </p>
 *
 * <p>
 * This class is designed to be used with Hibernate`s ID generation mechanism.
 * </p>
 *
 * <p>
 * <b>Functionality:</b>
 * This class overrides the default sequence generation behavior of Hibernate to create game IDs with a specific format.
 * It combines a fixed prefix ("GAME"), the current year, and a 5-digit sequence number to form a unique game ID.
 * The sequence number is incremented for each new game ID generated.
 * </p>
 *
 * <p>
 * <b>Execution Flow:</b>
 * <ol>
 *     <li>
 *         <b>Configuration:</b> When Hibernate starts, it reads the configuration of the {@link com.Batman.annotations.GameId} annotation,
 *         which specifies this {@link GameIdGenerator} as the ID generator. The {@link #configure(Type, Properties, ServiceRegistry)} method is called.
 *         This method sets the increment size to 1 and configures the ID type as a long.
 *     </li>
 *     <li>
 *         <b>ID Generation:</b> When a new game entity is persisted, Hibernate calls the {@link #generate(SharedSessionContractImplementor, Object)} method to generate a new ID.
 *     </li>
 *     <li>
 *         <b>Sequence Generation:</b> The {@link #generate(SharedSessionContractImplementor, Object)} method first calls the superclass's {@link SequenceStyleGenerator#generate(SharedSessionContractImplementor, Object)} method to get the next sequence number.
 *     </li>
 *     <li>
 *         <b>ID Formatting:</b> The sequence number is then formatted into a 5-digit string with leading zeros (e.g., 00001, 00002).
 *         The current year is obtained using the {@link #getYear()} method.
 *         Finally, the prefix "GAME", the year, and the formatted sequence number are concatenated to create the final game ID.
 *     </li>
 *     <li>
 *         <b>Error Handling:</b> If any exception occurs during the ID generation process, an {@link InternalException} is thrown.
 *     </li>
 * </ol>
 * </p>
 *
 * @author SK
 */

@Slf4j
public class GameIdGenerator extends SequenceStyleGenerator {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final String PREFIX = "GAME";
    private static final String ZERO_COUNT = "%05d";
    private static final String INCREMENT_PARAM = "increment_size";
    private static final int DEFAULT_INCREMENT_SIZE = 1;

    /**
     * Generates a new game ID.
     * This method overrides the {@link SequenceStyleGenerator#generate(SharedSessionContractImplementor, Object)} method
     * to provide a custom ID generation strategy.
     *
     * <p>
     * <b>Execution:</b>
     * <ol>
     *     <li>
     *         Calls the superclass's {@link SequenceStyleGenerator#generate(SharedSessionContractImplementor, Object)} method to get the next sequence number.
     *     </li>
     *     <li>
     *         Formats the sequence number into a 5-digit string with leading zeros.
     *     </li>
     *     <li>
     *         Gets the current year using the {@link #getYear()} method.
     *     </li>
     *     <li>
     *         Concatenates the prefix "GAME", the year, and the formatted sequence number to create the final game ID.
     *     </li>
     *     <li>
     *         If any exception occurs during the ID generation process, an {@link InternalException} is thrown.
     *     </li>
     * </ol>
     * </p>
     *
     * @param session The session in which the ID is being generated.
     * @param object  The entity for which the ID is being generated.
     * @return The generated game ID.
     * @throws InternalException If any exception occurs during the ID generation process.
     */
    @Override
    public Object generate(SharedSessionContractImplementor session, Object object) {
        Object generatedNumber = super.generate(session, object);
        log.debug("Next Generated Sequence Number is: {} ", generatedNumber);
        try {
            return PREFIX + getYear() + String.format(ZERO_COUNT, (Long) generatedNumber);
        } catch (Exception e) {
            log.error("Error While Generating Game ID :: {}", ExceptionUtils.getStackTrace(e));
            throw new InternalException(e.getMessage());
        }
    }

    /**
     * Configures the ID generator.
     * This method overrides the {@link SequenceStyleGenerator#configure(Type, Properties, ServiceRegistry)} method
     * to provide custom configuration for the ID generator.
     *
     * <p>
     * <b>Execution:</b>
     * <ol>
     *     <li>
     *         Sets the increment size to {@link #DEFAULT_INCREMENT_SIZE} (which is 1) in the parameters.
     *     </li>
     *     <li>
     *         Configures the ID type as a long using {@link NamedBasicTypeImpl} and {@link NumericJdbcType}.
     *     </li>
     *     <li>
     *         Calls the superclass's {@link SequenceStyleGenerator#configure(Type, Properties, ServiceRegistry)} method to complete the configuration.
     *     </li>
     * </ol>
     * </p>
     *
     * @param type            The type of the ID.
     * @param parameters      The parameters for the ID generator.
     * @param serviceRegistry The service registry.
     * @throws MappingException If any mapping exception occurs during the configuration process.
     */
    @Override
    public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) throws MappingException {
        parameters.put(INCREMENT_PARAM, DEFAULT_INCREMENT_SIZE);
        Type idType = new NamedBasicTypeImpl<>(new JavaTypeBasicAdaptor<>(Long.class), NumericJdbcType.INSTANCE,
                "long");
        super.configure(idType, parameters, serviceRegistry);
    }

    /**
     * Gets the current year as a string.
     * This method is used to get the current year to be included in the game ID.
     *
     * <p>
     * <b>Execution:</b>
     * <ol>
     *     <li>
     *         Gets the current year using {@link Year#now()}.
     *     </li>
     *     <li>
     *         Gets the year value using {@link Year#getValue()}.
     *     </li>
     *     <li>
     *         Converts the year value to a string using {@link String#valueOf(int)}.
     *     </li>
     * </ol>
     * </p>
     *
     * @return The current year as a string.
     */

    public String getYear() {
        return String.valueOf(Year.now().getValue());
    }

}