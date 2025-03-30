package com.Batman.helper;

import java.time.Year;
import java.util.Properties;

import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.spi.JavaTypeBasicAdaptor;
import org.hibernate.type.descriptor.jdbc.NumericJdbcType;
import org.hibernate.type.internal.NamedBasicTypeImpl;

public class GameIdGenerator extends SequenceStyleGenerator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String PREFIX = "GAME";
	private static final String ZERO_COUNT = "%05d";

	public static final String INCREMENT_PARAM = "increment_size";

	public static final int DEFAULT_INCREMENT_SIZE = 1;

	@Override
	public Object generate(SharedSessionContractImplementor session, Object object) {
		return PREFIX + getYear() + String.format(ZERO_COUNT, super.generate(session, object));
	}

	@Override
	public void configure(Type type, Properties parameters, ServiceRegistry serviceRegistry) throws MappingException {
		parameters.put(INCREMENT_PARAM, DEFAULT_INCREMENT_SIZE);
		Type idType = new NamedBasicTypeImpl<>(new JavaTypeBasicAdaptor<>(Long.class), NumericJdbcType.INSTANCE,
				"long");
		super.configure(idType, parameters, serviceRegistry);
	}
	
	 public String getYear() {
	        return String.valueOf(Year.now().getValue());
	    }

}